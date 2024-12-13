package myproject.services;

import io.jsonwebtoken.Jwts;

import java.util.ArrayList;
import java.util.Date;

public class JwtService {

    private static ArrayList<String> validTokens = new ArrayList<>();
    private static final long TOKENLIFETIME = 1000*3600;

    public static String generateToken(String user) {

        String token =  Jwts.builder()
                .setSubject(user)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKENLIFETIME))
                .compact();

        validTokens.add(token);
        return token;
    }

    public static void invalidateToken(String token) {
        validTokens.remove(token);
    }

    public static boolean tokenValid(String token) {
        return validTokens.contains(token);
    }
}