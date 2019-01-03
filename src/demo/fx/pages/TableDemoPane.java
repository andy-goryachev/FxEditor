// Copyright © 2017-2019 Andy Goryachev <andy@goryachev.com>
package demo.fx.pages;
import goryachev.common.util.CKit;
import goryachev.fx.CPane;
import goryachev.fx.table.FxTable;


/**
 * FxTable Demo Pane.
 */
public class TableDemoPane
	extends CPane
{
	public TableDemoPane()
	{
		FxTable table = new FxTable();
		table.addColumn("C1");
		table.addColumn("C2");
		table.addColumn("C3");
		table.addColumn("C4");
		table.addColumn("C5");
		table.addColumn("C6");
		table.addColumn("C7");
		table.setItems(CKit.asList("R1", "R2", "R3", "R4", "R5", "R6", "R7", "R8", "R9", "R10", "R11", "R12", "R13", "R14", "R15", "R16", "R17", "R18", "R19", "R20"));
		
		FxTable tableFit = new FxTable();
		tableFit.addColumn("C1");
		tableFit.addColumn("C2");
		tableFit.addColumn("C3");
		tableFit.addColumn("C4");
		tableFit.addColumn("C5");
		tableFit.addColumn("C6");
		tableFit.addColumn("C7");
		tableFit.setResizePolicyConstrained();
		tableFit.setItems(CKit.asList("R1", "R2", "R3", "R4", "R5", "R6", "R7", "R8", "R9", "R10", "R11", "R12", "R13", "R14", "R15", "R16", "R17", "R18", "R19", "R20"));
		tableFit.getSelectionModel().setCellSelectionEnabled(true);
		
		addColumn(FILL);
		addRows(FILL, FILL);
		add(0, 0, table);
		add(0, 1, tableFit);
	}
}
