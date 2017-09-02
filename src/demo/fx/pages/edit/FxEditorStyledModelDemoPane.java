// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package demo.fx.pages.edit;
import goryachev.fx.CPane;
import goryachev.fx.CssStyle;
import goryachev.fx.FX;
import goryachev.fx.FxCtl;
import goryachev.fx.edit.FxEditor;
import goryachev.fx.edit.SimpleStyledTextModel;
import goryachev.fx.edit.TSegments;
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
		TSegments ss;
		TStyle s;
		
		ss = new TSegments();
		s = new TStyle();
		ss.add(s, "Normal");
		model.add(ss);
		
		ss = new TSegments();
		s = new TStyle();
		s.setBold(true);
		ss.add(s, "Bold");
		model.add(ss);
		
		ss = new TSegments();
		s = new TStyle();
		s.setItalic(true);
		ss.add(s, "Italic");
		model.add(ss);
		
		ss = new TSegments();
		s = new TStyle();
		s.setUnderline(true);
		ss.add(s, "Underline");
		model.add(ss);
		
		ss = new TSegments();
		s = new TStyle();
		s.setItalic(true);
		s.setBold(true);
		ss.add(s, "Bold Italic");
		model.add(ss);
		
		ss = new TSegments();
		s = new TStyle();
		s.setItalic(true);
		s.setBold(true);
		s.setUnderline(true);
		ss.add(s, "Bold Italic Underline");
		model.add(ss);
		
		ss = new TSegments();
		ss.add(new TStyle().strikeThrough(), "Strike through");
		model.add(ss);
		
		ss = new TSegments();
		ss.add(new TStyle().underline(), "Underline");
		ss.add(new TStyle(), " ");
		ss.add(new TStyle().foreground(Color.RED), "Red Foreground ");
		ss.add(new TStyle().background(Color.GREEN), "Green Background");
		
		ss = new TSegments();
		ss.add(new TStyle().style(""), "LARGE");
		model.add(ss);
	}
}
