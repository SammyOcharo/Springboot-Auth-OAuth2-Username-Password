package com.samdev.customizedOauth20.ServiceImpl;

import com.samdev.customizedOauth20.Entity.User;
import com.samdev.customizedOauth20.Repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JWTService {

    public final String secret = "wertyuioiuytrewertyuiuytrewertyuiuytrertyuiuytre";
    private static final Logger logger = LoggerFactory.getLogger(JWTService.class);

    private final UserRepository userRepository;

    public JWTService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public String GenerateToken(User user){

         return Jwts
                 .builder()
                 .subject(user.getUsername())
                 .issuedAt(new Date(System.currentTimeMillis()))
                 .expiration(new Date(System.currentTimeMillis() + (60*60*1000)))
                 .signWith(SignWithKey())
                 .compact();
     }

    private SecretKey SignWithKey() {
         byte[] keyBytes = Decoders.BASE64.decode(secret);
         return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token){
         return Jwts.parser()
                 .verifyWith(SignWithKey())
                 .build()
                 .parseSignedClaims(token)
                 .getPayload();
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver){

         Claims claims = extractAllClaims(token);
         return resolver.apply(claims);
    }

    public String extractUsername(String token){
         return  extractClaim(token, Claims::getSubject);
    }

    public boolean isValid(String token, User user){
         String username = extractUsername(token);
        logger.info("Here is the username before token validation, {}", username);
        logger.info("Here is the username before token validation, {}", user.getUsername());
        logger.info("Here is the token check if its expired, {}", isTokenExpired(token));
         return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        logger.info("Here is the token check if its on isTokenExpired, {}", extractExpiration(token));
        logger.info("Here is the time currently, {}", new Date());
         return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
         return extractClaim(token, Claims::getExpiration);
    }

    public User createUser(String username, String email) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setRole("USER");
        user.setIsActive(1);
        return userRepository.save(user);
    }

}
