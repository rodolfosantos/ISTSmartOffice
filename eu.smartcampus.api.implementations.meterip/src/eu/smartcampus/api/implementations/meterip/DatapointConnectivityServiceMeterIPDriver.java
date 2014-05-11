package eu.smartcampus.api.implementations.meterip;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import eu.smartcampus.api.DatapointAddress;
import eu.smartcampus.api.DatapointMetadata;
import eu.smartcampus.api.DatapointReading;
import eu.smartcampus.api.DatapointValue;
import eu.smartcampus.api.IDatapointConnectivityService;

/**
 * The Class DatapointConnectivityServiceMeterIPDriver
 */
public class DatapointConnectivityServiceMeterIPDriver
        implements IDatapointConnectivityService {

    private String username;
    private String password;
    private Map<DatapointAddress, DatapointMetadata> datapoints;

    /**
     * Instantiates a new datapoint connectivity service meter ip driver.
     *
     * @param datapoints the datapoints
     * @param username the username
     * @param password the password
     */
    public DatapointConnectivityServiceMeterIPDriver(Map<DatapointAddress, DatapointMetadata> datapoints,
                                                     String username,
                                                     String password) {
        this.username = username;
        this.password = password;
        this.datapoints = datapoints;
    }

    /**
     * Gets the new measure.
     *
     * @param address the address
     * @return the new measure
     * @throws MalformedURLException the malformed url exception
     */
    private MeterMeasure getNewMeasure(String address) throws MalformedURLException {
        return new MeterMeasure(
                fetchSensorReading_HTTPS(new URL("https://" + address + "/reading")));
    }

    /**
     * Fetch sensor reading_ https.
     *
     * @param address the address
     * @return the string
     */
    private String fetchSensorReading_HTTPS(URL address) {

        String res = "";
        // Sensor server uses HTTPS and use a self-signed certificate, then we
        // must
        // create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
                return;
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
                return;
            }
        } };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
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
            connectionURL.setRequestProperty("Authorization", userNamePasswordBase64);
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

    /**
     * Base64 encode.
     *
     * @param string the string
     * @return the string
     */
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

    private final static char base64Array[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a',
            'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
            's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', '+', '/' };

    @Override
    public void addDatapointListener(DatapointListener listener) {
        return;//TODO never call this        
    }

    @Override
    public DatapointAddress[] getAllDatapoints() {
        return null;//TODO never call this 
    }

    @Override
    public DatapointMetadata getDatapointMetadata(DatapointAddress address) {
        return this.datapoints.get(address);
    }

    @Override
    public void removeDatapointListener(DatapointListener listener) {
        return;//TODO never call this 

    }

    @Override
    public int requestDatapointRead(DatapointAddress address, ReadCallback readCallback) {
        try {
            MeterMeasure value = getNewMeasure(address.getAddress());
            DatapointReading reading = new DatapointReading(new DatapointValue(
                    value.getTotalPower() + ""));
            readCallback.onReadCompleted(address, new DatapointReading[] { reading }, 0);
            return 0;
        } catch (MalformedURLException e) {
            readCallback.onReadAborted(address, ErrorType.DATAPOINT_NOT_FOUND, 0);
        }

        return 0;
    }

    @Override
    public int requestDatapointWindowRead(DatapointAddress address,
                                          long startTimestamp,
                                          long finishTimestamp,
                                          ReadCallback readCallback) {
        readCallback.onReadAborted(address, ErrorType.UNSUPORTED_DATAPOINT_OPERATION, 0);//not yet (missing history data storage)
        return 0;
    }

    @Override
    public int requestDatapointWrite(DatapointAddress address,
                                     DatapointValue[] values,
                                     WriteCallback writeCallback) {
        writeCallback.onWriteAborted(address, ErrorType.UNSUPORTED_DATAPOINT_OPERATION, 0);
        return 0;
    }
    
    /**
     * @author Diogo Anjos
     */
    public class MeterMeasure {

        private String id;
        private String name;
        private long ts;
        private Phase phase1;
        private Phase phase2;
        private Phase phase3;

        public MeterMeasure(String measureJSON) {
            JSONParser parser = new JSONParser();
            try {
                JSONObject measureJSONobject = (JSONObject) parser.parse(measureJSON);
                id = (String) measureJSONobject.get("id");
                name = (String) measureJSONobject.get("name");
                ts = (Long) measureJSONobject.get("timestamp");
                phase1 = parsePhaseJSONobject(measureJSONobject, "1");
                phase2 = parsePhaseJSONobject(measureJSONobject, "2");
                phase3 = parsePhaseJSONobject(measureJSONobject, "3");
            } catch (ParseException e) {
                System.err
                        .println("[Diogo] Constructor JavaMeasure.Java: probably malformed JSON file...");
                e.printStackTrace();
            }
        }

        public String getID() {
            return id;
        }

        public String getName() {
            return name;
        }

        public long geTimestamp() {
            return ts;
        }

        public long getVoltagePhase1() {
            return phase1.getVoltage();
        }

        public double getCurrentPhase1() {
            return phase1.getCurrent();
        }

        public double getPowerFactorPhase1() {
            return phase1.getPowerFactor();
        }

        public long getVoltagePhase2() {
            return phase2.getVoltage();
        }

        public double getCurrentPhase2() {
            return phase2.getCurrent();
        }

        public double getPowerFactorPhase2() {
            return phase2.getPowerFactor();
        }

        public long getVoltagePhase3() {
            return phase3.getVoltage();
        }

        public double getCurrentPhase3() {
            return phase3.getCurrent();
        }

        public double getPowerFactorPhase3() {
            return phase3.getPowerFactor();
        }

        public double getTotalPower() {
            double totalPower1 = getCurrentPhase1() * getVoltagePhase1() * getPowerFactorPhase1();
            double totalPower2 = getCurrentPhase2() * getVoltagePhase2() * getPowerFactorPhase2();
            double totalPower3 = getCurrentPhase3() * getVoltagePhase3() * getPowerFactorPhase3();
            return totalPower1 + totalPower2 + totalPower3;
        }


        @Override
        public String toString() {
            return "[Measure Dump] \n| id: " + id + "\n| name: " + name + "\n| ts: " + ts
                    + "\n| phase_1:" + phase1.toString() + "\n| phase_2:" + phase2.toString()
                    + "\n| phase_3:" + phase3.toString();
        }

        private Phase parsePhaseJSONobject(JSONObject obj, String phaseNumber) {
            JSONObject phases = (JSONObject) obj.get((String) "phases");
            JSONObject specificPhase = (JSONObject) phases.get(phaseNumber);

            Double current;
            @SuppressWarnings("unused")
            Double voltage;

            try {
                current = (Double) specificPhase.get("current");
            } catch (ClassCastException e) {
                current = 0.0;
            }

            if (specificPhase.get("voltage").getClass().equals(Long.class)) {
                voltage = (Double) new Double((Long) specificPhase.get("voltage"));
            }

            if (specificPhase.get("voltage").getClass().equals(Double.class)) {
                voltage = (Double) specificPhase.get("voltage");
            }

            Phase result = new Phase((Long) specificPhase.get("voltage"), current,
                    (Double) specificPhase.get("powerfactor"));
            return result;
        }


        private class Phase {
            private long voltage;
            private double current;
            private double powerFactor;

            public Phase(long v, double c, double pf) {
                voltage = v;
                current = c;
                powerFactor = pf;
            }

            public long getVoltage() {
                return voltage;
            }

            public double getCurrent() {
                return current;
            }

            public double getPowerFactor() {
                return powerFactor;
            }

            @Override
            public String toString() {
                return " voltage: " + voltage + " | current: " + current + " | powerfactor: "
                        + powerFactor;
            }

        }

    }


}
