//package connectripbe.connectrip_be.auth.config;
//
//
//import connectripbe.connectrip_be.global.exception.GlobalException;
//import connectripbe.connectrip_be.global.exception.type.ErrorCode;
//import java.util.Objects;
//import org.springframework.core.MethodParameter;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.support.WebDataBinderFactory;
//import org.springframework.web.context.request.NativeWebRequest;
//import org.springframework.web.method.support.HandlerMethodArgumentResolver;
//import org.springframework.web.method.support.ModelAndViewContainer;
//
///**
// * 컨트롤러 메소드에서 로그인 사용자 정보를 파라미터로 받을 수 있도록 지원
// */
//@Component
//public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
//
//
//      /**
//       * 파라미터가 @LoginUser 어노테이션을 가지고 있고, String 타입인 경우에만 true를 반환.
//       */
//      @Override
//      public boolean supportsParameter(MethodParameter parameter) {
//            boolean isLoginUserAnnotation = parameter.hasParameterAnnotation(LoginUser.class);
//            boolean isUserClass = String.class.equals(parameter.getParameterType());
//            return isLoginUserAnnotation && isUserClass;
//      }
//
//      @Override
//      public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
//              NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//            if(Objects.isNull(authentication)){
//                  throw new GlobalException(ErrorCode.UNKNOWN_ERROR);
//            }
//            return authentication.getName();
//      }
//}
//
