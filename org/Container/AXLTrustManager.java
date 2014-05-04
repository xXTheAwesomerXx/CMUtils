package org.Container;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class AXLTrustManager {

	/*public static String getKeys(String which) {
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
			return ConnectGUI.ip;
		case 2:
			return ConnectGUI.user;
		case 3:
			return ConnectGUI.pass;
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

	public static void main(String[] args) {
		String data = null;
		byte[] bArray = (byte[]) null;
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		// String sAXLSOAPRequest = "";
		// String sAXLRequest =
		// "<?xml version=\"1.0\" encoding=\"utf-8\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><soapenv:Body><ns1:SelectCmDevice soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:ns1=\"http://schemas.cisco.com/ast/soap/\"><StateInfo/><CmSelectionCriteria soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xsi:type=\"ns2:CmSelectionCriteria\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:ns2=\"http://schemas.cisco.com/ast/soap/\"><MaxReturnedDevices>200</MaxReturnedDevices><Class>Phone</Class><Model>255</Model><Status>Any</Status><SelectBy>Name</SelectBy><SelectItems soapenc:arrayType=\"ns2:SelectItem[2]\" xsi:type=\"soapenc:Array\"><item soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xsi:type=\"ns3:SelectItem\" xmlns:ns3=\"http://schemas.cisco.com/ast/soap/\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\"><Item>SEP1234</Item></item><item soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xsi:type=\"ns3:SelectItem\" xmlns:ns3=\"http://schemas.cisco.com/ast/soap/\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\"><Item>SEP1235</Item></item></SelectItems></CmSelectionCriteria></ns1:SelectCmDevice></soapenv:Body></soapenv:Envelope>\"";
		//
		// String authorization = "CCMAdministrator:Iptcis002";
		// authorization = Base64.encode(authorization.getBytes().toString());
		//
		// sAXLSOAPRequest =
		// "POST  realtimeservice/services/RisPort?wsdl HTTP/1.1";
		// sAXLSOAPRequest = sAXLSOAPRequest + "Host:localhost:8443";
		// sAXLSOAPRequest = sAXLSOAPRequest + "Authorization: Basic "
		// + authorization;
		//
		// sAXLSOAPRequest = sAXLSOAPRequest + "Charset: utf-8";
		// sAXLSOAPRequest = sAXLSOAPRequest
		// +
		// "Accept: application/soap+xml, application/dime, multipart/related, text/*";
		// sAXLSOAPRequest = sAXLSOAPRequest + "user-agent: ClientName";
		// sAXLSOAPRequest = sAXLSOAPRequest + "Host: nozomi";
		// sAXLSOAPRequest = sAXLSOAPRequest
		// + "Content-Type: text/xml; charset=utf-8";
		// sAXLSOAPRequest = sAXLSOAPRequest + "Content-Length: ";
		// sAXLSOAPRequest = sAXLSOAPRequest + sAXLRequest.length();
		// sAXLSOAPRequest = sAXLSOAPRequest
		// +
		// "SOAPAction: \"http://schemas.cisco.com/ast/soap/action/#PerfmonPort#PerfmonOpenSession\"";
		//
		// sAXLSOAPRequest = sAXLSOAPRequest + sAXLRequest;
		data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" ";
		data = data
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"> ";
		data = data
				+ "<SOAP-ENV:Body> <axl:executeSQLQuery xmlns:axl=\"http://www.cisco.com/AXL/7.0\" ";
		data = data
				+ " xsi:schemaLocation=\"http://www.cisco.com/AXL/1.0 http://gkar.cisco.com/schema/axlsoap.xsd\" ";
		data = data + "sequence=\"1234\">";
		data = data
				+ "<sql>select * from device</sql>";
		data = data
				+ "</axl:executeSQLQuery> </SOAP-ENV:Body> </SOAP-ENV:Envelope>";

		String header = getHttpsHeader(data);
		header = header + data;
		try {
			AXLJavaClient axl = new AXLJavaClient();
			// Implement the certificate-related stuffs required for sending
			// request via https
			X509TrustManager xtm = axl.new MyTrustManager();
			TrustManager[] mytm = { xtm };
			SSLContext ctx = SSLContext.getInstance("SSL");
			ctx.init(null, mytm, null);
			SSLSocketFactory sslFact = (SSLSocketFactory) ctx
					.getSocketFactory();
			socket = (SSLSocket) sslFact.createSocket(getKeys("ip"),
					Integer.parseInt("8443"));
			in = socket.getInputStream();
			StringBuffer sb = new StringBuffer(2048);
			bArray = new byte[2048];
			int ch = 0;
			int sum = 0;
			out = socket.getOutputStream();
			out.write(header.getBytes());
			while ((ch = in.read(bArray)) != -1) {
				sum += ch;
				sb.append(new String(bArray, 0, ch));
			}
			socket.close();

			System.out.println(sb.toString());
		} catch (UnknownHostException e) {
			System.err.println("Error connecting to host: " + e.getMessage());
			return;
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
			return;
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
	}*/

	public class MyTrustManager implements X509TrustManager {
		public MyTrustManager() {
		}

		public void checkClientTrusted(X509Certificate[] chain, String authType) {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}
}
