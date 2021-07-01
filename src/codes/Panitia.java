package codes;

import tools.Database;

import java.util.ArrayList;
import java.util.Arrays;

public class Panitia {
    private int id;
    private User user;
    private String jabatan;

    /**
     * Digunakan ketika memuat data dari database
     */
    public Panitia(int id, User user, String jabatan){
        this.id=id;
        this.user= user;
        this.jabatan=jabatan;
    }

    /**
     * Digunakan saat menambahkan panitia
     * @param panitiaUser
     * User akan ditambahkan sebagai panitia
     */
    public Panitia(User panitiaUser){
        this.user = panitiaUser;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getJabatan() {
        return jabatan;
    }


    /**
     * @param idEvent
     * Event.id di pemilih akan ditambahkan
     * @param pemilih
     * User akan ditambahkan ke daftar pemilih
     * @return
     * Mengembalikan nilai True atau False saat method sukses dieksekusi
     */
    public boolean tambahPemilih(int idEvent, User pemilih){
        String sql_tambah = "INSERT INTO daftarpemilih(npm,idEvent) VALUES(?,?)";
        ArrayList<String> param = new ArrayList<String>(Arrays.asList(pemilih.getNpm(),String.valueOf(idEvent)));
        if(Database.execute(sql_tambah,param)){
            int idPemilih = Database.getLastID("daftarpemilih");
            Evote.eventList.get(idEvent).daftarPemilih.put(idPemilih,pemilih);
            return true;
        }
        return false;
    }

    /**
     * @param idEvent
     * Event.id di pemilih akan dihapus
     * @param idPemilih
     * id pemilih akan dihapus
     * @return
     * Mengembalikan nilai True atau False saat method sukses dieksekusi
     */
    public boolean hapusPemilih(int idEvent, int idPemilih){
        String sql_hapus = "DELETE FROM daftarpemilih WHERE id=?";
        ArrayList<String> param = new ArrayList<String>(Arrays.asList(String.valueOf(idPemilih)));
        if(Database.execute(sql_hapus,param)){
            Evote.eventList.get(idEvent).daftarPemilih.remove(idPemilih);
            return true;
        }
        return false;
    }

    /**
     * @param paslon
     * Paslon akan ditambahkan
     * @param idEvent
     * Event.id di pemilihan dari paslon ini akan ditambahkan
     * @param idPemilihan
     * Pemilihan.id di paslon akan ditambahkan
     * @return
     * Mengembalikan nilai True atau False saat method sukses dieksekusi
     */
    public boolean tambahPaslon(Paslon paslon, int idEvent,int idPemilihan){
        String sql_tambah = "INSERT INTO calon(npmKetua,npmWakil,visi,misi,idPemilihan) VALUES(?,?,?,?,?)";
        ArrayList<String> param = new ArrayList<String>(Arrays.asList(paslon.getKetua().getNpm(), paslon.getWakil().getNpm(), paslon.getVisi(), paslon.getMisi(), String.valueOf(idPemilihan)));
        if(Database.execute(sql_tambah,param)){
            paslon.setId(Database.getLastID("calon"));
            Database.updateImage("UPDATE calon SET foto=? WHERE id="+paslon.getId(),paslon.getFoto());
            Evote.eventList.get(idEvent).daftarPemilihan.get(idPemilihan).paslonList.put(paslon.getId(),paslon);
            return true;
        }
        return false;
    }

    /**
     * @param paslon
     * Paslon yang telah diedit
     * @param idEvent
     * Event.id di pemilihan dari paslon ini ditambahkan
     * @param idPemilihan
     * Pemilihan.id di paslon akan diedit
     * @param withImage
     * Bernilai True atau False untuk meng-edit paslon.foto
     * @return
     * Mengembalikan nilai True atau False saat method sukses dieksekusi
     */
    public boolean editPaslon(Paslon paslon, int idEvent, int idPemilihan, boolean withImage){
        String sql_edit = "UPDATE calon SET npmKetua=?, npmWakil=?, visi=?, misi=? WHERE id=?";
        ArrayList<String> param = new ArrayList<>(Arrays.asList(paslon.getKetua().getNpm(),paslon.getWakil().getNpm(),paslon.getVisi(),paslon.getMisi(),String.valueOf(paslon.getId())));
        if(Database.execute(sql_edit,param)){
            if(withImage)
                Database.updateImage("UPDATE calon SET foto=? WHERE id="+paslon.getId(),paslon.getFoto());
            Evote.eventList.get(idEvent).daftarPemilihan.get(idPemilihan).paslonList.put(paslon.getId(),paslon);
            return true;
        }
        return false;
    }

    /**
     * @param idEvent
     * Event.id di pemilihan dari paslon ini ditambahkan
     * @param idPemilihan
     * Pemilihan id di paslon ini ditambahkan
     * @param idPaslon
     * Paslon.id dari paslon akan dihapus
     * @return
     * Mengembalikan nilai True atau False saat method sukses dieksekusi
     */
    public boolean hapusPaslon(int idEvent, int idPemilihan,int idPaslon){
        String sql_delete_calon = "DELETE FROM calon WHERE id=?";
        ArrayList<String> param = new ArrayList<String>();
        param.add(String.valueOf(idPaslon));
        if (Database.executeUpdate(sql_delete_calon,param)>0){
            Evote.eventList.get(idEvent).daftarPemilihan.get(idPemilihan).paslonList.remove(idPaslon);
            return true;
        }
        return false;
    }

    /**
     * @param idEvent
     * Event.id di pemilihan akan ditambahkan
     * @param pemilihan
     * Pemilihan yang akan ditambahkan
     * @return
     * Mengembalikan nilai True atau False saat method sukses dieksekusi
     */
    public boolean tambahPemilihan(int idEvent, Pemilihan pemilihan){
        String sql_tambah = "INSERT INTO pemilihan(nama,idEvent) VALUES(?,?)";
        ArrayList<String> param = new ArrayList<String>(Arrays.asList(pemilihan.getNama(),String.valueOf(idEvent)));
        if(Database.execute(sql_tambah,param)){
            pemilihan.setId(Database.getLastID("pemilihan"));
            Evote.eventList.get(idEvent).daftarPemilihan.put(pemilihan.getId(), pemilihan);
            return true;
        }
        return false;
    }

    /**
     * @param idEvent
     * Event.id di pemilihan akan diedit
     * @param pemilihan
     * Pemilihan yang telah diedit
     * @return
     * Mengembalikan nilai True atau False saat method sukses dieksekusi
     */
    public boolean editPemilihan(int idEvent, Pemilihan pemilihan){
        String sql_edit = "UPDATE pemilihan SET nama=? WHERE id=?";
        ArrayList<String> param = new ArrayList<>(Arrays.asList(pemilihan.getNama(),String.valueOf(pemilihan.getId())));
        if(Database.execute(sql_edit,param)){
            Evote.eventList.get(idEvent).daftarPemilihan.put(pemilihan.getId(),pemilihan);
            return true;
        }
        return false;
    }

    /**
     * @param idEvent
     * Event.id di pemilihan akan dihapus
     * @param idPemilihan
     * Pemilihan.id yang akan dihapus
     * @return
     */
    public boolean hapusPemilihan(int idEvent , int idPemilihan){
        String sql_delete_pemilihan = "DELETE FROM pemilihan WHERE id=?";
        ArrayList<String> param = new ArrayList<String>();
        param.add(String.valueOf(idPemilihan));
        if (Database.executeUpdate(sql_delete_pemilihan,param)>0){
            Evote.eventList.get(idEvent).daftarPemilihan.remove(idPemilihan);
            return true;
        }
        return false;
    }

    /**
     * @param idEvent
     * Event.id yang daftar pemilih akan diperiksa
     * @return
     * Mengembalikan nilai True atau False saat method sukses dieksekusi
     */
    public int cekDaftarPemilih(int idEvent){
        String sql_cek = "SELECT id FROM daftarpemilih WHERE idEvent=? AND used=0";
        ArrayList<String> param = new ArrayList<>(Arrays.asList(String.valueOf(idEvent)));
        return Database.executeCountQuery(sql_cek,param);
    }


    /**
     * @param idEvent
     * Event.id yang akan diatur menjadi enable=0 ( disabled )
     * @return
     * Mengembalikan nilai True atau False saat method sukses dieksekusi
     */
    public boolean selesaikanEvent(int idEvent){
        String sql_selesai = "UPDATE event SET enable=0 WHERE id=?";
        ArrayList<String> param = new ArrayList<>(Arrays.asList(String.valueOf(idEvent)));
        return Database.execute(sql_selesai, param);
    }
}