// AutoBooter.java -*- mode: Fundamental;-*-
// $Header: /home/cjm/cvs/autobooter/java/AutoBooter.java,v 0.3 2000-08-01 11:04:29 cjm Exp $

import java.lang.*;
import java.io.*;
import java.util.*;

/**
 * This class is the start point for the AutoBooter program.
 * This program is designed to be booted at startup, and starts a set of process which it
 * monitors and re-starts.
 * @author Chris Mottram
 * @version $Revision: 0.3 $
 */
public class AutoBooter implements AutoBooterProcessParentInterface
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: AutoBooter.java,v 0.3 2000-08-01 11:04:29 cjm Exp $");
	/**
	 * Defaults filename for the configuration property file.
	 */
	public final static String DEFAULT_CONFIGURATION_FILENAME = "/space/home/dev/bin/autobooter/java/autobooter.properties";
	/**
	 * Filename of the configuration property file used by the program.
	 * Initialised to DEFAULT_CONFIGURATION_FILENAME, can be set as a command line argument.
	 * @see #DEFAULT_CONFIGURATION_FILENAME
	 * @see #parseArgs
	 */
	private String configurationFilename = DEFAULT_CONFIGURATION_FILENAME;
	/**
	 * A list of properties held in the properties file. This contains configuration information in the
	 * program.
	 */
	private Properties properties = null;
	/**
	 * A list of threads spawned to keep track of processes.
	 */
	private Vector threadList = null;

	/**
	 * Initialisation method for the program. Does some startup. Loads the configuration file
	 * into the properties.
	 * @exception FileNotFoundException Thrown if the configuration file is not found.
	 * @exception IOException Thrown if the configuration file fails to load.
	 * @see #configurationFilename
	 * @see #properties
	 */
	public void init() throws FileNotFoundException, IOException
	{
		FileInputStream fileInputStream = null;

		properties = new Properties();
		fileInputStream = new FileInputStream(configurationFilename);
		properties.load(fileInputStream);
		fileInputStream.close();
	}

	/**
	 * Run method for the program. Starts the nessassary threads to monitor each process
	 * that is specified to be re-spawned.
	 * The proerties contain the details of the processes to be started.
	 * The started threads are put in the threadList.
	 * @see #properties
	 * @see #threadList
	 */
	public void run()
	{
		int i;
		boolean done;
		String processName = null;
		String processDirectory = null;
		String processCommandLine = null;
		int processRetryCount = 0;
		AutoBooterProcessThread thread = null;

		i = 0;
		done = false;
		threadList = new Vector();
		System.out.println(this.getClass().getName()+":run:started using configuration:"+
			configurationFilename);
		while(done == false)
		{
			processName = getProperty("autobooter.process.name."+i);
			if(processName != null)
			{
				processDirectory = getProperty("autobooter.process.directory."+i);
				processCommandLine = getProperty("autobooter.process.command_line."+i);
				processRetryCount = getPropertyInteger("autobooter.process.retry_count."+i);

				thread = new AutoBooterProcessThread(processName,i);
				thread.setParent(this);
				thread.setDirectory(processDirectory);
				thread.setCommandLine(processCommandLine);
				thread.setRetryCount(processRetryCount);
				thread.start();
				threadList.add(thread);
			}
			else
				done = true;
			i++;
		}// end while(done == false)
	}

	/**
	 * Routine to return the status the process an instance of AutoBooterProcessThread is controlling
	 * will return when it needs re-spawning.
	 * @return A status, returned from the process under control, that means the thread can re-spawn
	 * 	the process.
	 */
	public int getReSpawnStatus()
	{
		int retval;

		try
		{
			retval = getPropertyInteger("autobooter.process.status.respawn");
		}
		catch(NumberFormatException e)
		{
			System.err.println(this.getClass().getName()+":getReSpawnStatus:Property not found.");
			retval = 0;
		}
		return retval;
	}

	/**
	 * Routine to return the status the process an instance of AutoBooterProcessThread is controlling
	 * will return when it wants to go into engineering mode. This means the AutoBooterProcessThread for
	 * this process will terminate, and not re-start the process.
	 * @return A status, returned from the process under control, that means the thread should <b>stop</b>
	 * 	re-spawning the process.
	 */
	public int getEngineeringStatus()
	{
		int retval;

		try
		{
			retval = getPropertyInteger("autobooter.process.status.engineering");
		}
		catch(NumberFormatException e)
		{
			System.err.println(this.getClass().getName()+":getEngineeringStatus:Property not found.");
			retval = 15;
		}
		return retval;
	}

	/**
	 * Routine to return the retry time. If the instance of AutoBooterProcessThread respawns the
	 * process it is controlling more than retry_count times in this time, the thread assumes the
	 * the process is continually failing and stops trying to re-start it.
	 * @return A time, in milliseconds.
	 */
	public int getRetryTime()
	{
		int retval;

		try
		{
			retval = getPropertyInteger("autobooter.process.retry_time");
		}
		catch(NumberFormatException e)
		{
			System.err.println(this.getClass().getName()+":getRetryTime:Property not found.");
			retval = 300000;
		}
		return retval;
	}

	/**
	 * Method to parse command line arguments.
	 * Note the '-help' arguments causes a help message to be printed, and the program to terminate.
	 * @param args The command line arguments passed to the program.
	 * @see #configurationFilename
	 * @see #help
	 */
	public void parseArgs(String[] args)
	{
		for(int i = 0; i < args.length;i++)
		{
			if(args[i].equals("-co")||args[i].equals("-config"))
			{
				if((i+1)< args.length)
				{
					configurationFilename = new String(args[i+1]);
					i++;
				}
				else
					System.err.println("-config requires a filename");
			}
			else if(args[i].equals("-h")||args[i].equals("-help"))
			{
				help();
				System.exit(0);
			}
			else
				System.err.println(this.getClass().getName()+" '"+args[i]+"' not a recognised option");
		}// end for
	}

	/**
	 * Help message routine. Prints all command line arguments.
	 */
	public void help()
	{
		System.out.println(this.getClass().getName()+" Help:");
		System.out.println("AutoBooter starts a series of processes specified in a configuration file.");
		System.out.println("It re-spawns them when they terminate.");
		System.out.println("Arguments are:");
		System.out.println("\t-[co]|[config] <filename> - Location of the configuration properties file.");
		System.out.println("\t-h[elp] - show this message.");
	}

	/**
	 * Routine to get a properties value, given a key. Just calls the properties object getProperty routine.
	 * @param p The property key we want the value for.
	 * @return The properties value, as a string object.
	 * @see #properties
	 */
	public String getProperty(String p)
	{
		return properties.getProperty(p);
	}

	/**
	 * Routine to get a properties value, given a key. The value must be a valid integer, else a 
	 * NumberFormatException is thrown.
	 * @param p The property key we want the value for.
	 * @return The properties value, as an integer.
	 * @exception NumberFormatException If the properties value string is not a valid integer, this
	 * 	exception will be thrown when the Integer.parseInt routine is called.
	 * @see #properties
	 */
	public int getPropertyInteger(String p) throws NumberFormatException
	{
		String valueString = null;
		int returnValue = 0;

		valueString = properties.getProperty(p);
		returnValue = Integer.parseInt(valueString);
		return returnValue;
	}

	/**
	 * Main routine, entry point to AutoBooter program.
	 * Calls the following methods:
	 * <ul>
	 * <li>parseArgs.
	 * <li>init.
	 * <li>run.
	 * </ul>
	 * @param args Command line arguments passed to the program.
	 * @see #parseArgs
	 * @see #init
	 * @see #run
	 */
	public static void main(String[] args)
	{
		AutoBooter ab = new AutoBooter();

		ab.parseArgs(args);
		try
		{
			ab.init();
		}
		catch(Exception e)
		{
			System.err.println("AutoBooter init failed:"+e);
			System.exit(1);
		}
		ab.run();
	}
}
//
// $Log: not supported by cvs2svn $
// Revision 0.2  2000/07/06 13:50:56  cjm
// *** empty log message ***
//
// Revision 0.1  2000/07/06 10:26:21  cjm
// initial revision.
//
//
