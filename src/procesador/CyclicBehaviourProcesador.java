package procesador;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math4.legacy.stat.correlation.PearsonsCorrelation;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import datosFutbol.*;
import jade.content.lang.sl.SLCodec;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.Envelope;
import jade.lang.acl.*;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import java.io.Serializable;

public class CyclicBehaviourProcesador extends CyclicBehaviour {

	static Map<String,Integer> columnas;
	static String datos[][];
	static String pythonScriptPath = "./python_tools/scrapLaLiga.py";
	static String CSVPath = "./resources/rawData/data.csv";

	public void action() {
		//Recibimos la solicitud de la UI
		ACLMessage solicitudMsg=this.myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
		System.out.println(solicitudMsg.getContent());
		String[] solicitud = solicitudMsg.getContent().split(";");

		//Construimos y mandamos un mensaje solicitando los datos al buscador
		ACLMessage pedirDatos = new ACLMessage(ACLMessage.REQUEST);
		pedirDatos.addReceiver(new AID("agenteBuscador", AID.ISLOCALNAME));
		pedirDatos.setOntology("ontologia");
		pedirDatos.setLanguage(new SLCodec().getName());
		pedirDatos.setEnvelope(new Envelope());
		pedirDatos.getEnvelope().setPayloadEncoding("ISO8859_1");
		pedirDatos.setContent("datos;"+(solicitud[1].equals("A")?"A":"NA")); //Si el UI nos pide actualizar los datos antes, pedimos que se actualicen los datos
		this.myAgent.send(pedirDatos);

		try
		{	
			//Recibimos los datos, junto con el mapa de columnas
			ACLMessage columnasMsg=this.myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
			ACLMessage dataMsg=this.myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
			columnas = (HashMap<String,Integer>)columnasMsg.getContentObject();
			datos = (String[][])dataMsg.getContentObject();

			//Preparamos un mensage para enviar la info que se nos solicite
			Serializable respuesta = "ERROR"; //Respuesta por defecto(no deberia mandarse nunca)
			ACLMessage aclMessage = new ACLMessage(ACLMessage.INFORM);
			aclMessage.addReceiver(solicitudMsg.getSender());
			aclMessage.setOntology("ontologia");
			aclMessage.setLanguage(new SLCodec().getName());
			aclMessage.setEnvelope(new Envelope());
			aclMessage.getEnvelope().setPayloadEncoding("ISO8859_1");

			//Observamos que se nos pide en la peticion y actuamos en consecuencia
			if("general".equals(solicitud[0])){
				try {
					respuesta = dataX90(solicitud[2],solicitud[3]);
					aclMessage.setContentObject(respuesta);
				}
				catch(IndexOutOfBoundsException e) {
					aclMessage.setContent("Ese jugador probablemente no juega en el equipo indicado");
				}
				this.myAgent.send(aclMessage);
			}
			else if("comparar".equals(solicitud[0])) {
				try {
					respuesta = dataX90(solicitud[2],solicitud[3]);
					aclMessage.setContentObject(respuesta);
					this.myAgent.send(aclMessage);
					respuesta = dataX90(solicitud[4],solicitud[5]);
					aclMessage.setContentObject(respuesta);
				}
				catch(IndexOutOfBoundsException e) {
					aclMessage.setContent("Ese jugador probablemente no juega en el equipo indicado");
				}
				this.myAgent.send(aclMessage);
			}
			else if("datosEquipo".equals(solicitud[0])) {
				respuesta = equipo(solicitud[2]);
				aclMessage.setContentObject(respuesta);
				this.myAgent.send(aclMessage);
			}
			else if("correlaciones".equals(solicitud[0])) {
				ArrayList<Double> peso = new ArrayList<Double>();
				ArrayList<Double> cargas = new ArrayList<Double>();
				ArrayList<Double> altura = new ArrayList<Double>();
				ArrayList<Double> duelosAereos = new ArrayList<Double>();
				for(int i = 1; i < datos.length; i++) {
					//System.out.println(datos[i][columnas.get("peso")]);
					if(datos[i][columnas.get("peso")] != null && 
							datos[i][columnas.get("tackles_won")] != null &&
							(int) Double.parseDouble(datos[i][columnas.get("peso")]) != 0 && 
							(int) Double.parseDouble(datos[i][columnas.get("tackles_won")]) != 0){
						peso.add(Double.parseDouble(datos[i][columnas.get("peso")]));
						cargas.add(Double.parseDouble(datos[i][columnas.get("tackles_won")]));
					}
					if(datos[i][columnas.get("altura")] != null && 
							datos[i][columnas.get("aerial_duels_won")] != null &&
							(int) Double.parseDouble(datos[i][columnas.get("altura")]) != 0 && 
							(int) Double.parseDouble(datos[i][columnas.get("aerial_duels_won")]) != 0){
						altura.add(Double.parseDouble(datos[i][columnas.get("altura")]));
						duelosAereos.add(Double.parseDouble(datos[i][columnas.get("aerial_duels_won")]));
					}
				}
				PearsonsCorrelation correlacion = new PearsonsCorrelation();
				double[] arrayPeso = peso.stream().mapToDouble(i -> i).toArray();
				double[] arrayCargas = cargas.stream().mapToDouble(i -> i).toArray();
				double[] arrayAltura = altura.stream().mapToDouble(i -> i).toArray();
				double[] arrayDuelosAereos = new double[duelosAereos.size()];
				double coeficientePearsonPesoCargas = correlacion.correlation(arrayPeso, arrayCargas);
				double coeficientePearsonPesoDuelosAereos = correlacion.correlation(arrayPeso, arrayCargas);
				double coeficientePearsonAlturaDuelosAereos = correlacion.correlation(arrayAltura, arrayDuelosAereos);
				double coeficientePearsonAlturaCargas = correlacion.correlation(arrayAltura, arrayDuelosAereos);

				System.out.println("Voy a mandar los mensajes ahora");
				respuesta = arrayPeso;
				aclMessage.setContentObject(respuesta);
				this.myAgent.send(aclMessage);
				respuesta = arrayCargas;
				aclMessage.setContentObject(respuesta);
				this.myAgent.send(aclMessage);
				respuesta = arrayAltura;
				aclMessage.setContentObject(respuesta);
				this.myAgent.send(aclMessage);
				respuesta = arrayDuelosAereos;
				aclMessage.setContentObject(respuesta);
				this.myAgent.send(aclMessage);
				respuesta = coeficientePearsonPesoCargas;
				aclMessage.setContentObject(respuesta);
				this.myAgent.send(aclMessage);
				respuesta = coeficientePearsonPesoDuelosAereos;
				aclMessage.setContentObject(respuesta);
				this.myAgent.send(aclMessage);
				respuesta = coeficientePearsonAlturaDuelosAereos;
				aclMessage.setContentObject(respuesta);
				this.myAgent.send(aclMessage);
				respuesta = coeficientePearsonAlturaCargas;
				aclMessage.setContentObject(respuesta);
				this.myAgent.send(aclMessage);
			}
			else if("clusterizar".equals(solicitud[0])) {
				dataToCSV(procesarDatosClustering());
				CSVLoader loader = new CSVLoader();
				loader.setSource(new File("resources/AgenteProcesador/temp/tempData.csv"));
				Instances data = loader.getDataSet();

				// Configurar el algoritmo de clustering (k-Means)
				SimpleKMeans kmeans = new SimpleKMeans();
				kmeans.setNumClusters(Integer.parseInt(solicitud[2]));
				kmeans.buildClusterer(data);

				String[] playerNames = new String[data.numInstances()];
				int[] playerCluster = new int[data.numInstances()];

				for (int i = 0; i < data.numInstances(); i++) {
					int cluster = kmeans.clusterInstance(data.instance(i));
					playerNames[i] = datos[(int)data.instance(i).value(0)][0];
					playerCluster[i] = cluster + 1;
				}

				//Enviamos datos
				respuesta = playerNames;
				aclMessage.setContentObject(respuesta);
				this.myAgent.send(aclMessage);
				respuesta = playerCluster;
				aclMessage.setContentObject(respuesta);
				this.myAgent.send(aclMessage);
			}
			else if("actualizar".equals(solicitud[0])) {
				pedirDatos.setContent("actualizar;A");
				this.myAgent.send(pedirDatos);
				ACLMessage respuestaAct=this.myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
				aclMessage.setContent(respuestaAct.getContent());
				this.myAgent.send(aclMessage);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		} catch (UnreadableException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void dataToCSV(String[][] data) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("resources/AgenteProcesador/temp/tempData.csv"))) {
			for (String[] fila : data) {
				writer.write(String.join(",", fila));
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private PlayerDataX90 dataX90(String nombre, String equipo) {
		int i = 0;
		while(!(datos[i][0].contains(nombre) && datos[i][6].contains(equipo))) {
			System.out.println(i + " " + datos[i][0] + " " + datos[i][6]);
			i++;
		}
		if(datos[i][columnas.get("posicion")].equals("Portero")) {
			return new GoalkeeperPlayerDataX90(datos[i], columnas);
		}
		return new GeneralPlayerDataX90(datos[i], columnas);

	}

	private TeamData equipo(String equipo) {
		int i = 0;
		List<Integer> indices = new ArrayList<>();
		while(i<datos.length) {
			if(datos[i][7].contains(equipo)) {
				indices.add(i);
			}
			i++;
		}
		return new TeamData(indices, datos, columnas);

	}

	private static String[][] procesarDatosClustering(){
		//creamos matriz 
		String[][] datosC = new String[datos.length][73];
		//buscamos los parámetros de la lista 
		String headerC[] = {"id","posicion","altura","peso","goals","successful_passes_opposition_half","successful_passes_own_half","successful_open_play_passes","times_tackled","open_play_passes","headed_goals","successful_long_passes","total_successful_passes_excl_crosses_corners","forward_passes","successful_dribbles","total_fouls_won","total_fouls_conceded","backward_passes","through_balls","offsides","corners_won","yellow_cards","goals_from_inside_box","attempts_from_set_pieces","goal_assists","penalty_goals_conceded","foul_attempted_tackle","successful_layoffs","aerial_duels","penalty_goals","total_passes","shots_off_target_inc_woodwork","successful_short_passes","key_passes_attempt_assists","duels_won","blocked_shots","total_touches_in_opposition_box","total_clearances","goals_conceded_inside_box","hit_woodwork","total_shots","ground_duels_won","total_losses_of_possession","shots_on_target_inc_goals","goals_from_outside_box","recoveries","aerial_duels_won","blocks","goals_conceded_outside_box","touches","goals_conceded","total_tackles","clean_sheets","overruns","ground_duels","duels_lost","tackles_won","handballs_conceded","duels","successful_crosses_open_play","foul_won_penalty","throw_ins_to_own_player","successful_crosses_corners","straight_red_cards","interceptions","corners_taken_incl_short_corners","total_red_cards","successful_launches","penalties_conceded","red_cards_2nd_yellow","successful_corners_into_box","saves_made","penalties_faced"};
		datosC[0] = headerC;
		int j = 1;
		for(int i = 1; i < datos.length-1; i++ ){
			int minutosJugados = (int) Double.parseDouble(datos[i][columnas.get("time_played")]);
			if(minutosJugados < 1 || datos[i][columnas.get("posicion")].equals("Portero")) {
				continue;
			}


			datosC[j][0] = i + "";
			datosC[j][1] = datos[i][columnas.get("altura")];
			datosC[j][2] = datos[i][columnas.get("peso")];
			datosC[j][3] = String.valueOf((Double.parseDouble(datos[i][columnas.get("goals")])/minutosJugados)*90);
			datosC[j][4] = String.valueOf((Double.parseDouble(datos[i][columnas.get("successful_passes_opposition_half")])/minutosJugados)*90);
			datosC[j][5] = String.valueOf((Double.parseDouble(datos[i][columnas.get("successful_passes_own_half")])/minutosJugados)*90);
			datosC[j][6] = String.valueOf((Double.parseDouble(datos[i][columnas.get("successful_open_play_passes")])/minutosJugados)*90);
			datosC[j][7] = String.valueOf((Double.parseDouble(datos[i][columnas.get("times_tackled")])/minutosJugados)*90);
			datosC[j][8] = String.valueOf((Double.parseDouble(datos[i][columnas.get("open_play_passes")])/minutosJugados)*90);
			datosC[j][9] = String.valueOf((Double.parseDouble(datos[i][columnas.get("headed_goals")])/minutosJugados)*90);
			datosC[j][10] = String.valueOf((Double.parseDouble(datos[i][columnas.get("successful_long_passes")])/minutosJugados)*90);
			datosC[j][11] = String.valueOf((Double.parseDouble(datos[i][columnas.get("total_successful_passes_excl_crosses_corners")])/minutosJugados)*90);
			datosC[j][12] = String.valueOf((Double.parseDouble(datos[i][columnas.get("forward_passes")])/minutosJugados)*90);
			datosC[j][13] = String.valueOf((Double.parseDouble(datos[i][columnas.get("successful_dribbles")])/minutosJugados)*90);
			datosC[j][14] = String.valueOf((Double.parseDouble(datos[i][columnas.get("total_fouls_won")])/minutosJugados)*90);
			datosC[j][15] = String.valueOf((Double.parseDouble(datos[i][columnas.get("total_fouls_conceded")])/minutosJugados)*90);
			datosC[j][16] = String.valueOf((Double.parseDouble(datos[i][columnas.get("backward_passes")])/minutosJugados)*90);
			datosC[j][17] = String.valueOf((Double.parseDouble(datos[i][columnas.get("through_balls")])/minutosJugados)*90);
			datosC[j][18] = String.valueOf((Double.parseDouble(datos[i][columnas.get("offsides")])/minutosJugados)*90);
			datosC[j][19] = String.valueOf((Double.parseDouble(datos[i][columnas.get("corners_won")])/minutosJugados)*90);
			datosC[j][20] = String.valueOf((Double.parseDouble(datos[i][columnas.get("yellow_cards")])/minutosJugados)*90);
			datosC[j][21] = String.valueOf((Double.parseDouble(datos[i][columnas.get("goals_from_inside_box")])/minutosJugados)*90);
			datosC[j][22] = String.valueOf((Double.parseDouble(datos[i][columnas.get("attempts_from_set_pieces")])/minutosJugados)*90);
			datosC[j][23] = String.valueOf((Double.parseDouble(datos[i][columnas.get("goal_assists")])/minutosJugados)*90);
			datosC[j][24] = String.valueOf((Double.parseDouble(datos[i][columnas.get("penalty_goals_conceded")])/minutosJugados)*90);
			datosC[j][25] = String.valueOf((Double.parseDouble(datos[i][columnas.get("foul_attempted_tackle")])/minutosJugados)*90);
			datosC[j][26] = String.valueOf((Double.parseDouble(datos[i][columnas.get("successful_layoffs")])/minutosJugados)*90);
			datosC[j][27] = String.valueOf((Double.parseDouble(datos[i][columnas.get("aerial_duels")])/minutosJugados)*90);
			datosC[j][28] = String.valueOf((Double.parseDouble(datos[i][columnas.get("penalty_goals")])/minutosJugados)*90);
			datosC[j][29] = String.valueOf((Double.parseDouble(datos[i][columnas.get("total_passes")])/minutosJugados)*90);
			datosC[j][30] = String.valueOf((Double.parseDouble(datos[i][columnas.get("shots_off_target_inc_woodwork")])/minutosJugados)*90);
			datosC[j][31] = String.valueOf((Double.parseDouble(datos[i][columnas.get("successful_short_passes")])/minutosJugados)*90);
			datosC[j][32] = String.valueOf((Double.parseDouble(datos[i][columnas.get("key_passes_attempt_assists")])/minutosJugados)*90);
			datosC[j][33] = String.valueOf((Double.parseDouble(datos[i][columnas.get("duels_won")])/minutosJugados)*90);
			datosC[j][34] = String.valueOf((Double.parseDouble(datos[i][columnas.get("blocked_shots")])/minutosJugados)*90);
			datosC[j][35] = String.valueOf((Double.parseDouble(datos[i][columnas.get("total_touches_in_opposition_box")])/minutosJugados)*90);
			datosC[j][36] = String.valueOf((Double.parseDouble(datos[i][columnas.get("total_clearances")])/minutosJugados)*90);
			datosC[j][37] = String.valueOf((Double.parseDouble(datos[i][columnas.get("goals_conceded_inside_box")])/minutosJugados)*90);
			datosC[j][38] = String.valueOf((Double.parseDouble(datos[i][columnas.get("hit_woodwork")])/minutosJugados)*90);
			datosC[j][39] = String.valueOf((Double.parseDouble(datos[i][columnas.get("total_shots")])/minutosJugados)*90);
			datosC[j][40] = String.valueOf((Double.parseDouble(datos[i][columnas.get("ground_duels_won")])/minutosJugados)*90);
			datosC[j][41] = String.valueOf((Double.parseDouble(datos[i][columnas.get("total_losses_of_possession")])/minutosJugados)*90);
			datosC[j][42] = String.valueOf((Double.parseDouble(datos[i][columnas.get("shots_on_target_inc_goals")])/minutosJugados)*90);
			datosC[j][43] = String.valueOf((Double.parseDouble(datos[i][columnas.get("goals_from_outside_box")])/minutosJugados)*90);
			datosC[j][44] = String.valueOf((Double.parseDouble(datos[i][columnas.get("recoveries")])/minutosJugados)*90);
			datosC[j][45] = String.valueOf((Double.parseDouble(datos[i][columnas.get("aerial_duels_won")])/minutosJugados)*90);
			datosC[j][46] = String.valueOf((Double.parseDouble(datos[i][columnas.get("blocks")])/minutosJugados)*90);
			datosC[j][47] = String.valueOf((Double.parseDouble(datos[i][columnas.get("goals_conceded_outside_box")])/minutosJugados)*90);
			datosC[j][48] = String.valueOf((Double.parseDouble(datos[i][columnas.get("touches")])/minutosJugados)*90);
			datosC[j][49] = String.valueOf((Double.parseDouble(datos[i][columnas.get("goals_conceded")])/minutosJugados)*90);
			datosC[j][50] = String.valueOf((Double.parseDouble(datos[i][columnas.get("total_tackles")])/minutosJugados)*90);
			datosC[j][51] = String.valueOf((Double.parseDouble(datos[i][columnas.get("clean_sheets")])/minutosJugados)*90);
			datosC[j][52] = String.valueOf((Double.parseDouble(datos[i][columnas.get("overruns")])/minutosJugados)*90);
			datosC[j][53] = String.valueOf((Double.parseDouble(datos[i][columnas.get("ground_duels")])/minutosJugados)*90);
			datosC[j][54] = String.valueOf((Double.parseDouble(datos[i][columnas.get("duels_lost")])/minutosJugados)*90);
			datosC[j][55] = String.valueOf((Double.parseDouble(datos[i][columnas.get("tackles_won")])/minutosJugados)*90);
			datosC[j][56] = String.valueOf((Double.parseDouble(datos[i][columnas.get("handballs_conceded")])/minutosJugados)*90);
			datosC[j][57] = String.valueOf((Double.parseDouble(datos[i][columnas.get("duels")])/minutosJugados)*90);
			datosC[j][58] = String.valueOf((Double.parseDouble(datos[i][columnas.get("successful_crosses_open_play")])/minutosJugados)*90);
			datosC[j][59] = String.valueOf((Double.parseDouble(datos[i][columnas.get("foul_won_penalty")])/minutosJugados)*90);
			datosC[j][60] = String.valueOf((Double.parseDouble(datos[i][columnas.get("throw_ins_to_own_player")])/minutosJugados)*90);
			datosC[j][61] = String.valueOf((Double.parseDouble(datos[i][columnas.get("successful_crosses_corners")])/minutosJugados)*90);
			datosC[j][62] = String.valueOf((Double.parseDouble(datos[i][columnas.get("straight_red_cards")])/minutosJugados)*90);
			datosC[j][63] = String.valueOf((Double.parseDouble(datos[i][columnas.get("interceptions")])/minutosJugados)*90);
			datosC[j][64] = String.valueOf((Double.parseDouble(datos[i][columnas.get("corners_taken_incl_short_corners")])/minutosJugados)*90);
			datosC[j][65] = String.valueOf((Double.parseDouble(datos[i][columnas.get("total_red_cards")])/minutosJugados)*90);
			datosC[j][66] = String.valueOf((Double.parseDouble(datos[i][columnas.get("successful_launches")])/minutosJugados)*90);
			datosC[j][67] = String.valueOf((Double.parseDouble(datos[i][columnas.get("penalties_conceded")])/minutosJugados)*90);
			datosC[j][68] = String.valueOf((Double.parseDouble(datos[i][columnas.get("red_cards_2nd_yellow")])/minutosJugados)*90);
			datosC[j][69] = String.valueOf((Double.parseDouble(datos[i][columnas.get("successful_corners_into_box")])/minutosJugados)*90);
			datosC[j][70] = String.valueOf((Double.parseDouble(datos[i][columnas.get("saves_made")])/minutosJugados)*90);
			datosC[j][71] = String.valueOf((Double.parseDouble(datos[i][columnas.get("penalties_faced")])/minutosJugados)*90);
			j++;
		}
		String datosB[][] = new String[j][];
		for(int i = 0; i < j; i++) {
			datosB[i] = datosC[i];
		}


		return datosB;
	}


	static void leerCSV() {
		String CSVPath = "./output.csv";

		try {
			FileReader fileReader = new FileReader(CSVPath);
			CSVReader csvReader = new CSVReader(fileReader);
			columnas = new HashMap<>();

			//Contamos filas
			int numFilas =0;
			while ((csvReader.readNext()) != null) {
				numFilas++;
			}
			csvReader.close();


			//Rellenamos
			fileReader = new FileReader(CSVPath);
			csvReader = new CSVReader(fileReader);
			datos = new String[numFilas][];
			int n = 0;
			String[] linea;
			while (n < numFilas) {
				linea = csvReader.readNext();
				datos[n] = linea;
				n++;
			}
			csvReader.close();

			//Solucionamos imperfecciones
			fileReader = new FileReader(CSVPath);
			csvReader = new CSVReader(fileReader);
			String[] fila;
			int i = 0;
			while ((fila = csvReader.readNext()) != null) {
				int j = 0;
				for (String campo : fila) {
					if(i==0) {
						columnas.put(campo, j);
						datos[i][j] = campo;
					}
					else {
						if("".equals(campo) || "null".equals(campo)) {
							campo = "0";
						}
						datos[i][j] = campo;
					}
					j++;
				}
				i++;
			}

			// Cerrar el lector
			csvReader.close();

		} catch (CsvValidationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void pruebaCluesterizar() throws Exception{
		dataToCSV(procesarDatosClustering());
		CSVLoader loader = new CSVLoader();
		loader.setSource(new File("resources/AgenteProcesador/temp/tempData.csv"));
		Instances data = loader.getDataSet();

		// Configurar el algoritmo de clustering (k-Means)
		SimpleKMeans kmeans = new SimpleKMeans();
		kmeans.setNumClusters(3);
		kmeans.buildClusterer(data);

		//TODO: Pasar los resultados del clustering a el agenteUI


		// Imprimir los centroides de los clusters
		Instances centroids = kmeans.getClusterCentroids();
		for (int i = 0; i < centroids.numInstances(); i++) {
			System.out.println("Centroide del cluster " + (i + 1) + ": " + centroids.instance(i));
		}

		// Imprimir la asignación de cada instancia a un cluster
		for (int i = 0; i < data.numInstances(); i++) {
			int cluster = kmeans.clusterInstance(data.instance(i));
			System.out.println(data.instance(i).value(0)+ " pertenece al cluster " + (cluster + 1));
		}

	}

	//Metodo main para probar codigo
	public static void main(String[] args) {

		//probamos clustering
		leerCSV();
		//probamos cluesterizacion
		try {
			pruebaCluesterizar();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
//nombre,url,posicion,pais,altura,peso,equipo,goals,successful_passes_opposition_half,successful_passes_own_half,successful_open_play_passes,times_tackled,open_play_passes,headed_goals,successful_long_passes,total_successful_passes_excl_crosses_corners,forward_passes,successful_dribbles,total_fouls_won,total_fouls_conceded,backward_passes,through_balls,offsides,corners_won,yellow_cards,goals_from_inside_box,attempts_from_set_pieces,goal_assists,penalty_goals_conceded,foul_attempted_tackle,successful_layoffs,aerial_duels,penalty_goals,total_passes,shots_off_target_inc_woodwork,successful_short_passes,key_passes_attempt_assists,duels_won,blocked_shots,total_touches_in_opposition_box,total_clearances,goals_conceded_inside_box,hit_woodwork,total_shots,ground_duels_won,total_losses_of_possession,shots_on_target_inc_goals,goals_from_outside_box,recoveries,aerial_duels_won,blocks,goals_conceded_outside_box,touches,goals_conceded,total_tackles,clean_sheets,overruns,ground_duels,duels_lost,tackles_won,handballs_conceded,duels,successful_crosses_open_play,foul_won_penalty,throw_ins_to_own_player,successful_crosses_corners,straight_red_cards,interceptions,corners_taken_incl_short_corners,total_red_cards,successful_launches,penalties_conceded,red_cards_2nd_yellow,successful_corners_into_box,saves_made,penalties_faced
