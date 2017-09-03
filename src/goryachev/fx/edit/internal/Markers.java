// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit.internal;
import goryachev.common.util.WeakList;
import goryachev.fx.edit.Marker;
import java.lang.ref.WeakReference;
import java.util.List;


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


	public void update_OLD(int line, int startOffset, int endOffset, int inserted)
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
	
	
	/** 
	 * shifts all markers according to the following rules:
	 * 1. markers before 'min' are left unchanged
	 * 2. markers before 'max' are moved to 'min'
	 * 3. 'max' is moved to a position at the end of the inserted text.
	 * 4. markers after 'max' but on the same line has their offset shifted
	 * 5. markers below 'max' have their lines shifted 
	 */
	public void update(Marker min, Marker max, List<String> inserted)
	{
		// TODO verify this is the case
		int lineDelta = (max.getLine() - min.getLine()) + inserted.size() - 1;
		int offsetDelta = EditorTools.getLastLineLength(inserted) - max.getOffset();
		
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
				if(m.isBefore(min))
				{
					// unchanged
				}
				else if(m == max)
				{
					m.set(m.getLine() + lineDelta, m.getCharIndex() + offsetDelta, false);
//					m.addLine(lineDelta);
//					m.addOffset(offsetDelta);
				}
				else if(m.isBefore(max))
				{
					m.set(min);
				}
				else if(m.getLine() == max.getLine())
				{
					// move offset
					m.addOffset(offsetDelta);
				}
				else
				{
					// move line
					m.addLine(lineDelta);
				}
			}
		}
	}
}
