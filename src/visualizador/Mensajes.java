package visualizador;

import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class Mensajes extends Agent {
	
	public Mensajes(Integer code,String name,String teamName,String name2,String teamName2,String teamNameData,Integer clusters) {
		this.code=code;
		this.name=name;
		this.teamName=teamName;
		this.name2=name2;
		this.teamName2=teamName2;
		this.teamNameData=teamNameData;
		this.clusters=clusters;
	}
	
	// 1 Jugador general
	// 2 Comparar jugadores
	// 3 Datos de un equipo
	// 4 Correlaciones
	// 5 CLustering
	static Integer code;
	
	// 1 (y 2)
	static String name;
	static String teamName;
	
	// 2
	static String name2;
	static String teamName2;
	
	// 3
	static String teamNameData;
	
	// 5
	static Integer clusters;
	
}
