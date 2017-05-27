// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package demo.style;
import goryachev.fx.CPane;
import goryachev.fx.edit.FxEditor;
import demo.edit.Conf;
import demo.edit.TestFxColorEditorModel;
import javafx.geometry.Insets;


/**
 * FxEditor Demo Pane.
 */
public class FxEditorDemoPane
	extends CPane
{
	public final FxEditor ed;
	
	
	public FxEditorDemoPane()
	{
		ed = new FxEditor(new TestFxColorEditorModel(Conf.LINE_COUNT));
		ed.setContentPadding(new Insets(2, 5, 2, 5));
		
		setCenter(ed);
	}
}
