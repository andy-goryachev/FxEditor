// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.common.util.Assert;
import goryachev.common.util.FH;


/**
 * Encapsulates a single Selection Segment.
 */
public class SelectionSegment
{
	protected final TextPos start;
	protected final TextPos end;
	
	
	public SelectionSegment(TextPos start, TextPos end)
	{
		Assert.notNull(start, "start");
		Assert.notNull(end, "end");

		this.start = start;
		this.end = end;
	}
	
	
	public String toString()
	{
		return "[" + start + "-" + end + "]";
	}
	
	
	public TextPos getStart()
	{
		return start;
	}
	
	
	public TextPos getEnd()
	{
		return end;
	}


	public boolean contains(TextPos p)
	{
		if(p != null)
		{
			int st = start.compareTo(p);
			int en = end.compareTo(p);
			
			if((st >= 0) && (en <= 0))
			{
				return true;
			}
			else if((st <= 0) && (en >= 0))
			{
				return true;
			}
		}
		return false;
	}
	
	
	public int hashCode()
	{
		int h = FH.hash(SelectionSegment.class);
		h = FH.hash(h, start);
		return FH.hash(h, end);
	}
	
	
	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		else if(x instanceof SelectionSegment)
		{
			SelectionSegment z = (SelectionSegment)x;
			return 
				start.equals(z.start) && 
				end.equals(z.end);
		}
		else
		{
			return false;
		}
	}
}
