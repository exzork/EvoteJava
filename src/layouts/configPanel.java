package layouts;

import tools.Config;
import codes.User;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static javax.swing.JOptionPane.showMessageDialog;

public class configPanel {
    private JTextField hostDatabase;
    private JTextField portDatabase;
    private JTextField usernameDatabase;
    private JPasswordField passwordDatabase;
    private JTextField npmAdmin;
    private JTextField namaAdmin;
    private JPasswordField passwordAdmin;
    private JTextField hostSMTP;
    private JTextField portSMTP;
    private JTextField usernameSMTP;
    private JCheckBox enableSSL;
    private JPasswordField passwordSMTP;
    private JButton testKirimEmailKeButton;
    private JButton testKoneksiKeDatabaseButton;
    private JButton simpanAdminKeDatabaseButton;
    private JTextField namaDatabase;
    private JTextField statusDatabase;
    private JTextField statusSMTP;
    private JPanel configPanel;
    private JButton simpanButton;
    private JTextArea formatSMTP;
    private JTextField emailAdmin;
    private JPasswordField ulangPasswordAdmin;
    private JTextField subjectSMTP;
    private JComboBox lnfComboBox;
    private static JFrame configFrame;

    /**
     * Menyimpan config saat ini
     */
    private void saveConfig(){

        //lnf
        Config.PREF.put(Config.LOOK_N_FEEL,lnfComboBox.getSelectedItem().toString());

        //database
        Config.PREF.put(Config.HOST_DB,hostDatabase.getText());
        Config.PREF.put(Config.PORT_DB,portDatabase.getText());
        Config.PREF.put(Config.NAME_DB,namaDatabase.getText());
        Config.PREF.put(Config.USER_DB,usernameDatabase.getText());
        Config.PREF.put(Config.PASS_DB,String.valueOf(passwordDatabase.getPassword()));
        Config.PREF.put(Config.STATUS_DB,statusDatabase.getText());

        //admin
        Config.PREF.put(Config.NPM_ADMIN,npmAdmin.getText());
        Config.PREF.put(Config.NAMA_ADMIN,namaAdmin.getText());
        Config.PREF.put(Config.EMAIL_ADMIN,emailAdmin.getText());
        Config.PREF.put(Config.PASS_ADMIN,String.valueOf(passwordAdmin.getPassword()));

        //email
        Config.PREF.put(Config.HOST_SMTP,hostSMTP.getText());
        Config.PREF.put(Config.PORT_SMTP,portSMTP.getText());
        String isSSL = (enableSSL.isSelected())?"true":"false";
        Config.PREF.put(Config.SSL_SMTP,isSSL);
        Config.PREF.put(Config.USER_SMTP,usernameSMTP.getText());
        Config.PREF.put(Config.PASS_SMTP,String.valueOf(passwordSMTP.getPassword()));
        Config.PREF.put(Config.SUBJECT_SMTP,subjectSMTP.getText());
        Config.PREF.put(Config.FORMAT_SMTP,formatSMTP.getText());
        Config.PREF.put(Config.STATUS_SMTP,statusSMTP.getText());

        Config.loadIcon();
    }

    /**
     * Memuat config
     */
    private void loadConfig(){
        //Memuat icon
        testKoneksiKeDatabaseButton.setIcon(Config.CONNECTED_32);
        simpanAdminKeDatabaseButton.setIcon(Config.SAVE_32);
        testKirimEmailKeButton.setIcon(Config.SEND_32);
        simpanButton.setIcon(Config.SAVE_64);

        //lnf
        lnfComboBox.setSelectedItem(Config.PREF.get(Config.LOOK_N_FEEL,""));
        //database
        hostDatabase.setText(Config.PREF.get(Config.HOST_DB,""));
        portDatabase.setText(Config.PREF.get(Config.PORT_DB,""));
        namaDatabase.setText(Config.PREF.get(Config.NAME_DB,""));
        usernameDatabase.setText(Config.PREF.get(Config.USER_DB,""));
        passwordDatabase.setText(Config.PREF.get(Config.PASS_DB,""));
        statusDatabase.setText(Config.PREF.get(Config.STATUS_DB,""));

        //admin
        npmAdmin.setText(Config.PREF.get(Config.NPM_ADMIN,""));
        namaAdmin.setText(Config.PREF.get(Config.NAMA_ADMIN,""));
        emailAdmin.setText(Config.PREF.get(Config.EMAIL_ADMIN,""));
        passwordAdmin.setText(Config.PREF.get(Config.PASS_ADMIN,""));
        ulangPasswordAdmin.setText(Config.PREF.get(Config.PASS_ADMIN,""));

        //email
        hostSMTP.setText(Config.PREF.get(Config.HOST_SMTP,"smtp.gmail.com"));
        portSMTP.setText(Config.PREF.get(Config.PORT_SMTP,"465"));
        enableSSL.setSelected(Config.PREF.get(Config.SSL_SMTP, "true").equals("true"));
        usernameSMTP.setText(Config.PREF.get(Config.USER_SMTP,""));
        passwordSMTP.setText(Config.PREF.get(Config.PASS_SMTP,""));
        subjectSMTP.setText(Config.PREF.get(Config.SUBJECT_SMTP,""));
        formatSMTP.setText(Config.PREF.get(Config.FORMAT_SMTP,"<nama>=Nama , <npm>=NPM, <password>=Password"));
        statusSMTP.setText(Config.PREF.get(Config.STATUS_SMTP,""));
    }

