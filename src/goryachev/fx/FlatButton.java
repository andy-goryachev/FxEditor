// Copyright © 2016-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;


/**
 * Flat Button.
 */
public class FlatButton
	extends FxButton
{
	public static final CssStyle STYLE = new CssStyle("FlatButton_STYLE");
	
	
	public FlatButton(String text, FxAction a)
	{
		super(text, a);
		FX.style(this, STYLE);
	}
	
	
	public FlatButton(String text, Runnable handler)
	{
		super(text, handler);
		FX.style(this, STYLE);
	}
	
	
	public FlatButton(String text)
	{
		super(text);
		FX.style(this, STYLE);
	}
}
