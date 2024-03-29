package cgm.service;

import cgm.model.entity.CruiseGroup;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.Locale;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender javaMailSender,
                        TemplateEngine templateEngine
                        ) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    public void sendInfoEmail(String userEmail, String name, List<CruiseGroup> soldGroups) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        try{
            mimeMessageHelper.setFrom("info@crusit.bg");
            mimeMessageHelper.setTo(userEmail);

            mimeMessageHelper.setSubject("Cruise Group Has Sold Out!");
            mimeMessageHelper.setText(generateEmailText(name, soldGroups), true);

            javaMailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    private String generateEmailText(String username, List<CruiseGroup> soldGroups){

        Context context = new Context();
        context.setLocale(Locale.getDefault());
        context.setVariable("userName", username);
        context.setVariable("soldGroups", soldGroups);

        return templateEngine.process("info-email", context);
    }
}
