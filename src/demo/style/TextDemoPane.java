// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package demo.style;
import goryachev.fx.CComboBox;
import goryachev.fx.CPane;
import goryachev.fx.FX;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


/**
 * Text Components Pane.
 */
public class TextDemoPane
	extends CPane
{
	public final TextField textField;
	public final CComboBox comboBox;
	public final CComboBox comboBoxEditable;
	public final TextArea textPref;
	public final TextArea textFill;
	
	
	public TextDemoPane()
	{
		textField = new TextField("sample text");
		
		comboBox = new CComboBox(new String[] { "one", "two", "tree" });
		
		comboBoxEditable = new CComboBox(new String[] { "one", "two", "tree" });
		comboBoxEditable.setEditable(true);
		
		textPref = new TextArea("1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n")
		{
//			{
//				setPrefHeight(USE_COMPUTED_SIZE );
//			}
			
//			protected double computePrefWidth(double h)
//			{
//				double w = super.computePrefWidth(h);
//				D.print(w);
//				return w;
//			}
//			
//			
//			protected double computePrefHeight(double w)
//			{
//				double h = super.computePrefHeight(w);
//				D.print(h);
//				return h;
//			}
		};
		
		textFill = new TextArea("Line 0: zero  The second law of thermodynamics states that the total entropy of an isolated system can only increase over time. It can remain constant in ideal cases where the system is in a steady state (equilibrium) or undergoing a reversible process. The increase in entropy accounts for the irreversibility of natural processes, and the asymmetry between future and past. 熱力学第二برگشت‌ناپذیر法則（ねつりきがくだいにほうそく、英: second law of thermodynamics）は、エネルギーのبرگشت‌ناپذیر 移動の方向とエネルギーの質に関する法則である。 Второе начало термодинамики (второй закон термодинамики) устанавливает существование энтропии[1] как функции состояния термодинамической системы и вводит понятие абсолютной термодинамической температуры, то есть «второе начало представляет собой закон об энтропии» и её свойствах[4]. В изолированной системе энтропия остаётся либо неизменной, либо возрастает (в неравновесных процессах), достигая максимума при достижении термодинамического равновесия (закон возрастания энтропии). Встречающиеся в литературе различные формулировки второго начала термодинамики представляют собой собой частные выражения общего закона возрастания энтропии.قانون دوم ترمودینامیک بیان می‌کند که در یک پروسهٔ طبیعی ترمودینامیکی جمع انتروپی تک‌افتادهٔ سیستم‌های شرکت کننده در آن پروسه، همواره با گذشت زمان افزایش می‌یابد، (تنها اگر در شرایط ایده‌آل حالت دایمی، یا تحت فرایند برگشت‌پذیری قرار داشته‌باشد، ثابت می‌ماند). به بیان دیگر هیچ پروسهٔ ترمودینامیکی وجود ندارد که با گذشت زمان با افزایش انتروپی همراه نباشد. این افزایش آنتروپی برابر است با افزایش اتلاف انرژی، (و سازگار با فرایند برگشت‌ناپذیر و اصل نابرابری گذشته و آینده).ऊष्मागतिकी का द्वितीय सिद्धान्त प्राकृतिक प्रक्रमों के अनुत्क्रमणीयता को प्रतिपादित करता है। यह कई प्रकार से कहा जा सकता है। आचार्यों ने इस नियम के अनेक रूप दिए हैं जो मूलत: एक ही हैं। यह ऊष्मागतिक निकायों में 'एण्ट्रोपी' (Entropy) नामक भौतिक राशि के अस्तित्व को इंगित करता है। ऐसे उष्मिक इंजन का निर्माण करना संभव नहीं जो पूरे चक्र में काम करते हुए केवल एक ही पिंड से उष्मा ग्रहण करे और काम करनेवाले निकाय में बिना परिवर्तन लाए उस संपूर्ण उष्मा को काम में बदल दे (प्लांक-केल्विन)। बिना बाहरी सहायता के कोई भी स्वत: काम करनेवाली मशीन उष्मा को निम्नतापीय पिंड से उच्चतापीय में नह");
		textFill.setWrapText(true);
		
		// weird behavior: set several lines, click on a line in the middle, press backspace repeatedly
		// what happens next is weird
		//textFill.textProperty().addListener((s) -> D.print(Dump.toPrintable(textFill.getText())));
		//textFill.setText("1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n");
		
		// layout
		setPadding(10);
		setGaps(10, 5);
		addColumns
		(
			CPane.PREF,
			CPane.FILL
		);
		int r = 0;
		add(0, r, FX.label("Text Field:", Pos.TOP_RIGHT));
		add(1, r, textField);
		r++;
		add(0, r, FX.label("Combo Box:", Pos.TOP_RIGHT));
		add(1, r, comboBox);
		r++;
		add(0, r, FX.label("Combo Box (editable):", Pos.TOP_RIGHT));
		add(1, r, comboBoxEditable);
		r++;
		addRow(CPane.PREF);
		add(0, r, FX.label("Text Area (PREF):", Pos.TOP_RIGHT));
		add(1, r, textPref);
		r++;
		addRow(CPane.FILL);
		add(0, r, FX.label("Text Area (FILL):", Pos.TOP_RIGHT));
		add(1, r, textFill);
		r++;
	}
}
