// Copyright © 2016-2019 Andy Goryachev <andy@goryachev.com>
package demo.edit;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.fx.edit.AbstractPlainTextEditorModel;
import goryachev.fx.edit.Edit;
import goryachev.fx.edit.LineBox;
import javafx.scene.text.Text;


/**
 * test plain text model with up to 2 billion rows
 */
public class DemoColorEditorModel
	extends AbstractPlainTextEditorModel
{
	private final int lineCount;
	private final String[] lines;
	
	
	public DemoColorEditorModel(int lineCount)
	{
		this.lineCount = lineCount;
		this.lines = readFile();
	}
	
	
	private static String[] readFile()
	{
		String resource = "example.txt";
		try
		{
			String[] ss = CKit.readLines(DemoColorEditorModel.class, resource);
			return ss;
		}
		catch(Exception e)
		{
			return new String[] { "error reading resource " + resource };
		}
	}
	
	
	public LineBox getLineBox(int line)
	{
		String text = getText(line);
		CList<Segment> ss = new DemoSyntax(text).generateSegments();
		
		LineBox box = new LineBox();
		for(Segment s: ss)
		{
			Text t = new Text(s.text);
			t.setFill(s.color);
			
			box.addText(t);
		}
		return box;
	}
	
	
	public String getText(int line)
	{
		int ix = line % lines.length;
		return lines[ix];
	}
	

	public LoadInfo getLoadInfo()
	{
		return null; 
	}


	public int getLineCount()
	{
		return lineCount;
	}

	
	public Edit edit(Edit ed) throws Exception
	{
		throw new Exception();
	}
}
