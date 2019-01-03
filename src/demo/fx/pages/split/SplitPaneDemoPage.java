// Copyright © 2017-2019 Andy Goryachev <andy@goryachev.com>
package demo.fx.pages.split;
import goryachev.fx.CPane;
import goryachev.fx.FX;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import research.fx.CSplitPane;


/**
 * CSplitPane Demo Page.
 */
public class SplitPaneDemoPage
	extends CPane
{
	public SplitPaneDemoPage()
	{
		Color c1 = c(71, 219, 0);
		Color c2 = c(63, 217, 0);
		Color c3 = c(86, 215, 43);
		
		Color yel = c(253, 226, 135);
		Color grn = c(192, 246, 173);
		
		CSplitPane vsplit = new CSplitPane(false, t("four", yel), t("five", grn));
		CSplitPane hsplit = new CSplitPane(true, t("one", c1), t("two", c2), t("three", c3), vsplit);
		setCenter(hsplit);
	}
	
	
	protected Color c(int r, int g, int b)
	{
		return Color.rgb(r, g, b);
	}
	
	
	protected Label t(String text, Color c)
	{
		return FX.label(text, FX.background(c), TextAlignment.CENTER);
	}
}
