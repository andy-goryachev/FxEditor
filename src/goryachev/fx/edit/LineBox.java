// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.fx.FX;
import goryachev.fx.FxCtl;
import goryachev.fx.util.FxPathBuilder;
import javafx.geometry.Insets;
import javafx.scene.control.Labeled;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.PathElement;
import javafx.scene.text.Text;


/**
 * Represents a box enclosing a logical line of text.
 * Typically, it contains a CTextFlow which may be rendered on several rows in the view,
 * or it may contain a single Region representing a non-text component.
 */
public class LineBox
{
	private int lineNumber;
	private Labeled lineNumberComponent;
	private Region center;
	private double height;
	private double y;
	private static Insets LINE_NUMBERS_PADDING = new Insets(0, 7, 0, 0);
	
	
	public LineBox()
	{
	}
	
	
	public LineBox(Region center)
	{
		setCenter(center);
	}
	
	
	public static LineBox createTextBox()
	{
		return new LineBox(new CTextFlow());
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
		if(center == null)
		{
			center = new CTextFlow();
		}
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
	
	
	public double getY()
	{
		return y;
	}
	
	
	public void setY(double y)
	{
		this.y = y;
	}


	public void setHeight(double h)
	{
		height = h;
	}
	
	
	public double getHeight()
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
		return FX.label(FxEditor.LINE_NUMBER, Color.LIGHTGRAY, LINE_NUMBERS_PADDING,  FxCtl.FORCE_MIN_WIDTH);
	}


	public void addBoxOutline(FxPathBuilder b, double w)
	{
		double y0 = center.getLayoutY();
		double y1 = y0 + center.getHeight();
		
		b.moveto(0, y0);
		b.lineto(w, y0);
		b.lineto(w, y1);
		b.lineto(0, y1);
		b.lineto(0, y0);
	}
}