// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import javafx.scene.layout.Region;


/**
 * Represents a box enclosing a single line of text.
 */
public class LineBox
{
	private final int line;
	private final Region box;
	// TODO leading component
	// TODO trailing component
	
	
	public LineBox(int line, Region box)
	{
		this.line = line;
		this.box = box;
	}


	public Region getBox()
	{
		return box;
	}
	
	
	public int getLineNumber()
	{
		return line;
	}
}