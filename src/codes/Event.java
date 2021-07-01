package codes;

import javax.swing.*;
import java.util.HashMap;

public class Event {
    private int id;
    private String nama;
    private ImageIcon foto;
    private String deskripsi;
    public HashMap<Integer,Pemilihan> daftarPemilihan = new HashMap<>();
    public HashMap<Integer ,User> daftarPemilih = new HashMap<>();
    public HashMap<Integer, Panitia> daftarPanitia = new HashMap<>();
    private boolean enable;

    /**
     * Digunakan saat memuat data dari database
     * @param id
     * Event.id
     * @param nama
     * Event.nama
     * @param foto
     * Event.foto
     * @param deskripsi
     * Event.deskripsi
     */
    public Event(int id,String nama, ImageIcon foto, String deskripsi){
        this.id = id;
        this.nama=nama;
        this.foto=foto;
        this.deskripsi=deskripsi;
    }

    /**
     * Digunakan saat menambahkan Event
     * @param nama
     * Event.nama
     * @param foto
     * Event.foto
     * @param deskripsi
     * Event.deskripsi
     */
    public Event(String nama, ImageIcon foto, String deskripsi){
        this.nama=nama;
        this.foto=foto;
        this.deskripsi=deskripsi;
    }

    public void setFoto(ImageIcon foto) {
        this.foto = foto;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public ImageIcon getFoto() {
        return foto;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}