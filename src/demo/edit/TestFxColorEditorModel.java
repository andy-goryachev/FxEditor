// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package demo.edit;
import goryachev.common.util.CList;
import goryachev.common.util.SB;
import goryachev.fx.edit.CTextFlow;
import goryachev.fx.edit.FxPlainEditorModel;
import java.text.NumberFormat;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


/**
 * test plain text model with 2 billion rows
 */
public class TestFxColorEditorModel
	extends FxPlainEditorModel
{
	private NumberFormat format = NumberFormat.getInstance();
	
	
	public TestFxColorEditorModel()
	{
	}
	
	
	public Region getDecoratedLine(int line)
	{
		CList<Segment> ss = getSegments(line, true);
		
		CTextFlow f = new CTextFlow();
		for(Segment s: ss)
		{
			Text t = new Text(s.text);
			t.setFill(s.color);
			
			f.getChildren().add(t);
		}
		return f;
	}
	
	
	public String getPlainText(int line)
	{
		CList<Segment> ss = getSegments(line, false);
		SB sb = new SB();
		for(Segment s: ss)
		{
			sb.a(s.text);
		}
		return sb.toString();
	}


	public LoadInfo getLoadInfo()
	{
		return null; 
	}


	public int getLineCount()
	{
		return Integer.MAX_VALUE;
	}


	protected CList<Segment> getSegments(int line, boolean styles)
	{
		String s = String.valueOf(line);
		int sz = s.length();
		
		CList<Segment> ss = new CList<>();
		
		ss.add(new Segment(Color.LIGHTGRAY, "Line " + format.format(line) + ": "));
		
		for(int i=0; i<sz; i++)
		{
			char c = s.charAt(i);
			String w = toWord(c);
			
			ss.add(new Segment(styles ? toColor(c) : null, w));
		}
		
		if(line % 5 == 0)
		{
			ss.add(new Segment(Color.GRAY, " The second law of thermodynamics states that the total entropy of an isolated system can only increase over time. It can remain constant in ideal cases where the system is in a steady state (equilibrium) or undergoing a reversible process. The increase in entropy accounts for the irreversibility of natural processes, and the asymmetry between future and past."));
		}
		
		if(line % 7 == 0)
		{
			ss.add(new Segment(Color.GRAY, " 熱力学第二法則（ねつりきがくだいにほうそく、英: second law of thermodynamics）は、エネルギーの移動の方向とエネルギーの質に関する法則である。"));
		}
		
		if(line % 13 == 0)
		{
			ss.add(new Segment(Color.GRAY, " Второе начало термодинамики (второй закон термодинамики) устанавливает существование энтропии[1] как функции состояния термодинамической системы и вводит понятие абсолютной термодинамической температуры[2], то есть «второе начало представляет собой закон об энтропии»[3] и её свойствах[4]. В изолированной системе энтропия остаётся либо неизменной, либо возрастает (в неравновесных процессах[3]), достигая максимума при достижении термодинамического равновесия (закон возрастания энтропии)[5][6][2]. Встречающиеся в литературе различные формулировки второго начала термодинамики представляют собой собой частные выражения общего закона возрастания энтропии[5][6]."));
		}
		
		if(line % 17 == 0)
		{
			// FIX selection shape is incorrect if mixing LTR and RTL languages
			ss.add(new Segment(Color.GRAY, "قانون دوم ترمودینامیک بیان می‌کند که در یک پروسهٔ طبیعی ترمودینامیکی جمع انتروپی تک‌افتادهٔ سیستم‌های شرکت کننده در آن پروسه، همواره با گذشت زمان افزایش می‌یابد، (تنها اگر در شرایط ایده‌آل حالت دایمی، یا تحت فرایند برگشت‌پذیری قرار داشته‌باشد، ثابت می‌ماند). به بیان دیگر هیچ پروسهٔ ترمودینامیکی وجود ندارد که با گذشت زمان با افزایش انتروپی همراه نباشد. این افزایش آنتروپی برابر است با افزایش اتلاف انرژی، (و سازگار با فرایند برگشت‌ناپذیر و اصل نابرابری گذشته و آینده)."));
		}
		
		if(line % 31 == 0)
		{
			ss.add(new Segment(Color.GRAY, "ऊष्मागतिकी का द्वितीय सिद्धान्त प्राकृतिक प्रक्रमों के अनुत्क्रमणीयता को प्रतिपादित करता है। यह कई प्रकार से कहा जा सकता है। आचार्यों ने इस नियम के अनेक रूप दिए हैं जो मूलत: एक ही हैं। यह ऊष्मागतिक निकायों में 'एण्ट्रोपी' (Entropy) नामक भौतिक राशि के अस्तित्व को इंगित करता है। ऐसे उष्मिक इंजन का निर्माण करना संभव नहीं जो पूरे चक्र में काम करते हुए केवल एक ही पिंड से उष्मा ग्रहण करे और काम करनेवाले निकाय में बिना परिवर्तन लाए उस संपूर्ण उष्मा को काम में बदल दे (प्लांक-केल्विन)। बिना बाहरी सहायता के कोई भी स्वत: काम करनेवाली मशीन उष्मा को निम्नतापीय पिंड से उच्चतापीय में नहीं ले जा सकती, अर्थात् उष्मा ठंडे पिंड से गरम में स्वत: नहीं जा सकती (क्लाज़िउस)। "));
		}
		
		return ss;
	}
	
	
	protected String toWord(char c)
	{
		switch(c)
		{
		case '0': return "zero ";
		case '1': return "one ";
		case '2': return "two ";
		case '3': return "three ";
		case '4': return "four ";
		case '5': return "five ";
		case '6': return "six ";
		case '7': return "seven ";
		case '8': return "eight ";
		case '9': return "nine ";
		default: return String.valueOf(c);
		}
	}
	
	
	protected Color toColor(char c)
	{
		switch(c)
		{
		case '0': return c(0);
		case '1': return c(1);
		case '2': return c(2);
		case '3': return c(3);
		case '4': return c(4);
		case '5': return c(5);
		case '6': return c(6);
		case '7': return c(7);
		case '8': return c(8);
		case '9': return c(9);
		default: return null;
		}
	}
	
	
	protected Color c(int angle)
	{
		return Color.hsb(36.0 * angle + 1, 1.0, 0.7);
	}
	
	
	//
	
	
	public static class Segment
	{
		public final String text;
		public final Color color;
		
		
		public Segment(Color color, String text)
		{
			this.text = text;
			this.color = color;
		}
	}
}
