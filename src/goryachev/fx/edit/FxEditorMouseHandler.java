// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.common.util.D;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;


/**
 * FxEditor Mouse Handler.
 */
public class FxEditorMouseHandler
{
	protected final FxEditor editor;
	protected final SelectionController selector;


	public FxEditorMouseHandler(FxEditor ed, SelectionController sel)
	{
		this.editor = ed;
		this.selector = sel;
	}
	
	
	protected void handleScroll(ScrollEvent ev)
	{
		if(ev.isShiftDown())
		{
			// TODO horizontal scroll perhaps?
			D.print("horizontal scroll", ev.getDeltaX());
		}
		else if(ev.isShortcutDown())
		{
			// page up / page down
			if(ev.getDeltaY() >= 0)
			{
				editor.pageUp();
			}
			else
			{
				editor.pageDown();
			}
		}
		else
		{
			// vertical block scroll
			editor.blockScroll(ev.getDeltaY() >= 0);
		}
	}
	
	
	protected Marker getTextPos(MouseEvent ev)
	{
		double x = ev.getScreenX();
		double y = ev.getScreenY();
		return editor.getTextPos(x, y);
	}
	
	
	protected void selectLine(Marker m)
	{
		if(m != null)
		{
			FxEditorModel model = editor.getTextModel(); 
			int lines = model.getLineCount();
			int line = m.getLine();

			Marker start = editor.markers.newMarker(line, 0, true);
			line++;
			
			Marker end;
			if(line >= lines)
			{
				--line;
				int ix = Math.max(0, model.getPlainText(line).length() - 1);
				end = editor.markers.newMarker(line, ix, false);
			}
			else
			{
				end = editor.markers.newMarker(line, 0, true);
			}
			selector.setSelection(start, end);
		}
	}
	
	
	public void handleMouseClicked(MouseEvent ev)
	{
		if(ev.getButton() != MouseButton.PRIMARY)
		{
			return;
		}
		
		int clicks = ev.getClickCount();
		switch(clicks)
		{
		case 2:
			D.print("double click"); // FIX
			break;
		case 3:
			selectLine(getTextPos(ev));
			ev.consume();
			break;
		}
	}
	
	
	public void handleMousePressed(MouseEvent ev)
	{
		Marker pos = getTextPos(ev);
		editor.setSuppressBlink(true);
				
		if(ev.isShiftDown())
		{
			// expand selection from the anchor point to the current position
			// clearing existing (possibly multiple) selection
			selector.clearAndExtendLastSegment(pos);
		}
		else if(ev.isShortcutDown())
		{
			if(selector.isInsideOfSelection(pos) || (!editor.isMultipleSelectionEnabled()))
			{
				selector.setAnchor(pos);
				selector.setSelection(pos);
			}
			else
			{
				// add a new caret
				selector.setAnchor(pos);
				selector.addSelectionSegment(pos, pos);
			}
		}
		else
		{
			editor.clearSelection();
			selector.addSelectionSegment(pos, pos);
			selector.setAnchor(pos);
		}
		
		editor.requestFocus();
	}
	
	
	public void handleMouseDragged(MouseEvent ev)
	{		
		Marker pos = getTextPos(ev);
		selector.extendLastSegment(pos);
	}
	
	
	public void handleMouseReleased(MouseEvent ev)
	{
		editor.setSuppressBlink(false);
		selector.commitSelection();
	}
}