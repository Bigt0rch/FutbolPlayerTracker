package visualizador;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import datosFutbol.GeneralPlayerDataX90;
import datosFutbol.GoalkeeperPlayerDataX90;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;

//import AgenteVisualizador;
//import JFrameSeleccionar;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class JFramePrincipal extends JFrame {

    private JPanel contentPane;
    JFrameSeleccionar ventana;
    
    private static JTextArea textArea;

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
    //Definimos un constructor alternativo en el que pasamos la referencia al AgenteVisualizador como parámetro
    public JFramePrincipal(AgenteVisualizador agente) {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent arg0) {
                int selec = JOptionPane.showOptionDialog(null, "¿Desea salir de la aplicación?",
                		"Salir de la aplicación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (selec == JOptionPane.YES_OPTION){
                    dispose();
                }
                else
                if (selec == JOptionPane.NO_OPTION){
                    selec = 0;
                }
            }
        });
        setTitle("*** *** *** *** ***");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 868, 670);
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu mnArchivo = new JMenu("Menu");
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
        
     // Crear botones para cada opción
        JButton btnJugadorGeneral = new JButton("Jugador general");
        JButton btnCompararJugadores = new JButton("Comparar jugadores");
        JButton btnDatosEquipo = new JButton("Datos de un equipo");
        JButton btnCorrelaciones = new JButton("Correlaciones");
        JButton btnClustering = new JButton("Clustering");

        // Crear un JPanel para organizar los componentes
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(600, 600));

        // Crear un JPanel para los botones y añadir los botones
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5));
        buttonPanel.add(btnJugadorGeneral);
        buttonPanel.add(btnCompararJugadores);
        buttonPanel.add(btnDatosEquipo);
        buttonPanel.add(btnCorrelaciones);
        buttonPanel.add(btnClustering);

        // Añadir el JPanel de botones al JPanel principal
        panel.add(buttonPanel, BorderLayout.NORTH);

     // Crear un JTextArea y un JScrollPane para los resultados
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false); // Hacer que el JTextArea no sea editable

        // Añadir el mensaje de bienvenida
        textArea.append("Bienvenido, para continuar pulse algún botón.\n");

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 600));

        // Añadir el JScrollPane al JPanel principal
        panel.add(scrollPane, BorderLayout.CENTER);

        // Añadir el panel al contentPane de JFramePrincipal
        contentPane.setLayout(new BorderLayout());
        contentPane.add(panel, BorderLayout.CENTER);

        // Añadir ActionListeners a los botones para actualizar el JTextArea
        btnJugadorGeneral.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textArea.append("Has seleccionado: Jugador general\n");

                // Leer nombres de equipos desde un archivo .txt
                java.util.List<String> equipos = new ArrayList<>();
                System.out.println("El directorio de trabajo actual es: " + System.getProperty("user.dir"));
                try (BufferedReader br = new BufferedReader(new InputStreamReader(
                		new FileInputStream("./src/visualizador/Equipos.txt")))) {
                    String linea;
                    while ((linea = br.readLine()) != null) {
                        equipos.add(linea);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                // Convertir la lista de equipos a un array para el JComboBox
                String[] equiposArray = equipos.toArray(new String[0]);

                // Crear un JComboBox con los nombres de los equipos
                JComboBox<String> equipoComboBox = new JComboBox<>(equiposArray);

                // Mostrar el JComboBox en un JOptionPane para seleccionar el equipo
                int equipoSeleccionado = JOptionPane.showConfirmDialog(null, equipoComboBox, "Seleccione el equipo", JOptionPane.OK_CANCEL_OPTION);
                if (equipoSeleccionado == JOptionPane.OK_OPTION) {
                    String equipo = (String) equipoComboBox.getSelectedItem();

                    // Mostrar el equipo seleccionado en el JTextArea
                    textArea.append("Equipo: " + equipo + "\n");

                    // Asumimos que tienes otro archivo con nombres de jugadores
                    java.util.List<String> jugadores = new ArrayList<>();
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(
                    		new FileInputStream("./src/visualizador/Jugadores.txt")))) {
                        String linea;
                        while ((linea = br.readLine()) != null) {
                            jugadores.add(linea);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    // Convertir la lista de jugadores a un array para el JComboBox
                    String[] jugadoresArray = jugadores.toArray(new String[0]);

                    // Crear un JComboBox con los nombres de los jugadores
                    JComboBox<String> jugadorComboBox = new JComboBox<>(jugadoresArray);

                    // Mostrar el JComboBox en un JOptionPane para seleccionar el jugador
                    int jugadorSeleccionado = JOptionPane.showConfirmDialog(null, jugadorComboBox, "Seleccione el jugador", JOptionPane.OK_CANCEL_OPTION);
                    if (jugadorSeleccionado == JOptionPane.OK_OPTION) {
                        String nombre = (String) jugadorComboBox.getSelectedItem();

                        // Mostrar el nombre seleccionado en el JTextArea
                        textArea.append("Nombre: " + nombre + "\n");

                        // Crear el mensaje para enviar al AgenteVisualizador
                        Mensajes mensajeAEnviar = new Mensajes(1, nombre, equipo, null, null, null, null);

                        // Despertar al AgenteVisualizador
                        agente.doWake();
                    }
                }
            }
        });
        
        /*btnJugadorGeneral.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	textArea.append("Has seleccionado: Jugador general\n");
            	// Pedir al usuario que ingrese el nombre y el equipo
                String nombre = JOptionPane.showInputDialog(null, "Ingrese el nombre del jugador:");
                String equipo = JOptionPane.showInputDialog(null, "Ingrese el equipo del jugador:");
                
                // Mostrar los datos ingresados en el JTextArea
                textArea.append("Nombre: " + nombre + "\n");
                textArea.append("Equipo: " + equipo + "\n");
                Mensajes mensajeAEnviar = new Mensajes(1,nombre,equipo,null,null,null,null);
                //Despertamos al AgenteVisualizador
                agente.doWake();
            }
        });*/
        
        btnCompararJugadores.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	textArea.append("Has seleccionado: Comparar jugadores\n");
            	// Pedir al usuario que ingrese nombres y el equipos
                String nombre1 = JOptionPane.showInputDialog(null, "Ingrese el nombre del jugador 1:");
                String equipo1 = JOptionPane.showInputDialog(null, "Ingrese el equipo del jugador 1:");
                String nombre2 = JOptionPane.showInputDialog(null, "Ingrese el nombre del jugador 2:");
                String equipo2 = JOptionPane.showInputDialog(null, "Ingrese el equipo del jugador 2:");
                
                // Mostrar los datos ingresados en el JTextArea
                textArea.append("Nombre 1: " + nombre1 + "\n");
                textArea.append("Equipo 1: " + equipo1 + "\n");
                textArea.append("Nombre 2: " + nombre2 + "\n");
                textArea.append("Equipo 2: " + equipo2 + "\n");
                Mensajes mensajeAEnviar = new Mensajes(2,nombre1,equipo1,nombre2,equipo2,null,null);
                //Despertamos al AgenteVisualizador
                agente.doWake();
            }
        });
        
        btnDatosEquipo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	textArea.append("Has seleccionado: Datos de un equipo\n");
            }
        });
        
        btnCorrelaciones.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	textArea.append("Has seleccionado: Correlaciones\n");
            }
        });
        
        btnClustering.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	textArea.append("Has seleccionado: Clustering\n");
            }
        });
    }
    
    // Método para mostrar datos del jugador general
    public static void mostrarJugadorGeneral(GeneralPlayerDataX90 generalPlayerData) {
        textArea.append("Jugador General: \n");
        textArea.append("Goles: " + generalPlayerData.goals + "\n");
        textArea.append("Tiros: " + generalPlayerData.shots + "\n");
        textArea.append("Porcentaje de precisión en tiros: " + generalPlayerData.pShotAccuracy + "%\n");
        textArea.append("Asistencias: " + generalPlayerData.assists + "\n");
        textArea.append("Minutos jugados: " + generalPlayerData.minutes + "\n");
        textArea.append("Pases: " + generalPlayerData.passes + "\n");
        textArea.append("Pases exitosos: " + generalPlayerData.succesfull_passes + "\n");
        textArea.append("Porcentaje de precisión en pases: " + generalPlayerData.pPassAccuracy + "%\n");
        textArea.append("Duelos: " + generalPlayerData.duels + "\n");
        textArea.append("Duelos ganados: " + generalPlayerData.duels_won + "\n");
        textArea.append("Porcentaje de duelos ganados: " + generalPlayerData.pDuelAccuracy + "%\n");
        textArea.append("Faltas: " + generalPlayerData.fouls + "\n");
        textArea.append("Tarjetas amarillas: " + generalPlayerData.yellow_cards + "\n");
        textArea.append("Tarjetas rojas (por segunda amarilla): " + generalPlayerData.red_cards_2nd_yellow + "\n");
        textArea.append("Total de tarjetas rojas: " + generalPlayerData.total_red_cards + "\n");
        textArea.append("Tarjetas rojas directas: " + generalPlayerData.straight_red_cards + "\n");
        textArea.append("Regates exitosos: " + generalPlayerData.successful_dribbles + "\n");
        textArea.append("Regates fallidos: " + generalPlayerData.unsuccessful_dribbles + "\n");
        textArea.append("Porcentaje de regates exitosos: " + generalPlayerData.pDribbleAccuracy + "%\n");
    }
    
    // Método para mostrar datos del portero
    public static void mostrarJugadorPortero(GoalkeeperPlayerDataX90 gkPlayerData) {
        textArea.append("Portero: \n");
        textArea.append("Partidos jugados: " + gkPlayerData.games_played + "\n");
        textArea.append("Porterías imbatidas: " + gkPlayerData.clean_sheets + "\n");
        textArea.append("Porcentaje de porterías imbatidas: " + gkPlayerData.pClean_sheets_percentage + "%\n");
        textArea.append("Goles encajados: " + gkPlayerData.goals_conceded + "\n");
        textArea.append("Atrapes: " + gkPlayerData.catches + "\n");
        textArea.append("Porcentaje de paradas fuera del área: " + gkPlayerData.pSaves_made_from_outside_box_percentage + "%\n");
        textArea.append("Porcentaje de paradas dentro del área: " + gkPlayerData.pSaves_made_from_inside_box_percentage + "%\n");
        textArea.append("Porcentaje de precisión en atrapes: " + gkPlayerData.pCatchAccuracy + "%\n");
        textArea.append("Penales enfrentados: " + gkPlayerData.penalties_faced + "\n");
        textArea.append("Penales salvados: " + gkPlayerData.penalties_saved + "\n");
        textArea.append("Porcentaje de precisión en penales: " + gkPlayerData.pPenaltyAccuracy + "%\n");
    }
    
 // Método para comparar datos de dos porteros
    public static void compararPorteroConPortero(GoalkeeperPlayerDataX90 p1, GoalkeeperPlayerDataX90 p2) {
        textArea.append("Comparativa entre Porteros (Primer portero vs. segundo portero): \n");
        textArea.append("Partidos jugados: " + p1.games_played + " vs. " + p2.games_played + "\n");
        textArea.append("Porterías a cero: " + p1.clean_sheets + " vs. " + p2.clean_sheets + "\n");
        textArea.append("Porcentaje de porterías a cero: " + p1.pClean_sheets_percentage + "% vs. " + p2.pClean_sheets_percentage + "%\n");
        textArea.append("Goles encajados: " + p1.goals_conceded + " vs. " + p2.goals_conceded + "\n");
        textArea.append("Paradas: " + p1.catches + " vs. " + p2.catches + "\n");
        textArea.append("Porcentaje de paradas desde fuera del área: " + p1.pSaves_made_from_outside_box_percentage + "% vs. " + p2.pSaves_made_from_outside_box_percentage + "%\n");
        textArea.append("Porcentaje de paradas desde dentro del área: " + p1.pSaves_made_from_inside_box_percentage + "% vs. " + p2.pSaves_made_from_inside_box_percentage + "%\n");
        textArea.append("Porcentaje de atrapadas exitosas: " + p1.pCatchAccuracy + "% vs. " + p2.pCatchAccuracy + "%\n");
        textArea.append("Penaltis enfrentados: " + p1.penalties_faced + " vs. " + p2.penalties_faced + "\n");
        textArea.append("Penaltis parados: " + p1.penalties_saved + " vs. " + p2.penalties_saved + "\n");
        textArea.append("Porcentaje de paradas de penaltis: " + p1.pPenaltyAccuracy + "% vs. " + p2.pPenaltyAccuracy + "%\n");
    }

    /*
 // Método para comparar datos de dos jugadores generales
    public static void compararPorteroConGeneral(GoalkeeperPlayerDataX90 p1, GeneralPlayerDataX90 g2) {
        textArea.append("Comparativa entre Jugadores Generales (Primer jugador vs. segundo jugador): \n");
        textArea.append("Goles: " + g1.goals + " vs. " + g2.goals + "\n");
        textArea.append("Tiros: " + g1.shots + " vs. " + g2.shots + "\n");
        textArea.append("Porcentaje de precisión en tiros: " + g1.pShotAccuracy + "% vs. " + g2.pShotAccuracy + "%\n");
        textArea.append("Asistencias: " + g1.assists + " vs. " + g2.assists + "\n");
        textArea.append("Minutos jugados: " + g1.minutes + " vs. " + g2.minutes + "\n");
        textArea.append("Pases: " + g1.passes + " vs. " + g2.passes + "\n");
        textArea.append("Pases exitosos: " + g1.succesfull_passes + " vs. " + g2.succesfull_passes + "\n");
        textArea.append("Porcentaje de precisión en pases: " + g1.pPassAccuracy + "% vs. " + g2.pPassAccuracy + "%\n");
        textArea.append("Duelos: " + g1.duels + " vs. " + g2.duels + "\n");
        textArea.append("Duelos ganados: " + g1.duels_won + " vs. " + g2.duels_won + "\n");
        textArea.append("Porcentaje de duelos ganados: " + g1.pDuelAccuracy + "% vs. " + g2.pDuelAccuracy + "%\n");
        textArea.append("Faltas: " + g1.fouls + " vs. " + g2.fouls + "\n");
        textArea.append("Tarjetas amarillas: " + g1.yellow_cards + " vs. " + g2.yellow_cards + "\n");
        textArea.append("Tarjetas rojas (por segunda amarilla): " + g1.red_cards_2nd_yellow + " vs. " + g2.red_cards_2nd_yellow + "\n");
        textArea.append("Total de tarjetas rojas: " + g1.total_red_cards + " vs. " + g2.total_red_cards + "\n");
        textArea.append("Tarjetas rojas directas: " + g1.straight_red_cards + " vs. " + g2.straight_red_cards + "\n");
        textArea.append("Regates exitosos: " + g1.successful_dribbles + " vs. " + g2.successful_dribbles + "\n");
        textArea.append("Regates fallidos: " + g1.unsuccessful_dribbles + " vs. " + g2.unsuccessful_dribbles + "\n");
        textArea.append("Porcentaje de regates exitosos: " + g1.pDribbleAccuracy + "% vs. " + g2.pDribbleAccuracy + "%\n");
    }
    
 // Método para comparar datos de dos jugadores generales
    public static void compararGeneralConPortero(GeneralPlayerDataX90 g1, GoalkeeperPlayerDataX90 p2) {
        textArea.append("Comparativa entre Jugadores Generales (Primer jugador vs. segundo jugador): \n");
        textArea.append("Goles: " + g1.goals + " vs. " + g2.goals + "\n");
        textArea.append("Tiros: " + g1.shots + " vs. " + g2.shots + "\n");
        textArea.append("Porcentaje de precisión en tiros: " + g1.pShotAccuracy + "% vs. " + g2.pShotAccuracy + "%\n");
        textArea.append("Asistencias: " + g1.assists + " vs. " + g2.assists + "\n");
        textArea.append("Minutos jugados: " + g1.minutes + " vs. " + g2.minutes + "\n");
        textArea.append("Pases: " + g1.passes + " vs. " + g2.passes + "\n");
        textArea.append("Pases exitosos: " + g1.succesfull_passes + " vs. " + g2.succesfull_passes + "\n");
        textArea.append("Porcentaje de precisión en pases: " + g1.pPassAccuracy + "% vs. " + g2.pPassAccuracy + "%\n");
        textArea.append("Duelos: " + g1.duels + " vs. " + g2.duels + "\n");
        textArea.append("Duelos ganados: " + g1.duels_won + " vs. " + g2.duels_won + "\n");
        textArea.append("Porcentaje de duelos ganados: " + g1.pDuelAccuracy + "% vs. " + g2.pDuelAccuracy + "%\n");
        textArea.append("Faltas: " + g1.fouls + " vs. " + g2.fouls + "\n");
        textArea.append("Tarjetas amarillas: " + g1.yellow_cards + " vs. " + g2.yellow_cards + "\n");
        textArea.append("Tarjetas rojas (por segunda amarilla): " + g1.red_cards_2nd_yellow + " vs. " + g2.red_cards_2nd_yellow + "\n");
        textArea.append("Total de tarjetas rojas: " + g1.total_red_cards + " vs. " + g2.total_red_cards + "\n");
        textArea.append("Tarjetas rojas directas: " + g1.straight_red_cards + " vs. " + g2.straight_red_cards + "\n");
        textArea.append("Regates exitosos: " + g1.successful_dribbles + " vs. " + g2.successful_dribbles + "\n");
        textArea.append("Regates fallidos: " + g1.unsuccessful_dribbles + " vs. " + g2.unsuccessful_dribbles + "\n");
        textArea.append("Porcentaje de regates exitosos: " + g1.pDribbleAccuracy + "% vs. " + g2.pDribbleAccuracy + "%\n");
    }*/
    
 // Método para comparar datos de dos jugadores generales
    public static void compararGeneralConGeneral(GeneralPlayerDataX90 g1, GeneralPlayerDataX90 g2) {
        textArea.append("Comparativa entre Jugadores Generales (Primer jugador vs. segundo jugador): \n");
        textArea.append("Goles: " + g1.goals + " vs. " + g2.goals + "\n");
        textArea.append("Tiros: " + g1.shots + " vs. " + g2.shots + "\n");
        textArea.append("Porcentaje de precisión en tiros: " + g1.pShotAccuracy + "% vs. " + g2.pShotAccuracy + "%\n");
        textArea.append("Asistencias: " + g1.assists + " vs. " + g2.assists + "\n");
        textArea.append("Minutos jugados: " + g1.minutes + " vs. " + g2.minutes + "\n");
        textArea.append("Pases: " + g1.passes + " vs. " + g2.passes + "\n");
        textArea.append("Pases exitosos: " + g1.succesfull_passes + " vs. " + g2.succesfull_passes + "\n");
        textArea.append("Porcentaje de precisión en pases: " + g1.pPassAccuracy + "% vs. " + g2.pPassAccuracy + "%\n");
        textArea.append("Duelos: " + g1.duels + " vs. " + g2.duels + "\n");
        textArea.append("Duelos ganados: " + g1.duels_won + " vs. " + g2.duels_won + "\n");
        textArea.append("Porcentaje de duelos ganados: " + g1.pDuelAccuracy + "% vs. " + g2.pDuelAccuracy + "%\n");
        textArea.append("Faltas: " + g1.fouls + " vs. " + g2.fouls + "\n");
        textArea.append("Tarjetas amarillas: " + g1.yellow_cards + " vs. " + g2.yellow_cards + "\n");
        textArea.append("Tarjetas rojas (por segunda amarilla): " + g1.red_cards_2nd_yellow + " vs. " + g2.red_cards_2nd_yellow + "\n");
        textArea.append("Total de tarjetas rojas: " + g1.total_red_cards + " vs. " + g2.total_red_cards + "\n");
        textArea.append("Tarjetas rojas directas: " + g1.straight_red_cards + " vs. " + g2.straight_red_cards + "\n");
        textArea.append("Regates exitosos: " + g1.successful_dribbles + " vs. " + g2.successful_dribbles + "\n");
        textArea.append("Regates fallidos: " + g1.unsuccessful_dribbles + " vs. " + g2.unsuccessful_dribbles + "\n");
        textArea.append("Porcentaje de regates exitosos: " + g1.pDribbleAccuracy + "% vs. " + g2.pDribbleAccuracy + "%\n");
    }


}