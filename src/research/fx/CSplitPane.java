// Copyright © 2017-2018 Andy Goryachev <andy@goryachev.com>
package research.fx;
import goryachev.common.util.CList;
import goryachev.fx.CssStyle;
import goryachev.fx.FX;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;


/**
 * Split Pane Alternative without all this skin nonsense.
 * 
 * TODO background
 */
public class CSplitPane
	extends Pane
{
	public static CssStyle PANE = new CssStyle("CSplitPane_PANE");
	public static CssStyle DIVIDER = new CssStyle("CSplitPane_DIVIDER");
	
	public static final double DEFAULT_TARGET_RADIUS = 5.0;
	private final boolean horizontal;
	private double targetRadius = DEFAULT_TARGET_RADIUS;
	private Region target;
	private double dividerWidth = 1;
	private final ObservableList<Node> items = FXCollections.observableArrayList();
	private final CList<Divider> dividers = new CList();
	private final CList<Region> dividerBars = new CList();
	
	
	public CSplitPane(boolean horizontal, Node ... nodes)
	{
		this(horizontal);
		addItems(nodes);
	}
	
	
	public CSplitPane(boolean horizontal)
	{
		this.horizontal = horizontal;
		
		FX.style(this, PANE);
		
		addEventFilter(MouseEvent.MOUSE_ENTERED, (ev) -> handleMouseEntered(ev));
		addEventFilter(MouseEvent.MOUSE_MOVED, (ev) -> handleMouseMoved(ev));
		addEventFilter(MouseEvent.MOUSE_CLICKED, (ev) -> handleMouseClicked(ev));
		addEventFilter(MouseEvent.MOUSE_DRAGGED, (ev) -> handleMouseDragged(ev));
		addEventFilter(MouseEvent.MOUSE_EXITED, (ev) -> handleMouseExited(ev));
	}
	

	public ObservableList<Node> getItems()
	{
		return items;
	}

	
	public void addItem(Node nd)
	{
		getItems().add(nd);
		requestLayout();
	}
	
	
	public void addItems(Node ... nodes)
	{
		getItems().addAll(nodes);
		requestLayout();
	}
	

	public boolean isHorizontal()
	{
		return horizontal;
	}
	
	
	protected void attach(Node n)
	{
		if(n.getParent() != this)
		{
			getChildren().add(n);
			n.applyCss();
		}
	}
	
	
	protected Divider getDivider(int ix)
	{
		while(dividers.size() <= ix)
		{
			dividers.add(new Divider());
		}
		
		return dividers.get(ix);
	}
	
	
	protected Region getDividerBar(int ix)
	{
		while(dividerBars.size() <= ix)
		{
			Region r = new Region();
			r.setBackground(FX.background(Color.BLACK));
			FX.style(this, DIVIDER);
			r.setManaged(false);
			getChildren().add(r);
			r.applyCss();
			
			dividerBars.add(r);
		}
		
		return dividerBars.get(ix);
	}
	
	
	protected void handleMouseEntered(MouseEvent ev)
	{
		armTarget(ev);
	}
	
	
	protected void handleMouseClicked(MouseEvent ev)
	{
	}
	
	
	protected void handleMouseDragged(MouseEvent ev)
	{
		Divider d = armTarget(ev);
		if(d != null)
		{
			moveDivider(d, horizontal ? ev.getX() : ev.getY());
		}
	}
	
	
	protected void handleMouseExited(MouseEvent ev)
	{
		releaseTarget();
	}
	
	
	protected void handleMouseMoved(MouseEvent ev)
	{
		armTarget(ev);
	}
	
	
	protected Divider armTarget(MouseEvent ev)
	{
		Divider d = getDividerUnder(ev);
		if(d == null)
		{
			releaseTarget();
		}
		else
		{
			if(target == null)
			{
				double sz = targetRadius + targetRadius;
				target = new Region();
				target.setManaged(false);
				target.resize(sz, sz);
				
				getChildren().add(target);
			}
			
			target.relocate(ev.getX() - targetRadius, ev.getY() - targetRadius);
			// TODO change cursor when can't move
			target.setCursor(horizontal ? Cursor.H_RESIZE : Cursor.V_RESIZE);
		}
		return d;
	}
	
	
	protected void releaseTarget()
	{
		if(target != null)
		{
			getChildren().remove(target);
			target = null;
		}
	}
	
	
	protected void moveDivider(Divider d, double pos)
	{
		d.override = pos;
		requestLayout();
	}
	
	
	protected Divider getDividerUnder(MouseEvent ev)
	{
		double pos = horizontal ? ev.getX() : ev.getY();
		
		ObservableList<Node> cs = getItems();
		int sz = cs.size() - 1;
		for(int i=0; i<sz; i++)
		{
			Divider d = getDivider(i);
			if(Math.abs(d.position - pos) < targetRadius)
			{
				return d;
			}
		}
		
		return null;
	}
	
	
	// TODO fix jumping splits when resizing (accumulates rounding errors)
	protected void layoutChildren()
	{
		Insets m = getInsets();
		double left = m.getLeft();
		double right = getWidth() - m.getRight();
		double top = m.getTop();
		double bot = getHeight() - m.getBottom();
		
		ObservableList<Node> cs = getItems();
		int sz = cs.size();
		
		if(horizontal)
		{
			// FIX if one item
			double width = right - left - ((sz - 1) * dividerWidth);
			double height = bot - top;
			
			// TODO min, max, pref
			
			for(int i=0; i<sz; i++)
			{
				Node n = cs.get(i);
				attach(n);
				
				// FIX last item
				Divider d = getDivider(i);
				double constraint = d.constraint;
				if(constraint == 0.0)
				{
					constraint = 1.0 / sz;
				}
				d.span = snapSize(constraint * width);
			}
			
			double x = left;
			for(int i=0; i<sz; i++)
			{
				Node n = cs.get(i);
				Divider d = getDivider(i);
				n.resize(d.span, height);
				positionInArea(n, x, top, d.span, height, 0, null, HPos.CENTER, VPos.CENTER, isSnapToPixel());
				
				x += d.span;
				
				Region bar = getDividerBar(i);
				bar.resize(dividerWidth, height);
				positionInArea(bar, x, top, dividerWidth, height, 0, null, HPos.CENTER, VPos.CENTER, isSnapToPixel());
				
				x += dividerWidth; 
				d.position = x;
			}
		}
		else
		{
			// FIX if one item
			double width = right - left;
			double height = bot - top  - ((sz - 1) * dividerWidth);
			
			// TODO min, max, pref
			
			for(int i=0; i<sz; i++)
			{
				Node n = cs.get(i);
				attach(n);
				
				// FIX last item
				Divider d = getDivider(i);
				double constraint = d.constraint;
				if(constraint == 0.0)
				{
					constraint = 1.0 / sz;
				}
				d.span = snapSize(constraint * height);
			}
			
			double y = top;
			for(int i=0; i<sz; i++)
			{
				Node n = cs.get(i);
				Divider d = getDivider(i);
				n.resize(width, d.span);
				positionInArea(n, left, y, width, d.span, 0, null, HPos.CENTER, VPos.CENTER, isSnapToPixel());
				
				y += d.span;
				
				Region bar = getDividerBar(i);
				bar.resize(width, dividerWidth);
				positionInArea(bar, left, y, width, dividerWidth, 0, null, HPos.CENTER, VPos.CENTER, isSnapToPixel());
				
				y += dividerWidth; 
				d.position = y;
			}
		}
		
		while(dividerBars.size() > sz)
		{
			Region r = dividerBars.remove(sz);
			getChildren().remove(r);
		}
	}
	
	
	//
	
	
	protected static class Divider
	{
		/** 0 ... 1.0 specifies percent, > 1.0 specifies fixed location */
		public double constraint;
		
		public double span;
		public double position;
		public double override;
	}
}
