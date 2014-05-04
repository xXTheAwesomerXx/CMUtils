package org.Container;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.CMUtils.CMUtilsGUI;
import org.Container.AXLTrustManager.MyTrustManager;

public class Methods {

	public static String getKeys(String which) {
		int[] case_ = { 1, 2, 3 };
		String[] which_ = { "ip", "user", "pass" };
		int use = 0;
		for (int i = 0; i < which_.length; i++) {
			if (which == which_[i]) {
				use = case_[i];
			}
		}
		switch (use) {
		case 1:
			return Variables.ip;
		case 2:
			return Variables.user;
		case 3:
			return Variables.pass;
		}
		return null;
	}

	public static String substringBetween(String str, String open, String close) {
		if ((str == null) || (open == null) || (close == null)) {
			return null;
		}
		int start = str.indexOf(open);
		if (start != -1) {
			int end = str.indexOf(close, start + open.length());
			if (end != -1) {
				return str.substring(start + open.length(), end);
			}
		}
		return null;
	}

	public static String[] substringsBetween(String str, String open,
			String close) {
		if ((str == null) || (open == null) || (close == null)) {
			return null;
		}
		int strLen = str.length();
		if (strLen == 0) {
			return null;
		}
		int closeLen = close.length();
		int openLen = open.length();
		List<String> list = new ArrayList();
		int pos = 0;
		while (pos < strLen - closeLen) {
			int start = str.indexOf(open, pos);
			if (start < 0) {
				break;
			}
			start += openLen;
			int end = str.indexOf(close, start);
			if (end < 0) {
				break;
			}
			list.add(str.substring(start, end));
			pos = end + closeLen;
		}
		if (list.isEmpty()) {
			return null;
		}
		return (String[]) list.toArray(new String[list.size()]);
	}

	public static String getHttpsHeader(String content) {
		String header = null;
		String Base64String = Base64.encode(getKeys("user") + ":"
				+ getKeys("pass"));
		header = "POST /axl/ HTTP/1.0\r\n";
		header = header + "Host: " + getKeys("ip") + ":8443\r\n";
		header = header + "Authorization: Basic " + Base64String;
		header = header + "Accept: text/*\r\n";
		header = header + "Content-type: text/xml\r\n";
		header = header + "SOAPAction: \"CUCM:DB ver=7.0\"\r\n";
		header = header + "Content-length: ";
		header = header + content.length();
		header = header + "\r\n\r\n";
		return header;
	}

	public static boolean testConnection() {
		String data = null;
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		byte[] bArray = null;
		String SBuffer;
		data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" ";
		data = data
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"> ";
		data = data
				+ "<SOAP-ENV:Body> <axl:executeSQLQuery xmlns:axl=\"http://www.cisco.com/AXL/7.0\" ";
		data = data
				+ " xsi:schemaLocation=\"http://www.cisco.com/AXL/1.0 http://gkar.cisco.com/schema/axlsoap.xsd\" ";
		data = data + "sequence=\"1234\">";
		data = data + "<sql>SELECT * FROM numplan</sql>";
		data = data
				+ "</axl:executeSQLQuery> </SOAP-ENV:Body> </SOAP-ENV:Envelope>";

		String header = getHttpsHeader(data);
		header = header + data;
		StringBuffer sb = null;
		try {
			AXLTrustManager axl = new AXLTrustManager();
			X509TrustManager xtm = axl.new MyTrustManager();
			TrustManager[] mytm = { xtm };
			SSLContext ctx = SSLContext.getInstance("SSL");
			ctx.init(null, mytm, null);
			SSLSocketFactory sslFact = (SSLSocketFactory) ctx
					.getSocketFactory();
			socket = (SSLSocket) sslFact.createSocket(getKeys("ip"),
					Integer.parseInt("8443"));
			in = socket.getInputStream();
			bArray = new byte[2048];
			sb = new StringBuffer(2048);
			int ch = 0;
			int sum = 0;
			out = socket.getOutputStream();
			out.write(header.getBytes());
			while ((ch = in.read(bArray)) != -1) {
				sum += ch;
				sb.append(new String(bArray, 0, ch));
			}
			socket.close();
			SBuffer = sb.toString();
		} catch (UnknownHostException e) {
			System.err.println("Error connecting to host: " + e.getMessage());
			return false;
		} catch (IOException ioe) {
			System.err.println("Error sending/receiving from server: "
					+ ioe.getMessage());
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (Exception exc) {
				exc.printStackTrace();
				System.err.println("Error closing connection to server: "
						+ exc.getMessage());
			}
			return false;
		} catch (Exception ea) {
			System.err.println("Unknown exception " + ea.getMessage());
			return false;
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (Exception exc) {
				exc.printStackTrace();
				System.err.println("Error closing connection to server: "
						+ exc.getMessage());
			}
		}
		return SBuffer.contains("HTTP/1.1 200 OK");
	}

	public static boolean updateDeviceSetEnduser(String arg1, String arg2) {
		String data = null;
		// String sAXLSOAPRequest = null;
		// String sAXLRequest = null;
		String string = null;
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		byte[] bArray = null;
		String argument1String = null;
		String argument2String = null;
		// TODO: Argument 1 Strings
		String Base64String = Base64.encode(Methods.getKeys("user") + ":"
				+ Methods.getKeys("pass"));
		System.out.println("Args: " + arg1 + ", " + arg2);
		// Build the HTTPS Header
		data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" ";
		data = data
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"> ";
		data = data
				+ "<SOAP-ENV:Body> <axl:executeSQLUpdate xmlns:axl=\"http://www.cisco.com/AXL/7.0\" ";
		data = data
				+ " xsi:schemaLocation=\"http://www.cisco.com/AXL/1.0 http://gkar.cisco.com/schema/axlsoap.xsd\" ";
		data = data + "sequence=\"1234\">";
		// data = data +
		// "<sql>SELECT device.name,enduser.firstname,enduser.lastname,enduser.userid,device.description,typemodel.name,devicepool.name FROM device LEFT JOIN enduser ON enduser.pkid = device.fkenduser LEFT JOIN typemodel ON typemodel.enum = device.tkmodel LEFT JOIN devicepool ON devicepool.pkid = device.fkdevicepool where device.name like 'SEP%'</sql>";
		data = data
				+ "<sql>update device set device.fkenduser = ( select pkid from enduser where enduser.userid = '"
				+ arg1 + "' ) where device.name = '" + arg2 + "'</sql>";
		System.out.println(substringBetween(data, "<sql>", "</sql>"));
		// data = data
		// +
		// "<sql>SELECT device.name,enduser.firstname,enduser.lastname,enduser.userid,device.description,typemodel.name,devicepool.name FROM device LEFT JOIN enduser ON enduser.pkid = device.fkenduser LEFT JOIN typemodel ON typemodel.enum = device.tkmodel LEFT JOIN devicepool ON devicepool.pkid = device.fkdevicepool where device.name like 'SEP%'</sql>";
		data = data
				+ "</axl:executeSQLUpdate> </SOAP-ENV:Body> </SOAP-ENV:Envelope>";

		String header = Methods.getHttpsHeader(data);
		header = header + data;
		StringBuffer sb = null;
		try {
			AXLTrustManager axl = new AXLTrustManager();
			// Implement the certificate-related stuffs required for sending
			// request via https
			X509TrustManager xtm = axl.new MyTrustManager();
			TrustManager[] mytm = { xtm };
			SSLContext ctx = SSLContext.getInstance("SSL");
			ctx.init(null, mytm, null);
			SSLSocketFactory sslFact = (SSLSocketFactory) ctx
					.getSocketFactory();
			socket = (SSLSocket) sslFact.createSocket(Methods.getKeys("ip"),
					Integer.parseInt("8443"));
			in = socket.getInputStream();
			bArray = new byte[2048];
			sb = new StringBuffer(2048);
			int ch = 0;
			int sum = 0;
			out = socket.getOutputStream();
			out.write(header.getBytes());
			while ((ch = in.read(bArray)) != -1) {
				sum += ch;
				sb.append(new String(bArray, 0, ch));
			}
			socket.close();
			string = sb.toString();
			String replacePhoneName = string.replace("<name/>",
					"<name>NULL</name>");
			String replaceFirstName = replacePhoneName.replace("<firstname/>",
					"<firstname>NULL</firstname>");
			String replaceLastName = replaceFirstName.replace("<lastname/>",
					"<lastname>NULL</lastname>");
			String replaceUserID = replaceLastName.replace("<userid/>",
					"<userid><NONE></userid>");
			String replaceDesc = replaceUserID.replace("<description/>",
					"<description>NULL</description>");
			String replaceModel = replaceDesc.replace("<tkmodel/>",
					"<tkmodel>NULL</tkmodel>");
			String replaceDevicepool = replaceModel.replace("<fkdevicepool/>",
					"<fkdevicepool>NULL</fkdevicepool>");
			String finalString = replaceDevicepool;
			// System.out.println(finalString);
			Variables.phoneNames = Methods.substringsBetween(finalString,
					"<name>", "</name>");
			Variables.enduserFirstnames = Methods.substringsBetween(
					finalString, "<firstname>", "</firstname");
			Variables.enduserLastnames = Methods.substringsBetween(finalString,
					"<lastname>", "</lastname");
			Variables.phoneEndusers = Methods.substringsBetween(finalString,
					"<userid>", "</userid");
			Variables.phoneDesc = Methods.substringsBetween(finalString,
					"<description>", "</description");
			Variables.phoneModel = Methods.substringsBetween(finalString,
					"<tkmodel>", "</tkmodel");
			Variables.phoneDevpool = Methods.substringsBetween(finalString,
					"<fkdevicepool>", "</fkdevicepool");
			Variables.deviceTableRows = new String[Variables.phoneNames.length][9];
			for (int i = 0; i < Variables.phoneNames.length; i++) {
				Variables.deviceTableRows[i][0] = Integer.toString(i + 1);
				Variables.deviceTableRows[i][1] = Variables.phoneNames[i];
				Variables.deviceTableRows[i][2] = Variables.enduserFirstnames[i];
				Variables.deviceTableRows[i][3] = Variables.enduserLastnames[i];
				Variables.deviceTableRows[i][4] = Variables.phoneEndusers[i];
				Variables.deviceTableRows[i][5] = Variables.phoneDesc[i];
				Variables.deviceTableRows[i][6] = Variables.phoneModel[i];
				Variables.deviceTableRows[i][7] = Variables.phoneDevpool[i];
				Variables.deviceTableRows[i][8] = "false";
				System.out.println("UPDATE!" + Variables.deviceTableRows[i][0]
						+ ", " + Variables.deviceTableRows[i][1] + ", "
						+ Variables.deviceTableRows[i][2] + ", "
						+ Variables.deviceTableRows[i][3] + ", "
						+ Variables.deviceTableRows[i][4] + ", "
						+ Variables.deviceTableRows[i][5] + ", "
						+ Variables.deviceTableRows[i][6] + ", "
						+ Variables.deviceTableRows[i][7]);
			}
		} catch (UnknownHostException e) {
			System.err.println("Error connecting to host: " + e.getMessage());
			return false;
		} catch (IOException ioe) {
			System.err.println("Error sending/receiving from server: "
					+ ioe.getMessage());
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (Exception exc) {
				exc.printStackTrace();
				System.err.println("Error closing connection to server: "
						+ exc.getMessage());
			}
		} catch (Exception ea) {
			System.err.println("Unknown exception " + ea.getMessage());
			return false;
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (Exception exc) {
				exc.printStackTrace();
				System.err.println("Error closing connection to server: "
						+ exc.getMessage());
			}
		}
		return true;
	}

