// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.common.util.CList;
import goryachev.common.util.D;
import javafx.scene.text.Text;


/**
 * Editable plain text FxEditor model backed by an ArrayList<String>.
 */
public class SimpleEditablePlainTextEditorModel
	extends FxEditorModel
{
	protected final CList<String> lines = new CList("");
	
	
	public SimpleEditablePlainTextEditorModel()
	{
		setEditable(true);
	}
	
	
	public void setText(String text)
	{
		// TODO
	}
	
	
	public void addText(String text)
	{
		// TODO
	}
	
	
	public void insertText(int line, int pos, String text)
	{
		// TODO
	}
	
	
	public LineBox getLineBox(int line)
	{
		LineBox box = new LineBox();
		String s = getText(line);
		if(s != null)
		{
			Text t = new Text(s);
			box.addText(t);
		}
		return box;
	}
	
	
	protected String getText(int line)
	{
		if(lines.isValidIndex(line))
		{
			return lines.get(line);
		}
		return null;
	}
	
	
	public LoadInfo getLoadInfo()
	{
		return null; 
	}
	

	public int getLineCount()
	{
		return lines.size();
	}
	
	
	public Edit edit(Edit ed) throws Exception
	{
		D.print(ed);
		Edit undo = new Edit();

		for(Edit.Part p: ed)
		{
			// TODO apply and fire events
			// TODO add to undo
		}
		
		return undo;
	}
}
