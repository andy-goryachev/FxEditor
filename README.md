# FxEditor

![screenshot](https://github.com/andy-goryachev/FxEditor/blob/master/doc/screenshot.png)


## Why ##

Nearly all Java text editors, Swing and FX alike, suffer from one deficiency: inability to work with large 
data models, such as logs or query results.

The goal of this project is to provide a professional FX text component that is capable of handling billions of 
lines, provides syntax highlighting, multiple carets and multiple selection, rich text capabilities,
embedded images and embedded components.

The main idea which allows for all these features is separation of the editor and underlying data model.
The data model then can be made as simple as a contiguous in-memory byte array, or as complex as memory-mapped 
file with a concurrent change log that enables editing of a very large files.


## Try It Out ##

The project is at a very early stage: less than 24% of all 
[identified features](https://github.com/andy-goryachev/FxEditor/blob/master/FxEditor%20Feature%20Matrix.xlsx)
is currently implemented. 

To see how little is implemented, launch 
[FxEditorDemoApp.java](https://github.com/andy-goryachev/FxEditor/blob/master/src/demo/edit/FxEditorDemoApp.java)
or
[DemoStylingApp.java](https://github.com/andy-goryachev/FxEditor/blob/master/src/demo/style/DemoStylingApp.java).


## Example

```java
public class DemoWindow extends FxWindow
{
	public DemoWindow()
	{
		super("TestFxEditorWindow");
		setTitle("FxEditor Demo");
		setSize(600, 700);
		
		// data model
		FxEditorModel m = new TestFxColorEditorModel();
		// editor component is a Pane
		FxEditor ed = new FxEditor(m);
		ed.setMultipleSelectionEnabled(true);
		// add to layout		
		setCenter(ed);
	}
}
```

See [DemoWindow.java](https://github.com/andy-goryachev/FxEditor/blob/master/src/demo/edit/DemoWindow.java)


## Similar Projects

Tomas Mikula's [RichTextFX](https://github.com/TomasMikula/RichTextFX).


## License

This project and its source code is licensed under the [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0) and you should feel free to make adaptations of this work. Please see the included LICENSE file for further details.