	public static boolean addEnduserDeviceMap(String value1, String value2) {
		String data = null;
		// String sAXLSOAPRequest = null;
		// String sAXLRequest = null;
		String string = null;
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		byte[] bArray = null;
		String argument1String = null;
		String argument2String = null;
		// TODO: Argument 1 Strings
		String Base64String = Base64.encode(Methods.getKeys("user") + ":"
				+ Methods.getKeys("pass"));
		// Build the HTTPS Header
		data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" ";
		data = data
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"> ";
		data = data
				+ "<SOAP-ENV:Body> <axl:executeSQLUpdate xmlns:axl=\"http://www.cisco.com/AXL/7.0\" ";
		data = data
				+ " xsi:schemaLocation=\"http://www.cisco.com/AXL/1.0 http://gkar.cisco.com/schema/axlsoap.xsd\" ";
		data = data + "sequence=\"1234\">";
		// data = data +
		// "<sql>SELECT device.name,enduser.firstname,enduser.lastname,enduser.userid,device.description,typemodel.name,devicepool.name FROM device LEFT JOIN enduser ON enduser.pkid = device.fkenduser LEFT JOIN typemodel ON typemodel.enum = device.tkmodel LEFT JOIN devicepool ON devicepool.pkid = device.fkdevicepool where device.name like 'SEP%'</sql>";
		data = data
				+ "<sql>INSERT INTO enduserdevicemap (enduserdevicemap.fkenduser,enduserdevicemap.fkdevice,enduserdevicemap.tkuserassociation) SELECT enduser.pkid,device.pkid,typeuserassociation.enum FROM enduser,device,typeuserassociation WHERE enduser.userid = '"
				+ value1 + "' and device.name = '" + value2
				+ "' and typeuserassociation.enum = 1</sql>";
		System.out.println(substringBetween(data, "<sql>", "</sql>"));
		// data = data
		// +
		// "<sql>SELECT device.name,enduser.firstname,enduser.lastname,enduser.userid,device.description,typemodel.name,devicepool.name FROM device LEFT JOIN enduser ON enduser.pkid = device.fkenduser LEFT JOIN typemodel ON typemodel.enum = device.tkmodel LEFT JOIN devicepool ON devicepool.pkid = device.fkdevicepool where device.name like 'SEP%'</sql>";
		data = data
				+ "</axl:executeSQLUpdate> </SOAP-ENV:Body> </SOAP-ENV:Envelope>";

		String header = Methods.getHttpsHeader(data);
		header = header + data;
		StringBuffer sb = null;
		try {
			AXLTrustManager axl = new AXLTrustManager();
			// Implement the certificate-related stuffs required for sending
			// request via https
			X509TrustManager xtm = axl.new MyTrustManager();
			TrustManager[] mytm = { xtm };
			SSLContext ctx = SSLContext.getInstance("SSL");
			ctx.init(null, mytm, null);
			SSLSocketFactory sslFact = (SSLSocketFactory) ctx
					.getSocketFactory();
			socket = (SSLSocket) sslFact.createSocket(Methods.getKeys("ip"),
					Integer.parseInt("8443"));
			in = socket.getInputStream();
			bArray = new byte[2048];
			sb = new StringBuffer(2048);
			int ch = 0;
			int sum = 0;
			out = socket.getOutputStream();
			out.write(header.getBytes());
			while ((ch = in.read(bArray)) != -1) {
				sum += ch;
				sb.append(new String(bArray, 0, ch));
			}
			socket.close();
			string = sb.toString();
			String replacePhoneName = string.replace("<name/>",
					"<name>NULL</name>");
			String replaceFirstName = replacePhoneName.replace("<firstname/>",
					"<firstname>NULL</firstname>");
			String replaceLastName = replaceFirstName.replace("<lastname/>",
					"<lastname>NULL</lastname>");
			String replaceUserID = replaceLastName.replace("<userid/>",
					"<userid><NONE></userid>");
			String replaceDesc = replaceUserID.replace("<description/>",
					"<description>NULL</description>");
			String replaceModel = replaceDesc.replace("<tkmodel/>",
					"<tkmodel>NULL</tkmodel>");
			String replaceDevicepool = replaceModel.replace("<fkdevicepool/>",
					"<fkdevicepool>NULL</fkdevicepool>");
			String finalString = replaceDevicepool;
			// System.out.println(finalString);
			Variables.phoneNames = Methods.substringsBetween(finalString,
					"<name>", "</name>");
			Variables.enduserFirstnames = Methods.substringsBetween(
					finalString, "<firstname>", "</firstname");
			Variables.enduserLastnames = Methods.substringsBetween(finalString,
					"<lastname>", "</lastname");
			Variables.phoneEndusers = Methods.substringsBetween(finalString,
					"<userid>", "</userid");
			Variables.phoneDesc = Methods.substringsBetween(finalString,
					"<description>", "</description");
			Variables.phoneModel = Methods.substringsBetween(finalString,
					"<tkmodel>", "</tkmodel");
			Variables.phoneDevpool = Methods.substringsBetween(finalString,
					"<fkdevicepool>", "</fkdevicepool");
			Variables.deviceTableRows = new String[Variables.phoneNames.length][9];
			for (int i = 0; i < Variables.phoneNames.length; i++) {
				Variables.deviceTableRows[i][0] = Integer.toString(i + 1);
				Variables.deviceTableRows[i][1] = Variables.phoneNames[i];
				Variables.deviceTableRows[i][2] = Variables.enduserFirstnames[i];
				Variables.deviceTableRows[i][3] = Variables.enduserLastnames[i];
				Variables.deviceTableRows[i][4] = Variables.phoneEndusers[i];
				Variables.deviceTableRows[i][5] = Variables.phoneDesc[i];
				Variables.deviceTableRows[i][6] = Variables.phoneModel[i];
				Variables.deviceTableRows[i][7] = Variables.phoneDevpool[i];
				Variables.deviceTableRows[i][8] = "false";
			}
		} catch (UnknownHostException e) {
			System.err.println("Error connecting to host: " + e.getMessage());
			return false;
		} catch (IOException ioe) {
			System.err.println("Error sending/receiving from server: "
					+ ioe.getMessage());
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (Exception exc) {
				exc.printStackTrace();
				System.err.println("Error closing connection to server: "
						+ exc.getMessage());
			}
		} catch (Exception ea) {
			System.err.println("Unknown exception " + ea.getMessage());
			return false;
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (Exception exc) {
				exc.printStackTrace();
				System.err.println("Error closing connection to server: "
						+ exc.getMessage());
			}
		}
		return true;
	}

	public static boolean removeEnduserDeviceMap(String value1) {
		String data = null;
		// String sAXLSOAPRequest = null;
		// String sAXLRequest = null;
		String string = null;
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		byte[] bArray = null;
		String argument1String = null;
		String argument2String = null;
		// TODO: Argument 1 Strings
		String Base64String = Base64.encode(Methods.getKeys("user") + ":"
				+ Methods.getKeys("pass"));
		// Build the HTTPS Header
		data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" ";
		data = data
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"> ";
		data = data
				+ "<SOAP-ENV:Body> <axl:executeSQLUpdate xmlns:axl=\"http://www.cisco.com/AXL/7.0\" ";
		data = data
				+ " xsi:schemaLocation=\"http://www.cisco.com/AXL/1.0 http://gkar.cisco.com/schema/axlsoap.xsd\" ";
		data = data + "sequence=\"1234\">";
		// data = data +
		// "<sql>SELECT device.name,enduser.firstname,enduser.lastname,enduser.userid,device.description,typemodel.name,devicepool.name FROM device LEFT JOIN enduser ON enduser.pkid = device.fkenduser LEFT JOIN typemodel ON typemodel.enum = device.tkmodel LEFT JOIN devicepool ON devicepool.pkid = device.fkdevicepool where device.name like 'SEP%'</sql>";
		data = data
				+ "<sql>DELETE from enduserdevicemap WHERE fkdevice = ( SELECT pkid from device where name = '"
				+ value1 + "' )</sql>";
		System.out.println(substringBetween(data, "<sql>", "</sql>"));
		// data = data
		// +
		// "<sql>SELECT device.name,enduser.firstname,enduser.lastname,enduser.userid,device.description,typemodel.name,devicepool.name FROM device LEFT JOIN enduser ON enduser.pkid = device.fkenduser LEFT JOIN typemodel ON typemodel.enum = device.tkmodel LEFT JOIN devicepool ON devicepool.pkid = device.fkdevicepool where device.name like 'SEP%'</sql>";
		data = data
				+ "</axl:executeSQLUpdate> </SOAP-ENV:Body> </SOAP-ENV:Envelope>";

		String header = Methods.getHttpsHeader(data);
		header = header + data;
		StringBuffer sb = null;
		try {
			AXLTrustManager axl = new AXLTrustManager();
			// Implement the certificate-related stuffs required for sending
			// request via https
			X509TrustManager xtm = axl.new MyTrustManager();
			TrustManager[] mytm = { xtm };
			SSLContext ctx = SSLContext.getInstance("SSL");
			ctx.init(null, mytm, null);
			SSLSocketFactory sslFact = (SSLSocketFactory) ctx
					.getSocketFactory();
			socket = (SSLSocket) sslFact.createSocket(Methods.getKeys("ip"),
					Integer.parseInt("8443"));
			in = socket.getInputStream();
			bArray = new byte[2048];
			sb = new StringBuffer(2048);
			int ch = 0;
			int sum = 0;
			out = socket.getOutputStream();
			out.write(header.getBytes());
			while ((ch = in.read(bArray)) != -1) {
				sum += ch;
				sb.append(new String(bArray, 0, ch));
			}
			socket.close();
			string = sb.toString();
			String replacePhoneName = string.replace("<name/>",
					"<name>NULL</name>");
			String replaceFirstName = replacePhoneName.replace("<firstname/>",
					"<firstname>NULL</firstname>");
			String replaceLastName = replaceFirstName.replace("<lastname/>",
					"<lastname>NULL</lastname>");
			String replaceUserID = replaceLastName.replace("<userid/>",
					"<userid><NONE></userid>");
			String replaceDesc = replaceUserID.replace("<description/>",
					"<description>NULL</description>");
			String replaceModel = replaceDesc.replace("<tkmodel/>",
					"<tkmodel>NULL</tkmodel>");
			String replaceDevicepool = replaceModel.replace("<fkdevicepool/>",
					"<fkdevicepool>NULL</fkdevicepool>");
			String finalString = replaceDevicepool;
			// System.out.println(finalString);
			Variables.phoneNames = Methods.substringsBetween(finalString,
					"<name>", "</name>");
			Variables.enduserFirstnames = Methods.substringsBetween(
					finalString, "<firstname>", "</firstname");
			Variables.enduserLastnames = Methods.substringsBetween(finalString,
					"<lastname>", "</lastname");
			Variables.phoneEndusers = Methods.substringsBetween(finalString,
					"<userid>", "</userid");
			Variables.phoneDesc = Methods.substringsBetween(finalString,
					"<description>", "</description");
			Variables.phoneModel = Methods.substringsBetween(finalString,
					"<tkmodel>", "</tkmodel");
			Variables.phoneDevpool = Methods.substringsBetween(finalString,
					"<fkdevicepool>", "</fkdevicepool");
			Variables.deviceTableRows = new String[Variables.phoneNames.length][9];
			for (int i = 0; i < Variables.phoneNames.length; i++) {
				Variables.deviceTableRows[i][0] = Integer.toString(i + 1);
				Variables.deviceTableRows[i][1] = Variables.phoneNames[i];
				Variables.deviceTableRows[i][2] = Variables.enduserFirstnames[i];
				Variables.deviceTableRows[i][3] = Variables.enduserLastnames[i];
				Variables.deviceTableRows[i][4] = Variables.phoneEndusers[i];
				Variables.deviceTableRows[i][5] = Variables.phoneDesc[i];
				Variables.deviceTableRows[i][6] = Variables.phoneModel[i];
				Variables.deviceTableRows[i][7] = Variables.phoneDevpool[i];
				Variables.deviceTableRows[i][8] = "false";
			}
		} catch (UnknownHostException e) {
			System.err.println("Error connecting to host: " + e.getMessage());
			return false;
		} catch (IOException ioe) {
			System.err.println("Error sending/receiving from server: "
					+ ioe.getMessage());
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (Exception exc) {
				exc.printStackTrace();
				System.err.println("Error closing connection to server: "
						+ exc.getMessage());
			}
		} catch (Exception ea) {
			System.err.println("Unknown exception " + ea.getMessage());
			return false;
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (Exception exc) {
				exc.printStackTrace();
				System.err.println("Error closing connection to server: "
						+ exc.getMessage());
			}
		}
		return true;
	}

