// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.scene.effect.BlurType;
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
		Theme theme = Theme.current();
		
		add
		(
			selector(".root").defines
			(
				// text selection
				prop("-fx-accent", FX.rgba(0xffff8b, 0.7)),
				prop("-fx-base", theme.base),
				prop("-fx-highlight-text-fill", theme.selectedTextFG),
				// focus outline
				prop("-fx-focus-color", theme.focus),
				// focus glow
				prop("-fx-faint-focus-color", TRANSPARENT)
			),
						
			// checkbox FIX
			selector(".check-box").defines
			(
				labelPadding("0.0em 0.0em 0.0em 0.416667em"),
				textFill("-fx-text-background-color"),
				
				selector(HOVER, "> .box").defines
				(
					color(Color.RED) // -fx-hover-base;
				),
				selector(ARMED).defines
				(
					color(Color.GREEN) // "-fx-pressed-base")
				),
				
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
				
				selector(FOCUSED, "> .box").defines
				(
					backgroundColor("-fx-focus-color, -fx-inner-border, -fx-body-color, -fx-faint-focus-color, -fx-body-color"),
					backgroundInsets("-0.2, 1, 2, -1.4, 2.6"),
					backgroundRadius("3, 2, 1, 4, 1")
					
//					backgroundColor("-fx-focus-color, -fx-body-color"),
//					backgroundInsets("-2, -1"),
//					backgroundRadius("3, 2")
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
			
			// combo box
			selector(".combo-box-base").defines
			(
//				backgroundColor(Color.RED),
//				backgroundInsets(0),
//				backgroundRadius(0),
				
				selector(EDITABLE).defines
				(
					selector("> .text-field").defines
					(
						backgroundColor(theme.textBG),
						backgroundInsets(1), // 1 0 1 1
						backgroundRadius(0)
					),
					
					selector(FOCUSED, "> .text-field").defines
					(
						backgroundColor(theme.textBG),
						backgroundInsets(1),
						backgroundRadius(0)
					)
				)
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
					backgroundColor(theme.textBG)
				)
			),
			
			// hide empty table rows
			selector(".table-row-cell:empty").defines
			(
				backgroundColor(TRANSPARENT)
			),
			
			// text smoothing
			selector(".text").defines
			(
				prop("-fx-font-smoothing-type", "gray")
			),
			
			// text area
			// FIX change insets
			selector(".text-area").defines
			(
				backgroundColor(theme.textBG),
				
				selector(".content").defines
				(
					backgroundColor(theme.textBG),
					backgroundRadius(0)
				),
				
				selector(FOCUSED, ".content").defines
				(
					backgroundColor(theme.textBG),
					backgroundRadius(0),
					backgroundInsets(0)
				)
			),
			
			// fix text selection colors
			selector(".text-input").defines
			(
				backgroundInsets(commas(0, 1)),
				backgroundColor(commas(theme.outline, theme.textBG)),
				backgroundRadius(commas(0, 0)),
				
				new Selector(FOCUSED).defines
				(
					textFill(theme.textFG),
					prop("-fx-highlight-text-fill", theme.selectedTextFG),
					backgroundInsets(commas(0, 1)),
					backgroundColor(commas(theme.focus, theme.textBG)),
					backgroundRadius(commas(0, 0)),
					// TODO provide a method
					// BlurType blurType, Color color, double radius, double spread, double offsetX, double offsetY
					effect("dropshadow(two-pass-box, rgba(0, 0, 0, 0.4), 12, 0, 2, 2)")
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
			),
			
			// andy's hacks
			
			// FX.style() bold 
			selector(BOLD).defines
			(
				fontWeight("bold")
			),

			// disables horizontal scroll bar
			// FIX does not disable completely
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
			)
		);
	}
}
