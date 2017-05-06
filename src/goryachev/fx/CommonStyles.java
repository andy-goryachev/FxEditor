// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;

import goryachev.fx.FxStyleSheet.Selector;
import javafx.scene.paint.Color;

/**
 * Common Styles.
 */
public class CommonStyles
	extends FxStyleSheet
{
	/** bold type face */
	public static final CssStyle BOLD = new CssStyle("CommonStyles_BOLD");

	/** disables horizontal scroll bar */
	public static final CssStyle NO_HORIZONTAL_SCROLL_BAR = new CssStyle("CommonStyles_NO_HORIZONTAL_SCROLL_BAR");

	
	public CommonStyles()
	{
		add
		(
			// bold
			selector(BOLD).defines
			(
				fontWeight("bold")
			),
			
			// hide empty table rows
			selector(".table-row-cell:empty").defines
			(
				backgroundColor(TRANSPARENT)
			),
			
			// disables horizontal scroll bar
			selector(NO_HORIZONTAL_SCROLL_BAR).defines
			(
				selector(".scroll-bar:horizontal").defines
				(
					maxHeight(0),
					padding(0),
					opacity(0)
				),
				selector(".scroll-bar:horizontal *").defines
				(
					maxHeight(0),
					padding(0),
					opacity(0)
				)
			),
			
			// fix text selection colors
			selector(".text-input").defines
			(
				new Selector(FOCUSED).defines
				(
					textFill(Color.BLACK), // FIX theme
					prop("-fx-highlight-text-fill", Color.BLACK) // FIX theme
				)
			),
			
			// text smoothing
			selector(".text").defines
			(
				prop("-fx-font-smoothing-type", "gray")
			),
			
			// scroll pane
			selector(".scroll-pane").defines
			(
//				new Selector(FOCUSED).defines
//				(
//					// removes focused border from scroll pane
//					// TODO do it specifically for the content pane
//					backgroundInsets(1)
//				),
				new Selector(" > .viewport").defines
				(
					backgroundColor(Color.WHITE) // FIX theme
				)
			),
			
			// checkbox FIX
			selector(".check-box").defines
			(
				labelPadding("0.0em 0.0em 0.0em 0.416667em"),
				textFill("-fx-text-background-color"),
				
				selector(" > .box").defines
				(
					backgroundRadius(commas(3, 2, 1)),
					padding("0.166667em 0.166667em 0.25em 0.25em"),
					
					selector("> .mark").defines
					(
						backgroundColor(null),
						padding("0.416667em 0.416667em 0.5em 0.5em"),
						shape("M19 3H5c-1.11 0-2 .9-2 2v14c0 1.1.89 2 2 2h14c1.11 0 2-.9 2-2V5c0-1.1-.89-2-2-2zm-9 14l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z")
					)
				),
				
				selector(":indeterminate > .box").defines
				(
					padding(0),
					
					selector("> .mark").defines
					(
						shape("M0,0H10V2H0Z"),
						scaleShape(false),
						padding("0.666667em")
					)
				)
			),
			
			// radio buttons - FIX
			selector(".radio-button").defines
			(
//				padding(10),
				
				selector(".text").defines
				(
					fill("-fx-text-base-color")
				)
			),
			selector(".radio-button>.radio, .radio-button>.radio.unfocused, .radio-button:disabled>.radio, .radio-button:selected>.radio").defines
			(
				borderRadius(100),
				borderColor("gray"), // FIX
				borderWidth(2),
				backgroundRadius(100),
				backgroundColor(TRANSPARENT),
				padding(3)
			)
		);
	}
}
