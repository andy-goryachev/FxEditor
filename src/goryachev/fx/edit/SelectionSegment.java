// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.common.util.Assert;
import goryachev.common.util.FH;


/**
 * Encapsulates a single Selection Segment.
 */
public class SelectionSegment
{
	protected final Marker start;
	protected final Marker end;
	
	
	public SelectionSegment(Marker start, Marker end)
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
	
	
	/** returns the start (anchor) position */
	public Marker getStart()
	{
		return start;
	}
	
	
	/** returns the end (caret) position, may be before or after the anchor */
	public Marker getEnd()
	{
		return end;
	}
	
	
	/** returns a marker which is closer to the beginning of the text */
	public Marker getTop()
	{
		if(start.isBefore(end))
		{
			return start;
		}
		else
		{
			return end;
		}
	}
	
	
	/** returns a marker which is further from the beginning of the text */
	public Marker getBottom()
	{
		if(end.isBefore(start))
		{
			return start;
		}
		else
		{
			return end;
		}
	}


	public boolean contains(Marker p)
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


	public boolean isEmpty()
	{
		if(start.getLine() == end.getLine())
		{
			if(start.getLineOffset() == end.getLineOffset())
			{
				return true;
			}
		}
		return false;
	}


	/** returns overlapping segment or null if segments do not overlap.  the caret (end marker) is chosen from this segment */
	public SelectionSegment union(SelectionSegment s)
	{
		Marker m0 = s.getTop();
		Marker m1 = s.getBottom();
		
		if(contains(m0))
		{
			if(contains(m1))
			{
				// overlaps fully
				return this;
			}
			else
			{
				if(getTop() == start)
				{
					return new SelectionSegment(start, m1);
				}
				else
				{
					return new SelectionSegment(m1, end);
				}
			}
		}
		else
		{
			if(contains(m1))
			{
				if(getTop() == start)
				{
					return new SelectionSegment(m0, end);
				}
				else
				{
					return new SelectionSegment(end, m0);
				}
			}
			else
			{
				// no overlap
				return null;
			}
		}
	}


	/** returns a segment in which the start marker always comes before the end marker */
	public SelectionSegment getNormalizedSegment()
	{
		if(start.isBefore(end))
		{
			return this;
		}
		else
		{
			return new SelectionSegment(end, start);
		}
	}
}
