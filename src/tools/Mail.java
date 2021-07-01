package tools;

import java.util.Properties;
import java.util.logging.Level;
import java.util.prefs.Preferences;
import javax.mail.*;
import javax.mail.internet.*;

public final class Mail {
    static Preferences pref = Preferences.userNodeForPackage(layouts.configPanel.class);
    private final static String username = pref.get(Config.USER_SMTP,"");
    private final static String password = pref.get(Config.PASS_SMTP,"");
    private final static String HOST = pref.get(Config.HOST_SMTP,"");
    private final static String iSSL = pref.get(Config.SSL_SMTP,"true");
    private final static String PORT = pref.get(Config.PORT_SMTP,"465");

    /**
     * @param toEmail
     * Email tujuan
     * @param subject
     * Subject email
     * @param emailMessage
     * Pesan yang ingin dikirim
     * @return
     * Apakah berhasil mengirim email atau tidak
     */
    public static boolean sendMail(String toEmail,String subject,String emailMessage){
        Properties props = new Properties();

        props.put("mail.smtp.host",HOST);
        props.put("mail.smtp.auth","true");
        props.put("mail.smtp.ssl.enable",iSSL);
        props.put("mail.smtp.port", PORT);

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username,password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(emailMessage);
            Transport.send(message);
            return true;
        }catch (Exception ex){
            Util.logger.log(Level.WARNING,ex.getMessage());
            return false;
        }
    }
}
