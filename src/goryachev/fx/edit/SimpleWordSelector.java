// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.common.util.CKit;
import java.util.function.BiConsumer;


/**
 * Simple Word Selector uses whitespace and punctuation to delimit words.
 */
public class SimpleWordSelector
	implements BiConsumer<FxEditor,Marker>
{
	public SimpleWordSelector()
	{
	}
	
	
	protected boolean isWordChar(int c)
	{
		return Character.isLetterOrDigit(c);
	}
	
	
	protected int skipWordCharsForward(String text, int start)
	{
		int len = text.length();
		for(int i=start; i<len; i++)
		{
			// TODO surrogate
			char c = text.charAt(i);
			if(!isWordChar(c))
			{
				return i;
			}
		}
		return -1;
	}
	
	
	protected int skipNonWordCharsForward(String text, int start)
	{
		int len = text.length();
		for(int i=start; i<len; i++)
		{
			// TODO surrogate
			char c = text.charAt(i);
			if(isWordChar(c))
			{
				return i;
			}
		}
		return -1;
	}
	
	
	// FIX out of bounds error when dbl-clicking end of line
	protected int skipWordCharsBackward(String text, int start)
	{
		for(int i=start; i>=0; i--)
		{
			// TODO surrogate
			char c = text.charAt(i);
			if(!isWordChar(c))
			{
				return i;
			}
		}
		return -1;
	}
	
	
	public void accept(FxEditor ed, Marker m)
	{
		int line = m.getLine();
		String text = ed.getTextOnLine(line);
		if(text == null)
		{
			return;
		}
		
		int pos = m.getLineOffset();
		int len = ed.getTextLength(line);
		
		// FIX if pos==len
		int start = skipWordCharsBackward(text, pos);
		int end;
		if(start < 0)
		{
			start = skipNonWordCharsForward(text, pos);
			end = skipWordCharsForward(text, start);
			if(end < 0)
			{
				end = len;
			}
		}
		else
		{
			start++;
			
			end = skipWordCharsForward(text, pos);
			if(end < 0)
			{
				end = len;
			}
			else
			{
				end--;
			}
		}
		
		Marker m0 = ed.newMarker(line, start, true);
		Marker m1 = ed.newMarker(line, end, false);
		ed.select(m0, m1);
	}
}
