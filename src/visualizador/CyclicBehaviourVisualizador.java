package visualizador;

public class CyclicBehaviourVisualizador  extends CyclicBehaviour {
    private static final long serialVersionUID = 1L;
    public void action() {
        // Creamos un mensaje de espera bloqueante para esperar un mensaje de tipo INFORM
        ACLMessage
        msg1=this.myAgent.blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
        try{
            //Recibimos los datos a analizar, que vienen en el mensaje del agente AnalizadorWeka como un objeto // ??
            PlayerDataX90 playerDataX90 = new PlayerDataX90();
            general_playerDataX90 = (GeneralPlayerDataX90) msg1.getContentObject();
            gk_playerDataX90 = (GoalkeeperPlayerDataX90) msg1.getContentObject();
            //Mostramos al usuario el resultado obtenido en la pantalla en la JTextArea
            JTextArea textArea = new JTextArea();
            if (metodo_clasificacion.equals("J48")){
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
            }
            //Creamos una ventana para mostrar los resultados obtenidos. Lo podemos hacer por ejemplo con un JOptionPane
            //Y le ponemos un scroll para desplazarnos porque puede haber muchos resultados
            JScrollPane scrollPane = new JScrollPane(textArea);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            scrollPane.setPreferredSize( new Dimension( 800, 800 ) );
            JOptionPane.showMessageDialog(null, scrollPane, "Resultados Analisis WEKA",
            JOptionPane.INFORMATION_MESSAGE);
            //Limpiamos metodos_clasificación para que pase a estar vacío cuando el usuario haga una nueva solicitud
            metodo_clasificacion ="";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //Establecemos métodos para almacenar y recuperar el fichero y el método de clasificación
    public File getFichero() {
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
    }

}