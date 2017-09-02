// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package demo.fx.pages.edit;
import goryachev.common.util.CList;
import goryachev.fx.CssStyle;
import goryachev.fx.FX;
import goryachev.fx.edit.Edit;
import goryachev.fx.edit.FxEditorModel;
import goryachev.fx.edit.LineBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


/**
 * Text editor model backed by an ArrayList of LineSegments.
 */
public class SegmentTextEditorModel
	extends FxEditorModel
{
	public static CssStyle HEADING = new CssStyle("SegmentTextEditorModel_HEADING");
	
	private final CList<LineSegment> lines = new CList();
	
	
	public SegmentTextEditorModel()
	{
	}
	
	
	public LineBox getLineBox(int line)
	{
		LineSegment seg = lines.get(line);
		Text t = toText(seg);
		
		LineBox b = new LineBox();
		b.text().add(t);
		return b;
	}
	
	
	protected Text toText(LineSegment seg)
	{
		Text t = new Text(seg.text);
		
		switch(seg.type)
		{
		case BYTES:
			t.setFill(Color.DARKGREEN);
			break;
		case ERROR:
			t.setFill(Color.MAGENTA);
			break;
		case HEADING:
			t.setFill(Color.BLACK);
			FX.style(t, HEADING);
			break;
		case TEXT:
		default:
			t.setFill(Color.BLACK);
			break;
		}
		return t;
	}


	public LoadInfo getLoadInfo()
	{
		return null; 
	}
	
	
	public Edit edit(Edit ed) throws Exception
	{
		throw new Exception();
	}


	public int getLineCount()
	{
		return lines.size();
	}
	

	public void addSegments(CList<LineSegment> segments)
	{
		lines.addAll(segments);
	}


	public void addSegment(LineSegment seg)
	{
		lines.add(seg);
	}
}
