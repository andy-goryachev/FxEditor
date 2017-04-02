// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;


/**
 * Layout Operation caches the existing layout and newly created row Nodes.
 */
public class LayoutOp
{
	protected final FxEditorLayout layout;
	
	
	public LayoutOp(FxEditorLayout layout)
	{
		this.layout = layout;
	}
}
