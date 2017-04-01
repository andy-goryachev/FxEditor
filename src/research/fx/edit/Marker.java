// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.common.util.FH;


/**
 * Marker represents a position in the text model maintained 
 * in the presence of insertion and removals.
 */
public class Marker
	implements Comparable<Marker>
{
	private int line;
	private int offset;
	private boolean leading;
	
	
	public Marker(int line, int offset, boolean leading)
	{
		this.line = line;
		this.offset = offset;
		this.leading = leading;
	}
	
	
	/** returns the line number corresponding to this text position */
	public int getLine()
	{
		return line;
	}
	
	
	/** returns the position within the line */
	public int getLineOffset()
	{
		return offset;
	}
	
	
	public boolean isLeading()
	{
		return leading;
	}
	
	
	public String toString()
	{
		return line + "." + offset + (leading ? ".L" : "T");
	}

	
	public int compareTo(Marker m)
	{
		int d = line - m.line;
		if(d == 0)
		{
			d = offset - m.offset;
			if(leading != m.leading)
			{
				return leading ? -1 : 1;
			}
		}
		return d;
	}
	
	
	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		else if(x instanceof Marker)
		{
			Marker m = (Marker)x;
			return (leading == m.leading) && (line == m.line) && (offset == m.offset);
		}
		else
		{
			return false;
		}
	}


	public int hashCode()
	{
		int h = FH.hash(Marker.class);
		h = FH.hash(h, line);
		h = FH.hash(h, offset);
		return FH.hash(h, leading);
	}
}
