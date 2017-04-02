// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package demo.edit;
import goryachev.common.util.CList;
import goryachev.common.util.SB;
import java.text.NumberFormat;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import research.fx.edit.CTextFlow;
import research.fx.edit.FxEditorModel;
import research.fx.edit.FxPlainEditorModel;
import research.fx.edit.FxEditorModel.LoadInfo;


/**
 * test plain text model with 2 billion rows
 */
public class TestFxColorEditorModel
	extends FxPlainEditorModel
{
	private NumberFormat format = NumberFormat.getInstance();
	
	
	public TestFxColorEditorModel()
	{
	}
	
	
	public Region getDecoratedLine(int line)
	{
		CList<Segment> ss = getSegments(line, true);
		
		CTextFlow f = new CTextFlow();
		for(Segment s: ss)
		{
			Text t = new Text(s.text);
			t.setFill(s.color);
			
			f.getChildren().add(t);
		}
		return f;
	}
	
	
	public String getPlainText(int line)
	{
		CList<Segment> ss = getSegments(line, false);
		SB sb = new SB();
		for(Segment s: ss)
		{
			sb.a(s.text);
		}
		return sb.toString();
	}


	public LoadInfo getLoadInfo()
	{
		return null; 
	}


	public int getLineCount()
	{
		return Integer.MAX_VALUE;
	}


	protected CList<Segment> getSegments(int line, boolean styles)
	{
		String s = String.valueOf(line);
		int sz = s.length();
		
		CList<Segment> ss = new CList<>();
		
		ss.add(new Segment("Line " + format.format(line) + ": ", Color.BLACK));
		
		for(int i=0; i<sz; i++)
		{
			char c = s.charAt(i);
			String w = toWord(c);
			
			ss.add(new Segment(w, styles ? toColor(c) : null));
		}
		return ss;
	}
	
	
	protected String toWord(char c)
	{
		switch(c)
		{
		case '0': return "zero ";
		case '1': return "one ";
		case '2': return "two ";
		case '3': return "three ";
		case '4': return "four ";
		case '5': return "five ";
		case '6': return "six ";
		case '7': return "seven ";
		case '8': return "eight ";
		case '9': return "nine ";
		default: return String.valueOf(c);
		}
	}
	
	
	protected Color toColor(char c)
	{
		switch(c)
		{
		case '0': return c(0);
		case '1': return c(1);
		case '2': return c(2);
		case '3': return c(3);
		case '4': return c(4);
		case '5': return c(5);
		case '6': return c(6);
		case '7': return c(7);
		case '8': return c(8);
		case '9': return c(9);
		default: return null;
		}
	}
	
	
	protected Color c(int angle)
	{
		return Color.hsb(36.0 * angle, 1.0, 0.5);
	}
	
	
	//
	
	
	public static class Segment
	{
		public final String text;
		public final Color color;
		
		
		public Segment(String text, Color color)
		{
			this.text = text;
			this.color = color;
		}
	}
}
