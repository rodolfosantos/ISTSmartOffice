package eu.smartcampus.api.impl;

/**
 * @author Diogo Anjos
 */

// lib wiki: http://www.mkyong.com/java/json-simple-example-read-and-write-json/
// lib wiki: https://code.google.com/p/json-simple/
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;




public class SensorMeasure {

    private String id;
    private String name;
    private long ts;
    private Phase phase1;
    private Phase phase2;
    private Phase phase3;

    public SensorMeasure(String measureJSON) {
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
    
    public double getTotalPower(){
        double totalPower1 = getCurrentPhase1()*getVoltagePhase1()*getPowerFactorPhase1();        
        double totalPower2 = getCurrentPhase2()*getVoltagePhase2()*getPowerFactorPhase2();
        double totalPower3 = getCurrentPhase3()*getVoltagePhase3()*getPowerFactorPhase3();
        return totalPower1+totalPower2+totalPower3;
    }
    

    @Override
    public String toString() {
        return "[Measure Dump] \n| id: " + id + "\n| name: " + name + "\n| ts: " + ts + "\n| phase_1:"
                + phase1.toString() + "\n| phase_2:" + phase2.toString() + "\n| phase_3:"
                + phase3.toString();
    }

    private Phase parsePhaseJSONobject(JSONObject obj, String phaseNumber) {
        JSONObject phases = (JSONObject) obj.get((String) "phases");
        JSONObject specificPhase = (JSONObject) phases.get(phaseNumber);
        
        Double current;    
        Double voltage;
        
        try{
            current = (Double) specificPhase.get("current");
        }catch(ClassCastException e){
            current = 0.0;
        }
        
        if(specificPhase.get("voltage").getClass().equals(Long.class)){
            voltage = (Double) new Double((Long) specificPhase.get("voltage"));
        }
        
        if(specificPhase.get("voltage").getClass().equals(Double.class)){
            voltage = (Double) specificPhase.get("voltage");
        }
        
        Phase result = new Phase((Long) specificPhase.get("voltage"),
                current, 
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
