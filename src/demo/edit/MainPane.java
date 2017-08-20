// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package demo.edit;
import goryachev.fx.CInsets;
import goryachev.fx.CPane;
import goryachev.fx.FX;
import goryachev.fx.edit.FindPane;
import goryachev.fx.edit.FxEditor;
import goryachev.fx.edit.FxEditorModel;
import javafx.util.Duration;


/**
 * Main Pane.
 */
public class MainPane
	extends CPane
{
	public final FxEditor editor;

	
	public MainPane(FxEditorModel m)
	{
		editor = new FxEditor(m);
		editor.setContentPadding(new CInsets(2, 4));
		editor.setBlinkRate(Duration.millis(600));
		editor.setMultipleSelectionEnabled(true);
		
		setCenter(editor);
		
		showFindPane();
	}
	
	
	public void showFindPane()
	{
		FindPane p = new FindPane();
		setBottom(p);
		
		FX.later(() -> p.focusSearch());
	}
}
