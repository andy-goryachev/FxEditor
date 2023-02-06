// Copyright © 2019-2023 Andy Goryachev <andy@goryachev.com>
package demo.edit;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.D;
import goryachev.fxeditor.AbstractPlainTextEditorModel;
import goryachev.fxeditor.Edit;
import goryachev.fxeditor.FxEditorModel.LoadInfo;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;


/**
 * Growing Text Model.
 */
public class DemoGrowingModel
	extends AbstractPlainTextEditorModel
{
	protected final CList<String> lines = new CList();
	protected final Timeline timer;
	
	
	public DemoGrowingModel()
	{
		Duration period = Duration.millis(1000);
		
		timer = new Timeline(new KeyFrame(period, (ev) -> addSomeText()));
		timer.setCycleCount(Timeline.INDEFINITE);
		timer.play();
	}
	
	
	protected void addSomeText()
	{
		String text = nextText();
		append(text);
	}
	
	
	// TODO
	protected void append(String text)
	{
		D.print(text);
		
		int max = getLineCount();
		int line = Math.max(0, max - 1);
		String s = getPlainText(line);
		int charIndex = s == null ? 0 : s.length();
		
		String[] ss = CKit.split(text, '\n');
		
		for(int i=0; i<ss.length; i++)
		{
			int ix = line + i;
			s = getPlainText(ix);
			if(s == null)
			{
				lines.add(ss[i]);
			}
			else
			{
				lines.set(ix, s + ss[i]);
			}
		}
		
		int startCharsInserted = ss[0].length();
		int linesInserted = Math.max(0, ss.length - 1);
		fireTextUpdated(line, charIndex, startCharsInserted, linesInserted, line, charIndex, 0);
	}
	

	protected String nextText()
	{
		int r = new Random().nextInt(11);
		switch(r)
		{
		case 0:
			return "zero ";
		case 1:
			return "one ";
		case 2:
			return "two ";
		case 3:
			return "three ";
		case 4:
			return "four ";
		case 5:
			return "five ";
		case 6:
			return "six ";
		case 7:
			return "seven ";
		case 8:
			return "eight ";
		case 9:
			return "nine ";
		default:
			return "\n";	
		}
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
		throw new Error();
	}


	public String getPlainText(int line)
	{
		if(line < lines.size())
		{
			return lines.get(line);
		}
		return null;
	}
}