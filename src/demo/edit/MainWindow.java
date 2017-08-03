// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package demo.edit;
import goryachev.fx.CAction;
import goryachev.fx.CInsets;
import goryachev.fx.CMenu;
import goryachev.fx.CMenuBar;
import goryachev.fx.FX;
import goryachev.fx.FxDump;
import goryachev.fx.FxWindow;
import goryachev.fx.edit.FxEditor;
import goryachev.fx.edit.FxEditorModel;
import javafx.util.Duration;


/**
 * Demo Window.
 */
public class MainWindow
	extends FxWindow
{
	public final CAction prefsAction = new CAction(this::preferences);
	public final FxEditor editor;
	
	
	public MainWindow()
	{
		super("MainWindow");

		FxEditorModel m = new TestFxColorEditorModel(2_000_000_000);
		
		editor = new FxEditor(m);
		editor.setContentPadding(new CInsets(2, 4));
		editor.setBlinkRate(Duration.millis(600));
		editor.setMultipleSelectionEnabled(true);
		
		setTitle("FxEditor");
		setTop(createMenu());
		setCenter(editor);
		setSize(600, 700);
		
		// props
		bind("WRAP_TEXT", editor.wrapTextProperty());
		bind("SHOW_LINE_NUMBERS", editor.showLineNumbersProperty());
		
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
		// edit
		m = b.addMenu("Edit");
		m.add("Cut");
		m.add("Copy", editor.copyAction);
		m.add("Paste");
		m.separator();
		m.add("Select All", editor.selectAllAction);
		// view
		m = b.addMenu("View");
		m.add("Show Line Numbers", editor.showLineNumbersProperty());
		m.add("Wrap Text", editor.wrapTextProperty());
		// help
		m = b.addMenu("Help");
		m.add("About");
		return b;
	}
	
	
	protected void preferences()
	{
		new PreferencesDialog(this).open();
	}
}