// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.common.util.FH;


/**
 * Marker represents a position in the text model maintained 
 * in the presence of insertion and removals.
 */
public class Marker
	implements Comparable<Marker>
{
	private int line;
	private int charIndex;
	private boolean leading;
	
	
	public Marker(int line, int charIndex, boolean leading)
	{
		this.line = line;
		this.charIndex = charIndex;
		this.leading = leading;
	}
	

	public int hashCode()
	{
		int h = FH.hash(Marker.class);
		h = FH.hash(h, line);
		h = FH.hash(h, charIndex);
		return FH.hash(h, leading);
	}


	/** returns the line number corresponding to this text position */
	public int getLine()
	{
		return line;
	}
	
	
	/** returns the position within the line */
	public int getLineOffset()
	{
		return leading ? charIndex : charIndex + 1;
	}
	
	
	public int getCharIndex()
	{
		return charIndex;
	}
	
	
	public boolean isLeading()
	{
		return leading;
	}
	
	
	public String toString()
	{
		return line + "." + charIndex + (leading ? ".L" : "T");
	}

	
	public int compareTo(Marker m)
	{
		int d = line - m.line;
		if(d == 0)
		{
			d = charIndex - m.charIndex;
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
			return (leading == m.leading) && (line == m.line) && (charIndex == m.charIndex);
		}
		else
		{
			return false;
		}
	}


	public boolean isBefore(Marker m)
	{
		if(line < m.line)
		{
			return true;
		}
		else if(line == m.line)
		{
			// TODO or use insertion index?
			if(charIndex < m.charIndex)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
}
