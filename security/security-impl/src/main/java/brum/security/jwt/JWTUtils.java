package brum.security.jwt;

import brum.model.dto.users.UserPrincipal;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Builder;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class JWTUtils {
    public static final String SECRET = "hw@6h)oR6pf}5<t=@-Oi6cWnx*(AHWHx3X_0@)Gc<cIh%r{b?`%_qm;%pcl?'(R%n";
    private static final Logger LOG = LoggerFactory.getLogger(JWTUtils.class);
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String FOR_PATCH_PASSWORD_CLAIM = "forPatchPassword";
    public static final String ROLE_CLAIM = "role";
    public static final String STATUS_CLAIM = "status";
    public static final String REFRESH_EXPIRATION_DATE_CLAIM = "refreshExpirationDate";
    public static final String DEPARTMENT = "department";

    private JWTUtils() {
    }

    public static String generateToken(UserPrincipal user, boolean forPatchPassword, Long expirationMinutes, Long refreshExpirationMinutes) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationMinutes * 60 * 1000))
                .withClaim(ROLE_CLAIM, user.getRole().getName().name())
                .withClaim(STATUS_CLAIM, user.getStatus().name())
                .withClaim(FOR_PATCH_PASSWORD_CLAIM, forPatchPassword)
                .withClaim(REFRESH_EXPIRATION_DATE_CLAIM, new Date(System.currentTimeMillis() + (expirationMinutes + refreshExpirationMinutes) * 60 * 1000))
                .withClaim(DEPARTMENT, user.getDepartment())
                .sign(HMAC512(SECRET.getBytes()));
    }

    public static JWTData getUserDataFromToken(String token) {
        try {
            DecodedJWT decoded = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, ""));
            String username = decoded.getSubject();
            Boolean forPatchPassword = decoded.getClaim(FOR_PATCH_PASSWORD_CLAIM).asBoolean();
            Date refreshExpirationDate = decoded.getClaim(REFRESH_EXPIRATION_DATE_CLAIM).asDate();
            return JWTData.builder()
                    .username(username)
                    .forPatchPassword(forPatchPassword)
                    .refreshExpirationDate(refreshExpirationDate).build();
        } catch(TokenExpiredException e) {
            return parseForRefresh(token);
        } catch (JWTVerificationException e) {
            LOG.error("Error while verifying token: {}", e.getMessage());
            return null;
        }
    }

    private static JWTData parseForRefresh(String token) {
        try {
            Long expirationTime = JWT.decode(token.replace(TOKEN_PREFIX, "")).getExpiresAt().getTime() / 1000;
            Claim refreshExpirationClaim = JWT.decode(token.replace(TOKEN_PREFIX, "")).getClaim(REFRESH_EXPIRATION_DATE_CLAIM);
            if (refreshExpirationClaim.isNull()) {
                return null;
            }

            DecodedJWT decoded = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                    .acceptExpiresAt(refreshExpirationClaim.asLong() - expirationTime)
                    .withClaim(FOR_PATCH_PASSWORD_CLAIM, false)
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, ""));
            String username = decoded.getSubject();
            Date refreshExpirationDate = decoded.getClaim(REFRESH_EXPIRATION_DATE_CLAIM).asDate();
            Boolean forPatchPassword = decoded.getClaim(FOR_PATCH_PASSWORD_CLAIM).asBoolean();
            return JWTData.builder()
                    .username(username)
                    .forPatchPassword(forPatchPassword)
                    .forRefresh(true)
                    .refreshExpirationDate(refreshExpirationDate).build();
        } catch (JWTVerificationException e) {
            LOG.error("Error while verifying token: {}", e.getMessage());
            return null;
        }
    }

    @Data
    @Builder
    public static class JWTData {
        private String username;
        private Boolean forPatchPassword;
        private Boolean forRefresh;
        private Date refreshExpirationDate;
    }

}
