// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.fx.CPane;
import goryachev.fx.CssStyle;
import goryachev.fx.FX;
import java.text.NumberFormat;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.PathElement;


/**
 * Base component representing a line in the editor.
 * Depending on the actual implementation, a line might be a text line
 * spanning several rows (when text wrap is enabled), or an image, 
 * or some other component.
 */
public abstract class EditorLineBase
	extends CPane
{
	public final static CssStyle TEXT_LINE = new CssStyle("TextLine_TEXT_LINE");
	public final static CssStyle LINE_NUMBERS = new CssStyle("TextLine_LINE_NUMBERS");
	
	private int line;
	private double height;
	private static NumberFormat format;
	
	
	public EditorLineBase()
	{
		FX.style(this, TEXT_LINE);
	}
	
	
	public Node getLeadingNode()
	{
		return getLeft();
	}
	
	
	public void setLeadingNode(Node n)
	{
		setLeft(n);
	}
	
	
	public Node getTrailingNode()
	{
		return getRight();
	}
	
	
	public void setTrailingNode(Node n)
	{
		setRight(n);
	}
	
	
	public static void setLineNumberFormat(NumberFormat f)
	{
		format = f;
	}
	
	
	/** override for custom format (when using default node) */
	protected NumberFormat format()
	{
		if(format == null)
		{
			format = NumberFormat.getIntegerInstance();
		}
		return format;
	}
	
	
	public void setLineNumber(int num)
	{
		this.line = num;
		
		// TODO extract into a separate call
		//setLeadingNode(createLineNumberNode(num));
	}
	
	
	public int getLineNumber()
	{
		return line;
	}
	
	
	/** override for custom line number node */
	protected Node createLineNumberNode(int num)
	{
		return FX.label(LINE_NUMBERS, Pos.CENTER_RIGHT, format().format(num));
	}


	public void setLineHeight(double h)
	{
		height = h;
	}
	
	
	public double getLineHeight()
	{
		return height;
	}
	
	
	/** returns selection shape for a given range.  the base class returns a rectangular shape that envelopes the whole node */
	public PathElement[] getRange(int start, int end)
	{
		double w = getWidth();
		return new PathElement[]
		{
			new MoveTo(0, 0),
			new LineTo(w, 0),
			new LineTo(w, height),
			new LineTo(0, height),
			new LineTo(0, 0)
		};
	}


	/** returns caret shape */
	public PathElement[] getCaretShape(Marker m)
	{
		// TODO perhaps returns a full height caret at the start/end of the block?
		return null;
	}


	public CHitInfo getHit(double x, double y)
	{
		return null;
	}

	
	public int getTextLength()
	{
		return 0;
	}
}
