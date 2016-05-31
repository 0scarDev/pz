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
    var isConnected = false
//var manualViewController:ManualViewController?
    
    @IBAction func wifiBut(sender: UIButton) {
        let (success,errmsg)=client.connect(timeout: 1)
            if success{
                print("Successfully connected via WiFi")
                isConnected = true
                self.performSegueWithIdentifier("asdf", sender: nil)
                
            }else{
                print(errmsg)
            }
    }
    
    
    @IBAction func blueBut(sender: UIButton) {
        
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        
        
        let buttonMINUS = UIButton(frame: CGRect(x: 0, y: 0, width: 50, height: 50))
        buttonMINUS.setTitle("-", forState: .Normal)
        buttonMINUS.center = CGPoint(x: view.center.x - 100, y: view.center.y + 250)
        buttonMINUS.backgroundColor = UIColor.blackColor()
        buttonMINUS.addTarget(self, action: #selector(ViewController.ACCLeft), forControlEvents: .TouchDown)
        self.view.addSubview(buttonMINUS)
        
        
        
        
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func ACCLeft(){
        //connect
        let message = "it's working :)"
        
        if (!isConnected){
            let (success,errmsg)=client.connect(timeout: 1)
            if success{
                isConnected = true
                //send
                // let (success,errmsg)=client.send(data: message.dataUsingEncoding(NSUTF8StringEncoding)!)
                let (success,errmsg)=client.send(str: message)
                if success{
                    
                    //receive
                    /* let data=client.read(1024*10)
                     if let d=data{
                     if let str=String(bytes: d, encoding: NSUTF8StringEncoding){
                     print(str)
                     }
                     }
                     */}else{
                    print(errmsg)
                }
            }else{
                print(errmsg)
            }
            print("done")
        }
            
        else{
            
            client.send(str: message)
        }
        
    }
    
}

