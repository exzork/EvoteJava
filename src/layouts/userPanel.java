package layouts;

import tools.Config;
import tools.Util;
import codes.*;
import codes.Event;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;

import static javax.swing.JOptionPane.showMessageDialog;

public class userPanel {
    private JTabbedPane tabbedPane1;
    private JPanel userPanel;
    private JTable eventTable;
    private JButton nextButton;
    private JButton beforeButton;
    private JTable paslonTable ;
    private JLabel pemilihanLabel;
    private JButton simpanButton;
    private JButton logOutButton;
    private JLabel namaLabel;
    private JLabel npmLabel;
    private static JFrame userFrame;
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
    private DefaultTableModel paslonModel = new DefaultTableModel(){
        @Override
        public Class<?> getColumnClass(int column){
            return Paslon.class;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if(Evote.eventList.get(getSelectedIdEvent()).daftarPemilihan.get(indexPemilihan.get(currentPemilihanIndex).getId()).paslonList!=null&&rowIndex!=-1&&columnIndex!=-1){
                Set<Map.Entry<Integer,Paslon>> paslonSet = Evote.eventList.get(getSelectedIdEvent()).daftarPemilihan.get(indexPemilihan.get(currentPemilihanIndex).getId()).paslonList.entrySet();
                ArrayList<Paslon> paslons = new ArrayList<>();
                for(Map.Entry<Integer,Paslon> paslonEntry : paslonSet){
                    paslons.add(paslonEntry.getValue());
                }
                int indexPaslon = (rowIndex*4)+columnIndex;
                try {
                    return paslons.get(indexPaslon);
                }catch (Exception ex){
                    return null;
                }
            }
            return null;
        }
    };
    private ArrayList<Pemilihan> indexPemilihan = new ArrayList<>();
    private int currentPemilihanIndex;
    private HashMap<Integer,Paslon> selectionUser = new HashMap<>();


    private void loadEventTable(){
        eventModel.setRowCount(0);
        Set<Map.Entry<Integer, Event>> eventSet= Evote.eventList.entrySet();
        for (Map.Entry<Integer,Event> eventEntry:eventSet){
            Event ev = eventEntry.getValue();
            if(ev.isEnable())
                eventModel.addRow(new Object[]{ev.getId(),ev.getNama(), Util.scaleImage(ev.getFoto(),400,300),ev.getDeskripsi()});
        }
        eventTable.setModel(eventModel);
    }
    private void loadPaslonTable(int idEvent,int idPemilihan){
        paslonModel.setRowCount(0);
        Set<Map.Entry<Integer,Paslon>> paslonSet = Evote.eventList.get(idEvent).daftarPemilihan.get(idPemilihan).paslonList.entrySet();
        int x =0;
        Object rowdata[] = new Object[4];
        for (Map.Entry<Integer,Paslon> paslonEntry:paslonSet){
            Paslon paslon = paslonEntry.getValue();
            if(x<4){
                rowdata[x] = paslon;
                x++;
            }
            if (x==3){
                paslonModel.addRow(rowdata);
                x=0;
            }
        }
        if(x<4&&rowdata[0]!=null){
            paslonModel.addRow(rowdata);
            x=0;
        }
        paslonTable.setModel(paslonModel);
        pemilihanLabel.setText(Evote.eventList.get(idEvent).daftarPemilihan.get(idPemilihan).getNama());
    }

    private int getSelectedIdEvent(){
        return Integer.parseInt(eventModel.getValueAt(eventTable.getSelectedRow(),0).toString());
    }

    private Paslon getSelectedPaslon(){
        return (Paslon) paslonModel.getValueAt(paslonTable.getSelectedRow(),paslonTable.getSelectedColumn());
    }

    public userPanel(){
    }

