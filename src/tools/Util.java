package tools;

import codes.Evote;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.security.CodeSource;
import java.security.SecureRandom;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

public final class Util {
    public static final Logger logger = Logger.getLogger("MyLog");

    /**
     * Inisiasi Log file untuk mencatat pesan kesalahan
     */
    public static void initLogger(){

        FileHandler fh = null;
        try {
            try {
                CodeSource codeSource = Evote.class.getProtectionDomain().getCodeSource();
                File jarFile = new File(codeSource.getLocation().toURI().getPath());
                String jarDir = jarFile.getParentFile().getPath();
                fh = new FileHandler(jarDir+"/EvoteJava.log");
            } catch (IOException e) {
                e.printStackTrace();
            }
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (Exception e){
            e.printStackTrace();
        }
        logger.setUseParentHandlers(false);
    }

    /**
     * Fungsi untuk mengubah ukuran image
     * @param icon
     * Image icon yang akan diubah ukurannya
     * @param w
     * Width yang diingkan
     * @param h
     * Height yang diingikan
     * @return
     * ImageIcon yang telah diubah ukurannya
     */
    public static ImageIcon scaleImage(ImageIcon icon, int w, int h)
    {
        int nw = icon.getIconWidth();
        int nh = icon.getIconHeight();
        if(icon.getIconWidth() > w)
        {
            nw = w;
            nh = (nw * icon.getIconHeight()) / icon.getIconWidth();
        }
        if(nh > h)
        {
            nh = h;
            nw = (icon.getIconWidth() * nh) / icon.getIconHeight();
        }
        return new ImageIcon(icon.getImage().getScaledInstance(nw, nh, Image.SCALE_DEFAULT));
    }

    /**
     * Fungsi untuk mengambil ImageIcon sesuai dengan nama dan Look n Feel yang sedang dipakai
     * @param filename
     * Nama file dari icon yang ingin didapat
     * @return
     * ImageIcon yang didapat
     */
    public static ImageIcon getIcon(String filename) {
        try {
            String folderName="";

            switch (Config.PREF.get(Config.LOOK_N_FEEL,"")){
                case "FlatLight":
                case "FlatIntellij" :
                    folderName="dark/";
                    break;
                case "FlatDark":
                case "FlatDarcula":
                default:
                    folderName="light_gray/";
            }
            return new ImageIcon(ImageIO.read(Evote.class.getResourceAsStream("/image/"+folderName+filename)));
        } catch (Exception e) {
            //Tidak menampilkan ke log karena normal mendapatkan null saat load icon pertama
            return null;
        }
    }

    /**
     * @param length
     * Panjang password yang diinginkan
     * @return
     * Hasil random password
     */
    public static String createRandomPassword(int length) {
        String rand = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz01234567890";
        return new SecureRandom()
                .ints(length, 0, rand.length())
                .mapToObj(rand::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }
}