// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.common.util.CList;
import goryachev.common.util.D;
import goryachev.fx.CssStyle;
import goryachev.fx.FX;
import goryachev.fx.FxInvalidationListener;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.util.Duration;
import research.fx.edit.internal.CaretLocation;
import research.fx.edit.internal.FxEditorTools;
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
	protected final ReadOnlyObjectWrapper<Duration> blinkRate = new ReadOnlyObjectWrapper(Duration.millis(500));
	// TODO multiple selection
	// TODO caret visible
	// TODO line decorations/line numbers
	protected FxEditorLayout layout;
	/** index of the topmost visible line */
	protected int topLineIndex;
	/** defines horizontal shift */
	protected int offsetx;
	/** vertical shift applied to topmost line */
	protected int offsety;
	protected Markers markers = new Markers(32);
	protected ScrollBar vscroll;
	protected ScrollBar hscroll;
	protected final Timeline caretAnimation;
	protected final Path caretPath;
	protected final Path selectionHighlight;
	/** multiple selection segments: the end position corresponds to the caret */ 
	protected final ObservableList<SelectionSegment> segments = FXCollections.observableArrayList();


	
	public FxEditor()
	{
		this(FxEditorModel.getEmptyModel());
	}
	
	
	public FxEditor(FxEditorModel m)
	{
		setFocusTraversable(true);
		setModel(m);
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
		new FxInvalidationListener(blinkRateProperty(), true, () -> updateBlinkRate(getBlinkRate()));
		
		getChildren().addAll(selectionHighlight, vscroll(), caretPath);
		
		initController();
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
	
	
	public void setModel(FxEditorModel m)
	{
		FxEditorModel old = getModel();
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
	
	
	public FxEditorModel getModel()
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
		int start = FX.round(getModel().getLineCount() * pos);
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
	
	
	protected void layoutChildren()
	{
		layout = updateLayout(layout);
	}
	
	
	protected void setTopLineIndex(int x)
	{
		topLineIndex = x;
		requestLayout();
		// FIX update selection
	}
	
	
	protected FxEditorLayout updateLayout(FxEditorLayout prev)
	{
		if(prev != null)
		{
			prev.removeFrom(getChildren());
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
		FxEditorModel m = getModel();
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
			n.setManaged(true);
			
			double w = wrap ? wid : n.prefWidth(-1);
			n.setMaxWidth(wrap ? wid : Double.MAX_VALUE); 
			double h = n.prefHeight(w);
			
			LineBox b = new LineBox(ix, n);
			la.add(b);
			
			layoutInArea(n, x0, y, w, h, 0, null, true, true, HPos.LEFT, VPos.TOP);
			
			y += h;
			if(y > maxy)
			{
				break;
			}
		}
		
		la.addTo(getChildren());
		
		return la;
	}
	
	
	/** returns text position at the specified screen coordinates */
	public TextPos getTextPos(double screenx, double screeny)
	{
		return layout.getTextPos(screenx, screeny);
	}
	
	
	protected CaretLocation getCaretLocation(TextPos pos)
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
		return blinkRate.getReadOnlyProperty();
	}
	
	
	public Duration getBlinkRate()
	{
		return blinkRate.get();
	}
	
	
	public void setBlinkRate(Duration d)
	{
		blinkRate.set(d);
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
	
	
	
	protected void updateBlinkRate(Duration d)
	{
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
		caretPath.getElements().clear();
		selectionHighlight.getElements().clear();
		segments.clear();
	}

	
	// FIX setCaretEnabled
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

	
	protected boolean isInsideSelection(TextPos pos)
	{
		for(SelectionSegment s: segments)
		{
			if(s.contains(pos))
			{
				return true;
			}
		}
		return false;
	}
	

	/** adds a new segment from start to end */
	protected void addSelectionSegment(TextPos start, TextPos end)
	{
		segments.add(new SelectionSegment(start, end));
		selectionHighlight.getElements().addAll(createHighlightPath(start, end));
		caretPath.getElements().addAll(createCaretPath(end));
		
		// TODO combine overlapping segments
	}
	
	
	protected void clearAndExtendLastSegment(TextPos pos)
	{
		TextPos anchor = lastAnchor();
		if(anchor == null)
		{
			anchor = pos;
		}
		
		clearSelection();
		addSelectionSegment(anchor, pos);
	}
	
	
	protected void extendLastSegment(TextPos pos)
	{
		if(pos == null)
		{
			return;
		}
		
		int ix = segments.size() - 1;
		if(ix < 0)
		{
			 addSelectionSegment(pos, pos);
		}
		else
		{
			SelectionSegment s = segments.get(ix);
			TextPos anchor = s.getStart();
			segments.set(ix, new SelectionSegment(anchor, pos));
			
			// TODO combine overlapping segments
			reloadDecorations();
		}
	}
	
	
	protected TextPos lastAnchor()
	{
		int ix = segments.size() - 1;
		if(ix >= 0)
		{
			SelectionSegment s = segments.get(ix);
			return s.getStart();
		}
		return null;
	}
	
	
	// this method can possibly be optimized to modify decorations when possible
	// instead of re-creating them, to minimize flicker
	protected void reloadDecorations()
	{
		CList<PathElement> hs = new CList<>();
		CList<PathElement> cs = new CList<>();
		
		for(SelectionSegment s: segments)
		{
			TextPos start = s.getStart();
			TextPos end = s.getEnd();
			
			hs.addAll(createHighlightPath(start, end));
			cs.addAll(createCaretPath(end));
		}
		
		selectionHighlight.getElements().setAll(hs);
		caretPath.getElements().setAll(cs);
	}
	
	
	protected CList<PathElement> createCaretPath(TextPos p)
	{
		CList<PathElement> rv = new CList<>();
		CaretLocation c = getCaretLocation(p);
		if(c != null)
		{
			// TODO insert shape?
			rv.add(new MoveTo(c.x0, c.y0));
			rv.add(new LineTo(c.x0, c.y1));
		}
		return rv;
	}
	
	
	protected CList<PathElement> createHighlightPath(TextPos start, TextPos end)
	{		
		CList<PathElement> rv = new CList<>();
		
		if((start == null) || (end == null))
		{
			return rv;
		}
		
		if(start.compareTo(end) > 0)
		{
			TextPos tmp = start;
			start = end;
			end = tmp;
		}

		CaretLocation top = getCaretLocation(start);
		CaretLocation bot = getCaretLocation(end);
		
		if((top == null) || (bot == null))
		{
			return rv;
		}
		
		rv.add(new MoveTo(top.x0, top.y0));
		if(FxEditorTools.isNearlySame(top.y0, bot.y0))
		{
			rv.add(new LineTo(bot.x0, top.y0));
			rv.add(new LineTo(bot.x0, bot.y1));
			rv.add(new LineTo(top.x0, bot.y1));
			//rv.add(new ClosePath());
			rv.add(new LineTo(top.x0, top.y0));
		}
		else
		{
			double right = getWidth();
			double left = 0.0; // FIX padding
			
			rv.add(new LineTo(right, top.y0));
			rv.add(new LineTo(right, bot.y0));
			rv.add(new LineTo(bot.x0, bot.y0));
			rv.add(new LineTo(bot.x0, bot.y1));
			rv.add(new LineTo(left, bot.y1));
			rv.add(new LineTo(left, top.y1));
			rv.add(new LineTo(top.x0, top.y1));
			//rv.add(new ClosePath());
			rv.add(new LineTo(top.x0, top.y0));
		}
		
		return rv;
	}
}
