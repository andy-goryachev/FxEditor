// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import javafx.collections.ObservableList;


/**
 * FxEditor Selection Controller.
 */
public class EditorSelectionController
{
	protected final FxEditor editor;
	protected final ObservableList<SelectionSegment> segments;


	public EditorSelectionController(FxEditor ed, ObservableList<SelectionSegment> segments)
	{
		this.editor = ed;
		this.segments = segments;
	}


	public void clear()
	{
		segments.clear();
	}
	
	
	public boolean isInsideSelection(Marker pos)
	{
		for(SelectionSegment s: segments)
		{
			if(s.contains(pos))
			{
				return true;
			}
		}
		return false;
	}
	

	/** adds a new segment from start to end */
	public void addSelectionSegment(Marker beg, Marker end)
	{
		segments.add(new SelectionSegment(beg, end));
	}
	
	
	public void setSelection(Marker beg, Marker end)
	{
		clear();
		addSelectionSegment(beg, end);
	}
	
	
	public void clearAndExtendLastSegment(Marker pos)
	{
		Marker anchor = lastAnchor();
		if(anchor == null)
		{
			anchor = pos;
		}
		
		setSelection(anchor, pos);
	}
	
	
	public void extendLastSegment(Marker pos)
	{
		if(pos == null)
		{
			return;
		}
		
		int ix = segments.size() - 1;
		if(ix < 0)
		{
			 addSelectionSegment(pos, pos);
		}
		else
		{
			SelectionSegment s = segments.get(ix);
			Marker anchor = s.getStart();
			segments.set(ix, new SelectionSegment(anchor, pos));
		}
	}
	
	
	public Marker lastAnchor()
	{
		int ix = segments.size() - 1;
		if(ix >= 0)
		{
			SelectionSegment s = segments.get(ix);
			return s.getStart();
		}
		return null;
	}
	
	
	public SelectionSegment getLastSegment()
	{
		int sz = segments.size();
		if(sz > 0)
		{
			return segments.get(sz - 1);
		}
		return null;
	}


	/** combines overlapping segments and sets selection property */
	public void commitSelection()
	{
		int sz = segments.size();
		if(sz >= 2)
		{
			SelectionSegment last = segments.get(sz - 1);
			
			// starting from the last segment
			for(int i=segments.size()-2; i>=0; --i)
			{
				SelectionSegment seg = segments.get(i);
				SelectionSegment over = last.union(seg);
				if(over != null)
				{
					segments.set(sz - 1, over);
					segments.remove(i);
				}
			}
		}
		
		SelectionSegment[] sel = segments.toArray(new SelectionSegment[segments.size()]);
		editor.setSelection(new EditorSelection(sel));
	}
}
