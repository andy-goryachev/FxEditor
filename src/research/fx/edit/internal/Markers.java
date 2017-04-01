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
}
