// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit.internal;
import goryachev.common.util.SB;
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
	
	
	public String toString()
	{
		SB sb = new SB(128);
		sb.append("Markers(");
		boolean comma = false;
		for(Marker m: markers.asList())
		{
			if(comma)
			{
				sb.comma();
			}
			else
			{
				comma = true;
			}
			sb.append(m);
		}
		sb.append(")");
		return sb.toString();
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
	@Deprecated // TODO remove
	public void update(Marker min, Marker max, List<String> inserted)
	{
		// TODO verify this is the case
		int lineDelta = (max.getLine() - min.getLine()) + inserted.size() - 1;
		int offsetDelta = EditorTools.getLastLineLength(inserted) - max.getOffset();
		
		int sz = markers.size();
		for(int i=sz-1; i>=0; i--)
		{
			WeakReference<Marker> ref = markers.getRef(i);
			Marker m = ref.get(); 
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


	public void removed(Marker min, Marker max)
	{
		int lineDelta = (max.getLine() - min.getLine());
		
		int sz = markers.size();
		for(int i=sz-1; i>=0; i--)
		{
			WeakReference<Marker> ref = markers.getRef(i);
			Marker m = ref.get(); 
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
				else if(m.isBefore(max))
				{
					m.set(min);
				}
//				else if(m == max)
//				{
//					if(lineDelta == 0)
//					{
//						// same line
//						m.addOffset(-(max.getOffset() - min.getOffset()));
//					}
//					else
//					{
//						// 
//					}
//					m.set(m.getLine(), m.getCharIndex() + offsetDelta, false);
//				}
				else if(m.getLine() == max.getLine())
				{
					// move offset
					m.addOffset(-max.getOffset());
				}
				else
				{
					// move line
					m.addLine(lineDelta);
				}
			}
		}
	}
	
	
	public void inserted(Marker at, int lineCount)
	{
		int sz = markers.size();
		for(int i=sz-1; i>=0; i--)
		{
			WeakReference<Marker> ref = markers.getRef(i);
			Marker m = ref.get(); 
			if(m == null)
			{
				markers.remove(i);
			}
			else
			{
				if(m.isBefore(at))
				{
					// unchanged
				}
				else if(m.getLine() == at.getLine())
				{
					// both line and offset are affected
					m.addLine(lineCount);
					m.addOffset(-at.getOffset());
				}
				else
				{
					// move line
					m.addLine(lineCount);
				}
			}
		}
	}
}