	public static boolean addNumplan(String value1) {
		String data = null;
		// String sAXLSOAPRequest = null;
		// String sAXLRequest = null;
		String string = null;
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		byte[] bArray = null;
		// TODO: Argument 1 Strings
		String Base64String = Base64.encode(Methods.getKeys("user") + ":"
				+ Methods.getKeys("pass"));
		// Build the HTTPS Header
		data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" ";
		data = data
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"> ";
		data = data
				+ "<SOAP-ENV:Body> <axl:executeSQLUpdate xmlns:axl=\"http://www.cisco.com/AXL/7.0\" ";
		data = data
				+ " xsi:schemaLocation=\"http://www.cisco.com/AXL/1.0 http://gkar.cisco.com/schema/axlsoap.xsd\" ";
		data = data + "sequence=\"1234\">";
		// data = data +
		// "<sql>SELECT device.name,enduser.firstname,enduser.lastname,enduser.userid,device.description,typemodel.name,devicepool.name FROM device LEFT JOIN enduser ON enduser.pkid = device.fkenduser LEFT JOIN typemodel ON typemodel.enum = device.tkmodel LEFT JOIN devicepool ON devicepool.pkid = device.fkdevicepool where device.name like 'SEP%'</sql>";
		data = data
				+ "<sql>insert into numplan (dnorpattern,tkpatternusage) VALUES ("
				+ value1 + ",2)</sql>";
		System.out.println(substringBetween(data, "<sql>", "</sql>"));
		// data = data
		// +
		// "<sql>SELECT device.name,enduser.firstname,enduser.lastname,enduser.userid,device.description,typemodel.name,devicepool.name FROM device LEFT JOIN enduser ON enduser.pkid = device.fkenduser LEFT JOIN typemodel ON typemodel.enum = device.tkmodel LEFT JOIN devicepool ON devicepool.pkid = device.fkdevicepool where device.name like 'SEP%'</sql>";
		data = data
				+ "</axl:executeSQLUpdate> </SOAP-ENV:Body> </SOAP-ENV:Envelope>";

		String header = Methods.getHttpsHeader(data);
		header = header + data;
		StringBuffer sb = null;
		try {
			AXLTrustManager axl = new AXLTrustManager();
			// Implement the certificate-related stuffs required for sending
			// request via https
			X509TrustManager xtm = axl.new MyTrustManager();
			TrustManager[] mytm = { xtm };
			SSLContext ctx = SSLContext.getInstance("SSL");
			ctx.init(null, mytm, null);
			SSLSocketFactory sslFact = (SSLSocketFactory) ctx
					.getSocketFactory();
			socket = (SSLSocket) sslFact.createSocket(Methods.getKeys("ip"),
					Integer.parseInt("8443"));
			in = socket.getInputStream();
			bArray = new byte[2048];
			sb = new StringBuffer(2048);
			int ch = 0;
			int sum = 0;
			out = socket.getOutputStream();
			out.write(header.getBytes());
			while ((ch = in.read(bArray)) != -1) {
				sum += ch;
				sb.append(new String(bArray, 0, ch));
			}
			socket.close();
			string = sb.toString();
			String replacePhoneName = string.replace("<name/>",
					"<name>NULL</name>");
			String replaceFirstName = replacePhoneName.replace("<firstname/>",
					"<firstname>NULL</firstname>");
			String replaceLastName = replaceFirstName.replace("<lastname/>",
					"<lastname>NULL</lastname>");
			String replaceUserID = replaceLastName.replace("<userid/>",
					"<userid><NONE></userid>");
			String replaceDesc = replaceUserID.replace("<description/>",
					"<description>NULL</description>");
			String replaceModel = replaceDesc.replace("<tkmodel/>",
					"<tkmodel>NULL</tkmodel>");
			String replaceDevicepool = replaceModel.replace("<fkdevicepool/>",
					"<fkdevicepool>NULL</fkdevicepool>");
			String finalString = replaceDevicepool;
			// System.out.println(finalString);
			Variables.phoneNames = Methods.substringsBetween(finalString,
					"<name>", "</name>");
			Variables.enduserFirstnames = Methods.substringsBetween(
					finalString, "<firstname>", "</firstname");
			Variables.enduserLastnames = Methods.substringsBetween(finalString,
					"<lastname>", "</lastname");
			Variables.phoneEndusers = Methods.substringsBetween(finalString,
					"<userid>", "</userid");
			Variables.phoneDesc = Methods.substringsBetween(finalString,
					"<description>", "</description");
			Variables.phoneModel = Methods.substringsBetween(finalString,
					"<tkmodel>", "</tkmodel");
			Variables.phoneDevpool = Methods.substringsBetween(finalString,
					"<fkdevicepool>", "</fkdevicepool");
			Variables.deviceTableRows = new String[Variables.phoneNames.length][9];
			for (int i = 0; i < Variables.phoneNames.length; i++) {
				Variables.deviceTableRows[i][0] = Integer.toString(i + 1);
				Variables.deviceTableRows[i][1] = Variables.phoneNames[i];
				Variables.deviceTableRows[i][2] = Variables.enduserFirstnames[i];
				Variables.deviceTableRows[i][3] = Variables.enduserLastnames[i];
				Variables.deviceTableRows[i][4] = Variables.phoneEndusers[i];
				Variables.deviceTableRows[i][5] = Variables.phoneDesc[i];
				Variables.deviceTableRows[i][6] = Variables.phoneModel[i];
				Variables.deviceTableRows[i][7] = Variables.phoneDevpool[i];
				Variables.deviceTableRows[i][8] = "false";
			}
		} catch (UnknownHostException e) {
			System.err.println("Error connecting to host: " + e.getMessage());
			return false;
		} catch (IOException ioe) {
			System.err.println("Error sending/receiving from server: "
					+ ioe.getMessage());
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (Exception exc) {
				exc.printStackTrace();
				System.err.println("Error closing connection to server: "
						+ exc.getMessage());
			}
		} catch (Exception ea) {
			System.err.println("Unknown exception " + ea.getMessage());
			return false;
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (Exception exc) {
				exc.printStackTrace();
				System.err.println("Error closing connection to server: "
						+ exc.getMessage());
			}
		}
		return true;
	}

