package eu.smartcampus.api.deviceconnectivity.impls.modbus.master;

import eu.smartcampus.api.deviceconnectivity.Logger;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.base.ModbusUtils;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.msg.ModbusResponse;
import com.serotonin.modbus4j.msg.ReadCoilsRequest;
import com.serotonin.modbus4j.msg.ReadCoilsResponse;
import com.serotonin.modbus4j.msg.ReadDiscreteInputsRequest;
import com.serotonin.modbus4j.msg.ReadDiscreteInputsResponse;
import com.serotonin.modbus4j.msg.ReadExceptionStatusRequest;
import com.serotonin.modbus4j.msg.ReadExceptionStatusResponse;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersRequest;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersResponse;
import com.serotonin.modbus4j.msg.ReadInputRegistersRequest;
import com.serotonin.modbus4j.msg.ReadInputRegistersResponse;
import com.serotonin.modbus4j.msg.ReportSlaveIdRequest;
import com.serotonin.modbus4j.msg.ReportSlaveIdResponse;
import com.serotonin.modbus4j.msg.WriteCoilRequest;
import com.serotonin.modbus4j.msg.WriteCoilsRequest;
import com.serotonin.modbus4j.msg.WriteRegisterRequest;
import com.serotonin.modbus4j.msg.WriteRegisterResponse;
import com.serotonin.modbus4j.msg.WriteRegistersRequest;
import com.serotonin.modbus4j.msg.WriteRegistersResponse;


/**
 * The Class ModbusMasterEmulator.
 */
public class ModbusMasterLib {
	
	static private Logger log = Logger.getLogger(ModbusMasterLib.class.getName());  

    /** The timeout. */
    private final int timeout = 2000;

    /** The n retry. */
    private final int nRetry = 0;

    /** The master. */
    private ModbusMaster master;

    /**
     * Gets the ip parameters.
     * 
     * @return the ip parameters
     */
    private IpParameters getIpParameters() {
        IpParameters ipParameters = new IpParameters();
        ipParameters.setHost("localhost");
        ipParameters.setPort(ModbusUtils.TCP_PORT);
        ipParameters.setEncapsulated(false);
        return ipParameters;
    }

    /**
     * Creates the modbus master.
     */
    public void createModbusTcpMaster() {
        try {
            ModbusFactory modbusFactory = new ModbusFactory();
            IpParameters params = getIpParameters();
            master = modbusFactory.createTcpMaster(params, false);
            master.setTimeout(timeout);
            master.setRetries(nRetry);
            master.init();
        } catch (ModbusInitException e) {
            log.i(e.getMessage());
        }
    }

    /**
     * Destroy modbus master.
     */
    public void destroyModbusMaster() {
        master.destroy();
    }

    /**
     * Read coils.
     * 
     * @param slaveId the slave id
     * @param offset register address
     * @param len n registers read
     * @return array of boolean type results
     */
    public boolean[] readCoils(int slaveId, int offset, int len) {
        ReadCoilsResponse response = null;
        try {
            ReadCoilsRequest request = new ReadCoilsRequest(slaveId, offset, len);
            response = (ReadCoilsResponse) master.send(request);
            checkResponse(response);
        } catch (ModbusTransportException e) {
            log.e(e.getMessage());
        }
        return response.getBooleanData();
    }

    /**
     * Read discrete input.
     * 
     * @param slaveId the slave id
     * @param offset register address
     * @param len n registers read
     * @return array of boolean type results
     */
    public boolean[] readDiscreteInput(int slaveId, int offset, int len) {
        ReadDiscreteInputsResponse response = null;
        try {
            ReadDiscreteInputsRequest request = new ReadDiscreteInputsRequest(slaveId, offset, len);
            response = (ReadDiscreteInputsResponse) master.send(request);

            checkResponse(response);
        } catch (ModbusTransportException e) {
            log.e(e.getMessage());
        }
        return response.getBooleanData();
    }

