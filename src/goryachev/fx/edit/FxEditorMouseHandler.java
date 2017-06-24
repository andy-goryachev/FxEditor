// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.common.util.D;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;


/**
 * FxEditor Mouse Handler.
 */
public class FxEditorMouseHandler
{
	protected final FxEditor editor;
	protected final SelectionController sel;
	protected boolean dragging;
	protected boolean draggingScroll;


	public FxEditorMouseHandler(FxEditor ed, SelectionController sel)
	{
		this.editor = ed;
		this.sel = sel;
	}
	
	
	protected boolean isOverScrollBar(double x, double y)
	{
		if(x >= editor.vscroll.getLayoutX())
		{
			return true;
		}
		else if(y >= editor.hscroll.getLayoutY())
		{
			return true;
		}
		return false;
	}
	
	
	protected void handleScroll(ScrollEvent ev)
	{
		if(isOverScrollBar(ev.getX(), ev.getY()))
		{
			return;
		}
		
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
	
	
	protected void handleMousePressed(MouseEvent ev)
	{
		if(isOverScrollBar(ev.getX(), ev.getY()))
		{
			return;
		}
			
		Marker pos = getTextPos(ev);
		editor.setSuppressBlink(true);
		
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
				sel.setSelection(pos);
			}
			else
			{
				// add a new caret
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
		
		editor.requestFocus();
	}
	
	
	protected void handleMouseDragged(MouseEvent ev)
	{
		if(draggingScroll)
		{
			return;
		}
		
		if(isOverScrollBar(ev.getX(), ev.getY()))
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
		editor.setSuppressBlink(false);

		if(isOverScrollBar(ev.getX(), ev.getY()))
		{
			return;
		}
		
		sel.commitSelection();
	}
}