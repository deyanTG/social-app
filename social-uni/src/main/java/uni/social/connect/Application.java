package uni.social.connect;

import org.jsondoc.spring.boot.starter.EnableJSONDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by Tan on 18-Apr-16. Spring Boot Launcher
 */
@SpringBootApplication
@EnableAsync
@EnableJSONDoc
@EnableTransactionManagement
public class Application {

    public interface Constants {
        int DEFAULT_PAGE_SIZE = 10;
        String DEFAULT_PAGE_SIZE_STR = "10";
        String HTTP_STATUS_CODE = "javax.servlet.error.status_code";
        int REMEMBER_ME_IN_SECONDS = 86400;
        String REMEMBER_ME_PARAMETER_AND_COOKIE_NAME = "remember-me";
        int MAXIMUM_SESSIONS_ALLOWED = 1;
        int VERIFICATION_TOKEN_EXPIRATION = 60 * 24;
        int PASSWORD_RESET_TOKEN_EXPIRATION = 60 * 24;
        int MAXIMUM_ES_RESULTS = 1000;
        int MAXIMUM_GAZETTEER_RESULTS = 100;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