	public static boolean updatePrimaryExtension(String value1, String value2) {
		String data = null;
		// String sAXLSOAPRequest = null;
		// String sAXLRequest = null;
		String string = null;
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		byte[] bArray = null;
		// TODO: Argument 1 Strings
		String Base64String = Base64.encode(Methods.getKeys("user") + ":"
				+ Methods.getKeys("pass"));
		// Build the HTTPS Header
		data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" ";
		data = data
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"> ";
		data = data
				+ "<SOAP-ENV:Body> <axl:executeSQLUpdate xmlns:axl=\"http://www.cisco.com/AXL/7.0\" ";
		data = data
				+ " xsi:schemaLocation=\"http://www.cisco.com/AXL/1.0 http://gkar.cisco.com/schema/axlsoap.xsd\" ";
		data = data + "sequence=\"1234\">";
		// data = data +
		// "<sql>SELECT device.name,enduser.firstname,enduser.lastname,enduser.userid,device.description,typemodel.name,devicepool.name FROM device LEFT JOIN enduser ON enduser.pkid = device.fkenduser LEFT JOIN typemodel ON typemodel.enum = device.tkmodel LEFT JOIN devicepool ON devicepool.pkid = device.fkdevicepool where device.name like 'SEP%'</sql>";
		data = data
				+ "<sql>insert into endusernumplanmap (fkenduser,fknumplan,tkdnusage) select enduser.pkid, numplan.pkid, typednusage.enum from enduser,numplan,typednusage where enduser.userid = '"
				+ value1
				+ "' and numplan.dnorpattern = ( select dnorpattern from numplan where pkid = ( select fknumplan from devicenumplanmap where fkdevice = ( select pkid from device where name = '"
				+ value2 + "' )))and typednusage.enum = 1</sql>";
		System.out.println(substringBetween(data, "<sql>", "</sql>"));
		// data = data
		// +
		// "<sql>SELECT device.name,enduser.firstname,enduser.lastname,enduser.userid,device.description,typemodel.name,devicepool.name FROM device LEFT JOIN enduser ON enduser.pkid = device.fkenduser LEFT JOIN typemodel ON typemodel.enum = device.tkmodel LEFT JOIN devicepool ON devicepool.pkid = device.fkdevicepool where device.name like 'SEP%'</sql>";
		data = data
				+ "</axl:executeSQLUpdate> </SOAP-ENV:Body> </SOAP-ENV:Envelope>";

		String header = Methods.getHttpsHeader(data);
		header = header + data;
		StringBuffer sb = null;
		try {
			AXLTrustManager axl = new AXLTrustManager();
			// Implement the certificate-related stuffs required for sending
			// request via https
			X509TrustManager xtm = axl.new MyTrustManager();
			TrustManager[] mytm = { xtm };
			SSLContext ctx = SSLContext.getInstance("SSL");
			ctx.init(null, mytm, null);
			SSLSocketFactory sslFact = (SSLSocketFactory) ctx
					.getSocketFactory();
			socket = (SSLSocket) sslFact.createSocket(Methods.getKeys("ip"),
					Integer.parseInt("8443"));
			in = socket.getInputStream();
			bArray = new byte[2048];
			sb = new StringBuffer(2048);
			int ch = 0;
			int sum = 0;
			out = socket.getOutputStream();
			out.write(header.getBytes());
			while ((ch = in.read(bArray)) != -1) {
				sum += ch;
				sb.append(new String(bArray, 0, ch));
			}
			socket.close();
			string = sb.toString();
			String replacePhoneName = string.replace("<name/>",
					"<name>NULL</name>");
			String replaceFirstName = replacePhoneName.replace("<firstname/>",
					"<firstname>NULL</firstname>");
			String replaceLastName = replaceFirstName.replace("<lastname/>",
					"<lastname>NULL</lastname>");
			String replaceUserID = replaceLastName.replace("<userid/>",
					"<userid><NONE></userid>");
			String replaceDesc = replaceUserID.replace("<description/>",
					"<description>NULL</description>");
			String replaceModel = replaceDesc.replace("<tkmodel/>",
					"<tkmodel>NULL</tkmodel>");
			String replaceDevicepool = replaceModel.replace("<fkdevicepool/>",
					"<fkdevicepool>NULL</fkdevicepool>");
			String finalString = replaceDevicepool;
			// System.out.println(finalString);
			Variables.phoneNames = Methods.substringsBetween(finalString,
					"<name>", "</name>");
			Variables.enduserFirstnames = Methods.substringsBetween(
					finalString, "<firstname>", "</firstname");
			Variables.enduserLastnames = Methods.substringsBetween(finalString,
					"<lastname>", "</lastname");
			Variables.phoneEndusers = Methods.substringsBetween(finalString,
					"<userid>", "</userid");
			Variables.phoneDesc = Methods.substringsBetween(finalString,
					"<description>", "</description");
			Variables.phoneModel = Methods.substringsBetween(finalString,
					"<tkmodel>", "</tkmodel");
			Variables.phoneDevpool = Methods.substringsBetween(finalString,
					"<fkdevicepool>", "</fkdevicepool");
			Variables.deviceTableRows = new String[Variables.phoneNames.length][9];
			for (int i = 0; i < Variables.phoneNames.length; i++) {
				Variables.deviceTableRows[i][0] = Integer.toString(i + 1);
				Variables.deviceTableRows[i][1] = Variables.phoneNames[i];
				Variables.deviceTableRows[i][2] = Variables.enduserFirstnames[i];
				Variables.deviceTableRows[i][3] = Variables.enduserLastnames[i];
				Variables.deviceTableRows[i][4] = Variables.phoneEndusers[i];
				Variables.deviceTableRows[i][5] = Variables.phoneDesc[i];
				Variables.deviceTableRows[i][6] = Variables.phoneModel[i];
				Variables.deviceTableRows[i][7] = Variables.phoneDevpool[i];
				Variables.deviceTableRows[i][8] = "false";
			}
		} catch (UnknownHostException e) {
			System.err.println("Error connecting to host: " + e.getMessage());
			return false;
		} catch (IOException ioe) {
			System.err.println("Error sending/receiving from server: "
					+ ioe.getMessage());
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (Exception exc) {
				exc.printStackTrace();
				System.err.println("Error closing connection to server: "
						+ exc.getMessage());
			}
		} catch (Exception ea) {
			System.err.println("Unknown exception " + ea.getMessage());
			return false;
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (Exception exc) {
				exc.printStackTrace();
				System.err.println("Error closing connection to server: "
						+ exc.getMessage());
			}
		}
		return true;
	}

	public static boolean addNumplanDevicemap(String value1) {
		String data = null;
		// String sAXLSOAPRequest = null;
		// String sAXLRequest = null;
		String string = null;
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		byte[] bArray = null;
		// TODO: Argument 1 Strings
		String Base64String = Base64.encode(Methods.getKeys("user") + ":"
				+ Methods.getKeys("pass"));
		// Build the HTTPS Header
		data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" ";
		data = data
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"> ";
		data = data
				+ "<SOAP-ENV:Body> <axl:executeSQLUpdate xmlns:axl=\"http://www.cisco.com/AXL/7.0\" ";
		data = data
				+ " xsi:schemaLocation=\"http://www.cisco.com/AXL/1.0 http://gkar.cisco.com/schema/axlsoap.xsd\" ";
		data = data + "sequence=\"1234\">";
		// data = data +
		// "<sql>SELECT device.name,enduser.firstname,enduser.lastname,enduser.userid,device.description,typemodel.name,devicepool.name FROM device LEFT JOIN enduser ON enduser.pkid = device.fkenduser LEFT JOIN typemodel ON typemodel.enum = device.tkmodel LEFT JOIN devicepool ON devicepool.pkid = device.fkdevicepool where device.name like 'SEP%'</sql>";
		data = data
				+ "<sql>insert into devicenumplanmap (fkdevice,fknumplan,numplanindex) select device.pkid,endusernumplanmap.fknumplan,typednusage.enum from device, endusernumplanmap,typednusage where device.fkenduser = (select pkid from enduser where userid = '"
				+ value1
				+ "') and endusernumplanmap.fknumplan = ( select fknumplan from endusernumplanmap where fkenduser = ( select pkid from enduser where userid = '"
				+ value1 + "')) and typednusage.enum = 1</sql>";
		System.out.println(substringBetween(data, "<sql>", "</sql>"));
		// data = data
		// +
		// "<sql>SELECT device.name,enduser.firstname,enduser.lastname,enduser.userid,device.description,typemodel.name,devicepool.name FROM device LEFT JOIN enduser ON enduser.pkid = device.fkenduser LEFT JOIN typemodel ON typemodel.enum = device.tkmodel LEFT JOIN devicepool ON devicepool.pkid = device.fkdevicepool where device.name like 'SEP%'</sql>";
		data = data
				+ "</axl:executeSQLUpdate> </SOAP-ENV:Body> </SOAP-ENV:Envelope>";

		String header = Methods.getHttpsHeader(data);
		header = header + data;
		StringBuffer sb = null;
		try {
			AXLTrustManager axl = new AXLTrustManager();
			// Implement the certificate-related stuffs required for sending
			// request via https
			X509TrustManager xtm = axl.new MyTrustManager();
			TrustManager[] mytm = { xtm };
			SSLContext ctx = SSLContext.getInstance("SSL");
			ctx.init(null, mytm, null);
			SSLSocketFactory sslFact = (SSLSocketFactory) ctx
					.getSocketFactory();
			socket = (SSLSocket) sslFact.createSocket(Methods.getKeys("ip"),
					Integer.parseInt("8443"));
			in = socket.getInputStream();
			bArray = new byte[2048];
			sb = new StringBuffer(2048);
			int ch = 0;
			int sum = 0;
			out = socket.getOutputStream();
			out.write(header.getBytes());
			while ((ch = in.read(bArray)) != -1) {
				sum += ch;
				sb.append(new String(bArray, 0, ch));
			}
			socket.close();
			string = sb.toString();
			String replacePhoneName = string.replace("<name/>",
					"<name>NULL</name>");
			String replaceFirstName = replacePhoneName.replace("<firstname/>",
					"<firstname>NULL</firstname>");
			String replaceLastName = replaceFirstName.replace("<lastname/>",
					"<lastname>NULL</lastname>");
			String replaceUserID = replaceLastName.replace("<userid/>",
					"<userid><NONE></userid>");
			String replaceDesc = replaceUserID.replace("<description/>",
					"<description>NULL</description>");
			String replaceModel = replaceDesc.replace("<tkmodel/>",
					"<tkmodel>NULL</tkmodel>");
			String replaceDevicepool = replaceModel.replace("<fkdevicepool/>",
					"<fkdevicepool>NULL</fkdevicepool>");
			String finalString = replaceDevicepool;
			// System.out.println(finalString);
			Variables.phoneNames = Methods.substringsBetween(finalString,
					"<name>", "</name>");
			Variables.enduserFirstnames = Methods.substringsBetween(
					finalString, "<firstname>", "</firstname");
			Variables.enduserLastnames = Methods.substringsBetween(finalString,
					"<lastname>", "</lastname");
			Variables.phoneEndusers = Methods.substringsBetween(finalString,
					"<userid>", "</userid");
			Variables.phoneDesc = Methods.substringsBetween(finalString,
					"<description>", "</description");
			Variables.phoneModel = Methods.substringsBetween(finalString,
					"<tkmodel>", "</tkmodel");
			Variables.phoneDevpool = Methods.substringsBetween(finalString,
					"<fkdevicepool>", "</fkdevicepool");
			Variables.deviceTableRows = new String[Variables.phoneNames.length][9];
			for (int i = 0; i < Variables.phoneNames.length; i++) {
				Variables.deviceTableRows[i][0] = Integer.toString(i + 1);
				Variables.deviceTableRows[i][1] = Variables.phoneNames[i];
				Variables.deviceTableRows[i][2] = Variables.enduserFirstnames[i];
				Variables.deviceTableRows[i][3] = Variables.enduserLastnames[i];
				Variables.deviceTableRows[i][4] = Variables.phoneEndusers[i];
				Variables.deviceTableRows[i][5] = Variables.phoneDesc[i];
				Variables.deviceTableRows[i][6] = Variables.phoneModel[i];
				Variables.deviceTableRows[i][7] = Variables.phoneDevpool[i];
				Variables.deviceTableRows[i][8] = "false";
			}
		} catch (UnknownHostException e) {
			System.err.println("Error connecting to host: " + e.getMessage());
			return false;
		} catch (IOException ioe) {
			System.err.println("Error sending/receiving from server: "
					+ ioe.getMessage());
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (Exception exc) {
				exc.printStackTrace();
				System.err.println("Error closing connection to server: "
						+ exc.getMessage());
			}
		} catch (Exception ea) {
			System.err.println("Unknown exception " + ea.getMessage());
			return false;
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (Exception exc) {
				exc.printStackTrace();
				System.err.println("Error closing connection to server: "
						+ exc.getMessage());
			}
		}
		return true;
	}

