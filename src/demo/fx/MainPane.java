// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package demo.fx;
import goryachev.fx.CAction;
import goryachev.fx.CPane;
import goryachev.fx.table.FxTable;
import demo.fx.pages.ButtonDemoPane;
import demo.fx.pages.DemoLoginPane;
import demo.fx.pages.TableDemoPane;
import demo.fx.pages.TextDemoPane;
import demo.fx.pages.cpane.DemoCPane;
import demo.fx.pages.edit.ClipboardDemoPane;
import demo.fx.pages.edit.FxEditorDemoPane;
import demo.fx.pages.edit.FxEditorEditableDemoPane;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;


/**
 * Main Pane.
 */
public class MainPane
	extends CPane
{
	public final CAction reloadAction = new CAction(this::reload);
	public final FxTable<DemoPage> table;
	public final CPane detailPane;
	public final SplitPane split;
	
	
	public MainPane()
	{
		detailPane = new CPane();
		
		table = new FxTable();
		table.addColumn("Component");
		table.setResizePolicyConstrained();
		
		table.getItems().setAll
		(
			new DemoPage("FxEditor", FxEditorDemoPane.class),
			new DemoPage("FxEditor, Editable", FxEditorEditableDemoPane.class),
			new DemoPage("Clipboard", ClipboardDemoPane.class),
			new DemoPage("Buttons", ButtonDemoPane.class),
			new DemoPage("FxTable", TableDemoPane.class),
			new DemoPage("Text Components", TextDemoPane.class),
			new DemoPage("Login Panel", DemoLoginPane.class),
			new DemoPage("CPane", DemoCPane.class)
		);
		table.selectedItemProperty().addListener((s) -> updateSelection());
		
		split = new SplitPane(table, detailPane);
		split.setOrientation(Orientation.HORIZONTAL);
		
		setCenter(split);
		
		table.selectFirst();
	}
	
	
	protected void updateSelection()
	{
		// TODO store
		Node n;
		DemoPage p = table.getSelectedItem();
		if(p == null)
		{
			n = null;
		}
		else
		{
			n = p.getNode();
		}
		// TODO restore

		detailPane.setCenter(n);
	}
	
	
	protected void reload()
	{
		updateSelection();
	}
}