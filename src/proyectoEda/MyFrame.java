package proyectoEda;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import org.graphstream.graph.Graph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;

import proyectoEda.GraphController.GSelectMode;

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

	    int key = e.getKeyCode();

	    if(key == KeyEvent.VK_ESCAPE){
	    	Main.ShowControls();
	    }
	    if(key == KeyEvent.VK_Z){
	    	System.out.println("Pressed Z: Show all edges");
	    	GraphController.getInstance().ChangeSelectionMode(GSelectMode.All);
	    	GraphController.getInstance().ShowAllEdges();
	    }
	    if(key == KeyEvent.VK_X){
	    	System.out.println("Pressed X: Hide all edges / Reset");
	    	GraphController.getInstance().ChangeSelectionMode(GSelectMode.Def);
	    	GraphController.getInstance().HideAllEdges();
	    }
	    if(key == KeyEvent.VK_A){
	    	System.out.println("Pressed A: Show Arrivals to selected airport");
	    	GraphController.getInstance().ChangeSelectionMode(GSelectMode.Arr);
	    }
	    if(key == KeyEvent.VK_S){
	    	System.out.println("Pressed S: Show Arrivals and Departures from selected airport");
	    	GraphController.getInstance().ChangeSelectionMode(GSelectMode.ArrDep);
	    }
	    if(key == KeyEvent.VK_D){
	    	System.out.println("Pressed D: Show Departures from selected airport");
	    	GraphController.getInstance().ChangeSelectionMode(GSelectMode.Dep);
	    }
	    if(key == KeyEvent.VK_C){
	    	System.out.println("Pressed C: Show shortest path two nodes");
	    	GraphController.getInstance().ChangeSelectionMode(GSelectMode.Route);
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
