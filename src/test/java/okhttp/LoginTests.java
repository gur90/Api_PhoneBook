package okhttp;

import com.google.gson.Gson;
import dto.AuthRequestDto;
import dto.AuthRespinseDto;
import dto.ErrorDto;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class LoginTests {
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    @Test
    public void loginSuccessTest() throws IOException {
        AuthRequestDto auth= AuthRequestDto.builder()
                .username("annettgur+1@rambler.ru")
                .password("722063gurina!A_")
                .build();
        RequestBody body= RequestBody.create(gson.toJson(auth),JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/login/usernamepassword")
                .post(body).build();
        final Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(), 200);
        AuthRespinseDto responseDto= gson.fromJson(response.body().string(), AuthRespinseDto.class);
        System.out.println(responseDto.getToken());
        //eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiYW5uZXR0Z3VyKzFAcmFtYmxlci5ydSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjgyNTI0ODM4LCJpYXQiOjE2ODE5MjQ4Mzh9.ayBWuG-7ojoPfnquDBTm_QBtX6-yaA3oNOgC4fXaSgo
    }
    @Test
    public void loginWithWrongEmailTest() throws IOException {
        AuthRequestDto auth= AuthRequestDto.builder()
                .username("annettgur+1rambler.ru")
                .password("722063gurina!A_")
                .build();
        RequestBody body= RequestBody.create(gson.toJson(auth),JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/login/usernamepassword")
                .post(body).build();
         Response response = client.newCall(request).execute();
       Assert.assertFalse(response.isSuccessful());
       Assert.assertEquals(response.code(),  401);
        ErrorDto errorDto= gson.fromJson(response.body().string(), ErrorDto.class);

        Assert.assertEquals(errorDto.getError(),"Unauthorized");
        Assert.assertEquals(errorDto.getMessage(),"Login or Password incorrect");

    }
}
