package com.object.haru.user;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.object.haru.user.dto.KakaoDTO;
import com.object.haru.user.dto.UserChangeDTO;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;


/**
 *    유저 + 토큰 기능을 담당하는 Controller
 *
 *   @version          1.00    2023.02.01
 *   @author           한승완
 */

@RestController
@RequestMapping(value = "/kakao")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @ApiOperation(
            value = "카카오 회원가입 API => 카카오 로그인 code 받아서 전송하면됨 "
            , notes = "key" //(2023-02-07 한승완)
    @ResponseBody
    @GetMapping(value = "/oauth")
    public String KakaoCreate(@RequestParam String acccesstoken,
                              @RequestParam String fcmtoken) throws Exception{

        // 접근 링크 : https://kauth.kakao.com/oauth/authorize?client_id=a91d97f1d08c184bf4385f5553d57b6a&redirect_uri=http://localhost:8080/kakao/oauth&response_type=code
        KakaoDTO kakaoDTO = userService.GetkakaoProfile(acccesstoken);
        String jwtToken = userService.Optionalfindkakaoid(kakaoDTO,fcmtoken);


        if(jwtToken == null){
            HttpHeaders headers = new HttpHeaders();
            return "토큰 생성 실패";
        }else{
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "X-AUTH-TOKEN"+ jwtToken);
            JsonObject jwt = new JsonObject();
            jwt.addProperty("acccesstoken",jwtToken);
            Gson gson = new Gson();
            String jsonStr = gson.toJson(jwt);
            return jsonStr;
            //현재는 파일의 헤더 + 바디 모두다 토큰을 전송하지만 이후에는 헤더에만 전송할 예정
        }
    }



    @ApiOperation(
            value = "사용자 정보 변경 API"
            , notes = "사용자가 변경하고싶은 사항에 대한 변경")  //(2023-02-07 한승완)
    @PutMapping("/change")
    public void ChangeUserInfo(UserChangeDTO userChangeDTO){
        userService.ChangeUserInfo(userChangeDTO);
    }


    @ApiOperation(
            value = "사용자 삭제 API"
            , notes = "사용자의 kakaoid / 어플에서 헤더 지워주기!!! ")  //(2023-02-13 한승완)
    @GetMapping("/delete")
    public void DeleteUserInfo(Long kakaoid){
        userService.RemoveUserInfo(kakaoid);
    }

    @ApiOperation(
            value = "로그인 후 세션에서 사용할 사용자 정보 가져오는 API"
            , notes = "사용자의 kakaoid" )  //(2023-02-21 한승완)
    @GetMapping("/select/user")
    public UserResponseDTO SelectUserInfo(Long kakaoid){
        UserEntity user = userService.SelectUserInfo(kakaoid);
        UserResponseDTO userResponseDTO = new UserResponseDTO(
                user.getUid(),user.getKakaoid(),user.getName(),user.getSex(),user.getAge(), user.getCareer(),
                user.getPhoto());
        return userResponseDTO;
    }

    @Data
    @AllArgsConstructor //(2023-02-21 한승완)
    static class UserResponseDTO{
        private Long uid;
        private Long kakaoid;
        private String name;
        private String sex;
        private String age;
        private String career;
        private String photo;
    }
}
