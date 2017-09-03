// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.common.util.CList;
import goryachev.fx.edit.Edit.Part;
import java.util.Iterator;


/**
 * An Edit.
 */
public class Edit
	implements Iterable<Part>
{
	public static class Part
	{
		public SelectionSegment sel;
		public Object replaceText;
		
		public String toString()
		{
			return "{" + sel + "," + replaceText + "}";
		}
	}
	
	//
	
	private final CList<Part> parts = new CList();
	
	
	public Edit(EditorSelection sel, Object replaceText)
	{
		for(SelectionSegment ss: sel.getSegments())
		{
			addPart(ss, replaceText);
		}
	}
	
	
	public Edit()
	{
	}
	
	
	public void addPart(SelectionSegment sel, Object replaceText)
	{
		Part p = new Part();
		p.sel = sel;
		p.replaceText = replaceText;
		parts.add(p);
	}
	
	
	public String toString()
	{
		return "Edit(" + parts + ")";
	}
	
	
	public int getPartCount()
	{
		return parts.size();
	}
	
	
	public Part getPart(int ix)
	{
		return parts.get(ix);
	}


	public Iterator<Part> iterator()
	{
		return parts.iterator();
	}
}
