// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package demo.style;
import goryachev.fx.CPane;
import goryachev.fx.FX;
import goryachev.fx.FxCtl;
import goryachev.fx.edit.EditablePlainTextEditorModel;
import goryachev.fx.edit.FxEditor;
import demo.edit.Conf;
import demo.edit.TestFxColorEditorModel;
import javafx.geometry.Insets;
import javafx.scene.control.Label;


/**
 * FxEditor Demo Pane.
 */
public class FxEditorDemoPane
	extends CPane
{
	public FxEditorDemoPane()
	{
		FxEditor ed = new FxEditor(new TestFxColorEditorModel(Conf.LINE_COUNT));
		ed.setContentPadding(new Insets(2, 5, 2, 5));
		
		FxEditor edit = new FxEditor(new EditablePlainTextEditorModel());
		edit.setContentPadding(new Insets(2, 5, 2, 5));
		
		setGaps(10, 5);
		addColumns
		(
			CPane.FILL
		);
		addRows
		(
			CPane.PREF,
			CPane.FILL,
			CPane.PREF,
			CPane.FILL
		);
		int r = 0;
		add(0, r, label("Large Color Model (Read-Only)"));
		r++;
		add(0, r, ed);
		r++;
		add(0, r, label("Editable Plain Text Model"));
		r++;
		add(0, r, edit);
	}
	
	
	protected Label label(String text)
	{
		return FX.label(text, FxCtl.BOLD, new Insets(2, 7, 2, 7));
	}
}
