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
	private final CList<EditorLineBase> lines = new CList<>();
	private CMap<Integer,EditorLineBase> newLines;
	

	public FxEditorLayout(FxEditor ed, int topLine)
	{
		this.editor = ed;
		this.topLine = topLine;
	}
	
	
	/** returns text position at the screen coordinates, or null */
	public Marker getTextPos(double screenx, double screeny, Markers markers)
	{
		for(EditorLineBase line: lines)
		{
			Point2D p = line.screenToLocal(screenx, screeny);
			Insets pad = line.getPadding();
			double x = p.getX() - pad.getLeft();
			double y = p.getY() - pad.getTop();
			
			if(y >= 0)
			{
				if(y < line.getHeight())
				{
					CHitInfo hit = line.getHit(x, y);
					if(hit != null)
					{
						return markers.newMarker(line.getLineNumber(), hit.getCharIndex(), hit.isLeading());
					}
				}
			}
			else
			{
				break;
			}
		}
		
		EditorLineBase line = lines.getLast();
		int len = Math.max(0, line.getTextLength() - 1);
		return markers.newMarker(line.getLineNumber(), len, false);
	}
	
	
	public EditorLineBase getLineBox(int line)
	{
		if(newLines != null)
		{
			EditorLineBase b = newLines.get(line);
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
			EditorLineBase b = getLineBox(pos.getLine());
			if(b != null)
			{
				PathElement[] es = b.getCaretShape(pos);
				if(es != null)
				{
					return EditorTools.translateCaretLocation(parent, b, es);
				}
			}
		}
		return null;
	}
	

	protected void addLineBox(EditorLineBase b)
	{
		lines.add(b);
	}
	
	
	public void removeFrom(Pane p)
	{
		ObservableList<Node> cs = p.getChildren();
		for(EditorLineBase b: lines)
		{
			cs.remove(b);
		}
		
		if(newLines != null)
		{
			for(EditorLineBase b: newLines.values())
			{
				cs.remove(b);
			}
		}
	}


	public int getTopLine()
	{
		return topLine;
	}


	public int getVisibleLineCount()
	{
		return lines.size();
	}
	
	
	public double getLineHeight(int ix)
	{
		EditorLineBase b = (newLines == null ? null : newLines.get(ix));
		if(b == null)
		{
			b = getLineBox(ix);
			if(b == null)
			{
				b = editor.getTextModel().getDecoratedLine(ix);
				b.setLineNumber(ix);
				
				double h = editor.vflow.addAndComputePreferredHeight(b);
				b.setLineHeight(h);
			}
			
			if(newLines == null)
			{
				newLines = new CMap();
			}
			newLines.put(ix, b);
		}
			
		return b.getLineHeight();
	}
}