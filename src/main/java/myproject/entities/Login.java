package myproject.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Login {
    private String authToken;

    @Override
    public String toString() {
        return "{\"auth-token\" : \"" + authToken + "\" }";
    }
}