    public userPanel(User user){
        //Memuat icon
        logOutButton.setIcon(Config.LOGOUT_32);
        beforeButton.setIcon(Config.LEFT_32);
        nextButton.setIcon(Config.RIGHT_32);
        simpanButton.setIcon(Config.CHECKMARK_32);

        Evote.loadEventData(user,1);

        namaLabel.setText(user.getNama());
        npmLabel.setText(user.getNpm());

        eventTable.setAutoCreateRowSorter(true);
        eventTable.setFillsViewportHeight(true);
        eventModel.addColumn("ID Event");
        eventModel.addColumn("Nama");
        eventModel.addColumn("Foto");
        eventModel.addColumn("Deskripsi Event");
        eventTable.setRowHeight(300);
        loadEventTable();

        paslonTable.setAutoCreateRowSorter(false);
        paslonTable.setFillsViewportHeight(true);
        paslonTable.setTableHeader(null);
        paslonTable.setCellSelectionEnabled(true);
        paslonTable.setDefaultRenderer(Paslon.class,new PaslonCellRenderer());
        paslonTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        paslonModel.addColumn("");
        paslonModel.addColumn("");
        paslonModel.addColumn("");
        paslonModel.addColumn("");
        paslonTable.setModel(paslonModel);
        paslonTable.setRowHeight(400);


        logOutButton.addActionListener(e -> {
            loginForm lf = new loginForm();
            lf.showForm();
            userFrame.dispose();
        });

        tabbedPane1.addChangeListener(e -> {
            if(tabbedPane1.getSelectedIndex()==1){
                if(eventTable.getSelectionModel().isSelectionEmpty()){
                    showMessageDialog(userPanel,"Silahkan pilih salah satu event terlebih dahulu");
                    tabbedPane1.setSelectedIndex(0);
                }else{
                    indexPemilihan.clear();
                    Set<Map.Entry<Integer,Pemilihan>> pemilihanSet = Evote.eventList.get(getSelectedIdEvent()).daftarPemilihan.entrySet();
                    for (Map.Entry<Integer,Pemilihan> pemilihanEntry : pemilihanSet){
                        Pemilihan pemilihan = pemilihanEntry.getValue();
                        if(pemilihan.paslonList.size()>0){
                            indexPemilihan.add(pemilihan);
                        }
                    }
                    loadPaslonTable(getSelectedIdEvent(),indexPemilihan.get(0).getId());
                    currentPemilihanIndex=0;
                }
            }
        });

        nextButton.addActionListener(e -> {
            if(indexPemilihan.size()-1>currentPemilihanIndex){
                if(getSelectedPaslon()!=null){
                    selectionUser.put(indexPemilihan.get(currentPemilihanIndex).getId(),getSelectedPaslon());
                    currentPemilihanIndex++;
                    loadPaslonTable(getSelectedIdEvent(),indexPemilihan.get(currentPemilihanIndex).getId());
                }else{
                    showMessageDialog(userPanel,"Calon yang anda pilih kosong");
                }
            }else{
                showMessageDialog(userPanel,"Tidak ada pemilihan lain sesudah pemilihan ini");
            }
        });

        beforeButton.addActionListener(e -> {
            if (currentPemilihanIndex>0){
                if(getSelectedPaslon()!=null){
                    selectionUser.put(indexPemilihan.get(currentPemilihanIndex).getId(),getSelectedPaslon());
                    currentPemilihanIndex--;
                    loadPaslonTable(getSelectedIdEvent(),indexPemilihan.get(currentPemilihanIndex).getId());
                }else{
                    showMessageDialog(userPanel,"Calon yang anda pilih kosong");
                }
            }else{
                showMessageDialog(userPanel,"Tidak ada pemilihan lain sebelum pemilihan ini");
            }
        });

        simpanButton.addActionListener(e -> {
            if(getSelectedPaslon()!=null){
                selectionUser.put(indexPemilihan.get(currentPemilihanIndex).getId(),getSelectedPaslon());
                if (selectionUser.size()!=indexPemilihan.size()){
                    showMessageDialog(userPanel,"Ada pemilihan yang belum anda pilih");
                }else{
                    if(user.simpanPilihan(selectionUser)){
                        showMessageDialog(userPanel,"Pilihan anda disimpan, terimakasih telah menggunakan suara anda pada event pemilihan ini");
                        eventModel.setRowCount(0);
                        paslonModel.setRowCount(0);
                        Evote.loadEventData(user,1);
                        tabbedPane1.setSelectedIndex(0);
                    }else{
                        showMessageDialog(userPanel,"Gagal menyimpan pilihan");
                    }
                }
            }else{
                showMessageDialog(userPanel,"Calon yang anda pilih kosong");
            }
        });
    }

    public void showForm(User user){
        userFrame = new JFrame("User Panel");
        userFrame.setContentPane(new userPanel(user).userPanel);
        userFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        userFrame.pack();
        userFrame.setVisible(true);
        userFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        userFrame.setResizable(false);
    }

    public class PaslonCellRenderer implements TableCellRenderer{
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Paslon paslon = (Paslon) value;

            JLabel imagePaslon = new JLabel();
            JLabel ketuaPaslon = new JLabel();
            JLabel wakilPaslon = new JLabel();
            JLabel visiPaslon = new JLabel();
            JLabel misiPaslon = new JLabel();
            JPanel panel = new JPanel();
            JPanel centerPanel = new JPanel();

            if(paslon!=null){
                imagePaslon.setIcon(Util.scaleImage(paslon.getFoto(),400,250));
                ketuaPaslon.setText("Ketua : "+paslon.getKetua().getNama());
                if(paslon.getWakil()==null)
                    wakilPaslon.setText("Wakil Ketua : -");
                else
                    wakilPaslon.setText("Wakil Ketua : "+paslon.getWakil().getNama());
                visiPaslon.setText("Visi : "+paslon.getVisi());
                misiPaslon.setText("Misi : "+paslon.getMisi());
                centerPanel.setLayout(new BoxLayout(centerPanel,BoxLayout.Y_AXIS));
                centerPanel.add(ketuaPaslon);
                centerPanel.add(wakilPaslon);
                centerPanel.add(visiPaslon);
                centerPanel.add(misiPaslon);
                panel.setLayout(new BorderLayout());
                panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
                panel.add(imagePaslon,BorderLayout.PAGE_START);
                panel.add(centerPanel,BorderLayout.PAGE_END);
            }
            if(isSelected){
                centerPanel.setBackground(Config.SELECTED);
                panel.setBackground(Config.SELECTED);
            }else{
                centerPanel.setBackground(table.getBackground());
                panel.setBackground(table.getBackground());
            }
            return panel;
        }
    }
}