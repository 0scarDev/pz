
//
//  Bluetooth.swift
//  cse145
//
//  Created by Guan Wong on 4/13/16.
//  Copyright © 2016 Guan Wong. All rights reserved.
//

import UIKit
import CoreBluetooth

// setting global variable
let serial = Bluetooth();

class Bluetooth: NSObject, CBCentralManagerDelegate{
    
    private var centralManager: CBCentralManager?
    private var peripheralBLE: CBPeripheral?
    
    
    
    override init() {
        super.init()
        
        let centralQueue = dispatch_queue_create("com.diegowong", DISPATCH_QUEUE_SERIAL)
        centralManager = CBCentralManager(delegate: self, queue: centralQueue)
    }
    
    func startScanning() {
        if let central = centralManager {
            central.scanForPeripheralsWithServices([BLEServiceUUID], options: nil)
        }
        else{
            print("Something wrong happened. Error: V002 - Check Bluetooth.swift")
        }
    }
    
    var bleService: BTService? {
        didSet {
            if let service = self.bleService {
                service.startDiscoveringServices()
            }
        }
    }
    
    
    // new added
    func disconnect(){
        if let p = peripheralBLE{
            centralManager?.cancelPeripheralConnection(p)
        }
        else{
            print("Something wrong happened. Error: V001 - Check Bluetooth.swift")
        }
    }
    
    func stopScanning(){
        centralManager?.stopScan()
    }
    
    func isConnected() -> Bool{
        if let p = peripheralBLE{
            if (p.state == CBPeripheralState.Disconnected){
                return false
            }
            else{
                if (p.name == DeviceName){
                return true
                }
            }
        }
        
        
        return false
    }
    
    // MARK: - CBCentralManagerDelegate
    
    func centralManager(central: CBCentralManager, didDiscoverPeripheral peripheral: CBPeripheral, advertisementData: [String : AnyObject], RSSI: NSNumber) {
        // Be sure to retain the peripheral or it will fail during connection.
        
        // Validate peripheral information
        if ((peripheral.name == nil) || (peripheral.name == "")) {
            return
        }
        
        // If not already connected to a peripheral, then connect to this one
        if ((self.peripheralBLE == nil) || (self.peripheralBLE?.state == CBPeripheralState.Disconnected) || self.peripheralBLE?.identifier != MachineIdentifier) {
            // Retain the peripheral before trying to connect
            self.peripheralBLE = peripheral
            
            // Reset service
            self.bleService = nil
            
            // Connect to peripheral
            central.connectPeripheral(peripheral, options: nil)
        }
    }
    
    func centralManager(central: CBCentralManager, didConnectPeripheral peripheral: CBPeripheral) {
        
        // Create new service class
        if (peripheral == self.peripheralBLE) {
            self.bleService = BTService(initWithPeripheral: peripheral)
        }
     
        // Stop scanning for new devices
        central.stopScan()
    }
    
    func centralManager(central: CBCentralManager, didDisconnectPeripheral peripheral: CBPeripheral, error: NSError?) {
        
        // See if it was our peripheral that disconnected
        if (peripheral == self.peripheralBLE) {
            self.bleService = nil;
            self.peripheralBLE = nil;
        }
        
        // Start scanning for new devices
       // self.startScanning()
    }
    
    // MARK: - Private
    
    func clearDevices() {
        self.bleService = nil
        self.peripheralBLE = nil
    }
    
    func centralManagerDidUpdateState(central: CBCentralManager) {
        
        switch (central.state) {
        case CBCentralManagerState.PoweredOff:
            self.clearDevices()
            
        case CBCentralManagerState.Unauthorized:
            // Indicate to user that the iOS device does not support BLE.
            break
            
        case CBCentralManagerState.Unknown:
            // Wait for another event
            break
            
        case CBCentralManagerState.PoweredOn:
           // self.startScanning()
            break
        case CBCentralManagerState.Resetting:
            self.clearDevices()
            
        case CBCentralManagerState.Unsupported:
            break
        }
    }
    

    
}
