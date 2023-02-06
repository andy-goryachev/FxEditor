// Copyright © 2017-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.fxeditor;
import goryachev.common.util.CList;


/**
 * Simple Styled Text Model.
 * 
 * TODO insert, delete, allAll, setAll etc.
 */
public class SimpleStyledTextModel
	extends FxEditorModel
{
	private CList<LineBox> lines = new CList();
	
	
	public SimpleStyledTextModel()
	{
	}
	
	
	public String getPlainText(int line)
	{
		LineBox b = getLineBox(line);
		if(b != null)
		{
			return b.getText();
		}
		return null;
	}
	
	
	public LineBox getLineBox(int line)
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
		throw new Error();
	}
	
	
	protected LineBox last()
	{
		if(lines.size() == 0)
		{
			lines.add(new LineBox());
		}
		return lines.get(lines.size() - 1);
	}
	
	
	public void add(TStyle s, String text)
	{
		LineBox b = last();
		b.addText(s, text);
	}
	
	
	/** inserts a newline */
	public void nl()
	{
		lines.add(new LineBox());
	}
	
	
	/** construct text from a sequence of (TStyle,String) or (String).  A single "\n" separates line. */
	public void add(Object ... items)
	{
		for(int i=0; i<items.length; )
		{
			Object x = items[i++];
			if(x instanceof String)
			{
				if("\n".equals(x))
				{
					nl();
				}
				else
				{
					add(null, x.toString());
				}
			}
			else if(x instanceof TStyle)
			{
				TStyle s = (TStyle)x;
				String text = (String)items[i++];
				add(s, text);
			}
			else
			{
				throw new Error("?" + x);
			}
		}
	}
}
