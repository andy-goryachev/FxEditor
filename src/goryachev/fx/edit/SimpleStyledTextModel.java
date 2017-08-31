// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;

import javafx.scene.text.Text;

/**
 * Simple Styled Text Model.
 */
public abstract class SimpleStyledTextModel
	extends FxEditorModel
{
	protected abstract TSegments getSegments(int line);
	
	//
	
	public SimpleStyledTextModel()
	{
	}


	public String getPlainText(int line)
	{
		TSegments ss = getSegments(line);
		return ss.getPlainText();
	}


	public LineBox getDecoratedLine(int line)
	{
		LineBox b = new LineBox();
		for(TSegment s: getSegments(line))
		{
			b.addText(constructText(s));
		}
		return b;
	}


	protected Text constructText(TSegment seg)
	{
		Text t = new Text(seg.getText());
		TStyle s = seg.getStyle();
		
		if(s.isBold())
		{
			// TODO
		}
		
		String style = s.getStyle(); 
		if(style != null)
		{
			t.getStyleClass().add(style);
		}
		
		return t;
	}
}