	public static boolean addDevicenumplanEnduserNumplanAssoc(String value1) {
		String data = null;
		// String sAXLSOAPRequest = null;
		// String sAXLRequest = null;
		String string = null;
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		byte[] bArray = null;
		// TODO: Argument 1 Strings
		String Base64String = Base64.encode(Methods.getKeys("user") + ":"
				+ Methods.getKeys("pass"));
		// Build the HTTPS Header
		data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" ";
		data = data
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"> ";
		data = data
				+ "<SOAP-ENV:Body> <axl:executeSQLUpdate xmlns:axl=\"http://www.cisco.com/AXL/7.0\" ";
		data = data
				+ " xsi:schemaLocation=\"http://www.cisco.com/AXL/1.0 http://gkar.cisco.com/schema/axlsoap.xsd\" ";
		data = data + "sequence=\"1234\">";
		// data = data +
		// "<sql>SELECT device.name,enduser.firstname,enduser.lastname,enduser.userid,device.description,typemodel.name,devicepool.name FROM device LEFT JOIN enduser ON enduser.pkid = device.fkenduser LEFT JOIN typemodel ON typemodel.enum = device.tkmodel LEFT JOIN devicepool ON devicepool.pkid = device.fkdevicepool where device.name like 'SEP%'</sql>";
		data = data
				+ "<sql>insert into devicenumplanmapendusermap (fkdevicenumplanmap,fkenduser) select devicenumplanmap.pkid, enduser.pkid from devicenumplanmap, enduser where devicenumplanmap.pkid = ( select pkid from devicenumplanmap where fkdevice = ( select pkid from device where fkenduser = ( select pkid from enduser where userid = '"
				+ value1
				+ "' ))) and enduser.pkid = ( select pkid from enduser where userid = '"
				+ value1 + "' )</sql>";
		System.out.println(substringBetween(data, "<sql>", "</sql>"));

		// data = data
		// +
		// "<sql>SELECT device.name,enduser.firstname,enduser.lastname,enduser.userid,device.description,typemodel.name,devicepool.name FROM device LEFT JOIN enduser ON enduser.pkid = device.fkenduser LEFT JOIN typemodel ON typemodel.enum = device.tkmodel LEFT JOIN devicepool ON devicepool.pkid = device.fkdevicepool where device.name like 'SEP%'</sql>";
		data = data
				+ "</axl:executeSQLUpdate> </SOAP-ENV:Body> </SOAP-ENV:Envelope>";

		String header = Methods.getHttpsHeader(data);
		header = header + data;
		StringBuffer sb = null;
		try {
			AXLTrustManager axl = new AXLTrustManager();
			// Implement the certificate-related stuffs required for sending
			// request via https
			X509TrustManager xtm = axl.new MyTrustManager();
			TrustManager[] mytm = { xtm };
			SSLContext ctx = SSLContext.getInstance("SSL");
			ctx.init(null, mytm, null);
			SSLSocketFactory sslFact = (SSLSocketFactory) ctx
					.getSocketFactory();
			socket = (SSLSocket) sslFact.createSocket(Methods.getKeys("ip"),
					Integer.parseInt("8443"));
			in = socket.getInputStream();
			bArray = new byte[2048];
			sb = new StringBuffer(2048);
			int ch = 0;
			int sum = 0;
			out = socket.getOutputStream();
			out.write(header.getBytes());
			while ((ch = in.read(bArray)) != -1) {
				sum += ch;
				sb.append(new String(bArray, 0, ch));
			}
			socket.close();
			string = sb.toString();
			String replacePhoneName = string.replace("<name/>",
					"<name>NULL</name>");
			String replaceFirstName = replacePhoneName.replace("<firstname/>",
					"<firstname>NULL</firstname>");
			String replaceLastName = replaceFirstName.replace("<lastname/>",
					"<lastname>NULL</lastname>");
			String replaceUserID = replaceLastName.replace("<userid/>",
					"<userid><NONE></userid>");
			String replaceDesc = replaceUserID.replace("<description/>",
					"<description>NULL</description>");
			String replaceModel = replaceDesc.replace("<tkmodel/>",
					"<tkmodel>NULL</tkmodel>");
			String replaceDevicepool = replaceModel.replace("<fkdevicepool/>",
					"<fkdevicepool>NULL</fkdevicepool>");
			String finalString = replaceDevicepool;
			// System.out.println(finalString);
			Variables.phoneNames = Methods.substringsBetween(finalString,
					"<name>", "</name>");
			Variables.enduserFirstnames = Methods.substringsBetween(
					finalString, "<firstname>", "</firstname");
			Variables.enduserLastnames = Methods.substringsBetween(finalString,
					"<lastname>", "</lastname");
			Variables.phoneEndusers = Methods.substringsBetween(finalString,
					"<userid>", "</userid");
			Variables.phoneDesc = Methods.substringsBetween(finalString,
					"<description>", "</description");
			Variables.phoneModel = Methods.substringsBetween(finalString,
					"<tkmodel>", "</tkmodel");
			Variables.phoneDevpool = Methods.substringsBetween(finalString,
					"<fkdevicepool>", "</fkdevicepool");
			Variables.deviceTableRows = new String[Variables.phoneNames.length][9];
			for (int i = 0; i < Variables.phoneNames.length; i++) {
				Variables.deviceTableRows[i][0] = Integer.toString(i + 1);
				Variables.deviceTableRows[i][1] = Variables.phoneNames[i];
				Variables.deviceTableRows[i][2] = Variables.enduserFirstnames[i];
				Variables.deviceTableRows[i][3] = Variables.enduserLastnames[i];
				Variables.deviceTableRows[i][4] = Variables.phoneEndusers[i];
				Variables.deviceTableRows[i][5] = Variables.phoneDesc[i];
				Variables.deviceTableRows[i][6] = Variables.phoneModel[i];
				Variables.deviceTableRows[i][7] = Variables.phoneDevpool[i];
				Variables.deviceTableRows[i][8] = "false";
			}
		} catch (UnknownHostException e) {
			System.err.println("Error connecting to host: " + e.getMessage());
			return false;
		} catch (IOException ioe) {
			System.err.println("Error sending/receiving from server: "
					+ ioe.getMessage());
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (Exception exc) {
				exc.printStackTrace();
				System.err.println("Error closing connection to server: "
						+ exc.getMessage());
			}
		} catch (Exception ea) {
			System.err.println("Unknown exception " + ea.getMessage());
			return false;
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (Exception exc) {
				exc.printStackTrace();
				System.err.println("Error closing connection to server: "
						+ exc.getMessage());
			}
		}
		return true;
	}

