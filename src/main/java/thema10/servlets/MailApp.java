package thema10.servlets;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailApp {
    public static void send(String from, String to,String subject,String message){

        try {
            Properties props = new Properties();
//            props.put("mail.smtp.socketFactory.port", "587");
//            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//            props.put("mail.smtp.socketFactory.fallback", "true");
            props.put("mail.smtp.host", "smtp-mail.live.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.starttls.enable","true");
            props.put("mail.smtp.auth", "true");

            Session session = Session.getDefaultInstance(props);

//            Session emailSession = Session.getDefaultInstance(props, null);

            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from, "NoReply"));
            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to, "Mr. Recipient"));
            msg.setSubject("Welcome To Java Mail API");
            msg.setText(message);
            Transport.send(msg);
            System.out.println("Email sent successfully...");
        } catch (Exception e) {
            System.out.println("fout");
        }

    }
}