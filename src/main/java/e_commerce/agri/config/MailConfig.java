package e_commerce.agri.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

	@Bean
	public JavaMailSender javaMailSender() {
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost("smtp.gmail.com");
	    mailSender.setPort(587); // STARTTLS uses port 587
	    mailSender.setUsername("vasanth2572@gmail.com");
	    mailSender.setPassword("qizj nffb lxeo bpbz");

	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.starttls.required", "true");
	    props.put("mail.smtp.connectiontimeout", "5000");
	    props.put("mail.smtp.timeout", "5000");
	    props.put("mail.smtp.writetimeout", "5000");
	    props.put("mail.debug", "true");

	    return mailSender;
	}

}
