// AutoBooterConstants.java
// $Header: /home/cjm/cvs/autobooter/java/AutoBooterConstants.java,v 1.1 2004-03-05 15:23:03 cjm Exp $
import java.lang.*;
import java.io.*;

/**
 * This class holds some constant values for the AutoBooter program. 
 * @author Chris Mottram
 * @version $Revision: 1.1 $
 */
public class AutoBooterConstants
{
	/**
	 * Revision Control System id string, showing the version of the Class.
	 */
	public final static String RCSID = new String("$Id: AutoBooterConstants.java,v 1.1 2004-03-05 15:23:03 cjm Exp $");

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
//
