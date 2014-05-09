package eu.smartcampus.api.rest.deviceapi.impl;

/**
 * @author Diogo Anjos
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MeterDriverIP {

	private String username;
	private String password;

	public MeterDriverIP(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public MeterMeasure getNewMeasure(String address) throws MalformedURLException {
		return new MeterMeasure(fetchSensorReading_HTTPS(new URL("https://"+address+"/reading")));
	}

	private String fetchSensorReading_HTTPS(URL address) {

		String res = "";
		// Sensor server uses HTTPS and use a self-signed certificate, then we
		// must
		// create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs,
					String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs,
					String authType) {
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

		// now we are prepared to make HTTPS requests to a server with s
		// self signed certificates
		try {
			URL siteURL = address;
			URLConnection connectionURL = siteURL.openConnection();

			// Setting the doInput flag to indicate that the application intends
			// to read data from the URL connection.
			connectionURL.setDoInput(true);
			String userNamePasswordBase64 = "Basic "
					+ base64Encode(this.username + ":" + this.password);
			connectionURL.setRequestProperty("Authorization",
					userNamePasswordBase64);
			// connectionURL.connect(); //connection is already open

			BufferedReader in = new BufferedReader(new InputStreamReader(
					connectionURL.getInputStream()));

			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				// System.out.println(inputLine);
				res = res + inputLine + "\n";
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	private static String base64Encode(String string) {
		String encodedString = "";
		byte bytes[] = string.getBytes();
		int i = 0;
		int pad = 0;
		while (i < bytes.length) {
			byte b1 = bytes[i++];
			byte b2;
			byte b3;
			if (i >= bytes.length) {
				b2 = 0;
				b3 = 0;
				pad = 2;
			} else {
				b2 = bytes[i++];
				if (i >= bytes.length) {
					b3 = 0;
					pad = 1;
				} else
					b3 = bytes[i++];
			}
			byte c1 = (byte) (b1 >> 2);
			byte c2 = (byte) (((b1 & 0x3) << 4) | (b2 >> 4));
			byte c3 = (byte) (((b2 & 0xf) << 2) | (b3 >> 6));
			byte c4 = (byte) (b3 & 0x3f);
			encodedString += base64Array[c1];
			encodedString += base64Array[c2];
			switch (pad) {
			case 0:
				encodedString += base64Array[c3];
				encodedString += base64Array[c4];
				break;
			case 1:
				encodedString += base64Array[c3];
				encodedString += "=";
				break;
			case 2:
				encodedString += "==";
				break;
			}
		}
		return encodedString;
	}

	private final static char base64Array[] = { 'A', 'B', 'C', 'D', 'E', 'F',
			'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
			'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
			'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
			't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', '+', '/' };

}
