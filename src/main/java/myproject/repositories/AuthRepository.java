package myproject.repositories;

import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class AuthRepository {

    private HashMap<String, String> usersWithPasswords;

    public AuthRepository() {
        usersWithPasswords = new HashMap<>();

        usersWithPasswords.put("user","password");
        usersWithPasswords.put("user2","password2");
        usersWithPasswords.put("user3","password3");
    }

    public boolean credentialsCorrect(String username, String password) {
        return usersWithPasswords.containsKey(username) && usersWithPasswords.get(username).equals(password);
    }
}
















