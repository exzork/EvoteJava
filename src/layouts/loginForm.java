package layouts;

import tools.Config;
import codes.User;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static javax.swing.JOptionPane.showMessageDialog;

public class loginForm {
    private User user=new User();
    private JPanel loginForm;
    private JTextField username;
    private JPasswordField password;
    private JButton loginbtn;
    private static JFrame loginFrame;

    public loginForm() {
        loginbtn.setIcon(Config.LOGIN_32);

        loginbtn.addActionListener(e -> {
            user.login(username.getText(),String.valueOf(password.getPassword()));
            if(user.getNpm()==null){
                showMessageDialog(loginForm,"Username atau Password Salah.");
            }else {
                if (user.isAdmin()) {
                    showMessageDialog(loginForm, "Berhasil Login sebagai admin");
                    adminPanel ap = new adminPanel();
                    ap.showForm(user);
                    loginFrame.dispose();
                } else if (user.isPanitia()) {
                    showMessageDialog(loginForm, "Berhasil Login sebagai panitia.");
                    panitiaPanel pp = new panitiaPanel();
                    pp.showForm(user);
                    loginFrame.dispose();
                }else{
                    showMessageDialog(loginForm,"Berhasil Login sebagai user");
                    userPanel up = new userPanel();
                    up.showForm(user);
                    loginFrame.dispose();
                }
            }
        });

        //open config panel using ctrl alt P
        KeyStroke configKey = KeyStroke.getKeyStroke("ctrl alt P");
        Action openPanel = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                configPanel cp = new configPanel();
                cp.showForm();
                loginFrame.dispose();
            }
        };
        InputMap im = loginForm.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        im.put(configKey,"config");
        loginForm.getActionMap().put("config",openPanel);
    }

    /**
     * Form pertama yang ditampilkan
     */
    public void showForm(){
        JFrame.setDefaultLookAndFeelDecorated(true);
        Config.loadLnF();
        loginFrame = new JFrame("Login");
        loginFrame.setContentPane(new loginForm().loginForm);
        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        loginFrame.pack();
        loginFrame.setVisible(true);
        loginFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        loginFrame.setResizable(false);
    }
}