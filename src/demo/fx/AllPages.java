// Copyright © 2017-2019 Andy Goryachev <andy@goryachev.com>
package demo.fx;
import demo.fx.pages.ButtonDemoPane;
import demo.fx.pages.ColorsDemoPane;
import demo.fx.pages.DemoLoginPane;
import demo.fx.pages.TableDemoPane;
import demo.fx.pages.TextDemoPane;
import demo.fx.pages.cpane.DemoCPane;
import demo.fx.pages.edit.ClipboardDemoPane;
import demo.fx.pages.edit.FxEditorDemoPane;
import demo.fx.pages.edit.FxEditorEditableDemoPane;
import demo.fx.pages.edit.FxEditorStyledModelDemoPane;
import demo.fx.pages.split.SplitPaneDemoPage;


/**
 * All Demo Pages.
 */
public class AllPages
{
	public static DemoPage[] get()
	{
		return new DemoPage[]
		{
			new DemoPage("FxEditor, Editable", FxEditorEditableDemoPane.class),
			new DemoPage("FxEditor, Styled", FxEditorStyledModelDemoPane.class),
			new DemoPage("FxEditor", FxEditorDemoPane.class),
			new DemoPage("Split Pane", SplitPaneDemoPage.class),
			new DemoPage("Clipboard", ClipboardDemoPane.class),
			new DemoPage("Buttons", ButtonDemoPane.class),
			new DemoPage("FxTable", TableDemoPane.class),
			new DemoPage("Text Components", TextDemoPane.class),
			new DemoPage("Login Panel", DemoLoginPane.class),
			new DemoPage("CPane", DemoCPane.class),
			new DemoPage("Colors", ColorsDemoPane.class)
		};
	};
}
