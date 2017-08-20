// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package demo.edit;
import goryachev.fx.CAction;
import goryachev.fx.CMenu;
import goryachev.fx.CMenuBar;
import goryachev.fx.FX;
import goryachev.fx.FxDump;
import goryachev.fx.FxWindow;
import goryachev.fx.edit.FxEditor;
import goryachev.fx.edit.FxEditorModel;
import demo.fx.pages.edit.TestFxColorEditorModel;


/**
 * Demo Window.
 */
public class MainWindow
	extends FxWindow
{
	public final CAction prefsAction = new CAction(this::preferences);
	public final MainPane mainPane;
	
	
	public MainWindow()
	{
		super("MainWindow");

		FxEditorModel m = new TestFxColorEditorModel(2_000_000_000);
		
		mainPane = new MainPane(m);
				
		setTitle("FxEditor");
		setTop(createMenu());
		setCenter(mainPane);
		setSize(600, 700);
		
		// props
		bind("WRAP_TEXT", editor().wrapTextProperty());
		bind("SHOW_LINE_NUMBERS", editor().showLineNumbersProperty());
		
		// debug
		FxDump.attach(this);
	}
	
	
	protected FxEditor editor()
	{
		return mainPane.editor;
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
		m.add("Undo");
		m.add("Redo");
		m.separator();
		m.add("Cut");
		m.add("Copy", editor().copyAction);
		m.add("Paste");
		m.separator();
		m.add("Select All", editor().selectAllAction);
		m.add("Select Line");
		m.add("Split Selection into Lines");
		m.separator();
		m.add("Indent");
		m.add("Unindent");
		m.add("Duplicate");
		m.add("Delete Line");
		m.add("Move Line Up");
		m.add("Move Line Down");
		// find
		m = b.addMenu("Find");
		m.add("Find");
		m.add("Regex");
		m.add("Replace");
		m.separator();
		m.add("Find Next");
		m.add("Find Previous");
		m.add("Find and Select");
		// view
		m = b.addMenu("View");
		m.add("Show Line Numbers", editor().showLineNumbersProperty());
		m.add("Wrap Text", editor().wrapTextProperty());
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