	public static boolean getDevices(int condition1, int condition2,
			String argument) {
		String data = null;
		// String sAXLSOAPRequest = null;
		// String sAXLRequest = null;
		String string = null;
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		byte[] bArray = null;
		String condition1String = null;
		String condition2String = null;
		// TODO: Argument 1 Strings
		if (condition1 == 1) {
			condition1String = "device.name";
		} else if (condition1 == 2) {
			condition1String = "device.description";
		} else if (condition1 == 3) {
			condition1String = "devicepool.name";
		} else if (condition1 == 4) {
			condition1String = "typemodel.name";
		} else if (condition1 == 5) {
			condition1String = "enduser.userid";
		}
		if (condition2 == 1) {
			condition2String = "LIKE '" + argument + "%'";
		} else if (condition2 == 2) {
			condition2String = "LIKE '%" + argument + "%'";
		} else if (condition2 == 3) {
			condition2String = "LIKE '%" + argument + "'";
		} else if (condition2 == 4) {
			condition2String = "= '" + argument + "'";
		} else if (condition2 == 5) {
			condition2String = "IS NULL";
		} else if (condition2 == 6) {
			condition2String = "IS NOT NULL";
		}
		String Base64String = Base64.encode(Methods.getKeys("user") + ":"
				+ Methods.getKeys("pass"));
		// Build the HTTPS Header
		data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" ";
		data = data
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"> ";
		data = data
				+ "<SOAP-ENV:Body> <axl:executeSQLQuery xmlns:axl=\"http://www.cisco.com/AXL/7.0\" ";
		data = data
				+ " xsi:schemaLocation=\"http://www.cisco.com/AXL/1.0 http://gkar.cisco.com/schema/axlsoap.xsd\" ";
		data = data + "sequence=\"1234\">";
		// data = data +
		// "<sql>SELECT device.name,enduser.firstname,enduser.lastname,enduser.userid,device.description,typemodel.name,devicepool.name FROM device LEFT JOIN enduser ON enduser.pkid = device.fkenduser LEFT JOIN typemodel ON typemodel.enum = device.tkmodel LEFT JOIN devicepool ON devicepool.pkid = device.fkdevicepool where device.name like 'SEP%'</sql>";
		String sqlString = null;
		// if (condition1 != 3) {
		data = data
				+ "<sql>SELECT device.name as DeviceName, enduser.firstname as FirstName, enduser.lastname as LastName, enduser.userid as UserID, device.description as Desc, typemodel.name as ModelName, devicepool.name as DevicePool FROM device LEFT JOIN enduser ON device.fkenduser = enduser.pkid LEFT JOIN typemodel ON device.tkmodel = typemodel.enum LEFT JOIN devicepool ON device.fkdevicepool = devicepool.pkid WHERE "
				+ condition1String + " " + condition2String + "</sql>";
		System.out.println(substringBetween(data, "<sql>", "</sql>"));
		// } else {
		// data = data
		// +
		// "<sql>SELECT device.name,enduser.firstname,enduser.lastname,enduser.userid,device.description,device.tkmodel,device.fkdevicepool FROM device LEFT JOIN enduser ON device.fkenduser = enduser.pkid LEFT JOIN devicepool ON device.fkdevicepool = devicepool.pkid where "
		// + condition1String + " " + condition2String+"</sql>";
		// sqlString = "DEVICES: " +
		// "SELECT device.name,enduser.firstname,enduser.lastname,enduser.userid,device.description,device.tkmodel,device.fkdevicepool FROM device LEFT JOIN enduser ON device.fkenduser = enduser.pkid LEFT JOIN devicepool ON device.fkdevicepool = devicepool.pkid where "
		// + condition1String + " " + condition2String;
		// }
		// System.out.println(sqlString);
		// data = data
		// +
		// "<sql>SELECT device.name,enduser.firstname,enduser.lastname,enduser.userid,device.description,typemodel.name,devicepool.name FROM device LEFT JOIN enduser ON enduser.pkid = device.fkenduser LEFT JOIN typemodel ON typemodel.enum = device.tkmodel LEFT JOIN devicepool ON devicepool.pkid = device.fkdevicepool where device.name like 'SEP%'</sql>";
		data = data
				+ "</axl:executeSQLQuery> </SOAP-ENV:Body> </SOAP-ENV:Envelope>";

		String header = Methods.getHttpsHeader(data);
		header = header + data;
		StringBuffer sb = null;
		try {
			AXLTrustManager axl = new AXLTrustManager();
			// Implement the certificate-related stuffs required for sending
			// request via https
			X509TrustManager xtm = axl.new MyTrustManager();
			TrustManager[] mytm = { xtm };
			SSLContext ctx = SSLContext.getInstance("SSL");
			ctx.init(null, mytm, null);
			SSLSocketFactory sslFact = (SSLSocketFactory) ctx
					.getSocketFactory();
			socket = (SSLSocket) sslFact.createSocket(Methods.getKeys("ip"),
					Integer.parseInt("8443"));
			in = socket.getInputStream();
			bArray = new byte[2048];
			sb = new StringBuffer(2048);
			int ch = 0;
			int sum = 0;
			out = socket.getOutputStream();
			out.write(header.getBytes());
			while ((ch = in.read(bArray)) != -1) {
				sum += ch;
				sb.append(new String(bArray, 0, ch));
			}
			socket.close();
			string = sb.toString();
			// System.out.println("ALL: " + string);
			String replacePhoneName = string.replace("<devicename/>",
					"<devicename>NULL</devicename>");
			String replaceFirstName = replacePhoneName.replace("<firstname/>",
					"<firstname>NULL</firstname>");
			String replaceLastName = replaceFirstName.replace("<lastname/>",
					"<lastname>NULL</lastname>");
			String replaceUserID = replaceLastName.replace("<userid/>",
					"<userid><NONE></userid>");
			String replaceDesc = replaceUserID.replace("<desc/>",
					"<desc>NULL</desc>");
			String replaceModel = replaceDesc.replace("<modelname/>",
					"<modelname>NULL</modelname>");
			String replaceDevicepool = replaceModel.replace("<devicepool/>",
					"<devicepool>NULL</devicepool>");
			String finalString = replaceDevicepool;
			// System.out.println(finalString);
			Variables.phoneNames = Methods.substringsBetween(finalString,
					"<devicename>", "</devicename>");
			Variables.enduserFirstnames = Methods.substringsBetween(
					finalString, "<firstname>", "</firstname>");
			Variables.enduserLastnames = Methods.substringsBetween(finalString,
					"<lastname>", "</lastname>");
			Variables.phoneEndusers = Methods.substringsBetween(finalString,
					"<userid>", "</userid>");
			Variables.phoneDesc = Methods.substringsBetween(finalString,
					"<desc>", "</desc>");
			Variables.phoneModel = Methods.substringsBetween(finalString,
					"<modelname>", "</modelname>");
			Variables.phoneDevpool = Methods.substringsBetween(finalString,
					"<devicepool>", "</devicepool");
			Variables.deviceTableRows = new String[Variables.phoneNames.length][9];
			for (int i = 0; i < Variables.phoneNames.length; i++) {
				Variables.deviceTableRows[i][0] = Integer.toString(i + 1);
				Variables.deviceTableRows[i][1] = Variables.phoneNames[i];
				Variables.deviceTableRows[i][2] = Variables.enduserFirstnames[i];
				Variables.deviceTableRows[i][3] = Variables.enduserLastnames[i];
				Variables.deviceTableRows[i][4] = Variables.phoneEndusers[i];
				Variables.deviceTableRows[i][5] = Variables.phoneDesc[i];
				Variables.deviceTableRows[i][6] = Variables.phoneModel[i];
				Variables.deviceTableRows[i][7] = Variables.phoneDevpool[i];
				Variables.deviceTableRows[i][8] = "false";
			}
			CMUtilsGUI.logArea.append("Retrieved "
					+ Variables.phoneNames.length + " records! \n");
		} catch (UnknownHostException e) {
			System.err.println("Error connecting to host: " + e.getMessage());
			return false;
		} catch (IOException ioe) {
			System.err.println("Error sending/receiving from server: "
					+ ioe.getMessage());
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (Exception exc) {
				exc.printStackTrace();
				System.err.println("Error closing connection to server: "
						+ exc.getMessage());
			}
		} catch (Exception ea) {
			System.err.println("Unknown exception " + ea.getMessage());
			return false;
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (Exception exc) {
				exc.printStackTrace();
				System.err.println("Error closing connection to server: "
						+ exc.getMessage());
			}
		}
		return true;
	}

	public static boolean getNumplanAssoc() {
		String data = null;
		// String sAXLSOAPRequest = null;
		// String sAXLRequest = null;
		String string = null;
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		byte[] bArray = null;
		String condition1String = null;
		String condition2String = null;
		String Base64String = Base64.encode(Methods.getKeys("user") + ":"
				+ Methods.getKeys("pass"));
		// Build the HTTPS Header
		data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" ";
		data = data
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"> ";
		data = data
				+ "<SOAP-ENV:Body> <axl:executeSQLQuery xmlns:axl=\"http://www.cisco.com/AXL/7.0\" ";
		data = data
				+ " xsi:schemaLocation=\"http://www.cisco.com/AXL/1.0 http://gkar.cisco.com/schema/axlsoap.xsd\" ";
		data = data + "sequence=\"1234\">";
		// data = data +
		// "<sql>SELECT device.name,enduser.firstname,enduser.lastname,enduser.userid,device.description,typemodel.name,devicepool.name FROM device LEFT JOIN enduser ON enduser.pkid = device.fkenduser LEFT JOIN typemodel ON typemodel.enum = device.tkmodel LEFT JOIN devicepool ON devicepool.pkid = device.fkdevicepool where device.name like 'SEP%'</sql>";
		String sqlString = null;
		// if (condition1 != 3) {
		data = data
				+ "<sql>SELECT dnorpattern AS EXT from numplan where not exists ( select endusernumplanmap.pkid from endusernumplanmap where endusernumplanmap.fknumplan = numplan.pkid )</sql>";
		sqlString = "SELECT device.name as DeviceName, enduser.firstname as FirstName, enduser.lastname as LastName, enduser.userid as UserID, device.description as Desc, typemodel.name as ModelName, devicepool.name as DevicePool FROM device LEFT JOIN enduser ON device.fkenduser = enduser.pkid LEFT JOIN typemodel ON device.tkmodel = typemodel.enum LEFT JOIN devicepool ON device.fkdevicepool = devicepool.pkid WHERE "
				+ condition1String + " " + condition2String;
		// } else {
		// data = data
		// +
		// "<sql>SELECT device.name,enduser.firstname,enduser.lastname,enduser.userid,device.description,device.tkmodel,device.fkdevicepool FROM device LEFT JOIN enduser ON device.fkenduser = enduser.pkid LEFT JOIN devicepool ON device.fkdevicepool = devicepool.pkid where "
		// + condition1String + " " + condition2String+"</sql>";
		// sqlString = "DEVICES: " +
		// "SELECT device.name,enduser.firstname,enduser.lastname,enduser.userid,device.description,device.tkmodel,device.fkdevicepool FROM device LEFT JOIN enduser ON device.fkenduser = enduser.pkid LEFT JOIN devicepool ON device.fkdevicepool = devicepool.pkid where "
		// + condition1String + " " + condition2String;
		// }
		// System.out.println(sqlString);
		// data = data
		// +
		// "<sql>SELECT device.name,enduser.firstname,enduser.lastname,enduser.userid,device.description,typemodel.name,devicepool.name FROM device LEFT JOIN enduser ON enduser.pkid = device.fkenduser LEFT JOIN typemodel ON typemodel.enum = device.tkmodel LEFT JOIN devicepool ON devicepool.pkid = device.fkdevicepool where device.name like 'SEP%'</sql>";
		data = data
				+ "</axl:executeSQLQuery> </SOAP-ENV:Body> </SOAP-ENV:Envelope>";

		String header = Methods.getHttpsHeader(data);
		header = header + data;
		StringBuffer sb = null;
		try {
			AXLTrustManager axl = new AXLTrustManager();
			// Implement the certificate-related stuffs required for sending
			// request via https
			X509TrustManager xtm = axl.new MyTrustManager();
			TrustManager[] mytm = { xtm };
			SSLContext ctx = SSLContext.getInstance("SSL");
			ctx.init(null, mytm, null);
			SSLSocketFactory sslFact = (SSLSocketFactory) ctx
					.getSocketFactory();
			socket = (SSLSocket) sslFact.createSocket(Methods.getKeys("ip"),
					Integer.parseInt("8443"));
			in = socket.getInputStream();
			bArray = new byte[2048];
			sb = new StringBuffer(2048);
			int ch = 0;
			int sum = 0;
			out = socket.getOutputStream();
			out.write(header.getBytes());
			while ((ch = in.read(bArray)) != -1) {
				sum += ch;
				sb.append(new String(bArray, 0, ch));
			}
			socket.close();
			string = sb.toString();
			// System.out.println("ALL: " + string);
			String replaceDnorPattern = string.replace("<ext/>",
					"<ext>NULL</ext>");
			String finalString = replaceDnorPattern;
			// System.out.println(finalString);
			Variables.dnorPatterns = Methods.substringsBetween(finalString,
					"<ext>", "</ext>");
			Variables.lineAssocTableRows = new String[Variables.dnorPatterns.length][2];
			for (int i = 0; i < Variables.dnorPatterns.length; i++) {
				Variables.lineAssocTableRows[i][0] = Integer.toString(i + 1);
				Variables.lineAssocTableRows[i][1] = Variables.dnorPatterns[i];
			}
			CMUtilsGUI.logArea.append("Retrieved "
					+ Variables.dnorPatterns.length + " records! \n");
		} catch (UnknownHostException e) {
			System.err.println("Error connecting to host: " + e.getMessage());
			return false;
		} catch (IOException ioe) {
			System.err.println("Error sending/receiving from server: "
					+ ioe.getMessage());
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (Exception exc) {
				exc.printStackTrace();
				System.err.println("Error closing connection to server: "
						+ exc.getMessage());
			}
		} catch (Exception ea) {
			System.err.println("Unknown exception " + ea.getMessage());
			return false;
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (Exception exc) {
				exc.printStackTrace();
				System.err.println("Error closing connection to server: "
						+ exc.getMessage());
			}
		}
		return true;
	}

