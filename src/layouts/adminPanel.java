package layouts;

import tools.Config;
import tools.Util;
import codes.*;
import codes.Event;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import java.util.*;

import static javax.swing.JOptionPane.showMessageDialog;

public class adminPanel {
    private JTabbedPane tabbedPane1;
    public JPanel mainPanel;
    private JPanel eventPanel;
    private JTable eventTable;
    private JButton tambahEventButton;
    private JButton tambahUserButton;
    private JPanel userPanel;
    private JScrollPane scrollEventTable;
    private JTable userTable;
    private JScrollPane scrollUserTable;
    private JPanel eventAddPanel;
    public  JTextField namaEvent;
    public  JTextField urlFotoEvent;
    public  JTextField mulaiEvent;
    public  JTextField selesaiEvent;
    public  JTextArea deskripsiEvent;
    private JButton logoutButton;
    private JLabel namaLabel;
    private JLabel npmLabel;
    private JTextField npmUser;
    private JTextField namaUser;
    private JPanel userAddPanel;
    private JTextField emailUser;
    private JButton saveEventButton;
    private JButton eventDeleteButton;
    private JButton managePantita;
    private JPanel panitiaPanel;
    private JButton hapusPanitiaButton;
    private JButton tambahPanitiaButton;
    private JTextField npmPanitia;
    private JTextField jabatanPanitia;
    private JTable panitiaTable;
    private JButton hapusEventButton;
    private JButton hapusUserButton;
    private JButton pilihFileEventButton;
    private JLabel fileSelectedLabel;
    private JButton panitiaPanelButton;
    private JButton userPanelButton;
    private JButton editEventButton;
    private JButton editPanitiaButton;
    private JButton editUserButton;
    private JLabel addEventLabel;
    private JTextField idEventTextField;
    private JTextField idPanitiaTextField;
    private JLabel addPanitiaLabel;
    private JLabel addUserLabel;
    private JButton kirimPasswordButton;
    private JPanel addPanitiaPanel;
    private JTextField addNpmPanitia;
    private JButton addPanitiaButton;
    private JButton savePanitiaButton;
    private JButton deletePanitiaButton;
    private JTextField addJabatanPanitia;
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
    private DefaultTableModel panitiaModel = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private DefaultTableModel userModel = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private static JFrame adminFrame;

    /**
     * Memuat tabel event menggunakan data dari Evote.eventList
     */
    private void loadEventTabel(){
        eventModel.setRowCount(0);
        Set<Map.Entry<Integer,Event>> eventSet= Evote.eventList.entrySet();
        for (Map.Entry<Integer,Event> eventEntry:eventSet){
            Event ev = eventEntry.getValue();
            ImageIcon image = Util.scaleImage(ev.getFoto(),400,300);
            eventModel.addRow(new Object[]{ev.getId(),ev.getNama(),image,ev.getDeskripsi()});
        }
        eventTable.setModel(eventModel);
        eventTable.getColumnModel().getColumn(2).setPreferredWidth(400);
    }

    /**
     * Memuat tabel panitia dari Evote.eventList.daftarPanitia
     * @param idEvent
     * Event.id di panitia akan dimuat
     */
    private void loadPanitiaTabel(int idEvent){
        panitiaModel.setRowCount(0);
        Set<Map.Entry<Integer,Panitia>> panitiaSet = Evote.eventList.get(idEvent).daftarPanitia.entrySet();
        for (Map.Entry<Integer,Panitia> panitiaEntry : panitiaSet){
            Panitia panitia = panitiaEntry.getValue();
            panitiaModel.addRow(new Object[]{panitia.getId(),panitia.getUser().getNpm(),panitia.getUser().getNama(),panitia.getJabatan()});
        }
        panitiaTable.setModel(panitiaModel);
    }

    /**
     * Memuat tabel user dari Evote.userList
     */
    private void loadUserTable(){
        userModel.setRowCount(0);
        Set<Map.Entry<String ,User>> userSet = Evote.userList.entrySet();
        for (Map.Entry<String,User> userEntry : userSet){
            User user = userEntry.getValue();
            userModel.addRow(new Object[]{user.getNpm(),user.getNama(),user.getEmail()});
        }
        userTable.setModel(userModel);
    }

    /**
     * @return
     * Mendapatkan baris yang terpilih pada tabel Event dan mengembalikan ke Event.id
     */
    private int getSelectedIdEvent(){
        return Integer.parseInt(eventModel.getValueAt(eventTable.getSelectedRow(),0).toString());
    }

