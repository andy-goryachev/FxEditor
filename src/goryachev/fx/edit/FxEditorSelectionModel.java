// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 * FxEditor Selection Model.
 */
public class FxEditorSelectionModel
{
	protected final ObservableList<SelectionSegment> selection = FXCollections.observableArrayList();
	protected final ObservableList<SelectionSegment> unmodifiableSelection = FXCollections.unmodifiableObservableList(selection);


	public FxEditorSelectionModel()
	{
	}


	/** returns unmodifiable observable list containing selection segments */ 
	public ObservableList<SelectionSegment> getSelection()
	{
		return unmodifiableSelection;
	}


	public void clear()
	{
		selection.clear();
	}
	
	
	public boolean isInsideSelection(Marker pos)
	{
		for(SelectionSegment s: selection)
		{
			if(s.contains(pos))
			{
				return true;
			}
		}
		return false;
	}
	

	/** adds a new segment from start to end */
	public void addSelectionSegment(Marker start, Marker end)
	{
		selection.add(new SelectionSegment(start, end));
	}
	
	
	protected void clearAndExtendLastSegment(Marker pos)
	{
		Marker anchor = lastAnchor();
		if(anchor == null)
		{
			anchor = pos;
		}
		
		clear();
		addSelectionSegment(anchor, pos);
	}
	
	
	protected void extendLastSegment(Marker pos)
	{
		if(pos == null)
		{
			return;
		}
		
		int ix = selection.size() - 1;
		if(ix < 0)
		{
			 addSelectionSegment(pos, pos);
		}
		else
		{
			SelectionSegment s = selection.get(ix);
			Marker anchor = s.getStart();
			selection.set(ix, new SelectionSegment(anchor, pos));
		}
	}
	
	
	public Marker lastAnchor()
	{
		int ix = selection.size() - 1;
		if(ix >= 0)
		{
			SelectionSegment s = selection.get(ix);
			return s.getStart();
		}
		return null;
	}
	
	
	public SelectionSegment getLastSegment()
	{
		int sz = selection.size();
		if(sz > 0)
		{
			return selection.get(sz - 1);
		}
		return null;
	}
}
