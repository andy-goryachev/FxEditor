// Copyright © 2017-2019 Andy Goryachev <andy@goryachev.com>
package demo.fx.pages.edit;
import goryachev.fx.CPane;
import goryachev.fx.CssStyle;
import goryachev.fx.FX;
import goryachev.fx.FxCtl;
import goryachev.fx.FxPopupMenu;
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
	protected final FxEditor ed;

	
	public FxEditorDemoPane()
	{
		FX.style(this, EDITOR);
		
		ed = new FxEditor(new TestFxColorEditorModel(Conf.LINE_COUNT));
		ed.setContentPadding(new Insets(2, 5, 2, 5));
		ed.setMultipleSelectionEnabled(true);
		ed.setShowLineNumbers(true);
		
		setTop(label("Large Color Model (Read-Only)"));
		setCenter(ed);
		
		FX.setPopupMenu(ed, this::createPopupMenu);
	}
	
	
	protected FxPopupMenu createPopupMenu()
	{
		FxPopupMenu m = new FxPopupMenu();
		m.checkItem("Wrap Text", ed.wrapTextProperty());
		m.separator();
		m.item("Test");
		return m;
	}
	
	
	protected Label label(String text)
	{
		return FX.label(text, FxCtl.BOLD, new Insets(2, 7, 2, 7));
	}
}