    public configPanel(){
        loadConfig();
        User user = new User();
        testKoneksiKeDatabaseButton.addActionListener(e -> {
           String hasilTest = Config.testKoneksiDatabase(hostDatabase.getText(),portDatabase.getText(),namaDatabase.getText(),usernameDatabase.getText(),String.valueOf(passwordDatabase.getPassword()));
            if(hasilTest.equals("Sukses")){
                statusDatabase.setText("Terkoneksi");
                Config.PREF.put(Config.HOST_DB,hostDatabase.getText());
                Config.PREF.put(Config.PORT_DB,portDatabase.getText());
                Config.PREF.put(Config.NAME_DB,namaDatabase.getText());
                Config.PREF.put(Config.USER_DB,usernameDatabase.getText());
                Config.PREF.put(Config.PASS_DB,String.valueOf(passwordDatabase.getPassword()));
                Config.PREF.put(Config.STATUS_DB,statusDatabase.getText());
            }else{
                statusDatabase.setText("Tidak Terkoneksi");
            }
            showMessageDialog(configPanel,hasilTest);
        });

        simpanAdminKeDatabaseButton.addActionListener(e -> {
            if(npmAdmin.getText().isEmpty()||namaAdmin.getText().isBlank()||emailAdmin.getText().isBlank()||passwordAdmin.getPassword().length<=0){
                showMessageDialog(configPanel,"Mohon isi data Admin");
            }else{
                if(String.valueOf(passwordAdmin.getPassword()).equals(String.valueOf(ulangPasswordAdmin.getPassword()))) {
                    user.setNpm(npmAdmin.getText());
                    user.setNama(namaAdmin.getText());
                    user.setEmail(emailAdmin.getText());
                    showMessageDialog(configPanel, Config.saveAdminToDatabase(user, String.valueOf(passwordAdmin.getPassword())));
                }else{
                    showMessageDialog(configPanel,"Password tidak sama.");
                }
            }
        });

        testKirimEmailKeButton.addActionListener(e -> {
            String isSSL = "";
            if (!enableSSL.isSelected())
                isSSL="false";
            else
                isSSL="true";
            String testSend = Config.testSendEmail(user,String.valueOf(passwordAdmin.getPassword()),hostSMTP.getText(),portSMTP.getText(),isSSL,usernameSMTP.getText(),String.valueOf(passwordSMTP.getPassword()),subjectSMTP.getText(),formatSMTP.getText());
            if(testSend.equals("Sukses")){
                statusSMTP.setText("Terkoneksi");
            }else{
                statusSMTP.setText("Tidak Terkoneksi");
            }
            showMessageDialog(configPanel,testSend);
        });

        simpanButton.addActionListener(e -> {
            if(statusSMTP.getText().equals("Terkoneksi")&&statusDatabase.getText().equals("Terkoneksi")){
                saveConfig();
                showMessageDialog(configPanel,"Konfigurasi Berhasil Disimpan");
                loginForm lf = new loginForm();
                lf.showForm();
                configFrame.dispose();
            }else{
                showMessageDialog(configPanel,"Database dan SMTP Status harus dalam keadaan terkoneksi");
            }
        });

        lnfComboBox.addActionListener(e -> {
            Config.PREF.put(Config.LOOK_N_FEEL,lnfComboBox.getSelectedItem().toString());
            configFrame.dispose();
            Config.loadLnF();
            configPanel cp = new configPanel();
            cp.showForm();
        });
    }

    /**
     * Method untuk menampilkan panel ini
     */
    public void showForm(){
        configFrame = new JFrame("Konfigurasi Panel");
        configFrame.setContentPane(new configPanel().configPanel);
        configFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        configFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                loginForm lf = new loginForm();
                lf.showForm();
                super.windowClosing(e);
            }
        });
        configFrame.pack();
        configFrame.setVisible(true);
        configFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        configFrame.setResizable(false);
    }
}