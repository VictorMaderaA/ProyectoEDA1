package proyectoEda;

import javax.swing.JOptionPane;

public class Main {

	
	public static void main(String[] args) {
		
		GraphController gController = GraphController.getInstance();
		
		gController.HideAllEdges();
		
		
		MyFrame frame2 = new MyFrame();
		frame2.SetGraph(gController.graph);
		frame2.setVisible(true);
		
		ShowControls();
	}

	
	public static void ShowControls()
	{
		String Message =  "Escape: Mostrar controles. \n"
						+ "Z: Mostrar todas las rutas. \n"
						+ "X: Ocultar todas las rutas. \n"
						+ "A: Mostrar rutas y aeropuertos que llegan a un nodo. \n"
						+ "S: Mostrar rutas y aeropuertos que llegan y salen de un nodo. \n"
						+ "D: Mostrar rutas y aeropuertos que salen de un nodo. \n"
						+ "C: Mostrar ruta mas corta entre dos nodos. \n"
						+ "Q: Mostrar informacion extra de la seleccion. \n";
        JOptionPane.showMessageDialog(null, Message, "Controles", JOptionPane.INFORMATION_MESSAGE);
	}
}