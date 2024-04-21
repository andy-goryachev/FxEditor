// Copyright © 2017-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fxeditor;
import java.io.StringWriter;
import javafx.scene.input.DataFormat;


/**
 * Plain Text Clipboard Handler.
 */
public class PlainTextClipboardHandler
	extends ClipboardHandlerBase
{
	public PlainTextClipboardHandler()
	{
		super(DataFormat.PLAIN_TEXT);
	}


	public Object copy(FxEditorModel model, EditorSelection sel) throws Exception
	{
		StringWriter wr = new StringWriter();
		model.getPlainText(sel, wr);
		String rv = wr.toString();
		return rv;
	}
}
