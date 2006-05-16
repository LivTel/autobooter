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
// AutoBooterConstants.java
// $Header: /home/cjm/cvs/autobooter/java/AutoBooterConstants.java,v 1.2 2006-05-16 16:20:19 cjm Exp $
import java.lang.*;
import java.io.*;

/**
 * This class holds some constant values for the AutoBooter program. 
 * @author Chris Mottram
 * @version $Revision: 1.2 $
 */
public class AutoBooterConstants
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: AutoBooterConstants.java,v 1.2 2006-05-16 16:20:19 cjm Exp $");

	/**
	 * Error code. No error.
	 */
	public final static int AUTOBOOTER_ERROR_CODE_NO_ERROR 		= 0;
	/**
	 * The base Error number, for all AutoBooter error codes. 
	 */
	public final static int AUTOBOOTER_ERROR_CODE_BASE 	        = 1000000;

	/**
	 * Logging level. Don't do any logging.
	 */
	public final static int AUTOBOOTER_LOG_LEVEL_NONE 			= 0;
	/**
	 * Logging level. Log Commands messages received/sent.
	 */
	public final static int AUTOBOOTER_LOG_LEVEL_COMMANDS 			= (1<<0);
	/**
	 * Logging level. Log if any logging is turned on.
	 */
	public final static int AUTOBOOTER_LOG_LEVEL_ALL 			= (AUTOBOOTER_LOG_LEVEL_COMMANDS);
	/**
	 * Logging level used by the error logger. We want to log all errors,
	 * hence this value should be used for all errors.
	 */
	public final static int AUTOBOOTER_LOG_LEVEL_ERROR			= 1;
}
//
// $Log: not supported by cvs2svn $
// Revision 1.1  2004/03/05 15:23:03  cjm
// Initial revision
//
//
