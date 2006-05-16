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
// AutoBooterProcessParentInterface.java
// $Header: /home/cjm/cvs/autobooter/java/AutoBooterLogInterface.java,v 1.2 2006-05-16 16:20:20 cjm Exp $

import java.lang.*;

/**
 * This interface exposes the AutoBooter logging methods. It is implemented by the AutoBooter main class.
 * This allows classes passed object exposing this interface to log.
 * @see AutoBooterProcessThread
 * @author Chris Mottram
 * @version $Revision: 1.2 $
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
// Revision 1.1  2004/03/05 15:23:03  cjm
// Initial revision
//
//
