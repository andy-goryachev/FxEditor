// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import javafx.scene.shape.PathElement;
import javafx.scene.text.Text;


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
			setCenterNode(textFlow);
		}
		return textFlow;
	}
	
	
	public void addText(Text t)
	{
		text().getChildren().add(t);
	}
	
	
	public void addText(Text ... items)
	{
		text().getChildren().addAll(items);
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
	
	
	public PathElement[] getCaretShape(Marker m)
	{
		return text().getCaretShape(m.getCharIndex(), m.isLeading());
	}
	
	
	public CHitInfo getHit(double x, double y)
	{
		return text().getHit(x, y);
	}
	
	
	public int getTextLength()
	{
		String s = text().getText();
		return s == null ? 0 : s.length();
	}
}
