// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package research.fx;
import goryachev.common.util.CList;
import goryachev.common.util.D;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;


/**
 * Split Pane Alternative without all this skin nonsense.
 */
public class CSplitPane
	extends Pane
{
	public static final double DEFAULT_TARGET_RADIUS = 5.0;
	private final boolean horizontal;
	private double targetRadius = DEFAULT_TARGET_RADIUS;
	private Region target;
	private final CList<Divider> dividers = new CList();
	
	
	public CSplitPane(boolean horizontal, Node ... nodes)
	{
		this(horizontal);
		addAll(nodes);
	}
	
	
	public CSplitPane(boolean horizontal)
	{
		this.horizontal = horizontal;
		addEventFilter(MouseEvent.MOUSE_ENTERED, (ev) -> handleMouseEntered(ev));
		addEventFilter(MouseEvent.MOUSE_MOVED, (ev) -> handleMouseMoved(ev));
		addEventFilter(MouseEvent.MOUSE_CLICKED, (ev) -> handleMouseClicked(ev));
		addEventFilter(MouseEvent.MOUSE_DRAGGED, (ev) -> handleMouseDragged(ev));
		addEventFilter(MouseEvent.MOUSE_EXITED, (ev) -> handleMouseExited(ev));
	}
	
	
	public void add(Node nd)
	{
		getChildren().add(nd);
		requestLayout();
	}
	
	
	public void addAll(Node ... nodes)
	{
		getChildren().addAll(nodes);
		requestLayout();
	}
	

	public boolean isHorizontal()
	{
		return horizontal;
	}
	
	
	protected void layoutChildren()
	{
		Insets m = getInsets();
			
		ObservableList<Node> cs = getChildren();
		int sz = cs.size();
		
		if(horizontal)
		{
			double min = m.getLeft();
			double max = getWidth() - m.getRight();
			double wid = max - min;
			
			for(int i=0; i<sz; i++)
			{
				Node n = cs.get(i);
				Divider d = getDivider(i);
				double w = d.constraint * wid;
			}
		}
		else
		{
			
		}
	}
	
	
	protected Divider getDivider(int ix)
	{
		Divider d = new Divider();
		d.constraint = 0.33;
		return d;
	}
	
	
	protected void handleMouseEntered(MouseEvent ev)
	{
		D.print(ev);
		armTarget(ev);
	}
	
	
	protected void handleMouseClicked(MouseEvent ev)
	{
		D.print(ev);
	}
	
	
	protected void handleMouseDragged(MouseEvent ev)
	{
		D.print(ev);
		armTarget(ev);
	}
	
	
	protected void handleMouseExited(MouseEvent ev)
	{
		D.print(ev);
		releaseTarget();
	}
	
	
	protected void handleMouseMoved(MouseEvent ev)
	{
		D.print(ev);
		armTarget(ev);
	}
	
	
	protected void armTarget(MouseEvent ev)
	{
		Divider d = getDividerUnder(ev);
		if(d != null)
		{
			if(target == null)
			{
				double sz = targetRadius + targetRadius;
				target = new Region();
				target.resize(sz, sz);
				
				getChildren().add(target);
			}
			
			target.relocate(ev.getX() - targetRadius, ev.getY() - targetRadius);
			// TODO change cursor when can't move
			target.setCursor(horizontal ? Cursor.H_RESIZE : Cursor.V_RESIZE);
		}
		else
		{
			releaseTarget();
		}
	}
	
	
	protected void releaseTarget()
	{
		if(target != null)
		{
			getChildren().remove(target);
			target = null;
		}
	}
	
	
	protected Divider getDividerUnder(MouseEvent ev)
	{
		ObservableList<Node> cs = getChildren();
		int sz = cs.size() - 1;
		
		return getDivider(0); // FIX
	}
	
	
	//
	
	
	protected static class Divider
	{
		/** 0 ... 1.0 specifies percent, > 1.0 specifies fixed location */
		public double constraint;
	}
}
