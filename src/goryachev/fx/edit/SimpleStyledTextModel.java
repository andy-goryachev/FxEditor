// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.common.util.CList;


/**
 * Simple Styled Text Model.
 * 
 * TODO insert, delete, allAll, setAll etc.
 */
public class SimpleStyledTextModel
	extends AbstractStyledTextModel
{
	private CList<TSegments> lines = new CList();
	
	
	public SimpleStyledTextModel()
	{
	}


	protected TSegments getSegments(int line)
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
	
	
	public void add(TSegments s)
	{
		lines.add(s);
	}
}
