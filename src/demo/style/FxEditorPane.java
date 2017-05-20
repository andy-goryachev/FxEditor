// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package demo.style;
import goryachev.fx.CPane;
import goryachev.fx.edit.FxEditor;
import demo.edit.Conf;
import demo.edit.TestFxColorEditorModel;


/**
 * FxEditor Demo Pane.
 */
public class FxEditorPane
	extends CPane
{
	public final FxEditor ed;
	
	
	public FxEditorPane()
	{
		ed = new FxEditor(new TestFxColorEditorModel(Conf.LINE_COUNT));
		
		setCenter(ed);
	}
}
