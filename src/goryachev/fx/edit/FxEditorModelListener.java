// Copyright © 2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;


/**
 * FxEditor Model Listener.
 * 
 * The idea is that after each event, the model indexes change.
 * The clients should query the model for new information, using new text row indexes.
 */
public interface FxEditorModelListener
{
	/**
	 * The text between two positions has changed: either deleted, replaced, or inserted.
	 * @param numberOfLinesInserted is the number of lines (essentially, a number of line breaks)
	 * that would exist after the transformation. 
	 */
	public void eventTextUpdated(int startLine, int startCharIndex, int endLine, int endCharIndex, int numberOfLinesInserted);

	/** 
	 * All lines in the editor have changed.  
	 * The clients should re-query the model and rebuild everything 
	 */
	public void eventAllLinesChanged();
}
