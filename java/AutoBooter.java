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
// AutoBooter.java
// $Header: /home/cjm/cvs/autobooter/java/AutoBooter.java,v 0.5 2006-05-16 16:20:20 cjm Exp $

import java.lang.*;
import java.io.*;
import java.text.*;
import java.util.*;

import ngat.util.*;
import ngat.util.logging.*;

/**
 * This class is the start point for the AutoBooter program.
 * This program is designed to be booted at startup, and starts a set of process which it
 * monitors and re-starts.
 * @author Chris Mottram
 * @version $Revision: 0.5 $
 */
public class AutoBooter implements AutoBooterLogInterface
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: AutoBooter.java,v 0.5 2006-05-16 16:20:20 cjm Exp $");
	/**
	 * Filename of the configuration property file used by the program.
	 * Initialised to DEFAULT_CONFIGURATION_FILENAME, can be set as a command line argument.
	 * @see AutoBooterStatus#DEFAULT_CONFIGURATION_FILENAME
	 * @see #parseArgs
	 */
	private String configurationFilename = AutoBooterStatus.DEFAULT_CONFIGURATION_FILENAME;
	/**
	 * A list of threads spawned to keep track of processes.
	 */
	private Vector threadList = null;
	/**
	 * The status object, that controls the properies loaded from file.
	 */
	private AutoBooterStatus status = null;
	/**
	 * The logging logger.
	 */
	protected Logger logLogger = null;
	/**
	 * The error logger.
	 */
	protected Logger errorLogger = null;
	/**
	 * The filter used to filter messages sent to the logging logger.
	 * @see #logLogger
	 */
	protected BitFieldLogFilter logFilter = null;

	/**
	 * Initialisation method for the program. Does some startup. Loads the configuration file
	 * into the status object. Initialises the loggers.
	 * @exception FileNotFoundException Thrown if the configuration file is not found.
	 * @exception IOException Thrown if the configuration file fails to load.
	 * @see #configurationFilename
	 * @see #status
	 * @see #initLoggers
	 * @see AutoBooterStatus#load
	 */
	public void init() throws FileNotFoundException, IOException
	{
		status = new AutoBooterStatus();

		status.load(configurationFilename);
	// Logging
		initLoggers();
	}

	/**
	 * Initialise log handlers. Called from init only, not re-configured on a REDATUM level reboot.
	 * @see #init
	 * @see #initLogHandlers
	 * @see #errorLogger
	 * @see #logLogger
	 * @see #logFilter
	 */
	protected void initLoggers()
	{
	// errorLogger setup
		errorLogger = LogManager.getLogger("error");
		initLogHandlers(errorLogger);
		errorLogger.setLogLevel(Logging.ALL);
	// logLogger setup
		logLogger = LogManager.getLogger("log");
		initLogHandlers(logLogger);
		logLogger.setLogLevel(Logging.ALL);
		logFilter = new BitFieldLogFilter(status.getLogLevel());
		logLogger.setFilter(logFilter);
	}

	/**
	 * Method to create and add all the handlers for the specified logger.
	 * These handlers are in the status properties:
	 * "autobooter.log."+l.getName()+".handler."+index+".name" retrieves the relevant class name
	 * for each handler.
	 * @param l The logger.
	 * @see AutoBooterStatus#getProperty
	 * @see #initFileLogHandler
	 * @see #initConsoleLogHandler
	 */
	protected void initLogHandlers(Logger l)
	{
		LogHandler handler = null;
		String handlerName = null;
		int index = 0;

		do
		{
			handlerName = status.getProperty("autobooter.log."+l.getName()+".handler."+index+".name");
			if(handlerName != null)
			{
				try
				{
					handler = null;
					if(handlerName.equals("ngat.util.logging.FileLogHandler"))
					{
						handler = initFileLogHandler(l,index);
					}
					else if(handlerName.equals("ngat.util.logging.ConsoleLogHandler"))
					{
						handler = initConsoleLogHandler(l,index);
					}
					else if(handlerName.equals("ngat.util.logging.MulticastLogRelay"))
					{
						handler = initMulticastLogRelay(l,index);
					}
					else
					{
						error("initLogHandlers:Unknown handler:"+handlerName);
					}
					if(handler != null)
					{
						handler.setLogLevel(Logging.ALL);
						l.addHandler(handler);
					}
				}
				catch(Exception e)
				{
					error("initLogHandlers:Adding Handler failed:",e);
				}
				index++;
			}
		}
		while(handlerName != null);
	}

	/**
	 * Routine to add a FileLogHandler to the specified logger.
	 * This method expects either 3 or 6 constructor parameters to be in the status properties.
	 * If there are 6 parameters, we create a record limited file log handler with parameters:
	 * <ul>
	 * <li><b>param.0</b> is the filename.
	 * <li><b>param.1</b> is the formatter class name.
	 * <li><b>param.2</b> is the record limit in each file.
	 * <li><b>param.3</b> is the start index for file suffixes.
	 * <li><b>param.4</b> is the end index for file suffixes.
	 * <li><b>param.5</b> is a boolean saying whether to append to files.
	 * </ul>
	 * If there are 3 parameters, we create a time period file log handler with parameters:
	 * <ul>
	 * <li><b>param.0</b> is the filename.
	 * <li><b>param.1</b> is the formatter class name.
	 * <li><b>param.2</b> is the time period, either 'HOURLY_ROTATION','DAILY_ROTATION' or 'WEEKLY_ROTATION'.
	 * </ul>
	 * @param l The logger to add the handler to.
	 * @param index The index in the property file of the handler we are adding.
	 * @return A LogHandler of the relevant class is returned, if no exception occurs.
	 * @exception NumberFormatException Thrown if the numeric parameters in the properties
	 * 	file are not valid numbers.
	 * @exception FileNotFoundException Thrown if the specified filename is not valid in some way.
	 * @exception NullPointerException Thrown if a property value is null.
	 * @exception IllegalArgumentException Thrown if a property value is not legal.
	 * @see #status
	 * @see #initLogFormatter
	 * @see AutoBooterStatus#getProperty
	 * @see AutoBooterStatus#getPropertyInteger
	 * @see AutoBooterStatus#getPropertyBoolean
	 * @see AutoBooterStatus#propertyContainsKey
	 * @see AutoBooterStatus#getPropertyLogHandlerTimePeriod
	 */
	protected LogHandler initFileLogHandler(Logger l,int index) throws NumberFormatException,
	          FileNotFoundException, NullPointerException, IllegalArgumentException
	{
		LogFormatter formatter = null;
		LogHandler handler = null;
		String fileName;
		int recordLimit,fileStart,fileLimit,timePeriod;
		boolean append;

		fileName = status.getProperty("autobooter.log."+l.getName()+".handler."+index+".param.0");
		formatter = initLogFormatter("autobooter.log."+l.getName()+".handler."+index+".param.1");
		// if we have more then 3 parameters, we are using a recordLimit FileLogHandler
		// rather than a time period log handler.
		if(status.propertyContainsKey("autobooter.log."+l.getName()+".handler."+index+".param.3"))
		{
			recordLimit = status.getPropertyInteger("autobooter.log."+l.getName()+".handler."+
								index+".param.2");
			fileStart = status.getPropertyInteger("autobooter.log."+l.getName()+".handler."+
							      index+".param.3");
			fileLimit = status.getPropertyInteger("autobooter.log."+l.getName()+".handler."+
							      index+".param.4");
			append = status.getPropertyBoolean("autobooter.log."+l.getName()+".handler."+index+".param.5");
			handler = new FileLogHandler(fileName,formatter,recordLimit,fileStart,fileLimit,append);
		}
		else
		{
			// This is a time period log handler.
			timePeriod = status.getPropertyLogHandlerTimePeriod("autobooter.log."+l.getName()+".handler."+
									    index+".param.2");
			handler = new FileLogHandler(fileName,formatter,timePeriod);
		}
		return handler;
	}

	/**
	 * Routine to add a MulticastLogRelay to the specified logger.
	 * The parameters to the constructor are stored in the status properties:
	 * <ul>
	 * <li>param.0 is the multicast group name i.e. "228.0.0.1".
	 * <li>param.1 is the port number i.e. 5000.
	 * </ul>
	 * @param l The logger to add the handler to.
	 * @param index The index in the property file of the handler we are adding.
	 * @return A LogHandler of the relevant class is returned, if no exception occurs.
	 * @exception IOException Thrown if the multicast socket cannot be created for some reason.
	 */
	protected LogHandler initMulticastLogRelay(Logger l,int index) throws IOException
	{
		LogHandler handler = null;
		String groupName = null;
		int portNumber;

		groupName = status.getProperty("autobooter.log."+l.getName()+".handler."+index+".param.0");
		portNumber = status.getPropertyInteger("autobooter.log."+l.getName()+".handler."+index+".param.1");
		handler = new MulticastLogRelay(groupName,portNumber);
		return handler;
	}

	/**
	 * Routine to add a ConsoleLogHandler to the specified logger.
	 * The parameters to the constructor are stored in the status properties:
	 * <ul>
	 * <li>param.0 is the formatter class name.
	 * </ul>
	 * @param l The logger to add the handler to.
	 * @param index The index in the property file of the handler we are adding.
	 * @return A LogHandler of class FileLogHandler is returned, if no exception occurs.
	 */
	protected LogHandler initConsoleLogHandler(Logger l,int index)
	{
		LogFormatter formatter = null;
		LogHandler handler = null;

		formatter = initLogFormatter("autobooter.log."+l.getName()+".handler."+index+".param.0");
		handler = new ConsoleLogHandler(formatter);
		return handler;
	}

	/**
	 * Method to create an instance of a LogFormatter, given a property name
	 * to retrieve it's details from. If the property does not exist, or the class does not exist
	 * or an instance cannot be instansiated we try to return a ngat.util.logging.BogstanLogFormatter.
	 * @param propertyName A property name, present in the status's properties, 
	 * 	which has a value of a valid LogFormatter sub-class name. i.e.
	 * 	<pre>autobooter.log.log.handler.0.param.1 =ngat.util.logging.BogstanLogFormatter</pre>
	 * @return An instance of LogFormatter is returned.
	 */
	protected LogFormatter initLogFormatter(String propertyName)
	{
		LogFormatter formatter = null;
		String formatterName = null;
		Class formatterClass = null;

		formatterName = status.getProperty(propertyName);
		if(formatterName == null)
		{
			error("initLogFormatter:NULL formatter for:"+propertyName);
			formatterName = "ngat.util.logging.BogstanLogFormatter";
		}
		try
		{
			formatterClass = Class.forName(formatterName);
		}
		catch(ClassNotFoundException e)
		{
			error("initLogFormatter:Unknown class formatter:"+formatterName+
				" from property "+propertyName);
			formatterClass = BogstanLogFormatter.class;
		}
		try
		{
			formatter = (LogFormatter)formatterClass.newInstance();
		}
		catch(Exception e)
		{
			error("initLogFormatter:Cannot create instance of formatter:"+formatterName+
				" from property "+propertyName);
			formatter = (LogFormatter)new BogstanLogFormatter();
		}
	// set better date format if formatter allows this.
	// Note we really need LogFormatter to generically allow us to do this
		if(formatter instanceof BogstanLogFormatter)
		{
			BogstanLogFormatter blf = (BogstanLogFormatter)formatter;

			blf.setDateFormat(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS z"));
		}
		if(formatter instanceof SimpleLogFormatter)
		{
			SimpleLogFormatter slf = (SimpleLogFormatter)formatter;

			slf.setDateFormat(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS z"));
		}
		return formatter;
	}

	/**
	 * Routine to write the string to the relevant logger. If the relevant logger has not been
	 * created yet the error gets written to System.out.
	 * @param level The level of logging this message belongs to.
	 * @param s The string to write.
	 * @see #logLogger
	 */
	public void log(int level,String s)
	{
		if(logLogger != null)
			logLogger.log(level,s);
		else
		{
			if((status.getLogLevel()&level) > 0)
				System.out.println(s);
		}
	}

	/**
	 * Routine to write the string to the relevant logger. If the relevant logger has not been
	 * created yet the error gets written to System.err.
	 * @param s The string to write.
	 * @see #errorLogger
	 */
	public void error(String s)
	{
		if(errorLogger != null)
			errorLogger.log(AutoBooterConstants.AUTOBOOTER_LOG_LEVEL_ERROR,s);
		else
			System.err.println(s);
	}

	/**
	 * Routine to write the string to the relevant logger. If the relevant logger has not been
	 * created yet the error gets written to System.err.
	 * @param s The string to write.
	 * @param e An exception that caused the error to occur.
	 * @see #errorLogger
	 */
	public void error(String s,Exception e)
	{
		if(errorLogger != null)
		{
			errorLogger.log(AutoBooterConstants.AUTOBOOTER_LOG_LEVEL_ERROR,s,e);
			errorLogger.dumpStack(AutoBooterConstants.AUTOBOOTER_LOG_LEVEL_ERROR,e);
		}
		else
			System.err.println(s+e);
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
		log(AutoBooterConstants.AUTOBOOTER_LOG_LEVEL_COMMANDS,this.getClass().getName()+
		    ":run:started using configuration:"+configurationFilename);
		while(done == false)
		{
			processName = status.getProperty("autobooter.process.name."+i);
			if(processName != null)
			{
				processDirectory = status.getProperty("autobooter.process.directory."+i);
				processCommandLine = status.getProperty("autobooter.process.command_line."+i);
				processRetryCount = status.getPropertyInteger("autobooter.process.retry_count."+i);

				thread = new AutoBooterProcessThread(processName,i);
				thread.setLogger(this);
				thread.setStatus(status);
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
	 * Get status instance.
	 * @return The status instance.
	 * @see #status
	 */
	public AutoBooterStatus getStatus()
	{
		return status;
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
// Revision 0.4  2004/03/05 15:23:03  cjm
// Added logging.
// Removed getProperty methods and put into Status object.
//
// Revision 0.3  2000/08/01 11:04:29  cjm
// Added startup log message.
//
// Revision 0.2  2000/07/06 13:50:56  cjm
// *** empty log message ***
//
// Revision 0.1  2000/07/06 10:26:21  cjm
// initial revision.
//
//
