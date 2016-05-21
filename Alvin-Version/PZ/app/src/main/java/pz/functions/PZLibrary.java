package pz.functions;

import java.util.UUID;

/**
 * A manageable listing of variables used for PZ.
 */

public class PZLibrary {

    // Device Name, Device Address
    // BLEService, PositionChar
    // MachineIdentifier
    String [][] compatibleListing = new String[][] {
            { "sophie", "20:91:48:4B:71:A4", "FFE0", "FFE1", "D031E051-158B-F550-BC0D-253B59224BD1" },
            { "Test", "Test", "Test", "Test", "Test" }
    };

    public PZLibrary(){

    }

    // Check if device is compatible with PZ
    public boolean pzDeviceCheck(String deviceListing, String deviceAddress){
        // Scan through the listings
        for (int index = 0; index < compatibleListing.length; index++){
            if(compatibleListing[index][0].equals(deviceListing) && compatibleListing[index][1].equals(deviceAddress)){
                return true;
            }
        }
        return false;
    }

    // Check if device is compatible with PZ, also extract info
    public void pzDeviceExtract(String deviceListing, String deviceAddress, UUID connectUUID){
        // Scan through the listings
        for (int index = 0; index < compatibleListing.length; index++){
            if(compatibleListing[index][0].equals(deviceListing) && compatibleListing[index][1].equals(deviceAddress)){
                connectUUID = UUID.fromString(compatibleListing[index][4]);
            }
        }
    }
}
