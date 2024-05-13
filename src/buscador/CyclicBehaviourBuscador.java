package buscador;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import jade.content.lang.sl.SLCodec;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.Envelope;
import jade.lang.acl.*;
import java.io.Serializable;

public class CyclicBehaviourBuscador extends CyclicBehaviour {
	
	Map<String,Integer> columnas = new HashMap<>();
	String datos[][];
	String pythonScriptPath = "./resources/python/script.py";
	String CSVPath = "./resources/rawData/data.csv";

	public void action() {
		// Creamos la espera de mensajes en modo bloqueante y con un filtro de tipo REQUEST
		ACLMessage msg=this.myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
		Serializable respuesta;
		try
		{
						//Imprimimos por pantalla el texto a buscar
			System.out.println(msg.getSender().getName()+":"+ (String)msg.getContentObject());
			
			String[] mensaje = ((String)msg.getContentObject()).split(";");
			
			leerCSV();
			respuesta = "ERROR";
			if("actualizar".equals(mensaje[0])) {
				actualizarCSV(null);
				respuesta = "DONE";
			}
			else if("general".equals(mensaje[0])){
				respuesta = jugadorX90(mensaje[1],mensaje[2]);
			}
			else if("generalPortero".equals(mensaje[0])) {
				
			}
				
            
			
			
			//Cuando la búsqueda ha finalizado, enviamos un mensaje de respuesta
			ACLMessage aclMessage = new ACLMessage(ACLMessage.INFORM);
			aclMessage.addReceiver(msg.getSender());
			aclMessage.setOntology("ontologia");
			aclMessage.setLanguage(new SLCodec().getName());
			aclMessage.setEnvelope(new Envelope());
			aclMessage.getEnvelope().setPayloadEncoding("ISO8859_1");
			aclMessage.setContentObject(respuesta);
			this.myAgent.send(aclMessage);
		}
		catch (UnreadableException e)
		{
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void actualizarCSV(String[] params) {
		try {
			String[] command = {"python", pythonScriptPath};
			ProcessBuilder pb = new ProcessBuilder(command);
			Process process;			
			process = pb.start();
			int exitCode = process.waitFor();
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void leerCSV() {
        try {
        	FileReader fileReader = new FileReader(CSVPath);
        	CSVReader csvReader = new CSVReader(fileReader);
        	
        	String[] fila;
        	int i = 0;
			while ((fila = csvReader.readNext()) != null) {
				int j = 0;
			    for (String campo : fila) {
			    	if(i==0) {
			    		columnas.put(campo, j);
			    	}
			    	else {
			    		if(campo == null) {
			    			campo = "0";
			    		}
			    		datos[i-1][j] = campo;
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
	
	private GeneralPlayerDataX90 jugadorX90(String nombre, String equipo) {
		int i = 0;
		while(!(datos[i][0].contains(nombre) && datos[i][6].contains(equipo))) {
			i++;
		}
		return new GeneralPlayerDataX90(datos[i], columnas);
		
	} 

}
