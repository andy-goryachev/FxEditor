// Copyright © 2014-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicLong;


/** 
 * Generates (hopefully) globally unique identifiers.
 */
public class GUID256
{
	private static final int INITIAL_RANDOMNESS_BYTES = 32;
	private static byte[] machineID;
	private static AtomicLong seq = new AtomicLong();
	

	public static BKey generateKey()
	{
		return new BKey(generateBytes());
	}
	
	
	/** generates globally uniqueue byte array */
	public static byte[] generateBytes()
	{
		CDigest d = new CDigest.SHA256();
		d.update(machineID());
		d.update(System.currentTimeMillis());
		d.update(System.nanoTime());
		d.update(seq.incrementAndGet());
		return d.digest();
	}
	
	
	/** generates GUID as a hex string */
	public static String generateHexString()
	{
		return Hex.toHexString(generateBytes());
	}
	
	
	/** generates GUID as a decimal string */
	public static String generateDecimalString()
	{
		return new BigInteger(1, generateBytes()).toString();
	}
	
	
	private synchronized static byte[] machineID()
	{
		if(machineID == null)
		{
			// let's throw some degree of randomness
			byte[] b = new byte[INITIAL_RANDOMNESS_BYTES];
			new SecureRandom().nextBytes(b);
			
			CDigest d = new CDigest.SHA256();
			d.update(b);
			
			// java runtime
			Runtime r = Runtime.getRuntime();
			d.update(r.availableProcessors());
			d.update(r.freeMemory());
			d.update(r.hashCode());
			
			// machine-specific parameters
			d.update(System.getProperty("java.runtime.version"));
			d.update(System.getProperty("java.class.path"));
			d.update(System.getProperty("os.arch"));
			d.update(System.getProperty("os.name"));
			d.update(System.getProperty("os.version"));
			
			// user-specific parameters
			d.update(System.getProperty("user.country"));
			d.update(System.getProperty("user.dir"));
			d.update(System.getProperty("user.home"));
			d.update(System.getProperty("user.language"));
			d.update(System.getProperty("user.name"));
			d.update(System.getProperty("user.timezone"));
			
			machineID = d.digest();
		}
		return machineID;
	}
}
