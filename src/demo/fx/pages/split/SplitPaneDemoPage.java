// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package demo.fx.pages.split;
import goryachev.fx.CPane;
import javafx.scene.control.Label;
import research.fx.CSplitPane;


/**
 * CSplitPane Demo Page.
 */
public class SplitPaneDemoPage
	extends CPane
{
	public SplitPaneDemoPage()
	{
		CSplitPane split = new CSplitPane(true, new Label("one"), new Label("two"), new Label("three"));
		setCenter(split);
	}
}
