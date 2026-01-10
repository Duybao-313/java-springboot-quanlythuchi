//package com.duybao.QUANLYCHITIEU.Config;
//
//import com.duybao.QUANLYCHITIEU.Exception.AppException;
//import com.duybao.QUANLYCHITIEU.Exception.ErrorCode;
//import com.duybao.QUANLYCHITIEU.Service.JwtService;
//import com.nimbusds.jose.JOSEException;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.security.oauth2.jwt.JwtException;
//import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;
//import java.nio.charset.StandardCharsets;
//import java.text.ParseException;
//import java.util.Objects;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class JwtDecoderConfig implements JwtDecoder {
//    @Value("${jwt.secret}")
//    private String SECRET_KEY ;
//
//    private final JwtService jwtService;
//    private NimbusJwtDecoder nimbusJwtDecoder=null;
//    @Override
//    public Jwt decode(String token) throws JwtException {
//
//        try {
//            if(!jwtService.VerifyToken(token)){
//            throw new AppException(ErrorCode.TOKEN_INVALID);
//            }
//            if(Objects.isNull(nimbusJwtDecoder)){
//                SecretKey secretKey= new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8),"HS512");
//                nimbusJwtDecoder= NimbusJwtDecoder
//                        .withSecretKey(secretKey)
//                        .macAlgorithm(MacAlgorithm.HS512)
//                        .build();
//            }
//        } catch (ParseException | JOSEException e) {
//            throw new AppException(ErrorCode.INTERNAL_ERROR);
//        }
//        return nimbusJwtDecoder.decode(token);
//    }
//}
