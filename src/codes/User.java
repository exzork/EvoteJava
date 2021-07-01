package codes;
import tools.Config;
import tools.Database;
import tools.Util;

import java.util.*;
import java.util.logging.Level;
import java.util.prefs.Preferences;

public class User {
    private String npm;
    private String nama;
    private String email;

    /**
     * default costructor, used in several case
     */
    public User(){};

    /**
     * Digunakan ketika memuat data pemilih dari database
     */
    public User(String npm){
        String sql_getUser = "SELECT * FROM user WHERE npm=?";
        ArrayList<String> param = new ArrayList<>(Arrays.asList(npm));
        try{
            if (Database.execute(sql_getUser,param)){
                ArrayList getUserList = Database.executeQuery(sql_getUser,param);
                HashMap getUserData = (HashMap) getUserList.get(0);
                this.npm = getUserData.get("npm").toString();
                this.nama = getUserData.get("nama").toString();
                this.email = getUserData.get("email").toString();
            }
        }catch (Exception e){
            System.out.println("NPM User Tidak ditemukan");
        }
    }

    /**
     * Diguakan ketika memuat data user dari database dan menambahkan user
     */
    public User(String npm, String nama, String email){
        this.npm=npm;
        this.nama=nama;
        this.email= email;
    }

    /**
     * @param npm
     * npm dari user yang akan login
     * @param password
     * password dari user yang akan login
     * @return
     * User object dengan attributes jika berhasil login
     */
    public User login(String npm,String password){
        String sql_login = "SELECT * FROM user WHERE npm=? AND password=?";
        ArrayList<String> param =  new ArrayList<String >(Arrays.asList(npm,password));
        try{
            if(Database.executeCountQuery(sql_login,param)>0){
                ArrayList userList = Database.executeQuery(sql_login,param);
                HashMap userData = (HashMap) userList.get(0);
                this.npm = (String) userData.get("npm");
                this.nama = (String) userData.get("nama");
                this.email = (String) userData.get("email");
                return this;
            }
        }catch (Exception e){
            Util.logger.log(Level.WARNING,e.getMessage());
        }
        return this;
    }

    /**
     * Memeriksa user npm apakah sama dengan pilihan admin npm atau tidak
     * @return
     * Mengembalikan nilai admin True atau False
     */
    public boolean isAdmin(){
        Preferences pref = Preferences.userNodeForPackage(layouts.configPanel.class);
        return this.npm.equals(pref.get(Config.NPM_ADMIN,""));
    }

    /**
     * Memeriksa database apakah user ini panitia atau tidak
     * @return
     * Mengembalikan nilai panitia True atau False
     */
    public boolean isPanitia(){
        String sql_check = "SELECT * FROM panitia WHERE npm=?";
        ArrayList<String> param = new ArrayList<>(Arrays.asList(npm));
        return Database.executeCountQuery(sql_check, param) > 0;
    }

    /**
     * @param pilihan
     * Pilihan paslon yang telah dipilih user dari form
     * @return
     * Mengembalikan nilai True atau False saat method sukses dieksekusi
     */
    public boolean simpanPilihan(HashMap<Integer,Paslon> pilihan){
        String sql_simpan = "UPDATE daftarpemilih SET used=1 WHERE npm=?";
        ArrayList<String> param = new ArrayList<String>(Arrays.asList(this.npm));
        if(Database.execute(sql_simpan,param)){
            Set<Map.Entry<Integer,Paslon>> pilihanSet = pilihan.entrySet();
            for (Map.Entry<Integer,Paslon> pilihanEntry : pilihanSet){
                Paslon paslon = pilihanEntry.getValue();
                if(paslon!=null){
                    paslon.terpilih();
                }
            }
            return true;
        }
        return false;
    }

    public void setNpm(String npm) {
        this.npm = npm;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNpm() {
        return npm;
    }

    public String getNama() {
        return nama;
    }

    public String getEmail() {
        return email;
    }
}