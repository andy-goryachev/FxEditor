// Copyright © 2017-2019 Andy Goryachev <andy@goryachev.com>
package demo.fx;
import goryachev.common.util.D;
import goryachev.fx.CPane;
import goryachev.fx.FX;
import goryachev.fx.FxAction;
import goryachev.fx.FxInt;
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
	public final FxAction reloadAction = new FxAction(this::reload);
	public final FxTable<DemoPage> table;
	public final CPane detailPane;
	public final SplitPane split;
	public final FxInt selectedPage = new FxInt();
	
	
	public MainPane()
	{
		detailPane = new CPane();
		
		table = new FxTable();
		table.addColumn("Example");
		table.setAutoResizeMode(true);
		
		table.getItems().setAll(AllPages.get());
		table.selectedItemProperty().addListener((s,p,c) -> updateSelection());
		table.selectedIndexProperty().addListener((s,p,c) -> selectedPage.set(c));
		
		split = new SplitPane(table, detailPane);
		split.setOrientation(Orientation.HORIZONTAL);
		FX.preventSplitPaneResizing(table);
		
		setCenter(split);
		
		table.selectFirst();
		
		FX.bind(this, "selectedPage", selectedPage);
		FX.setOnSettingsLoaded(this, this::restoreSelection);
	}
	
	
	protected void restoreSelection()
	{
		int ix = selectedPage.get();
		table.selectRow(ix);
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