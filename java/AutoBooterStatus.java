// AutoBooterStatus.java
// $Header: /home/cjm/cvs/autobooter/java/AutoBooterStatus.java,v 1.1 2004-03-05 15:23:03 cjm Exp $
import java.lang.*;
import java.io.*;
import java.util.*;

import ngat.util.logging.FileLogHandler;

/**
 * This class holds status information for the Autobooter program.
 * @author Chris Mottram
 * @version $Revision: 1.1 $
 * @see AutoBooterProcessStatusInterface
 */
public class AutoBooterStatus implements AutoBooterProcessStatusInterface
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: AutoBooterStatus.java,v 1.1 2004-03-05 15:23:03 cjm Exp $");
	/**
	 * Defaults filename for the configuration property file.
	 */
	public final static String DEFAULT_CONFIGURATION_FILENAME = "/space/home/dev/bin/autobooter/java/autobooter.properties";
	/**
	 * The logging level. Defaults to all.
	 * @see AutoBooterConstants#AUTOBOOTER_LOG_LEVEL_ALL
	 */
	private int logLevel = AutoBooterConstants.AUTOBOOTER_LOG_LEVEL_ALL;
	/**
	 * A list of properties held in the properties file. This contains configuration information in the
	 * program.
	 */
	private Properties properties = null;

	/**
	 * Default constructor. Initialises the properties.
	 * @see #properties
	 */
	public AutoBooterStatus()
	{
		super();
		properties = new Properties();
	}

	/**
	 * The load method for the class. This loads the property file from disc, using the specified
	 * filename. Any old properties are first cleared.
	 * @param filename The filename of a Java property file containing general configuration for the Autobooter.
	 * 	If filename is null, DEFAULT_PROPERTY_FILE_NAME is used.
	 * @see #properties
	 * @see #DEFAULT_CONFIGURATION_FILENAME
	 * @exception FileNotFoundException Thrown if a configuration file is not found.
	 * @exception IOException Thrown if an IO error occurs whilst loading a configuration file.
	 */
	public void load(String filename) throws FileNotFoundException, IOException
	{
		FileInputStream fileInputStream = null;

	// clear old properties
		properties.clear();
	// normal properties load
		if(filename == null)
			filename = DEFAULT_CONFIGURATION_FILENAME;
		fileInputStream = new FileInputStream(filename);
		properties.load(fileInputStream);
		fileInputStream.close();
	}

	/**
	 * Set the logging level for Autobooter.
	 * @param level The level of logging.
	 */
	public synchronized void setLogLevel(int level)
	{
		logLevel = level;
	}

	/**
	 * Get the logging level for Autobooter.
	 * @return The current log level.
	 */	
	public synchronized int getLogLevel()
	{
		return logLevel;
	}

	/**
	 * Method to return whether the loaded properties contain the specified keyword.
	 * Calls the proprties object containsKey method. Note assumes the properties object has been initialised.
	 * @param p The property key we wish to test exists.
	 * @return The method returnd true if the specified key is a key in out list of properties,
	 *         otherwise it returns false.
	 * @see #properties
	 */
	public boolean propertyContainsKey(String p)
	{
		return properties.containsKey(p);
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
		try
		{
			returnValue = Integer.parseInt(valueString);
		}
		catch(NumberFormatException e)
		{
			// re-throw exception with more information e.g. keyword
			throw new NumberFormatException(this.getClass().getName()+":getPropertyInteger:keyword:"+
				p+":valueString:"+valueString);
		}
		return returnValue;
	}

	/**
	 * Routine to get a properties value, given a key. The value must be a valid long, else a 
	 * NumberFormatException is thrown.
	 * @param p The property key we want the value for.
	 * @return The properties value, as a long.
	 * @exception NumberFormatException If the properties value string is not a valid long, this
	 * 	exception will be thrown when the Long.parseLong routine is called.
	 * @see #properties
	 */
	public long getPropertyLong(String p) throws NumberFormatException
	{
		String valueString = null;
		long returnValue = 0;

		valueString = properties.getProperty(p);
		try
		{
			returnValue = Long.parseLong(valueString);
		}
		catch(NumberFormatException e)
		{
			// re-throw exception with more information e.g. keyword
			throw new NumberFormatException(this.getClass().getName()+":getPropertyLong:keyword:"+
				p+":valueString:"+valueString);
		}
		return returnValue;
	}

	/**
	 * Routine to get a properties value, given a key. The value must be a valid double, else a 
	 * NumberFormatException is thrown.
	 * @param p The property key we want the value for.
	 * @return The properties value, as an double.
	 * @exception NumberFormatException If the properties value string is not a valid double, this
	 * 	exception will be thrown when the Double.valueOf routine is called.
	 * @see #properties
	 */
	public double getPropertyDouble(String p) throws NumberFormatException
	{
		String valueString = null;
		Double returnValue = null;

		valueString = properties.getProperty(p);
		try
		{
			returnValue = Double.valueOf(valueString);
		}
		catch(NumberFormatException e)
		{
			// re-throw exception with more information e.g. keyword
			throw new NumberFormatException(this.getClass().getName()+":getPropertyDouble:keyword:"+
				p+":valueString:"+valueString);
		}
		return returnValue.doubleValue();
	}

	/**
	 * Routine to get a properties boolean value, given a key. The properties value should be either 
	 * "true" or "false".
	 * Boolean.valueOf is used to convert the string to a boolean value.
	 * @param p The property key we want the boolean value for.
	 * @return The properties value, as an boolean.
	 * @exception NullPointerException If the properties value string is null, this
	 * 	exception will be thrown.
	 * @see #properties
	 */
	public boolean getPropertyBoolean(String p) throws NullPointerException
	{
		String valueString = null;
		Boolean b = null;

		valueString = properties.getProperty(p);
		if(valueString == null)
		{
			throw new NullPointerException(this.getClass().getName()+":getPropertyBoolean:keyword:"+
				p+":Value was null.");
		}
		b = Boolean.valueOf(valueString);
		return b.booleanValue();
	}

	/**
	 * Routine to get an integer representing a ngat.util.logging.FileLogHandler time period.
	 * The value of the specified property should contain either:'HOURLY_ROTATION', 'DAILY_ROTATION' or
	 * 'WEEKLY_ROTATION'.
	 * @param p The property key we want the time period value for.
	 * @return The properties value, as an FileLogHandler time period (actually an integer).
	 * @exception NullPointerException If the properties value string is null an exception is thrown.
	 * @exception IllegalArgumentException If the properties value string is not a valid time period,
	 *            an exception is thrown.
	 * @see #properties
	 */
	public int getPropertyLogHandlerTimePeriod(String p) throws NullPointerException, IllegalArgumentException
	{
		String valueString = null;
		int timePeriod = 0;
 
		valueString = properties.getProperty(p);
		if(valueString == null)
		{
			throw new NullPointerException(this.getClass().getName()+
						       ":getPropertyLogHandlerTimePeriod:keyword:"+
						       p+":Value was null.");
		}
		if(valueString.equals("HOURLY_ROTATION"))
			timePeriod = FileLogHandler.HOURLY_ROTATION;
		else if(valueString.equals("DAILY_ROTATION"))
			timePeriod = FileLogHandler.DAILY_ROTATION;
		else if(valueString.equals("WEEKLY_ROTATION"))
			timePeriod = FileLogHandler.WEEKLY_ROTATION;
		else
		{
			throw new IllegalArgumentException(this.getClass().getName()+
							   ":getPropertyLogHandlerTimePeriod:keyword:"+
							   p+":Illegal value:"+valueString+".");
		}
		return timePeriod;
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

}
//
// $Log: not supported by cvs2svn $
//
