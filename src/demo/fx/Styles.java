// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package demo.fx;
import goryachev.fx.CommonStyles;
import goryachev.fx.FxStyleSheet;
import goryachev.fx.Theme;
import demo.fx.pages.edit.FxEditorDemoPane;
import demo.fx.pages.edit.SegmentTextEditorModel;


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
			
			selector(SegmentTextEditorModel.HEADING).defines
			(
				fontSize("150%"),
				fontWeight("bold")
			),
			
			selector(FxEditorDemoPane.EDITOR).defines
			(
				fontSize("170%")
			)
		);
	}
}
