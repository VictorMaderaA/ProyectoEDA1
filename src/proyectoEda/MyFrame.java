package proyectoEda;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import org.graphstream.graph.Graph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;

import Enums.GSelectMode;

public class MyFrame extends JFrame implements KeyListener {

	private static final long serialVersionUID = 1L;
	
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
		view.addKeyListener(this);
		this.add(view);
		addKeyListener(this);
	}

	@Override
	public void keyPressed(KeyEvent e) {

	    GraphController gC = GraphController.getInstance();
	    
	    switch (e.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			Main.ShowControls();
			break;
		case KeyEvent.VK_Z:
	    	System.out.println("Pressed Z: Show all edges");
	    	gC.ChangeSelectionMode(GSelectMode.All);
	    	gC.ShowAllEdges();
			break;
		case KeyEvent.VK_X:
	    	System.out.println("Pressed X: Hide all edges / Reset");
	    	gC.ChangeSelectionMode(GSelectMode.Def);
	    	gC.HideAllEdges();
			break;
		case KeyEvent.VK_A:
	    	System.out.println("Pressed A: Show Arrivals to selected airport");
	    	gC.ChangeSelectionMode(GSelectMode.Arr);
			break;
		case KeyEvent.VK_S:
	    	System.out.println("Pressed S: Show Arrivals and Departures from selected airport");
	    	gC.ChangeSelectionMode(GSelectMode.ArrDep);
			break;
		case KeyEvent.VK_D:
	    	System.out.println("Pressed D: Show Departures from selected airport");
	    	gC.ChangeSelectionMode(GSelectMode.Dep);
			break;
		case KeyEvent.VK_C:
	    	System.out.println("Pressed C: Show shortest path two nodes");
	    	gC.ChangeSelectionMode(GSelectMode.Route);
			break;
		case KeyEvent.VK_Q:
	    	System.out.println("Pressed Q: Show info");
	    	gC.ShowInfo();
			break;

		default:
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
