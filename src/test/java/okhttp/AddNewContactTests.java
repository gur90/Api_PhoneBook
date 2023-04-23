package okhttp;

import com.google.gson.Gson;
import dto.ContactDto;
import dto.ContactResponseDto;
import dto.ErrorDto;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Random;

public class AddNewContactTests {
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiYW5uZXR0Z3VyKzFAcmFtYmxlci5ydSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjgyNTI0ODM4LCJpYXQiOjE2ODE5MjQ4Mzh9.ayBWuG-7ojoPfnquDBTm_QBtX6-yaA3oNOgC4fXaSgo";
    @Test
    public void addNewContactTests() throws IOException {
        int i=new Random().nextInt(1000)+1000;
        ContactDto contactDto= ContactDto.builder()
                .name("Oli")
                .lastName("Voor")
                .email("kan"+i+"@gmail.co")
                .phone("1234567890"+i)
                .address("Keln")
                .description("dert")
                .build();
        RequestBody body= RequestBody.create(gson.toJson(contactDto), JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization", token)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(),200);
        ContactResponseDto resDto= gson.fromJson(response.body().string(), ContactResponseDto.class);
        System.out.println(resDto.getMessage());

    }
    @Test
    public void addNewContact1Tests() throws IOException {

        ContactDto contactDto= ContactDto.builder()
                .name("Ann")
                .lastName("Voor")
                .email("kana@gmail.co")
                .phone("12345678901")
                .address("Keln")
                .description("dert")
                .build();
        RequestBody body= RequestBody.create(gson.toJson(contactDto), JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization", token)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(),200);
        ContactResponseDto resDto= gson.fromJson(response.body().string(), ContactResponseDto.class);
        System.out.println(resDto.getMessage());

    }
    @Test
    public void addNewContactNegativeTests() throws IOException {
        int i=new Random().nextInt(1000)+1000;
        ContactDto contactDto= ContactDto.builder()
                .name("")
                .lastName("Voor")
                .email("kan"+i+"@gmail.co")
                .phone("12345678")
                .address("Keln")
                .description("dert")
                .build();
        RequestBody body= RequestBody.create(gson.toJson(contactDto), JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization", token)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),  400);
        ErrorDto errorDto= gson.fromJson(response.body().string(), ErrorDto.class);
        Assert.assertEquals(errorDto.getError(),"Bad Request");
        System.out.println(errorDto.getMessage());
    }

    @Test
    public void addNewContactUnauthUserNegativeTest() throws IOException {
        int i=new Random().nextInt(1000)+1000;
        ContactDto contactDto= ContactDto.builder()
                .name("")
                .lastName("Voor")
                .email("kan"+i+"@gmail.co")
                .phone("1234567890"+i)
                .address("Keln")
                .description("dert")
                .build();
        RequestBody body= RequestBody.create(gson.toJson(contactDto), JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization", "fdhgjbbvvcgcvc")
                .post(body)
                .build();


        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 401);

        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        Assert.assertEquals(errorDto.getError(),"Unauthorized");
    }
    @Test
    public void addNewContactDublicateNegativeTest() throws IOException {
        ContactDto contactDto= ContactDto.builder()
                .name("Ann")
                .lastName("Voor")
                .email("kana@gmail.co")
                .phone("12345678901")
                .address("Keln")
                .description("dert")
                .build();
        RequestBody body= RequestBody.create(gson.toJson(contactDto), JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization", token)
                .post(body)
                .build();


        Response response = client.newCall(request).execute();
       //Assert.assertFalse(response.isSuccessful());
        //Assert.assertEquals(response.code(), 409);

        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        System.out.println(errorDto.getMessage());
        //Assert.assertEquals(errorDto.getError(),"Unauthorized");
    }
}
