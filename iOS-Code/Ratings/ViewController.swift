//
//  ViewController.swift
//  arduinoWifiTest
//
//  Created by Guan Wong on 5/19/16.
//  Copyright © 2016 Guan Wong. All rights reserved.
//

import UIKit

import Darwin.C

//socket
let client:TCPClient = TCPClient(addr: "172.20.10.4", port: 80)

class ViewController: UIViewController {
//var manualViewController:ManualViewController?
    
    @IBAction func wifiBut(sender: UIButton) {
        let (success,errmsg)=client.connect(timeout: 1)
            if success{
                print("Successfully connected via WiFi")
                client.connected = true
                //update time of arduino
                
                //move to next scene
                self.performSegueWithIdentifier("asdf", sender: nil)
                
            }else{
                print(errmsg)
            }
    }
    
    
    @IBAction func blueBut(sender: UIButton) {
        serial.startScanning()
        NSTimer.scheduledTimerWithTimeInterval(5, target: self, selector: #selector(ViewController.connectTimeOut), userInfo: nil, repeats: false)
        
    }
    
    func connectTimeOut(){
        serial.stopScanning()
        if serial.isConnected() {
            //update time of arduino
             updateArduinoTime()
            // move to next scene
            self.performSegueWithIdentifier("asdf", sender: nil)
        }
        else{
            print("Bluetooth failed to connect")
        }
        
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        // initialize bluetooth
        serial
        
       
        
    }
    
    func updateArduinoTime(){
        let date = NSDate()
        let dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat = "HH:mm:ss"
        // this one is for hh:mm
        let str = dateFormatter.stringFromDate(date)
       // print(str)
        dateFormatter.dateFormat = "yyyy-MM-dd"
        let wday = dateFormatter.stringFromDate(date)
        
        let weekday = getDayOfWeek(wday)! - 1
      //  print("weekday: \(String(weekday!)) and Time: \(str)")
        
        let msg:String = "SETT \(String(weekday)) \(str)"
      //  let msg:String = "SETT \(String(weekday)) 9:26:00"
        print(msg)
        // command to send
        if(serial.isConnected()){
  
            if let svc = serial.bleService{
           
                svc.sendMessageToDevice(msg)
            }
        }
    }
    
    func getDayOfWeek(today:String)->Int? {
        
        let formatter  = NSDateFormatter()
        formatter.dateFormat = "yyyy-MM-dd"
        if let todayDate = formatter.dateFromString(today) {
            let myCalendar = NSCalendar(calendarIdentifier: NSCalendarIdentifierGregorian)!
            let myComponents = myCalendar.components(.Weekday, fromDate: todayDate)
            let weekDay = myComponents.weekday
            return weekDay
        } else {
            return nil
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
}

