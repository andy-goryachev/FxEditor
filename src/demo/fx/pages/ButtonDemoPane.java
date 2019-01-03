// Copyright © 2017-2019 Andy Goryachev <andy@goryachev.com>
package demo.fx.pages;
import goryachev.fx.CPane;
import goryachev.fx.FX;
import goryachev.fx.FxButton;
import goryachev.fx.FxCheckBox;
import goryachev.fx.HPane;
import javafx.geometry.Pos;
import javafx.scene.control.RadioButton;


/**
 * ButtonPane.
 */
public class ButtonDemoPane
	extends CPane
{
	public ButtonDemoPane()
	{
		HPane bp = new HPane(10);
		bp.add(button("Active", false));
		bp.add(button("Disabled", true));
		
		setPadding(10);
		setGaps(10, 5);
		addColumns
		(
			CPane.PREF,
			CPane.FILL
		);
		
		int r = 0;
		add(0, r, FX.label("Check Boxes:", Pos.TOP_RIGHT));
		add(1, r, new FxCheckBox("selected", true));
		r++;
		add(1, r, new FxCheckBox("deselected", false));
		r++;
		add(1, r, cb("selected, disabled", true, true));
		r++;
		add(1, r, cb("deselected, disabled", false, true));
		r++;
		add(0, r, FX.label("Radio Buttons:", Pos.TOP_RIGHT));
		add(1, r, rb("selected", true, false));
		r++;
		add(1, r, rb("deselected", false, false));
		r++;
		add(1, r, rb("selected, disabled", true, true));
		r++;
		add(1, r, rb("deselected, disabled", false, true));
		r++;
		add(0, r, FX.label("Buttons:", Pos.TOP_RIGHT));
		add(1, r, bp);
		r++;
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
	
	
	protected FxButton button(String text, boolean disabled)
	{
		FxButton b = new FxButton(text);
		b.setDisable(disabled);
		return b;
	}
}
