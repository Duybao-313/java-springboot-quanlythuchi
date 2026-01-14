package com.duybao.QUANLYCHITIEU.Service;

import com.duybao.QUANLYCHITIEU.DTO.Response.ApiResponse;
import com.duybao.QUANLYCHITIEU.DTO.Response.TokenResponse;
import com.duybao.QUANLYCHITIEU.Exception.AppException;
import com.duybao.QUANLYCHITIEU.Exception.ErrorCode;
import com.duybao.QUANLYCHITIEU.Model.User;
import com.duybao.QUANLYCHITIEU.Repository.InvalidatedTokenRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;

import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

@Value("${jwt.secret}")
    protected String SECRET_KEY ;

    private final InvalidatedTokenRepository invalidatedTokenRepository;
    public String generateToken(User user) {
        JWSHeader jwsHeader=new JWSHeader(JWSAlgorithm.HS512);
        long EXPIRATION_TIME = 1000 * 60 * 60 ;
        JWTClaimsSet jwtClaimsSet=new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .claim("role", user.getRole().getName())
                .claim("userid", user.getId())
                .issueTime(new Date())
                .jwtID(UUID.randomUUID().toString())
                .expirationTime(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .build();
        Payload payload=new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject=new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(SECRET_KEY.getBytes(StandardCharsets.UTF_8)));
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        return jwsObject.serialize();

    }

    public TokenResponse introspect(String token) throws ParseException, JOSEException {
        boolean valid=true;
        try {
            VerifyToken(token);
        } catch (AppException e){
            valid=false;
        }

        return TokenResponse.builder()
                .valid(valid)
                .build();
    };
    public SignedJWT VerifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier=new MACVerifier(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        SignedJWT signedJWT=SignedJWT.parse(token);
        Date expiration=signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified=signedJWT.verify(verifier);

        if (!(verified && expiration.after(new Date()))){
            throw new AppException(ErrorCode.TOKEN_INVALID);
        }
        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())){
            throw new AppException(ErrorCode.TOKEN_INVALID);
        }
        return signedJWT;
    }

}
