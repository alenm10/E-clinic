package mrs.eclinicapi.service;

import mrs.eclinicapi.dto.OnDisapproveVacationCompleteEvent;
import mrs.eclinicapi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class DisapproveVacationListener implements
        ApplicationListener<OnDisapproveVacationCompleteEvent> {

    @Autowired
    UnregisteredUserService service;

    @Autowired
    private Environment env;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnDisapproveVacationCompleteEvent event) {
        this.confirmDisapproveVacation(event);
    }

    private void confirmDisapproveVacation(OnDisapproveVacationCompleteEvent event) {

        User user = event.getUser();



        String recipientAddress = user.getEmail();
        String subject = "Vacation request declined";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        String mail = env.getProperty("spring.mail.username");
        if (mail == null) return;
        email.setFrom(mail);
        email.setSubject(subject);
        email.setText(user.getName() + ", your vacation request was declined." +
                "\r\n" + "Reason: " + event.getReason());
        mailSender.send(email);
    }
}
