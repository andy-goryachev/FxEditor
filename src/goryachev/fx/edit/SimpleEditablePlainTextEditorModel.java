// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.common.util.CList;
import goryachev.fx.edit.internal.EditorTools;
import java.util.List;
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
			List<String> text = applyEdit(sel, p.replaceText);
			undo.addPart(p.sel, text);
		}
		
		return undo;
	}


	/** applies edit and returns removed text. */
	protected List<String> applyEdit(SelectionSegment sel, List<String> replaceText)
	{
		Marker min = sel.getMin();
		Marker max = sel.getMax();
		
		List<String> removed = getTextRange(min, max);
		removeRange(min, max);
		fireEvent((ed) -> ed.dumpState()); // FIX

		insert(max, replaceText);
		fireEvent((ed) -> ed.dumpState()); // FIX
		
		return removed;
	}
	
	
	protected List<String> getTextRange(Marker min, Marker max)
	{
		int sz = max.getLine() - min.getLine() + 1;
		int ix = min.getLine();
		CList<String> rv = new CList(sz);
		
		if(sz == 1)
		{
			String s = getText(ix);
			s = EditorTools.substring(s, min.getOffset(), max.getOffset());
			rv.add(s);
		}
		else
		{
			for(int i=0; i<=sz; i++)
			{
				String s = getText(ix);
				ix++;
				
				if(i == 0)
				{
					s = EditorTools.substring(s, min.getOffset(), s.length());
				}
				else if(i == sz)
				{
					s = EditorTools.substring(s, 0, max.getOffset());
				}
				else
				{
					// full length
				}
				
				rv.add(s);
			}
		}
		return rv;
	}
	
	
	protected void removeRange(Marker min, Marker max)
	{
		int sz = max.getLine() - min.getLine() + 1;
		int ix = min.getLine();
		
		if(sz == 1)
		{
			String s = getText(ix);
			s = EditorTools.removeRange(s, min.getOffset(), max.getOffset());
			lines.set(ix, s);
		}
		else
		{
			for(int i=sz; i>=0; i--)
			{
				String s = getText(ix);
				ix--;
				
				if(i == 0)
				{
					s = EditorTools.substring(s, 0, min.getOffset());
					lines.set(ix, s);
				}
				else if(i == sz)
				{
					s = EditorTools.substring(s, max.getOffset(), s.length());
					lines.set(ix, s);
				}
				else
				{
					lines.remove(ix);
				}
			}
		}
		
		fireEvent((ed) -> ed.eventRangeRemoved(min, max));
	}
	
	
	protected void insert(Marker m, List<String> inserted)
	{
		int line = m.getLine();
		int sz = inserted.size();
		String s = getText(line);
		
		if(sz == 1)
		{
			s = EditorTools.insert(s, m.getOffset(), inserted.get(0));
			lines.set(line, s);
		}
		else
		{
			String left = s.substring(0, m.getOffset());
			String right = s.substring(m.getOffset());

			for(int i=0; i<=sz; i++)
			{
				if(i == 0)
				{
					s = left + inserted.get(i);
					lines.set(line, s);
				}
				else if(i == sz)
				{
					s = inserted.get(i) + right;
					lines.set(line, s);
				}
				else
				{
					s = inserted.get(i);
					lines.add(line, s);
				}
				
				line++;
			}
		}
		
		fireEvent((ed) -> ed.eventRangeInserted(m, inserted));
	}
}
