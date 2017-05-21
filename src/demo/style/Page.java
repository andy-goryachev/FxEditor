// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package demo.style;
import goryachev.common.util.Log;
import javafx.scene.Node;


/**
 * Demo Page.
 */
public class Page
{
	public final String text;
	public final Class<? extends Node> type;
	
	
	public Page(String text, Class<? extends Node> c)
	{
		this.text = text;
		this.type = c;
	}
	
	
	public String toString()
	{
		return text;
	}


	public Node getNode()
	{
		try
		{
			return type.newInstance();
		}
		catch(Exception e)
		{
			Log.ex(e);
			return null;
		}
	}
}
