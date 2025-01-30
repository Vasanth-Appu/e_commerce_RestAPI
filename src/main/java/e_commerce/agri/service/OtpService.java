package e_commerce.agri.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import e_commerce.agri.modal.Customer;
import e_commerce.agri.repository.CustomerRepo;

@Service
public class OtpService {
	@Autowired 
	private JavaMailSender javaMailSender;
	@Autowired CustomerRepo customerRepo;
	  
	@Value("${twilio.phone.number}")
	    private String twilioPhoneNumber;
	  
    private final Map<String, String> otpStore = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> otpExpiry = new ConcurrentHashMap<>();
    private final int OTP_EXPIRATION_MINUTES = 5;

    public void saveOtp(String email, String otp) {
        otpStore.put(email, otp);
        otpExpiry.put(email, LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTES));
    }

    public boolean verifyOtp(String email, String otp) {
        if (!otpStore.containsKey(email) || LocalDateTime.now().isAfter(otpExpiry.get(email))) {
            return false;
        }
        return otpStore.get(email).equals(otp);
    }

    public void deleteOtp(String email) {
        otpStore.remove(email);
        otpExpiry.remove(email);
    }

    public void sendOtp(String email, String otp) {
 
        System.out.println("Sending OTP " + otp + " to " + email); //uncommand when use email auuth
//        SimpleMailMessage mailOtp = new SimpleMailMessage();
//        mailOtp.setTo(email);
//        mailOtp.setSubject("Otp verification By Apsa");
//        mailOtp.setText("Verify your OTP: " + otp );
//       // mailOtp.setText("Verify your OTP: <b>" + otp + "</b>", "text/html");
//        javaMailSender.send(mailOtp);
    }
  

    public void sendSms(String email, String otp) {
        Optional<Customer> getCusNum = customerRepo.findByEmailId(email);

        if (getCusNum.isPresent()) {
            String number = getCusNum.get().getCustContact();

            try {
                Message message = Message.creator(
                        new PhoneNumber("+91 "+number),  // Receiver's phone number
                        new PhoneNumber(twilioPhoneNumber), // Twilio phone number
                        "Your APSA verification OTP is: " + otp)
                    .create();
                
                System.out.println("SMS Sent Successfully. SID: " + message.getSid());
            } catch (Exception e) {
                System.err.println("Error sending SMS: " + e.getMessage());
            }
        } else {
            System.err.println("Customer not found with email: " + email);
        }
    }

    

}
