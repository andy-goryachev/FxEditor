// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.common.util.CKit;
import goryachev.common.util.FH;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


/**
 * Text Segment Style.
 * 
 * TODO relative font size? font family?
 */
public class TStyle
{
	private static final int BOLD = 0x0001;
	private static final int ITALIC = 0x0002;
	private static final int UNDERLINE = 0x0004;
	private static final int SUPERSCRIPT = 0x0008;
	private static final int SUBSCRIPT = 0x0010;
	private static final int STRIKETHROUGH = 0x0020;
	
	private int flags;
	private Color bg;
	private Color fg;
	private String style;
	private Font font;
	private Node node;
	
	
	public TStyle()
	{
	}
	
	
	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		else if(x instanceof TStyle)
		{
			TStyle z = (TStyle)x;
			return
				(flags == z.flags) &&
				CKit.equals(fg, z.fg) &&
				CKit.equals(bg, z.bg) &&
				CKit.equals(style, z.style) &&
				CKit.equals(font, z.font) &&
				(node == z.node);
		}
		else
		{
			return false;
		}
	}
	
	
	public int hashCode()
	{
		int h = FH.hash(TStyle.class);
		h = FH.hash(h, flags);
		h = FH.hash(h, bg);
		h = FH.hash(h, fg);
		h = FH.hash(h, style);
		h = FH.hash(h, font);
		h = FH.hash(h, node);
		return h;
	}
	
	
	public boolean isBold()
	{
		return getFlag(BOLD);
	}
	
	
	public void setBold(boolean on)
	{
		setFlag(BOLD, on);
	}
	
	
	public boolean isItalic()
	{
		return getFlag(ITALIC);
	}
	
	
	public void setItalic(boolean on)
	{
		setFlag(ITALIC, on);
	}
	
	
	public boolean isUnderline()
	{
		return getFlag(UNDERLINE);
	}
	
	
	public void setUnderline(boolean on)
	{
		setFlag(UNDERLINE, on);
	}
	
	
	public boolean isSuperScript()
	{
		return getFlag(SUPERSCRIPT);
	}
	
	
	public void setSuperScript(boolean on)
	{
		setFlag(SUPERSCRIPT, on);
	}
	
	
	public boolean isSubScript()
	{
		return getFlag(SUBSCRIPT);
	}
	
	
	public void setSubScript(boolean on)
	{
		setFlag(SUBSCRIPT, on);
	}
	
	
	public boolean isStrikeThrough()
	{
		return getFlag(STRIKETHROUGH);
	}
	
	
	public void setStrikeThrough(boolean on)
	{
		setFlag(STRIKETHROUGH, on);
	}
	
	
	protected boolean getFlag(int flag)
	{
		return ((flags & flag) != 0);
	}
	
	
	protected void setFlag(int flag, boolean on)
	{
		if(on)
		{
			flags |= flag;
		}
		else
		{
			flags &= (~flag);
		}
	}
	
	
	public Color getForeground()
	{
		return fg;
	}
	
	
	public void setForeground(Color c)
	{
		fg = c;
	}
	
	
	public Color getBackground()
	{
		return bg;
	}
	
	
	public void setBackground(Color c)
	{
		bg = c;
	}
	
	
	public String getStyle()
	{
		return style;
	}
	
	
	public void setStyle(String s)
	{
		style = s;
	}
	
	
	public Font getFont()
	{
		return font;
	}
	
	
	public void setFont(Font f)
	{
		font = f;
	}

	
	public Node getNode()
	{
		return node;
	}
	
	
	public void setNode(Node n)
	{
		node = n;
	}
}