    /**
     * @return
     * Mendapatkan baris yang terpilih pada tabel User dan mengembalikan ke User.npm
     */
    private String getSelectedNpmUser(){
        return userModel.getValueAt(userTable.getSelectedRow(),0).toString();
    }

    /**
     * @return
     * Mendapatkan baris yang terpilih pada tabel Panitia dan mengembalikan ke Panitia.id
     */
    private int getSelectedIdPanitia(){
        return Integer.parseInt(panitiaModel.getValueAt(panitiaTable.getSelectedRow(),0).toString());
    }

    public adminPanel(){

    }
    public adminPanel(User user) {
        //Memuat icon untuk tombol
        logoutButton.setIcon(Config.LOGOUT_32);
        panitiaPanelButton.setIcon(Config.CONTROL_PANEL_32);
        userPanelButton.setIcon(Config.CONTROL_PANEL_32);
        pilihFileEventButton.setIcon(Config.CAMERA_32);
        tambahEventButton.setIcon(Config.PLUS_32);
        editEventButton.setIcon(Config.EDIT_32);
        hapusEventButton.setIcon(Config.TRASH_32);
        tambahPanitiaButton.setIcon(Config.PLUS_32);
        editPanitiaButton.setIcon(Config.EDIT_32);
        hapusPanitiaButton.setIcon(Config.TRASH_32);
        tambahUserButton.setIcon(Config.PLUS_32);
        editUserButton.setIcon(Config.EDIT_32);
        kirimPasswordButton.setIcon(Config.RESET_32);
        hapusUserButton.setIcon(Config.TRASH_32);

        Evote.loadEventData(user,3);
        Evote.loadUserData();
        Admin admin = new Admin();
        npmLabel.setText(user.getNpm());
        namaLabel.setText(user.getNama());

        eventTable.setAutoCreateRowSorter(true);
        eventTable.setFillsViewportHeight(true);
        eventTable.setRowHeight(300);
        eventModel.addColumn("ID Event");
        eventModel.addColumn("Nama");
        eventModel.addColumn("Foto");
        eventModel.addColumn("Deskripsi Event");
        loadEventTabel();

        panitiaTable.setAutoCreateRowSorter(true);
        panitiaTable.setFillsViewportHeight(true);
        panitiaModel.addColumn("ID Panitia");
        panitiaModel.addColumn("NPM");
        panitiaModel.addColumn("Nama");
        panitiaModel.addColumn("Jabatan");

        userTable.setAutoCreateRowSorter(true);
        userTable.setFillsViewportHeight(true);
        userModel.addColumn("NPM");
        userModel.addColumn("Nama");
        userModel.addColumn("Email");
        loadUserTable();


        logoutButton.addActionListener(e -> {
            loginForm lf = new loginForm();
            lf.showForm();
            adminFrame.dispose();
        });

        panitiaPanelButton.addActionListener(e -> {
            if(user.isPanitia()) {
                panitiaPanel pp = new panitiaPanel();
                pp.showForm(user);
                adminFrame.dispose();
            }else{
                showMessageDialog(mainPanel,"Anda bukan panitia event apapun.");
            }
        });

        userPanelButton.addActionListener(e -> {
            layouts.userPanel up = new userPanel();
            up.showForm(user);
            adminFrame.dispose();
        });

        //event Button
        pilihFileEventButton.addActionListener(e -> {
            JFileChooser jFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            jFileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", ImageIO.getReaderFileSuffixes()));
            int choose = jFileChooser.showOpenDialog(mainPanel);
            if (choose==JFileChooser.APPROVE_OPTION){
                fileSelectedLabel.setText(jFileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        tambahEventButton.addActionListener(e -> {
            ImageIcon imageIcon = new ImageIcon(fileSelectedLabel.getText());
            Event event = new Event(namaEvent.getText(),imageIcon,deskripsiEvent.getText());
            if(idEventTextField.getText().equals("Auto")){//Tambah Event
                if (imageIcon.getIconWidth()!=-1){
                    if(admin.tambahEvent(event)){

                        showMessageDialog(mainPanel,"Berhasil menambahkan event");
                        loadEventTabel();
                    }else
                        showMessageDialog(mainPanel,"Gagal menambahkan event");
                }else {
                    showMessageDialog(mainPanel, "Pastikan sertakan foto dan bukan file lain");
                }
            }else{//Edit Event
               boolean withImage = imageIcon.getIconWidth()!=-1;
               event.setId(Integer.parseInt(idEventTextField.getText()));
               if(admin.editEvent(event,withImage)){
                   idEventTextField.setText("Auto");

                   showMessageDialog(mainPanel,"Berhasil mengubah event");
                   Evote.loadEventData(user,3);
                   loadEventTabel();
               }else {
                   showMessageDialog(mainPanel, "Gagal mengubah event");
               }
               tambahEventButton.setIcon(Config.PLUS_32);
               tambahEventButton.setText("Tambah");
               addEventLabel.setText("Tambahkan Event");
            }
            namaEvent.setText("");
            deskripsiEvent.setText("");
            fileSelectedLabel.setText("Silahkan Pilih File");
        });

        editEventButton.addActionListener(e -> {
            if(!eventTable.getSelectionModel().isSelectionEmpty()){
                Event editable = Evote.eventList.get(getSelectedIdEvent());
                idEventTextField.setText(String.valueOf(editable.getId()));
                namaEvent.setText(editable.getNama());
                fileSelectedLabel.setText("Tidak perlu memilih foto jika tidak dibutuhkan");
                deskripsiEvent.setText(editable.getDeskripsi());
                Evote.eventList.remove(getSelectedIdEvent());
                loadEventTabel();
                tambahEventButton.setIcon(Config.CHECKMARK_32);
                tambahEventButton.setText("Simpan");
                addEventLabel.setText("Edit Event");
            }else
                showMessageDialog(mainPanel,"Silahkan pilih salah satu event terlebih dahulu");
        });

        hapusEventButton.addActionListener(e -> {
            if (admin.hapusEvent(getSelectedIdEvent())){
                showMessageDialog(mainPanel,"Berhasil menghapus event");
                loadEventTabel();
            }else{
                showMessageDialog(mainPanel,"Gagal menghapus event");
            }
        });

        //panitia Button
        tambahPanitiaButton.addActionListener(e -> {
            User newUserPanitia = new User(npmPanitia.getText());
            if(idPanitiaTextField.getText().equals("Auto")){//tambah panitia
                if(newUserPanitia.getNama()==null){
                    showMessageDialog(mainPanel,"User dengan NPM "+npmPanitia.getText()+" tidak ditemukan, tidak dapat menambah panitia");
                }else{
                    Panitia newPanitia = new Panitia(newUserPanitia);
                    newPanitia.setJabatan(jabatanPanitia.getText());
                    if(admin.tambahPanitia(getSelectedIdEvent(),newPanitia)){
                        loadPanitiaTabel(getSelectedIdEvent());
                        showMessageDialog(mainPanel,"Berhasil menambahkan panitia");
                    }else{
                        showMessageDialog(mainPanel,"Gagal menambahkan panitia");
                    }
                }
            }else{
                if(newUserPanitia.getNama()==null){
                    showMessageDialog(mainPanel,"User dengan NPM "+npmPanitia.getText()+" tidak ditemukan, tidak dapat mengubah panitia");
                }else{
                    Panitia editPanitia = new Panitia(newUserPanitia);
                    editPanitia.setJabatan(jabatanPanitia.getText());
                    editPanitia.setId(Integer.parseInt(idPanitiaTextField.getText()));
                    if(admin.editPanitia(getSelectedIdEvent(),editPanitia)){
                        loadPanitiaTabel(getSelectedIdEvent());
                        showMessageDialog(mainPanel,"Berhasil mengubah panitia");
                    }else{
                        showMessageDialog(mainPanel, "Gagal mengubah panitia");
                    }
                    addPanitiaLabel.setText("Tambahkan Panitia");
                    idPanitiaTextField.setText("Auto");
                    tambahPanitiaButton.setText("Tambah");
                    tambahPanitiaButton.setIcon(Config.PLUS_32);
                }
            }
            npmPanitia.setText("");
            jabatanPanitia.setText("");
        });

        editPanitiaButton.addActionListener(e -> {
            if(!panitiaTable.getSelectionModel().isSelectionEmpty()){
                Panitia editable = Evote.eventList.get(getSelectedIdEvent()).daftarPanitia.get(getSelectedIdPanitia());
                idPanitiaTextField.setText(String.valueOf(editable.getId()));
                npmPanitia.setText(editable.getUser().getNpm());
                jabatanPanitia.setText(editable.getJabatan());
                Evote.eventList.get(getSelectedIdEvent()).daftarPanitia.remove(getSelectedIdPanitia());
                loadPanitiaTabel(getSelectedIdEvent());
                tambahPanitiaButton.setIcon(Config.CHECKMARK_32);
                tambahPanitiaButton.setText("Simpan");
                addPanitiaLabel.setText("Edit Panitia");
            }else
                showMessageDialog(mainPanel,"Silahkan pilih salah satu panitia terlebih dahulu");
        });

        hapusPanitiaButton.addActionListener(e -> {
            if(admin.hapusPanitia(getSelectedIdEvent(),getSelectedIdPanitia())){
                showMessageDialog(mainPanel,"Berhasil menghapus panitia");
                loadPanitiaTabel(getSelectedIdEvent());
            }else{
                showMessageDialog(mainPanel,"Gagal menghapus panitia");
            }
        });

        //User Button
        tambahUserButton.addActionListener(e -> {
            User newUser = new User(npmUser.getText(),namaUser.getText(),emailUser.getText());
            if(tambahUserButton.getText().equals("Tambah")){//Tambah User
                if(admin.tambahUser(newUser)){
                    loadUserTable();
                    showMessageDialog(mainPanel,"Berhasil menambah User");
                }else
                    showMessageDialog(mainPanel,"Gagal menambah User");
            }else{
                if(admin.editUser(newUser)){
                    showMessageDialog(mainPanel,"Berhasil mengubah User");
                    loadUserTable();
                }else{
                    showMessageDialog(mainPanel, "Gagal mengubah User");
                }
                npmUser.setEnabled(true);
                npmUser.setEditable(true);
                addUserLabel.setText("Tambahkan User");
                tambahUserButton.setIcon(Config.PLUS_32);
                tambahUserButton.setText("Tambah");
            }
            npmUser.setText("");
            namaUser.setText("");
            emailUser.setText("");
        });

        editUserButton.addActionListener(e->{
            if(!userTable.getSelectionModel().isSelectionEmpty()){
                User editable = Evote.userList.get(getSelectedNpmUser());
                npmUser.setText(editable.getNpm());
                npmUser.setEditable(false);
                npmUser.setEnabled(false);
                namaUser.setText(editable.getNama());
                emailUser.setText(editable.getEmail());
                Evote.userList.remove(getSelectedNpmUser());
                loadUserTable();
                addUserLabel.setText("Edit User");
                tambahUserButton.setIcon(Config.CHECKMARK_32);
                tambahUserButton.setText("Simpan");
            }else
                showMessageDialog(mainPanel,"Mohon pilih user yang akan diubah terlebih dahulu");
        });

        kirimPasswordButton.addActionListener(e->{
            if(!userTable.getSelectionModel().isSelectionEmpty()){
                User resend = Evote.userList.get(getSelectedNpmUser());
                if(admin.resendEmail(resend)){
                    showMessageDialog(mainPanel,"Berhasil mengirimkan ulang password untuk npm "+resend.getNpm()+" ke email "+resend.getEmail());
                }else
                    showMessageDialog(mainPanel,"Gagal mengirim ulang password");
            }
        });

        hapusUserButton.addActionListener(e -> {
            if(!userTable.getSelectionModel().isSelectionEmpty()){
                if(admin.hapusUser(getSelectedNpmUser())){
                    showMessageDialog(mainPanel,"Berhasil menghapus User");
                    loadUserTable();
                }else{
                    showMessageDialog(mainPanel,"Gagal menghapus User");
                }
            }else
                showMessageDialog(mainPanel,"Mohon pilih panitia yang akan dihapus terlebih dahulu");
        });

        tabbedPane1.addChangeListener(e -> {
            if (tabbedPane1.getSelectedIndex()==1){
                if(eventTable.getSelectionModel().isSelectionEmpty()){
                    showMessageDialog(mainPanel,"Silahkan pilih salah satu event terlebih dahulu");
                    tabbedPane1.setSelectedIndex(0);
                }else{
                    loadPanitiaTabel(getSelectedIdEvent());
                }
            }
        });
    }

    /**
     * Method untuk menampilkan panel ini
     * @param user
     * User yang bisa mengakses panel ini
     */
    public void showForm(User user){
        adminFrame = new JFrame("Admin Panel");
        adminFrame.setContentPane(new adminPanel(user).mainPanel);
        adminFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        adminFrame.pack();
        adminFrame.setVisible(true);
        adminFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        adminFrame.setResizable(false);

    }
}