// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package research.fx.edit.internal;


/**
 * FxEditor Tools.
 */
public class FxEditorTools
{
	public static boolean isNearlySame(double a, double b)
	{
		// in case for some reason floating point computation result is slightly off
		return Math.abs(a - b) < 0.01;
	}
}
