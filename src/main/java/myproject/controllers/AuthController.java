package myproject.controllers;

import myproject.entities.Credentials;
import myproject.entities.Error;
import myproject.entities.Login;
import myproject.services.AuthService;
import myproject.services.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody Credentials credentials) {

        if (!authService.credentialsCorrect(credentials.getLogin(),credentials.getPassword())) {
            return new ResponseEntity<>(new Error("Bad credentials",400), HttpStatus.BAD_REQUEST);
        }

        String token = JwtService.generateToken(credentials.getLogin());
        Login login = new Login(token);
        String json = login.toString();
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout(@RequestHeader("auth-token") String token) {
        if (!JwtService.tokenValid(token)) {
            return new ResponseEntity<>(new Error("Incorrect token",401), HttpStatus.UNAUTHORIZED);
        }

        JwtService.invalidateToken(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}