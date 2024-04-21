// Copyright © 2017-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fxeditor;


/**
 * An Edit.
 */
public class Edit
{
	private final EditorSelection selection;
	private final CharSequence replaceText;
	
	
	public Edit(EditorSelection sel, CharSequence replaceText)
	{
		this.selection = sel;
		this.replaceText = replaceText;
	}
	
	
	public EditorSelection getSelection()
	{
		return selection;
	}
	
	
	public CharSequence getReplaceText()
	{
		return replaceText;
	}
}
