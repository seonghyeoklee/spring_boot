package com.study.boot1;

import com.google.gson.GsonBuilder;
import com.study.boot1.model.GoogleOAuth;
import com.study.boot1.model.GoogleUserInfo;
import com.study.boot1.rest.GoogleOAuthAPI;
import com.study.boot1.rest.GoogleUserInfoAPI;
import com.study.boot1.util.MailUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.test.context.junit4.SpringRunner;
import retrofit2.Response;

import java.util.Base64;
import java.util.Locale;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Boot1ApplicationTests {
    @Autowired
    Environment environment;

    @Autowired
    MessageSource messageSource;

    @Autowired
    GoogleOAuthAPI googleOAuthAPI;

    @Autowired
    GoogleUserInfoAPI googleUserInfoAPI;


    @Test
    public void getLocaleMessage() {
        String code = "test.a.b";
        System.out.println( messageSource.getMessage(code, new Object[]{"@@@"}, Locale.KOREA) );
        System.out.println( messageSource.getMessage(code, new Object[]{"@@@"}, Locale.US) );
    }

    @Test
    public void getProperty(){
        String url = environment.getProperty("spring.datasource.url");
        System.out.println(url);
    }

    @Test
    public void sendMail() {
        new MailUtil().send("hahaha", "woody.jwk@kakaomobility.com", "테스트" ,"테스트입니다.");
    }

    @Test
    public void generateKey(){
        String key = KeyGenerators.string().generateKey();
        System.out.println( "key : "+key );
    }

    @Test
    public void cryptoAES256() {
        String key = "c263335844bd9b08";
        String password = "passwordpasswordpasswordpassword";

        String originString = "aaaa";

        TextEncryptor textEncryptor = Encryptors.text(password, key);

        String encryptedString = textEncryptor.encrypt(originString);
        System.out.println( "encryptedString : " + encryptedString );

        originString = textEncryptor.decrypt(encryptedString);
        System.out.println( "orginString : " + originString );

        String encodeString = Base64.getEncoder().encodeToString(encryptedString.getBytes());
        System.out.println( encodeString );

        String decodeString = new String(Base64.getDecoder().decode(encodeString.getBytes()));
        System.out.println( decodeString );
    }

    @Test
    public void googleAuthTest() throws Exception {
        String code = "4/0gBfMuWg9Lhf9XU_GK9g_KaugfVtk7fnvOjkYWuNeP_wlnv5OfQbMzzfRpCMEX36E--Z-Jur7jSIGbRIxAuP9c0";

        Response<GoogleOAuth> response = googleOAuthAPI.getToken(GoogleOAuthAPI.TOKEN_STATIC_FILED_MAP, code).execute();

        GoogleUserInfo googleUserInfo = googleUserInfoAPI.userInfo(response.body().getTokenType()+" "+response.body().getAccessToken()).execute().body();

        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(googleUserInfo));
    }

}