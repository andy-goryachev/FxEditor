// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit.internal;
import goryachev.common.util.WeakList;
import goryachev.fx.edit.Marker;


/**
 * Maintains weak list of Markers.
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
		Marker m = new Marker(lineNumber, charIndex, leading);
		markers.add(m);
		// TODO perhaps check for uncontrollable growth here
		return m;
	}
	
	
	public void clear()
	{
		markers.clear();
	}
}
