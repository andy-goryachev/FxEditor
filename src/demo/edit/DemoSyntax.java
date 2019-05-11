// Copyright © 2017-2019 Andy Goryachev <andy@goryachev.com>
package demo.edit;
import goryachev.common.util.CList;
import javafx.scene.paint.Color;


/**
 * DemoSyntax.
 */
public class DemoSyntax
{
	private final String text;
	private final CList<Segment> segments = new CList();
	private int start;
	private Color color = Color.BLACK;
	
	
	public DemoSyntax(String text)
	{
		this.text = text;
	}
	
	
	public CList<Segment> generateSegments()
	{
		for(int i=0; i<text.length(); i++)
		{
			char c = text.charAt(i);
			
			int close = getClosingChar(c);
			if(close >= 0)
			{
				int ix = text.indexOf(close, i);
				if(ix >= 0)
				{
					addSegment(i);
					color = Color.BLUE;
					i = ix;
					
					addSegment(i);
					continue;
				}
			}
			
			Color col = getColor(c);
			if(!col.equals(color))
			{
				addSegment(i);
				color = col;
			}
		}
		
		addSegment(text.length());

		return segments;
	}
	
	
	protected int getClosingChar(char c)
	{
		switch(c)
		{
		case '(':
			return ')';
		case '[':
			return ']';
		case '{':
			return '}';
		}
		return -1;
	}


	protected void addSegment(int end)
	{
		if(end > start)
		{
			String s = text.substring(start, end);
			segments.add(new Segment(color, s));
			start = end;
		}
	}
	
	
	protected Color getColor(char c)
	{
		if(Character.isDigit(c))
		{
			return Color.RED;
		}
		
		byte dir = Character.getDirectionality(c);
		switch(dir)
		{
		case Character.DIRECTIONALITY_ARABIC_NUMBER:
		case Character.DIRECTIONALITY_RIGHT_TO_LEFT:
		case Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC:
		case Character.DIRECTIONALITY_RIGHT_TO_LEFT_EMBEDDING:
		case Character.DIRECTIONALITY_RIGHT_TO_LEFT_OVERRIDE:
			return Color.OLIVEDRAB;
		}
		
		return Color.BLACK;
	}
}
