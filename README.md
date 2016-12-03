# FxEditor

JavaFX Rich Text Editor That Can Handle Billions of Lines.

![screenshot](https://github.com/andy-goryachev/FxEditor/blob/master/screenshots/2016-1126-172643-252.png)


## Why ##

Nearly all Java text editors, Swing and FX alike, suffer from one deficiency: inability to work with large 
data models, such as logs or query results.

The goal of this project is to provide an FX text component that not only is capable of handling billions of 
lines, but also provide syntax highlighting, multiple carets and multiple selection, rich text capabilities,
embedded images and other complex features.

The main idea which allows for all these features is separation of the editor and underlying data model.
The data model then can be made as simple as a contiguous in-memory byte array, or as complex as memory-mapped 
file with a concurrent change log that enables editing of a very large files.


## Try It Out ##

The project is at a very early stage.  

To see how little is implemented, run 
[TestFxEditorApp.java](https://github.com/andy-goryachev/FxEditor/blob/master/src/demo/edit/TestFxEditorApp.java).


## Similar Projects

Tomas Mikula's [RichTextFX](https://github.com/TomasMikula/RichTextFX) 


## Example
```java
public class TestFxEditorWindow extends FxWindow
{
	public TestFxEditorWindow()
	{
		super("TestFxEditorWindow");
		setTitle("FxEditor Demo");
		setSize(600, 700);
		
		// data model
		FxEditorModel m = new TestFxColorEditorModel();
		// editor component is a Pane
		FxEditor ed = new FxEditor(m);
		ed.setBlinkRate(Duration.millis(600));
		// add to layout		
		setCenter(ed);
	}
}
```


## License

This project and its source code is licensed under the [GNU General Public License version 3](https://www.gnu.org/licenses/gpl-3.0.en.html) and you should feel free to make adaptations of this work. Please see the included LICENSE file for further details.

Please [contact me](http://goryachev.com/contact.html) if you want to acquire this library under a business-friendly license.

