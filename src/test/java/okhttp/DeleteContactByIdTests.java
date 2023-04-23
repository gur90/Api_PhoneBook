package okhttp;

import com.google.gson.Gson;
import dto.ContactDto;
import dto.ContactResponseDto;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Random;

public class DeleteContactByIdTests {
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiYW5uZXR0Z3VyKzFAcmFtYmxlci5ydSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjgyNTI0ODM4LCJpYXQiOjE2ODE5MjQ4Mzh9.ayBWuG-7ojoPfnquDBTm_QBtX6-yaA3oNOgC4fXaSgo";
    String id;


    @BeforeMethod
    public void precondition() throws IOException {
        int i = new Random().nextInt(1000) + 1000;
        ContactDto contactDto = ContactDto.builder()
                .name("Oli")
                .lastName("Voor")
                .email("kan" + i + "@gmail.co")
                .phone("1234567890" + i)
                .address("Keln")
                .description("dert")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(contactDto), JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization", token)
                .post(body)
                .build();

        final Response response = client.newCall(request).execute();

        ContactResponseDto resDto = gson.fromJson(response.body().string(), ContactResponseDto.class);
        System.out.println(resDto.getMessage());
        String message = resDto.getMessage();
        String[] split = message.split(": ");
        id = split[1];

    }

    @Test
    public void deleteContactByIdSuccessTest() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/" + id)
                .addHeader("Authorization", token)
                .delete().build();
        final Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(),200);
        ContactResponseDto resDto= gson.fromJson(response.body().string(), ContactResponseDto.class);
        Assert.assertEquals(resDto.getMessage(), "Contact was deleted!");
    }
}
