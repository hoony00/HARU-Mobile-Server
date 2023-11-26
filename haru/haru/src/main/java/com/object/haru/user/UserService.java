package com.object.haru.user;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.object.haru.exception.BaseException;
import com.object.haru.fcm.FirebaseCloudMessageService;
import com.object.haru.fcm.dto.FcmTokenDTO;
import com.object.haru.user.dto.KakaoDTO;
import com.object.haru.user.dto.UserChangeDTO;
import com.object.haru.user.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 *    유저관련 기능을 담당하는 Service
 *
 *   @version          1.00    2023.02.01
 *   @author           한승완
 */

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    private final FirebaseCloudMessageService fcmService;

    // 카카오 토큰 받는 기능 (2023-02-02 한승완)
    public String GetKaKaoAccessToken(String code){
        String access_Token=""; // 초기화
        String refresh_Token =""; // 초기화
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try{
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("secret");
            sb.append("secret); //TODO REST_API_KEY
            sb.append("secret"); //TODO 인가코드 받은 redirect_uri
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            //200 = 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);
            //JSON 파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

            System.out.println("access_token : " + access_Token);
            System.out.println("refresh_token : " + refresh_Token);

            br.close();
            bw.close();

        }catch (IOException e) {
            e.printStackTrace();
        }

        return access_Token;
    }

    // 카카오로 로그인해서 사용자 정보 반환 받는 기능 (2023-02-02 한승완)
    public KakaoDTO GetkakaoProfile(String token) {
        String reqURL = "secret";

        //access_token 이용하여 사용자 정보 조회
        int kakaoid = 0;
        String nickname = null;
        String photo = null;


        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token);
            //200 = 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";
            // String image = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            kakaoid = element.getAsJsonObject().get("id").getAsInt();
            nickname = element.getAsJsonObject().get("properties").getAsJsonObject().get("nickname").getAsString();
            photo = element.getAsJsonObject().get("properties").getAsJsonObject().get("profile_image").getAsString();

            System.out.println("id : " + kakaoid);
            System.out.println("nickname : " + nickname);

            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        KakaoDTO kakaoDTO = new KakaoDTO(Integer.toUnsignedLong(kakaoid),nickname,photo);
        return  kakaoDTO;
    }

    //카카오 로그인으로 회원가입 (2023-02-07 한승완)
    @Transactional
    public UserEntity SaveKakaoUser(KakaoDTO kakaoDTO){
        return userRepository.save(UserEntity.builder()
                .kakaoid(kakaoDTO.getKakaoid()).name(kakaoDTO.getName()).userrole("USER")
                        .photo(kakaoDTO.getPhoto())
                .password(passwordEncoder.encode(kakaoDTO.getKakaoid().toString())).build());
                // + 썸네일 이미지 url 들어갈 예정
    }

    //회원가입 당시 아이디가 DB에 존재하는지 검증 (2023-02-02 한승완)
    public String Optionalfindkakaoid(KakaoDTO kakaoDTO, String fcmtoken) throws BaseException, FirebaseAuthException {

        Optional<UserEntity> user = userRepository.FindByKakaoId(kakaoDTO.getKakaoid());

        if(user.isEmpty()){
            UserEntity user2 = SaveKakaoUser(kakaoDTO);
            fcmService.overWriteToken(new FcmTokenDTO(fcmtoken,kakaoDTO.getKakaoid()));
            //firebase에도 사용자 추가
         //   String fireabaseUid = firebaseAuth(kakaoDTO);
        //    System.out.println(" #### firebase uid : " + fireabaseUid);

            return jwtTokenProvider.createToken(Long.toString(user2.getKakaoid()));
        }else {
            return jwtTokenProvider.createToken(Long.toString(user.get().getKakaoid()));
        }
    }




    //카카오 아이디로 uid를 가져오기 (2023-02-02 한승완)
    public Long findkakaoid(Long kakaoid){
        return userRepository.findByKakaoid(kakaoid).getUid();
    }

    //유저 정보 수정 (2023-02-09 한승완)
    public void ChangeUserInfo(UserChangeDTO userChangeDTO) {
        UserEntity user = userRepository.findByKakaoid(userChangeDTO.getKakaoid());
        if (user != null) {
            user.setAge(userChangeDTO.getAge());
            user.setName(userChangeDTO.getName());
            user.setCareer(userChangeDTO.getCareer());
            user.setSex(userChangeDTO.getSex());
            user.setPhoto(userChangeDTO.getPhoto());
        }
    }

    public void RemoveUserInfo(Long kakaoid){
        Optional<UserEntity> user = userRepository.FindByKakaoId(kakaoid);
        if(user.isPresent()) {
            user.get().setPhoto(null);
            user.get().setName("DeletedUser" + (int) (Math.random() * 9999));
            user.get().setAge(null);
            user.get().setSex(null);
            user.get().setCareer(null);
            user.get();
            user.get();
            user.get();
        }

    }


    public UserEntity SelectUserInfo(Long kakaoid){
        UserEntity user = userRepository.findByKakaoid(kakaoid);
            return user;
    }

}
