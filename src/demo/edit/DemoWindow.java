// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package demo.edit;
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
public class DemoWindow
	extends FxWindow
{
	public final FxEditor editor;
	
	
	public DemoWindow()
	{
		super("TestFxEditorWindow");
		
		FxEditorModel m =
			// new TestFxPlainEditorModel();
			new TestFxColorEditorModel();
		
		editor = new FxEditor(m);
		editor.setContentPadding(new CInsets(2, 4));
		editor.setBlinkRate(Duration.millis(600));
		
		setTitle("FxEditor Demo");
		setTop(createMenu());
		setCenter(editor);
		setSize(600, 700);
		
		// props
		bind("WRAP_TEXT", editor.wrapTextProperty());
		
		// debug
		FxDump.attach(this);
	}
	
	
	protected CMenuBar createMenu()
	{
		CMenuBar b = new CMenuBar();
		// file
		CMenu m = b.addMenu("File");
		m.add("Exit", FX.exitAction());
		// view
		m = b.addMenu("View");
		m.add("Wrap Text", editor.wrapTextProperty());
		// help
		m = b.addMenu("Help");
		m.add("About");
		return b;
	}
}