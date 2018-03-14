// Copyright © 2017-2018 Andy Goryachev <andy@goryachev.com>
package demo.fx;
import goryachev.fx.CAction;
import goryachev.fx.CPane;
import goryachev.fx.table.FxTable;
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
		table.addColumn("Example");
		table.setResizePolicyConstrained();
		
		table.getItems().setAll(AllPages.get());
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