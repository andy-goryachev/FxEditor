// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.common.util.D;
import goryachev.fx.CssStyle;
import goryachev.fx.FX;
import goryachev.fx.util.CPathBuilder;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.util.Duration;
import research.fx.Binder;
import research.fx.edit.internal.CaretLocation;
import research.fx.edit.internal.Markers;


/**
 * Fx Text Editor, intended to provide capabilities missing in JavaFX, such as:
 * <pre>
 * - text editor with syntax highlighing
 * - capability to display and edit large texts (up to 2 billion rows)
 * - capability to use external models (disk, net)
 * - capability to insert arbitrary row widgets
 */
public class FxEditor
	extends Pane
{
	/** caret style */
	public static final CssStyle CARET = new CssStyle("FxEditor_CARET");
	/** selection highlight */
	public static final CssStyle HIGHLIGHT = new CssStyle("FxEditor_HIGHLIGHT");
	/** panel style */
	public static final CssStyle PANEL = new CssStyle("FxEditor_PANEL");
	
	protected final SimpleBooleanProperty editable = new SimpleBooleanProperty(false); // TODO for now
	protected final ReadOnlyObjectWrapper<FxEditorModel> model = new ReadOnlyObjectWrapper<>();
	protected final ReadOnlyObjectWrapper<Boolean> wrap = new ReadOnlyObjectWrapper<Boolean>(true)
	{
		protected void invalidated()
		{
			requestLayout();
		}
	};
	protected final ReadOnlyObjectWrapper<Boolean> singleSelection = new ReadOnlyObjectWrapper<>();
	// TODO multiple selection
	// TODO caret visible
	// TODO line decorations/line numbers
	protected FxEditorLayout layout;
	/** index of the topmost visible line */
	protected int topLineIndex;
	/** horizontal shift in pixels */
	protected int offsetx;
	/** vertical shift in pixels, applied to topmost line */
	protected int offsety;
	protected Markers markers = new Markers(32);
	protected ScrollBar vscroll;
	protected ScrollBar hscroll;
	protected final ReadOnlyObjectWrapper<Duration> caretBlinkRate = new ReadOnlyObjectWrapper(Duration.millis(500));
	protected final Timeline caretAnimation;
	protected final Path caretPath;
	protected final Path selectionHighlight;
	protected final FxEditorSelectionModel selection;

	
	public FxEditor()
	{
		this(FxEditorModel.getEmptyModel());
	}
	
	
	public FxEditor(FxEditorModel m)
	{
		setFocusTraversable(true);
		setTextModel(m);
		FX.style(this, PANEL);
		setBackground(FX.background(Color.WHITE));
		
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
		Binder.onChange(this::updateBlinkRate, true, blinkRateProperty());
		
		getChildren().addAll(selectionHighlight, vscroll(), caretPath);
		
		selection = createSelectionModel();
		selection.getSelection().addListener((Observable src) -> requestLayout());
		Binder.onChange(this::requestLayout, widthProperty(), heightProperty());
		
		initController();
	}
	
	
	/** override to provide your own selection model */
	protected FxEditorSelectionModel createSelectionModel()
	{
		return new FxEditorSelectionModel();
	}
	
	
	/** override to provide your own controller */
	protected void initController()
	{
		FxEditorController h = new FxEditorController(this);
		
		addEventFilter(KeyEvent.KEY_PRESSED, (ev) -> h.handleKeyPressed(ev));
		addEventFilter(KeyEvent.KEY_RELEASED, (ev) -> h.handleKeyReleased(ev));
		addEventFilter(KeyEvent.KEY_TYPED, (ev) -> h.handleKeyTyped(ev));
		addEventFilter(MouseEvent.MOUSE_PRESSED, (ev) -> h.handleMousePressed(ev));
		addEventFilter(MouseEvent.MOUSE_RELEASED, (ev) -> h.handleMouseReleased(ev));
		addEventFilter(MouseEvent.MOUSE_DRAGGED, (ev) -> h.handleMouseDragged(ev));
		addEventFilter(ScrollEvent.ANY, (ev) -> h.handleScroll(ev));
	}
	
	
	public FxEditorSelectionModel getSelectionModel()
	{
		return selection;
	}
	
	
	public void setTextModel(FxEditorModel m)
	{
		markers.clear();
		
		FxEditorModel old = getTextModel();
		if(old != null)
		{
			old.removeListener(this);
		}
		
		model.set(m);
		
		if(m != null)
		{
			m.addListener(this);
		}
		
		requestLayout();
	}
	
	
	public FxEditorModel getTextModel()
	{
		return model.get();
	}
	
	
	protected ScrollBar vscroll()
	{
		if(vscroll == null)
		{
			vscroll = createVScrollBar();
		}
		return vscroll;
	}
	
	
	protected ScrollBar hscroll()
	{
		if(hscroll == null)
		{
			hscroll = createHScrollBar();
		}
		return hscroll;
	}
	
	
	protected ScrollBar createVScrollBar()
	{
		ScrollBar s = new ScrollBar();
		s.setOrientation(Orientation.VERTICAL);
		s.setManaged(true);
		s.setMin(0.0);
		s.setMax(1.0);
		s.valueProperty().addListener((src,old,val) -> setVerticalAbsolutePosition(val.doubleValue()));
		return s;
	}
	
	
	// TODO
	protected ScrollBar createHScrollBar()
	{
		ScrollBar s = new ScrollBar();
		s.setOrientation(Orientation.HORIZONTAL);
		s.setManaged(true);
		s.setMin(0.0);
		s.setMax(1.0);
		//s.valueProperty().addListener((src,old,val) -> setHAbsolutePosition(val.doubleValue()));
		return s;
	}
	
	
	protected void setVerticalAbsolutePosition(double pos)
	{
		// TODO account for visible line count
		int start = FX.round(getTextModel().getLineCount() * pos);
		setTopLineIndex(start);
	}
	
	
	protected void scrollRelative(double pixels)
	{
		if(pixels < 0)
		{
			double toScroll = pixels;
			int ix = getViewStartLine();
			int offsety = getOffsetY();
			
			LayoutOp op = newLayoutOp();
			
			// TODO
			// using the current layout, add lines until scrolled up to the necessary number of pixels
			// or the first/last line
//			while(toScroll > 0)
//			{
//				if(ix <= 0)
//				{
//					break;
//				}
//			}
		}
		else
		{
			
		}
	}
	
	
	public boolean isWrapText()
	{
		return wrap.get();
	}
	
	
	public void setWrapText(boolean on)
	{
		wrap.set(on);
	}
	
	
	public ReadOnlyObjectProperty<FxEditorModel> modelProperty()
	{
		return model.getReadOnlyProperty();
	}
	
	
	protected void setTopLineIndex(int x)
	{
		topLineIndex = x;
		requestLayout();
	}
	
	
	protected void layoutChildren()
	{
		layout = createLayout(layout);
		reloadSelectionDecorations();
	}
	
	
	protected FxEditorLayout createLayout(FxEditorLayout prev)
	{
		if(prev != null)
		{
			prev.removeFrom(this);
		}
		
		double width = getWidth();
		double height = getHeight();
		
		// position the scrollbar(s)
		ScrollBar vscroll = vscroll();
		if(vscroll.isVisible())
		{
			double w = vscroll.prefWidth(-1);
			layoutInArea(vscroll, width - w, 0, w, height, 0, null, true, true, HPos.LEFT, VPos.TOP);
		}
		
		// TODO is loaded?
		FxEditorModel m = getTextModel();
		int lines = m.getLineCount();
		FxEditorLayout la = new FxEditorLayout(topLineIndex, offsety);
		
		Insets pad = getInsets();
		double maxy = height - pad.getBottom();
		double y = pad.getTop();
		double x0 = pad.getLeft();
		double wid = width - x0 - pad.getRight() - vscroll.getWidth(); // TODO leading, trailing components
		boolean wrap = isWrapText();
		
		for(int ix=topLineIndex; ix<lines; ix++)
		{
			Region n = m.getDecoratedLine(ix);
			getChildren().add(n);
			n.applyCss();
			n.setManaged(true);
			
			double w = wrap ? wid : n.prefWidth(-1);
			n.setMaxWidth(wrap ? wid : Double.MAX_VALUE); 
			double h = n.prefHeight(w);
			
			LineBox b = new LineBox(ix, n);
			la.addLineBox(b);
			
			layoutInArea(n, x0, y, w, h, 0, null, true, true, HPos.LEFT, VPos.TOP);
			
			y += h;
			if(y > maxy)
			{
				break;
			}
		}
		
		return la;
	}
	
	
	/** returns text position at the specified screen coordinates */
	public Marker getTextPos(double screenx, double screeny)
	{
		return layout.getTextPos(screenx, screeny, markers);
	}
	
	
	protected CaretLocation getCaretLocation(Marker pos)
	{
		return layout.getCaretLocation(this, pos);
	}
	
	
	protected int getOffsetX()
	{
		return offsetx;
	}
	
	
	protected int getOffsetY()
	{
		return offsety;
	}
	
	
	protected int getViewStartLine()
	{
		return layout.startLine();
	}
	
	
	public ReadOnlyObjectProperty<Duration> blinkRateProperty()
	{
		return caretBlinkRate.getReadOnlyProperty();
	}
	
	
	public Duration getBlinkRate()
	{
		return caretBlinkRate.get();
	}
	
	
	public void setBlinkRate(Duration d)
	{
		caretBlinkRate.set(d);
	}
	
	
	public boolean isEditable()
	{
		return editable.get();
	}
	
	
	/** enables editing.  FIX not yet editable */
	public void setEditable(boolean on)
	{
		editable.set(on);
	}


	protected LayoutOp newLayoutOp()
	{
		return new LayoutOp(layout);
	}

	
	protected void eventAllChanged()
	{
		clearSelection();
		
		if(vscroll != null)
		{
			vscroll.setValue(0);
		}
		
		if(hscroll != null)
		{
			hscroll.setValue(0);
		}
		
		requestLayout();
	}


	protected void eventLinesDeleted(int start, int count)
	{
		// FIX
		D.print(start, count);
	}


	protected void eventLinesInserted(int start, int count)
	{
		// FIX
		D.print(start, count);
	}


	protected void eventLinesModified(int start, int count)
	{
		// FIX
		D.print(start, count);
	}
	
	
	// TODO stop blinking when dragging
	protected void updateBlinkRate()
	{
		Duration d = getBlinkRate();
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
	
	
	public void clearSelection()
	{
		selection.clear();
	}

	
	public void setCaretVisible(boolean on)
	{
		// FIX property
		caretPath.setVisible(on);
	}
	

	protected void setCaretElements(PathElement[] es)
	{
		// reset caret so it's always on when moving, unlike MS Word
		caretAnimation.stop();
		caretPath.getElements().setAll(es);
		caretAnimation.play();
	}

	
	protected void reloadSelectionDecorations()
	{
		CPathBuilder hb = new CPathBuilder();
		CPathBuilder cb = new CPathBuilder();
		
		for(SelectionSegment s: selection.getSelection())
		{
			Marker start = s.getStart();
			Marker end = s.getEnd();
			
			createSelectionHighlight(hb, start, end);
			createCaretPath(cb, end);
		}
		
		selectionHighlight.getElements().setAll(hb.getPath());
		caretPath.getElements().setAll(cb.getPath());
	}
	
	
	protected void createCaretPath(CPathBuilder p, Marker m)
	{
		CaretLocation c = getCaretLocation(m);
		if(c != null)
		{
			p.moveto(c.x, c.y0);
			p.lineto(c.x, c.y1);
		}
	}
	
	
	protected void createSelectionHighlight(CPathBuilder p, Marker startMarker, Marker endMarker)
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
		
		if(endMarker.getLine() < topLineIndex)
		{
			// selection is above visible area
			return;
		}
		
		if(startMarker.getLine() >= (topLineIndex + layout.getVisibleLineCount()))
		{
			// selection is below visible area
			return;
		}

		CaretLocation beg = getCaretLocation(startMarker);
		CaretLocation end = getCaretLocation(endMarker);
		
//		D.print(offsety, beg, end); // FIX
		
		double left = 0.0;
		double right = getWidth() - left - vscroll().getWidth();
		double top = 0.0; 
		double bottom = getHeight(); // TODO hscroll
		boolean sameLine = startMarker.getLine() == endMarker.getLine();
		
		// there is a number of possible shapes resulting from intersection of
		// the selection shape and the visible area.  the logic below explicitly generates 
		// resulting paths because the selection can be quite large.
		
		if(beg == null)
		{
			if(end == null)
			{
				if((startMarker.getLine() < topLineIndex) && (endMarker.getLine() >= (topLineIndex + layout.getVisibleLineCount())))
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
			
			// start caret is above visible area
			
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
			// end caret is below visible area
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
			if(sameLine)
			{
				p.moveto(beg.x, beg.y0);
				p.lineto(end.x, beg.y0);
				p.lineto(end.x, end.y1);
				p.lineto(beg.x, end.y1);
				p.lineto(beg.x, beg.y0);
			}
			else
			{
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


	/** returns plain text on the specified line */
	public String getTextOnLine(int line)
	{
		return model.get().getPlainText(line);
	}
}
