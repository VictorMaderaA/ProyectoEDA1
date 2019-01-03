package proyectoEda;

import javax.swing.JFrame;
import org.graphstream.graph.Graph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;

public class MyFrame extends JFrame{

	int width = 1900;
	int height = 1080;
	
	public MyFrame() {
		setSize(width,height);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH); 
		setUndecorated(false);
		setResizable(true);
	}
	
	public void SetGraph(Graph graph)
	{
		Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		viewer.disableAutoLayout();	
		ViewPanel view = viewer.addDefaultView(false);
		view.setMouseManager(MapMouseManager.GetMouseManager());
		
		this.add(view);
		
	}
	
}
