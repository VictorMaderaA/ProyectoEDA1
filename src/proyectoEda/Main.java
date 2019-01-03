package proyectoEda;

public class Main {

	
	public static void main(String[] args) {
		
		GraphController gController = GraphController.getInstance();
		gController.GenerateStartingGraph();
		
		gController.HideAllEdges();
		
		
		MyFrame frame2 = new MyFrame();
		frame2.SetGraph(gController.graph);
		frame2.setVisible(true);
	}

}
