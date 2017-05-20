// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package demo.style;
import goryachev.fx.CPane;
import goryachev.fx.table.FxTable;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;


/**
 * Styling Demo Pane.
 */
public class StylingDemoPane
	extends CPane
{
	public final FxTable<DemoPage> table;
	public final CPane detailPane;
	public final SplitPane split;
	
	
	public StylingDemoPane()
	{
		detailPane = new CPane();
		
		table = new FxTable();
		table.addColumn("Component");
		table.setResizePolicyConstrained();
		
		table.getItems().setAll
		(
			new DemoPage("Buttons", ButtonPane.class),
			new DemoPage("FxEditor", FxEditorPane.class),
			new DemoPage("Text Components", TextPane.class)
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
}