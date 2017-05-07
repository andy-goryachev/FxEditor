// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package demo.style;
import goryachev.fx.CAction;
import goryachev.fx.CMenu;
import goryachev.fx.CMenuBar;
import goryachev.fx.FX;
import goryachev.fx.FxDump;
import goryachev.fx.FxWindow;


/**
 * Demo Window.
 */
public class StylingDemoMainWindow
	extends FxWindow
{
	public final CAction prefsAction = new CAction(this::preferences);
	
	
	public StylingDemoMainWindow()
	{
		super("MainWindow");

		setTitle("FxEditor Demo");
		setTop(createMenu());
		setCenter(new StylingDemoPane());
		setSize(600, 700);
		
		// debug
		FxDump.attach(this);
	}
	
	
	protected CMenuBar createMenu()
	{
		CMenuBar b = new CMenuBar();
		// file
		CMenu m = b.addMenu("File");
		m.add("Preferences", prefsAction);
		m.separator();
		m.add("Exit", FX.exitAction());
		// help
		m = b.addMenu("Help");
		m.add("About");
		return b;
	}
	
	
	protected void preferences()
	{
	}
}