// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.common.util.CList;
import goryachev.common.util.D;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 * FxEditor Selection Controller.
 */
public class SelectionController
{
	public final ObservableList<SelectionSegment> segments = FXCollections.observableArrayList();
	private final ReadOnlyObjectWrapper<EditorSelection> selectionProperty = new ReadOnlyObjectWrapper(EditorSelection.EMPTY);
	private Marker anchor;
	private CList<SelectionSegment> originalSelection;


	public SelectionController()
	{
	}
	
	
	public ReadOnlyObjectProperty<EditorSelection> selectionProperty()
	{
		return selectionProperty.getReadOnlyProperty();
	}
	
	
	public EditorSelection getSelection()
	{
		return selectionProperty.get();
	}


	public void clear()
	{
		segments.clear();
		originalSelection = null;
	}
	
	
	/** returns true if marker is inside of any of selection segments */
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
	
	
	public void setSelection(Marker anchor, Marker caret)
	{
		clear();
		addSelectionSegment(anchor, caret);
	}
	

	public void addSelectionSegment(Marker anchor, Marker caret)
	{
		addSegmentInOrder(new SelectionSegment(anchor, caret));
		originalSelection = null;
	}
	
	
	public void setSelection(Marker m)
	{
		setSelection(m, m);
	}
	
	
	public void clearAndExtendLastSegment(Marker pos)
	{
		if(anchor == null)
		{
			anchor = pos;
		}
		
		setSelection(anchor, pos);
	}
	
	
	public void setAnchor(Marker p)
	{
		anchor = p;
	}
	
	
	/** 
	 * extends the new selection segment from the anchor point to the specified position,
	 * updating the segments list such that it remains to be ordered and the segments do not overlap each other
	 */
	public void extendLastSegment(Marker pos)
	{
		if(anchor == null)
		{
			anchor = pos;
		}
		
		// FIX this is incorrect: need to remove last added segment, and create new from the anchor point
		
		SelectionSegment last = new SelectionSegment(anchor, pos);
		addSegmentInOrder(last);
	}
	
	
	protected void addSegmentInOrder(SelectionSegment last)
	{
		D.print("last=", last); // FIX
		
		if(originalSelection == null)
		{
			originalSelection = new CList<>(segments);
		}
		
		// merge last segment and original selection to produce ordered, non-overlapping segments
		CList<SelectionSegment> sorted = new CList(originalSelection.size() + 1);
		for(SelectionSegment s: originalSelection)
		{
			if(last == null)
			{
				sorted.add(s);
			}
			else
			{
				if(last.overlaps(s))
				{
					last = last.combine(s);
				}
				else if(last.isBefore(s))
				{
					sorted.add(last);
					sorted.add(s);
					last = null;
				}
				else
				{
					sorted.add(s);
				}
			}
		}
		
		if(last != null)
		{
			sorted.add(last);
		}
		
		// sorted list contains the new selection
		// we could merge the original list, but for now it's easier just to replace the items
		segments.setAll(sorted);
		D.print(sorted); // FIX
	}
	

	/** called at the end of drag gesture to clear transient values and update the selection property */
	public void commitSelection()
	{
		originalSelection = null;
		
		EditorSelection es = new EditorSelection(segments.toArray(new SelectionSegment[segments.size()]));
		selectionProperty.set(es);
		
		D.print(es); // FIX
	}
}
