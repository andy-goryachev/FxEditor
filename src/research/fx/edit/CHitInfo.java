// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;


/**
 * A public version of HitInfo.
 */
public class CHitInfo
{
	private final int index;
	private final boolean leading;


	public CHitInfo(int index, boolean leading)
	{
		this.index = index;
		this.leading = leading;
	}
	
	
	public String toString()
	{
		return index + (leading ? ".L" : ".T");
	}


	public int getIndex()
	{
		return index;
	}


	public boolean isLeading()
	{
		return leading;
	}


	public int getInsertionIndex()
	{
		return leading ? index : index + 1;
	}
}
