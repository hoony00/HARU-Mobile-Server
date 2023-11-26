package com.object.haru;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.object.haru.exception.BaseException;
import com.object.haru.user.UserService;
import com.object.haru.user.dto.KakaoDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/test")
public class TestController {

    private final UserService userService;

    public TestController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/get")
    public String TestCont(@RequestParam(value = "id")String id){
        System.out.println(id);
        return id;
    }

    @GetMapping("/login")
    public String TestLogin() throws BaseException, FirebaseAuthException {
        KakaoDTO kakaoDTO = new KakaoDTO(123456789L,"테스트ID",null);
        String jwtToken = userService.Optionalfindkakaoid(kakaoDTO,null);

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



}
