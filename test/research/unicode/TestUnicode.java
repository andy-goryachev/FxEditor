// Copyright © 2017-2018 Andy Goryachev <andy@goryachev.com>
package research.unicode;
import goryachev.common.test.TF;
import goryachev.common.test.Test;


/**
 * Test Unicode.
 */
public class TestUnicode
{
	public static void main(String[] args)
	{
		TF.run();
	}
	
	
	@Test
	public void print()
	{
		// https://www.w3.org/People/danield/unic/unichar.htm
	    t("Etruscan", 0x00010200, 0x00010227);
	    t("Gothic", 0x00010230, 0x0001024B);
	    t("Klingon", 0x000123D0, 0x000123F9);
	    t("Western Musical Symbols", 0x0001D103, 0x0001D1D7);
	}
	
	
	protected void t(String name, int min, int max)
	{
		System.out.println(name);
		
		for(int i=min; i<=max; i++)
		{
			System.out.print(new String(Character.toChars(i)));
		}
		System.out.println("\n");
	}
}
