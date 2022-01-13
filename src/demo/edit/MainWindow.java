// Copyright © 2017-2022 Andy Goryachev <andy@goryachev.com>
package demo.edit;
import goryachev.fx.FX;
import goryachev.fx.FxAction;
import goryachev.fx.FxBoolean;
import goryachev.fx.FxDump;
import goryachev.fx.FxMenuBar;
import goryachev.fx.FxPopupMenu;
import goryachev.fx.FxWindow;
import goryachev.fx.internal.LocalSettings;
import goryachev.fxeditor.FxEditor;
import goryachev.fxeditor.FxEditorModel;


/**
 * FxEditor Demo Window.
 */
public class MainWindow
	extends FxWindow
{
	public final FxAction prefsAction = new FxAction(this::preferences);
	public final MainPane mainPane;
	protected FxBoolean tailMode = new FxBoolean();
	protected FxEditorModel model;
	protected static DemoColorEditorModel largeModel;
	protected static DemoGrowingModel growingModel;
	
	
	public MainWindow()
	{
		super("MainWindow");

		mainPane = new MainPane();
				
		setTitle("FxEditor");
		setTop(createMenu());
		setCenter(mainPane);
		setSize(600, 700);
		
		// props
		LocalSettings.get(this).
			add("WORD_WRAP", editor().wordWrapProperty()).
			add("SHOW_LINE_NUMBERS", editor().showLineNumbersProperty()).
			add("TAIL_MODE", tailMode);

		tailMode.addListener((s,p,c) -> updateModel());
		updateModel();
		
		FX.setPopupMenu(editor(), this::createPopupMenu);
		
		// debug
		FxDump.attach(this);
	}
	
	
	protected void updateModel()
	{
		if(tailMode.get())
		{
			if(growingModel == null)
			{
				growingModel = new DemoGrowingModel();
			}
			model = growingModel;
		}
		else
		{
			if(largeModel == null)
			{
				largeModel = new DemoColorEditorModel(2_000_000_000);
			}
			model = largeModel;
		}
		editor().setModel(model);
	}
	
	
	protected FxEditor editor()
	{
		return mainPane.editor;
	}
	
	
	protected FxPopupMenu createPopupMenu()
	{
		FxPopupMenu m = new FxPopupMenu();
		m.item("Cut");
		m.item("Copy");
		m.item("Paste");
		return m;
	}
	
	
	protected FxMenuBar createMenu()
	{
		FxMenuBar m = new FxMenuBar();
		// file
		m.menu("File");
		m.separator();
		m.item("Growing Model", tailMode);
		m.item("New Window, Same Model", new FxAction(this::newWindow));
		m.separator();
		m.item("Preferences", prefsAction);
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
		m.item("Word Wrap", editor().wordWrapProperty());
		// help
		m.menu("Help");
		m.item("About");
		return m;
	}
	
	
	protected void preferences()
	{
	}
	
	
	protected void newWindow()
	{
		MainWindow w = new MainWindow();
		w.tailMode.set(tailMode.get());
		w.open();
	}
}