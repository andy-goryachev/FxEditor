// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.common.util.D;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;


/**
 * FxEditor Controller.
 */
public class FxEditorMouseController
{
	protected final FxEditor editor;
	protected final EditorSelectionController sel;
	protected boolean dragging;
	protected boolean draggingScroll;


	public FxEditorMouseController(FxEditor ed, EditorSelectionController sel)
	{
		this.editor = ed;
		this.sel = sel;
	}
	
	
	public void moveCaret(boolean right)
	{
		// TODO
	}
	
	
	protected void handleScroll(ScrollEvent ev)
	{
		// on scrollbar
		if(ev.getX() >= editor.vscroll().getLayoutX())
		{
			return;
		}
		
		if(ev.isShiftDown())
		{
			// TODO horizontal scroll
			D.print("horizontal scroll", ev.getDeltaX());
		}
		else if(ev.isShortcutDown())
		{
			// TODO page up / page down
			D.print("page scroll", ev.getDeltaY(), ev.getTextDeltaY(), ev.getTextDeltaYUnits());
		}
		else
		{
			// TODO vertical scroll
			D.print("vertical scroll", ev.getDeltaY(), ev.getTextDeltaY(), ev.getTextDeltaYUnits());
		}
	}
	
	
	protected Marker getTextPos(MouseEvent ev)
	{
		double x = ev.getScreenX();
		double y = ev.getScreenY();
		return editor.getTextPos(x, y);
	}
	
	
	protected void handleMousePressed(MouseEvent ev)
	{
		// on scrollbar
		if(ev.getX() >= editor.vscroll().getLayoutX())
		{
			return;
		}
			
		// TODO property: multiple selection
		Marker pos = getTextPos(ev);
		
		if(ev.isShiftDown())
		{
			// FIX there might be a zero length segment (single caret)
			
			// expand selection from the anchor point to the current position
			// clearing existing (possibly multiple) selection
			sel.clearAndExtendLastSegment(pos);
		}
		else if(ev.isShortcutDown())
		{
			if(sel.isInsideSelection(pos) || (!editor.isMultipleSelectionEnabled()))
			{
				// replace selection with a single caret
				sel.clear();
				sel.addSelectionSegment(pos, pos);
			}
			else
			{
				// FIX add a new caret
				sel.addSelectionSegment(pos, pos);
			}
		}
		else
		{
			editor.clearSelection();
			if(pos != null)
			{
				sel.addSelectionSegment(pos, pos);
			}
		}
	}
	
	
	protected void handleMouseDragged(MouseEvent ev)
	{
		if(draggingScroll)
		{
			return;
		}
		
		// on scrollbar
		if(ev.getX() >= editor.vscroll().getLayoutX())
		{
			dragging = false;
			draggingScroll = true;
			return;
		}
		
		dragging = true;
		
		Marker pos = getTextPos(ev);
		sel.extendLastSegment(pos);
	}
	
	
	protected void handleMouseReleased(MouseEvent ev)
	{
		dragging = false;
		draggingScroll = false;

		// on scrollbar
		if(ev.getX() >= editor.vscroll().getLayoutX())
		{
			return;
		}
		
		EditorSelection es = sel.commitSelection();
		editor.setSelection(es);
	}
}