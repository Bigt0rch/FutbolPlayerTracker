package visualizador;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;

public class CyclicBehaviourDataRequest extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;

	public void action() {
		try {
			// El agente de usuario espera a que el usuario haya seleccionado método de
			// clasificación y un fichero
			myAgent.doWait();
			// Desde el interfaz de usuario el usuario ha pulsado el botón Enviar y podemos
			// continuar
			System.out.println("Continuando ...");
			// Definimos un objeto DatosAnalizar que sea serializable y que contenga
			// método de clasificación a aplicar que ha seleccionado el usuario en el
			// interfaz
			// fichero a analizar que ha cargado el usuario en el interfaz
//			DatosAnalizar datosAnalizar = new DatosAnalizar();
//			datosAnalizar.setFile(fichero);
//			datosAnalizar.setMethod(metodo_clasificacion);
			// Enviamos el mensaje de solicitud de clasificación al agente Análisis
			// Buscar el agente en el directorio de Servicios
			// Utilizamos la función de envío definida en Utils
			// Crea un mensaje de tipo REQUEST
			// Lo envía a los agentes que tienen registrado el servicio "analisis"
			// Como contenido se pasa el objeto analizar
//			Utils.enviarMensaje(this.myAgent, "analisis", datosAnalizar);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
