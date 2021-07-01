package codes;

import tools.Database;
import tools.Util;
import layouts.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public final class Evote {
    /**
     * HashMap eventList akan menampung semua data Event
     */
    public static HashMap<Integer,Event> eventList = new HashMap<>();
    /**
     * HashMap userList akan menampung semua data User
     */
    public static HashMap<String,User> userList = new HashMap<>();

    /**
     * Fungsi main program
     */
    public static void main(String[] args) {
        Util.initLogger();
        loginForm lf = new loginForm();
        lf.showForm();
    }

    /**
     * @param user
     * User yang mengakses method ini
     * @param level
     * perbedaan level akan memuat data yang berbeda tergantung level
     * User level : 1=User, 2=Panitia, 3=Admin
     */
    public static void loadEventData(User user,int level){
        eventList.clear();
        ArrayList eventData = null;
        switch (level){
            case 1:
                String sql_load_event_1 = "SELECT id,nama,deskripsi,enable FROM event WHERE id IN (SELECT idEvent FROM daftarpemilih WHERE npm=? AND used=0)";
                ArrayList<String> paramPanitia1= new ArrayList<String>();
                paramPanitia1.add(user.getNpm());
                eventData = Database.executeQuery(sql_load_event_1,paramPanitia1);
                break;
            case 2:
                String sql_load_event_2 = "SELECT id,nama,deskripsi,enable FROM event WHERE id IN (SELECT idEvent FROM panitia WHERE npm=?)";
                ArrayList<String> paramPanitia2= new ArrayList<String>();
                paramPanitia2.add(user.getNpm());
                eventData = Database.executeQuery(sql_load_event_2,paramPanitia2);
                break;
            case 3:
                String sql_load_event_3 = "SELECT id,nama,deskripsi,enable FROM event";
                eventData = Database.executeQuery(sql_load_event_3);
                break;
        }

        for (Object eventDatum : eventData) {
            HashMap eventRow = (HashMap) eventDatum;
            int idEvent = Integer.parseInt(eventRow.get("id").toString());
            ImageIcon foto = Database.getImage("SELECT foto FROM event WHERE id="+idEvent);
            Event ev = new Event(idEvent, eventRow.get("nama").toString(), foto, eventRow.get("deskripsi").toString());
            ev.daftarPemilih = loadDaftarPemilihData(String.valueOf(idEvent));
            ev.daftarPemilihan = loadPemilihanData(String.valueOf(idEvent));
            ev.daftarPanitia = loadPanitiaData(String.valueOf(idEvent));
            ev.setEnable(eventRow.get("enable").toString().equals("1"));
            eventList.put(ev.getId(), ev);
        }
    }

    /**
     * @param idEvent
     * Event.id di pemilih akan dimuat
     * @return
     * HashMap yang mengandung pemilih(User)
     */
    private static HashMap<Integer,User> loadDaftarPemilihData(String idEvent){
        HashMap<Integer,User> listDpt = new HashMap<>();
        String sql_load_dpt = "SELECT d.id as id, d.npm as npm, u.nama as nama FROM daftarpemilih d LEFT JOIN user u ON u.npm=d.npm WHERE d.idEvent=?";
        ArrayList<String> param = new ArrayList<String >();
        param.add(idEvent);
        ArrayList dptData = Database.executeQuery(sql_load_dpt,param);
        for (Object dptDatum : dptData) {
            HashMap dptRow = (HashMap) dptDatum;
            User dptUser ;
            try {
                dptUser=new User(dptRow.get("npm").toString());
            }catch (Exception ex){
                dptUser = new User();
                dptUser.setNpm(dptRow.get("npm").toString());
            }
            listDpt.put(Integer.parseInt(dptRow.get("id").toString()), dptUser);
        }
        return listDpt;
    }

    /**
     * @param idEvent
     * Event.id di Pemilihan akan dimuat
     * @return
     * HashMap yang mengandung Pemilihan
     */
    private static HashMap<Integer,Pemilihan> loadPemilihanData(String idEvent){
        HashMap<Integer,Pemilihan> listPemilihan = new HashMap<>();
        String sql_load_pemilihan = "SELECT * FROM pemilihan WHERE idEvent = ?";
        ArrayList<String> param = new ArrayList<String>();
        param.add(idEvent);
        ArrayList pemilihanData = Database.executeQuery(sql_load_pemilihan, param);
        for (Object pemilihanDatum : pemilihanData) {
            HashMap pemilihanRow = (HashMap) pemilihanDatum;
            Pemilihan pemilihanEv = new Pemilihan(Integer.parseInt(pemilihanRow.get("id").toString()), pemilihanRow.get("nama").toString());
            pemilihanEv.paslonList = loadPaslonData(pemilihanRow.get("id").toString());
            listPemilihan.put(pemilihanEv.getId(), pemilihanEv);
        }
        return listPemilihan;
    }

    /**
     * @param idPemilihan
     * Pemilihan.id di paslon akan dimuat
     * @return
     * HashMap yang mengandung Paslon
     */
    private static HashMap<Integer,Paslon> loadPaslonData(String idPemilihan){
        HashMap<Integer,Paslon> listPaslon = new HashMap<>();
        String sql_load_calon = "SELECT c.id, u1.nama AS ketua, u2.nama AS wakil, c.visi, c.misi, c.npmKetua as npmKetua, c.npmWakil as npmWakil, c.suara FROM calon c LEFT JOIN user u1 ON u1.npm = c.npmKetua LEFT JOIN user u2 ON c.npmWakil = u2.npm WHERE c.idPemilihan = ?";
        ArrayList<String> param = new ArrayList<String>();
        param.add(idPemilihan);
        ArrayList paslonData = Database.executeQuery(sql_load_calon, param);
        for (Object paslonDatum : paslonData) {
            HashMap paslonRow = (HashMap) paslonDatum;
            User ketua = new User(paslonRow.get("npmKetua").toString());
            User wakil;
            try {
                wakil = new User(paslonRow.get("npmWakil").toString());
            } catch (Exception ex) {
                wakil = null;
            }
            ImageIcon image=Database.getImage("SELECT foto FROM calon WHERE id="+paslonRow.get("id").toString());
            Paslon paslon = new Paslon(Integer.parseInt(paslonRow.get("id").toString()), ketua, wakil,image, paslonRow.get("visi").toString(), paslonRow.get("misi").toString(),Integer.parseInt(paslonRow.get("suara").toString()));
            listPaslon.put(paslon.getId(), paslon);
        }
        return listPaslon;
    }

    /**
     * @param idEvent
     * Event.id di Panitia akan dimuat
     * @return
     * HashMap yang mengandung Panitia
     */
    private static HashMap<Integer, Panitia> loadPanitiaData(String idEvent){
        HashMap<Integer,Panitia> listPanitia = new HashMap<>();
        String sql_load_panitia = "SELECT * FROM panitia WHERE idEvent = ?";
        ArrayList<String> param = new ArrayList<>(Arrays.asList(idEvent));
        ArrayList panitiaData = Database.executeQuery(sql_load_panitia,param);
        for(Object panitiaDatum : panitiaData){
            HashMap panitiaRow = (HashMap) panitiaDatum;
            User userPanitia = new User(panitiaRow.get("npm").toString());
            Panitia newPanitia = new Panitia(Integer.parseInt(panitiaRow.get("id").toString()),userPanitia,panitiaRow.get("jabatan").toString());
            listPanitia.put(newPanitia.getId(), newPanitia);
        }
        return  listPanitia;
    }

    /**
     * Method untuk memuat data user dan menempatkannya pada userList
     */
    public static void loadUserData(){
        userList.clear();
        String sql_load_user = "SELECT npm,nama,email FROM user";
        ArrayList userData = Database.executeQuery(sql_load_user);
        for(Object userDatum:userData){
            HashMap userRow = (HashMap) userDatum;
            User userLoad = new User(userRow.get("npm").toString(),userRow.get("nama").toString(),userRow.get("email").toString());
            userList.put(userLoad.getNpm(),userLoad);
        }
    }
}