package mymeal.tagrem.com.mymeal.mail;

import android.util.Log;

import com.sun.mail.imap.Utility;

import org.apache.http.auth.AuthenticationException;

import java.util.Date;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Created by Nikesh on 11/14/2014.
 */
public class Mail extends javax.mail.Authenticator {

    String UserEmail;
    String UserPassword;
    String UserSubject;
    String SendMailTO = "india-meals@tagrem.com";

    private Multipart _multipart;

    public Mail(String UserEmail,String UserPassword,String UserSubject)
    {
        this.UserEmail = UserEmail;
        this.UserPassword = UserPassword;
        this.UserSubject = UserSubject;

        _multipart = new MimeMultipart();

        // There is something wrong with MailCap, javamail can not find a handler for the multipart/mixed part, so this bit needs to be added.
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);
    }

    public boolean send(boolean isforLogin) {
        try {
            Properties props = _setProperties();
            if (!UserEmail.equals("") && !UserPassword.equals("") && !UserSubject.equals("")) {
                Session session = Session.getInstance(props, this);
                MimeMessage msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress(UserEmail));

                InternetAddress[] addressTo = new InternetAddress[1];
                if(isforLogin)
                addressTo[0] = new InternetAddress(UserEmail);
                else
                addressTo[0] = new InternetAddress(SendMailTO);
                msg.setRecipients(MimeMessage.RecipientType.TO, addressTo);

                msg.setSubject(UserSubject);
                msg.setSentDate(new Date());

                // setup message body
                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText("");
                _multipart.addBodyPart(messageBodyPart);

                // Put parts in message
                msg.setContent(_multipart);

                Transport.send(msg);
                    return true;
            } else {
                return false;
            }
        }
        catch (Exception ex)
        {
            return false;
        }
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(UserEmail, UserPassword);
    }

    private Properties _setProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.tagrem.com");
         props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", 587);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.socketFactory.port", 587);
        return props;
    }
}
