// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.CMap;
import goryachev.common.util.Log;
import java.io.StringWriter;
import java.io.Writer;
import java.util.function.Consumer;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.Region;


/**
 * FxEditor Model Base Class.
 */
public abstract class FxEditorModel
{
	public static class LoadInfo
	{
		public final double progress;
		public final int lineCount;
		public final long startTime;
		public final long currentTime;
		
		
		public LoadInfo(double progress, int lineCount, long startTime, long currentTime)
		{
			this.progress = progress;
			this.lineCount = lineCount;
			this.startTime = startTime;
			this.currentTime = currentTime;
		}
	}
	
	//
	
	/** returns a load info structure that gives us information about the loading process and estimates of line count/file size, 
	 * or null if the data has been loaded. */ 
	public abstract LoadInfo getLoadInfo();
	
	/** returns a known line count.  if the model is still loading, returns the best estimate of the number of lines. */
	public abstract int getLineCount();
	
	/** returns plain text at the specified line, or null if unknown */
	public abstract String getPlainText(int line);
	
	/** 
	 * returns a non-null Region containing Text, TextFlow, or any other Nodes representing a line.
	 * I am not sure this should be a part of the editor model, because the presentation should be controlled by the editor ui.
	 * What this method needs to return is a list/array of segments encapsulating text, text style and colors.
	 * Another consideration is support for arbitrary Nodes such as images (tables and so on) - and for those we need to 
	 * have a ui component.
	 */
	public abstract Region getDecoratedLine(int line);
	
	//

	private CList<FxEditor> listeners = new CList<>();
	private static FxEditorModel empty;
	
	
	public FxEditorModel()
	{
	}
	
	
	public void addListener(FxEditor li)
	{
		listeners.add(li);
	}
	
	
	public void removeListener(FxEditor li)
	{
		listeners.remove(li);
	}
	
	
	public static FxEditorModel getEmptyModel()
	{
		if(empty == null)
		{
			empty = new FxEditorModel()
			{
				public LoadInfo getLoadInfo()
				{
					long t = System.currentTimeMillis();
					return new LoadInfo(1.0, 0, t, t); 
				}
				public int getLineCount() { return 0; }
				public String getPlainText(int line) { return null; }
				public Region getDecoratedLine(int line) { return null; }
			};
		}
		return empty;
	}
	
	
	public void fireAllChanged()
	{
		fireEvent((li) -> li.eventAllChanged());
	}
	
	
	protected void fireEvent(Consumer<FxEditor> f)
	{
		for(FxEditor li: listeners)
		{
			f.accept(li);
		}
	}
	
	
	/** copies every data format the model contains to the clipboard */
	public void copy(EditorSelection sel)
	{
		sel = sel.getNormalizedSelection();
		
		CMap<DataFormat,Object> m = new CMap();
		String s = copyPlainText(sel);
		if(s != null)
		{
			m.put(DataFormat.PLAIN_TEXT, s);
		}
		
		Clipboard c = Clipboard.getSystemClipboard();
		c.setContent(m);
	}
	
	
	public String copyPlainText(EditorSelection sel)
	{
		try
		{
			StringWriter wr = new StringWriter();
			getPlainText(sel, wr);
			return wr.toString();
		}
		catch(Exception e)
		{
			// TODO communicate error to the ui
			Log.ex(e);
			return null;
		}
	}


	/** plain text copy, expecting normalized selection ranges */
	public void getPlainText(EditorSelection sel, Writer wr) throws Exception
	{
		sel = sel.getNormalizedSelection();
		for(SelectionSegment s: sel.getSegments())
		{
			CKit.checkCancelled();
			writePlainText(s, wr);
		}
	}


	protected void writePlainText(SelectionSegment seg, Writer wr) throws Exception
	{
		Marker m0 = seg.getTop();
		Marker m1 = seg.getBottom();
		
		int first = m0.getLine();
		int last = m1.getLine();
		
		for(int i=first; i<=last; i++)
		{
			CKit.checkCancelled();
			String s = getPlainText(i);
			
			if(i == first)
			{
				if(i == last)
				{
					s = s.substring(m0.getLineOffset(), m1.getLineOffset() - 1);
				}
				else
				{
					s = s.substring(m0.getLineOffset());
				}
			}
			else
			{
				wr.write("\n");
				
				if(i == last)
				{
					s = s.substring(0, m1.getLineOffset() - 1);
				}
			}
			
			wr.write(s);
		}
	}
}
