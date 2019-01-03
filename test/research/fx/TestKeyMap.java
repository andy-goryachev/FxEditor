// Copyright © 2016-2019 Andy Goryachev <andy@goryachev.com>
package research.fx;
import goryachev.fx.FxButton;


/**
 * Test KeyMap.
 */
public class TestKeyMap
{
	enum Accelerators
	{
		NEW,
		OPEN,
		SAVE,
		SAVE_AS
	}
	
	
	public static final KeyMap<Accelerators> km = new KeyMap<>();
	
	
	// TODO define a function
	public void defineFunction()
	{
		FxButton n = new FxButton("New");
		// adds a key listener with 
		km.set(n, Accelerators.NEW);
	}
	
	
	public void construct()
	{
		km.map(Accelerators.NEW, "n");
		km.map(Accelerators.OPEN, "o");
		km.map(Accelerators.SAVE, "s");
		km.map(Accelerators.SAVE_AS, "a");
	}
}
