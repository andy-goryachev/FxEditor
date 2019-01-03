// Copyright © 2017-2019 Andy Goryachev <andy@goryachev.com>
package demo.fx.pages;
import goryachev.fx.CPane;
import goryachev.fx.FX;
import goryachev.fx.table.FxTable;
import javafx.geometry.Pos;
import javafx.scene.layout.Region;


/**
 * Colors Demo Pane.
 */
public class ColorsDemoPane
	extends CPane
{
	public ColorsDemoPane()
	{
		FxTable<Entry> table = new FxTable();
		table.addColumn("Color").
			setRenderer((en) -> 
			{
				Region t = new Region();
				t.setBackground(FX.background(FX.rgb(en.rgb)));
				return t;
			}).
			setPrefWidth(200);
		table.addColumn("Hex").
			setConverter((en) -> String.format("%06x", en.rgb)).
			setAlignment(Pos.CENTER_RIGHT).
			setPrefWidth(100);
		table.addColumn("Decimal").
			setConverter((en) -> String.format("%3d,%3d,%3d", (en.rgb >> 16) & 0xff, (en.rgb >> 8) & 0xff, (en.rgb) & 0xff)).
			setAlignment(Pos.CENTER_RIGHT).
			setPrefWidth(100);
		table.setItems
		(
			new Entry[]
			{
				c(71, 219, 0, "sRGB"),
				c(63, 217, 0, "RGB"),
				c(86, 215, 43, "natural"),
				c(0x1990b8, "blue"),
				c(0x2f9c0a, "green"),
				c(0xc92c2c, "red"),
				c(0xa67f59, "brown"),
				c(253, 226, 135, "yellow"),
				c(192, 246, 173, "green")
			}
		);
		
		setCenter(table);
	}
	
	
	protected static Entry c(int r, int g, int b, String text)
	{
		Entry en = new Entry();
		en.rgb = ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
		en.text = text;
		return en;
	}
	
	
	protected static Entry c(int rgb, String text)
	{
		Entry en = new Entry();
		en.rgb = rgb;
		en.text = text;
		return en;
	}
	
	
	//
	
	
	protected static class Entry
	{
		public int rgb;
		public String text;
	}
}
