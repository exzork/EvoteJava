package tools;

import codes.User;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.prefs.Preferences;

public final class Config {
    public static Preferences PREF = Preferences.userNodeForPackage(layouts.configPanel.class);;

    public final static String LOOK_N_FEEL = "LOOK_N_FEEL";

    public final static String HOST_DB     = "HOST_DB";
    public final static String PORT_DB     = "PORT_DB";
    public final static String NAME_DB     = "NAME_DB";
    public final static String USER_DB     = "USER_DB";
    public final static String PASS_DB     = "PASS_DB";
    public final static String STATUS_DB   = "STATUS_DB";

    public final static String NPM_ADMIN   = "NPM_ADMIN";
    public final static String NAMA_ADMIN  = "NAMA_ADMIN";
    public final static String EMAIL_ADMIN = "EMAIL_ADMIN";
    public final static String PASS_ADMIN  = "PASS_ADMIN";

    public final static String HOST_SMTP   = "HOST_SMTP";
    public final static String PORT_SMTP   = "PORT_SMTP";
    public final static String SSL_SMTP    = "SSL_SMTP";
    public final static String USER_SMTP   = "USER_SMTP";
    public final static String PASS_SMTP   = "PASS_SMTP";
    public final static String SUBJECT_SMTP= "SUBJECT_SMTP";
    public final static String FORMAT_SMTP = "FORMAT_SMTP";
    public final static String STATUS_SMTP = "STATUS_SMTP";

    public static ImageIcon CAMERA_32;
    public static ImageIcon CHECKMARK_32;
    public static ImageIcon CONNECTED_32;
    public static ImageIcon CONTROL_PANEL_32;
    public static ImageIcon EDIT_32;
    public static ImageIcon LEFT_32;
    public static ImageIcon RIGHT_32;
    public static ImageIcon LOGIN_32;
    public static ImageIcon LOGOUT_32;
    public static ImageIcon PLUS_32;
    public static ImageIcon RESET_32;
    public static ImageIcon SAVE_32;
    public static ImageIcon SAVE_64;
    public static ImageIcon SEND_32;
    public static ImageIcon TRASH_32;

    public static Color SELECTED;

    static Connection conn ;

    /**
     * Memuat icon untuk disimpan di Class ini
     */
    public static void loadIcon(){
        CAMERA_32 = Util.getIcon("camera_32px.png");
        CHECKMARK_32 = Util.getIcon("checkmark_yes_32px.png");
        CONNECTED_32 = Util.getIcon("connected_32px.png");
        CONTROL_PANEL_32 = Util.getIcon("control_panel_32px.png");
        EDIT_32 = Util.getIcon("edit_32px.png");
        LEFT_32 = Util.getIcon("left_32px.png");
        RIGHT_32 = Util.getIcon("right_32px.png");
        LOGIN_32 = Util.getIcon("login_32px.png");
        LOGOUT_32 = Util.getIcon("logout_rounded_left_32px.png");
        PLUS_32 = Util.getIcon("plus_32px.png");
        RESET_32 = Util.getIcon("reset_32px.png");
        SAVE_32 = Util.getIcon("save_32px.png");
        SAVE_64 = Util.getIcon("save_32px.png");
        SEND_32 = Util.getIcon("send_32px.png");
        TRASH_32 = Util.getIcon("trash_32px.png");

        try {
            switch (Config.PREF.get(Config.LOOK_N_FEEL,"")){
                case "FlatDark":
                case "FlatDarcula":
                    SELECTED = Color.DARK_GRAY;
                    break;
                case "FlatLight":
                case "FlatIntellij":
                default:
                    SELECTED = Color.LIGHT_GRAY;
                    break;
            }
            UIManager.getLookAndFeelDefaults().put("defaultFont",new Font("Arial",Font.PLAIN,15));
        } catch (Exception e) {
            Util.logger.log(Level.WARNING,e.getMessage());
        }
    }