	public static boolean getDeviceAssocDevices() {
		String data = null;
		// String sAXLSOAPRequest = null;
		// String sAXLRequest = null;
		String string = null;
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		byte[] bArray = null;
		String condition1String = null;
		String condition2String = null;
		String Base64String = Base64.encode(Methods.getKeys("user") + ":"
				+ Methods.getKeys("pass"));
		// Build the HTTPS Header
		data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" ";
		data = data
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"> ";
		data = data
				+ "<SOAP-ENV:Body> <axl:executeSQLQuery xmlns:axl=\"http://www.cisco.com/AXL/7.0\" ";
		data = data
				+ " xsi:schemaLocation=\"http://www.cisco.com/AXL/1.0 http://gkar.cisco.com/schema/axlsoap.xsd\" ";
		data = data + "sequence=\"1234\">";
		// data = data +
		// "<sql>SELECT device.name,enduser.firstname,enduser.lastname,enduser.userid,device.description,typemodel.name,devicepool.name FROM device LEFT JOIN enduser ON enduser.pkid = device.fkenduser LEFT JOIN typemodel ON typemodel.enum = device.tkmodel LEFT JOIN devicepool ON devicepool.pkid = device.fkdevicepool where device.name like 'SEP%'</sql>";
		String sqlString = null;
		// if (condition1 != 3) {
		data = data
				+ "<sql>SELECT device.name AS DeviceName from device WHERE NOT EXISTS ( SELECT * from enduserdevicemap where enduserdevicemap.fkdevice = device.pkid AND device.fkenduser IS NOT NULL )</sql>";
		sqlString = "SELECT device.name as DeviceName, enduser.firstname as FirstName, enduser.lastname as LastName, enduser.userid as UserID, device.description as Desc, typemodel.name as ModelName, devicepool.name as DevicePool FROM device LEFT JOIN enduser ON device.fkenduser = enduser.pkid LEFT JOIN typemodel ON device.tkmodel = typemodel.enum LEFT JOIN devicepool ON device.fkdevicepool = devicepool.pkid WHERE "
				+ condition1String + " " + condition2String;
		// } else {
		// data = data
		// +
		// "<sql>SELECT device.name,enduser.firstname,enduser.lastname,enduser.userid,device.description,device.tkmodel,device.fkdevicepool FROM device LEFT JOIN enduser ON device.fkenduser = enduser.pkid LEFT JOIN devicepool ON device.fkdevicepool = devicepool.pkid where "
		// + condition1String + " " + condition2String+"</sql>";
		// sqlString = "DEVICES: " +
		// "SELECT device.name,enduser.firstname,enduser.lastname,enduser.userid,device.description,device.tkmodel,device.fkdevicepool FROM device LEFT JOIN enduser ON device.fkenduser = enduser.pkid LEFT JOIN devicepool ON device.fkdevicepool = devicepool.pkid where "
		// + condition1String + " " + condition2String;
		// }
		// System.out.println(sqlString);
		// data = data
		// +
		// "<sql>SELECT device.name,enduser.firstname,enduser.lastname,enduser.userid,device.description,typemodel.name,devicepool.name FROM device LEFT JOIN enduser ON enduser.pkid = device.fkenduser LEFT JOIN typemodel ON typemodel.enum = device.tkmodel LEFT JOIN devicepool ON devicepool.pkid = device.fkdevicepool where device.name like 'SEP%'</sql>";
		data = data
				+ "</axl:executeSQLQuery> </SOAP-ENV:Body> </SOAP-ENV:Envelope>";

		String header = Methods.getHttpsHeader(data);
		header = header + data;
		StringBuffer sb = null;
		try {
			AXLTrustManager axl = new AXLTrustManager();
			// Implement the certificate-related stuffs required for sending
			// request via https
			X509TrustManager xtm = axl.new MyTrustManager();
			TrustManager[] mytm = { xtm };
			SSLContext ctx = SSLContext.getInstance("SSL");
			ctx.init(null, mytm, null);
			SSLSocketFactory sslFact = (SSLSocketFactory) ctx
					.getSocketFactory();
			socket = (SSLSocket) sslFact.createSocket(Methods.getKeys("ip"),
					Integer.parseInt("8443"));
			in = socket.getInputStream();
			bArray = new byte[2048];
			sb = new StringBuffer(2048);
			int ch = 0;
			int sum = 0;
			out = socket.getOutputStream();
			out.write(header.getBytes());
			while ((ch = in.read(bArray)) != -1) {
				sum += ch;
				sb.append(new String(bArray, 0, ch));
			}
			socket.close();
			string = sb.toString();
			// System.out.println("ALL: " + string);
			String replaceDevicename = string.replace("<devicename/>",
					"<devicename>NULL</devicename>");
			String finalString = replaceDevicename;
			Variables.devAssocDevicenames = Methods.substringsBetween(
					finalString, "<devicename>", "</devicename>");
			Variables.devAssocDeviceRows = new String[Variables.devAssocDevicenames.length][1];
			for (int i = 0; i < Variables.devAssocDevicenames.length; i++) {
				Variables.devAssocDeviceRows[i][0] = Variables.devAssocDevicenames[i];
			}
		} catch (UnknownHostException e) {
			System.err.println("Error connecting to host: " + e.getMessage());
			return false;
		} catch (IOException ioe) {
			System.err.println("Error sending/receiving from server: "
					+ ioe.getMessage());
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (Exception exc) {
				exc.printStackTrace();
				System.err.println("Error closing connection to server: "
						+ exc.getMessage());
			}
		} catch (Exception ea) {
			System.err.println("Unknown exception " + ea.getMessage());
			return false;
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (Exception exc) {
				exc.printStackTrace();
				System.err.println("Error closing connection to server: "
						+ exc.getMessage());
			}
		}
		return true;
	}

	public static boolean getDeviceAssoc() {
		String data = null;
		// String sAXLSOAPRequest = null;
		// String sAXLRequest = null;
		String string = null;
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		byte[] bArray = null;
		String condition1String = null;
		String condition2String = null;
		String Base64String = Base64.encode(Methods.getKeys("user") + ":"
				+ Methods.getKeys("pass"));
		// Build the HTTPS Header
		data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" ";
		data = data
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"> ";
		data = data
				+ "<SOAP-ENV:Body> <axl:executeSQLQuery xmlns:axl=\"http://www.cisco.com/AXL/7.0\" ";
		data = data
				+ " xsi:schemaLocation=\"http://www.cisco.com/AXL/1.0 http://gkar.cisco.com/schema/axlsoap.xsd\" ";
		data = data + "sequence=\"1234\">";
		// data = data +
		// "<sql>SELECT device.name,enduser.firstname,enduser.lastname,enduser.userid,device.description,typemodel.name,devicepool.name FROM device LEFT JOIN enduser ON enduser.pkid = device.fkenduser LEFT JOIN typemodel ON typemodel.enum = device.tkmodel LEFT JOIN devicepool ON devicepool.pkid = device.fkdevicepool where device.name like 'SEP%'</sql>";
		String sqlString = null;
		// if (condition1 != 3) {
		data = data
				+ "<sql>SELECT enduser.userid AS userid, enduser.firstname AS firstname, enduser.lastname AS lastname from enduser WHERE NOT EXISTS ( SELECT * from enduserdevicemap WHERE enduserdevicemap.fkenduser = enduser.pkid )</sql>";
		sqlString = "SELECT device.name as DeviceName, enduser.firstname as FirstName, enduser.lastname as LastName, enduser.userid as UserID, device.description as Desc, typemodel.name as ModelName, devicepool.name as DevicePool FROM device LEFT JOIN enduser ON device.fkenduser = enduser.pkid LEFT JOIN typemodel ON device.tkmodel = typemodel.enum LEFT JOIN devicepool ON device.fkdevicepool = devicepool.pkid WHERE "
				+ condition1String + " " + condition2String;
		// } else {
		// data = data
		// +
		// "<sql>SELECT device.name,enduser.firstname,enduser.lastname,enduser.userid,device.description,device.tkmodel,device.fkdevicepool FROM device LEFT JOIN enduser ON device.fkenduser = enduser.pkid LEFT JOIN devicepool ON device.fkdevicepool = devicepool.pkid where "
		// + condition1String + " " + condition2String+"</sql>";
		// sqlString = "DEVICES: " +
		// "SELECT device.name,enduser.firstname,enduser.lastname,enduser.userid,device.description,device.tkmodel,device.fkdevicepool FROM device LEFT JOIN enduser ON device.fkenduser = enduser.pkid LEFT JOIN devicepool ON device.fkdevicepool = devicepool.pkid where "
		// + condition1String + " " + condition2String;
		// }
		// System.out.println(sqlString);
		// data = data
		// +
		// "<sql>SELECT device.name,enduser.firstname,enduser.lastname,enduser.userid,device.description,typemodel.name,devicepool.name FROM device LEFT JOIN enduser ON enduser.pkid = device.fkenduser LEFT JOIN typemodel ON typemodel.enum = device.tkmodel LEFT JOIN devicepool ON devicepool.pkid = device.fkdevicepool where device.name like 'SEP%'</sql>";
		data = data
				+ "</axl:executeSQLQuery> </SOAP-ENV:Body> </SOAP-ENV:Envelope>";

		String header = Methods.getHttpsHeader(data);
		header = header + data;
		StringBuffer sb = null;
		try {
			AXLTrustManager axl = new AXLTrustManager();
			// Implement the certificate-related stuffs required for sending
			// request via https
			X509TrustManager xtm = axl.new MyTrustManager();
			TrustManager[] mytm = { xtm };
			SSLContext ctx = SSLContext.getInstance("SSL");
			ctx.init(null, mytm, null);
			SSLSocketFactory sslFact = (SSLSocketFactory) ctx
					.getSocketFactory();
			socket = (SSLSocket) sslFact.createSocket(Methods.getKeys("ip"),
					Integer.parseInt("8443"));
			in = socket.getInputStream();
			bArray = new byte[2048];
			sb = new StringBuffer(2048);
			int ch = 0;
			int sum = 0;
			out = socket.getOutputStream();
			out.write(header.getBytes());
			while ((ch = in.read(bArray)) != -1) {
				sum += ch;
				sb.append(new String(bArray, 0, ch));
			}
			socket.close();
			string = sb.toString();
			// System.out.println("ALL: " + string);
			String replaceUserid = string.replace("<userid/>",
					"<userid>NULL</userid>");
			String replaceFirstname = replaceUserid.replace("<firstname/>",
					"<firstname>NULL</firstname>");
			String replaceLastname = replaceFirstname.replace("<lastname/>",
					"<lastname>NULL</lastname>");
			String finalString = replaceLastname;
			Variables.devAssocUserids = Methods.substringsBetween(finalString,
					"<userid>", "</userid>");
			Variables.devAssocFirstnames = Methods.substringsBetween(
					finalString, "<firstname>", "</firstname>");
			Variables.devAssocLastnames = Methods.substringsBetween(
					finalString, "<lastname>", "</lastname>");
			Variables.deviceAssocTableRows = new String[Variables.devAssocUserids.length][5];
			for (int i = 0; i < Variables.devAssocUserids.length; i++) {
				Variables.deviceAssocTableRows[i][0] = Integer.toString(i + 1);
				Variables.deviceAssocTableRows[i][2] = Variables.devAssocUserids[i];
				Variables.deviceAssocTableRows[i][3] = Variables.devAssocFirstnames[i];
				Variables.deviceAssocTableRows[i][4] = Variables.devAssocLastnames[i];
			}
			CMUtilsGUI.logArea.append("Retrieved "
					+ Variables.devAssocUserids.length + " records! \n");
		} catch (UnknownHostException e) {
			System.err.println("Error connecting to host: " + e.getMessage());
			return false;
		} catch (IOException ioe) {
			System.err.println("Error sending/receiving from server: "
					+ ioe.getMessage());
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (Exception exc) {
				exc.printStackTrace();
				System.err.println("Error closing connection to server: "
						+ exc.getMessage());
			}
		} catch (Exception ea) {
			System.err.println("Unknown exception " + ea.getMessage());
			return false;
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (Exception exc) {
				exc.printStackTrace();
				System.err.println("Error closing connection to server: "
						+ exc.getMessage());
			}
		}
		return true;
	}

