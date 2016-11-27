// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;


/**
 * Selection Segment.
 */
public class SelectionSegment
{
	protected final TextPos start;
	protected final TextPos end;
	
	
	public SelectionSegment(TextPos start, TextPos end)
	{
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
}
