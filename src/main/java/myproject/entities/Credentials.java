package myproject.entities;

import lombok.Data;

@Data
public class Credentials {
    private String login;
    private String password;

    public Credentials() {}

    public Credentials(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
