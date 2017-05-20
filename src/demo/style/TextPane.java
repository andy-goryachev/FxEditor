// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package demo.style;
import goryachev.fx.CPane;
import goryachev.fx.FX;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


/**
 * Text Components Pane.
 */
public class TextPane
	extends CPane
{
	public TextPane()
	{
		// layout
		setPadding(10);
		setGaps(10, 5);
		addColumns
		(
			CPane.PREF,
			CPane.FILL
		);
		int r = 0;
		add(0, r, FX.label("Text Field:", Pos.TOP_RIGHT));
		add(1, r, new TextField("sample text"));
		r++;
		addRow(CPane.PREF);
		add(0, r, FX.label("Text Area (PREF):", Pos.TOP_RIGHT));
		add(1, r, new TextArea("1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n"));
		r++;
		addRow(CPane.FILL);
		add(0, r, FX.label("Text Area (FILL):", Pos.TOP_RIGHT));
		add(1, r, new TextArea("1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n"));
		r++;
	}
}
