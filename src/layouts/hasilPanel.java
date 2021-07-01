package layouts;

import tools.Util;
import codes.*;
import codes.Event;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.Map;
import java.util.Set;

public class hasilPanel {
    private JPanel panel1;
    private JLabel eventLabel;
    private JTable hasilTable;
    private DefaultTableModel hasilModel = new DefaultTableModel(){
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return Pemilihan.class;
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private static JFrame hasilFrame;

    public hasilPanel(){}

    public hasilPanel(Event event){
        eventLabel.setText(event.getNama());
        hasilTable.setAutoCreateRowSorter(false);
        hasilTable.setFillsViewportHeight(true);
        hasilTable.setTableHeader(null);
        hasilTable.setCellSelectionEnabled(true);
        hasilTable.setDefaultRenderer(Pemilihan.class, new hasilCellRenderer());
        hasilTable.setRowHeight(400);
        hasilModel.addColumn("");
        Set<Map.Entry<Integer,Pemilihan>> pemilihanSet = event.daftarPemilihan.entrySet();
        for(Map.Entry<Integer,Pemilihan> pemilihanEntry:pemilihanSet){
            Pemilihan pemilihan = pemilihanEntry.getValue();
            if (pemilihan.paslonList.size()!=0){
                hasilModel.addRow(new Object[]{pemilihan});
            }
        }
        hasilTable.setModel(hasilModel);
    }

    /**
     * Menampilkan hasil event
     * @param event
     * event yang ingin hasilnya ditampilkan
     */
    public void showForm(Event event) {
        hasilFrame = new JFrame("Hasil Pemilihan");
        hasilFrame.setContentPane(new hasilPanel(event).panel1);
        hasilFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        hasilFrame.pack();
        hasilFrame.setVisible(true);
        hasilFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        hasilFrame.setResizable(false);
    }

    /**
     * CellRenderer untuk menkostumisasi table hasil pemilihan
     */
    public class hasilCellRenderer implements TableCellRenderer{

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Pemilihan pemilihan = (Pemilihan) value;

            JPanel panelRow = new JPanel();
            panelRow.setLayout(new BorderLayout());
            panelRow.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

            JLabel namaPemilihan = new JLabel();
            namaPemilihan.setText(pemilihan.getNama());
            namaPemilihan.setFont(new Font("VERDANA",Font.PLAIN,18));
            namaPemilihan.setHorizontalAlignment(SwingConstants.CENTER);

            JPanel panelCalonRow = new JPanel();
            GridLayout gridLayout = new GridLayout();
            gridLayout.setColumns(4);
            panelCalonRow.setLayout(gridLayout);

            Set<Map.Entry<Integer, Paslon>> paslonSet = pemilihan.paslonList.entrySet();
            for(Map.Entry<Integer,Paslon> paslonEntry : paslonSet){
                Paslon paslon = paslonEntry.getValue();
                JLabel namaKetua = new JLabel("Ketua : "+paslon.getKetua().getNama());
                JLabel namaWakil;
                try{
                    namaWakil = new JLabel("Wakil : "+paslon.getWakil().getNama());
                }catch (Exception ex){
                    namaWakil = new JLabel("Wakil : -");
                }
                JLabel suaraPaslon = new JLabel("Perolehan Suara : "+paslon.getSuara());

                JPanel panelCalonColumn = new JPanel();
                panelCalonColumn.setLayout(new BorderLayout());
                panelCalonColumn.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

                JLabel imagePaslon = new JLabel();
                imagePaslon.setIcon(Util.scaleImage(paslon.getFoto(),400,250));

                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new BoxLayout(infoPanel,BoxLayout.Y_AXIS));
                infoPanel.add(namaKetua);
                infoPanel.add(namaWakil);
                infoPanel.add(suaraPaslon);

                panelCalonColumn.add(imagePaslon,BorderLayout.PAGE_START);
                panelCalonColumn.add(infoPanel,BorderLayout.PAGE_END);

                panelCalonRow.add(panelCalonColumn);
            }

            panelRow.add(namaPemilihan,BorderLayout.PAGE_START);
            panelRow.add(panelCalonRow,BorderLayout.PAGE_END);
            return panelRow;
        }
    }
}