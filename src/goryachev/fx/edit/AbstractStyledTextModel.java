// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.common.util.SB;
import goryachev.fx.FX;
import goryachev.fx.FxCtl;
import goryachev.fx.internal.CssTools;
import javafx.scene.text.Text;


/**
 * Styled Text Model Base Class.
 */
public abstract class AbstractStyledTextModel
	extends FxEditorModel
{
	protected abstract TSegments getSegments(int line);
	
	//
	
	public AbstractStyledTextModel()
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
		TSegments ss = getSegments(line);
		if(ss != null)
		{
			for(TSegment s: ss)
			{
				b.addText(constructText(s));
			}
		}
		return b;
	}


	protected Text constructText(TSegment seg)
	{
		Text t = new Text(seg.getText());
		TStyle st = seg.getStyle();
		
		String css = createCss(st);
		if(css != null)
		{
			t.setStyle(css);
		}
		
		String style = st.getStyle(); 
		if(style != null)
		{
			t.getStyleClass().add(style);
		}
		
		return t;
	}

	
	protected String createCss(TStyle s)
	{
		SB sb = new SB();
		
		if(s.isBold())
		{
			sb.append("-fx-font-weight:bold;");
		}
		
		if(s.isItalic())
		{
			sb.append("-fx-font-style:italic;");
		}
		
		if(s.isStrikeThrough())
		{
			sb.append("-fx-strikethrough:true;");
		}
		
		// these are not easily supported in javafx
//		if(s.isSubScript())
//		{
//			// TODO use scaling + border? 
//		}
//		if(s.isSuperScript())
//		{
//			// TODO use scaling + border? 
//		}
		
		if(s.isUnderline())
		{
			sb.append("-fx-underline:true;");
		}
		
		if(s.getForeground() != null)
		{
			sb.append("-fx-fill:").append(CssTools.toColor(s.getForeground())).append(";");
		}
		
//		if(s.getBackground() != null)
//		{
//			// TODO perhaps add a shape in the shape of text run
//			sb.append("-fx-background-color:").append(CssTools.toColor(s.getBackground())).append(";");
//		}

		return sb.toString();
	}
}
