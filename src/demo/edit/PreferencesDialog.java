// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package demo.edit;
import goryachev.fx.CCheckBox;
import goryachev.fx.CPane;
import goryachev.fx.FX;
import javafx.geometry.Pos;
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
		setTitle("Preferences");
		setSize(300, 300);
		center();
				
		CPane p = new CPane();
		p.setPadding(10);
		p.setGaps(10, 5);
		p.addColumns
		(
			CPane.PREF,
			CPane.FILL
		);
		int r = 0;
		p.add(0, r, FX.label("TextField", Pos.CENTER_RIGHT));
		p.add(1, r, new TextField());
		r++;
		p.add(1, r, new CCheckBox("selected checkbox", true));
		r++;
		p.add(1, r, new CCheckBox("deselected checkbox", false));
		r++;
		p.add(1, r, cb("selected disabled checkbox", true, true));
		r++;
		p.add(1, r, cb("deselected disabled checkbox", false, true));
		r++;
		setCenter(p);
		
		closeOnEscape();
	}
	
	
	protected CCheckBox cb(String text, boolean selected, boolean disabled)
	{
		CCheckBox c = new CCheckBox(text, selected);
		c.setDisable(disabled);
		return c;
	}
}
