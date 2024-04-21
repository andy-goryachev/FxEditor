// Copyright © 2016-2024 Andy Goryachev <andy@goryachev.com>
package demo.edit;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.fxeditor.AbstractPlainTextEditorModel;
import goryachev.fxeditor.Edit;
import goryachev.fxeditor.FxEditorModel.LoadInfo;
import goryachev.fxeditor.LineBox;
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
		String text = getPlainText(line);
		CList<Segment> ss = new DemoSyntax(text).generateSegments();
		
		LineBox box = new LineBox();
		if(ss.size() == 0)
		{
			// needs at least one empty Text child to compute height properly
			box.addText(new Text(""));
		}
		else
		{
			for(Segment s: ss)
			{
				Text t = new Text(s.text);
				t.setFill(s.color);
				
				box.addText(t);
			}
		}
		return box;
	}
	
	
	public String getPlainText(int line)
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
