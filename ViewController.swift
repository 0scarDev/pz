import UIKit
import Darwin
import AVFoundation

class ViewController: UIViewController {
    
    var format : NSDateFormatter!
    var labelClock : UILabel!
    var timeinput : UITextField!
    var url = NSURL(fileURLWithPath:NSBundle.mainBundle().pathForResource("tomorrow", ofType:"wav")!)
    var audioPlayer = AVAudioPlayer()

    @IBOutlet weak var label: UILabel!
    
    @IBOutlet weak var texty: UITextField!
   
    
    @IBAction func Submit(sender: AnyObject) {
        label.text = texty.text
    }

    @IBAction func end(sender: AnyObject) {
        exit(0)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        audioPlayer = AVAudioPlayer(contentsOfURL: url, error: nil)
        
        labelClock = UILabel(frame: CGRectMake(0,0,250,100))
        labelClock.center = view.center
        labelClock.textAlignment = NSTextAlignment.Center
        labelClock.numberOfLines = 0 //Multi-lines
        labelClock.font = UIFont(name: "Helvetica-Bold", size: 20)
        view.addSubview(labelClock)
        
        
        
        format = NSDateFormatter()
        
        
        //Custom Format for clock
        format.dateFormat = "hh:mm a"
        
        //Update the date and time of the clock periodically.
        NSTimer.scheduledTimerWithTimeInterval(1, target: self, selector: "updateClock", userInfo: nil, repeats: true)
        
    }
    
    //Update clock every second
    func updateClock() {
        let now = NSDate()

        labelClock.text = format.stringFromDate(now)
        if (texty.text == labelClock.text){
            audioPlayer.play() //replace code with closing blinds
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    
}