    /**
     * Read holding registers.
     * 
     * @param slaveId the slave id
     * @param offset register address
     * @param len n registers read
     * @return array of short type results
     */
    public short[] readHoldingRegisters(int slaveId, int offset, int len) {
        ReadHoldingRegistersResponse response = null;
        try {
            ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest(slaveId, offset,
                    len);
            response = (ReadHoldingRegistersResponse) master.send(request);

            checkResponse(response);
        } catch (ModbusTransportException e) {
            log.e(e.getMessage());
        }
        return response.getShortData();
    }

    /**
     * Read input registers.
     * 
     * @param slaveId the slave id
     * @param offset register address
     * @param len n registers read
     * @return array of short type results
     */
    public short[] readInputRegisters(int slaveId, int offset, int len) {
        ReadInputRegistersResponse response = null;
        try {
            ReadInputRegistersRequest request = new ReadInputRegistersRequest(slaveId, offset, len);
            response = (ReadInputRegistersResponse) master.send(request);

            checkResponse(response);
        } catch (ModbusTransportException e) {
            log.e(e.getMessage());
        }
        return response.getShortData();
    }

    /**
     * Read exception status.
     * 
     * @param slaveId the slave id
     * @return the byte type result
     */
    private byte readExceptionStatus(int slaveId) {
        ReadExceptionStatusResponse response = null;
        try {
            ReadExceptionStatusRequest request = new ReadExceptionStatusRequest(slaveId);
            response = (ReadExceptionStatusResponse) master.send(request);
            checkResponse(response);
        } catch (ModbusTransportException e) {
            log.e(e.getMessage());
        }
        return response.getExceptionStatus();
    }

    /**
     * Report slave id.
     * 
     * @param slaveId the slave id
     * @return the array of byte type results
     */
    private byte[] reportSlaveId(int slaveId) {
        ReportSlaveIdResponse response = null;
        try {
            ReportSlaveIdRequest request = new ReportSlaveIdRequest(slaveId);
            response = (ReportSlaveIdResponse) master.send(request);
            checkResponse(response);
        } catch (ModbusTransportException e) {
            log.e(e.getMessage());
        }
        return response.getData();
    }

    /**
     * Write coil.
     * 
     * @param slaveId the slave id
     * @param offset register address
     * @param value the boolean type result
     */
    public void writeCoil(int slaveId, int offset, boolean value) {

        try {
            WriteCoilRequest request = new WriteCoilRequest(slaveId, offset, value);
            checkResponse(master.send(request));
        } catch (ModbusTransportException e) {
            log.e(e.getMessage());
        }
    }

    /**
     * Write coils.
     * 
     * @param slaveId the slave id
     * @param offset register address
     * @param values the array of boolean type results
     */
    public void writeCoils(int slaveId, int offset, boolean[] values) {
        try {
            WriteCoilsRequest request = new WriteCoilsRequest(slaveId, offset, values);
            checkResponse(master.send(request));
        } catch (ModbusTransportException e) {
            log.e(e.getMessage());
        }
    }

    /**
     * Write register.
     * 
     * @param slaveId the slave id
     * @param offset register address
     * @param value boolean type result
     */
    public void writeRegister(int slaveId, int offset, int value) {
        try {
            WriteRegisterRequest request = new WriteRegisterRequest(slaveId, offset, value);
            WriteRegisterResponse response = (WriteRegisterResponse) master.send(request);
            checkResponse(response);
        } catch (ModbusTransportException e) {
            log.e(e.getMessage());
        }
    }

    /**
     * Write registers.
     * 
     * @param slaveId the slave id
     * @param offset the start
     * @param values the array of short type results
     */
    public void writeRegisters(int slaveId, int offset, short[] values) {
        try {
            WriteRegistersRequest request = new WriteRegistersRequest(slaveId, offset, values);
            WriteRegistersResponse response = (WriteRegistersResponse) master.send(request);
            checkResponse(response);
        } catch (ModbusTransportException e) {
            log.e(e.getMessage());
        }
    }

    /**
     * Check response.
     * 
     * @param response the response
     */
    private void checkResponse(ModbusResponse response) {
        if (response.isException())
            log.e("Exception response: message=" + response.getExceptionMessage());
        else
            log.e("Success");
    }
}
