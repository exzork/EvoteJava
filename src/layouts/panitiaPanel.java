package layouts;

import tools.Config;
import tools.Util;
import codes.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import java.util.Map;
import java.util.Set;

import static javax.swing.JOptionPane.showMessageDialog;

public class panitiaPanel {
    private JTabbedPane tabbedPane1;
    private JPanel panitiaPanel;
    private JTable eventTable;
    private JTable daftarPemilihTable;
    private JTextField npmDPT;
    private JButton tambahDPT;
    private JButton hapusDPT;
    private JLabel namaLabel;
    private JLabel npmLabel;
    public JButton logoutButton;
    private JTable pemilihanTable;
    private JButton tambahPemilihanButton;
    private JButton hapusPemilihanButton;
    private JTextField namaPemilihan;
    private JTable paslonTable;
    private JTextField npmKetua;
    private JTextField npmWakil;
    private JTextField urlFotoCalon;
    private JTextArea visiCalon;
    private JTextArea misiCalon;
    private JButton tambahPaslonButton;
    private JButton hapusPaslonButton;
    private JButton pilihFotoButton;
    private JLabel paslonFotoLabel;
    private JButton userPanelButton;
    private JButton lihatHasilPemilihanButton;
    private JButton editPemilihanButton;
    private JButton editPaslonButton;
    private JLabel addPemilihanLabel;
    private JTextField idPemilihanTextField;
    private JLabel addPaslonLabel;
    private JTextField idPaslonTextField;
    private DefaultTableModel eventModel = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
        @Override
        public Class<?> getColumnClass(int column){
            switch (column){
                case 2: return ImageIcon.class;
                default:return Object.class;
            }
        }
    };
    private DefaultTableModel pemilihanModel = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column != 0;
        }
    };
    private DefaultTableModel paslonModel = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
        @Override
        public Class<?> getColumnClass(int column){
            switch (column){
                case 3: return ImageIcon.class;
                default:return Object.class;
            }
        }
    };
    private DefaultTableModel daftarPemilihModel = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column) {
            return column!=0;
        }
    };
    private static JFrame panitiaFrame;



    //Memasukkan data yang sebelumnya telah diambil dari database ke masing masing table.
    private void loadEventTable(){
        eventModel.setRowCount(0);
        Set<Map.Entry<Integer,Event>> eventSet= Evote.eventList.entrySet();
        for (Map.Entry<Integer,Event> eventEntry:eventSet){
            Event ev = eventEntry.getValue();
            eventModel.addRow(new Object[]{ev.getId(),ev.getNama(), Util.scaleImage(ev.getFoto(),400,300),ev.getDeskripsi()});
        }
        eventTable.setModel(eventModel);
        eventTable.getColumnModel().getColumn(2).setPreferredWidth(400);
    }

    private void loadDaftarPemilihTable(int idEvent){
        daftarPemilihModel.setRowCount(0);
        Set<Map.Entry<Integer,User>> daftarPemilihSet = Evote.eventList.get(idEvent).daftarPemilih.entrySet();
        for (Map.Entry<Integer ,User> daftarPemilihEntry:daftarPemilihSet){
            User user = daftarPemilihEntry.getValue();
            int idDaftarPemilih = daftarPemilihEntry.getKey();
            try {
                daftarPemilihModel.addRow(new Object[]{idDaftarPemilih,user.getNpm(),user.getNama()});
            }catch (Exception ex){
                daftarPemilihModel.addRow(new Object[]{idDaftarPemilih,user.getNpm(),""});
            }
        }
        daftarPemilihTable.setModel(daftarPemilihModel);
    }

    private void loadPemilihanTable(int idEvent){
        pemilihanModel.setRowCount(0);
        Set<Map.Entry<Integer,Pemilihan>> pemilihanSet = Evote.eventList.get(idEvent).daftarPemilihan.entrySet();
        for(Map.Entry<Integer,Pemilihan> pemilihanEntry : pemilihanSet){
            Pemilihan pem = pemilihanEntry.getValue();
            pemilihanModel.addRow(new Object[]{pem.getId(),pem.getNama()});
        }
        pemilihanTable.setModel(pemilihanModel);
    }

    private void loadPaslonTable(int idEvent,int idPemilihan){
        paslonModel.setRowCount(0);
        Set<Map.Entry<Integer,Paslon>> paslonSet = Evote.eventList.get(idEvent).daftarPemilihan.get(idPemilihan).paslonList.entrySet();
        for (Map.Entry<Integer,Paslon> paslonEntry : paslonSet){
            Paslon paslon = paslonEntry.getValue();
            ImageIcon image = Util.scaleImage(paslon.getFoto(),400,300);
            try {
                paslonModel.addRow(new Object[]{paslon.getId(),paslon.getKetua().getNama(),paslon.getWakil().getNama(),image,paslon.getVisi(),paslon.getMisi()});
            }catch (Exception ex){
                paslonModel.addRow(new Object[]{paslon.getId(),paslon.getKetua().getNama(),"-",image,paslon.getVisi(),paslon.getMisi()});
            }
        }
        paslonTable.setModel(paslonModel);
        paslonTable.getColumnModel().getColumn(3).setPreferredWidth(400);
    }

    //Function untuk mengambil id pada masing masing table yang diselect
    private int getSelectedIdEvent(){
        return Integer.parseInt(eventModel.getValueAt(eventTable.getSelectedRow(),0).toString());
    }

    private int getSelectedIdPemilihan(){
        return Integer.parseInt(pemilihanModel.getValueAt(pemilihanTable.getSelectedRow(),0).toString());
    }

    private int getSelectedIdPaslon(){
        return Integer.parseInt(paslonModel.getValueAt(paslonTable.getSelectedRow(),0).toString());
    }

    private int getSelectedIdPemilih(){
        return Integer.parseInt(daftarPemilihModel.getValueAt(daftarPemilihTable.getSelectedRow(),0).toString());
    }

    //Default constructor
    public panitiaPanel(){


    }

    //Constructor yang dijalankan untuk menampilkan semua form
    public panitiaPanel(User user) {
        //Memuat icon
        logoutButton.setIcon(Config.LOGOUT_32);
        userPanelButton.setIcon(Config.CONTROL_PANEL_32);
        lihatHasilPemilihanButton.setIcon(Config.CHECKMARK_32);
        tambahDPT.setIcon(Config.PLUS_32);
        hapusDPT.setIcon(Config.TRASH_32);
        tambahPemilihanButton.setIcon(Config.PLUS_32);
        editPemilihanButton.setIcon(Config.EDIT_32);
        hapusPemilihanButton.setIcon(Config.TRASH_32);
        pilihFotoButton.setIcon(Config.CAMERA_32);
        tambahPaslonButton.setIcon(Config.PLUS_32);
        editPaslonButton.setIcon(Config.EDIT_32);
        hapusPaslonButton.setIcon(Config.TRASH_32);

        Evote.loadEventData(user,2);//load data dari database saat aplikasi dimulai
        Panitia panitia = new Panitia(user);

        npmLabel.setText(user.getNpm());
        namaLabel.setText(user.getNama());

        eventTable.setAutoCreateRowSorter(true);
        eventTable.setFillsViewportHeight(true);
        eventModel.addColumn("ID Event");
        eventModel.addColumn("Nama");
        eventModel.addColumn("Foto");
        eventModel.addColumn("Deskripsi Event");
        eventTable.setRowHeight(300);
        loadEventTable();//Load data event ke table


        daftarPemilihTable.setAutoCreateRowSorter(true);
        daftarPemilihTable.setFillsViewportHeight(true);
        daftarPemilihModel.addColumn("ID DPT");
        daftarPemilihModel.addColumn("NPM");
        daftarPemilihModel.addColumn("Nama");

        pemilihanTable.setAutoCreateRowSorter(true);
        pemilihanTable.setFillsViewportHeight(true);
        pemilihanModel.addColumn("ID Pemilihan");
        pemilihanModel.addColumn("Nama");

        paslonTable.setAutoCreateRowSorter(true);
        paslonTable.setFillsViewportHeight(true);
        paslonModel.addColumn("ID Paslon");
        paslonModel.addColumn("Ketua");
        paslonModel.addColumn("Wakil Ketua");
        paslonModel.addColumn("URL Foto");
        paslonModel.addColumn("Visi");
        paslonModel.addColumn("Misi");
        paslonTable.setRowHeight(300);

        //navigasi button
        logoutButton.addActionListener(e -> {
            loginForm lf = new loginForm();
            lf.showForm();
            panitiaFrame.dispose();
        });
        userPanelButton.addActionListener(e -> {
            userPanel up = new userPanel();
            up.showForm(user);
            panitiaFrame.dispose();
        });
        //Button Event
        lihatHasilPemilihanButton.addActionListener(e -> {
            if (!eventTable.getSelectionModel().isSelectionEmpty()){
                int pemilihBelumMemilih = panitia.cekDaftarPemilih(getSelectedIdEvent());
                if (pemilihBelumMemilih>0&&Evote.eventList.get(getSelectedIdEvent()).isEnable()){
                    int lanjutLihat = JOptionPane.showConfirmDialog(panitiaPanel,"Apakah anda yakin? Ada "+pemilihBelumMemilih+" pemilih yang belum menggunakan hak pilihnya. Setelah hasil tampil, pemilih tidak dapat menggunakan hak pilihnya lagi.","Warning",JOptionPane.YES_NO_OPTION);
                    if(lanjutLihat==0){
                        if(panitia.selesaikanEvent(getSelectedIdEvent())){
                            Evote.loadEventData(user,2);
                            hasilPanel hp = new hasilPanel();
                            hp.showForm(Evote.eventList.get(getSelectedIdEvent()));
                        }
                    }
                }else {
                    if(panitia.selesaikanEvent(getSelectedIdEvent())){
                        Evote.loadEventData(user,2);
                        hasilPanel hp = new hasilPanel();
                        hp.showForm(Evote.eventList.get(getSelectedIdEvent()));
                    }
                }
            }else
                showMessageDialog(panitiaPanel,"Silahkan pilih salah satu event terlebih dahulu");
        });

        //Button pemilihan
        tambahPemilihanButton.addActionListener(e -> {
            Pemilihan newPemilihan = new Pemilihan(namaPemilihan.getText());
            if(idPemilihanTextField.getText().equals("Auto")){//Tambah
                if(panitia.tambahPemilihan(getSelectedIdEvent(),newPemilihan)){
                    loadPemilihanTable(getSelectedIdEvent());
                    namaPemilihan.setText("");
                    showMessageDialog(panitiaPanel,"Berhasil menambah pemilihan.");
                }
                else {
                    showMessageDialog(panitiaPanel,"Gagal menambah pemilihan.");
                }
            }else{//Edit
                newPemilihan.setId(Integer.parseInt(idPemilihanTextField.getText()));
                if(panitia.editPemilihan(getSelectedIdEvent(),newPemilihan)){
                    namaPemilihan.setText("");
                    loadPemilihanTable(getSelectedIdEvent());
                    showMessageDialog(panitiaPanel,"Berhasil mengubah pemilihan");
                }else{
                    showMessageDialog(panitiaPanel,"Gagal mengubah pemilihan");
                }
                idPemilihanTextField.setText("Auto");
                addPemilihanLabel.setText("Tambahkan Pemilihan");
                tambahPemilihanButton.setText("Tambah");
                tambahPemilihanButton.setIcon(Config.PLUS_32);
            }
        });

        editPemilihanButton.addActionListener(e -> {
            if(!pemilihanTable.getSelectionModel().isSelectionEmpty()){
                Pemilihan editable = Evote.eventList.get(getSelectedIdEvent()).daftarPemilihan.get(getSelectedIdPemilihan());
                namaPemilihan.setText(editable.getNama());
                addPemilihanLabel.setText("Edit Pemilihan");
                Evote.eventList.get(getSelectedIdEvent()).daftarPemilihan.remove(getSelectedIdPemilihan());
                idPemilihanTextField.setText(String.valueOf(editable.getId()));
                tambahPemilihanButton.setText("Simpan");
                tambahPemilihanButton.setIcon(Config.CHECKMARK_32);
                loadPemilihanTable(getSelectedIdEvent());
            }else
                showMessageDialog(panitiaPanel,"Mohon pilih pemilihan yang akan diedit");
        });

        hapusPemilihanButton.addActionListener(e -> {
            if (panitia.hapusPemilihan(getSelectedIdEvent(),getSelectedIdPemilihan())){
                loadPemilihanTable(getSelectedIdEvent());
                showMessageDialog(panitiaPanel,"Berhasil menghapus pemilihan");
            }
            else {
                showMessageDialog(panitiaPanel,"Gagal menghapus pemilihan");
            }
        });
        //Button  paslon

        pilihFotoButton.addActionListener(e -> {
            JFileChooser jFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            jFileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", ImageIO.getReaderFileSuffixes()));
            int choose = jFileChooser.showOpenDialog(panitiaPanel);
            if (choose==JFileChooser.APPROVE_OPTION){
                paslonFotoLabel.setText(jFileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        tambahPaslonButton.addActionListener(e -> {
            User ketua = new User(npmKetua.getText());
            User wakil;
            try {
                wakil = new User(npmWakil.getText());
            }catch (Exception ex){
                wakil = null;
            }
            if(idPaslonTextField.getText().equals("Auto")){//Tambah
                if(ketua.getNpm()!=null){
                    if(npmWakil.getText().isBlank() == (wakil.getNpm()==null)){
                        ImageIcon image = new ImageIcon(paslonFotoLabel.getText());
                        Paslon paslon = new Paslon(ketua,wakil,image,visiCalon.getText(),misiCalon.getText());
                        if(image.getIconWidth()!=-1){
                            if(panitia.tambahPaslon(paslon,getSelectedIdEvent(),getSelectedIdPemilihan())){
                                loadPaslonTable(getSelectedIdEvent(),getSelectedIdPemilihan());
                                showMessageDialog(panitiaPanel,"Berhasil menambah paslon.");
                            }
                            else {
                                showMessageDialog(panitiaPanel,"Gagal menambah paslon.");
                            }
                        }else{
                            showMessageDialog(panitiaPanel, "Pastikan file yang anda pilih adalah foto");
                        }
                    }else{
                        showMessageDialog(panitiaPanel,"User tidak ditemukan NPM Wakil Ketua : "+npmWakil.getText()+", gagal menambah paslon");
                    }
                }else{
                    showMessageDialog(panitiaPanel,"User tidak ditemukan NPM Ketua : "+npmKetua.getText()+", gagal menambah paslon");
                }
            }else{//edit
                if(ketua.getNpm()!=null){
                    if(npmWakil.getText().isBlank() == (wakil.getNpm()==null)){
                        ImageIcon image = new ImageIcon(paslonFotoLabel.getText());
                        Paslon paslon = new Paslon(ketua,wakil,image,visiCalon.getText(),misiCalon.getText());
                        paslon.setId(Integer.parseInt(idPaslonTextField.getText()));
                        boolean withImage = image.getIconWidth()!=-1;
                        if(panitia.editPaslon(paslon,getSelectedIdEvent(),getSelectedIdPemilihan(),withImage)){//edit
                            loadPaslonTable(getSelectedIdEvent(),getSelectedIdPemilihan());
                            showMessageDialog(panitiaPanel,"Berhasil menambah paslon.");
                        }
                        else {
                            showMessageDialog(panitiaPanel,"Gagal menambah paslon.");
                        }
                        addPaslonLabel.setText("Tambahkan Paslon");
                        tambahPaslonButton.setText("Tambah");
                        tambahPaslonButton.setIcon(Config.PLUS_32);
                        Evote.loadEventData(user,2);
                        loadPaslonTable(getSelectedIdEvent(),getSelectedIdPemilihan());
                    }else{
                        showMessageDialog(panitiaPanel,"User tidak ditemukan NPM Wakil Ketua : "+npmWakil.getText()+", gagal menambah paslon");
                    }
                }else{
                    showMessageDialog(panitiaPanel,"User tidak ditemukan NPM Ketua : "+npmKetua.getText()+", gagal menambah paslon");
                }
            }
            npmKetua.setText("");
            npmWakil.setText("");
            paslonFotoLabel.setText("Silahkan Pilih Foto");
            visiCalon.setText("");
            misiCalon.setText("");
        });

        editPaslonButton.addActionListener(e -> {
            if(!paslonTable.getSelectionModel().isSelectionEmpty()){
                Paslon editable = Evote.eventList.get(getSelectedIdEvent()).daftarPemilihan.get(getSelectedIdPemilihan()).paslonList.get(getSelectedIdPaslon());
                npmKetua.setText(editable.getKetua().getNpm());
                npmWakil.setText(editable.getWakil().getNpm());
                paslonFotoLabel.setText("Tidak perlu memilih foto jika tidak dibutuhkan");
                visiCalon.setText(editable.getVisi());
                misiCalon.setText(editable.getMisi());
                addPaslonLabel.setText("Edit Paslon");
                tambahPaslonButton.setText("Simpan");
                tambahPaslonButton.setIcon(Config.CHECKMARK_32);
                idPaslonTextField.setText(String.valueOf(editable.getId()));
                Evote.eventList.get(getSelectedIdEvent()).daftarPemilihan.get(getSelectedIdPemilihan()).paslonList.remove(getSelectedIdPaslon());
                loadPaslonTable(getSelectedIdEvent(),getSelectedIdPemilihan());
            }else{
                showMessageDialog(panitiaPanel,"Mohon pilih paslon yang akan diedit");
            }
        });

        hapusPaslonButton.addActionListener(e -> {
            if (panitia.hapusPaslon(getSelectedIdEvent(),getSelectedIdPemilihan(),getSelectedIdPaslon())){
                loadPaslonTable(getSelectedIdEvent(),getSelectedIdPemilihan());
                showMessageDialog(panitiaPanel,"Berhasil menghapus paslon.");
            }
            else {
                showMessageDialog(panitiaPanel,"Gagak menghapus paslon.");
            }
        });

        //Button daftar pemilih
        tambahDPT.addActionListener(e -> {
            User userDPT = new User(npmDPT.getText());;
            if(userDPT.getNpm()!=null){
                if(panitia.tambahPemilih(getSelectedIdEvent(),userDPT)){
                    loadDaftarPemilihTable(getSelectedIdEvent());
                    showMessageDialog(panitiaPanel,"Berhasil menambahkan pemilih");
                }else{
                    showMessageDialog(panitiaPanel,"Gagal menambah pemilih");
                }
            }else{
                showMessageDialog(panitiaPanel,"Tidak ada user dengan NPM "+npmDPT.getText()+" , gagal menambahkan ke daftar pemilih");
            }
            npmDPT.setText("");
        });

        hapusDPT.addActionListener(e -> {
            if(panitia.hapusPemilih(getSelectedIdEvent(),getSelectedIdPemilih())){
                loadDaftarPemilihTable(getSelectedIdEvent());
                showMessageDialog(panitiaPanel,"Berhasil menghapus pemilih");
            }
        });

        //Mengecek apakah jika memilih tab dibawah sudah memilih dalam table
        tabbedPane1.addChangeListener(e -> {
            if(tabbedPane1.getSelectedIndex()==1){
                if (eventTable.getSelectionModel().isSelectionEmpty()){
                    showMessageDialog(panitiaPanel,"Silahkan pilih salah satu event terlebih dahulu");
                    tabbedPane1.setSelectedIndex(0);
                }else{
                    loadDaftarPemilihTable(getSelectedIdEvent());
                }
            }
            if(tabbedPane1.getSelectedIndex()==2){
                if(eventTable.getSelectionModel().isSelectionEmpty()){
                    showMessageDialog(panitiaPanel,"Silahkan pilih salah satu event terlebih dahulu");
                    tabbedPane1.setSelectedIndex(0);
                }
                else{
                    loadPemilihanTable(getSelectedIdEvent());
                }
            }
            if (tabbedPane1.getSelectedIndex() == 3) {
                if(pemilihanTable.getSelectionModel().isSelectionEmpty()){
                    showMessageDialog(panitiaPanel,"Silahkan pilih salah satu pemilihan terlebih dahulu");
                    tabbedPane1.setSelectedIndex(2);
                }
                else{
                    loadPaslonTable(getSelectedIdEvent(),getSelectedIdPemilihan());
                }
            }
        });

    }

    public void showForm(User user){//fungsi untuk menampilkan form
        panitiaFrame = new JFrame("Panitia Panel");
        panitiaFrame.setContentPane(new panitiaPanel(user).panitiaPanel);
        panitiaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        panitiaFrame.pack();
        panitiaFrame.setVisible(true);
        panitiaFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        panitiaFrame.setResizable(false);
    }
}