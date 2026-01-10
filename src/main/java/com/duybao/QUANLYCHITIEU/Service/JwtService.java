package com.duybao.QUANLYCHITIEU.Service;

import com.duybao.QUANLYCHITIEU.Model.User;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;

import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Date;
@Slf4j
@Service
public class JwtService {

@Value("${jwt.secret}")
    private String SECRET_KEY ;


    public String generateToken(User user) {
        log.info(String.valueOf(SECRET_KEY.getBytes().length));
        JWSHeader jwsHeader=new JWSHeader(JWSAlgorithm.HS512);
        long EXPIRATION_TIME = 1000 * 60 * 60 * 24;
        JWTClaimsSet jwtClaimsSet=new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .claim("role", user.getRole().getName())
                .claim("userid", user.getId())
                .issueTime(new Date())
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
    public Boolean VerifyToken(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT=SignedJWT.parse(token);

        Date expiration=signedJWT.getJWTClaimsSet().getExpirationTime();
        if(expiration.before(new Date()) ) {
            return false;
        }

        return signedJWT.verify(new MACVerifier(SECRET_KEY.getBytes(StandardCharsets.UTF_8)));
    }

}
