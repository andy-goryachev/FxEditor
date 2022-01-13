// Copyright © 2016-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.hacks;
import goryachev.common.util.CList;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.shape.PathElement;
import javafx.scene.text.HitInfo;
import javafx.scene.text.TextFlow;
import javafx.stage.Window;


/**
 * FxHacks for Java 9+.
 */
public class FxHacksJava9
	extends FxHacks
{
	public FxHacksJava9()
	{
	}
	
	
	public PathElement[] getCaretShape(TextFlow t, int index, boolean leading)
	{
		return t.caretShape(index, leading);
	}
	
	
	public PathElement[] getRange(TextFlow t, int start, int end)
	{
		return t.rangeShape(start, end);
	}
	
	
	protected HitInfo getHitInfo(TextFlow t, double x, double y)
	{
		Point2D p = new Point2D(x, y);
		return t.hitTest(p);
	}
	
	
	public CHitInfo getHit(TextFlow t, double x, double y)
	{
		HitInfo h = getHitInfo(t, x, y);
		int ix = h.getCharIndex();
		boolean leading = h.isLeading();
		return new CHitInfo(ix, leading);
	}
	
	
	public int getTextPos(TextFlow t, double x, double y)
	{
		HitInfo h = getHitInfo(t, x, y);
		return h.getInsertionIndex();
	}


	public List<Window> getWindows()
	{
		return new CList(Window.getWindows());
	}
}