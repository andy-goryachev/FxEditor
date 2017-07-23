// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import javafx.scene.layout.Region;
import javafx.scene.shape.PathElement;


/**
 * Represents a box enclosing a single line of text.
 * 
 * TODO make this a node
 * TODO add getLeadingNode() and getTrailingNode()
 * TODO add style for line highlight
 * TODO getSelectionPath()
 */
public class LineBox
{
	private final int line;
	private final Region box;
	private double height;
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


	public void setHeight(double h)
	{
		height = h;
	}
	
	
	public double getHeight()
	{
		return height;
	}
	
	
	/** returns selection shape for a given range.  negative 'end' value is equivalent to the offset of the last symbol in the text */
	public PathElement[] getRange(int start, int end)
	{
		if(box instanceof CTextFlow)
		{
			CTextFlow t = (CTextFlow)box;
			if(end < 0)
			{
				end = t.getText().length();
			}
			return t.getRange(start, end);
		}
		return null;
	}
}