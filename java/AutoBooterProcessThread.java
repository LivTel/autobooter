// AutoBooterProcessThread.java
// $Header: /home/cjm/cvs/autobooter/java/AutoBooterProcessThread.java,v 0.3 2004-03-05 15:23:03 cjm Exp $

import java.lang.*;
import java.io.*;
import java.util.*;

/**
 * This class extends thread. One instance of this class is spawned for each process
 * the program controls. This thread starts the process, and monitors for it's completion.
 * It re-spawns the process until the retry-count is exhausted in the retry-time, or the
 * process returns an engineering return status.
 * @author Chris Mottram
 * @version $Revision: 0.3 $
 */
public class AutoBooterProcessThread extends Thread
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: AutoBooterProcessThread.java,v 0.3 2004-03-05 15:23:03 cjm Exp $");
	/**
	 * The name of the process this thread is controlling.
	 */
	private String name = null;
	/**
	 * The process number of the thread we are controlling. 
	 */
	private int processNumber = 0;
	/**
	 * The class containing status information this class needs.
	 * @see AutoBooterProcessStatusInterface
	 */
	private AutoBooterProcessStatusInterface status = null;
	/**
	 * An object reference implementing a log interface.
	 * @see AutoBooterLogInterface
	 */
	private AutoBooterLogInterface logger = null;
	/**
	 * The directory to run the command from.
	 */
	private String directory = null;
	/**
	 * The command line to start the process.
	 */
	private String commandLine = null;
	/**
	 * Number of times to try to start the process in the retry-time, before giving up.
	 */
	private int retryCount = 0;
	/**
	 * Thread handling the output stream from the started process.
	 */
	private AutoBooterStreamThread outputStreamThread = null;
	/**
	 * Thread handling the error stream from the started process.
	 */
	private AutoBooterStreamThread errorStreamThread = null;

	/**
	 * Constructor. Sets the name and process number.
	 */
	public AutoBooterProcessThread(String name,int processNumber)
	{
		super(name);
		this.name = name;
		this.processNumber = processNumber;
	}

	/**
	 * Method to set the number of the process being controlled by this thread.
	 * @param pn The number to set.
	 */
	public void setProcessNumber(int pn)
	{
		this.processNumber = pn;
	}

	/**
	 * Method to set the status object instance.
	 * @param s The status object reference.
	 */
	public void setStatus(AutoBooterProcessStatusInterface s)
	{
		status = s;
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
	 * Method to set the directory to run the command from.
	 * @param d The directory string.
	 */
	public void setDirectory(String d)
	{
		directory = d;
	}

	/**
	 * Method to set the command line to start the process.
	 * @param cl The command line string.
	 */
	public void setCommandLine(String cl)
	{
		commandLine = cl;
	}

	/**
	 * Method to set the retry count.
	 * @param rc The retry count to set.
	 */
	public void setRetryCount(int rc)
	{
		retryCount = rc;
	}

	/**
	 * Run method called when the thread starts.
	 * This runs a loop, which keeps starting the specified command in the specified directory,
	 * and waits for process termination. If the process terminates with an engineering status the loop is
	 * exited. If the process terminates with an error status (not a respawn status) 
	 * more than retryCount times in retryTime
	 * (an autobooter default) the loop terminates (assumes the process has some terminal error).
	 * An AutoBooterStreamThread is started for the process's output and error streams, which redirects these
	 * streams to AutoBooter's System.out and System.err respectively.
	 * @see #directory
	 * @see #commandLine
	 * @see #outputStreamThread
	 * @see #errorStreamThread
	 * @see #retryCount
	 * @see #status
	 * @see AutoBooterStreamThread
	 * @see AutoBooterProcessStatusInterface#getRetryTime
	 * @see AutoBooterProcessStatusInterface#getReSpawnStatus
	 * @see AutoBooterProcessStatusInterface#getEngineeringStatus
	 */
	public void run()
	{
		Process process;
		Runtime runtime;
		String[] commandList = null;
		int processStatus,retryIndex;
		long retryStartTime;
		long endTime;
		boolean done;

		done = false;
		processStatus = 0;
		retryIndex = 0;
		retryStartTime = System.currentTimeMillis();
		endTime = 0;
		runtime = Runtime.getRuntime();
		commandList = new String[3];
		commandList[0] = "/bin/sh";
		commandList[1] = "-c";
		commandList[2] = new String("( cd "+directory+" ; "+commandLine+" )");
		while(!done)
		{
			try
			{
				logger.log(AutoBooterConstants.AUTOBOOTER_LOG_LEVEL_COMMANDS,
					   this.getClass().getName()+":run:"+name+" started:try "+retryIndex+
					   " of "+retryCount+":\n\t"+
					commandList[0]+" "+commandList[1]+" "+commandList[2]);
				process = runtime.exec(commandList);
				outputStreamThread = new AutoBooterStreamThread(name,process.getInputStream(),
					System.out);
				outputStreamThread.setLogger(logger);
				outputStreamThread.start();
				errorStreamThread = new AutoBooterStreamThread(name,process.getErrorStream(),
					System.err);
				errorStreamThread.setLogger(logger);
				errorStreamThread.start();
				processStatus = process.waitFor();
				endTime = System.currentTimeMillis();
				logger.log(AutoBooterConstants.AUTOBOOTER_LOG_LEVEL_COMMANDS,this.getClass().getName()+
					   ":run:"+name+" returned "+processStatus+".");
			}
			catch(Exception e)
			{
				logger.error(this.getClass().getName()+":run:"+name,e);
				processStatus = 0;
			}
			finally
			{
				if(status.getReSpawnStatus() != processStatus)
					retryIndex++;
			// we keep running out of memory due to threads being spawned.
			// This tries to reclaim memory.
				System.gc();
			}
			if((endTime-retryStartTime) > status.getRetryTime())
			{
				logger.log(AutoBooterConstants.AUTOBOOTER_LOG_LEVEL_COMMANDS,this.getClass().getName()+
					   ":run:"+name+":Retry Count Reset:"+retryIndex);
				retryIndex = 0;
				retryStartTime = System.currentTimeMillis();
			}
			if(retryIndex > retryCount)
			{
				logger.error(this.getClass().getName()+":run:"+name+
					":Retry Count exceeded:terminating.");
				done = true;
			}
			if(status.getEngineeringStatus() == processStatus)
			{
				logger.log(AutoBooterConstants.AUTOBOOTER_LOG_LEVEL_COMMANDS,this.getClass().getName()+
					   ":run:"+name+":Engineering Mode enabled:terminating.");
				done = true;
			}
		}// while(!done)
	}// run method
}
//
// $Log: not supported by cvs2svn $
// Revision 0.2  2000/08/01 11:04:47  cjm
// More descriptive startup message.
//
// Revision 0.1  2000/07/06 10:27:03  cjm
// initial revision.
//
//
