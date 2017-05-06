// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.common.util.D;
import goryachev.fx.Binder;
import goryachev.fx.CAction;
import goryachev.fx.CBooleanProperty;
import goryachev.fx.CssStyle;
import goryachev.fx.FX;
import goryachev.fx.edit.internal.CaretLocation;
import goryachev.fx.edit.internal.Markers;
import java.io.StringWriter;
import javafx.beans.Observable;
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
import javafx.scene.paint.Color;
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
	
	public final CAction copyAction = new CAction(this::copy);
	public final CAction selectAllAction = new CAction(this::selectAll);
	
	protected final SimpleBooleanProperty editable = new SimpleBooleanProperty(false); // TODO for now
	protected final ReadOnlyObjectWrapper<FxEditorModel> modelProperty = new ReadOnlyObjectWrapper<>();
	protected final CBooleanProperty wrapTextProperty = new CBooleanProperty(true, this::updateLayout);
	protected final ReadOnlyBooleanWrapper multipleSelection = new ReadOnlyBooleanWrapper(false);
	protected final ObservableList<SelectionSegment> segments = FXCollections.observableArrayList();
	protected final ReadOnlyObjectWrapper<EditorSelection> selection = new ReadOnlyObjectWrapper(EditorSelection.EMPTY);
	protected Markers markers = new Markers(32);
	protected final VFlow vflow;
	protected final ScrollBar vscroll;
	protected final ScrollBar hscroll;
	protected final BooleanProperty displayCaret = new SimpleBooleanProperty(true);
	protected final ReadOnlyObjectWrapper<Duration> caretBlinkRate = new ReadOnlyObjectWrapper(Duration.millis(500));
	protected final EditorSelectionController selector;
	protected final KeyMap keymap;
	protected boolean handleScrollEvents = true;

	
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
		
		vflow = new VFlow(this);
		
		vscroll = createVScrollBar();
		
		hscroll = createHScrollBar();
		hscroll.visibleProperty().bind(wrapTextProperty.not());
		
		getChildren().addAll(vflow, vscroll, hscroll);
		
		selector = createSelectionController();
		segments.addListener((Observable src) -> vflow.reloadSelectionDecorations());

		Binder.onChange(vflow::updateBlinkRate, true, blinkRateProperty());
		Binder.onChange(this::updateLayout, widthProperty(), heightProperty());
		
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
	
	
	public void setContentPadding(Insets m)
	{
		vflow.setPadding(m);
	}
	
	
	/** override to provide your own selection model */
	protected EditorSelectionController createSelectionController()
	{
		return new EditorSelectionController(this, segments);
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
		m.shortcut(KeyCode.A, this::selectAll);
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
		
		updateLayout();
	}
	
	
	public FxEditorModel getTextModel()
	{
		return modelProperty.get();
	}
	
	
	public int getLineCount()
	{
		return getTextModel().getLineCount();
	}
	
	
	protected ScrollBar createVScrollBar()
	{
		ScrollBar s = new ScrollBar();
		s.setOrientation(Orientation.VERTICAL);
		s.setManaged(true);
		s.setMin(0.0);
		s.setMax(1.0);
		s.valueProperty().addListener((src,old,val) -> setAbsolutePositionVertical(val.doubleValue()));
		s.addEventFilter(ScrollEvent.ANY, (ev) -> ev.consume());
		return s;
	}
	
	
	protected ScrollBar createHScrollBar()
	{
		ScrollBar s = new ScrollBar();
		s.setOrientation(Orientation.HORIZONTAL);
		s.setManaged(true);
		s.setMin(0.0);
		s.setMax(1.0);
		s.valueProperty().addListener((src,old,val) -> setAbsolutePositionHorizontal(val.doubleValue()));
		s.addEventFilter(ScrollEvent.ANY, (ev) -> ev.consume());
		return s;
	}
	
	
	protected void setAbsolutePositionVertical(double pos)
	{
		if(handleScrollEvents)
		{
			// TODO account for visible line count
			int start = FX.round(getTextModel().getLineCount() * pos);
			setTopLineIndex(start);
		}
	}
	
	
	protected void setAbsolutePositionHorizontal(double pos)
	{
		// TODO
	}
	
	
	protected void setHandleScrollEvents(boolean on)
	{
		handleScrollEvents = on;
	}
	
	
	public boolean isWrapText()
	{
		return wrapTextProperty.get();
	}
	
	
	public void setWrapText(boolean on)
	{
		wrapTextProperty.set(on);
	}
	
	
	public BooleanProperty wrapTextProperty()
	{
		return wrapTextProperty;
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
	
	
	protected void setTopLineIndex(int ix)
	{
		vflow.setTopLineIndex(ix);
		updateLayout();
	}
	
	
	protected void updateLayout()
	{
		if(vflow != null)
		{
			vflow.requestLayout();
		}
		requestLayout();
	}
	
	
	protected void layoutChildren()
	{
		Insets m = getPadding();
		double x0 = m.getLeft();
		double y0 = m.getTop();
		
		double vscrollWidth = 0.0;
		double hscrollHeight = 0.0;
		
		// position the scrollbar(s)
		if(vscroll.isVisible())
		{
			vscrollWidth = vscroll.prefWidth(-1);
		}
		
		if(hscroll.isVisible())
		{
			hscrollHeight = hscroll.prefHeight(-1);
		}
		
		double w = getWidth() - m.getLeft() - m.getRight() - vscrollWidth;
		double h = getHeight() - m.getTop() - m.getBottom() - hscrollHeight;

		// layout children
		layoutInArea(vscroll, w, y0, vscrollWidth, h, 0, null, true, true, HPos.RIGHT, VPos.TOP);
		layoutInArea(hscroll, x0, h, w, hscrollHeight, 0, null, true, true, HPos.LEFT, VPos.BOTTOM);
		layoutInArea(vflow, x0, y0, w, h, 0, null, true, true, HPos.LEFT, VPos.TOP);
	}
	
	
	/** returns text position at the specified screen coordinates */
	public Marker getTextPos(double screenx, double screeny)
	{
		return vflow.layout.getTextPos(screenx, screeny, markers);
	}
	
	
	protected CaretLocation getCaretLocation(Marker pos)
	{
		return vflow.layout.getCaretLocation(this, pos);
	}
	
	
	protected int getViewStartLine()
	{
		return vflow.layout.startLine();
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

	
	protected void eventAllChanged()
	{
		clearSelection();
		vflow.invalidateLayout();
		vflow.reset();
		
		vscroll.setValue(0);
		hscroll.setValue(0);
		
		updateLayout();
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
	
	
	public void setDisplayCaret(boolean on)
	{
		displayCaret.set(on);
	}
	
	
	public boolean isDisplayCaret()
	{
		return displayCaret.get();
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
		vflow.pageUp();
	}
	
	
	public void pageDown()
	{
		vflow.pageDown();
	}
	
	
	public void blockScroll(boolean up)
	{
		vflow.blockScroll(up);
	}
	
	
	public void copy()
	{
		modelProperty.get().copy(getSelection());
	}
	
	
	public void selectAll()
	{
		int ix = getLineCount();
		if(ix > 0)
		{
			--ix;
			
			String s = getTextModel().getPlainText(ix);
			Marker beg = new Marker(0, 0, true);
			Marker end = new Marker(ix, Math.max(0, s.length() - 1), false);
			
			selector.setSelection(beg, end);
			selector.commitSelection();
		}
	}
	
	
	protected void setSuppressBlink(boolean on)
	{
		vflow.setSuppressBlink(on);
	}


	public void scrollToVisible(int ix)
	{
		if((ix >= 0) && (ix < getLineCount()))
		{
			// FIX smarter positioning so the target line is somewhere at 25% of the height
			vflow.setOrigin(ix, 0);
		}
	}
}
