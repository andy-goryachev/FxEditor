// Copyright © 2017-2018 Andy Goryachev <andy@goryachev.com>
package demo.fx.pages.edit;
import goryachev.fx.CPane;
import goryachev.fx.CssStyle;
import goryachev.fx.FX;
import goryachev.fx.FxCtl;
import goryachev.fx.edit.FxEditor;
import goryachev.fx.edit.SimpleStyledTextModel;
import goryachev.fx.edit.TStyle;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;


/**
 * FxEditor with SimpleStyledTextModel Demo Pane.
 */
public class FxEditorStyledModelDemoPane
	extends CPane
{
	public static CssStyle EDITOR = new CssStyle("FxEditorStyledModelDemoPane_EDITOR");
	public final SimpleStyledTextModel model;
	
	
	public FxEditorStyledModelDemoPane()
	{
		FX.style(this, EDITOR);
		
		model = new SimpleStyledTextModel();
		populate();
			
		FxEditor ed = new FxEditor(model);
		ed.setContentPadding(new Insets(2, 5, 2, 5));
		ed.setMultipleSelectionEnabled(true);
		ed.setShowLineNumbers(true);
		
		setTop(label("Styled Text"));
		setCenter(ed);
	}
	
	
	protected Label label(String text)
	{
		return FX.label(text, FxCtl.BOLD, new Insets(2, 7, 2, 7));
	}
	
	
	protected void populate()
	{
		model.add(null, "Normal");
		model.add(new TStyle().bold(), "Bold");
		model.add(new TStyle().italic(), "Italic");
		model.add(new TStyle().underline(), "Underline");
		model.add(new TStyle().bold().italic(), "Bold Italic");
		model.add(new TStyle().bold().italic().underline(), "Bold Italic Underline");
		model.add(new TStyle().strikeThrough(), "Strike through");
		model.add
		(
			new TStyle().underline(), "Underline",
			null, " ",
			new TStyle().foreground(Color.RED), "Red Foreground ",
			new TStyle().background(Color.GREEN), "Green Background"
		);
		// TODO font size
		model.add(new TStyle().style(""), "LARGE");
	}
}
