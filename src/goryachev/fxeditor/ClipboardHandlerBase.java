// Copyright © 2017-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.fxeditor;
import javafx.scene.input.DataFormat;


/**
 * Clipboard Handler Base.
 */
public abstract class ClipboardHandlerBase
{
	public abstract Object copy(FxEditorModel model, EditorSelection sel) throws Exception;
	
	//
	
	private final DataFormat format;
	
	
	public ClipboardHandlerBase(DataFormat format)
	{
		this.format = format;
	}
	
	
	public DataFormat getFormat()
	{
		return format;
	}
}
