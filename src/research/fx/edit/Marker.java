// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;


/**
 * Marker represents a position in the text model maintained 
 * in the presence of insertion and removals.
 */
public interface Marker
{
	/** returns plain text position within text.  TODO long? */
	public int getTextOffset();
	
	
	/** returns the line number corresponding to this text position */
	public int getLine();
	
	
	/** returns the position within the line */
	public int getLineOffset();
}
