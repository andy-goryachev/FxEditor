// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.fx.CssStyle;
import goryachev.fx.FX;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.util.Duration;


/**
 * Fx Text Editor.
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
	
	private final SimpleBooleanProperty editable = new SimpleBooleanProperty(false); // TODO for now
	private final ReadOnlyObjectWrapper<FxEditorModel> model = new ReadOnlyObjectWrapper<>();
	private final ReadOnlyObjectWrapper<Boolean> wrap = new ReadOnlyObjectWrapper<Boolean>(true)
	{
		protected void invalidated()
		{
			requestLayout();
		}
	};
	private final ReadOnlyObjectWrapper<Boolean> singleSelection = new ReadOnlyObjectWrapper<>();
	private final ReadOnlyObjectWrapper<Duration> blinkRate = new ReadOnlyObjectWrapper(Duration.millis(500));
	// TODO multiple selection
	// TODO caret visible
	// TODO line decorations/line numbers
	private FxEditorController control = new FxEditorController(this);
	private FxEditorLayout layout;
	private int offsetx;
	private int offsety;
	private int startIndex;
	
	
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
	}
	
	
	public void setModel(FxEditorModel m)
	{
		FxEditorModel old = getModel();
		if(old != null)
		{
			old.removeListener(control);
		}
		
		model.set(m);
		
		if(m != null)
		{
			m.addListener(control);
		}
		
		requestLayout();
	}
	
	
	public FxEditorModel getModel()
	{
		return model.get();
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
	
	
	public void setStartIndex(int x)
	{
		startIndex = x;
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
		ScrollBar vscroll = control.vscroll();
		if(vscroll.isVisible())
		{
			double w = vscroll.prefWidth(-1);
			layoutInArea(vscroll, width - w, 0, w, height, 0, null, true, true, HPos.LEFT, VPos.TOP);
		}
		
		// TODO is loaded?
		FxEditorModel m = getModel();
		int lines = m.getLineCount();
		FxEditorLayout la = new FxEditorLayout(startIndex, offsety);
		
		Insets pad = getInsets();
		double maxy = height - pad.getBottom();
		double y = pad.getTop();
		double x0 = pad.getLeft();
		double wid = width - x0 - pad.getRight() - vscroll.getWidth(); // TODO leading, trailing components
		boolean wrap = isWrapText();
		
		for(int ix=startIndex; ix<lines; ix++)
		{
			Region n = m.getDecoratedLine(ix);
			n.setManaged(true);
			
			// TODO wrapping
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
	
	
	public CaretLocation getCaretLocation(TextPos pos)
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


	public LayoutOp newLayoutOp()
	{
		return new LayoutOp(layout);
	}


	public void setCaretVisible(boolean on)
	{
		// TODO
	}
}
