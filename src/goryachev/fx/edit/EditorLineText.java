// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import javafx.scene.shape.PathElement;


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
	
	
	
	/** returns selection shape for a given range.  negative 'end' value is equivalent to the offset of the last symbol in the text */
	public PathElement[] getRange(int start, int end)
	{
		CTextFlow t = text();
		if(end < 0)
		{
			end = t.getText().length();
		}
		return t.getRange(start, end);
	}
}
