// Copyright © 2016-2019 Andy Goryachev <andy@goryachev.com>
package demo.fx;
import goryachev.fx.CommonStyles;
import goryachev.fx.FxStyleSheet;
import goryachev.fx.Theme;
import goryachev.fx.edit.FxEditor;
import demo.fx.pages.edit.FxEditorDemoPane;
import demo.fx.pages.edit.FxEditorStyledModelDemoPane;
import demo.fx.pages.edit.SegmentTextEditorModel;
import javafx.scene.paint.Color;


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
			),
			
			selector(FxEditorStyledModelDemoPane.EDITOR).defines
			(
				fontSize("170%"),
				selector(".text").defines
				(
					fontStyle("italic")
				)
			),
			
			selector(FxEditor.VFLOW).defines
			(
				backgroundColor(Color.gray(0.97))
			),
			selector(FxEditor.CARET_LINE_HIGHLIGHT).defines
			(
				fill(Color.WHITE),
				effect("dropshadow(two-pass-box, rgba(0, 0, 0, 0.3), 7, 0, 0, 0)")
			),
			selector(FxEditor.SELECTION_HIGHLIGHT).defines
			(
				fill(Color.rgb(255, 255, 0, 0.25))
			)
		);
	}
}
