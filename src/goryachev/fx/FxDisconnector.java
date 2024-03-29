// Copyright © 2021-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.CList;
import goryachev.common.util.Disconnectable;
import java.lang.ref.WeakReference;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;


/**
 * Fx Disconnector.
 */
public class FxDisconnector
	implements Disconnectable
{
	private final CList<Disconnectable> items = new CList();
	private static final Object KEY = new Object();
	
	
	public FxDisconnector()
	{
	}
	
	
	public static FxDisconnector get(Node n)
	{
		Object x = n.getProperties().get(KEY);
		if(x instanceof FxDisconnector)
		{
			return (FxDisconnector)x;
		}
		FxDisconnector d = new FxDisconnector();
		n.getProperties().put(KEY, d);
		return d;
	}
	
	
	public static void disconnect(Node n)
	{
		Object x = n.getProperties().get(KEY);
		if(x instanceof FxDisconnector)
		{
			((FxDisconnector)x).disconnect();
		}
	}
	
	
	public void addDisconnectable(Disconnectable d)
	{
		items.add(d);
	}


	public void disconnect()
	{
		for(int i=items.size()-1; i>=0; i--)
		{
			Disconnectable d = items.remove(i);
			d.disconnect();
		}
	}
	
	
	// change listeners
	
	
	public Disconnectable addChangeListener(Runnable callback, ObservableValue<?> ... props)
	{
		return addChangeListener(callback, false, props);
	}
	

	public Disconnectable addChangeListener(Runnable onChange, boolean fireImmediately, ObservableValue<?> ... props)
	{
		ChLi li = new ChLi()
		{
			public void disconnect()
			{
				for(ObservableValue p: props)
				{
					p.removeListener(this);
				}
			}

			
			public void changed(ObservableValue p, Object oldValue, Object newValue)
			{
				onChange.run();
			}
		};
		
		items.add(li);
		
		for(ObservableValue p: props)
		{
			p.addListener(li);
		}
		
		if(fireImmediately)
		{
			onChange.run();
		}
		
		return li;
	}
	
	
	public <T> Disconnectable addChangeListener(ObservableValue<T> prop, ChangeListener<T> li)
	{
		return addChangeListener(prop, false, li);
	}
	
	
	public <T> Disconnectable addChangeListener(ObservableValue<T> prop, boolean fireImmediately, ChangeListener<T> li)
	{
		Disconnectable d = new Disconnectable()
		{
			public void disconnect()
			{
				prop.removeListener(li);
			}
		};
		
		items.add(d);
		prop.addListener(li);
		
		if(fireImmediately)
		{
			T v = prop.getValue();
			li.changed(prop, null, v);
		}
		
		return d;
	}


	public Disconnectable addWeakChangeListener(Runnable onChange, ObservableValue<?> ... props)
	{
		return addWeakChangeListener(onChange, false, props);
	}


	public Disconnectable addWeakChangeListener(Runnable onChange, boolean fireImmediately, ObservableValue<?> ... props)
	{
		ChLi li = new ChLi()
		{
			WeakReference<Runnable> ref = new WeakReference(onChange);


			public void disconnect()
			{
				for(ObservableValue p: props)
				{
					p.removeListener(this);
				}
			}


			public void changed(ObservableValue p, Object oldValue, Object newValue)
			{
				Runnable r = ref.get();
				if(r == null)
				{
					disconnect();
				}
				else
				{
					r.run();
				}
			}
		};

		items.add(li);

		for(ObservableValue p: props)
		{
			p.addListener(li);
		}

		if(fireImmediately)
		{
			onChange.run();
		}

		return li;
	}


	public <T> Disconnectable addWeakChangeListener(ObservableValue<T> prop, ChangeListener<T> li)
	{
		return addChangeListener(prop, false, li);
	}


	public <T> Disconnectable addWeakChangeListener(ObservableValue<T> prop, boolean fireImmediately, ChangeListener<T> listener)
	{
		ChLi<T> d = new ChLi<T>()
		{
			WeakReference<ChangeListener<T>> ref = new WeakReference<>(listener);


			public void disconnect()
			{
				prop.removeListener(this);
			}


			public void changed(ObservableValue<? extends T> p, T oldValue, T newValue)
			{
				ChangeListener<T> li = ref.get();
				if(li == null)
				{
					disconnect();
				}
				else
				{
					li.changed(p, oldValue, newValue);
				}
			}
		};

		items.add(d);
		prop.addListener(d);

		if(fireImmediately)
		{
			T v = prop.getValue();
			listener.changed(prop, null, v);
		}
		
		return d;
	}
	
	
	// invalidation listeners
	
	
	public Disconnectable addInvalidationListener(Runnable callback, ObservableValue<?> ... props)
	{
		return addInvalidationListener(callback, false, props);
	}
	

	public Disconnectable addInvalidationListener(Runnable onChange, boolean fireImmediately, ObservableValue<?> ... props)
	{
		InLi li = new InLi()
		{
			public void disconnect()
			{
				for(ObservableValue p: props)
				{
					p.removeListener(this);
				}
			}


			public void invalidated(Observable p)
			{
				onChange.run();
			}
		};
		
		items.add(li);
		
		for(ObservableValue p: props)
		{
			p.addListener(li);
		}
		
		if(fireImmediately)
		{
			onChange.run();
		}
		
		return li;
	}
	
	
	public <T> Disconnectable addInvalidationListener(ObservableValue<T> prop, InvalidationListener li)
	{
		return addInvalidationListener(prop, false, li);
	}
	
	
	public <T> Disconnectable addInvalidationListener(ObservableValue<T> prop, boolean fireImmediately, InvalidationListener li)
	{
		Disconnectable d = new Disconnectable()
		{
			public void disconnect()
			{
				prop.removeListener(li);
			}
		};
		
		items.add(d);
		prop.addListener(li);
		
		if(fireImmediately)
		{
			li.invalidated(prop);
		}
		
		return d;
	}


	public Disconnectable addWeakInvalidationListener(Runnable onChange, ObservableValue<?> ... props)
	{
		return addWeakInvalidationListener(onChange, false, props);
	}


	public Disconnectable addWeakInvalidationListener(Runnable onChange, boolean fireImmediately, ObservableValue<?> ... props)
	{
		InLi li = new InLi()
		{
			WeakReference<Runnable> ref = new WeakReference(onChange);


			public void disconnect()
			{
				for(ObservableValue p: props)
				{
					p.removeListener(this);
				}
			}


			public void invalidated(Observable p)
			{
				Runnable r = ref.get();
				if(r == null)
				{
					disconnect();
				}
				else
				{
					r.run();
				}
			}
		};

		items.add(li);

		for(ObservableValue p: props)
		{
			p.addListener(li);
		}

		if(fireImmediately)
		{
			onChange.run();
		}

		return li;
	}


	public Disconnectable addWeakInvalidationListener(ObservableValue<?> prop, InvalidationListener li)
	{
		return addWeakInvalidationListener(prop, false, li);
	}


	public Disconnectable addWeakInvalidationListener(ObservableValue<?> prop, boolean fireImmediately, InvalidationListener listener)
	{
		InLi d = new InLi()
		{
			WeakReference<InvalidationListener> ref = new WeakReference<>(listener);


			public void disconnect()
			{
				prop.removeListener(this);
			}


			public void invalidated(Observable p)
			{
				InvalidationListener li = ref.get();
				if(li == null)
				{
					disconnect();
				}
				else
				{
					li.invalidated(p);
				}
			}
		};

		items.add(d);
		prop.addListener(d);

		if(fireImmediately)
		{
			listener.invalidated(prop);
		}
		
		return d;
	}
	
	
	// list change listeners
	
	
	public <T> Disconnectable addListChangeListener(ObservableList<T> list, ListChangeListener<T> listener)
	{
		Disconnectable d = new Disconnectable()
		{
			public void disconnect()
			{
				list.removeListener(listener);
			}
		};
		
		items.add(d);
		list.addListener(listener);
		
		return d;
	}
	
	
	public <T> Disconnectable addWeakListChangeListener(ObservableList<T> list, ListChangeListener<T> listener)
	{
		LiChLi<T> li = new LiChLi<T>()
		{
			WeakReference<ListChangeListener<T>> ref = new WeakReference<>(listener);
			
			
			public void disconnect()
			{
				list.removeListener(this);
			}

			public void onChanged(Change<? extends T> ch)
			{
				ListChangeListener<T> li = ref.get();
				if(li == null)
				{
					disconnect();
				}
				else
				{
					li.onChanged(ch);
				}
			}
		};
		
		items.add(li);
		list.addListener(li);
		
		return li;
	}
	
	
	// TODO event handlers
	
	
	// TODO event filters
	
	
	//
	
	
	protected static abstract class ChLi<T> implements Disconnectable, ChangeListener<T> { }
	
	protected static abstract class InLi implements Disconnectable, InvalidationListener { }
	
	protected static abstract class LiChLi<T> implements Disconnectable, ListChangeListener<T> { }
}
