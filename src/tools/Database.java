package tools;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.prefs.Preferences;

public final class Database {
    static Preferences pref = Preferences.userNodeForPackage(layouts.configPanel.class);

    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_HOST = pref.get(Config.HOST_DB,"");
    static final String DB_PORT = pref.get(Config.PORT_DB,"");
    static final String DB_NAME = pref.get(Config.NAME_DB,"");
    static final String DB_URL = "jdbc:mysql://"+DB_HOST+":"+DB_PORT+"/"+DB_NAME;
    static final String USER = pref.get(Config.USER_DB,"");
    static final String PASS = pref.get(Config.PASS_DB,"");

    static Connection conn;
    static Statement stmt;
    static ResultSet rs;
    static PreparedStatement ps;

    /**
     * @param sql
     * Sql yang akan dijalankan
     * @param param
     * Parameter dari sql yang akan dijalankan
     * @return
     * Apakah berhasil mengeksekusi sql tersebut atau tidak
     */
    public static boolean execute(String sql, ArrayList<String> param){
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            ps = conn.prepareStatement(sql);
            for (int i =1 ; i<=param.size();i++){
                ps.setString(i,param.get(i-1));
            }
            try {
                ps.execute();
                ps.close();
                conn.close();
                return true;
            }catch (Exception e){
               Util.logger.log(Level.WARNING,e.getMessage());
               return false;
            }
        }catch (Exception e){
            Util.logger.log(Level.WARNING,e.getMessage());
        }
        return false;
    }

    /**
     * @param sql
     * Sql yang mengupdate column image
     * @param imageIcon
     * Image icon yang baru
     * @return
     * Apakah berhasil mengeksekusi atau tidak
     */
    public static boolean updateImage(String sql, ImageIcon imageIcon){
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            ps = conn.prepareStatement(sql);
            BufferedImage bi = new BufferedImage(imageIcon.getIconWidth(),imageIcon.getIconHeight(),BufferedImage.TYPE_INT_RGB);
            Graphics g = bi.createGraphics();
            imageIcon.paintIcon(null,g,0,0);
            g.dispose();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bi,"jpeg",os);
            InputStream is = new ByteArrayInputStream(os.toByteArray());
            ps.setBlob(1,is);
            try {
                ps.execute();
                ps.close();
                conn.close();
                return true;
            }catch (Exception e){
                Util.logger.log(Level.WARNING,e.getMessage());
                return false;
            }
        }catch (Exception e){
            Util.logger.log(Level.WARNING,e.getMessage());
        }
        return false;
    }

    /**
     * @param sql
     * Sql untuk mengambil column image (blob)
     * @return
     * Image yang berhasil diambil
     */
    public static ImageIcon getImage(String sql){
        ImageIcon imageIcon = null;
        Blob blob = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if(rs.next()){
               blob=rs.getBlob("foto");
            }
            InputStream in = null;
            try {
                in = blob.getBinaryStream();
            } catch (SQLException throwables) {
                Util.logger.log(Level.WARNING,throwables.getMessage());
            }
            BufferedImage imageBuffer = null;
            try {
                imageBuffer = ImageIO.read(in);
            } catch (IOException e) {
                Util.logger.log(Level.WARNING,e.getMessage());
            }
            imageIcon= new ImageIcon(imageBuffer);
            stmt.close();
            conn.close();
        }catch (Exception e){
            Util.logger.log(Level.WARNING,e.getMessage());
        }
        return imageIcon;
    }

    /**
     * @param sql
     * Sql yang akan dijalankan
     * @param param
     * parameter dari sql yang akan dijalankan
     * @return
     * jumlah affected row (-1 jika terjadi kesalahan)
     */
    public static int executeUpdate(String sql, ArrayList<String> param){
        int affected = -1;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            ps = conn.prepareStatement(sql);
            for (int i =1 ; i<=param.size();i++){
                ps.setString(i,param.get(i-1));
            }
            affected = ps.executeUpdate();
            ps.close();
            conn.close();
        }catch (Exception e){
            Util.logger.log(Level.WARNING,e.getMessage());
        }
        return affected;
    }

    /**
     * @param sql
     * Sql yang akan dijalankan
     * @return
     * Data arraylist dari hasil query
     */
    public static ArrayList executeQuery(String sql){
        ArrayList arrayList = new ArrayList();
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            ResultSetMetaData resultSetMetaData =  rs.getMetaData();
            int columns = resultSetMetaData.getColumnCount();
            while (rs.next()){
                HashMap row = new HashMap(columns);
                for (int i =1;i<=columns;++i){
                    row.put(resultSetMetaData.getColumnLabel(i),rs.getString(i));
                }
                arrayList.add(row);
            }
            stmt.close();
            conn.close();
        }catch (Exception e){
            Util.logger.log(Level.WARNING,e.getMessage());
        }
        return arrayList;
    }

    /**
     * @param sql
     * Sql yang akan dijalankan
     * @param param
     * Parameter dari sql yang akan dijalankan
     * @return
     * Data arraylist dari hasil query
     */
    public static ArrayList executeQuery(String sql,ArrayList<String> param){
        ArrayList arrayList = new ArrayList();
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            ps = conn.prepareStatement(sql);
            for (int i =1 ; i<=param.size();i++){
                ps.setString(i,param.get(i-1));
            }
            rs = ps.executeQuery();
            ResultSetMetaData resultSetMetaData =  rs.getMetaData();
            int columns = resultSetMetaData.getColumnCount();
            while (rs.next()){
                HashMap row = new HashMap(columns);
                for (int i =1;i<=columns;++i){
                    row.put(resultSetMetaData.getColumnLabel(i),rs.getString(i));
                }
                arrayList.add(row);
            }
            ps.close();
            conn.close();
        }catch (Exception e){
            Util.logger.log(Level.WARNING,e.getMessage());
        }
        return arrayList;
    }


    /**
     * @param sql
     * Sql untuk menghitung jumlah row
     * @param param
     * parameter dari sql
     * @return
     * jumlah row yang didapatkan dari sql
     */
    public static int executeCountQuery(String sql,ArrayList<String> param){
        int count = 0;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            ps = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            for (int i =1 ; i<=param.size();i++){
                ps.setString(i,param.get(i-1));
            }
            rs = ps.executeQuery();
            rs.last();
            count = rs.getRow();
            rs.beforeFirst();
            ps.close();
            conn.close();
        }catch (Exception e){
            Util.logger.log(Level.WARNING,e.getMessage());
        }
        return count;
    }

    /**
     * @param tableName
     * Nama tabel yang ingin diketahui last idnya
     * @return
     * Last id dari tabel
     */
    public static int getLastID(String tableName){
        String sql = "SELECT id FROM "+tableName+" ORDER BY id DESC LIMIT 1";
        ArrayList ArrayRes = executeQuery(sql);

        if (ArrayRes.size()==1){
            HashMap hashMapRes = (HashMap) ArrayRes.get(0);
            return Integer.parseInt(hashMapRes.get("id").toString());
        }
        return -1;
    }
}