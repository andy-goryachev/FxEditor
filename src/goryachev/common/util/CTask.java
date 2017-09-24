// Copyright © 2017 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.util.function.Consumer;


/**
 * Common Task.
 */
public class CTask<T>
	implements Runnable
{
	protected final ValueGenerator<T> generator;
	protected Consumer<T> onSuccess;
	protected Consumer<Throwable> onError;
	protected Runnable onFinish;
	protected static ParallelExecutor exec = initExecutor();
	
	
	public CTask(ValueGenerator<T> generator)
	{
		this.generator = generator;
	}
	

	public CTask onError(Consumer<Throwable> onError)
	{
		this.onError = onError;
		return this;
	}
	
	
	public CTask onSuccess(Consumer<T> onSuccess)
	{
		this.onSuccess = onSuccess;
		return this;
	}
	
	
	public CTask onFinish(Runnable onFinish)
	{
		this.onFinish = onFinish;
		return this;
	}
	
	
	public void submit()
	{
		submit(this);
	}
	
	
	public static void submit(Runnable r)
	{
		exec.submit(r);
	}
	
	
	public void run()
	{
		try
		{
			T result = generator.generate();
			
			try
			{
				handleSuccess(result);
			}
			catch(Throwable e)
			{
				Log.ex(e);
			}
		}
		catch(Throwable e)
		{
			try
			{
				handleError(e);
			}
			catch(Throwable err)
			{
				Log.ex(e);
			}
		}
		
		try
		{
			handleFinish();
		}
		catch(Throwable e)
		{
			Log.ex(e);
		}
	}
	
	
	private static ParallelExecutor initExecutor()
	{
		ParallelExecutor ex = new ParallelExecutor("CTask.Executor", 5);
		return ex;
	}

	
	protected void handleSuccess(T result)
	{
		Consumer<T> c = onSuccess;
		if(c != null)
		{
			c.accept(result);
		}
	}
	
	
	protected void handleError(Throwable e)
	{
		Consumer<Throwable> c = onError;
		if(c != null)
		{
			c.accept(e);
		}
	}
	
	
	protected void handleFinish()
	{
		Runnable r = onFinish;
		if(r != null)
		{
			r.run();
		}
	}
}
