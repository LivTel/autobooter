// AutoBooterProcessStatusInterface.java
// $Header: /home/cjm/cvs/autobooter/java/AutoBooterProcessStatusInterface.java,v 1.1 2004-03-05 15:23:03 cjm Exp $

import java.lang.*;

/**
 * This interface is implemented the status object, to allow processes to
 * obtain the status information they need.
 * This class was originally AutoBooterProcessParentInterface v0.1.
 * @see AutoBooterProcessThread
 * @author Chris Mottram
 * @version $Revision: 1.1 $
 */
public interface AutoBooterProcessStatusInterface
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
// Revision 0.1  2000/07/06 10:27:03  cjm
// initial revision.
//
//
