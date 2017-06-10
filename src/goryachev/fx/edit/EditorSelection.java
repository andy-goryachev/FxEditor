// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.common.util.CComparator;
import goryachev.common.util.CList;


/**
 * An immutable object that represents text selection within FxEditor.
 */
public class EditorSelection
{
	public static final EditorSelection EMPTY = createEmpty();
	private final SelectionSegment[] segments;
	
	
	public EditorSelection(SelectionSegment[] segments)
	{
		this.segments = segments;
	}
	
	
	private static EditorSelection createEmpty()
	{
		Marker m = new Marker(0, 0, true);
		return NormalizedSelection.create(new SelectionSegment[] { new SelectionSegment(m, m) });
	}
	
	
	/** true if contans ordered segments */ 
	public boolean isOrdered()
	{
		return false;
	}


	/** returns original segment array */
	public SelectionSegment[] getSegments()
	{
		return segments;
	}
	
	
	/** returns last or the only selection segment */
	public SelectionSegment getSegment()
	{
		if(segments.length == 0)
		{
			return null;
		}
		return segments[segments.length - 1];
	}
	
	
	public int getSegmentCount()
	{
		return segments.length;
	}
	
	
	public boolean isEmpty()
	{
		for(SelectionSegment s: segments)
		{
			if(!s.isEmpty())
			{
				return false;
			}
		}
		return false;
	}
	
	
	public boolean isNotEmpty()
	{
		return !isEmpty();
	}
	
	
	public boolean hasMultipleSegments()
	{
		return segments.length > 1;
	}


	@Deprecated // TODO make sure always normalized + add boolean flag to the selection to indicate where the caret is
	public EditorSelection getNormalizedSelection()
	{
		return NormalizedSelection.create(segments);
	}
	
	
	//
	
	
	public static class NormalizedSelection extends EditorSelection
	{
		private NormalizedSelection(SelectionSegment[] segments)
		{
			super(segments);
		}
		
		
		public boolean isOrdered()
		{
			return true;
		}
		
		
		/** 
		 * returns normalized selection ranges: sorted from the closest to the beginning of the document,
		 * with a start marker always coming before the end marker 
		 */
		public static NormalizedSelection create(SelectionSegment[] segments)
		{
			int sz = segments.length;
			CList<SelectionSegment> ss = new CList<>(sz);
			for(int i=0; i<sz; i++)
			{
				ss.add(segments[i].getNormalizedSegment());
			}
			
			if(sz > 1)
			{
				new CComparator<SelectionSegment>()
				{
					public int compare(SelectionSegment a, SelectionSegment b)
					{
						return a.getStart().compareTo(b.getStart());
					}
				}.sort(ss);
			}
			
			return new NormalizedSelection(ss.toArray(new SelectionSegment[sz]));
		}
	}
}
