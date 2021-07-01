package codes;

import java.util.HashMap;

public class Pemilihan {
    private int id;
    private String nama;
    public HashMap<Integer,Paslon> paslonList=new HashMap<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    /**
     * Digunakan ketika memuat pemilihan dari database
     */
    public Pemilihan(int id, String nama){
        this.id = id;
        this.nama=nama;
    }

    /**
     * Digunakan ketika menambahkan pemilihan
     */
    public Pemilihan(String nama){
        this.nama=nama;
    }
}
