// AutoBooterProcessParentInterface.java
// $Header: /home/cjm/cvs/autobooter/java/AutoBooterLogInterface.java,v 1.1 2004-03-05 15:23:03 cjm Exp $

import java.lang.*;

/**
 * This interface exposes the AutoBooter logging methods. It is implemented by the AutoBooter main class.
 * This allows classes passed object exposing this interface to log.
 * @see AutoBooterProcessThread
 * @author Chris Mottram
 * @version $Revision: 1.1 $
 * @see AutoBooter
 */
public interface AutoBooterLogInterface
{
	/**
	 * Method to write the string to the relevant logger.
	 * @param level The level of logging this message belongs to.
	 * @param s The string to write.
	 */
	public void log(int level,String s);
	/**
	 * Method to write the string to the error logger.
	 * @param s The string to write.
	 */
	public void error(String s);
	/**
	 * Method to write the string to the error logger.
	 * @param s The string to write.
	 * @param e An exception that caused the error to occur.
	 */
	public void error(String s,Exception e);
}
//
// $Log: not supported by cvs2svn $
//
