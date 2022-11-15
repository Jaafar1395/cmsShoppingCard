package com.jchaaban.cmsshoppingcard.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private final String senderEmail = "hadi13afyouni@gmail.com";

    public void sendEmailWithFileAttachment(String username, String receiverEmail) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setSubject("Hallo " + username + " im Anhang finden Sie die Rechnung Ihrer bestellung");
        helper.setFrom(senderEmail);
        helper.setTo(receiverEmail);

        helper.setText("<h2>Vielen Dank für Ihren Einkauf<h2>", true);

//        FileSystemResource file = new FileSystemResource(new File("src/main/resources/static/billings/billing.pdf"));
//        helper.addAttachment("Rechnung.pdf", file);
        mailSender.send(message);
    }

}
