package uni.social.connect.listener;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import uni.social.connect.event.OnRegistrationCompleteEvent;
import uni.social.connect.model.User;
import uni.social.connect.service.UserService;

@Component
@javax.transaction.Transactional
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

	@Autowired
	private UserService userService;

	@Autowired
	@Qualifier("mailSender")
	private MailSender mailSender;

	@Override
	public void onApplicationEvent(OnRegistrationCompleteEvent event) {
		this.confirmRegistration(event);
	}

	private void confirmRegistration(ApplicationEvent event) {
		OnRegistrationCompleteEvent registrationEvent = (OnRegistrationCompleteEvent) event;
		if (event == null) {
			return;
		}

		User user = registrationEvent.getUser();

		// TODO : think about more secure token!!!
		String token = UUID.randomUUID().toString();

		userService.createVerificationToken(user, token);

		String recipientAddress = user.getUsername();
		String subject = "Registration Confirmation";
		String confirmationUrl = registrationEvent.getAppUrl() + "/regitrationConfirm.html?token=" + token;
		// String message = messages.getMessage("message.regSucc", null,
		// registrationEvent.getLocale());
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setSubject(subject);
		email.setText("This is your token " + " rn" + "http://localhost:8080" + confirmationUrl);
		mailSender.send(email);
	}

}

