// Copyright © 2017-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit.internal;
import goryachev.common.util.SB;
import goryachev.fx.FX;
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
 * of the selected text depending on the direction of text.
 * 
 * TODO RTL text
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
	private double topLeft = Double.NaN;
	private double topRight = Double.NaN;
	private double bottomUp = Double.NaN;
	private double bottomDn = Double.NaN;
	private double bottomLeft = Double.NaN;
	private double bottomRight = Double.NaN;

	
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
			" botUp=" + bottomUp +
			" botDn=" + bottomDn;
	}


	// computes edge coordinates for middle, top trailing, and bottom leading sections
	protected void process(PathElement[] elements)
	{
		for(PathElement em: elements)
		{
			double x;
			double y;
			
			if(em instanceof LineTo)
			{
				LineTo m = (LineTo)em;
				x = m.getX();
				y = m.getY();
			}
			else if(em instanceof MoveTo)
			{
				MoveTo m = (MoveTo)em;
				x = m.getX();
				y = m.getY();
			}
			else
			{
				throw new Error("?" + em);
			}
			
			setTop(x, y);
			setBottom(x, y);
		}
	}


	protected void generateMiddle(boolean topLTR, boolean bottomLTR)
	{
		if(Double.isNaN(topUp))
		{
			return;
		}
		
		// only if the middle exists
		if(bottomUp > topDn)
		{
			if(topLTR)
			{
				pathBuilder.moveto(topRight, topUp);
				pathBuilder.lineto(right, topUp);
				pathBuilder.lineto(right, topDn);
				pathBuilder.lineto(topRight, topDn);
				pathBuilder.lineto(topRight, topUp);
			}
			else
			{
				// TODO
			}
			
			pathBuilder.moveto(left, topDn);
			pathBuilder.lineto(right, topDn);
			pathBuilder.lineto(right, bottomUp);
			pathBuilder.lineto(left, bottomUp);
			pathBuilder.lineto(left, topDn);
			
			// TODO trailer
		}
	}

	
	protected void setTop(double x, double y)
	{
		boolean setx = false;
		
		if(isSmaller(y, topUp))
		{
			if(isSmaller(topUp, topDn))
			{
				topDn = topUp;
				setx = true;
			}
			topUp = y;
		}
		else if(isSmaller(y, topDn) && (y > topUp))
		{
			topDn = y;
			setx = true;
		}
		
		if(setx)
		{
			if(isSmaller(x, topLeft))
			{
				topLeft = x;
			}
			
			if(!isSmaller(topRight, x))
			{
				topRight = x;
			}
		}
	}
	

	protected void setBottom(double x, double y)
	{
		boolean setx = false;
		
		if(isLarger(y, bottomDn))
		{
			if(isLarger(bottomDn, bottomUp))
			{
				bottomUp = bottomDn;
				setx = true;
			}
			bottomDn = y;
		}
		else if(isLarger(y, bottomUp) && (y < bottomDn))
		{
			bottomUp = y;
			setx = true;
		}
		
		if(setx)
		{
			if(isSmaller(x, bottomLeft))
			{
				bottomLeft = x;
			}
			
			if(!isSmaller(x, bottomRight))
			{
				bottomRight = x;
			}
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
	
	
	private static int r(double x)
	{
		return FX.round(x);
	}
	
	
	private static String dump(PathElement[] elements)
	{
		SB sb = new SB();
		if(elements == null)
		{
			sb.append("null");
		}
		else
		{
			for(PathElement em: elements)
			{
				if(em instanceof MoveTo)
				{
					MoveTo m = (MoveTo)em;
					sb.a("M");
					sb.a(r(m.getX()));
					sb.a(",");
					sb.a(r(m.getY()));
					sb.a(" ");
				}
				else if(em instanceof LineTo)
				{
					LineTo m = (LineTo)em;
					sb.a("L");
					sb.a(r(m.getX()));
					sb.a(",");
					sb.a(r(m.getY()));
					sb.a(" ");
				}
			}
		}
		return sb.toString();
	}


	public void generate(PathElement[] top, PathElement[] bottom, boolean topLTR, boolean bottomLTR)
	{		
		process(top);
		
		if(bottom == null)
		{
			pathBuilder.addAll(top);
		}
		else
		{
			process(bottom);
			
//			D.print("top", dump(top), "bottom", dump(bottom)); // FIX
//			D.print(" top=(" + r(topLeft) + "x" + r(topUp) + ")..(" + r(topRight) + "x" + r(topDn) + ")");
//			D.print(" bot=(" + r(bottomLeft) + "x" + r(bottomUp) + ")..(" + r(bottomRight) + "x" + r(bottomDn) + ")");
			
			pathBuilder.addAll(top);
			generateMiddle(topLTR, bottomLTR);
			pathBuilder.addAll(bottom);
		}
	}
}
