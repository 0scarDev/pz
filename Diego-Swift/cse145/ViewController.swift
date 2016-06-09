//
//  ViewController.swift
//  cse145
//
//  Created by Guan Wong on 4/13/16.
//  Copyright © 2016 Guan Wong. All rights reserved.
//

import UIKit

class ViewController: UIViewController {

    // semi global variables
    let labelInfo = UILabel(frame: CGRect(x: 0, y: 0, width: 150, height: 50))
      let buttonConnect = UIButton(frame: CGRect(x: 0, y: 0, width: 100, height: 50))
    
    let labelSpeed = UILabel(frame: CGRect(x: 0, y: 0, width: 150, height: 50))
    var speed:Int = 94
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        view.backgroundColor = UIColor.redColor()
        
        
        // Watch Bluetooth connection
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(ViewController.connectionChanged(_:)), name: BLEServiceChangedStatusNotification, object: nil)
        
        // initialize bluetooth
        serial
        
     let butnames = ["AutoLeft", "Stop", "AutoRight"]
     let butnames2 = ["ManuLeft", "ManuRight"]
        for i in 0...2{
            let button = UIButton(frame: CGRect(x: 0, y: 0, width: 90, height: 50))
            button.setTitle(butnames[i], forState: .Normal)
            button.center = CGPoint(x: view.center.x - 150 + CGFloat(i*150), y: view.center.y)
            button.backgroundColor = UIColor.blackColor()
            button.addTarget(self, action: Selector(butnames[i]), forControlEvents: .TouchDown)
            self.view.addSubview(button)
        }
        
        for i in 0...1{
            let button = UIButton(frame: CGRect(x: 0, y: 0, width: 90, height: 50))
            button.setTitle(butnames2[i], forState: .Normal)
            button.center = CGPoint(x: view.center.x - 100 + CGFloat(i*150), y: view.center.y + 100)
            button.backgroundColor = UIColor.blackColor()
            button.addTarget(self, action: Selector("\(butnames2[i]):"), forControlEvents: .TouchDown)
            button.addTarget(self, action: #selector(ViewController.Stop), forControlEvents: .TouchUpInside)
            button.addTarget(self, action: #selector(ViewController.Stop), forControlEvents: .TouchUpOutside)
            self.view.addSubview(button)
        }
        
      
        buttonConnect.setTitle("Connect", forState: .Normal)
        buttonConnect.tag = 1
        buttonConnect.center = CGPoint(x: view.center.x, y: view.center.y - 200)
        buttonConnect.backgroundColor = UIColor.blackColor()
        buttonConnect.addTarget(self, action: #selector(ViewController.Connectivity(_:)), forControlEvents: .TouchDown)
        self.view.addSubview(buttonConnect)
        
        labelInfo.center = CGPoint(x: view.center.x, y: view.center.y - 100)
        labelInfo.textAlignment = NSTextAlignment.Center
        labelInfo.text = "Not Connected"
        self.view.addSubview(labelInfo)
        
        labelSpeed.center = CGPoint(x: view.center.x, y: view.center.y + 250)
        labelSpeed.textAlignment = NSTextAlignment.Center
        labelSpeed.text = "Speed: 0"
        self.view.addSubview(labelSpeed)
        
        // + and -
        let buttonMINUS = UIButton(frame: CGRect(x: 0, y: 0, width: 50, height: 50))
        buttonMINUS.setTitle("-", forState: .Normal)
        buttonMINUS.center = CGPoint(x: view.center.x - 100, y: view.center.y + 250)
        buttonMINUS.backgroundColor = UIColor.blackColor()
        buttonMINUS.addTarget(self, action: #selector(ViewController.ACCLeft), forControlEvents: .TouchDown)
        self.view.addSubview(buttonMINUS)
        
        let buttonPLUS = UIButton(frame: CGRect(x: 0, y: 0, width: 50, height: 50))
        buttonPLUS.setTitle("+", forState: .Normal)
        buttonPLUS.center = CGPoint(x: view.center.x + 100, y: view.center.y + 250)
        buttonPLUS.backgroundColor = UIColor.blackColor()
        buttonPLUS.addTarget(self, action: #selector(ViewController.ACCRight), forControlEvents: .TouchDown)
        self.view.addSubview(buttonPLUS)
        
    }
    @IBAction func ACCLeft(){
        if ( speed <= 170){
        upgradeLabel(++speed)
            self.sendCommand("SERV", value: String(speed))
        }
        }
    
    @IBAction func ACCRight(){
        if speed > 0 {
        upgradeLabel(--speed)
        self.sendCommand("SERV ", value: String(speed))
        }
        }
    
    @IBAction func AutoLeft(){
    self.sendCommand("SERV ", value: String(130))
            upgradeLabel(130)
    }
    
    @IBAction func AutoRight(){
        self.sendCommand("SERV ", value: String(50))
        upgradeLabel(50)
    }
    
    @IBAction func Stop(){
        self.sendCommand("SERV ", value: String(94))
        upgradeLabel(94)
    }
    
    @IBAction func ManuLeft(sender: UIButton){
        self.sendCommand("SERV ", value: String(130))
       upgradeLabel(130)
    }
    @IBAction func ManuRight(sender: UIButton){
        self.sendCommand("SERV ", value: String(50))
        upgradeLabel(50)
    }
    
    
    @IBAction func Connectivity(sender: UIButton){
        if ( sender.tag == 0){
            sender.setTitle("Connect", forState: .Normal)
            sender.tag = 1
                serial.disconnect()
        }
        else{
            labelInfo.text = "Connecting..."
            serial.startScanning()
            sender.enabled = false
            sender.backgroundColor = UIColor.lightGrayColor()
            NSTimer.scheduledTimerWithTimeInterval(5, target: self, selector: #selector(ViewController.connectTimeOut), userInfo: nil, repeats: false)
        }
     
    }
    
    func upgradeLabel(spd:Int){
        if ( serial.isConnected()){
            speed = abs(spd)
        labelSpeed.text = "Speed: \(String(94 - speed))"
        }
        }
    
    func connectTimeOut(){
        serial.stopScanning()
        if serial.isConnected() {
            return
        }
        else{
            buttonConnect.tag = 1
            buttonConnect.setTitle("Connect", forState: .Normal)
            labelInfo.text = "Failed to connect"
            buttonConnect.backgroundColor = UIColor.blackColor()
            buttonConnect.enabled = true;

        }
        
    }
    
    func connectionChanged(notification: NSNotification) {
        // Connection status changed. Indicate on GUI.
        let userInfo = notification.userInfo as! [String: Bool]
        
        dispatch_async(dispatch_get_main_queue(), {
            // Set image based on connection status
            if let isConnected: Bool = userInfo["isConnected"] {
                if isConnected {
                    self.labelInfo.text = "Connected"
                    
                    //update button
                    self.buttonConnect.tag = 0
                    self.buttonConnect.backgroundColor = UIColor.blackColor()
                    self.buttonConnect.enabled = true;
                    self.buttonConnect.setTitle("Disconnect", forState: .Normal)
                    // Send position
                        //self.sendPosition(UInt8(94.0))
                        // self.sendText("SERV1500")
                } else {
                 
                    self.labelInfo.text = "Not Connected"
                
                }
            }
        });
    }
    
    
     func sendPosition(position: UInt8) {
    // Send position to BLE Shield (if service exists and is connected)
    if let bleService = serial.bleService {
        bleService.writePosition(position)
       // bleService.sendMessageToDevice("asd");
    }
    else{
        print("Something wrong happened. Error: V004 - Check ViewController.swift")
        }
}

    func sendCommand(type:String, value: String) {
        // Send position to BLE Shield (if service exists and is connected)
        if let bleService = serial.bleService {
            bleService.sendMessageToDevice(type+value)
        }
        else{
            print("Something wrong happened. Error: V004 - Check ViewController.swift")
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }


}

