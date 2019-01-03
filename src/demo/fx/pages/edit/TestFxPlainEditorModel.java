// Copyright © 2016-2019 Andy Goryachev <andy@goryachev.com>
package demo.fx.pages.edit;
import goryachev.common.util.SB;
import goryachev.fx.edit.AbstractPlainTextEditorModel;
import goryachev.fx.edit.Edit;
import goryachev.fx.edit.LineBox;
import javafx.scene.text.Text;


/**
 * test plain text model with 2 billion rows
 */
public class TestFxPlainEditorModel
	extends AbstractPlainTextEditorModel
{
	public TestFxPlainEditorModel()
	{
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


	public LoadInfo getLoadInfo()
	{
		return null; 
	}
	
	
	public Edit edit(Edit ed) throws Exception
	{
		throw new Exception();
	}


	public int getLineCount()
	{
		return Integer.MAX_VALUE;
	}
	
	
	public String getText(int line)
	{
		return getPlainText_0(line) + "   " + line + "   " + getPlainText_0(line);
	}


	public String getPlainText_0(int line)
	{
		String s = String.valueOf(line);
		int sz = s.length();
		
		SB sb = new SB();
		for(int i=0; i<sz; i++)
		{
			char c = s.charAt(i);
			String w = toWord(c);
			
			if(i > 0)
			{
				sb.sp();
			}
			sb.a(w);
		}
		return sb.toString();
	}
	
	
	protected String toWord(char c)
	{
		switch(c)
		{
		case '0': return "zero";
		case '1': return "one";
		case '2': return "two";
		case '3': return "three";
		case '4': return "four";
		case '5': return "five";
		case '6': return "six";
		case '7': return "seven";
		case '8': return "eight";
		case '9': return "nine";
		default: return String.valueOf(c);
		}
	}
}
