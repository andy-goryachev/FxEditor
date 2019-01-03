// Copyright © 2017-2019 Andy Goryachev <andy@goryachev.com>
package demo.edit;
import goryachev.fx.FX;
import goryachev.fx.FxAction;
import goryachev.fx.FxDump;
import goryachev.fx.FxMenuBar;
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
	public final FxAction prefsAction = new FxAction(this::preferences);
	public final MainPane mainPane;
	protected FxEditorModel model;
	
	
	public MainWindow(FxEditorModel m)
	{
		super("MainWindow");

		if(m == null)
		{
			m = new TestFxColorEditorModel(2_000_000_000);
		}
		this.model = m;
		mainPane = new MainPane(model);
				
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
	
	
	protected FxMenuBar createMenu()
	{
		FxMenuBar m = new FxMenuBar();
		// file
		m.menu("File");
		m.item("Preferences", prefsAction);
		m.separator();
		m.item("New Window, Same Model", new FxAction(this::newWindow));
		m.separator();
		m.item("Exit", FX.exitAction());
		// edit
		m.menu("Edit");
		m.item("Undo");
		m.item("Redo");
		m.separator();
		m.item("Cut");
		m.item("Copy", editor().copyAction);
		m.item("Paste");
		m.separator();
		m.item("Select All", editor().selectAllAction);
		m.item("Select Line");
		m.item("Split Selection into Lines");
		m.separator();
		m.item("Indent");
		m.item("Unindent");
		m.item("Duplicate");
		m.item("Delete Line");
		m.item("Move Line Up");
		m.item("Move Line Down");
		// find
		m.menu("Find");
		m.item("Find");
		m.item("Regex");
		m.item("Replace");
		m.separator();
		m.item("Find Next");
		m.item("Find Previous");
		m.item("Find and Select");
		// view
		m.menu("View");
		m.item("Show Line Numbers", editor().showLineNumbersProperty());
		m.item("Wrap Text", editor().wrapTextProperty());
		// help
		m.menu("Help");
		m.item("About");
		return m;
	}
	
	
	protected void preferences()
	{
		new PreferencesDialog(this).open();
	}
	
	
	protected void newWindow()
	{
		new MainWindow(model).open();
	}
}