// Copyright © 2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;


/**
 * FxEditor Model Listener.
 * 
 * TODO need more detailed information about changes.
 * 
 * The idea is that after each event, the model indexes change accordingly.
 * The clients should query the model for new information, using new text row indexes.
 */
public interface FxEditorModelListener
{
	public void eventLinesDeleted(int start, int count);

	public void eventLinesInserted(int start, int count);

	public void eventLinesUpdated(int start, int count);

	public void eventAllLinesChanged();
}
