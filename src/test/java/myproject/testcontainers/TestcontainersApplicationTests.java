package myproject.testcontainers;

import com.google.gson.Gson;
import myproject.entities.Credentials;
import myproject.entities.Login;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.GenericContainer;

public class TestcontainersApplicationTests {

    private final GenericContainer<?> cloudApp = new GenericContainer<>("cloudimage:latest")
            .withExposedPorts(8080);
    @BeforeEach
    public void setUp() {
        cloudApp.start();
    }

    @AfterEach
    public void tearDown() {
        cloudApp.stop();
    }


    @Test
    public void loginTest() {
        RestTemplate restTemplate = new RestTemplate();
        Gson gson = new Gson();

        Credentials credentials = new Credentials("user","password");
        HttpEntity<String> bodyWithCredentialsHttpEntity = new HttpEntity<>(gson.toJson(credentials));

        ResponseEntity<?> postLoginEntity =
                restTemplate.postForEntity("http://localhost:8080/login", bodyWithCredentialsHttpEntity, Login.class);
        Assertions.assertEquals(HttpStatus.OK, postLoginEntity.getStatusCode());
    }

    @Test
    public void deleteForEntityTest() {
        RestTemplate restTemplate = new RestTemplate();
        Gson gson = new Gson();

        Credentials credentials = new Credentials("user","password");
        HttpEntity<String> bodyWithCredentialsHttpEntity = new HttpEntity<>(gson.toJson(credentials));

        ResponseEntity<?> postLoginEntity =
                restTemplate.postForEntity("http://localhost:8080/login?login=myuser&password=mypass", bodyWithCredentialsHttpEntity, Login.class);
        Assertions.assertEquals(HttpStatus.OK, postLoginEntity.getStatusCode());
        Login login = (Login)postLoginEntity.getBody();


        HttpHeaders headersMap2 = new HttpHeaders();
        headersMap2.set("auth-token", login.getAuthToken());

        ResponseEntity<?> postFileEntity =
                restTemplate.exchange("http://localhost:8080/file?filename=myfile", HttpMethod.DELETE, null, Void.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, postFileEntity.getStatusCode());
    }
}