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
public class DemoStylingMainWindow
	extends FxWindow
{
	public final CAction prefsAction = new CAction(this::preferences);
	public final DemoPane pane;
	
	
	public DemoStylingMainWindow()
	{
		super("MainWindow");
		
		pane = new DemoPane();

		setTitle("FX Demo");
		setTop(createMenu());
		setCenter(pane);
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
		// tools
		m = b.addMenu("Tools");
		m.add("Reload", pane.reloadAction);
		// help
		m = b.addMenu("Help");
		m.add("About");
		return b;
	}
	
	
	protected void preferences()
	{
	}
}