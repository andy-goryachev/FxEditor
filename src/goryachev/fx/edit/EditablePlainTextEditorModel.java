// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.common.util.CList;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;


/**
 * Editable plain text FxEditor model.
 */
public class EditablePlainTextEditorModel
	extends FxEditorModel
{
	protected final CList<String> lines = new CList("");
	
	
	public EditablePlainTextEditorModel()
	{
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
	
	
	public Region getDecoratedLine(int line)
	{
		CTextFlow t = new CTextFlow();
		String s = getPlainText(line);
		if(s != null)
		{
			Text tx = new Text(s);
			t.getChildren().add(tx); 
		}
		return t;
	}
	
	
	public LoadInfo getLoadInfo()
	{
		return null; 
	}
	

	public int getLineCount()
	{
		return lines.size();
	}
	
	
	public String getPlainText(int line)
	{
		return lines.get(line);
	}

	
	public Edit edit(Edit ed) throws Exception
	{
		// TODO
		throw new Exception();
	}
}
