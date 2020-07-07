// Copyright © 2016-2020 Andy Goryachev <andy@goryachev.com>
package demo.edit;
import goryachev.fx.CommonStyles;
import goryachev.fx.FxStyleSheet;
import goryachev.fx.Theme;
import goryachev.fxeditor.FxEditor;


/**
 * this is how a style sheet is generated.
 */
public class Styles
	extends FxStyleSheet
{
	public Styles()
	{
		Theme theme = Theme.current();
		
		add
		(
			// common fx styles
			new CommonStyles(),
			
			selector(MainPane.PANE, FxEditor.PANE).defines
			(
				fontSize("200%")
			)
		);
	}
}
