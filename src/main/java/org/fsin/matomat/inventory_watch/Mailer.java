package org.fsin.matomat.inventory_watch;

import org.fsin.matomat.Configurator;
import org.fsin.matomat.database.Database;
import org.fsin.matomat.database.model.AdminEntry;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.Properties;

public class Mailer {

    public static class CratesPerProduct {
        String product;
        int crates;

        public CratesPerProduct(String product, int crates) {
            this.product = product;
            this.crates = crates;
        }

        public String getProduct() {
            return product;
        }

        public int getCrates() {
            return crates;
        }
    }

    public void sendTestMessage() {
        sendReorderMessage("testProduct",
                new CratesPerProduct[] {
                        new CratesPerProduct("testProduct", 1337),
                        new CratesPerProduct("pink cream", 73),
                        new CratesPerProduct("soilent green", 42)
                });
    }

    public void sendReorderMessage(String productOutOfStock, CratesPerProduct[] cratesLeft) {
        try {
            Database db = Database.getInstance();
            List<AdminEntry> admins = db.adminGetAll(0, 10000, true);
            Configurator config = Configurator.getInstance();

            String message = buildMessage(
                    loadTemplate(config.getValueString("mail_template_file")),
                    productOutOfStock, cratesLeft);

            String subject = config.getValueString("mail_subject")
                    .replace("<{product}>", productOutOfStock);

            for(AdminEntry admin : admins) {
                try {
                    sendMessage(admin.getEmail(), message, subject);
                } catch (Exception e)  {
                    System.err.println("Could not send message to admin: "
                            + admin.getUsername()
                            + " with address: "
                            + admin.getEmail());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.err.println("Could not send mail to any admins:");
            e.printStackTrace();
        }

    }

    private String loadTemplate(String fileName) {
        try {
            BufferedReader file = new BufferedReader(new FileReader(fileName));
            String line;
            StringBuilder msg = new StringBuilder();
            while((line = file.readLine()) != null) {
                msg.append(line);
                msg.append("\n");
            }
            return msg.toString();
        } catch (Exception e) {
            throw new RuntimeException("Could not load mail template", e);
        }
    }

    private String buildMessage(String template, String product, CratesPerProduct[] cratesLeft) {

        StringBuilder productCrateList = new StringBuilder();
        for(CratesPerProduct cpp : cratesLeft) {
            productCrateList.append(cpp.getProduct());
            productCrateList.append(":\t");
            productCrateList.append(cpp.getCrates());
            productCrateList.append("\n");
        }
        return template.replace("<{product}>", product)
                .replace("<{crates_left_list}>", productCrateList.toString());
    }

    private void sendMessage(String recipient, String mesg, String subject) {
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
            message.setSubject(subject);
            MimeBodyPart msgBody = new MimeBodyPart();
            msgBody.setContent(mesg, "text/plain");
            message.setContent( new MimeMultipart(msgBody));
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
