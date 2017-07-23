// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import javafx.scene.text.Text;


/**
 * Plain Text FxEditor Model.
 */
public abstract class FxPlainEditorModel
	extends FxEditorModel
{
	public FxPlainEditorModel()
	{
	}
	
	
	public EditorLineBase getDecoratedLine(int line)
	{		
		EditorLineText b = new EditorLineText();
		String s = getPlainText(line);
		if(s != null)
		{
			Text tx = new Text(s);
			b.addText(tx);
		}
		return b;
	}
}
