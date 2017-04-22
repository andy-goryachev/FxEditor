// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.common.util.D;
import goryachev.fx.Binder;
import goryachev.fx.CBooleanProperty;
import goryachev.fx.CssStyle;
import goryachev.fx.FX;
import goryachev.fx.edit.internal.CaretLocation;
import goryachev.fx.edit.internal.EditorTools;
import goryachev.fx.edit.internal.Markers;
import goryachev.fx.util.FxPathBuilder;
import java.io.StringWriter;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.Observable;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.util.Duration;


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
	protected final ReadOnlyObjectWrapper<FxEditorModel> modelProperty = new ReadOnlyObjectWrapper<>();
	protected final CBooleanProperty wrapText = new CBooleanProperty(true, this::requestLayout);
	protected final ReadOnlyBooleanWrapper multipleSelection = new ReadOnlyBooleanWrapper(false);
	protected final ObservableList<SelectionSegment> segments = FXCollections.observableArrayList();
	protected final ReadOnlyObjectWrapper<EditorSelection> selection = new ReadOnlyObjectWrapper(EditorSelection.EMPTY);
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
	protected final BooleanProperty displayCaret = new SimpleBooleanProperty(true);
	protected final BooleanProperty caretVisible = new SimpleBooleanProperty(true);
	protected final ReadOnlyObjectWrapper<Duration> caretBlinkRate = new ReadOnlyObjectWrapper(Duration.millis(500));
	protected final Timeline caretAnimation;
	protected final Path caretPath;
	protected final Path selectionHighlight;
	protected final EditorSelectionController selector;
	protected final KeyMap keymap;

	
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
		caretPath.visibleProperty().bind(new BooleanBinding()
		{
			{
				bind(focusedProperty(), disabledProperty(), displayCaret, caretVisible);
			}

			protected boolean computeValue()
			{
				return caretVisible.get() && isDisplayCaret() && isFocused() && (!isDisabled());
			}
		});
		
		caretAnimation = new Timeline();
		caretAnimation.setCycleCount(Animation.INDEFINITE);
		Binder.onChange(this::updateBlinkRate, true, blinkRateProperty());
		
		getChildren().addAll(selectionHighlight, vscroll(), caretPath);
		
		selector = createSelectionController();
		segments.addListener((Observable src) -> reloadSelectionDecorations());
		Binder.onChange(this::requestLayout, widthProperty(), heightProperty());
		
		keymap = createKeyMap();
		
		initMouseController();
		
		// init key handler
		addEventFilter(KeyEvent.ANY, (ev) ->
		{
			if(!ev.isConsumed())
			{
				Runnable a = keymap.getActionForKeyEvent(ev);
				if(a != null)
				{
					a.run();
				}
			}
		});
	}
	
	
	/** override to provide your own selection model */
	protected EditorSelectionController createSelectionController()
	{
		return new EditorSelectionController(segments);
	}
	
	
	/** override to provide your own controller */
	protected void initMouseController()
	{
		FxEditorMouseController h = new FxEditorMouseController(this, selector);
		
		addEventFilter(MouseEvent.MOUSE_PRESSED, (ev) -> h.handleMousePressed(ev));
		addEventFilter(MouseEvent.MOUSE_RELEASED, (ev) -> h.handleMouseReleased(ev));
		addEventFilter(MouseEvent.MOUSE_DRAGGED, (ev) -> h.handleMouseDragged(ev));
		addEventFilter(ScrollEvent.ANY, (ev) -> h.handleScroll(ev));
	}
	
	
	/** override to provide your own controller */
	protected KeyMap createKeyMap()
	{
		KeyMap m = new KeyMap();
		m.shortcut(KeyCode.C, this::copy);
		m.add(KeyCode.PAGE_DOWN, this::pageDown);
		m.add(KeyCode.PAGE_UP, this::pageUp);
		return m;
	}
	
	
	public ReadOnlyObjectProperty<EditorSelection> selectionProperty()
	{
		return selection.getReadOnlyProperty();
	}
	
	
	public EditorSelection getSelection()
	{
		return selection.get();
	}
	
	
	/** perhaps make this method public */
	protected void setSelection(EditorSelection es)
	{
		selection.set(es);
	}
	
	
	public void clearSelection()
	{
		selector.clear();
	}

	
	protected Runnable getActionForKeyEvent(KeyEvent ev)
	{
		return null;
	}
	
	
	public void setTextModel(FxEditorModel m)
	{
		markers.clear();
		
		FxEditorModel old = getTextModel();
		if(old != null)
		{
			old.removeListener(this);
		}
		
		modelProperty.set(m);
		
		if(m != null)
		{
			m.addListener(this);
		}
		
		requestLayout();
	}
	
	
	public FxEditorModel getTextModel()
	{
		return modelProperty.get();
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
		return wrapText.get();
	}
	
	
	public void setWrapText(boolean on)
	{
		wrapText.set(on);
	}
	
	
	public BooleanProperty wrapTextProperty()
	{
		return wrapText;
	}
	
	
	public void setMultipleSelectionEnabled(boolean on)
	{
		multipleSelection.set(on);
	}
	
	
	public boolean isMultipleSelectionEnabled()
	{
		return multipleSelection.get();
	}
	
	
	public ReadOnlyBooleanProperty multipleSelectionProperty()
	{
		return multipleSelection.getReadOnlyProperty();
	}
	
	
	public ReadOnlyObjectProperty<FxEditorModel> modelProperty()
	{
		return modelProperty.getReadOnlyProperty();
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
		FxEditorModel model = getTextModel();
		int lines = model.getLineCount();
		FxEditorLayout la = new FxEditorLayout(topLineIndex, offsety);
		
		Insets pad = getInsets();
		double maxy = height - pad.getBottom();
		double y = pad.getTop();
		double x0 = pad.getLeft();
		boolean wrap = isWrapText();
		double wid = width - x0 - pad.getRight() - vscroll.getWidth(); // TODO leading, trailing components
		
		for(int ix=topLineIndex; ix<lines; ix++)
		{
			Region nd = model.getDecoratedLine(ix);
			getChildren().add(nd);
			nd.applyCss();
			nd.setManaged(true);
			
			double w = wrap ? wid : nd.prefWidth(-1);
			nd.setMaxWidth(wrap ? wid : Double.MAX_VALUE); 
			double h = nd.prefHeight(w);
			
			LineBox b = new LineBox(ix, nd);
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
	
	
	public void setDisplayCaret(boolean on)
	{
		displayCaret.set(on);
	}
	
	
	public boolean isDisplayCaret()
	{
		return displayCaret.get();
	}
	
	
	// blinking caret
	protected void setCaretVisible(boolean on)
	{
		caretVisible.set(on);
	}
	

	// TODO part of move caret
//	protected void setCaretElements(PathElement[] es)
//	{
//		// reset caret so it's always on when moving, unlike MS Word
//		caretAnimation.stop();
//		caretPath.getElements().setAll(es);
//		caretAnimation.play();
//	}

	
	protected void reloadSelectionDecorations()
	{
		FxPathBuilder hb = new FxPathBuilder();
		FxPathBuilder cb = new FxPathBuilder();
		
		for(SelectionSegment s: segments)
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
		CaretLocation c = getCaretLocation(m);
		if(c != null)
		{
			p.moveto(c.x, c.y0);
			p.lineto(c.x, c.y1);
		}
	}
	
	
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
		
		double left = 0.0;
		double right = getWidth() - left - vscroll().getWidth();
		double top = 0.0; 
		double bottom = getHeight(); // TODO hscroll
		
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


	/** returns plain text on the specified line */
	public String getTextOnLine(int line)
	{
		return modelProperty.get().getPlainText(line);
	}


	/** returns selected plain text, concatenating multiple selection segments if necessary */
	public String getSelectedText() throws Exception
	{
		StringWriter wr = new StringWriter();
		modelProperty.get().getPlainText(getSelection(), wr);
		return wr.toString();
	}
	
	
	public void pageUp()
	{
		// TODO
		D.print();
	}
	
	
	public void pageDown()
	{
		// TODO
		D.print();
	}
	
	
	public void copy()
	{
		// TODO use model to copy every data format it can
		modelProperty.get().copy(getSelection());
	}
	
	
	public void selectAll()
	{
		// TODO
	}
}
