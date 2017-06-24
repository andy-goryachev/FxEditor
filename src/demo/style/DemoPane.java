// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package demo.style;
import goryachev.fx.CAction;
import goryachev.fx.CPane;
import goryachev.fx.table.FxTable;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;


/**
 * Styling Demo Pane.
 */
public class DemoPane
	extends CPane
{
	public final CAction reloadAction = new CAction(this::reload);
	public final FxTable<Page> table;
	public final CPane detailPane;
	public final SplitPane split;
	
	
	public DemoPane()
	{
		detailPane = new CPane();
		
		table = new FxTable();
		table.addColumn("Component");
		table.setResizePolicyConstrained();
		
		table.getItems().setAll
		(
			new Page("FxEditor", FxEditorDemoPane.class),
			new Page("Buttons", ButtonDemoPane.class),
			new Page("FxTable", TableDemoPane.class),
			new Page("Text Components", TextDemoPane.class)
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
		Page p = table.getSelectedItem();
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