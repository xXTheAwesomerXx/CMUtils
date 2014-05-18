package org.Container;

public class Variables {
	public static String ip = "";
	public static String user = "";
	public static String pass = "";
	public static String connectionsString = new String();
	public static String[] connections = new String[0];
	public static String[][] connectionDetails;
	public static boolean connected;
	public static boolean connectionFailed;
	public static boolean executeQuery;
	
	public static String[] sqlRows;
	public static String[] sqlRowFields;
	
	public static String[] connectionHostnames = new String[0];
	public static String[] connectionUsernames = new String[0];
	public static String[] connectionPasswords = new String[0];
	public static String[] connectionNames = new String[0];
	// DeviceTable
	public static String[] enduserFirstnames = null;
	public static String[] enduserLastnames = null;
	public static String[] phoneNames = null;
	public static String[] phoneEndusers = null;
	public static String[] phoneDesc = null;
	public static String[] phoneModel = null;
	public static String[] phoneDevpool = null;
	public static String[] phoneAllEndusersIDs = null;
	public static String[] phoneAllEndusersFirstnames = null;
	public static String[] phoneAllEndusersLastnames = null;
	// LineAssocTable
	public static String[] dnorPatterns = null;
	public static String[] lineAssocFirstnames = null;
	public static String[] lineAssocLastnames = null;
	public static String[] lineAssocUserids = null;
	// enduserAssocTable
	public static String[] devAssocDevicenames = null;
	public static String[] devAssocFirstnames = null;
	public static String[] devAssocLastnames = null;
	public static String[] devAssocUserids = null;
	// TableVariables
	public static String[][] enduserRows;
	public static String[][] devAssocDeviceRows;
	public static String[][] lineAssocEnduserRows;
	
	public static String[][] oldDeviceTableEnduserRows;
	public static String[][] newDeviceTableEnduserRows;
	
	public static String[][] oldenduserAssocTableEnduserRows;
	public static String[][] newenduserAssocTableEnduserRows;
	
	public static String[][] oldLineAssocTableEnduserRows;
	public static String[][] newLineAssocTableEnduserRows;
	
	public static String[][] deviceTableRows;
	public static String[][] lineAssocTableRows;
	public static String[][] enduserAssocTableRows;
	public static String[][] sqlTableRows;
	
	public static final String[] deviceTableColumns = { "#", "Device Name",
			"Firstname", "Lastname", "Owner ID", "Description", "Device Type",
			"Devicepool", "x" };
	public static final String[] lineAssocTableColumns = { "#", "Pattern", "Firstname", "Lastname", "Owner ID", "x" };
	public static final String[] enduserAssocTableColumns = { "#", "Device Name", "Owner ID", "Firstname", "Lastname", "x" };
	public static String[] sqlTableColumns;
}
