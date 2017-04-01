// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package research.fx;
import java.lang.ref.WeakReference;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Binding;


/**
 * Binder.
 */
public class Binder
{
	/** fires handler if any of the observables change */
	public static void onChange(Runnable handler, Observable ... props)
	{
		Helper li = new Helper(handler);

		for(Observable p: props)
		{
			p.addListener(li);
		}
	}


	//


	public static class Helper
		implements InvalidationListener
	{
		private final WeakReference<Runnable> ref;


		public Helper(Runnable handler)
		{
			ref = new WeakReference<>(handler);
		}


		@Override
		public void invalidated(Observable observable)
		{
			final Runnable h = ref.get();
			if(h == null)
			{
				observable.removeListener(this);
			}
			else
			{
				h.run();
			}
		}
	}
}
