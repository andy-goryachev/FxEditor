// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.common.util.CList;
import goryachev.common.util.SB;
import java.util.Iterator;


/**
 * Convenient List of Text Model Segments.
 */
@Deprecated // FIX remove
public class TSegments
	implements Iterable<TSegment>
{
	private final CList<TSegment> segments;
	private int length;
	private String plain;
	
	
	public TSegments(int capacity)
	{
		segments = new CList(capacity);
	}
	
	
	public TSegments()
	{
		this(8);
	}
	
	
	public void add(TStyle style, String text)
	{
		segments.add(new TSegment(style, text));
		length += text.length();
	}
	
	
	public String getPlainText()
	{
		if(plain == null)
		{
			SB sb = new SB(length);
			for(TSegment s: segments)
			{
				sb.append(s.getText());
			}
			plain = sb.toString();
		}
		return plain;
	}
	
	
	public TSegment getSegment(int ix)
	{
		return segments.get(ix);
	}
	
	
	public int getTextLength()
	{
		return length;
	}
	
	
	public int getSegmentCount()
	{
		return segments.size();
	}


	public Iterator<TSegment> iterator()
	{
		return segments.iterator();
	}
}
