package ist.smartoffice.deviceconnectivity.impls.modbus.master;



/**
 * The Class ModbusMasterEmu.
 */
public final class ModbusMasterEmu {


    /**
     * Instantiates a new modbus master emu.
     */
    private ModbusMasterEmu() {
    }

    /**
     * The main method.
     * 
     * @param args the arguments
     */
    public static void main(String[] args) {

        final int slaveId = 2;
        final int registerId = 10;
        final int nRegisters = 6;

        ModbusMasterLib emu = new ModbusMasterLib();
        emu.createModbusTcpMaster();
        for (int i = 0; i < nRegisters + 1; i++) {
            emu.writeCoil(slaveId, registerId + i, true);
        }
        emu.readCoils(slaveId, registerId, nRegisters);
        emu.destroyModbusMaster();

    }

}
