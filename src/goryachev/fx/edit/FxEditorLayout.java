// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.common.util.CList;
import goryachev.common.util.CMap;
import goryachev.fx.edit.internal.CaretLocation;
import goryachev.fx.edit.internal.EditorTools;
import goryachev.fx.edit.internal.Markers;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.PathElement;


/**
 * FxEditor Layout.
 */
public class FxEditorLayout
{
	private final FxEditor editor;
	private final int topLine;
	private final CList<LineBox> lines = new CList<>();
	private CMap<Integer,LineBox> newLines;
	

	public FxEditorLayout(FxEditor ed, int topLine)
	{
		this.editor = ed;
		this.topLine = topLine;
	}
	
	
	/** returns text position at the screen coordinates, or null */
	public Marker getTextPos(double screenx, double screeny, Markers markers)
	{
		for(LineBox line: lines)
		{
			Region box = line.getBox();
			Point2D p = box.screenToLocal(screenx, screeny);
			Insets pad = box.getPadding();
			double x = p.getX() - pad.getLeft();
			double y = p.getY() - pad.getTop();
			
			if(y > 0)
			{
				if(y < box.getHeight())
				{
					if(box instanceof CTextFlow)
					{
						CHitInfo hit = ((CTextFlow)box).getHit(x, y);
						if(hit != null)
						{
							return markers.newMarker(line.getLineNumber(), hit.getCharIndex(), hit.isLeading());
						}
					}
				}
			}
			else
			{
				break;
			}
		}
		return null;
	}
	
	
	public LineBox getLineBox(int line)
	{
		if(newLines != null)
		{
			LineBox b = newLines.get(line);
			if(b != null)
			{
				return b;
			}
		}
		
		line -= topLine;
		if((line >= 0) && (line < lines.size()))
		{
			return lines.get(line);
		}
		return null;
	}
	
	
	public CaretLocation getCaretLocation(Region parent, Marker pos)
	{
		if(pos != null)
		{
			LineBox b = getLineBox(pos.getLine());
			if(b != null)
			{
				Region box = b.getBox();
				if(box instanceof CTextFlow)
				{
					PathElement[] es = ((CTextFlow)box).getCaretShape(pos.getCharIndex(), pos.isLeading());
					if(es != null)
					{
						return EditorTools.translateCaretLocation(parent, box, es);
					}
				}
			}
		}
		return null;
	}
	

	protected void addLineBox(LineBox b)
	{
		lines.add(b);
	}
	
	
	public void removeFrom(Pane p)
	{
		ObservableList<Node> cs = p.getChildren();
		for(LineBox b: lines)
		{
			cs.remove(b.getBox());
		}
		
		if(newLines != null)
		{
			for(LineBox b: newLines.values())
			{
				cs.remove(b.getBox());
			}
		}
	}


	public int startLine()
	{
		return topLine;
	}


	public int getVisibleLineCount()
	{
		return lines.size();
	}
	
	
	public double getLineHeight(int ix)
	{
		LineBox b = (newLines == null ? null : newLines.get(ix));
		if(b == null)
		{
			b = getLineBox(ix);
			if(b == null)
			{
				Region r = editor.getTextModel().getDecoratedLine(ix);
				b = new LineBox(ix, r);
				
				double h = editor.vflow.addAndComputePreferredHeight(r);
				b.setHeight(h);
			}
			
			if(newLines == null)
			{
				newLines = new CMap();
			}
			newLines.put(ix, b);
		}
			
		return b.getHeight();
	}
}