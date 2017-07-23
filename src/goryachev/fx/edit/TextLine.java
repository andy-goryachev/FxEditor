// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.fx.CPane;
import goryachev.fx.CssStyle;
import goryachev.fx.FX;
import java.text.NumberFormat;
import javafx.geometry.Pos;
import javafx.scene.Node;


/**
 * Component represents a line of text.
 */
public class TextLine
	extends CPane
{
	public final static CssStyle TEXT_LINE = new CssStyle("TextLine_TEXT_LINE");
	public final static CssStyle LINE_NUMBERS = new CssStyle("TextLine_LINE_NUMBERS");
	
	private int line;
	private CTextFlow textFlow;
	private double height;
	private static NumberFormat format;
	
	
	public TextLine()
	{
		FX.style(this, TEXT_LINE);
	}
	
	
	public CTextFlow text()
	{
		if(textFlow == null)
		{
			textFlow = new CTextFlow();
			setCenter(textFlow);
		}
		return textFlow;
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
		setLeadingNode(createLineNumberNode(num));
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
}
