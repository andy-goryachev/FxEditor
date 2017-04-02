// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package demo.edit;
import goryachev.common.util.SB;
import goryachev.fx.edit.CTextFlow;
import goryachev.fx.edit.FxEditorModel.LoadInfo;
import goryachev.fx.edit.FxPlainEditorModel;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;


/**
 * test plain text model with 2 billion rows
 */
public class TestFxPlainEditorModel
	extends FxPlainEditorModel
{
	public TestFxPlainEditorModel()
	{
	}
	
	
	public Region getDecoratedLine(int line)
	{
		CTextFlow f = new CTextFlow();
		String s = getPlainText(line);
		if(s != null)
		{
			Text t = new Text(s);
			f.getChildren().add(t); 
		}
		return f;
	}


	public LoadInfo getLoadInfo()
	{
		return null; 
	}


	public int getLineCount()
	{
		return Integer.MAX_VALUE;
	}
	
	
	public String getPlainText(int line)
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
