package org.fsin.matomat.inventory_watch;

import org.fsin.matomat.Configurator;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class Mailer {

    public void sendMessage(String recipient, String mesg) {
        try {
            final Configurator config = Configurator.getInstance();


            Properties prop = new Properties();
            prop.put("mail.smtp.auth", true);
            prop.put("mail.smtp.starttls.enable", config.getValueBool("mail_starttls"));
            prop.put("mail.smtp.host", config.getValueString("mail_smtp_host"));
            prop.put("mail.smtp.port", config.getValueInt("mail_smtp_port"));
            prop.put("mail.smtp.ssl.trust", config.getValueString("mail_smtp_host"));

            Session session = Session.getInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(config.getValueString("mail_smtp_user"),
                            config.getValueString("mail_smtp_password"));
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(config.getValueString("mail_address")));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject("Mate is empty");
            MimeBodyPart msgBody = new MimeBodyPart();
            msgBody.setContent(mesg, "text/plain");
            message.setContent( new MimeMultipart(msgBody));
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
