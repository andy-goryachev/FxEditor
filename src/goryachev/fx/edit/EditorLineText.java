// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.fx.CPane;
import goryachev.fx.CssStyle;
import goryachev.fx.FX;
import java.text.NumberFormat;
import javafx.geometry.Pos;
import javafx.scene.Node;


/**
 * Editor text line.
 * 
 * Depending on the editor's text wrap property, the text may span one
 * or multiple rows, layed out by CTextFlow.
 */
public class EditorLineText
	extends EditorLineBase
{
	private CTextFlow textFlow;
	
	
	public EditorLineText()
	{
	}
	
	
	/** returns the text flow node, creating it as necessary */
	public CTextFlow text()
	{
		if(textFlow == null)
		{
			textFlow = new CTextFlow();
			setCenter(textFlow);
		}
		return textFlow;
	}
}
