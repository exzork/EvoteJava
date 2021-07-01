package codes;

import tools.Database;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Paslon {
    private int id;
    private User ketua,wakil;
    private int suara=0;
    private ImageIcon foto;
    private String visi;
    private String misi;

    public Paslon(){

    }

    /**
     * Digunakan ketika menambahkan paslon
     */
    public Paslon(User ketua, User wakil,ImageIcon foto,String visi, String misi){
        this.ketua=ketua;
        this.wakil=wakil;
        this.foto = foto;
        this.visi=visi;
        this.misi=misi;
    }

    /**
     * Digunakan ketika memuat paslon dari database
     */
    public Paslon(int id, User ketua, User wakil,ImageIcon foto,String visi, String misi, int suara){
        this.id=id;
        this.ketua=ketua;
        this.wakil=wakil;
        this.foto=foto;
        this.visi=visi;
        this.misi=misi;
        this.suara=suara;
    }
    /**
     * Menambah suara , dan menyimpan kedalam database
     */
    public void terpilih(){
        String sql_terpilih = "UPDATE calon SET suara=suara+1 WHERE id=?";
        ArrayList<String> param = new ArrayList<>(Arrays.asList(String.valueOf(this.id)));
        Database.execute(sql_terpilih,param);
        suara+=1;
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

    public User getKetua() {
        return ketua;
    }

    public User getWakil() {
        return wakil;
    }

    public ImageIcon getFoto() {
        return foto;
    }

    public String getVisi(){
        return visi;
    }

    public String getMisi(){
        return misi;
    }

    public int getSuara() {
        return suara;
    }
}