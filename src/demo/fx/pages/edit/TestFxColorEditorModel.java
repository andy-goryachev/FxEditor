// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package demo.fx.pages.edit;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.fx.edit.AbstractPlainTextEditorModel;
import goryachev.fx.edit.Edit;
import goryachev.fx.edit.LineBox;
import demo.edit.DemoSyntax;
import demo.edit.Segment;
import javafx.scene.text.Text;


/**
 * test plain text model with up to 2 billion rows
 */
public class TestFxColorEditorModel
	extends AbstractPlainTextEditorModel
{
	private final int lineCount;
	private final String[] lines;
	
	
	public TestFxColorEditorModel(int lineCount)
	{
		this.lineCount = lineCount;
		this.lines = readFile();
	}
	
	
	private static String[] readFile()
	{
		String resource = "example.txt";
		try
		{
			String[] ss = CKit.readLines(TestFxColorEditorModel.class, resource);
			return ss;
		}
		catch(Exception e)
		{
			return new String[] { "error reading resource " + resource };
		}
	}
	
	
	public LineBox getDecoratedLine(int line)
	{
		String text = getPlainText(line);
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
