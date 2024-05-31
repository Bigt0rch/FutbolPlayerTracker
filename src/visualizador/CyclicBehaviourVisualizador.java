package visualizador;

import java.awt.Dimension;
import java.io.File;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import datosFutbol.GeneralPlayerDataX90;
import datosFutbol.GoalkeeperPlayerDataX90;
import datosFutbol.PlayerDataX90;
import datosFutbol.TeamData;
import jade.content.lang.sl.SLCodec;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.Envelope;
import jade.lang.acl.*;

public class CyclicBehaviourVisualizador  extends CyclicBehaviour {
	private static final long serialVersionUID = 1L;

	//private JPanel contentPane;

	Integer currentCode=0;

	public void action() {

		// Creamos un mensaje de espera bloqueante para esperar un mensaje de tipo INFORM
		// ACLMessage msg1=this.myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));


		// El agente de usuario espera a que el usuario haya seleccionado método de
		// clasificación y un fichero
		myAgent.doWait();



		//Construimos y mandamos un mensaje solicitando los datos al buscador
		ACLMessage pedirDatos = new ACLMessage(ACLMessage.REQUEST);
		pedirDatos.addReceiver(new AID("agenteProcesador", AID.ISLOCALNAME));
		pedirDatos.setOntology("ontologia");
		pedirDatos.setLanguage(new SLCodec().getName());
		pedirDatos.setEnvelope(new Envelope());
		pedirDatos.getEnvelope().setPayloadEncoding("ISO8859_1");

		if(Mensajes.code==1) {
			currentCode=1;
			pedirDatos.setContent("general;NA;"+Mensajes.name+";"+Mensajes.teamName); //Si el UI nos pide actualizar los datos antes, pedimos que se actualicen los datos
		}

		else if(Mensajes.code==2) {
			currentCode=2;
			pedirDatos.setContent("comparar;NA;"+Mensajes.name+";"+Mensajes.teamName+";"+Mensajes.name2+";"+Mensajes.teamName2);
		}

		else if(Mensajes.code==3) {
			currentCode=3;
			pedirDatos.setContent("datosEquipo;NA;"+Mensajes.teamNameData);
		}

		else if(Mensajes.code==4) {
			currentCode=4;
			pedirDatos.setContent("correlaciones;NA");
		} else if(Mensajes.code==5) {
			currentCode=5;
			pedirDatos.setContent("clusterizar;NA;"+Mensajes.clusters);
		} else if(Mensajes.code==6) {
			currentCode=6;
			pedirDatos.setContent("actualizar;NA;");
		}

		this.myAgent.send(pedirDatos);


		try{

			if(currentCode==1) {
				ACLMessage result=this.myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
				if(result.getContent() != null && result.getContent().equals("ERROR")) {
					JFramePrincipal.textArea.append("Ha ocurrido algo inesperado, comprueba que lo que has pedido sea correcto (quizas un jugador no juega donde tu crees\n");
				}
				else {					
					PlayerDataX90 dataResult = (PlayerDataX90) result.getContentObject();

					if(dataResult.position.equals("Portero")) {
						GoalkeeperPlayerDataX90 gk_playerDataX90 = (GoalkeeperPlayerDataX90) result.getContentObject();
						JFramePrincipal.mostrarJugadorPortero(gk_playerDataX90);
					} else {
						GeneralPlayerDataX90 general_playerDataX90 = (GeneralPlayerDataX90) result.getContentObject();
						JFramePrincipal.mostrarJugadorGeneral(general_playerDataX90);
					}
				}

			} else if(currentCode==2){
				boolean p1=false;
				boolean g1=false;
				boolean p2=false;
				boolean g2 = false;
				GoalkeeperPlayerDataX90 gk_playerDataX90_1=null;
				GeneralPlayerDataX90 general_playerDataX90_1=null;
				GoalkeeperPlayerDataX90 gk_playerDataX90_2=null;
				GeneralPlayerDataX90 general_playerDataX90_2=null;
				ACLMessage result1=this.myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
				if(result1.getContent() != null && result1.getContent().equals("ERROR")) {
					JFramePrincipal.textArea.append("Ha ocurrido algo inesperado, comprueba que lo que has pedido sea correcto (quizas un jugador no juega donde tu crees\n");
				}
				else {	
					PlayerDataX90 dataResult1 = (PlayerDataX90) result1.getContentObject();
					ACLMessage result2=this.myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
					if(result2.getContent() != null && result2.getContent().equals("ERROR")) {
						JFramePrincipal.textArea.append("Ha ocurrido algo inesperado, comprueba que lo que has pedido sea correcto (quizas un jugador no juega donde tu crees\n");
					}
					else {
						
						PlayerDataX90 dataResult2 = (PlayerDataX90) result2.getContentObject();
						
						if(dataResult1.position.equals("Portero")) {
							gk_playerDataX90_1 = (GoalkeeperPlayerDataX90) result1.getContentObject();
							p1=true;
						} else {
							general_playerDataX90_1 = (GeneralPlayerDataX90) result1.getContentObject();
							g1=true;
						}
						
						if(dataResult2.position.equals("Portero")) {
							gk_playerDataX90_2 = (GoalkeeperPlayerDataX90) result2.getContentObject();
							p2=true;
						} else {
							general_playerDataX90_2 = (GeneralPlayerDataX90) result2.getContentObject();
							g2=true;
						}
						
						if(p1 && p2){
							JFramePrincipal.compararPorteroConPortero(gk_playerDataX90_1,gk_playerDataX90_2);
						} else if(g1 && g2) {
							JFramePrincipal.compararGeneralConGeneral(general_playerDataX90_1,general_playerDataX90_2);
						} else {
							JFramePrincipal.operacionNoImplementada(1);
						}
					}
				}

			} else if(currentCode==3){
				ACLMessage result=this.myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
				if(result.getContent() != null && result.getContent().equals("ERROR")) {
					JFramePrincipal.textArea.append("Ha ocurrido algo inesperado, comprueba que lo que has pedido sea correcto (quizas un jugador no juega donde tu crees\n");
				}
				else {	
					TeamData teamData = (TeamData) result.getContentObject();
					JFramePrincipal.mostrarDatosEquipo(teamData);
				}
			} else if(currentCode==4){
				ACLMessage peso=this.myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
				if(peso.getContent() != null && peso.getContent().equals("ERROR")) {
					JFramePrincipal.textArea.append("Ha ocurrido algo inesperado, comprueba que lo que has pedido sea correcto (quizas un jugador no juega donde tu crees\n");
				}
				else {	
					double[] pesoData = (double[]) peso.getContentObject();
					ACLMessage cargas=this.myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
					double[] cargasData = (double[]) cargas.getContentObject();
					ACLMessage altura=this.myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
					double[] alturaData = (double[]) altura.getContentObject();
					ACLMessage duelosAereos=this.myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
					double[] duelosAereosData = (double[]) duelosAereos.getContentObject();

					ACLMessage pesoYCargas=this.myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
					double pesoYCargasData = (double) pesoYCargas.getContentObject();
					ACLMessage pesoYAltura=this.myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
					double pesoYAlturaData = (double) pesoYAltura.getContentObject();
					ACLMessage pesoYDuelosAereos=this.myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
					double pesoYDuelosAereosData = (double) pesoYDuelosAereos.getContentObject();

					ACLMessage cargasYAltura=this.myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
					double cargasYAlturaData = (double) cargasYAltura.getContentObject();
					ACLMessage cargasYduelosAereos=this.myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
					double cargasYduelosAereosData = (double) cargasYduelosAereos.getContentObject();

					ACLMessage alturaYDuelosAereos=this.myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
					double alturaYDuelosAereosData = (double) alturaYDuelosAereos.getContentObject();

					JFramePrincipal.mostrarCorrelaciones(pesoData,cargasData,alturaData,duelosAereosData,
							pesoYCargasData,pesoYAlturaData,pesoYDuelosAereosData,
							cargasYAlturaData,cargasYduelosAereosData,alturaYDuelosAereosData);
				}

			} else if(currentCode==5){
				ACLMessage param1=this.myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
				if(param1.getContent() != null && param1.getContent().equals("ERROR")) {
					JFramePrincipal.textArea.append("Ha ocurrido algo inesperado, comprueba que lo que has pedido sea correcto (quizas un jugador no juega donde tu crees\n");
				}
				else {	
					String[] dataResult1 = (String[]) param1.getContentObject();
					ACLMessage param2=this.myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
					int[] dataResult2 = (int[]) param2.getContentObject();

					JFramePrincipal.mostrarClusters(dataResult1,dataResult2);
				}
			} else if(currentCode==6){
				ACLMessage res=this.myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
				String dataRes = res.getContent();

				JFramePrincipal.confirmarActualizacion(dataRes);
			}


			//Recibimos los datos a analizar, que vienen en el mensaje del agente AnalizadorWeka como un objeto // ??
			//            PlayerDataX90 playerDataX90 = new PlayerDataX90();


			//Mostramos al usuario el resultado obtenido en la pantalla en la JTextArea
			JTextArea textArea = new JTextArea();
			/*if (metodo_clasificacion.equals("J48")){
                String j48 = textArea.getText() + " Resultado del análisis con WEKA utilizando el algoritmo de clasificación"
                + "J48: \n" + resultadoAnalisis.getEvaluation().toSummaryString() + "\n\n" +
                resultadoAnalisis.getClasificadorJ48().toString();
                textArea.setText(j48);
            }
            else
            if (metodo_clasificacion.equals("KNN")){
                String knn = textArea.getText() + "\n\n Resultado del análisis con WEKA utilizando el algoritmo de clasificación"
                + "KNN: \n" + resultadoAnalisis.getEvaluation().toSummaryString() + "\n\n" +
                resultadoAnalisis.getClasificadorKnn().toString();
                textArea.setText(knn);
            }*/
			//Creamos una ventana para mostrar los resultados obtenidos. Lo podemos hacer por ejemplo con un JOptionPane
			//Y le ponemos un scroll para desplazarnos porque puede haber muchos resultados
			JScrollPane scrollPane = new JScrollPane(textArea);
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			scrollPane.setPreferredSize( new Dimension( 600, 600 ) );
			//            JOptionPane.showMessageDialog(null, scrollPane, "Football Player Tracker",JOptionPane.INFORMATION_MESSAGE);

			/* OPCIONES EN DESPLEGABLE
            comboBox.setBounds(246, 93, 147, 47);
            contentPane.add(comboBox);
            comboBox.addItem("Jugador general");
            comboBox.addItem("Comparar jugadores");
            comboBox.addItem("Datos de un equipo");
            comboBox.addItem("Correlaciones");
            comboBox.addItem("Clustering");
            JTextArea textArea2 = new JTextArea();
            textArea2.setBounds(33, 239, 351, 43);
            contentPane.add(textArea2);
            JButton btnSeleccionar = new JButton("Seleccionar");
			 */

			//Limpiamos metodos_clasificación para que pase a estar vacío cuando el usuario haga una nueva solicitud
			////metodo_clasificacion ="";
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//Establecemos métodos para almacenar y recuperar el fichero y el método de clasificación
	/*public File getFichero() {
        return fichero;
    }

    public void setFichero(File fichero_nuevo) {
        this.fichero=fichero_nuevo;
    }

    public String getMetodo() {
        return metodo_clasificacion;
    }

    public void setMetodo(String nuevo_metodo) {
        this.metodo_clasificacion = nuevo_metodo;
    }*/

}