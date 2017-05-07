// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package demo.style;
import goryachev.fx.CCheckBox;
import goryachev.fx.CPane;
import goryachev.fx.FX;
import goryachev.fx.edit.FxEditor;
import demo.edit.Conf;
import demo.edit.TestFxColorEditorModel;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


/**
 * Text Pane.
 */
public class TextPane
	extends CPane
{
	public TextPane()
	{
		FxEditor ed = new FxEditor(new TestFxColorEditorModel(Conf.LINE_COUNT));
		
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
		addRow(CPane.FILL);
		add(0, r, FX.label("Text Area:", Pos.TOP_RIGHT));
		add(1, r, new TextArea("1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n"));
		r++;
		addRow(CPane.FILL);
		add(0, r, FX.label("FxEditor:", Pos.TOP_RIGHT));
		add(1, r, ed);
		r++;
	}
}
