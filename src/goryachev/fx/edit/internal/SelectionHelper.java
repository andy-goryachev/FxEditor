// Copyright © 2017-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit.internal;
import goryachev.fx.util.FxPathBuilder;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.PathElement;


/**
 * Selection Helper encapsulates the logic required to generate selection shapes.
 * 
 * The goal is to find out which shapes correspond to the top-most and bottom-most 
 * text rows (in the presence of wrapping).  These shapes (#) should be added to selection as is.
 * Any space in between (x) would generate a single rectangular block that fills the
 * width of the container.  Additional shapes (#) will be added when necessary to make
 * the selection appear contiguious.  These shapes are positioned to the left or to the right
 * of the selected text depending on the direction of text. - last part TODO
 * 
 *	 ----***--***####
 *	 xxxxxxxxxxxxxxxx
 *	 xxxxxxxxxxxxxxxx
 *	 ####**----------
 */
public class SelectionHelper
{
	private final FxPathBuilder pathBuilder;
	private final double left;
	private final double right;
	private double topUp = Double.NaN;
	private double topDn = Double.NaN;
	private double botUp = Double.NaN;
	private double botDn = Double.NaN;

	
	public SelectionHelper(FxPathBuilder b, double left, double right)
	{
		this.pathBuilder = b;
		this.left = left;
		this.right = right;
	}
	
	
	public String toString()
	{
		return 
			"topUp=" + topUp +
			" topDn=" + topDn +
			" botUp=" + botUp +
			" botDn=" + botDn;
	}


	protected void process(PathElement[] elements)
	{
		for(PathElement em: elements)
		{
			double y = getY(em);
			setTop(y);
			setBottom(y);
		}
	}


	protected void generateMiddle()
	{
		if(Double.isNaN(topUp))
		{
			return;
		}
		
		if(botUp > topDn)
		{
			// only if the middle exists
			pathBuilder.moveto(left, topDn);
			pathBuilder.lineto(right, topDn);
			pathBuilder.lineto(right, botUp);
			pathBuilder.lineto(left, botUp);
			pathBuilder.lineto(left, topDn);
		}
	}


	protected double getY(PathElement em)
	{
		if(em instanceof LineTo)
		{
			LineTo m = (LineTo)em;
			return m.getY();
		}
		else if(em instanceof MoveTo)
		{
			MoveTo m = (MoveTo)em;
			return m.getY();
		}
		else
		{
			throw new Error("?" + em);
		}
	}
	
	
	protected void setTop(double y)
	{
		if(isSmaller(y, topUp))
		{
			if(isSmaller(topUp, topDn))
			{
				topDn = topUp;
			}
			topUp = y;
		}
		else if(isSmaller(y, topDn) && (y > topUp))
		{
			topDn = y;
		}
	}
	

	protected void setBottom(double y)
	{
		if(isLarger(y, botDn))
		{
			if(isLarger(botDn, botUp))
			{
				botUp = botDn;
			}
			botDn = y;
		}
		else if(isLarger(y, botUp) && (y < botDn))
		{
			botUp = y;
		}
	}
	

	protected boolean isSmaller(double y, double current)
	{
		if(Double.isNaN(y))
		{
			return false;
		}
		else if(Double.isNaN(current))
		{
			return true;
		}
		else if(y < current)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	
	protected boolean isLarger(double y, double current)
	{
		if(Double.isNaN(y))
		{
			return false;
		}
		else if(Double.isNaN(current))
		{
			return true;
		}
		else if(y > current)
		{
			return true;
		}
		else
		{
			return false;
		}
	}


	public void generate(PathElement[] top, PathElement[] bottom, boolean topLTF, boolean bottomLTR)
	{
		process(top);
		
		if(bottom == null)
		{
			pathBuilder.addAll(top);
		}
		else
		{
			process(bottom);
			
			pathBuilder.addAll(top);
			// TODO add top line trailer
			generateMiddle();
			// TODO add bottom line leader
			pathBuilder.addAll(bottom);
		}
	}
}
