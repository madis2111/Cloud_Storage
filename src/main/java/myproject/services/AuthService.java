package myproject.services;

import myproject.repositories.AuthRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private AuthRepository authRepository;

    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public boolean credentialsCorrect(String username, String password) {

        return authRepository.credentialsCorrect(username, password);
    }
}