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
import java.util.stream.Stream;

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

import java.io.Serializable;

public class CyclicBehaviourProcesador extends CyclicBehaviour {

	Map<String,Integer> columnas;
	String datos[][];
	static String pythonScriptPath = "./python_tools/scrapLaLiga.py";
	static String CSVPath = "./resources/rawData/data.csv";

	public void action() {
		//Recibimos la solicitud de la UI
		ACLMessage solicitudMsg=this.myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));

		String[] solicitud = solicitudMsg.getContent().split(";");

		//Construimos y mandamos un mensaje solicitando los datos al buscador
		ACLMessage pedirDatos = new ACLMessage(ACLMessage.REQUEST);
		pedirDatos.addReceiver(new AID("Buscador", AID.ISLOCALNAME));
		pedirDatos.setOntology("ontologia");
		pedirDatos.setLanguage(new SLCodec().getName());
		pedirDatos.setEnvelope(new Envelope());
		pedirDatos.getEnvelope().setPayloadEncoding("ISO8859_1");
		pedirDatos.setContent("datos;"+(solicitud[0].equals("A")?"A":"NA")); //Si el UI nos pide actualizar los datos antes, pedimos que se actualicen los datos
		this.myAgent.send(pedirDatos);

		try
		{	
			//Recibimos los datos, junto con el mapa de columnas
			ACLMessage dataMsg=this.myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
			ACLMessage columnasMsg=this.myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
			HashMap<String,Integer> columnas = (HashMap<String,Integer>)columnasMsg.getContentObject();
			String datos[][] = (String[][])dataMsg.getContentObject();
			
			//Preparamos un mensage para enviar la info que se nos solicite
			Serializable respuesta = "ERROR"; //Respuesta por defecto(no deberia mandarse nunca)
			ACLMessage aclMessage = new ACLMessage(ACLMessage.INFORM);
			aclMessage.addReceiver(solicitudMsg.getSender());
			aclMessage.setOntology("ontologia");
			aclMessage.setLanguage(new SLCodec().getName());
			aclMessage.setEnvelope(new Envelope());
			aclMessage.getEnvelope().setPayloadEncoding("ISO8859_1");
			aclMessage.setContentObject(respuesta);
			
			//Observamos que se nos pide en la peticion y actuamos en consecuencia
			if("general".equals(solicitud[0])){
				respuesta = jugadorX90(solicitud[2],solicitud[3]);
				aclMessage.setContentObject(respuesta);
				this.myAgent.send(aclMessage);
			}
			else if("comparar".equals(solicitud[0])) {
				respuesta = jugadorX90(solicitud[2],solicitud[3]);
				aclMessage.setContentObject(respuesta);
				this.myAgent.send(aclMessage);
				respuesta = jugadorX90(solicitud[4],solicitud[5]);
				aclMessage.setContentObject(respuesta);
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
				for(int i = 0; i < datos.length; i++) {
					if(datos[i][columnas.get("peso")] != null && 
					   datos[i][columnas.get("tackles_won")] != null &&
					   Integer.parseInt(datos[i][columnas.get("peso")]) != 0 && 
					   Integer.parseInt(datos[i][columnas.get("tackles_won")]) != 0){
						peso.add((double)Integer.parseInt(datos[i][columnas.get("peso")]));
						cargas.add((double)Integer.parseInt(datos[i][columnas.get("tackles_won")]));
					}
					if(datos[i][columnas.get("altura")] != null && 
					   datos[i][columnas.get("aerial_duels_won")] != null &&
					   Integer.parseInt(datos[i][columnas.get("altura")]) != 0 && 
					   Integer.parseInt(datos[i][columnas.get("aerial_duels_won")]) != 0){
						altura.add((double)Integer.parseInt(datos[i][columnas.get("altura")]));
						duelosAereos.add((double)Integer.parseInt(datos[i][columnas.get("aerial_duels_won")]));
					}
				}
				PearsonsCorrelation correlacion = new PearsonsCorrelation();
				double[] arrayPeso = peso.stream().mapToDouble(i -> i).toArray();
				double[] arrayCargas = cargas.stream().mapToDouble(i -> i).toArray();
				double[] arrayAltura = altura.stream().mapToDouble(i -> i).toArray();
				double[] arrayDuelosAereos = new double[duelosAereos.size()];
		        double coeficientePearsonPeso = correlacion.correlation(arrayPeso, arrayCargas);
		        double coeficientePearsonAltura = correlacion.correlation(arrayAltura, arrayDuelosAereos);
		        
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
				respuesta = coeficientePearsonPeso;
				aclMessage.setContentObject(respuesta);
				this.myAgent.send(aclMessage);
				respuesta = coeficientePearsonAltura;
				aclMessage.setContentObject(respuesta);
				this.myAgent.send(aclMessage);
			}
			else if("clusterizar".equals(solicitud[0])) {
				CSVLoader loader = new CSVLoader();
		        loader.setSource(new File("resources/AgenteProcesador/temp/tempData.csv"));
		        Instances data = loader.getDataSet();
		        
		        // Establecer el índice del atributo clase (si existe)
		        if (data.classIndex() == -1) {
		            data.setClassIndex(3);
		        }

		        // Configurar el algoritmo de clustering (k-Means)
		        SimpleKMeans kmeans = new SimpleKMeans();
		        kmeans.setNumClusters(5);
		        kmeans.buildClusterer(data);
		        
		        //TODO: Pasar los resultados del clustering a el agenteUI
		        //TODO: Hacer que se utilicen datos por cada 90 mins para que la comparativa sea correcta
		        
		        // Imprimir los centroides de los clusters
		        Instances centroids = kmeans.getClusterCentroids();
		        for (int i = 0; i < centroids.numInstances(); i++) {
		            System.out.println("Centroide del cluster " + (i + 1) + ": " + centroids.instance(i));
		        }

		        // Imprimir la asignación de cada instancia a un cluster
		        for (int i = 0; i < data.numInstances(); i++) {
		            int cluster = kmeans.clusterInstance(data.instance(i));
		            System.out.println("Instancia " + (i + 1) + " pertenece al cluster " + (cluster + 1));
		        }
			}
			else if("actualizar".equals(solicitud[0])) {

			}




			
			
			this.myAgent.send(aclMessage);
		}
		catch (IOException e) {
			e.printStackTrace();
		} catch (UnreadableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void actualizarCSV(String[] params) {
		try {
			String[] command = {"python3", pythonScriptPath};
			String[] result = Stream.concat(Stream.of(command), Stream.of(params)).toArray(String[]::new);
			ProcessBuilder pb = new ProcessBuilder(result);
			Process process;			
			process = pb.start();
			int exitCode = process.waitFor();
			System.out.println(exitCode);


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void dataToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("resources/AgenteProcesador/temp/tempData.csv"))) {
            for (String[] fila : datos) {
                writer.write(String.join(",", fila));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	private PlayerDataX90 jugadorX90(String nombre, String equipo) {
		int i = 0;
		while(!(datos[i][0].contains(nombre) && datos[i][7].contains(equipo))) {
			i++;
		}
		return new GeneralPlayerDataX90(datos[i], columnas);

	}
	
	private PlayerDataX90 porteroX90(String nombre, String equipo) {
		int i = 0;
		while(!(datos[i][0].contains(nombre) && datos[i][7].contains(equipo))) {
			i++;
		}
		return new GoalkeeperPlayerDataX90(datos[i], columnas);

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

	public static void main(String[] args) {
		actualizarCSV(new String[] { CSVPath });
		System.out.println("Ayuda");
	}

}
