// Copyright © 2017-2018 Andy Goryachev <andy@goryachev.com>
package demo.edit;

import javafx.scene.paint.Color;

/**
 * Segment.
 */
public class Segment
{
	public final String text;
	public final Color color;
	
	
	public Segment(Color color, String text)
	{
		this.text = text;
		this.color = color;
	}
}