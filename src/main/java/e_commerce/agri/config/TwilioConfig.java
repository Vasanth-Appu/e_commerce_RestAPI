package e_commerce.agri.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import com.twilio.Twilio;

@Configuration
public class TwilioConfig {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @jakarta.annotation.PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }
}

