package br.ufscar.dc.dsw1.debatr.helper;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Properties;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.jboss.jandex.Main;

import br.ufscar.dc.dsw1.debatr.domain.User;

public class JwtHelper {

    private static Algorithm getAlgorithm() {
        String secret = "";
        Properties properties = new Properties();
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("application.properties");

        try {
            properties.load(inputStream);
            secret = properties.getProperty("secret");
        } catch (IOException ignored) {}

        return Algorithm.HMAC256(secret);
    }

    public static String generateJWTToken(User user, String intent) {
        String token = null;
        try {
            Algorithm algorithm = JwtHelper.getAlgorithm();
            token = JWT.create()
                    .withIssuer("debatr")
                    .withExpiresAt(Date.from(Instant.now().plus(300, ChronoUnit.SECONDS)))
                    .withClaim("username", user.getUsername())
                    .withClaim("intent", intent)
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            e.printStackTrace();
        }

        return token;
    }

    public static DecodedJWT getDecodedJWT(String token) {
        try {
            Algorithm algorithm = JwtHelper.getAlgorithm();
            JWTVerifier verifier = JWT.require(algorithm).withIssuer("debatr").build();

            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            e.printStackTrace();
        }

        return null;
    }

//    public static void changePasswordForTokenOwner(String token, String plaintextPassword) {
//        DecodedJWT jwt = JwtHelper.getDecodedJWT(token);
//
//        if (jwt != null) {
//            Claim userIdClaim = jwt.getClaim("username");
//            final String username = userIdClaim.asString();
//
//            dao.changeUserPassword(username, plaintextPassword);
//        }
//    }

    /**
     * Indica se o usuário que informou o token pode ou não ter seu email verificado.
     * @param token O token JWT informado pelo usuário.
     * @param username O nome de usuário da entidade que desejamos verificar.
     * @return [true], se o usuário estiver apto a ter seu email autorizado. [false], caso contrário.
     */
    public static boolean verifyToken(String token, String username) {
        DecodedJWT jwt = JwtHelper.getDecodedJWT(token);

        if (jwt != null) {
            Claim usernameClaim = jwt.getClaim("username");
            Claim intentClaim = jwt.getClaim("intent");

            final String tokenUsername = usernameClaim.asString();
            final String intent = intentClaim.asString();

            return tokenUsername.equals(username) && intent.equals("verify");
        }

        return false;
    }

    /**
     * Returns true if the token is not expired.
     */
    public static boolean isTokenValid(String token) {
        DecodedJWT jwt = JwtHelper.getDecodedJWT(token);

        if (jwt != null) {
            return !jwt.getExpiresAt().before(new Date());
        }

        return false;
    }
}


