package proyectoEda;

import java.awt.event.MouseEvent;

import org.graphstream.graph.Node;
import org.graphstream.ui.view.util.DefaultMouseManager;

public class MapMouseManager {
	
	public static DefaultMouseManager GetMouseManager()
	{
		DefaultMouseManager manager = new DefaultMouseManager() {

		    @Override
		    public void mouseDragged(MouseEvent event) {

		    }

		    @Override
		    protected void mouseButtonPress(MouseEvent event) {
		        super.mouseButtonPress(event);

		        System.out.println("Press");
		    }

		    @Override
		    public void mouseClicked(MouseEvent event) {
		        super.mouseClicked(event);
		        System.out.println("Clicked");
		    }

		    @Override
		    public void mousePressed(MouseEvent event) {
		        super.mousePressed(event);

		        // if you need object of Node pressed, following code will help you, curElement is already defined at DefaultMouseManager.
		        curElement = view.findNodeOrSpriteAt(event.getX(), event.getY());

		        if (curElement != null) {
		            Node node = graph.getNode(curElement.getId());
		            if(node != null) {
		                System.out.println("Mouse pressed at node: " + node.getId());
		                GraphController.getInstance().NodeSelected(node);
		            }
		        }

		    }

		};
		
		return manager;
	}
	
}
