// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.fx.FX;
import goryachev.fx.FxCtl;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Labeled;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.PathElement;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;


/**
 * Represents a box enclosing a single line of text.
 */
public class LineBox
{
	private int lineNumber;
	private Labeled lineNumberComponent;
	private Region center;
	private double height;
	private static Insets PADDING = new Insets(0, 10, 0, 0);
	
	
	public LineBox()
	{
	}
	
	
	public LineBox(Region center)
	{
		setCenter(center);
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
	
	
	public LineBox addText(Text t)
	{
		text().getChildren().add(t);
		return this;
	}
	
	
	public LineBox addText(Text ... items)
	{
		text().getChildren().addAll(items);
		return this;
	}
	
	
	public void setLineNumberComponent(Labeled c)
	{
		lineNumberComponent = c;
	}


	public Labeled getLineNumberComponent()
	{
		if(lineNumberComponent == null)
		{
			lineNumberComponent = createLineNumberComponent();
		}
		return lineNumberComponent;
	}
	
	
	public Labeled getLineNumberComponentRaw()
	{
		return lineNumberComponent;
	}


	protected Labeled createLineNumberComponent()
	{
		return FX.label(FxEditor.LINE_NUMBER, Color.LIGHTGRAY, PADDING,  FxCtl.FORCE_MIN_WIDTH);
	}
}