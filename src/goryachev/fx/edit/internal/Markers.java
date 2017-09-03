// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit.internal;
import goryachev.common.util.WeakList;
import goryachev.fx.edit.Marker;
import java.lang.ref.WeakReference;


/**
 * Maintains weak list of Markers.
 * This editor-specific class is needed to allow for marker adjustment after an editing operation.
 */
public class Markers
{
	private final WeakList<Marker> markers;
	
	
	public Markers(int size)
	{
		markers = new WeakList<Marker>();
	}


	public Marker newMarker(int lineNumber, int charIndex, boolean leading)
	{
		Marker m = new Marker(this, lineNumber, charIndex, leading);
		markers.add(m);
		
		if(markers.size() > 1000000)
		{
			throw new Error("too many markers");
		}
		
		return m;
	}
	
	
	public void clear()
	{
		markers.clear();
	}


	public void update(int line, int startOffset, int endOffset, int inserted)
	{
		int sz = markers.size();
		for(int i=sz-1; i>=0; i--)
		{
			WeakReference<Marker> ref = markers.getRef(i);
			Marker m =ref.get(); 
			if(m == null)
			{
				markers.remove(i);
			}
			else
			{
				if(m.isBefore(line, startOffset))
				{
					// unchanged
				}
				else if(m.isAfter(line, endOffset))
				{
					// unchanged
				}
				else
				{
					// move to end position
					m.set(line, endOffset, false);
				}
			}
		}
	}
}
