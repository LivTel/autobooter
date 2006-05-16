/*   
    Copyright 2006, Astrophysics Research Institute, Liverpool John Moores University.

    This file is part of AutoBooter.

    AutoBooter is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    AutoBooter is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with AutoBooter; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/
// AutoBooterStreamThread.java
// $Header: /home/cjm/cvs/autobooter/java/AutoBooterStreamThread.java,v 0.3 2006-05-16 16:20:24 cjm Exp $

import java.lang.*;
import java.io.*;
import java.util.*;

/**
 * This class extends thread. One instance of this class is spawned for each stream that needs
 * to be read by the program. Each process has an output and error stream that needs to be read.
 * The thread waits for input on the stream, and re-broadcasts it to the specified stream.
 * @author Chris Mottram
 * @version $Revision: 0.3 $
 */
public class AutoBooterStreamThread extends Thread
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: AutoBooterStreamThread.java,v 0.3 2006-05-16 16:20:24 cjm Exp $");
	/**
	 * The name of the command this stream thread is attached to.
	 */
	private String name = null;
	/**
	 * An object to send log messages to.
	 */
	private AutoBooterLogInterface logger = null;
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
	 * Method to set the log object instance.
	 * @param l An object reference implementing the log interface.
	 * @see #logger
	 */
	public void setLogger(AutoBooterLogInterface l)
	{
	       logger = l;
	}

	/**
	 * Run method, called when the thread is run. 
	 * Loops, reading bytes from the input stream and writing them to the output stream.
	 * @see #name
	 * @see #logger
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
			logger.error(this.getClass().getName()+":run:"+name,e);
		}
		logger.log(AutoBooterConstants.AUTOBOOTER_LOG_LEVEL_COMMANDS,this.getClass().getName()+":run:"+name+
			   ":Stream terminated.");
	}
}
//
// $Log: not supported by cvs2svn $
// Revision 0.2  2004/03/05 15:23:03  cjm
// Added logging.
//
// Revision 0.1  2000/07/06 10:27:03  cjm
// initial revision.
//
//

