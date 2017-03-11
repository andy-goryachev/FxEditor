// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.common.util.FH;


/**
 * Text Position.
 */
public class TextPos
	implements Comparable<TextPos>
{
	private final int line;
	private final int index;
	private final boolean leading;
	
	
	public TextPos(int line, int index, boolean leading)
	{
		this.line = line;
		this.index = index;
		this.leading = leading;
	}
	
	
	public String toString()
	{
		return line + "." + index + (leading ? ".L" : ".T");
	}


	public int getInsertionIndex()
	{
		return leading ? index : index + 1;
	}
	
	
	public int getLine()
	{
		return line;
	}


	public int getIndex()
	{
		return index;
	}
	
	
	public boolean isLeading()
	{
		return leading;
	}
	
	
	public int compareTo(TextPos p)
	{
		int d = line - p.line;
		if(d == 0)
		{
			d = index - p.index;
			if(d == 0)
			{
				if(leading != p.leading)
				{
					return leading ? -1 : 1;
				}
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
		else if(x instanceof TextPos)
		{
			TextPos z = (TextPos)x;
			return (line == z.line) && (index == z.index) && (leading == z.leading);
		}
		else
		{
			return false;
		}
	}


	public int hashCode()
	{
		int h = FH.hash(TextPos.class);
		h = FH.hash(h, line);
		h = FH.hash(h, index);
		return FH.hash(h, leading);
	}
}
