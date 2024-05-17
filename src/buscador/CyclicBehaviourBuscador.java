package buscador;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import jade.content.lang.sl.SLCodec;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.Envelope;
import jade.lang.acl.*;
import java.io.Serializable;

public class CyclicBehaviourBuscador extends CyclicBehaviour {
	
	HashMap<String,Integer> columnas = new HashMap<>();
	String datos[][];
	static String pythonScriptPath = "./python_tools/scrapLaLiga.py";
	static String CSVPath = "./resources/AgenteBuscador/rawData/data.csv";

	public void action() {
		// Recibimos una solicitud de datos
		ACLMessage msg=this.myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
		Serializable respuesta;
		try
		{
						//Imprimimos por pantalla el texto a buscar
			System.out.println(msg.getSender().getName()+":"+ (String)msg.getContent());
			
			String[] mensaje = ((String)msg.getContent()).split(";");
			
			//Si se solicita que los datos se actualicen se hace
			respuesta = "ERROR";
			if("A".equals(mensaje[1]) || "actualizar".equals(mensaje[0])) {
				actualizarCSV(new String[] { CSVPath });
				respuesta = datos;
			}
			
			//Leemos los datos del CSV y los cargamos en memoria para mandarlos
			leerCSV();
				
			//Enviamos los datos crudos al procesador
			ACLMessage aclMessage = new ACLMessage(ACLMessage.INFORM);
			aclMessage.addReceiver(new AID("agenteProcesador", AID.ISLOCALNAME));
			aclMessage.setOntology("ontologia");
			aclMessage.setLanguage(new SLCodec().getName());
			aclMessage.setEnvelope(new Envelope());
			aclMessage.getEnvelope().setPayloadEncoding("ISO8859_1");
			if(mensaje[0].equals("datos")) {
				respuesta = columnas;
				aclMessage.setContentObject(respuesta);
				this.myAgent.send(aclMessage);
				respuesta = datos;
				aclMessage.setContentObject(respuesta);
				this.myAgent.send(aclMessage);
			}
			else {
				aclMessage.setContent("OK");
				this.myAgent.send(aclMessage);
			}
		}
		catch (IOException e) {
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
			e.printStackTrace();
		}
	}
	
	private void leerCSV() {
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
	
	//Metodo main para probar codigo
	public static void main(String[] args) {
		actualizarCSV(new String[] { CSVPath });
		System.out.println("Ayuda");
	}

}
