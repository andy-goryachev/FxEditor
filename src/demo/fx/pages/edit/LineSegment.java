// Copyright © 2017-2019 Andy Goryachev <andy@goryachev.com>
package demo.fx.pages.edit;
import goryachev.common.util.CKit;
import goryachev.common.util.FH;


/**
 * Line Segment.
 */
public class LineSegment
{
	public final LineType type;
	public final String text;
	
	
	public LineSegment(LineType type, String text)
	{
		this.type = type;
		this.text = text;
	}
	

	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		else if(x instanceof LineSegment)
		{
			LineSegment s = (LineSegment)x;
			return (type == s.type) && CKit.equals(text, s.text);
		}
		else
		{
			return false;
		}
	}


	public int hashCode()
	{
		int h = FH.hash(getClass());
		h = FH.hash(h, type);
		return FH.hash(h, text);
	}
}
