// Copyright © 2019-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.fxeditor;
import goryachev.fx.FxStyleSheet;
import goryachev.fx.Theme;


/**
 * FxEditorStyles.
 */
public class FxEditorStyles
	extends FxStyleSheet
{
	public Object fxEditor(Theme theme)
	{
		return selector(FxEditor.PANE).defines
		(
			backgroundColor(commas(theme.textBG, theme.textBG)),
			backgroundInsets(commas(0, 1)),
			backgroundRadius(0),

			// FIX does not work
			selector(FOCUSED).defines
			(
				backgroundColor(commas(theme.focus, theme.textBG)),
				backgroundInsets(commas(0, 1))
			)
		);
	}
}
