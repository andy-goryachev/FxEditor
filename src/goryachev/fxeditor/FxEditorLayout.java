// Copyright © 2016-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.fxeditor;
import goryachev.common.util.CList;
import goryachev.common.util.CMap;
import goryachev.fxeditor.internal.CaretLocation;
import goryachev.fxeditor.internal.EditorTools;
import goryachev.fxeditor.internal.Markers;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.PathElement;
import javafx.scene.text.HitInfo;


/**
 * FxEditor Layout.
 */
public class FxEditorLayout
{
	private final FxEditor editor;
	private final int topLine;
	private final CList<LineBox> lines = new CList<>();
	private CMap<Integer,LineBox> newLines;
	private double lineNumbersColumnWidth;
	private double unwrappedWidth;
	private double vwidth;
	private double vheight;
	

	public FxEditorLayout(FxEditor ed, VFlow vf)
	{
		this.editor = ed;
		this.topLine = vf.getTopLine();
		this.vwidth = vf.getWidth();
		this.vheight = vf.getHeight();
	}
	
	
	public String toString()
	{
		return "FxEditorLayout[" + topLine + "-" + (topLine + getVisibleLineCount()) + "]";
	}
	
	
	/** returns true if the layout is still valid */
	public boolean isValid(VFlow vf)
	{
		return 
			(vf.getWidth() == vwidth) &&
			(vf.getHeight() == vheight) &&
			(vf.getTopLine() == topLine);
	}
	
	
	/** returns text position at the screen coordinates, or null */
	public Marker getTextPos(double screenx, double screeny, Markers markers)
	{
		for(LineBox line: lines)
		{
			Region box = line.getCenter();
			Point2D p = box.screenToLocal(screenx, screeny);
			Insets pad = box.getPadding();
			double x = p.getX() - pad.getLeft();
			double y = p.getY() - pad.getTop();
		
			if(y < 0)
			{
				return markers.newMarker(line.getLineNumber(), 0, true);
			}
			else if(y < box.getHeight())
			{
				if(box instanceof CTextFlow)
				{
					HitInfo hit =  ((CTextFlow)box).hitTest(x, y);
					if(hit != null)
					{
						return markers.newMarker(line.getLineNumber(), hit.getCharIndex(), hit.isLeading());
					}
				}
			}
		}
		
		LineBox line = lines.getLast();
		if(line == null)
		{
			return Marker.ZERO;
		}
		
		Region box = line.getCenter();
		int len = 0;
		if(box instanceof CTextFlow)
		{
			len = Math.max(0, ((CTextFlow)box).getText().length() - 1);
		}
		return markers.newMarker(line.getLineNumber(), len, false);
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
		
		int ix = line - topLine;
		if((ix >= 0) && (ix < lines.size()))
		{
			return lines.get(ix);
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
				Region box = b.getCenter();
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
			cs.remove(b.getCenter());
			
			Node ln = b.getLineNumberComponentRaw();
			if(ln != null)
			{
				cs.remove(ln);
			}
		}
		
		if(newLines != null)
		{
			for(LineBox b: newLines.values())
			{
				cs.remove(b.getCenter());
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
		LineBox b = (newLines == null ? null : newLines.get(ix));
		if(b == null)
		{
			b = getLineBox(ix);
			if(b == null)
			{
				b = editor.getModel().getLineBox(ix);
				b.setLineNumber(ix);
				
				double h = editor.vflow.addAndComputePreferredHeight(b.getCenter());
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


	public void setLineNumbersColumnWidth(double w)
	{
		lineNumbersColumnWidth = w;
	}
	
	
	public double getLineNumbersColumnWidth()
	{
		return lineNumbersColumnWidth;
	}
	
	
	public void setUnwrappedWidth(double w)
	{
		unwrappedWidth = w;
	}
	
	
	public double getUnwrappedWidth()
	{
		return unwrappedWidth;
	}
	
	
	public double getTotalWidth()
	{
		return unwrappedWidth + lineNumbersColumnWidth;
	}
}