// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.common.util.CList;
import goryachev.fx.edit.internal.EditorTools;
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
		Edit undo = new Edit();

		for(Edit.Part p: ed)
		{
			SelectionSegment sel = p.sel;
			Object text = applyEdit(sel, p.replaceText);
			undo.addPart(p.sel, text);
		}
		
		return undo;
	}


	/** applies edit and returns removed text: either a String or a String[] */
	protected Object applyEdit(SelectionSegment sel, Object replaceText)
	{
		Marker min = sel.getMin();
		Marker max = sel.getMax();
		
		if(sel.isOneLine())
		{
			int line = min.getLine();
			String text = lines.get(line);
			
			String old = EditorTools.substring(text, min.getLineOffset(), max.getLineOffset());
			
			if(replaceText instanceof String)
			{
				String repl = (String)replaceText;
				String upd = EditorTools.replace(text, min.getLineOffset(), max.getLineOffset(), repl);
				
				lines.set(line, upd);
				fireEvent((ed) -> ed.eventLineModified(line, min.getLineOffset(), max.getLineOffset(), repl.length()));
				
				return old;
			}
			else
			{
				// TODO multiple lines
				throw new Error();
			}
		}
		else
		{
			// TODO multiple lines
			throw new Error();
		}
		// TODO apply and fire events
		// TODO add to undo		
	}
}
