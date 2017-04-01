// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package research.fx.edit.internal;
import goryachev.common.util.WeakList;
import research.fx.edit.Marker;


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


	public Marker newMarker(int lineNumber, int pos, boolean leading)
	{
		Marker m = new Marker(lineNumber, pos, leading);
		markers.add(m);
		// TODO perhaps check for uncontrollable growth here
		return m;
	}
	
	
	public void clear()
	{
		markers.clear();
	}
}
