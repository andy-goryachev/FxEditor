// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import javafx.scene.layout.Region;
import javafx.scene.shape.PathElement;
import javafx.scene.text.Text;


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
	private int lineNumber;
	private Region left;
	private Region center;
	private Region right;
	private double height;
	// TODO leading component
	// TODO trailing component
	
	
	public LineBox()
	{
	}
	
	
	public String toString()
	{
		return "LineBox:" + lineNumber;
	}
	
	
	public void setCenter(Region n)
	{
		center = n;
	}


	public Region getCenter()
	{
		return center;
	}
	
	
	public void init(int lineNumber)
	{
		this.lineNumber = lineNumber;
	}
	
	
	public int getLineNumber()
	{
		return lineNumber;
	}


	public void setLineHeight(double h)
	{
		height = h;
	}
	
	
	public double getLineHeight()
	{
		return height;
	}
	
	
	public int getTextLength()
	{
		if(center instanceof CTextFlow)
		{
			CTextFlow t = (CTextFlow)center;
			return t.getText().length();
		}
		return 0;
	}
	
	
	/** returns selection shape for a given range */
	public PathElement[] getRange(int start, int end)
	{
		if(center instanceof CTextFlow)
		{
			CTextFlow t = (CTextFlow)center;
			return t.getRange(start, end);
		}
		return null;
	}
	
	
	/** returns selection shape for a given range */
	public PathElement[] getCaretShape(int index, boolean leading)
	{
		if(center instanceof CTextFlow)
		{
			CTextFlow t = (CTextFlow)center;
			return t.getCaretShape(index, leading);
		}
		return null;
	}
	
	
	/** returns the text flow node, creating it as necessary */
	public CTextFlow text()
	{
		if(center == null)
		{
			CTextFlow t = new CTextFlow();
			center = t;
			return t;
		}
		else if(center instanceof CTextFlow)
		{
			return (CTextFlow)center;
		}
		else
		{
			throw new Error("not a text row: " + center);
		}
	}
	
	
	public void addText(Text t)
	{
		text().getChildren().add(t);
	}
	
	
	public void addText(Text ... items)
	{
		text().getChildren().addAll(items);
	}
}