    /**
     * Memuat look and feel
     */
    public static void loadLnF(){
        loadIcon();
        try {
            switch (Config.PREF.get(Config.LOOK_N_FEEL,"")){
                case "FlatDark":
                    UIManager.setLookAndFeel( new FlatDarkLaf() );
                    break;
                case "FlatDarcula":
                    UIManager.setLookAndFeel( new FlatDarculaLaf() );
                    break;
                case "FlatIntellij" :
                    UIManager.setLookAndFeel( new FlatIntelliJLaf() );
                    break;
                case "FlatLight":
                default:
                    UIManager.setLookAndFeel( new FlatLightLaf() );
                    break;
            }
            UIManager.getLookAndFeelDefaults().put("defaultFont",new Font("Arial",Font.PLAIN,15));
        } catch (Exception e) {
            Util.logger.log(Level.WARNING,e.getMessage());
        }
    }

    /**
     * Test koneksi ke database apakah berhasil apa tidak
     * @param host
     * db host
     * @param port
     * db port
     * @param dbname
     * db name
     * @param username
     * db user
     * @param password
     * db password
     *
     * @return
     *
     * String dari fungsi createmysqltable atau pesan kesalahan
     */
    public static String testKoneksiDatabase(String host, String port, String dbname, String username, String password){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://"+host+":"+port+"/"+dbname;
            conn = DriverManager.getConnection(url,username,password);
            return createMysqlTable();
        }catch (Exception e){
            Util.logger.log(Level.WARNING,e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * Membuat mysqltable table jika belum ada
     * @return
     * String sukses atau pesan kesalahan
     */
    private static String createMysqlTable(){
        Scanner scan = new Scanner(Util.class.getResourceAsStream("/sql/createTable.sql")).useDelimiter(";");
        try {
            Statement st = conn.createStatement();
            while (scan.hasNext()){
                String line = scan.next().trim();
                if (!line.isEmpty())
                    st.execute(line);
            }
            return "Sukses";
        }catch (Exception e){
            Util.logger.log(Level.WARNING,e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * Save data admin ke database atau update jika sudah ada
     * @param user
     * User admin yang akan ditambahkan/update
     * @param password
     * Password dari user tersebut.
     * @return
     * Sukses, gagal atau pesan kesalahan
     */
    public static String saveAdminToDatabase(User user,String password){
        String sql = "INSERT INTO user(npm,nama,email,password) VALUES(?,?,?,?) ON DUPLICATE KEY UPDATE nama=?,email=?,password=?";
        ArrayList<String> param = new ArrayList<>(Arrays.asList(user.getNpm(),user.getNama(),user.getEmail(),password,user.getNama(),user.getEmail(),password));
        try {
            if(Database.execute(sql,param))
                return "Sukses";
            else
                return "Gagal menyimpan data";
        }catch (Exception e){
            Util.logger.log(Level.WARNING,e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * @param user
     * User admin
     * @param userPassword
     * Password user yang akan dikirimkan
     * @param host
     * Host smtp
     * @param port
     * Port smtp
     * @param isSSL
     * Apakah smtp menggunakan ssl atau tidak
     * @param username
     * username smtp
     * @param password
     * password smtp
     * @param subject
     * subject email
     * @param format
     * format email
     * @return
     * pesan sukses atau pesan kesalahan
     */
    public static String testSendEmail(User user, String userPassword,String host, String port, String isSSL, String username, String password, String subject, String format){
        if(user.getEmail() == null)
            return "Pastikan User Admin sudah disimpan ke database!";
        try {
            Properties props = new Properties();

            props.put("mail.smtp.host",host);
            props.put("mail.smtp.auth","true");
            props.put("mail.smtp.ssl.enable",isSSL);
            props.put("mail.smtp.port", port);
            props.put("mail.smtp.timeout",5000);
            props.put("mail.smtp.connectiontimeout",5000);

            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username,password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(user.getEmail()));
            message.setSubject(subject);

            format=format.replace("<nama>",user.getNama());
            format=format.replace("<npm>", user.getNpm());
            format=format.replace("<password>",userPassword);

            message.setText(format);
            Transport.send(message);
            return "Sukses";
        }catch (Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            Util.logger.log(Level.WARNING,sw.toString());
            return e.getMessage();
        }
    }
}
