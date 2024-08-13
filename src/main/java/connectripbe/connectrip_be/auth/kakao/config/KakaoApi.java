package connectripbe.connectrip_be.auth.kakao.config;


import connectripbe.connectrip_be.auth.kakao.dto.api.KakaoTokenApiResponse;
import connectripbe.connectrip_be.auth.kakao.dto.api.KakaoUserInfoApiResponse;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;


@HttpExchange
public interface KakaoApi {

  @PostExchange("https://kauth.kakao.com/oauth/token")
  KakaoTokenApiResponse getToken(@RequestParam("grant_type") String grant_type,
      @RequestParam("client_id") String client_id,
      @RequestParam("redirect_uri") String redirect_uri,
      @RequestParam("code") String code);

  @GetExchange("https://kapi.kakao.com/v2/user/me")
  KakaoUserInfoApiResponse getUserInfo(@RequestHeader("Authorization") String property);

}
