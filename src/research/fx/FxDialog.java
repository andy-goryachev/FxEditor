// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package research.fx;
import goryachev.fx.FX;
import goryachev.fx.FxWindow;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Window;


/**
 * Fx Dialog.
 */
public class FxDialog
	extends FxWindow
{
	public FxDialog(String name, Node parent)
	{
		this(name, FX.getParentWindow(parent));
	}
	
	
	public FxDialog(String name, Window parent)
	{
		super(name);
		initOwner(parent);
		initModality(Modality.APPLICATION_MODAL);
	}
	
	
	public void closeOnEscape()
	{
		addEventHandler(KeyEvent.KEY_PRESSED, (ev) -> 
		{
			if(ev.getCode() == KeyCode.ESCAPE)
			{
				close();;
			}
		});
	}
	
	
	// FIX change WindowsFx.restoreWindow instead
	public void center()
	{
		Window win = getOwner();
		if(win != null)
		{
			double w = getWidth();
			double h = getHeight();
			
			double x = (win.getX() + win.getWidth() - w) / 2;
			double y = (win.getY() + win.getHeight() - h) / 2;
			setX(x);
			setY(y);
		}
	}
}
