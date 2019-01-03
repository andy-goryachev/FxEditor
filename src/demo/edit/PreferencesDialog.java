// Copyright © 2017-2019 Andy Goryachev <andy@goryachev.com>
package demo.edit;
import goryachev.fx.CPane;
import goryachev.fx.FX;
import goryachev.fx.FxCheckBox;
import goryachev.fx.edit.FxEditor;
import demo.fx.pages.edit.TestFxColorEditorModel;
import javafx.geometry.Pos;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import research.fx.FxDialog;


/**
 * Preferences Dialog.
 */
public class PreferencesDialog
	extends FxDialog
{
	public PreferencesDialog(Window parent)
	{
		super("PreferencesDialog", parent);
		setTitle("Styling Demo");
		setSize(500, 400);
		center();
		
		FxEditor ed = new FxEditor(new TestFxColorEditorModel(Conf.LINE_COUNT));
		
		// layout
		CPane p = new CPane();
		p.setPadding(10);
		p.setGaps(10, 5);
		p.addColumns
		(
			CPane.PREF,
			CPane.FILL
		);
		int r = 0;
		p.add(0, r, FX.label("Check Boxes:", Pos.TOP_RIGHT));
		p.add(1, r, new FxCheckBox("selected", true));
		r++;
		p.add(1, r, new FxCheckBox("deselected", false));
		r++;
		p.add(1, r, cb("selected, disabled", true, true));
		r++;
		p.add(1, r, cb("deselected, disabled", false, true));
		r++;
		p.add(0, r, FX.label("Radio Buttons:", Pos.TOP_RIGHT));
		p.add(1, r, rb("selected", true, false));
		r++;
		p.add(1, r, rb("deselected", false, false));
		r++;
		p.add(1, r, rb("selected, disabled", true, true));
		r++;
		p.add(1, r, rb("deselected, disabled", false, true));
		r++;
		p.add(0, r, FX.label("Text Field:", Pos.TOP_RIGHT));
		p.add(1, r, new TextField());
		r++;
		p.addRow(CPane.FILL);
		p.add(0, r, FX.label("Text Area:", Pos.TOP_RIGHT));
		p.add(1, r, new TextArea("1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n"));
		r++;
		p.addRow(CPane.FILL);
		p.add(0, r, FX.label("FxEditor:", Pos.TOP_RIGHT));
		p.add(1, r, ed);
		r++;
		setCenter(p);
		
		closeOnEscape();
	}
	
	
	protected FxCheckBox cb(String text, boolean selected, boolean disabled)
	{
		FxCheckBox c = new FxCheckBox(text, selected);
		c.setDisable(disabled);
		return c;
	}
	
	
	protected RadioButton rb(String text, boolean selected, boolean disabled)
	{
		RadioButton b = new RadioButton(text);
		b.setSelected(selected);
		b.setDisable(disabled);
		return b;
	}
}
