// Copyright © 2016-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.fxeditor;
import goryachev.common.util.CList;
import goryachev.fx.CssStyle;
import goryachev.fx.FX;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.text.HitInfo;
import javafx.util.Duration;


/**
 * A styleable TextArea.
 */
public class StyledTextPane
	extends StackPane
{
	public static final CssStyle CARET = new CssStyle("StyledTextPane_CARET");
	
	protected final CTextFlow textFlow;
	protected final Path caret;
	protected final Timeline caretTimeline;
	protected final ReadOnlyIntegerWrapper selectionIndex = new ReadOnlyIntegerWrapper(-1);


	public StyledTextPane()
	{
		textFlow = new CTextFlow();
		
		caret = new Path();
		FX.style(caret, CARET);
		caret.setManaged(false);
		caret.setStroke(Color.BLACK);
		
		getChildren().add(textFlow);
		
		caretTimeline = new Timeline();
		caretTimeline.setCycleCount(Animation.INDEFINITE);
		// TODO property
		updateBlinkRate(Duration.millis(500));
		
		// FIX allow custom handlers
		new StyledTextPaneMouseController(this);
	}
	
	
	// TODO blinkRate property
	protected void updateBlinkRate(Duration d)
	{
		Duration period = d.multiply(2);
		
		caretTimeline.stop();
		caretTimeline.getKeyFrames().setAll
		(
			new KeyFrame(Duration.ZERO, (ev) -> setCaretVisible(true)),
			new KeyFrame(d, (ev) -> setCaretVisible(false)),
			new KeyFrame(period)
		);
		caretTimeline.play();
	}


	public PathElement[] caretShape(int index, boolean leading)
	{
		return textFlow.caretShape(index, leading);
	}


	public PathElement[] rangeShape(int start, int end)
	{
		return textFlow.rangeShape(start, end);
	}


	/** returns text position at the specified screen coordinates */
	public int getInsertionIndex(double x, double y)
	{
		Point2D p = textFlow.screenToLocal(x, y);
		HitInfo h = textFlow.hitTest(p);
		return (h == null ? -1 : h.getInsertionIndex());
	}

	
	public void setSelection(int ix)
	{
		if(ix < 0)
		{
			caret.getElements().clear();
			setCaretVisible(false);
		}
		else
		{
			if(caret.getParent() == null)
			{
				textFlow.getChildren().add(caret);
			}
			
			PathElement[] es = caretShape(ix, true);
			caret.getElements().setAll(es);
			setCaretVisible(true);
		}
		
		selectionIndex.set(ix);
	}
	
	
	public void setCaretVisible(boolean on)
	{
		caret.setVisible(on);
	}
	
	
	// TODO replace with selection span
	public ReadOnlyIntegerProperty selectionIndexProperty()
	{
		return selectionIndex.getReadOnlyProperty();
	}


	// FIX replace with styled segments
	public void setText(CList<Node> textNodes)
	{
		textFlow.getChildren().setAll(textNodes);
		setSelection(0);
	}


	public String getText()
	{
		return textFlow.getText();
	}
}
