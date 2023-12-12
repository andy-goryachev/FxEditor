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


## Features

* supports up to 2^31 lines of text
* multiple selection and carets
* supports syntax highlight


## Requirements

Requires JavaFX 17+.
 

## Try It Out ##

The project is at a very early stage: less than 29% of all 
[identified features](https://github.com/andy-goryachev/FxEditor/blob/master/FxEditor%20Feature%20Matrix.xlsx)
is currently implemented. 

To see how little is implemented, launch 
[FxEditorDemoApp.java](https://github.com/andy-goryachev/FxEditor/blob/master/src/demo/edit/FxEditorApp.java)



## Example

```java
public class MainWindow extends FxWindow
{
	public MainWindow()
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

See [MainWindow.java](https://github.com/andy-goryachev/FxEditor/blob/master/src/demo/edit/MainWindow.java)


## Warning

This project is not ready for production.  Mutable (editable) text models are not yet implemented.

The editor relies on JavaFX PrismTextLayout to properly render proportional unicode text, which results in
performance issues when trying to render very long lines (millions of symbols), making it unsuitable for
projects that have to deal with such files, like programming editors or log viewers.  A fixed-width replacement
[FxTextEditor](https://github.com/andy-goryachev/FxTextEditor) is currently being worked on.


## Similar Projects

- Tomas Mikula's [RichTextFX](https://github.com/TomasMikula/RichTextFX)
- Gluon's [The Rich Text Area Project](https://github.com/gluonhq/rich-text-area)


## OpenJFX

This work continues under OpenJFX project:

- [JDK-8301121](https://bugs.openjdk.org/browse/JDK-8301121)
- [RichTextArea](https://github.com/andy-goryachev-oracle/jfx/blob/ag.rich.text.area/modules/javafx.controls/src/main/java/javafx/scene/control/rich/RichTextArea.java)


## License

This project and its source code is licensed under the [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0) and you should feel free to make adaptations of this work. Please see the included LICENSE file for further details.

