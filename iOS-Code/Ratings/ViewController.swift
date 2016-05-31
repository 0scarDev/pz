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
let client:TCPClient = TCPClient(addr: "192.168.4.1 ", port: 1001)

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
        
        updateArduinoTime()
        
    }
    
    func updateArduinoTime(){
        let date = NSDate()
        let dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat = "HH:mm"
        // this one is for hh:mm
        let str = dateFormatter.stringFromDate(date)
       // print(str)
        dateFormatter.dateFormat = "yyyy-MM-dd"
        let wday = dateFormatter.stringFromDate(date)
        
        let weekday = getDayOfWeek(wday)
        print("weekday: \(String(weekday!)) and Time: \(str)")
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

