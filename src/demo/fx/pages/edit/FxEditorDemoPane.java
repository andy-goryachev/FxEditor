// Copyright © 2017-2019 Andy Goryachev <andy@goryachev.com>
package demo.fx.pages.edit;
import goryachev.fx.CPane;
import goryachev.fx.CssStyle;
import goryachev.fx.FX;
import goryachev.fx.FxCtl;
import goryachev.fx.edit.FxEditor;
import demo.edit.Conf;
import javafx.geometry.Insets;
import javafx.scene.control.Label;


/**
 * FxEditor Demo Pane.
 */
public class FxEditorDemoPane
	extends CPane
{
	public static CssStyle EDITOR = new CssStyle("FxEditorDemoPane_EDITOR");

	
	public FxEditorDemoPane()
	{
		FX.style(this, EDITOR);
		
		FxEditor ed = new FxEditor(new TestFxColorEditorModel(Conf.LINE_COUNT));
		ed.setContentPadding(new Insets(2, 5, 2, 5));
		ed.setMultipleSelectionEnabled(true);
		ed.setShowLineNumbers(true);
		
		setTop(label("Large Color Model (Read-Only)"));
		setCenter(ed);
	}
	
	
	protected Label label(String text)
	{
		return FX.label(text, FxCtl.BOLD, new Insets(2, 7, 2, 7));
	}
}
