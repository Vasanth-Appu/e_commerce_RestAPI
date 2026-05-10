package e_commerce.agri.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import e_commerce.agri.modal.Customer;
import e_commerce.agri.repository.CustomerRepo;
import jakarta.mail.internet.MimeMessage;

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

        try {

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("🔐 OTP Verification - APSA");

            String htmlContent =
                    "<div style='font-family:Arial,sans-serif;max-width:600px;margin:auto;" +
                    "padding:30px;border:1px solid #e0e0e0;border-radius:12px;" +
                    "box-shadow:0 4px 12px rgba(0,0,0,0.08);'>" +

                    "<div style='text-align:center;padding-bottom:20px;'>" +
                    "<h1 style='color:#2e7d32;margin:0;'>APSA</h1>" +
                    "<p style='color:#666;font-size:14px;'>Secure OTP Verification</p>" +
                    "</div>" +

                    "<hr style='border:none;border-top:1px solid #eee;'/>" +

                    "<p style='font-size:16px;color:#333;'>Hello,</p>" +

                    "<p style='font-size:15px;color:#555;line-height:1.6;'>" +
                    "Use the following One-Time Password (OTP) to continue your verification process." +
                    "</p>" +

                    "<div style='text-align:center;margin:30px 0;'>" +
                    "<span style='display:inline-block;padding:18px 40px;" +
                    "font-size:32px;font-weight:bold;letter-spacing:8px;" +
                    "color:#ffffff;background:#2e7d32;border-radius:10px;'>" +
                    otp +
                    "</span>" +
                    "</div>" +

                    "<p style='font-size:14px;color:#777;'>" +
                    "This OTP is valid for <b>5 minutes</b>." +
                    "</p>" +

                    "<p style='font-size:14px;color:#777;'>" +
                    "Do not share this OTP with anyone." +
                    "</p>" +

                    "<hr style='border:none;border-top:1px solid #eee;margin-top:25px;'/>" +

                    "<p style='font-size:13px;color:#999;text-align:center;'>" +
                    "© 2026 APSA. All rights reserved." +
                    "</p>" +

                    "</div>";

            helper.setText(htmlContent, true);

            javaMailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
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
    
    
    private Map<String, Customer> pendingUsers = new HashMap<>();

    public void storeTempCustomer(String email, Customer customer) {
        pendingUsers.put(email, customer);
        System.out.println("pending users ------------------->"+pendingUsers);
    }

    public Customer getTempCustomer(String email) {
    	System.out.println("get email--------------------------->"+ email);
        return pendingUsers.get(email);
    }

    public void deleteTempCustomer(String email) {
        pendingUsers.remove(email);
    }

    

}
