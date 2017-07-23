// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit.internal;
import goryachev.fx.util.FxPathBuilder;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.PathElement;


/**
 * Selection Helper encapsulates the logic required to generate selection shapes.
 * 
 * The goal is to find out which shapes correspond to the top-most and bottom-most 
 * text rows (in the presence of wrapping).  These shapes should be added to selection as is.
 * The (possible) space in between would generate a single rectangular block that fills the
 * width of the container:
 * 
 *	 ----***--***----
 *	 ****************
 *	 ****************
 *	 ----**----------
 * 
 * This algorithm has a minor drawback where the top and bottom row selection may not go
 * to the left and right side of the container.  This is because I am too lazy to account 
 * for RTL/LTR text and wrapping.
 */
public class SelectionHelper
{
	private final FxPathBuilder pathBuilder;
	private double y0 = Double.NaN;
	private double y1 = Double.NaN;
	private double y2 = Double.NaN;
	private double y3 = Double.NaN;

	
	public SelectionHelper(FxPathBuilder b)
	{
		this.pathBuilder = b;
	}
	
	
	public String toString()
	{
		return 
			"y0=" + y0 +
			" y1=" + y1 +
			" y2=" + y2 +
			" y3=" + y3;
	}


	public void process(PathElement[] elements)
	{
		for(PathElement em: elements)
		{
			double y = getY(em);
			setTop(y);
			setBottom(y);
		}
	}


	public void generateTop(PathElement[] elements)
	{
		boolean include = false;
		double y;
		for(PathElement em: elements)
		{
			if(em instanceof LineTo)
			{
				if(include)
				{
					pathBuilder.add(em);
				}
			}
			else if(em instanceof MoveTo)
			{
				y = getY(em);
				if((y == y0) || (y == y1))
				{
					pathBuilder.add(em);
					include = true;
				}
				else
				{
					include = false;
				}
			}
		}
	}


	public void generateMiddle(double left, double right)
	{
		if(Double.isNaN(y0))
		{
			return;
		}
		
		if(y2 > y0)
		{
			// only if the middle exists
			pathBuilder.moveto(left, y1);
			pathBuilder.lineto(right, y1);
			pathBuilder.lineto(right, y2);
			pathBuilder.lineto(left, y2);
			pathBuilder.lineto(left, y1);
		}
	}


	public void generateBottom(PathElement[] elements)
	{
		boolean include = false;
		double y;
		for(PathElement em: elements)
		{
			if(em instanceof LineTo)
			{
				if(include)
				{
					pathBuilder.add(em);
				}
			}
			else if(em instanceof MoveTo)
			{
				y = getY(em);
				if((y == y2) || (y == y3))
				{
					pathBuilder.add(em);
					include = true;
				}
				else
				{
					include = false;
				}
			}
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
		if(isSmaller(y, y0))
		{
			if(isSmaller(y0, y1))
			{
				y1 = y0;
			}
			y0 = y;
		}
		else if(isSmaller(y, y1) && (y > y0))
		{
			y1 = y;
		}
	}
	

	protected void setBottom(double y)
	{
		if(isLarger(y, y3))
		{
			if(isLarger(y3, y2))
			{
				y2 = y3;
			}
			y3 = y;
		}
		else if(isLarger(y, y2) && (y < y3))
		{
			y2 = y;
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
}
