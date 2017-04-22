// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.fx.Binder;
import goryachev.fx.FX;
import goryachev.fx.edit.internal.CaretLocation;
import goryachev.fx.edit.internal.EditorTools;
import goryachev.fx.util.FxPathBuilder;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.util.Duration;


/**
 * Vertical Flow manages LineBoxes.
 */
public class VFlow
	extends Pane
{
	protected final FxEditor editor;
	public final Timeline caretAnimation;
	public final Path caretPath;
	public final Path selectionHighlight;
	protected final BooleanProperty caretVisible = new SimpleBooleanProperty(true);
	// TODO line decorations/line numbers
	protected FxEditorLayout layout;
	
	
	public VFlow(FxEditor ed)
	{
		this.editor = ed;
		
		selectionHighlight = new Path();
		FX.style(selectionHighlight, FxEditor.HIGHLIGHT);
		selectionHighlight.setManaged(false);
		selectionHighlight.setStroke(null);
		selectionHighlight.setFill(Color.rgb(255, 255, 0, 0.25));
		
		caretPath = new Path();
		FX.style(caretPath, FxEditor.CARET);
		caretPath.setManaged(false);
		caretPath.setStroke(Color.BLACK);
		
		caretAnimation = new Timeline();
		caretAnimation.setCycleCount(Animation.INDEFINITE);
		
		getChildren().addAll(selectionHighlight, caretPath);
	}
	
	
	protected void layoutChildren()
	{
		layout = recreateLayout(layout);
		reloadSelectionDecorations();
	}
	
	
	// TODO stop blinking when dragging
	public void updateBlinkRate()
	{
		Duration d = editor.getBlinkRate();
		Duration period = d.multiply(2);
		
		caretAnimation.stop();
		caretAnimation.getKeyFrames().setAll
		(
			new KeyFrame(Duration.ZERO, (ev) -> setCaretVisible(true)),
			new KeyFrame(d, (ev) -> setCaretVisible(false)),
			new KeyFrame(period)
		);
		caretAnimation.play();
	}
	
	
	// blinking caret
	public void setCaretVisible(boolean on)
	{
		caretVisible.set(on);
	}
	
	
	public boolean isCaretVisible()
	{
		return caretVisible.get();
	}
	

	public FxEditorLayout recreateLayout(FxEditorLayout prev)
	{
		if(prev != null)
		{
			prev.removeFrom(this);
		}
		
		double width = getWidth();
		double height = getHeight();
		
		// TODO is loaded?
		FxEditorModel model = editor.getTextModel();
		int lines = model.getLineCount();
		FxEditorLayout la = new FxEditorLayout(editor.topLineIndex, editor.offsety);
		
		Insets pad = getInsets();
		double maxy = height - pad.getBottom();
		double y = pad.getTop();
		double x0 = pad.getLeft();
		boolean wrap = editor.isWrapText();
		
		// TODO account for leading, trailing components
		double wid = width - x0 - pad.getRight();
		
		for(int ix=editor.topLineIndex; ix<lines; ix++)
		{
			LineBox b = (prev == null ? null : prev.getLineBox(ix));
			
			Region nd;
			if(b == null)
			{
				nd = model.getDecoratedLine(ix);
			}
			else
			{
				nd = b.getBox();
			}
			getChildren().add(nd);
			nd.applyCss();
			nd.setManaged(true);
			
			double w = wrap ? wid : nd.prefWidth(-1);
			nd.setMaxWidth(wrap ? wid : Double.MAX_VALUE); 
			double h = nd.prefHeight(w);
			
			if(b == null)
			{
				b = new LineBox(ix, nd);
			}
			la.addLineBox(b);
			
			layoutInArea(nd, x0, y, w, h, 0, null, true, true, HPos.LEFT, VPos.TOP);
			
			y += h;
			if(y > maxy)
			{
				break;
			}
		}
		
		return la;
	}
	
	
	public void reloadSelectionDecorations()
	{
		FxPathBuilder hb = new FxPathBuilder();
		FxPathBuilder cb = new FxPathBuilder();
		
		for(SelectionSegment s: editor.segments)
		{
			Marker start = s.getStart();
			Marker end = s.getEnd();
			
			createSelectionHighlight(hb, start, end);
			createCaretPath(cb, end);
		}
		
		selectionHighlight.getElements().setAll(hb.getPath());
		caretPath.getElements().setAll(cb.getPath());
	}
	
	
	protected void createCaretPath(FxPathBuilder p, Marker m)
	{
		CaretLocation c = editor.getCaretLocation(m);
		if(c != null)
		{
			p.moveto(c.x, c.y0);
			p.lineto(c.x, c.y1);
		}
	}
	
	
	// FIX selection shape is incorrect if mixing LTR and RTL languages
	protected void createSelectionHighlight(FxPathBuilder p, Marker startMarker, Marker endMarker)
	{		
		if((startMarker == null) || (endMarker == null))
		{
			return;
		}
		
		if(startMarker.compareTo(endMarker) > 0)
		{
			Marker tmp = startMarker;
			startMarker = endMarker;
			endMarker = tmp;
		}
		
		if(endMarker.getLine() < editor.topLineIndex)
		{
			// selection is above visible area
			return;
		}
		
		if(startMarker.getLine() >= (editor.topLineIndex + layout.getVisibleLineCount()))
		{
			// selection is below visible area
			return;
		}

		CaretLocation beg = editor.getCaretLocation(startMarker);
		CaretLocation end = editor.getCaretLocation(endMarker);
		
		double left = 0.0;
		double right = getWidth() - left;
		double top = 0.0; 
		double bottom = getHeight();
		
		// there is a number of possible shapes resulting from intersection of
		// the selection shape and the visible area.  the logic below explicitly generates 
		// resulting paths because the selection can be quite large.
		
		if(beg == null)
		{
			if(end == null)
			{
				if((startMarker.getLine() < editor.topLineIndex) && (endMarker.getLine() >= (editor.topLineIndex + layout.getVisibleLineCount())))
				{
					// 04
					p.moveto(left, top);
					p.lineto(right, top);
					p.lineto(right, bottom);
					p.lineto(left, bottom);
					p.lineto(left, top);
				}
				return;
			}
			
			// start caret is above the visible area
			boolean crossTop = end.containsY(top);
			boolean crossBottom = end.containsY(bottom);
			
			if(crossBottom)
			{
				if(crossTop)
				{
					// 01
					p.moveto(left, top);
					p.lineto(end.x, top);
					p.lineto(end.x, bottom);
					p.lineto(left, bottom);
					p.lineto(left, top);
				}
				else
				{
					// 02
					p.moveto(left, top);
					p.lineto(right, top);
					p.lineto(right, end.y0);
					p.lineto(end.x, end.y0);
					p.lineto(end.x, bottom);
					p.lineto(left, bottom);
					p.lineto(left, top);
				}
			}
			else
			{
				if(crossTop)
				{
					// 03
					p.moveto(left, top);
					p.lineto(end.x, top);
					p.lineto(end.x, end.y1);
					p.lineto(left, end.y1);
					p.lineto(left, top);
				}
				else
				{
					// 05
					p.moveto(left, top);
					p.lineto(right, top);
					p.lineto(right, end.y0);
					p.lineto(end.x, end.y0);
					p.lineto(end.x, end.y1);
					p.lineto(left, end.y1);
					p.lineto(left, top);
				}
			}
		}
		else if(end == null)
		{
			// end caret is below the visible area
			boolean crossTop = beg.containsY(top);
			boolean crossBottom = beg.containsY(bottom);
			
			if(crossTop)
			{
				if(crossBottom)
				{
					// 06
					p.moveto(beg.x, top);
					p.lineto(right, top);
					p.lineto(right, bottom);
					p.lineto(beg.x, bottom);
					p.lineto(beg.x, top);
				}
				else
				{
					// 07
					p.moveto(beg.x, top);
					p.lineto(right, top);
					p.lineto(right, bottom);
					p.lineto(left, bottom);
					p.lineto(left, beg.y1);
					p.lineto(beg.x, beg.y1);
					p.lineto(beg.x, top);
				}
			}
			else
			{
				if(crossBottom)
				{
					// 08
					p.moveto(beg.x, beg.y0);
					p.lineto(right, beg.y0);
					p.lineto(right, bottom);
					p.lineto(beg.x, bottom);
					p.lineto(beg.x, beg.y0);
				}
				else
				{
					// 09
					p.moveto(beg.x, beg.y0);
					p.lineto(right, beg.y0);
					p.lineto(right, bottom);
					p.lineto(left, bottom);
					p.lineto(left, beg.y1);
					p.lineto(beg.x, beg.y1);
					p.lineto(beg.x, beg.y0);
				}
			}
		}
		else
		{
			// both carets are in the visible area
			if(EditorTools.isCloseEnough(beg.y0, end.y0))
			{
				// 10
				p.moveto(beg.x, beg.y0);
				p.lineto(end.x, beg.y0);
				p.lineto(end.x, end.y1);
				p.lineto(beg.x, end.y1);
				p.lineto(beg.x, beg.y0);
			}
			else
			{
				// 11
				p.moveto(beg.x, beg.y0);
				p.lineto(right, beg.y0);
				p.lineto(right, end.y0);
				p.lineto(end.x, end.y0);
				p.lineto(end.x, end.y1);
				p.lineto(left, end.y1);
				p.lineto(left, beg.y1);
				p.lineto(beg.x, beg.y1);
				p.lineto(beg.x, beg.y0);
			}
		}
	}


}
