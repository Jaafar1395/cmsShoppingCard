package com.jchaaban.cmsshoppingcard.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private final String senderEmail = "hadi13afyouni@gmail.com";

    public void sendEmailWithFileAttachment(String username, String receiverEmail, File attachment) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setSubject("Renchnung");
        helper.setFrom(senderEmail);
        helper.setTo(receiverEmail);
        helper.addAttachment("Rechnung.pdf", attachment);

        helper.setText("<h3>Hallo " + username + " im Anhang finden Sie die Rechnung Ihrer Bestellung<h3>", true);
        helper.setText("<h3>Vielen Dnak für Ihren Eunfauf<h3>", true);

        mailSender.send(message);
    }

}
