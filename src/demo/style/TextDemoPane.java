// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package demo.style;
import goryachev.fx.CComboBox;
import goryachev.fx.CPane;
import goryachev.fx.FX;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


/**
 * Text Components Pane.
 */
public class TextDemoPane
	extends CPane
{
	public final TextField textField;
	public final CComboBox comboBox;
	public final CComboBox comboBoxEditable;
	public final TextArea textPref;
	public final TextArea textFill;
	
	
	public TextDemoPane()
	{
		textField = new TextField("sample text");
		
		comboBox = new CComboBox(new String[] { "one", "two", "tree" });
		
		comboBoxEditable = new CComboBox(new String[] { "one", "two", "tree" });
		comboBoxEditable.setEditable(true);
		
		textPref = new TextArea("1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n")
		{
//			{
//				setPrefHeight(USE_COMPUTED_SIZE );
//			}
			
//			protected double computePrefWidth(double h)
//			{
//				double w = super.computePrefWidth(h);
//				D.print(w);
//				return w;
//			}
//			
//			
//			protected double computePrefHeight(double w)
//			{
//				double h = super.computePrefHeight(w);
//				D.print(h);
//				return h;
//			}
		};
		
		textFill = new TextArea();

		// weird behavior: set several lines, click on a line in the middle, press backspace repeatedly
		// what happens next is weird
		//textFill.textProperty().addListener((s) -> D.print(Dump.toPrintable(textFill.getText())));
		//textFill.setText("1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n");
		
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
		add(1, r, textField);
		r++;
		add(0, r, FX.label("Combo Box:", Pos.TOP_RIGHT));
		add(1, r, comboBox);
		r++;
		add(0, r, FX.label("Combo Box (editable):", Pos.TOP_RIGHT));
		add(1, r, comboBoxEditable);
		r++;
		addRow(CPane.PREF);
		add(0, r, FX.label("Text Area (PREF):", Pos.TOP_RIGHT));
		add(1, r, textPref);
		r++;
		addRow(CPane.FILL);
		add(0, r, FX.label("Text Area (FILL):", Pos.TOP_RIGHT));
		add(1, r, textFill);
		r++;
	}
}
