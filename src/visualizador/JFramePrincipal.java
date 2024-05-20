package visualizador;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import AgenteVisualizador;
import JFrameSeleccionar;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class JFramePrincipal extends JFrame {

    private JPanel contentPane;
    JFrameSeleccionar ventana;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    JFramePrincipal frame = new JFramePrincipal();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public JFramePrincipal() {}
    //Definimos un constructor alternativo en el que pasamos la referencia al AgenteUsuario como parámetro
    public JFramePrincipal(AgenteUsuario agente) {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent arg0) {
                int selec = JOptionPane.showOptionDialog(null, "El agente de visualización finalizará. Está seguro de cerrar la ventana?", "Salir
                de la aplicación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (selec == JOptionPane.YES_OPTION){
                    dispose();
                }
                else
                if (selec == JOptionPane.NO_OPTION){
                    selec = 0;
                }
            }
        });
        setTitle("Agente de Visualización");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 868, 670);
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu mnArchivo = new JMenu("Archivo");
        menuBar.add(mnArchivo);
        JMenuItem mntmNuevaPrediccion = new JMenuItem("Nueva Prediccion");
        mntmNuevaPrediccion.addActionListener(new ActionListener() {
            //Cuando se pulsa el elemento del menú NuevaPrediccion se crea una nueva ventana JFrameSeleccionar
            public void actionPerformed(ActionEvent e) {
                ventana = new JFrameSeleccionar(agente);
                ventana.setVisible(true);    
            }
        });
        mnArchivo.add(mntmNuevaPrediccion);
        JMenuItem mntmSalir = new JMenuItem("Salir");
        mntmSalir.addActionListener(new ActionListener(){
            //Cuando se pulsa en el elemento del menú Salir, sacamos un JOptionPane al usuario para que decida si se cierra la ventana o no
            public void actionPerformed(ActionEvent arg0){
                int seleccion = JOptionPane.showOptionDialog(null, "El agente de usuario finalizará. Está seguro de cerrar la ventana?",
                "Salir de la aplicación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (seleccion == 0){
                    dispose();
                    agente.doDelete();
                }
            }
        });
        mnArchivo.add(mntmSalir);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
    }

}