// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.common.util.SB;
import javafx.scene.Node;
import javafx.scene.shape.PathElement;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;


/**
 * A TextFlow with public methods otherwise inaccessible in JavaFX.
 */
public class CTextFlow
	extends TextFlow
{
	public CTextFlow(Node ... children)
	{
		super(children);
	}


	public CTextFlow()
	{
	}


	public PathElement[] getCaretShape(int index, boolean leading)
	{
		return FxHacks.get().getCaretShape(this, index, leading);
	}


	public PathElement[] getRange(int start, int end)
	{
		return FxHacks.get().getRange(this, start, end);
	}


	/** returns hit info at the specified local coordinates */
	public CHitInfo getHit(double x, double y)
	{
		return FxHacks.get().getHit(this, x, y);
	}
	
	
	public String getText()
	{
		SB sb = new SB();
		for(Node n: getChildrenUnmodifiable())
		{
			if(n instanceof Text)
			{
				sb.append(((Text)n).getText());
			}
		}
		return sb.toString();
	}
}
