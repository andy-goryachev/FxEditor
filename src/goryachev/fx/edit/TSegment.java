// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;


/**
 * Text Model Segment.
 */
public class TSegment
{
	private final TStyle style;
	private final String text;
	
	
	public TSegment(TStyle style, String text)
	{
		this.style = style;
		this.text = text;
	}
	
	
	public TStyle getStyle()
	{
		return style;
	}
	
	
	public String getText()
	{
		return text;
	}
}
