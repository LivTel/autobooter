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
// AutoBooterProcessStatusInterface.java
// $Header: /home/cjm/cvs/autobooter/java/AutoBooterProcessStatusInterface.java,v 1.3 2006-05-16 16:20:21 cjm Exp $

import java.lang.*;

/**
 * This interface is implemented the status object, to allow processes to
 * obtain the status information they need.
 * This class was originally AutoBooterProcessParentInterface v0.1.
 * @see AutoBooterProcessThread
 * @author Chris Mottram
 * @version $Revision: 1.3 $
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
	 * Routine to return whether the specified status returned by a process an instance of AutoBooterProcessThread 
	 * is controlling is in the set of engineering statii, that cause this process to go into engineering mode. 
	 * This means the AutoBooterProcessThread for
	 * this process will terminate, and not re-start the process.
	 * @param status The status integer returned by the process.
	 * @return A boolean, if true the process will not be respawned.
	 */
	public boolean isEngineeringStatus(int status);
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
// Revision 1.2  2005/12/06 17:09:06  cjm
// Swapped getEngineeringStatus for isEngineeringStatus, to allow
// multiple process return values to stop re-spawning a process.
//
// Revision 1.1  2004/03/05 15:23:03  cjm
// Initial revision
//
// Revision 0.1  2000/07/06 10:27:03  cjm
// initial revision.
//
//
