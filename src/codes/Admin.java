package codes;
import tools.Config;
import tools.Database;
import tools.Mail;
import tools.Util;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.prefs.Preferences;

public class Admin {
    User user;

    /**
     * @param user
     * mengatur user pada admin
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @param event
     * Event ditambahkan
     * @return
     * Mengembalikan nilai True atau False pada saat method sukses dijalankan
     */
    public boolean tambahEvent(Event event)  {
        String sql_tambah_event = "INSERT INTO event(nama,deskripsi) VALUES(?,?)";
        ArrayList<String> param = new ArrayList<>(Arrays.asList(event.getNama(),event.getDeskripsi()));
        if(Database.execute(sql_tambah_event,param)){
            int idNewEvent = Database.getLastID("event");
            event.setId(idNewEvent);
            Database.updateImage("UPDATE event SET foto=? WHERE id="+event.getId(),event.getFoto());
            Evote.eventList.put(idNewEvent,event);
            return true;
        }
        return false;
    }

    /**
     * @param event
     * Event diedit (Event.id tidak perlu diedit)
     * @param withImage
     * Bernilai yes atau no pada saat meng-update (yes jika true)
     * @return
     * Mengembalikan nilai True atau False pada saat method berhasil dieksekusi
     */
    public boolean editEvent(Event event,boolean withImage){
        String sql_edit_event = "UPDATE event SET nama=?,deskripsi=? WHERE id=?";
        ArrayList<String> param = new ArrayList<>(Arrays.asList(event.getNama(),event.getDeskripsi(),String.valueOf(event.getId())));
        if(Database.execute(sql_edit_event,param)){
            if(withImage)
                Database.updateImage("UPDATE event SET foto=? WHERE id="+event.getId(),event.getFoto());
            ImageIcon image = Database.getImage("SELECT foto FROM event WHERE id="+event.getId());
            event.setFoto(image);
            Evote.eventList.put(event.getId(),event);
            return true;
        }
        return false;
    }

    /**
     * @param idEvent
     * Event id
     * @return
     * Mengembalikan nilai True atau False saat method sukses dieksekusi
     */
    public boolean hapusEvent(int idEvent){
        String sql_hapus_list = "DELETE FROM event WHERE id=?";
        ArrayList<String> param = new ArrayList<>(Arrays.asList(String.valueOf(idEvent)));
        if(Database.execute(sql_hapus_list,param)){
            Evote.eventList.remove(idEvent);
            return true;
        }
        return false;
    }

    /**
     * @param idEvent
     * Event.id di panitia akan ditambahkan
     * @param panitia
     * Panitia ditambahkan
     * @return
     * Mengembalikan nilai True atau False saat method sukses dieksekusi
     */
    public boolean tambahPanitia(int idEvent, Panitia panitia){
        String sql_tambah_panitia="INSERT INTO panitia(npm,jabatan,idEvent) VALUES(?,?,?)";
        ArrayList<String> param = new ArrayList<>(Arrays.asList(panitia.getUser().getNpm(),panitia.getJabatan(),String.valueOf(idEvent)));
        if(Database.execute(sql_tambah_panitia,param)){
            int idNewPanitia = Database.getLastID("panitia");
            panitia.setId(idNewPanitia);
            Evote.eventList.get(idEvent).daftarPanitia.put(idNewPanitia,panitia);
            return true;
        }
        return false;
    }

    /**
     * @param idEvent
     * Event.id di panitia sedang akan diedit
     * @param panitia
     * Panitia yang sudah diedit ( tidak perlu mengubah panitia.id )
     * @return
     * Mengembalikan nilai True atau False saat method sukses dieksekusi
     */
    public boolean editPanitia(int idEvent, Panitia panitia){
        String sql_edit_panitia ="UPDATE panitia SET npm=?,jabatan=? WHERE id=?";
        ArrayList<String> param = new ArrayList<>(Arrays.asList(panitia.getUser().getNpm(),panitia.getJabatan(),String.valueOf(panitia.getId())));
        if(Database.execute(sql_edit_panitia,param)){
            Evote.eventList.get(idEvent).daftarPanitia.put(panitia.getId(),panitia);
            return true;
        }
        return false;
    }

