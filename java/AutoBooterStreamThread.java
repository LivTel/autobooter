// AutoBooterStreamThread.java -*- mode: Fundamental;-*-
// $Header: /home/cjm/cvs/autobooter/java/AutoBooterStreamThread.java,v 0.1 2000-07-06 10:27:03 cjm Exp $

import java.lang.*;
import java.io.*;
import java.util.*;

/**
 * This class extends thread. One instance of this class is spawned for each stream that needs
 * to be read by the program. Each process has an output and error stream that needs to be read.
 * The thread waits for input on the stream, and re-broadcasts it to the specified stream.
 * @author Chris Mottram
 * @version $Revision: 0.1 $
 */
public class AutoBooterStreamThread extends Thread
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: AutoBooterStreamThread.java,v 0.1 2000-07-06 10:27:03 cjm Exp $");
	/**
	 * The name of the command this stream thread is attached to.
	 */
	private String name = null;
	/**
	 * The stream to rebroadcast.
	 */
	private InputStream inputStream = null;
	/**
	 * The stream to rebroadcast on.
	 */
	private OutputStream outputStream = null;

	/**
	 * Constructor.
	 * @param n The name of the command this stream thread is attached to.
	 * @param in The stream to rebroadcast.
	 * @param out The stream to rebroadcast on.
	 */
	public AutoBooterStreamThread(String n,InputStream in,OutputStream out)
	{
		name = n;
		inputStream = in;
		outputStream = out;
	}

	/**
	 * Run method, called when the thread is run. 
	 * Loops, reading bytes from the input stream and writing them to the output stream.
	 * @see #name
	 * @see #inputStream
	 * @see #outputStream
	 */
	public void run()
	{
		int c = 0;

		try
		{
			while((c = inputStream.read()) != -1)
			{
				outputStream.write(c);
			}
		}
		catch (IOException e)
		{
			System.err.println(this.getClass().getName()+":run:"+name+":"+e);
		}
		System.err.println(this.getClass().getName()+":run:"+name+":Stream terminated.");
	}
}
//
// $Log: not supported by cvs2svn $
//

