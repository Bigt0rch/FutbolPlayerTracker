package visualizador;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import AgenteVisualizador;
import javax.swing.JLabel;

public class JFrameSeleccionar extends JFrame {

    private JPanel contentPane;
    File fichero1;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    JFrameSeleccionar frame = new JFrameSeleccionar();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public JFrameSeleccionar(AgenteUsuario agente) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 698, 555);
        contentPane = new JPanel();
        contentPane.setBackground(Color.LIGHT_GRAY);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        JComboBox comboBox = new JComboBox();
        comboBox.setBounds(246, 93, 147, 47);
        contentPane.add(comboBox);
        comboBox.addItem("J48");
        comboBox.addItem("KNN");
        JTextArea textArea = new JTextArea();
        textArea.setBounds(33, 239, 351, 43);
        contentPane.add(textArea);
        JButton btnSeleccionar = new JButton("Seleccionar");
        btnSeleccionar.addActionListener(new ActionListener() {
            //Cuando se pulsa el botón seleccionar se lanza una ventana de apertura de ficheros (utilizando para ello la clase UtilsUI)
            public void actionPerformed(ActionEvent e) {
                String ruta_fichero ="";
                fichero1 = UtilsUI.abrirFichero(ruta_fichero);
                textArea.setText(fichero1.getPath());
            }
        });
        btnSeleccionar.setBounds(413, 251, 115, 29);
        contentPane.add(btnSeleccionar);
        JButton btnEnviar = new JButton("Enviar");
        btnEnviar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                //Al pulsar el botón Enviar se selecciona el método de clasificación del ComboBox del interaz y el fichero que se hubiese cargado
                System.out.println("Se ha pulsado el botón enviar");
                System.out.println("Combo: " + comboBox.getSelectedItem());
                agente.setMetodo(comboBox.getSelectedItem().toString());
                System.out.println("Fichero: " + textArea.getText());
                agente.setFichero(fichero1);
                //Despertamos al AgenteUsuario
                agente.doWake();
                //Cerramos la ventana
                dispose();
            }
        });
        btnEnviar.setBounds(265, 402, 115, 29);
        contentPane.add(btnEnviar);
    }

}