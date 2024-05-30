package visualizador;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import datosFutbol.GeneralPlayerDataX90;
import datosFutbol.GoalkeeperPlayerDataX90;
import datosFutbol.TeamData;

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
import java.util.Arrays;

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
        JButton btnJugadorGeneral = new JButton("Jugador");
        JButton btnCompararJugadores = new JButton("Comparaciones");
        JButton btnDatosEquipo = new JButton("Equipos");
        JButton btnCorrelaciones = new JButton("Correlaciones");
        JButton btnClustering = new JButton("Clustering");
        JButton btnActualizar = new JButton("Actualizar");

        // Crear un JPanel para organizar los componentes
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(600, 600));

        // Crear un JPanel para los botones y añadir los botones
        JPanel buttonPanel = new JPanel(new GridLayout(1, 6));
        buttonPanel.add(btnJugadorGeneral);
        buttonPanel.add(btnCompararJugadores);
        buttonPanel.add(btnDatosEquipo);
        buttonPanel.add(btnCorrelaciones);
        buttonPanel.add(btnClustering);
        buttonPanel.add(btnActualizar);

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
                textArea.append("Has seleccionado: Jugador\n");

                // Leer nombres de equipos desde un archivo .txt
                java.util.List<String> equipos = new ArrayList<>();
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
        
        btnCompararJugadores.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                textArea.append("Has seleccionado: Comparar Jugadores\n");

                // Leer nombres de equipos desde un archivo .txt
                java.util.List<String> equipos = new ArrayList<>();
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

                // Mostrar el JComboBox en un JOptionPane para seleccionar el equipo1
                int equipoSeleccionado1 = JOptionPane.showConfirmDialog(null, equipoComboBox, "Seleccione el equipo del primer jugador", JOptionPane.OK_CANCEL_OPTION);
                if (equipoSeleccionado1 == JOptionPane.OK_OPTION) {
                    String equipo1 = (String) equipoComboBox.getSelectedItem();

                    // Mostrar el equipo seleccionado en el JTextArea
                    textArea.append("Equipo 1: " + equipo1 + "\n");

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
                    int jugadorSeleccionado1 = JOptionPane.showConfirmDialog(null, jugadorComboBox, "Seleccione el primer jugador", JOptionPane.OK_CANCEL_OPTION);
                    if (jugadorSeleccionado1 == JOptionPane.OK_OPTION) {
                        String nombre1 = (String) jugadorComboBox.getSelectedItem();

                        // Mostrar el nombre seleccionado en el JTextArea
                        textArea.append("Nombre 1: " + nombre1 + "\n");
                        
                     // Mostrar el JComboBox en un JOptionPane para seleccionar el equipo2
                        int equipoSeleccionado2 = JOptionPane.showConfirmDialog(null, equipoComboBox, "Seleccione el equipo del segundo jugador", JOptionPane.OK_CANCEL_OPTION);
                        if (equipoSeleccionado2 == JOptionPane.OK_OPTION) {
                        	String equipo2 = (String) equipoComboBox.getSelectedItem();

                            // Mostrar el equipo seleccionado en el JTextArea
                            textArea.append("Equipo 2: " + equipo2 + "\n");
                            
                         // Mostrar el JComboBox en un JOptionPane para seleccionar el jugador
                            int jugadorSeleccionado2 = JOptionPane.showConfirmDialog(null, jugadorComboBox, "Seleccione el segundo jugador", JOptionPane.OK_CANCEL_OPTION);
                            if (jugadorSeleccionado2 == JOptionPane.OK_OPTION) {
                                String nombre2 = (String) jugadorComboBox.getSelectedItem();

                                // Mostrar el nombre seleccionado en el JTextArea
                                textArea.append("Nombre 2: " + nombre2 + "\n");
                                
                             // Crear el mensaje para enviar al AgenteVisualizador
                                Mensajes mensajeAEnviar = new Mensajes(2, nombre1, equipo1, nombre2, equipo2, null, null);

                                // Despertar al AgenteVisualizador
                                agente.doWake();
                            }
                        }

                        
                    }
                }
            }
        });
        
        btnDatosEquipo.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                textArea.append("Has seleccionado: Datos de un equipo\n");

                // Leer nombres de equipos desde un archivo .txt
                java.util.List<String> equipos = new ArrayList<>();
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
                    
                    // Crear el mensaje para enviar al AgenteVisualizador
                    Mensajes mensajeAEnviar = new Mensajes(3, null, null, null, null, equipo, null);

                    // Despertar al AgenteVisualizador
                    agente.doWake();

                }
            }
        });
        
        btnCorrelaciones.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                textArea.append("Has seleccionado: Correlaciones\n");
                
                // Crear el mensaje para enviar al AgenteVisualizador
                Mensajes mensajeAEnviar = new Mensajes(4, null, null, null, null, null, null);
                
                // Despertar al AgenteVisualizador
                agente.doWake();

            }
        });
        
        btnClustering.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                textArea.append("Has seleccionado: Clustering\n");

                Integer[] numerosArray = {2,3,4,5,6,7,8};
                JComboBox<Integer> numerosComboBox = new JComboBox<>(numerosArray);
                int numSeleccionado = JOptionPane.showConfirmDialog(null, numerosComboBox, "Seleccione el equipo", JOptionPane.OK_CANCEL_OPTION);
                if (numSeleccionado == JOptionPane.OK_OPTION) {
                    Integer num = (Integer) numerosComboBox.getSelectedItem();

                    // Mostrar el equipo seleccionado en el JTextArea
                    textArea.append("Número de clusters a realizar: " + num + "\n");
                    
                    // Crear el mensaje para enviar al AgenteVisualizador
                    Mensajes mensajeAEnviar = new Mensajes(5, null, null, null, null, null, num);

                    // Despertar al AgenteVisualizador
                    agente.doWake();

                }
            }
        });
        
        btnActualizar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                textArea.append("Actualizando información de jugadores. Esto puede llevar varios minutos...\n");
                
                // Crear el mensaje para enviar al AgenteVisualizador
                Mensajes mensajeAEnviar = new Mensajes(6, null, null, null, null, null, null);
                
                // Despertar al AgenteVisualizador
                agente.doWake();

            }
        });
    }
    
    // Método para mostrar datos del jugador general
    public static void mostrarJugadorGeneral(GeneralPlayerDataX90 generalPlayerData) {
        textArea.append("Datos del jugador de campo cada 90 mins: \n");
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
    
 // Método para comparar datos de dos jugadores generales
    public static void compararGeneralConGeneral(GeneralPlayerDataX90 g1, GeneralPlayerDataX90 g2) {
        textArea.append("Comparativa entre Jugadores (Estadisticas cada 90 mins) (Primer jugador vs. segundo jugador): \n");
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
    
    // Método para mostrar los datos de un equipo
    public static void mostrarDatosEquipo(TeamData td) {
        textArea.append("Datos del equipo: \n");
        textArea.append("Goles: " + td.goles + "\n");
        textArea.append("Tiros a puerta: " + td.tiros_a_puerta + "\n");
        textArea.append("Goles recibidos: " + td.goles_recibidos + "\n");
        textArea.append("Tiros a puerta recibidos: " + td.tiros_a_puerta_recibidos + "\n");
        textArea.append("Pases: " + td.pases + "\n");
        textArea.append("Asistencias: " + td.asistencias + "\n");
        textArea.append("Duelos: " + td.duelos + "\n");
        textArea.append("Duelos ganados: " + td.duelos_ganados + "\n");
        textArea.append("Faltas: " + td.faltas + "\n");
        textArea.append("Tarjetas rojas directas: " + td.rojas_directas + "\n");
        textArea.append("Tarjetas rojas por segunda amarilla: " + td.rojas_por_2a_amarilla + "\n");
        textArea.append("Total de tarjetas rojas: " + td.rojas + "\n");
        textArea.append("Tarjetas amarillas: " + td.amarillas + "\n");
    }
    
 // Método para mostrar los datos de un equipo
    public static void mostrarCorrelaciones(double[] peso,double[] cargas,double[] altura,double[] duelosAereos,
    		double pesoYCargas,double pesoYAltura,double pesoYDuelosAereos,
    		double cargasYAltura,double cargasYDuelosAereos,double alturaYDuelosAereos) {
    	textArea.append("Correlaciones: \n");

    	String[] paramsArray = {"Peso","Cargas","Altura","Duelos aéreos ganados"};
        JComboBox<String> paramsComboBox = new JComboBox<>(paramsArray);
        
        double[] campo1=null;
        double[] campo2=null;
        int coefMostrado=0; // Decena representa primer parametro, unidad el segundo
        
        int param1Seleccionado = JOptionPane.showConfirmDialog(null, paramsComboBox, "Seleccione el primer parámetro", JOptionPane.OK_CANCEL_OPTION);
        if (param1Seleccionado == JOptionPane.OK_OPTION) {
            String param1 = (String) paramsComboBox.getSelectedItem();
            textArea.append("Eje X: " + param1 + "\n");
            if(param1.equals("Peso")) {
            	coefMostrado+=10;
            	campo1=peso;
            } else if(param1.equals("Cargas")) {
            	coefMostrado+=20;
            	campo1=cargas;
            } else if(param1.equals("Altura")) {
            	coefMostrado+=30;
            	campo1=altura;
            } else if(param1.equals("Duelos aéreos ganados")) {
            	coefMostrado+=40;
            	campo1=duelosAereos;
            }
        }
        int param2Seleccionado = JOptionPane.showConfirmDialog(null, paramsComboBox, "Seleccione el segundo parámetro", JOptionPane.OK_CANCEL_OPTION);
        if (param2Seleccionado == JOptionPane.OK_OPTION) {
            String param2 = (String) paramsComboBox.getSelectedItem();
            textArea.append("Eje Y: " + param2 + "\n");
            if(param2.equals("Peso")) {
            	coefMostrado+=1;
            	campo2=peso;
            } else if(param2.equals("Cargas")) {
            	coefMostrado+=2;
            	campo2=cargas;
            } else if(param2.equals("Altura")) {
            	coefMostrado+=3;
            	campo2=altura;
            } else if(param2.equals("Duelos aéreos ganados")) {
            	coefMostrado+=4;
            	campo2=duelosAereos;
            }
        }
        
        double coefVisualizado=0;
        
        if(coefMostrado==12) {
        	coefVisualizado=pesoYCargas;
        } else if(coefMostrado==13) {
        	coefVisualizado=pesoYAltura;
        } else if(coefMostrado==14) {
        	coefVisualizado=pesoYDuelosAereos;
        } else if(coefMostrado==23) {
        	coefVisualizado=cargasYAltura;
        } else if(coefMostrado==24) {
        	coefVisualizado=cargasYDuelosAereos;
        } else if(coefMostrado==34) {
        	coefVisualizado=alturaYDuelosAereos;
        }
        
        mostrarDiagrama(campo1, campo2, coefVisualizado);
    	
    }

    public static void mostrarDiagrama(double[] datosX, double[] datosY, double dato) {
        JFrame frame = new JFrame("Diagrama de Puntos");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Determinar los límites de los datos en los ejes X e Y
                double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
                double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;
                for (int i = 0; i < datosX.length; i++) {
                    if (datosX[i] < minX) minX = datosX[i];
                    if (datosX[i] > maxX) maxX = datosX[i];
                    if (datosY[i] < minY) minY = datosY[i];
                    if (datosY[i] > maxY) maxY = datosY[i];
                }/*
                double minX = Arrays.stream(datosX).min().orElse(0);
                double maxX = Arrays.stream(datosX).max().orElse(1);
                double minY = Arrays.stream(datosY).min().orElse(0);
                double maxY = Arrays.stream(datosY).max().orElse(1);*/
                System.out.println("minX: " + minX);
                System.out.println("minY: " + minY);
                // Calcular los valores de las escalas en función de los límites
                double escalaX = (maxX - minX) / 10; // 10 marcas en el eje X
                double escalaY = (maxY - minY) / 10; // 10 marcas en el eje Y

                // Dibujar ejes y escalas
                g.drawLine(50, getHeight() - 50, getWidth() - 50, getHeight() - 50); // Eje X
                g.drawLine(50, getHeight() - 50, 50, 50); // Eje Y
                int xMarca = 0;
                int yMarca = 0;
                for (int i = 0; i <= 10; i++) {
                    xMarca = 50 + (int) (i * (getWidth() - 100) / 10);
                    yMarca = getHeight() - 50 - (int) (i * (getHeight() - 100) / 10);
                    g.drawLine(xMarca, getHeight() - 50, xMarca, getHeight() - 45); // Marcas en el eje X
                    g.drawLine(45, yMarca, 50, yMarca); // Marcas en el eje Y
                    g.drawString(String.format("%.1f", minX + i * escalaX), xMarca - 10, getHeight() - 30); // Etiquetas en el eje X
                    g.drawString(String.format("%.1f", minY + i * escalaY), 15, yMarca + 5); // Etiquetas en el eje Y
                }

                // Dibujar puntos
                for (int i = 0; i < datosX.length; i++) {
                	System.out.println("datosX[" + i + "] = " + datosX[i] + ", datosY[" + i + "] = " + datosY[i]);
                    
                    // Verificación de los valores mínimos y escalas
                    System.out.println("minX = " + minX + ", minY = " + minY + ", escalaX = " + escalaX + ", escalaY = " + escalaY);
                    
                    /*int x = 50 + (int) ((datosX[i] - minX) / escalaX * (getWidth() - 100));
                    int y = getHeight() - 50 - (int) ((datosY[i] - minY) / escalaY * (getHeight() - 100));*/
                    int x = (int)(((datosX[i]-minX)/(maxX-minX))*(getWidth() - 100))+ 50;
                    int y = (int)((1-((datosY[i]-minY)/(maxY-minY)))*(getHeight() - 100))+ 50;
                    
                    // Imprimir las coordenadas calculadas
                    System.out.println("Coordenadas calculadas: x = " + x + ", y = " + y);
                    
                    g.fillOval(x - 2, y - 2, 5, 5); // Dibujamos un punto en la posición
                }
            }
        };

        JLabel label = new JLabel("Dato: " + dato);
        panel.add(label);

        frame.add(panel);
        frame.setSize(600, 600);
        frame.setVisible(true);
    }
    
    public static void mostrarClusters(String[] j,int[] c) {
    	textArea.append("Clusters de jugadores: \n");
    	if(j.length!=c.length) {
    		textArea.append("Hubo un error inesperado... \n");
    	} else {
    		textArea.append("Cluster ||| Jugador: \n");
    		for(int i=0;i<j.length;i++) {
    			textArea.append("   "+c[i]+"    |||   "+j[i]+"\n");
    		}
    	}
    }
    
    public static void confirmarActualizacion(String r) {
    	if(r.equals("OK")) {
    		textArea.append("Jugadores actualizados correctamente.\n");
    	} else {
    		textArea.append("Hubo un error inesperado al actualizar los jugadores...\n");
    	}
    }
    
    // Operacion nula
    public static void operacionNoImplementada(int i) {
    	if(i==1) { // Comparar jugadores de distntios tipos (Portero y General o viceversa)
    		textArea.append("En esta versión del programa, no es posible comparar jugadores de distinto tipo (porteros con generales o viceversa). \n");
    	} else {
    		textArea.append("Se produjo un error inesperado... \n");
    	}
    }
    


}