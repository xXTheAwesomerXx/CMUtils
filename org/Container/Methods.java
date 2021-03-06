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
import javax.swing.JScrollBar;

import org.CMUtils.CMUtilsGUI;

public class Methods {

	// Method: getKeys
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

	// Method: substringBetween
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

	// Method: substringsBetween
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
		List<String> list = new ArrayList<String>();
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

	// Method: substringsBetween (Delimiter)
	public static String[] substringsBetween(String str, String open,
			String close, String delimiter) {
		if ((str == null) || (open == null) || (close == null)) {
			return null;
		}
		int strLen = str.length();
		if (strLen == 0) {
			return null;
		}
		int closeLen = close.length();
		int openLen = open.length();
		List<String> list = new ArrayList<String>();
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
			if (!str.substring(start, end).contains(delimiter)) {
				list.add(str.substring(start, end));

			}
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

	public static final String getEnvelope(String sqlQuery) {
		String data;
		data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" ";
		data = data
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"> ";
		data = data
				+ "<SOAP-ENV:Body> <axl:executeSQLQuery xmlns:axl=\"http://www.cisco.com/AXL/7.0\" ";
		data = data
				+ " xsi:schemaLocation=\"http://www.cisco.com/AXL/1.0 http://gkar.cisco.com/schema/axlsoap.xsd\" ";
		data = data + "sequence=\"1234\">";
		data = data + "<sql>" + sqlQuery + "</sql>";
		data = data
				+ "</axl:executeSQLQuery> </SOAP-ENV:Body> </SOAP-ENV:Envelope>";
		return data;
	}

	public static boolean testConnection() {
		String data = getEnvelope("SELECT * FROM numplan");
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		byte[] bArray = null;
		String SBuffer;
//		 data =
//		 "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" ";
//		 data = data
//		 +
//		 "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"> ";
//		 data = data
//		 +
//		 "<SOAP-ENV:Body> <axl:executeSQLQuery xmlns:axl=\"http://www.cisco.com/AXL/7.0\" ";
//		 data = data
//		 +
//		 " xsi:schemaLocation=\"http://www.cisco.com/AXL/1.0 http://gkar.cisco.com/schema/axlsoap.xsd\" ";
//		 data = data + "sequence=\"1234\">";
//		 data = data + "<sql>SELECT * FROM numplan</sql>";
//		 data = data
//		 + "</axl:executeSQLQuery> </SOAP-ENV:Body> </SOAP-ENV:Envelope>";

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
			out = socket.getOutputStream();
			out.write(header.getBytes());
			while ((ch = in.read(bArray)) != -1) {
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
		String string = null;
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		byte[] bArray = null;
		data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" ";
		data = data
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"> ";
		data = data
				+ "<SOAP-ENV:Body> <axl:executeSQLUpdate xmlns:axl=\"http://www.cisco.com/AXL/7.0\" ";
		data = data
				+ " xsi:schemaLocation=\"http://www.cisco.com/AXL/1.0 http://gkar.cisco.com/schema/axlsoap.xsd\" ";
		data = data + "sequence=\"1234\">";
		data = data
				+ "<sql>update device set device.fkenduser = ( select pkid from enduser where enduser.userid = '"
				+ arg1 + "' ) where device.name = '" + arg2 + "'</sql>";
		data = data
				+ "</axl:executeSQLUpdate> </SOAP-ENV:Body> </SOAP-ENV:Envelope>";

		String header = Methods.getHttpsHeader(data);
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
			socket = (SSLSocket) sslFact.createSocket(Methods.getKeys("ip"),
					Integer.parseInt("8443"));
			in = socket.getInputStream();
			bArray = new byte[2048];
			sb = new StringBuffer(2048);
			int ch = 0;
			out = socket.getOutputStream();
			out.write(header.getBytes());
			while ((ch = in.read(bArray)) != -1) {
				sb.append(new String(bArray, 0, ch));
			}
			socket.close();
			string = sb.toString();
			String replacePhoneName = string.replace("<name/>",
					"<name>N/A</name>");
			String replaceFirstName = replacePhoneName.replace("<firstname/>",
					"<firstname>N/A</firstname>");
			String replaceLastName = replaceFirstName.replace("<lastname/>",
					"<lastname>N/A</lastname>");
			String replaceUserID = replaceLastName.replace("<userid/>",
					"<userid><NONE></userid>");
			String replaceDesc = replaceUserID.replace("<description/>",
					"<description>N/A</description>");
			String replaceModel = replaceDesc.replace("<tkmodel/>",
					"<tkmodel>N/A</tkmodel>");
			String replaceDevicepool = replaceModel.replace("<fkdevicepool/>",
					"<fkdevicepool>N/A</fkdevicepool>");
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
		String string = null;
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		byte[] bArray = null;
		data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" ";
		data = data
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"> ";
		data = data
				+ "<SOAP-ENV:Body> <axl:executeSQLUpdate xmlns:axl=\"http://www.cisco.com/AXL/7.0\" ";
		data = data
				+ " xsi:schemaLocation=\"http://www.cisco.com/AXL/1.0 http://gkar.cisco.com/schema/axlsoap.xsd\" ";
		data = data + "sequence=\"1234\">";
		data = data
				+ "<sql>INSERT INTO enduserdevicemap (enduserdevicemap.fkenduser,enduserdevicemap.fkdevice,enduserdevicemap.tkuserassociation) SELECT enduser.pkid,device.pkid,typeuserassociation.enum FROM enduser,device,typeuserassociation WHERE enduser.userid = '"
				+ value1 + "' and device.name = '" + value2
				+ "' and typeuserassociation.enum = 1</sql>";
		data = data
				+ "</axl:executeSQLUpdate> </SOAP-ENV:Body> </SOAP-ENV:Envelope>";

		String header = Methods.getHttpsHeader(data);
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
			socket = (SSLSocket) sslFact.createSocket(Methods.getKeys("ip"),
					Integer.parseInt("8443"));
			in = socket.getInputStream();
			bArray = new byte[2048];
			sb = new StringBuffer(2048);
			int ch = 0;
			out = socket.getOutputStream();
			out.write(header.getBytes());
			while ((ch = in.read(bArray)) != -1) {
				sb.append(new String(bArray, 0, ch));
			}
			socket.close();
			string = sb.toString();
			String replacePhoneName = string.replace("<name/>",
					"<name>N/A</name>");
			String replaceFirstName = replacePhoneName.replace("<firstname/>",
					"<firstname>N/A</firstname>");
			String replaceLastName = replaceFirstName.replace("<lastname/>",
					"<lastname>N/A</lastname>");
			String replaceUserID = replaceLastName.replace("<userid/>",
					"<userid><NONE></userid>");
			String replaceDesc = replaceUserID.replace("<description/>",
					"<description>N/A</description>");
			String replaceModel = replaceDesc.replace("<tkmodel/>",
					"<tkmodel>N/A</tkmodel>");
			String replaceDevicepool = replaceModel.replace("<fkdevicepool/>",
					"<fkdevicepool>N/A</fkdevicepool>");
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
		String string = null;
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		byte[] bArray = null;
		data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" ";
		data = data
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"> ";
		data = data
				+ "<SOAP-ENV:Body> <axl:executeSQLUpdate xmlns:axl=\"http://www.cisco.com/AXL/7.0\" ";
		data = data
				+ " xsi:schemaLocation=\"http://www.cisco.com/AXL/1.0 http://gkar.cisco.com/schema/axlsoap.xsd\" ";
		data = data + "sequence=\"1234\">";
		data = data
				+ "<sql>DELETE * from enduserdevicemap</sql>";
		data = data
				+ "</axl:executeSQLUpdate> </SOAP-ENV:Body> </SOAP-ENV:Envelope>";

		String header = Methods.getHttpsHeader(data);
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
			socket = (SSLSocket) sslFact.createSocket(Methods.getKeys("ip"),
					Integer.parseInt("8443"));
			in = socket.getInputStream();
			bArray = new byte[2048];
			sb = new StringBuffer(2048);
			int ch = 0;
			out = socket.getOutputStream();
			out.write(header.getBytes());
			while ((ch = in.read(bArray)) != -1) {
				sb.append(new String(bArray, 0, ch));
			}
			socket.close();
			string = sb.toString();
			String replacePhoneName = string.replace("<name/>",
					"<name>N/A</name>");
			String replaceFirstName = replacePhoneName.replace("<firstname/>",
					"<firstname>N/A</firstname>");
			String replaceLastName = replaceFirstName.replace("<lastname/>",
					"<lastname>N/A</lastname>");
			String replaceUserID = replaceLastName.replace("<userid/>",
					"<userid><NONE></userid>");
			String replaceDesc = replaceUserID.replace("<description/>",
					"<description>N/A</description>");
			String replaceModel = replaceDesc.replace("<tkmodel/>",
					"<tkmodel>N/A</tkmodel>");
			String replaceDevicepool = replaceModel.replace("<fkdevicepool/>",
					"<fkdevicepool>N/A</fkdevicepool>");
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

	// TODO: Add method to remove endusernumplanmap
	public static boolean removeEnduserNumplanMap(String value1) {
		String data = null;
		String string = null;
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		byte[] bArray = null;
		data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" ";
		data = data
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"> ";
		data = data
				+ "<SOAP-ENV:Body> <axl:executeSQLUpdate xmlns:axl=\"http://www.cisco.com/AXL/7.0\" ";
		data = data
				+ " xsi:schemaLocation=\"http://www.cisco.com/AXL/1.0 http://gkar.cisco.com/schema/axlsoap.xsd\" ";
		data = data + "sequence=\"1234\">";
		data = data
				+ "<sql>DELETE from endusernumplanmap WHERE fkdevice IS NOT ( SELECT pkid from device where name = '"
				+ value1 + "' )</sql>";
		data = data
				+ "</axl:executeSQLUpdate> </SOAP-ENV:Body> </SOAP-ENV:Envelope>";

		String header = Methods.getHttpsHeader(data);
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
			socket = (SSLSocket) sslFact.createSocket(Methods.getKeys("ip"),
					Integer.parseInt("8443"));
			in = socket.getInputStream();
			bArray = new byte[2048];
			sb = new StringBuffer(2048);
			int ch = 0;
			out = socket.getOutputStream();
			out.write(header.getBytes());
			while ((ch = in.read(bArray)) != -1) {
				sb.append(new String(bArray, 0, ch));
			}
			socket.close();
			string = sb.toString();
			String replacePhoneName = string.replace("<name/>",
					"<name>N/A</name>");
			String replaceFirstName = replacePhoneName.replace("<firstname/>",
					"<firstname>N/A</firstname>");
			String replaceLastName = replaceFirstName.replace("<lastname/>",
					"<lastname>N/A</lastname>");
			String replaceUserID = replaceLastName.replace("<userid/>",
					"<userid><NONE></userid>");
			String replaceDesc = replaceUserID.replace("<description/>",
					"<description>N/A</description>");
			String replaceModel = replaceDesc.replace("<tkmodel/>",
					"<tkmodel>N/A</tkmodel>");
			String replaceDevicepool = replaceModel.replace("<fkdevicepool/>",
					"<fkdevicepool>N/A</fkdevicepool>");
			String finalString = replaceDevicepool;
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
		String string = null;
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		byte[] bArray = null;
		data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" ";
		data = data
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"> ";
		data = data
				+ "<SOAP-ENV:Body> <axl:executeSQLUpdate xmlns:axl=\"http://www.cisco.com/AXL/7.0\" ";
		data = data
				+ " xsi:schemaLocation=\"http://www.cisco.com/AXL/1.0 http://gkar.cisco.com/schema/axlsoap.xsd\" ";
		data = data + "sequence=\"1234\">";
		data = data
				+ "<sql>insert into numplan (dnorpattern,tkpatternusage) VALUES ("
				+ value1 + ",2)</sql>";
		data = data
				+ "</axl:executeSQLUpdate> </SOAP-ENV:Body> </SOAP-ENV:Envelope>";

		String header = Methods.getHttpsHeader(data);
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
			socket = (SSLSocket) sslFact.createSocket(Methods.getKeys("ip"),
					Integer.parseInt("8443"));
			in = socket.getInputStream();
			bArray = new byte[2048];
			sb = new StringBuffer(2048);
			int ch = 0;
			out = socket.getOutputStream();
			out.write(header.getBytes());
			while ((ch = in.read(bArray)) != -1) {
				sb.append(new String(bArray, 0, ch));
			}
			socket.close();
			string = sb.toString();
			String replacePhoneName = string.replace("<name/>",
					"<name>N/A</name>");
			String replaceFirstName = replacePhoneName.replace("<firstname/>",
					"<firstname>N/A</firstname>");
			String replaceLastName = replaceFirstName.replace("<lastname/>",
					"<lastname>N/A</lastname>");
			String replaceUserID = replaceLastName.replace("<userid/>",
					"<userid><NONE></userid>");
			String replaceDesc = replaceUserID.replace("<description/>",
					"<description>N/A</description>");
			String replaceModel = replaceDesc.replace("<tkmodel/>",
					"<tkmodel>N/A</tkmodel>");
			String replaceDevicepool = replaceModel.replace("<fkdevicepool/>",
					"<fkdevicepool>N/A</fkdevicepool>");
			String finalString = replaceDevicepool;
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
		String string = null;
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		byte[] bArray = null;
		data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" ";
		data = data
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"> ";
		data = data
				+ "<SOAP-ENV:Body> <axl:executeSQLUpdate xmlns:axl=\"http://www.cisco.com/AXL/7.0\" ";
		data = data
				+ " xsi:schemaLocation=\"http://www.cisco.com/AXL/1.0 http://gkar.cisco.com/schema/axlsoap.xsd\" ";
		data = data + "sequence=\"1234\">";
		data = data
				+ "<sql>insert into endusernumplanmap (fkenduser,fknumplan,tkdnusage) select enduser.pkid, numplan.pkid, typednusage.enum from enduser,numplan,typednusage where enduser.userid = '"
				+ value1
				+ "' and numplan.dnorpattern = ( select dnorpattern from numplan where pkid = ( select fknumplan from devicenumplanmap where fkdevice = ( select pkid from device where name = '"
				+ value2 + "' )))and typednusage.enum = 1</sql>";
		data = data
				+ "</axl:executeSQLUpdate> </SOAP-ENV:Body> </SOAP-ENV:Envelope>";

		String header = Methods.getHttpsHeader(data);
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
			socket = (SSLSocket) sslFact.createSocket(Methods.getKeys("ip"),
					Integer.parseInt("8443"));
			in = socket.getInputStream();
			bArray = new byte[2048];
			sb = new StringBuffer(2048);
			int ch = 0;
			out = socket.getOutputStream();
			out.write(header.getBytes());
			while ((ch = in.read(bArray)) != -1) {
				sb.append(new String(bArray, 0, ch));
			}
			socket.close();
			string = sb.toString();
			String replacePhoneName = string.replace("<name/>",
					"<name>N/A</name>");
			String replaceFirstName = replacePhoneName.replace("<firstname/>",
					"<firstname>N/A</firstname>");
			String replaceLastName = replaceFirstName.replace("<lastname/>",
					"<lastname>N/A</lastname>");
			String replaceUserID = replaceLastName.replace("<userid/>",
					"<userid><NONE></userid>");
			String replaceDesc = replaceUserID.replace("<description/>",
					"<description>N/A</description>");
			String replaceModel = replaceDesc.replace("<tkmodel/>",
					"<tkmodel>N/A</tkmodel>");
			String replaceDevicepool = replaceModel.replace("<fkdevicepool/>",
					"<fkdevicepool>N/A</fkdevicepool>");
			String finalString = replaceDevicepool;
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
		String string = null;
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		byte[] bArray = null;
		data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" ";
		data = data
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"> ";
		data = data
				+ "<SOAP-ENV:Body> <axl:executeSQLUpdate xmlns:axl=\"http://www.cisco.com/AXL/7.0\" ";
		data = data
				+ " xsi:schemaLocation=\"http://www.cisco.com/AXL/1.0 http://gkar.cisco.com/schema/axlsoap.xsd\" ";
		data = data + "sequence=\"1234\">";
		data = data
				+ "<sql>insert into devicenumplanmap (fkdevice,fknumplan,numplanindex) select device.pkid,endusernumplanmap.fknumplan,typednusage.enum from device, endusernumplanmap,typednusage where device.fkenduser = (select pkid from enduser where userid = '"
				+ value1
				+ "') and endusernumplanmap.fknumplan = ( select fknumplan from endusernumplanmap where fkenduser = ( select pkid from enduser where userid = '"
				+ value1 + "')) and typednusage.enum = 1</sql>";
		data = data
				+ "</axl:executeSQLUpdate> </SOAP-ENV:Body> </SOAP-ENV:Envelope>";

		String header = Methods.getHttpsHeader(data);
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
			socket = (SSLSocket) sslFact.createSocket(Methods.getKeys("ip"),
					Integer.parseInt("8443"));
			in = socket.getInputStream();
			bArray = new byte[2048];
			sb = new StringBuffer(2048);
			int ch = 0;
			out = socket.getOutputStream();
			out.write(header.getBytes());
			while ((ch = in.read(bArray)) != -1) {
				sb.append(new String(bArray, 0, ch));
			}
			socket.close();
			string = sb.toString();
			String replacePhoneName = string.replace("<name/>",
					"<name>N/A</name>");
			String replaceFirstName = replacePhoneName.replace("<firstname/>",
					"<firstname>N/A</firstname>");
			String replaceLastName = replaceFirstName.replace("<lastname/>",
					"<lastname>N/A</lastname>");
			String replaceUserID = replaceLastName.replace("<userid/>",
					"<userid><NONE></userid>");
			String replaceDesc = replaceUserID.replace("<description/>",
					"<description>N/A</description>");
			String replaceModel = replaceDesc.replace("<tkmodel/>",
					"<tkmodel>N/A</tkmodel>");
			String replaceDevicepool = replaceModel.replace("<fkdevicepool/>",
					"<fkdevicepool>N/A</fkdevicepool>");
			String finalString = replaceDevicepool;
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
		String string = null;
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		byte[] bArray = null;
		data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" ";
		data = data
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"> ";
		data = data
				+ "<SOAP-ENV:Body> <axl:executeSQLUpdate xmlns:axl=\"http://www.cisco.com/AXL/7.0\" ";
		data = data
				+ " xsi:schemaLocation=\"http://www.cisco.com/AXL/1.0 http://gkar.cisco.com/schema/axlsoap.xsd\" ";
		data = data + "sequence=\"1234\">";
		data = data
				+ "<sql>insert into devicenumplanmapendusermap (fkdevicenumplanmap,fkenduser) select devicenumplanmap.pkid, enduser.pkid from devicenumplanmap, enduser where devicenumplanmap.pkid = ( select pkid from devicenumplanmap where fkdevice = ( select pkid from device where fkenduser = ( select pkid from enduser where userid = '"
				+ value1
				+ "' ))) and enduser.pkid = ( select pkid from enduser where userid = '"
				+ value1 + "' )</sql>";
		data = data
				+ "</axl:executeSQLUpdate> </SOAP-ENV:Body> </SOAP-ENV:Envelope>";

		String header = Methods.getHttpsHeader(data);
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
			socket = (SSLSocket) sslFact.createSocket(Methods.getKeys("ip"),
					Integer.parseInt("8443"));
			in = socket.getInputStream();
			bArray = new byte[2048];
			sb = new StringBuffer(2048);
			int ch = 0;
			out = socket.getOutputStream();
			out.write(header.getBytes());
			while ((ch = in.read(bArray)) != -1) {
				sb.append(new String(bArray, 0, ch));
			}
			socket.close();
			string = sb.toString();
			String replacePhoneName = string.replace("<name/>",
					"<name>N/A</name>");
			String replaceFirstName = replacePhoneName.replace("<firstname/>",
					"<firstname>N/A</firstname>");
			String replaceLastName = replaceFirstName.replace("<lastname/>",
					"<lastname>N/A</lastname>");
			String replaceUserID = replaceLastName.replace("<userid/>",
					"<userid><NONE></userid>");
			String replaceDesc = replaceUserID.replace("<description/>",
					"<description>N/A</description>");
			String replaceModel = replaceDesc.replace("<tkmodel/>",
					"<tkmodel>N/A</tkmodel>");
			String replaceDevicepool = replaceModel.replace("<fkdevicepool/>",
					"<fkdevicepool>N/A</fkdevicepool>");
			String finalString = replaceDevicepool;
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
		String string = null;
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		byte[] bArray = null;
		String condition1String = null;
		String condition2String = null;
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

		data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" ";
		data = data
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"> ";
		data = data
				+ "<SOAP-ENV:Body> <axl:executeSQLQuery xmlns:axl=\"http://www.cisco.com/AXL/7.0\" ";
		data = data
				+ " xsi:schemaLocation=\"http://www.cisco.com/AXL/1.0 http://gkar.cisco.com/schema/axlsoap.xsd\" ";
		data = data + "sequence=\"1234\">";
		data = data
				+ "<sql>SELECT device.name as DeviceName, enduser.firstname as FirstName, enduser.lastname as LastName, enduser.userid as UserID, device.description as Desc, typemodel.name as ModelName, devicepool.name as DevicePool FROM device LEFT JOIN enduser ON device.fkenduser = enduser.pkid LEFT JOIN typemodel ON device.tkmodel = typemodel.enum LEFT JOIN devicepool ON device.fkdevicepool = devicepool.pkid WHERE "
				+ condition1String + " " + condition2String + "</sql>";
		data = data
				+ "</axl:executeSQLQuery> </SOAP-ENV:Body> </SOAP-ENV:Envelope>";
		String header = Methods.getHttpsHeader(data);
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
			socket = (SSLSocket) sslFact.createSocket(Methods.getKeys("ip"),
					Integer.parseInt("8443"));
			in = socket.getInputStream();
			bArray = new byte[2048];
			sb = new StringBuffer(2048);
			int ch = 0;
			out = socket.getOutputStream();
			out.write(header.getBytes());
			while ((ch = in.read(bArray)) != -1) {
				sb.append(new String(bArray, 0, ch));
			}
			socket.close();
			string = sb.toString();
			String replacePhoneName = string.replace("<devicename/>",
					"<devicename>N/A</devicename>");
			String replaceFirstName = replacePhoneName.replace("<firstname/>",
					"<firstname>N/A</firstname>");
			String replaceLastName = replaceFirstName.replace("<lastname/>",
					"<lastname>N/A</lastname>");
			String replaceUserID = replaceLastName.replace("<userid/>",
					"<userid>N/A</userid>");
			String replaceDesc = replaceUserID.replace("<desc/>",
					"<desc>N/A</desc>");
			String replaceModel = replaceDesc.replace("<modelname/>",
					"<modelname>N/A</modelname>");
			String replaceDevicepool = replaceModel.replace("<devicepool/>",
					"<devicepool>N/A</devicepool>");
			String finalString = replaceDevicepool;
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
				if (!Variables.phoneNames[i].equalsIgnoreCase("N/A")) {
					Variables.deviceTableRows[i][1] = Variables.phoneNames[i];
				}
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
			JScrollBar scrollbar = CMUtilsGUI.logScrollPane
					.getVerticalScrollBar();
			scrollbar.setAutoscrolls(true);
			scrollbar.setValue(scrollbar.getMaximum());
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

		String string = null;
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		byte[] bArray = null;
		data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" ";
		data = data
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"> ";
		data = data
				+ "<SOAP-ENV:Body> <axl:executeSQLQuery xmlns:axl=\"http://www.cisco.com/AXL/7.0\" ";
		data = data
				+ " xsi:schemaLocation=\"http://www.cisco.com/AXL/1.0 http://gkar.cisco.com/schema/axlsoap.xsd\" ";
		data = data + "sequence=\"1234\">";
		data = data
				+ "<sql>SELECT dnorpattern AS EXT from numplan where not exists ( select endusernumplanmap.pkid from endusernumplanmap where endusernumplanmap.fknumplan = numplan.pkid )</sql>";
		data = data
				+ "</axl:executeSQLQuery> </SOAP-ENV:Body> </SOAP-ENV:Envelope>";

		String header = Methods.getHttpsHeader(data);
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
			socket = (SSLSocket) sslFact.createSocket(Methods.getKeys("ip"),
					Integer.parseInt("8443"));
			in = socket.getInputStream();
			bArray = new byte[2048];
			sb = new StringBuffer(2048);
			int ch = 0;
			out = socket.getOutputStream();
			out.write(header.getBytes());
			while ((ch = in.read(bArray)) != -1) {
				sb.append(new String(bArray, 0, ch));
			}
			socket.close();
			string = sb.toString();
			String replaceDnorPattern = string.replace("<ext/>",
					"<ext>N/A</ext>");
			String finalString = replaceDnorPattern;
			Variables.dnorPatterns = Methods.substringsBetween(finalString,
					"<ext>", "</ext>");
			Variables.lineAssocTableRows = new String[Variables.dnorPatterns.length][2];
			for (int i = 0; i < Variables.dnorPatterns.length; i++) {
				Variables.lineAssocTableRows[i][0] = Integer.toString(i + 1);
				Variables.lineAssocTableRows[i][1] = Variables.dnorPatterns[i];
			}
			CMUtilsGUI.logArea.append("Retrieved "
					+ Variables.dnorPatterns.length + " records! \n");
			JScrollBar scrollbar = CMUtilsGUI.logScrollPane
					.getVerticalScrollBar();
			scrollbar.setAutoscrolls(true);
			scrollbar.setValue(scrollbar.getMaximum());
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

	public static boolean getLineAssocEndusers() {
		String data = null;
		String string = null;
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		byte[] bArray = null;
		data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" ";
		data = data
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"> ";
		data = data
				+ "<SOAP-ENV:Body> <axl:executeSQLQuery xmlns:axl=\"http://www.cisco.com/AXL/7.0\" ";
		data = data
				+ " xsi:schemaLocation=\"http://www.cisco.com/AXL/1.0 http://gkar.cisco.com/schema/axlsoap.xsd\" ";
		data = data + "sequence=\"1234\">";
		data = data
				+ "<sql>SELECT enduser.userid AS userid, enduser.firstname AS firstname, enduser.lastname AS lastname FROM enduser WHERE NOT EXISTS ( SELECT * from endusernumplanmap WHERE endusernumplanmap.fkenduser = enduser.pkid )</sql>";
		data = data
				+ "</axl:executeSQLQuery> </SOAP-ENV:Body> </SOAP-ENV:Envelope>";

		String header = Methods.getHttpsHeader(data);
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
			socket = (SSLSocket) sslFact.createSocket(Methods.getKeys("ip"),
					Integer.parseInt("8443"));
			in = socket.getInputStream();
			bArray = new byte[2048];
			sb = new StringBuffer(2048);
			int ch = 0;
			out = socket.getOutputStream();
			out.write(header.getBytes());
			while ((ch = in.read(bArray)) != -1) {
				sb.append(new String(bArray, 0, ch));
			}
			socket.close();
			string = sb.toString();
			String replaceUserid = string.replace("<userid/>",
					"<userid>N/A</userid>");
			String replaceFirstname = replaceUserid.replace("<firstname/>",
					"<firstname>N/A</firstname>");
			String replaceLastname = replaceFirstname.replace("<lastname/>",
					"<lastname>N/A</lastname>");
			String finalString = replaceLastname;
			Variables.lineAssocUserids = Methods.substringsBetween(finalString,
					"<userid>", "</userid>");
			Variables.lineAssocFirstnames = Methods.substringsBetween(
					finalString, "<firstname>", "</firstname>");
			Variables.lineAssocLastnames = Methods.substringsBetween(
					finalString, "<lastname>", "</lastname>");
			Variables.lineAssocTableRows = new String[Variables.lineAssocUserids.length][5];
			for (int i = 0; i < Variables.lineAssocUserids.length; i++) {
				Variables.lineAssocTableRows[i][0] = Integer.toString(i + 1);
				Variables.lineAssocTableRows[i][2] = Variables.lineAssocUserids[i];
				Variables.lineAssocTableRows[i][3] = Variables.lineAssocFirstnames[i];
				Variables.lineAssocTableRows[i][4] = Variables.lineAssocLastnames[i];
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

	public static boolean getEnduserAssocDevices() {
		String data = null;
		String string = null;
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		byte[] bArray = null;
		data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" ";
		data = data
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"> ";
		data = data
				+ "<SOAP-ENV:Body> <axl:executeSQLQuery xmlns:axl=\"http://www.cisco.com/AXL/7.0\" ";
		data = data
				+ " xsi:schemaLocation=\"http://www.cisco.com/AXL/1.0 http://gkar.cisco.com/schema/axlsoap.xsd\" ";
		data = data + "sequence=\"1234\">";
		data = data
				+ "<sql>SELECT device.name AS DeviceName from device WHERE NOT EXISTS ( SELECT * from enduserdevicemap where enduserdevicemap.fkdevice = device.pkid AND device.fkenduser IS NOT NULL )</sql>";
		data = data
				+ "</axl:executeSQLQuery> </SOAP-ENV:Body> </SOAP-ENV:Envelope>";

		String header = Methods.getHttpsHeader(data);
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
			socket = (SSLSocket) sslFact.createSocket(Methods.getKeys("ip"),
					Integer.parseInt("8443"));
			in = socket.getInputStream();
			bArray = new byte[2048];
			sb = new StringBuffer(2048);
			int ch = 0;
			out = socket.getOutputStream();
			out.write(header.getBytes());
			while ((ch = in.read(bArray)) != -1) {
				sb.append(new String(bArray, 0, ch));
			}
			socket.close();
			string = sb.toString();
			String replaceDevicename = string.replace("<devicename/>",
					"<devicename>N/A</devicename>");
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

	public static boolean getEnduserAssoc() {
		String data = null;
		String string = null;
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		byte[] bArray = null;
		data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" ";
		data = data
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"> ";
		data = data
				+ "<SOAP-ENV:Body> <axl:executeSQLQuery xmlns:axl=\"http://www.cisco.com/AXL/7.0\" ";
		data = data
				+ " xsi:schemaLocation=\"http://www.cisco.com/AXL/1.0 http://gkar.cisco.com/schema/axlsoap.xsd\" ";
		data = data + "sequence=\"1234\">";
		data = data
				+ "<sql>SELECT enduser.userid AS userid, enduser.firstname AS firstname, enduser.lastname AS lastname from enduser WHERE NOT EXISTS ( SELECT * from enduserdevicemap WHERE enduserdevicemap.fkenduser = enduser.pkid )</sql>";
		data = data
				+ "</axl:executeSQLQuery> </SOAP-ENV:Body> </SOAP-ENV:Envelope>";

		String header = Methods.getHttpsHeader(data);
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
			socket = (SSLSocket) sslFact.createSocket(Methods.getKeys("ip"),
					Integer.parseInt("8443"));
			in = socket.getInputStream();
			bArray = new byte[2048];
			sb = new StringBuffer(2048);
			int ch = 0;
			out = socket.getOutputStream();
			out.write(header.getBytes());
			while ((ch = in.read(bArray)) != -1) {
				sb.append(new String(bArray, 0, ch));
			}
			socket.close();
			string = sb.toString();
			String replaceUserid = string.replace("<userid/>",
					"<userid>N/A</userid>");
			String replaceFirstname = replaceUserid.replace("<firstname/>",
					"<firstname>N/A</firstname>");
			String replaceLastname = replaceFirstname.replace("<lastname/>",
					"<lastname>N/A</lastname>");
			String finalString = replaceLastname;
			Variables.devAssocUserids = Methods.substringsBetween(finalString,
					"<userid>", "</userid>");
			Variables.devAssocFirstnames = Methods.substringsBetween(
					finalString, "<firstname>", "</firstname>");
			Variables.devAssocLastnames = Methods.substringsBetween(
					finalString, "<lastname>", "</lastname>");
			Variables.enduserAssocTableRows = new String[Variables.devAssocUserids.length][5];
			for (int i = 0; i < Variables.devAssocUserids.length; i++) {
				Variables.enduserAssocTableRows[i][0] = Integer.toString(i + 1);
				Variables.enduserAssocTableRows[i][2] = Variables.devAssocUserids[i];
				Variables.enduserAssocTableRows[i][3] = Variables.devAssocFirstnames[i];
				Variables.enduserAssocTableRows[i][4] = Variables.devAssocLastnames[i];
			}
			CMUtilsGUI.logArea.append("Retrieved "
					+ Variables.devAssocUserids.length + " records! \n");
			JScrollBar scrollbar = CMUtilsGUI.logScrollPane
					.getVerticalScrollBar();
			scrollbar.setAutoscrolls(true);
			scrollbar.setValue(scrollbar.getMaximum());
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
		String string = null;
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		byte[] bArray = null;
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
			out = socket.getOutputStream();
			out.write(header.getBytes());
			while ((ch = in.read(bArray)) != -1) {
				sb.append(new String(bArray, 0, ch));
			}
			socket.close();
			string = sb.toString();
			String replaceUserID = string.replace("<userid/>",
					"<userid>N/A</userid>");
			String replaceFirstname = replaceUserID.replace("<firstname/>",
					"<firstname>N/A</firstname>");
			String replaceLastname = replaceFirstname.replace("<lastname/>",
					"<lastname>N/A</lastname>");
			String finalString = replaceLastname;
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
		String string = null;
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		byte[] bArray = null;
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
		String header = Methods.getHttpsHeader(data);
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
			socket = (SSLSocket) sslFact.createSocket(Methods.getKeys("ip"),
					Integer.parseInt("8443"));
			in = socket.getInputStream();
			bArray = new byte[2048];
			sb = new StringBuffer(2048);
			int ch = 0;
			out = socket.getOutputStream();
			out.write(header.getBytes());
			while ((ch = in.read(bArray)) != -1) {
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
		return "<return>"
				+ Methods.substringBetween(string, "<return>", "</return>")
						.replaceAll("></", ">N/A</")
						.replaceAll("N/A</row>", "</row>") + "</return>";
	}

	public static boolean executeSQLUpdate(String sqlUpdateString) {
		String data = null;
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		byte[] bArray = null;
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
			out = socket.getOutputStream();
			out.write(header.getBytes());
			while ((ch = in.read(bArray)) != -1) {
				sb.append(new String(bArray, 0, ch));
			}
			socket.close();
			sb.toString();
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
