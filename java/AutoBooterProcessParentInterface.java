// AutoBooterProcessParentInterface.java -*- mode: Fundamental;-*-
// $Header: /home/cjm/cvs/autobooter/java/AutoBooterProcessParentInterface.java,v 0.1 2000-07-06 10:27:03 cjm Exp $

import java.lang.*;

/**
 * This interface is implemented by all classes wanting to start AutoBooterProcessThread's,
 * to supply instances of these threads with the per-process data they need.
 * @see AutoBooterProcessThread
 * @author Chris Mottram
 * @version $Revision: 0.1 $
 */
public interface AutoBooterProcessParentInterface
{
	/**
	 * Routine to return the status the process an instance of AutoBooterProcessThread is controlling
	 * will return when it needs re-spawning.
	 * @return A status, returned from the process under control, that means the thread can re-spawn
	 * 	the process.
	 */
	public int getReSpawnStatus();
	/**
	 * Routine to return the status the process an instance of AutoBooterProcessThread is controlling
	 * will return when it wants to go into engineering mode. This means the AutoBooterProcessThread for
	 * this process will terminate, and not re-start the process.
	 * @return A status, returned from the process under control, that means the thread should <b>stop</b>
	 * 	re-spawning the process.
	 */
	public int getEngineeringStatus();
	/**
	 * Routine to return the retry time. If the instance of AutoBooterProcessThread respawns the
	 * process it is controlling more than retry_count times in this time, the thread assumes the
	 * the process is continually failing and stops trying to re-start it.
	 * @return A time, in milliseconds.
	 */
	public int getRetryTime();
}
//
// $Log: not supported by cvs2svn $
//