	public static boolean getAllEndusers() {
		String data = null;
		// String sAXLSOAPRequest = null;
		// String sAXLRequest = null;
		String string = null;
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		byte[] bArray = null;
		String Base64String = Base64.encode(Methods.getKeys("user") + ":"
				+ Methods.getKeys("pass"));
		// Build the HTTPS Header
		data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" ";
		data = data
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"> ";
		data = data
				+ "<SOAP-ENV:Body> <axl:executeSQLQuery xmlns:axl=\"http://www.cisco.com/AXL/7.0\" ";
		data = data
				+ " xsi:schemaLocation=\"http://www.cisco.com/AXL/1.0 http://gkar.cisco.com/schema/axlsoap.xsd\" ";
		data = data + "sequence=\"1234\">";

		data = data
				+ "<sql>SELECT enduser.userid,enduser.firstname,enduser.lastname from enduser</sql>";
		data = data
				+ "</axl:executeSQLQuery> </SOAP-ENV:Body> </SOAP-ENV:Envelope>";

		String header = Methods.getHttpsHeader(data);
		header = header + data;
		StringBuffer sb = null;
		try {
			AXLTrustManager axl = new AXLTrustManager();
			// Implement the certificate-related stuffs required for sending
			// request via https
			X509TrustManager xtm = axl.new MyTrustManager();
			TrustManager[] mytm = { xtm };
			SSLContext ctx = SSLContext.getInstance("SSL");
			ctx.init(null, mytm, null);
			SSLSocketFactory sslFact = (SSLSocketFactory) ctx
					.getSocketFactory();
			socket = (SSLSocket) sslFact.createSocket(Methods.getKeys("ip"),
					Integer.parseInt("8443"));
			in = socket.getInputStream();
			bArray = new byte[2048];
			sb = new StringBuffer(2048);
			int ch = 0;
			int sum = 0;
			out = socket.getOutputStream();
			out.write(header.getBytes());
			while ((ch = in.read(bArray)) != -1) {
				sum += ch;
				sb.append(new String(bArray, 0, ch));
			}
			socket.close();
			string = sb.toString();
			String replaceUserID = string.replace("<userid/>",
					"<userid>NULL</userid>");
			String replaceFirstname = replaceUserID.replace("<firstname/>",
					"<firstname>NULL</firstname>");
			String replaceLastname = replaceFirstname.replace("<lastname/>",
					"<lastname>NULL</lastname>");
			String finalString = replaceLastname;
			// System.out.println(finalString);
			Variables.phoneAllEndusersIDs = Methods.substringsBetween(
					finalString, "<userid>", "</userid");
			Variables.phoneAllEndusersFirstnames = Methods.substringsBetween(
					finalString, "<firstname>", "</firstname>");
			Variables.phoneAllEndusersLastnames = Methods.substringsBetween(
					finalString, "<lastname>", "</lastname>");

			Variables.enduserRows = new String[Variables.phoneAllEndusersIDs.length][3];
			for (int i = 0; i < Variables.phoneAllEndusersIDs.length; i++) {
				Variables.enduserRows[i][0] = Variables.phoneAllEndusersIDs[i];
				Variables.enduserRows[i][1] = Variables.phoneAllEndusersFirstnames[i];
				Variables.enduserRows[i][2] = Variables.phoneAllEndusersLastnames[i];
			}
		} catch (UnknownHostException e) {
			System.err.println("Error connecting to host: " + e.getMessage());
			return false;
		} catch (IOException ioe) {
			System.err.println("Error sending/receiving from server: "
					+ ioe.getMessage());
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (Exception exc) {
				exc.printStackTrace();
				System.err.println("Error closing connection to server: "
						+ exc.getMessage());
			}
		} catch (Exception ea) {
			System.err.println("Unknown exception " + ea.getMessage());
			return false;
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (Exception exc) {
				exc.printStackTrace();
				System.err.println("Error closing connection to server: "
						+ exc.getMessage());
			}
		}
		return true;
	}

	public static String executeSQLSelect(String sqlSelectString) {
		String data = null;
		// String sAXLSOAPRequest = null;
		// String sAXLRequest = null;
		String string = null;
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		byte[] bArray = null;
		String Base64String = Base64.encode(Methods.getKeys("user") + ":"
				+ Methods.getKeys("pass"));
		// Build the HTTPS Header
		data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" ";
		data = data
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"> ";
		data = data
				+ "<SOAP-ENV:Body> <axl:executeSQLQuery xmlns:axl=\"http://www.cisco.com/AXL/7.0\" ";
		data = data
				+ " xsi:schemaLocation=\"http://www.cisco.com/AXL/1.0 http://gkar.cisco.com/schema/axlsoap.xsd\" ";
		data = data + "sequence=\"1234\">";

		data = data + "<sql>" + sqlSelectString + "</sql>";
		data = data
				+ "</axl:executeSQLQuery> </SOAP-ENV:Body> </SOAP-ENV:Envelope>";
		// System.out.println("SQLSelectString: " + sqlSelectString);
		String header = Methods.getHttpsHeader(data);
		header = header + data;
		StringBuffer sb = null;
		try {
			AXLTrustManager axl = new AXLTrustManager();
			// Implement the certificate-related stuffs required for sending
			// request via https
			X509TrustManager xtm = axl.new MyTrustManager();
			TrustManager[] mytm = { xtm };
			SSLContext ctx = SSLContext.getInstance("SSL");
			ctx.init(null, mytm, null);
			SSLSocketFactory sslFact = (SSLSocketFactory) ctx
					.getSocketFactory();
			socket = (SSLSocket) sslFact.createSocket(Methods.getKeys("ip"),
					Integer.parseInt("8443"));
			in = socket.getInputStream();
			bArray = new byte[2048];
			sb = new StringBuffer(2048);
			int ch = 0;
			int sum = 0;
			out = socket.getOutputStream();
			out.write(header.getBytes());
			while ((ch = in.read(bArray)) != -1) {
				sum += ch;
				sb.append(new String(bArray, 0, ch));
			}
			socket.close();
			string = sb.toString();
		} catch (UnknownHostException e) {
			System.err.println("Error connecting to host: " + e.getMessage());
			return "Error connecting to host: " + e.getMessage();
		} catch (IOException ioe) {
			System.err.println("Error sending/receiving from server: "
					+ ioe.getMessage());
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (Exception exc) {
				exc.printStackTrace();
				System.err.println("Error closing connection to server: "
						+ exc.getMessage());
			}
		} catch (Exception ea) {
			System.err.println("Unknown exception " + ea.getMessage());
			return "Unknown exception " + ea.getMessage();
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (Exception exc) {
				exc.printStackTrace();
				System.err.println("Error closing connection to server: "
						+ exc.getMessage());
			}
		}
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><return>"
				+ Methods.substringBetween(string, "<return>", "</return>")
				+ "</return>";
	}

	public static boolean executeSQLUpdate(String sqlUpdateString) {
		String data = null;
		// String sAXLSOAPRequest = null;
		// String sAXLRequest = null;
		String string = null;
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		byte[] bArray = null;
		String Base64String = Base64.encode(Methods.getKeys("user") + ":"
				+ Methods.getKeys("pass"));
		// Build the HTTPS Header
		data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" ";
		data = data
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"> ";
		data = data
				+ "<SOAP-ENV:Body> <axl:executeSQLUpdate xmlns:axl=\"http://www.cisco.com/AXL/7.0\" ";
		data = data
				+ " xsi:schemaLocation=\"http://www.cisco.com/AXL/1.0 http://gkar.cisco.com/schema/axlsoap.xsd\" ";
		data = data + "sequence=\"1234\">";

		data = data + "<sql>" + sqlUpdateString + "</sql>";
		data = data
				+ "</axl:executeSQLUpdate> </SOAP-ENV:Body> </SOAP-ENV:Envelope>";

		String header = Methods.getHttpsHeader(data);
		header = header + data;
		StringBuffer sb = null;
		try {
			AXLTrustManager axl = new AXLTrustManager();
			// Implement the certificate-related stuffs required for sending
			// request via https
			X509TrustManager xtm = axl.new MyTrustManager();
			TrustManager[] mytm = { xtm };
			SSLContext ctx = SSLContext.getInstance("SSL");
			ctx.init(null, mytm, null);
			SSLSocketFactory sslFact = (SSLSocketFactory) ctx
					.getSocketFactory();
			socket = (SSLSocket) sslFact.createSocket(Methods.getKeys("ip"),
					Integer.parseInt("8443"));
			in = socket.getInputStream();
			bArray = new byte[2048];
			sb = new StringBuffer(2048);
			int ch = 0;
			int sum = 0;
			out = socket.getOutputStream();
			out.write(header.getBytes());
			while ((ch = in.read(bArray)) != -1) {
				sum += ch;
				sb.append(new String(bArray, 0, ch));
			}
			socket.close();
			string = sb.toString();
		} catch (UnknownHostException e) {
			System.err.println("Error connecting to host: " + e.getMessage());
			return false;
		} catch (IOException ioe) {
			System.err.println("Error sending/receiving from server: "
					+ ioe.getMessage());
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (Exception exc) {
				exc.printStackTrace();
				System.err.println("Error closing connection to server: "
						+ exc.getMessage());
			}
		} catch (Exception ea) {
			System.err.println("Unknown exception " + ea.getMessage());
			return false;
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (Exception exc) {
				exc.printStackTrace();
				System.err.println("Error closing connection to server: "
						+ exc.getMessage());
			}
		}
		return true;
	}
}
