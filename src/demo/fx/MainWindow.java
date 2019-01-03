// Copyright © 2017-2019 Andy Goryachev <andy@goryachev.com>
package demo.fx;
import goryachev.fx.FX;
import goryachev.fx.FxAction;
import goryachev.fx.FxDump;
import goryachev.fx.FxMenuBar;
import goryachev.fx.FxWindow;


/**
 * Demo Window.
 */
public class MainWindow
	extends FxWindow
{
	public final FxAction prefsAction = new FxAction(this::preferences);
	public final FxAction newWindowAction = new FxAction(this::newWindow);
	public final MainPane pane;
	
	
	public MainWindow()
	{
		super("MainWindow");
		
		pane = new MainPane();

		setTitle("FX Demo / Java " + System.getProperty("java.version"));
		setTop(createMenu());
		setCenter(pane);
		setSize(600, 700);
		
		// debug
		FxDump.attach(this);
	}
	
	
	protected FxMenuBar createMenu()
	{
		FxMenuBar m = new FxMenuBar();
		// app
		m.menu("FxDemo");
		m.item("New Window", newWindowAction);
		m.item("Preferences", prefsAction);
		m.separator();
		m.item("Close Window", closeWindowAction);
		m.item("Exit", FX.exitAction());
		// tools
		m.menu("Tools");
		m.item("Reload", pane.reloadAction);
		// help
		m.menu("Help");
		m.item("About");
		return m;
	}
	
	
	protected void newWindow()
	{
		new MainWindow().open();
	}
	
	
	protected void preferences()
	{
	}
}