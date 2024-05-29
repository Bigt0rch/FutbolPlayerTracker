package visualizador;

import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class AgenteVisualizador extends Agent {
	//Métodos de clasificación que vamos a utilizar en este ejemplo: árbol de decisión J48 y clustering KNN
//	static final int _J48 =0;
//	static final int _KNN = 6;
//	private File fichero;
//	private String metodo_clasificacion;
	JFramePrincipal principal;
	JFrameSeleccionar ventana;
	protected void setup() {
	//Añadimos los comportamientos cíclicos para Solicitar análisis al analizador y para Mostrar resultados al usuario
	addBehaviour(new CyclicBehaviourVisualizador());
	//addBehaviour(new CyclicBehaviourDataRequest());
	//Lanzamos el interfaz en un hilo de ejecución independiente --> Creamos un MainGUI, que a su vez crea un JFrame principal para el agente
	MainGUI gui=new MainGUI(this.getLocalName(), this);
	gui.run();
	}
	/*
	 OPERACIONES

General jugador
"general;NA;VINÍCIUS;Real Madrid"
1 PlayerDataX90
----------------------------------------------------------------------------
comparar
"comparar;NA;VINÍCIUS;Real Madrid;Modric;Real Madrid"
1 PlayerDataX90
2 PlayerDataX90
----------------------------------------------------------------------------
datosEquipo
"datosEquipo;NA;Girona"
1 TeamData
----------------------------------------------------------------------------
actualizar
"actualizar"
1 "OK"
----------------------------------------------------------------------------
correlaciones
"correlaciones;NA"
1 double[] peso (kg)
2 double[] cargas
3 double[] altura (cm)
4 double[] duelosAereosGanados
5 coeficientePearson pesoYCargas
6 coeficientePearson pesoYDuelosAereos
7 coeficientePearson alturaYDuelosAereos
8 coeficientePearson alturaYCargas
----------------------------------------------------------------------------
clustering
"clusterizar,NA,3" //3 es el numero de clusters que quieres, el usuario
                                  //puede solicitar los que quiera
1 String[] nombresJugadores //El jugador en nombresJugadores[i]
2 int[] clustersJugadores        //pertenece al cluster indicado en 
                                                        //clustersJugadores[i]
----------------------------------------------------------------------------
===============================================================

MAS INFO
Clases info
PlayerDataX90     GeneralPlayerDatax90 - Jugadores de campo
        GoalKeeperPlayerDatax90 - Porteros

Mensaje de error:
"ERROR"
	 */
}
