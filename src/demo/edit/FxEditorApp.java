// Copyright © 2016-2022 Andy Goryachev <andy@goryachev.com>
package demo.edit;
import goryachev.common.log.Log;
import goryachev.common.util.FileSettingsProvider;
import goryachev.common.util.GlobalSettings;
import goryachev.fx.CssLoader;
import java.io.File;
import javafx.application.Application;
import javafx.stage.Stage;


/**
 * Test FxEditor app.
 */
public class FxEditorApp
	extends Application
{
	public static void main(String[] args)
	{
		launch(args);
	}


	public void init() throws Exception
	{
		// TODO change to something visible in Documents? platform-specific?
		File baseDir = new File(System.getProperty("user.home"), ".goryachev.com/FxEditorDemo");
			
		// TODO
		//File logFolder = new File(baseDir, "logs"); 
		Log.initForDebug();
		
		File settingsFile = new File(baseDir, "settings.conf");
		FileSettingsProvider p = new FileSettingsProvider(settingsFile);
		GlobalSettings.setProvider(p);
		p.loadQuiet();
	}


	public void start(Stage stage) throws Exception
	{
		new MainWindow().open();
		
		// init styles
		CssLoader.setStyles(() -> new Styles());
	}
}
