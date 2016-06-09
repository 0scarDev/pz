//
//  ManualViewController.swift
//  Ratings
//
//  Created by Guan Wong on 5/30/16.
//  Copyright © 2016 Ray Wenderlich. All rights reserved.
//

import UIKit

class ManualViewController: UIViewController {

    var amountSlide:Int = 0
    
    @IBAction func CounterClock(sender: UIButton) {
        
        let str:String = "COUT \(String(amountSlide))"
        
        sendToArduino(str)
    }
    
    @IBAction func ClockWise(sender: UIButton) {
        let str:String = "CLOC \(String(amountSlide))"
        
        sendToArduino(str)
    }
 
    @IBAction func SetMaxCounter(sender: UIButton) {
        let str:String = "SMIN"
        
        sendToArduino(str)
    }
    
    @IBAction func SetMaxClockWise(sender: UIButton) {
        let str:String = "SMAX"
        
        sendToArduino(str)
    }
    
    @IBAction func DisconnectAction(sender: UIButton) {
        if (client.isConnected()){
        client.close()
        }
        
        if (serial.isConnected()){
            serial.disconnect()
        }
        self.performSegueWithIdentifier("disconnectBack", sender: nil)
    }
    
    @IBAction func sliderValueHasChanged(sender: UISlider) {
        print(sender.value)
        amountSlide = Int(sender.value)
    }
    
    func sendToArduino(str:String){
        // bluetooth
        if(serial.isConnected()){
            if let svc = serial.bleService{
                svc.sendMessageToDevice(str)
            }
        }
            // WiFi
        else if(client.isConnected()){
            client.send(str: str)
        }
        else{
            print("MSG NOT SENT: \(str)")
        }
    }
    
    
}
