// Copyright © 2017-2019 Andy Goryachev <andy@goryachev.com>
package demo.fx.pages.edit;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.CMap;
import goryachev.common.util.CSorter;
import goryachev.common.util.D;
import goryachev.common.util.Hex;
import goryachev.fx.CPane;
import goryachev.fx.edit.FxEditor;
import java.nio.ByteBuffer;
import java.util.Set;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.util.Duration;


/**
 * FxEditor Demo Pane.
 */
public class ClipboardDemoPane
	extends CPane
{
	private static final Duration PERIOD = Duration.millis(1000);
	public final FxEditor editor;
	private Timeline timeline;
	private CMap<DataFormat,CList<LineSegment>> prevContent;
	
	
	public ClipboardDemoPane()
	{
		editor = new FxEditor();
		editor.setContentPadding(new Insets(2, 5, 2, 5));
		editor.setMultipleSelectionEnabled(true);
		
		setCenter(editor);
		
		timeline = new Timeline(new KeyFrame(PERIOD, (ev) -> handleClipboard()));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
		
		updateContent(null);
	}
	
	
	protected void handleClipboard()
	{
		Clipboard c = Clipboard.getSystemClipboard();
		CMap<DataFormat,CList<LineSegment>> content = getContent(c);
		if(CKit.notEquals(content, prevContent))
		{
			prevContent = content;
			updateContent(content);
		}
	}


	protected CMap<DataFormat,CList<LineSegment>> getContent(Clipboard clip)
	{
		Set<DataFormat> formats = clip.getContentTypes();
		CMap<DataFormat,CList<LineSegment>> rv = new CMap<>(formats.size());
		for(DataFormat f: formats)
		{
			Object x = clip.getContent(f);
			CList<LineSegment> segments = createSegments(f, x);
			rv.put(f, segments);
		}
		return rv;
	}


	private CList<LineSegment> createSegments(DataFormat f, Object x)
	{
		CList<LineSegment> rv = new CList();
		rv.add(new LineSegment(LineType.HEADING, f.toString()));
		
		if(x instanceof String)
		{
			String[] ss = CKit.split((String)x, "\n");
			for(String s: ss)
			{
				rv.add(new LineSegment(LineType.TEXT, s.trim()));
			}
		}
		else if(x instanceof ByteBuffer)
		{
			ByteBuffer b = (ByteBuffer)x;
			byte[] bytes = new byte[b.remaining()];
			b.get(bytes);
			
			String[] ss = Hex.toHexStringsASCII(bytes);
			for(String s: ss)
			{
				rv.add(new LineSegment(LineType.BYTES, s.trim()));
			}
		}
		else
		{
			rv.add(new LineSegment(LineType.ERROR, "unknown data type: " + (x == null ? "null" : x.getClass())));
		}
		return rv;
	}


	protected void updateContent(CMap<DataFormat,CList<LineSegment>> content)
	{
		SegmentTextEditorModel m = new SegmentTextEditorModel();
		
		if(content != null)
		{
			CList<DataFormat> formats = content.keys();
			CSorter.sort(formats);
			
			for(DataFormat f: formats)
			{
				CList<LineSegment> lines = content.get(f);
				m.addSegments(lines);
			}
		}
		
		editor.setTextModel(m);
		D.print(m.getLineCount());
	}
}
