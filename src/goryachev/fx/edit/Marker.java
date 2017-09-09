// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.common.util.Assert;
import goryachev.common.util.FH;
import goryachev.common.util.SB;
import goryachev.fx.edit.internal.Markers;


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
	
	
	public Marker(Markers owner, int line, int charIndex, boolean leading)
	{
		Assert.notNull(owner, "owner");
		
		this.line = line;
		this.charIndex = charIndex;
		this.leading = leading;		
	}
	
	
	public String toString()
	{
		SB sb = new SB(16);
		sb.a("M(");
		sb.a(line);
		sb.a(':');
		sb.a(getCharIndex());
		if(leading)
		{
			sb.a('L');
		}
		else
		{
			sb.a('T');
		}
		sb.a(':');
		sb.a(getOffset());
		sb.a(")");
		return sb.toString();
	}


	public int hashCode()
	{
		int h = FH.hash(Marker.class);
		h = FH.hash(h, line);
		h = FH.hash(h, charIndex);
		return FH.hash(h, leading);
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


	/** returns the line index */
	public int getLine()
	{
		return line;
	}
	
	
	/** returns the effective caret position (insert point) */
	public int getOffset()
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
	
	
	public int compareTo(Marker m)
	{
		int d = line - m.line;
		if(d == 0)
		{
			d = getOffset() - m.getOffset();
			if(d == 0)
			{
				if(leading != m.leading)
				{
					return leading ? -1 : 1;
				}
			}
		}
		return d;
	}
	

	public boolean isBefore(Marker m)
	{
		if(line < m.line)
		{
			return true;
		}
		else if(line == m.line)
		{
			if(getOffset() < m.getOffset())
			{
				return true;
			}
			else if(getOffset() == m.getOffset())
			{
				if(leading == m.leading)
				{
					// same but not before
					return false;
				}
				else
				{
					return leading;
				}
			}
		}
		
		return false;
	}


	public boolean isBefore(int line, int startOffset)
	{
		if(this.line < line)
		{
			return true;
		}
		else if(this.line == line)
		{
			if(getOffset() < startOffset)
			{
				return true;
			}
		}
		
		return false;
	}
	
	
	public boolean isAfter(int line, int startOffset)
	{
		if(this.line > line)
		{
			return true;
		}
		else if(this.line == line)
		{
			if(getOffset() > startOffset)
			{
				return true;
			}
		}
		
		return false;
	}
	
	
	public void set(int line, int index, boolean leading)
	{
		this.line = line;
		this.charIndex = index;
		this.leading = leading;
	}
	
	
	public void set(Marker m)
	{
		this.line = m.getLine();
		this.charIndex = m.getCharIndex();
		this.leading = m.isLeading();
	}
	
	
	public void setCharIndex(int ix)
	{
		charIndex = ix;
	}
	
	
	public void setLeading(boolean on)
	{
		leading = on;
	}
	
	
	public void setLine(int ix)
	{
		line = ix;
	}


	public void addLine(int delta)
	{
		line += delta;
	}
	
	
	public void addOffset(int delta)
	{
		charIndex += delta;
	}
}
