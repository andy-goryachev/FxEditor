// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.fx.FX;
import goryachev.fx.edit.internal.CaretLocation;
import goryachev.fx.edit.internal.EditorTools;
import goryachev.fx.edit.internal.SelectionHelper;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.Rectangle;
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
	protected final BooleanProperty suppressBlink = new SimpleBooleanProperty(false);
	protected final Rectangle clip;
	// TODO line decorations/line numbers
	protected FxEditorLayout layout;
	/** index of the topmost visible line */
	protected int topLineIndex;
	/** horizontal shift in pixels */
	protected double offsetx;
	/** vertical offset or the viewport relative to the topmost line.  always positive */
	protected double offsety;

	
	public VFlow(FxEditor ed)
	{
		this.editor = ed;
		
		clip = new Rectangle();
		
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
		setClip(clip);
		
		caretPath.visibleProperty().bind(new BooleanBinding()
		{
			{
				bind(caretVisible, editor.displayCaretProperty, editor.focusedProperty(), editor.disabledProperty(), suppressBlink);
			}

			protected boolean computeValue()
			{
				return (isCaretVisible() || suppressBlink.get()) && editor.isDisplayCaret() && editor.isFocused() && (!editor.isDisabled());
			}
		});
	}
	
	
	public void setOrigin(int top, double offy)
	{
		topLineIndex = top;
		offsety = offy;
		
		layoutChildren();
		
		// update scroll
		editor.setHandleScrollEvents(false);
		int max = editor.getLineCount();
		double v = (max == 0 ? 0.0 : top / (double)max); 
		editor.vscroll.setValue(v);
		editor.setHandleScrollEvents(true);
	}


	public void setTopLineIndex(int ix)
	{
		topLineIndex = ix;
	}
	
	
	protected void layoutChildren()
	{
		layout = recreateLayout(layout);
		updateCaretAndSelection();
	}
	
	
	public void invalidateLayout()
	{
		if(layout != null)
		{
			layout.removeFrom(this);
		}
		layout = null;
	}
	
	
	public void reset()
	{
		offsetx = 0;
		offsety = 0;
		topLineIndex = 0;
	}
	
	
	public void setSuppressBlink(boolean on)
	{
		suppressBlink.set(on);
		
		if(!on)
		{
			// restart animation cycle
			updateBlinkRate();
		}
	}
	
	
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
	
	
	/** used for blinking animation */
	protected void setCaretVisible(boolean on)
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
		clip.setWidth(width);
		clip.setHeight(height);
		
		// TODO is loaded?
		FxEditorModel model = editor.getTextModel();
		int lines = model.getLineCount();
		FxEditorLayout la = new FxEditorLayout(editor, topLineIndex);
		
		Insets pad = getInsets();
		double maxy = height - pad.getBottom();
		double y = pad.getTop() - offsety;
		double x0 = pad.getLeft();
		boolean wrap = editor.isWrapText();
		
		// TODO account for leading, trailing components
		double wid = width - x0 - pad.getRight();
		
		for(int ix=topLineIndex; ix<lines; ix++)
		{
			LineBox b = (prev == null ? null : prev.getLineBox(ix));
			
			if(b == null)
			{
				b = model.getDecoratedLine(ix);
				b.init(ix);
			}
			
			Region nd = b.getCenter();
			getChildren().add(nd);
			nd.applyCss();
			nd.setManaged(true);
			la.addLineBox(b);
			
			double w = wrap ? wid : nd.prefWidth(-1);
			nd.setMaxWidth(wrap ? wid : Double.MAX_VALUE);
			
			double h = nd.prefHeight(w);
			b.setLineHeight(h);
			
			layoutInArea(nd, x0, y, w, h, 0, null, true, true, HPos.LEFT, VPos.TOP);
			
			y += h;
			if(y > maxy)
			{
				break;
			}
		}
		
		return la;
	}
	
	
	public double addAndComputePreferredHeight(Region nd)
	{
		// warning: the same code in recreateLayout() above
		Insets pad = getInsets();
		double x0 = pad.getLeft();
		boolean wrap = editor.isWrapText();
		double width = getWidth();
		
		// TODO account for leading, trailing components
		double wid = width - x0 - pad.getRight();
		
		getChildren().add(nd);
		nd.applyCss();
		nd.setManaged(true);
		
		double w = wrap ? wid : nd.prefWidth(-1);
		nd.setMaxWidth(wrap ? wid : Double.MAX_VALUE);
		return nd.prefHeight(w);
	}
	
	
	public void updateCaretAndSelection()
	{
		FxPathBuilder selectionBuilder = new FxPathBuilder();
		FxPathBuilder caretBuilder = new FxPathBuilder();
		
		for(SelectionSegment s: editor.selector.segments)
		{
			Marker start = s.getMin();
			Marker end = s.getMax();
			Marker caret = s.getCaret();
			
			createSelectionHighlight(selectionBuilder, start, end);
			createCaretPath(caretBuilder, caret);
		}
		
		selectionHighlight.getElements().setAll(selectionBuilder.getPath());
		caretPath.getElements().setAll(caretBuilder.getPath());
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
	
	
	protected FxEditorLayout getEditorLayout()
	{
		if(layout == null)
		{
			layout = new FxEditorLayout(editor, topLineIndex);
		}
		return layout;
	}
	
	
	public void pageUp()
	{
		blockScroll(getHeight(), true);
	}
	
	
	public void pageDown()
	{
		blockScroll(getHeight(), false);
	}
	
	
	public void blockScroll(boolean up)
	{
		// this could be a preference
		double BLOCK_SCROLL_FACTOR = 0.1;
		double BLOCK_MIN_SCROLL = 40;
		
		double h = getHeight();
		double delta = h * BLOCK_SCROLL_FACTOR;
		if(delta < BLOCK_MIN_SCROLL)
		{
			delta = h;
		}
		
		blockScroll(delta, up);
	}
	
	
	protected void blockScroll(double delta, boolean up)
	{
		if(up)
		{
			if(delta <= offsety)
			{
				// no need to query the model
				setOrigin(topLineIndex, offsety -= delta);
				return;
			}
			else
			{
				int ix = topLineIndex;
				double targetY = -delta;
				double y = -offsety;
					
				for(;;)
				{
					--ix;
					if(ix < 0)
					{
						// top line
						setOrigin(0, 0);
						return;
					}
					
					double dy = getEditorLayout().getLineHeight(ix);
					y -= dy;
					if(y < targetY)
					{
						break;
					}
				}
				
				setOrigin(ix, targetY - y);
				return;
			}
		}
		else
		{
			int ix = topLineIndex;
			double targetY = delta;
			double y = -offsety;
			
			for(;;)
			{
				if(ix >= editor.getLineCount())
				{
					// FIX need to figure out what to do in this case
					break;
				}
				
				double dy = getEditorLayout().getLineHeight(ix);
				if(y + dy > targetY)
				{
					setOrigin(ix, targetY - y);
					return;
				}
				
				y += dy;
				ix++;
			}
		}
	}
	
	
	protected PathElement[] getRangeTop()
	{
		double w = getWidth();
		
		return new PathElement[]
		{
			new MoveTo(0, -1),
			new LineTo(w, -1),
			new LineTo(w, 0),
			new LineTo(0, 0),
			new LineTo(0, -1)
		};
	}
	
	
	protected PathElement[] getRangeBottom()
	{
		double w = getWidth();
		double h = getHeight();
		double h1 = h + 1;
		
		return new PathElement[]
		{
			new MoveTo(0, h),
			new LineTo(w, h),
			new LineTo(w, h1),
			new LineTo(0, h1),
			new LineTo(0, h)
		};
	}
	
	
	protected PathElement[] getRangeShape(int line, int startOffset, int endOffset)
	{
		LineBox lineBox = layout.getLineBox(line);
		if(lineBox == null)
		{
			return null;
		}
		
		if(endOffset < 0)
		{
			endOffset = lineBox.getTextLength();
		}
		
		PathElement[] pe;
		if(startOffset == endOffset)
		{
			// not a range, use caret shape instead
			pe = lineBox.getCaretShape(startOffset, false);
		}
		else
		{
			pe = lineBox.getRange(startOffset, endOffset);
		}
		
		if(pe == null)
		{
			return null;
		}
		else
		{
			return EditorTools.translatePath(this, lineBox.getCenter(), pe);	
		}
	}
	
	
	/**
	 * Populates path builder with selection shapes between two markers.
	 * This method handles RTL and LTR text.
	 */
	protected void createSelectionHighlight(FxPathBuilder b, Marker startMarker, Marker endMarker)
	{
		if((startMarker == null) || (endMarker == null))
		{
			return;
		}
		
		// enforce startMarker < endMarker
		if(startMarker.compareTo(endMarker) > 0)
		{
			throw new Error(startMarker + "<" + endMarker);
		}
		
		if(endMarker.getLine() < topLineIndex)
		{
			// selection is above visible area
			return;
		}
		else if(startMarker.getLine() >= (topLineIndex + layout.getVisibleLineCount()))
		{
			// selection is below visible area
			return;
		}
		
		// get selection shapes for top and bottom lines
		// translated to this VFlow coordinates.
		// when we say "visible text line" we mean the first row of text, since the model text line
		// might contain multiple visible rows due to wrapping.
		PathElement[] top;
		PathElement[] bottom;
		if(startMarker.getLine() == endMarker.getLine())
		{
			top = getRangeShape(startMarker.getLine(), startMarker.getLineOffset(), endMarker.getLineOffset());
			bottom = null;
		}
		else
		{
			top = getRangeShape(startMarker.getLine(), startMarker.getLineOffset(), -1);
			if(top == null)
			{
				top = getRangeTop();
			}
			
			bottom = getRangeShape(endMarker.getLine(), 0, endMarker.getLineOffset());
			if(bottom == null)
			{
				bottom = getRangeBottom();
			}
		}
		
		// generate shapes
		double left = 0.0;
		double right = getWidth() - left;
		SelectionHelper h = new SelectionHelper(b, left, right);
		
		h.process(top);
		
		if(bottom == null)
		{
			h.generateTop(top);
			h.generateMiddle();
			h.generateBottom(top);
		}
		else
		{
			h.process(bottom);

			h.generateTop(top);
			h.generateMiddle();
			h.generateBottom(bottom);
		}
	}
}
