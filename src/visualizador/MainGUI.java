package visualizador;

import javax.swing.JFrame;
import JFramePrincipal;
import AgenteVisualizador;

public class MainGUI extends Thread {
    String titulo;
    AgenteVisualizador agenteVisualizador;
    public MainGUI(String tit,AgenteVisualizador a){
        //Se reciben dos parámetros
        //el título a poner en la ventana
        //la referencia al AgenteUsuario
        this.titulo= "Conexión Visualizador. Agente " + tit;
        this.agenteVisualizador=a;
    }

    public void run(){
        //El interfaz se basará en una ventana de tipo JFrame
        //Lo personalizamos creando un constructor personalizado para que pueda acceder al agente desde el hilo del interfaz
        JFrame jFrame;
        jFrame=new JFramePrincipal(agenteVisualizador);
        jFrame.setTitle(titulo);
        jFrame.setVisible(true);
        jFrame.setResizable(true);
    }

}