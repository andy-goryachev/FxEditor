// Copyright © 2019 Andy Goryachev <andy@goryachev.com>
package demo.edit;
import goryachev.common.util.CList;
import goryachev.fx.edit.AbstractPlainTextEditorModel;
import goryachev.fx.edit.Edit;


/**
 * Growing Text Model.
 */
public class DemoGrowingModel
	extends AbstractPlainTextEditorModel
{
	protected final CList<String> lines = new CList();
	
	
	public DemoGrowingModel()
	{
		// TODO start timer to add random text to the end.
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
}
