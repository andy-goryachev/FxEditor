// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.CMap;
import javafx.scene.paint.Color;


/**
 * Color Theme.
 */
public class Theme
{
	// TODO perhaps use annotations?
	public enum Key
	{
		/** base color for all objects */
		BASE("base", Color.class),
		/** focus outline color */
		FOCUS("focus", Color.class),
		/** inactive or unfocused selection color */
		INACTIVE_SELECTION_BG("inactiveSelectionBG", Color.class),
		OUTLINE("outline", Color.class),
		SELECTED_TEXT_BG("selectedTextBG", Color.class),
		SELECTED_TEXT_FG("selectedTextFG", Color.class),
		TEXT_BG("textBG", Color.class),
		TEXT_FG("textFG", Color.class),
		;
		
		public final String name;
		public final Class type;
		
		Key(String name, Class type)
		{
			this.name = name;
			this.type = type;
		}
	}
	
	public final Color base;
	public final Color focus;
	public final Color inactiveSelectionBG;
	public final Color outline;
	public final Color selectedTextBG;
	public final Color selectedTextFG;
	public final Color textBG;
	public final Color textFG;
	private final CMap<Key,Object> data;
	
	private static Theme current;
	
	
	public Theme(CMap<Key,Object> data)
	{	
		this.data = data;
		
		base = c(Key.BASE);
		focus = c(Key.FOCUS);
		inactiveSelectionBG = c(Key.INACTIVE_SELECTION_BG);
		outline = c(Key.OUTLINE);
		selectedTextBG = c(Key.SELECTED_TEXT_BG);
		selectedTextFG = c(Key.SELECTED_TEXT_FG);
		textBG = c(Key.TEXT_BG);
		textFG = c(Key.TEXT_FG);
	}
	
	
	protected Color c(Key k)
	{
		if(k.type != Color.class)
		{
			throw new Error("Key must have Color type: " + k);
		}
		
		Object v = data.get(k);
		if(v instanceof Color)
		{
			return (Color)v;
		}
		return Color.RED;
	}
	

	public static Theme current()
	{
		if(current == null)
		{
			CMap<Key,Object> m = loadFromSettings();
			if(m == null)
			{
				m = createDefaultTheme();
			}
			current = new Theme(m);
		}
		return current;
	}
	
	
	private static CMap<Key,Object> loadFromSettings()
	{
		// TODO first standard names
		// TODO use keys to load values
		return null;
	}
	

	public void check()
	{
		CList<Color> fs = CKit.collectPublicStaticFields(getClass(), Color.class);
		for(Color c: fs)
		{
			if(c == null)
			{
				throw new Error("must defina all colors in a theme");
			}
		}
	}
	
	
	private static CMap<Key,Object> createDefaultTheme()
	{
		return createFromArray
		(
			Key.BASE, FX.rgb(0xececec),
			Key.FOCUS, FX.rgb(0xff6d00),
			Key.INACTIVE_SELECTION_BG, FX.rgb(0xff6d00),
			Key.OUTLINE, FX.rgb(0xdddddd),
			Key.SELECTED_TEXT_BG, FX.rgb(0xffff00),
			Key.SELECTED_TEXT_FG, Color.BLACK,
			Key.TEXT_BG, Color.WHITE,
			Key.TEXT_FG, Color.BLACK
		);
	}
	
	
	private static CMap<Key,Object> createFromArray(Object ... items)
	{
		CMap<Key,Object> d = new CMap();
		for(int i=0; i<items.length; )
		{
			Key k = (Key)items[i++];
			Object v = items[i++];
			if(!k.type.isAssignableFrom(v.getClass()))
			{
				throw new Error(k + " requires type " + k.type);
			}
			d.put(k, v);
		}
		return d;
	}
}
