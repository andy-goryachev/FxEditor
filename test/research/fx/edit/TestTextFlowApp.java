// Copyright © 2016-2022 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.common.log.Log;
import goryachev.common.util.FileSettingsProvider;
import goryachev.common.util.GlobalSettings;
import goryachev.common.util.SB;
import goryachev.fx.FxWindow;
import goryachev.fx.hacks.CHitInfo;
import goryachev.fxeditor.CTextFlow;
import java.io.File;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import javafx.stage.Stage;


/**
 * Test app
 */
public class TestTextFlowApp
	extends Application
{
	public static void main(String[] args)
	{
		launch(args);
	}


	public void init() throws Exception
	{
		// TODO change to something visible in Documents? platform-specific?
		File baseDir = new File(System.getProperty("user.home"), ".goryachev.com");
			
		//File logFolder = new File(baseDir, "logs"); 
		Log.initForDebug();
		
		File settingsFile = new File(baseDir, "settings.conf");
		FileSettingsProvider p = new FileSettingsProvider(settingsFile);
		GlobalSettings.setProvider(p);
		p.loadQuiet();
	}


	public void start(Stage stage) throws Exception
	{
		new TestTextFlowWindow().open();
	}
	
	
	//
	
	
	public static class TestTextFlowWindow
		extends FxWindow
	{
		protected final Text info;
		protected final Path caret;
		protected final Path highlight;
		
		
		public TestTextFlowWindow()
		{
			super("TestTextFlowWindow");
			
			setTitle("TextFlow Test");
			setSize(600, 200);
			
			info = new Text();
			
			highlight = new Path();
			highlight.setManaged(false);
			highlight.setStroke(null);
			highlight.setFill(Color.YELLOW);
			
			caret = new Path();
			caret.setManaged(false);
			caret.setStroke(Color.BLACK);
	
			setTop(tf());
			setBottom(new CTextFlow(info));
		}
		
		
		protected CTextFlow tf()
		{
			String text = "The quick brown fox jumps over the lazy dog.  از ویکی‌پدیا، دانشنامهٔ آزاد すばしっこい茶色の狐はのろまな犬を飛び越える 𓀀𓀁𓀂𓀃 𓀄𓀅𓀆𓀇𓀈 𓀉𓀊𓀋𓀌𓀍𓀎𓀏";
			
			Text t = new Text(text);
			t.setStyle("-fx-font-size:300%");
			
			CTextFlow f = new CTextFlow(highlight, t, caret);
			f.addEventFilter(MouseEvent.ANY, (ev) -> handleMouseEvent(f, ev));
			return f;
		}
	
	
		protected void handleMouseEvent(CTextFlow t, MouseEvent ev)
		{
			Point2D p = t.screenToLocal(ev.getScreenX(), ev.getScreenY());
			CHitInfo h = t.getHit(p.getX(), p.getY());

			// NOTE: this test code does not handle text resizing
			highlight.getElements().setAll(t.getRange(0, h.getInsertionIndex()));
			caret.getElements().setAll(t.getCaretShape(h.getCharIndex(), h.isLeading()));
				
			SB sb = new SB();
			sb.a(h);
			
			info.setText(sb.toString());
		}
	}
}