    /**
     * @param idEvent
     * Event.id di panitia akan dihapus
     * @param idPanitia
     * Panitia.id dari panitia saat akan dihapus
     * @return
     * Mengembalikan nilai True atau False saat method sukses dieksekusi
     */
    public boolean hapusPanitia(int idEvent, int idPanitia){
        String sql_hapus_panitia = "DELETE FROM panitia WHERE id=?";
        ArrayList<String> param = new ArrayList<>(Arrays.asList(String.valueOf(idPanitia)));
        if(Database.execute(sql_hapus_panitia,param)){
            Evote.eventList.get(idEvent).daftarPanitia.remove(idPanitia);
            return true;
        }
        return false;
    }

    /**
     * @param user
     * User akan ditambahkan
     * @return
     * Mengembalikan nilai True arau False saat method sukses dieksekusi
     */
    public boolean tambahUser(User user){
        String sql_tambah_user = "INSERT INTO user(npm,nama,email,password) VALUES(?,?,?,?)";
        String randomPassword = Util.createRandomPassword(8);
        ArrayList<String> param = new ArrayList<>(Arrays.asList(user.getNpm(),user.getNama(),user.getEmail(),randomPassword));

        Preferences pref = Preferences.userNodeForPackage(layouts.configPanel.class);
        String SUBJECT = pref.get(Config.SUBJECT_SMTP,"");
        String FORMAT = pref.get(Config.FORMAT_SMTP,"");
        FORMAT = FORMAT.replace("<nama>",user.getNama());
        FORMAT = FORMAT.replace("<npm>",user.getNpm());
        FORMAT = FORMAT.replace("<password>",randomPassword);

        if(!Mail.sendMail(user.getEmail(),SUBJECT,FORMAT))
            return false;
        if(Database.execute(sql_tambah_user,param)){
            Evote.userList.put(user.getNpm(),user);
            return true;
        }
        return false;
    }

    /**
     * @param user
     * User yang telah diedit ( Tidak perlu mengganti User.npm )
     * @return
     * Mengembalikan nilai True atau False saat method sukses dieksekusi
     */
    public boolean editUser(User user){
        String sql_edit_user = "UPDATE user SET nama=?,email=? WHERE npm=?";
        ArrayList<String> param = new ArrayList<>(Arrays.asList(user.getNama(),user.getEmail(),user.getNpm()));
        if(Database.execute(sql_edit_user,param)){
            Evote.userList.put(user.getNpm(),user);
            return true;
        }
        return false;
    }


    /**
     * @param user
     * User yang meminta pengiriman email
     * @return
     * Mengembalikan nilai True atau False saat method sukses dieksekusi
     */
    public boolean resendEmail(User user){
        Preferences pref = Preferences.userNodeForPackage(layouts.configPanel.class);
        String SUBJECT = pref.get(Config.SUBJECT_SMTP,"");
        String FORMAT = pref.get(Config.FORMAT_SMTP,"");
        String randomPassword = Util.createRandomPassword(8);
        FORMAT = FORMAT.replace("<nama>",user.getNama());
        FORMAT = FORMAT.replace("<npm>",user.getNpm());
        FORMAT = FORMAT.replace("<password>",randomPassword);
         if(Mail.sendMail(user.getEmail(), SUBJECT,FORMAT)){
             String sql_edit_password_user = "UPDATE user SET password=? WHERE npm=?";
             ArrayList<String> param = new ArrayList<>(Arrays.asList(randomPassword,user.getNpm()));
             if(Database.execute(sql_edit_password_user,param))
                 return true;
         }
         return false;
    }

    /**
     * @param npm
     * User.npm yang akan dihapus
     * @return
     * Mengembalikan nilai True atau False saat method sukses dieksekusi
     */
    public boolean hapusUser(String npm){
        String sql_hapus_user ="DELETE FROM user WHERE npm=?";
        ArrayList<String> param = new ArrayList<>(Arrays.asList(npm));
        if(Database.execute(sql_hapus_user,param)){
            Evote.userList.remove(npm);
            return true;
        }
        return false;
    }
}