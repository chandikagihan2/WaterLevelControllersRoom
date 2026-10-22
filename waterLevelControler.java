import java.awt.*;
import java.util.*;
import javax.swing.*;

class WaterLevelObserver extends JFrame {
    private String id;

    public WaterLevelObserver() {
    }

    public WaterLevelObserver(String id) {
        this.id = id;
    }

    public void setId(String id ) {
        this.id = id;
    }
    public String  getId(){
        return id;
    }
	public void update(int waterLevel){
		 
	}
}
class Alarm extends WaterLevelObserver {
    private JLabel statusLabel;
    private int alarmLevel  = 50;

    public Alarm(String id) {
        super(id);
        setTitle("Alarm - " + id);
        setSize(320, 150);
        setLayout(new FlowLayout());

        statusLabel = new JLabel(id + " --> Alarm OFF (Limit: " + alarmLevel + ")", SwingConstants.CENTER);
        statusLabel.setFont(new Font("", Font.BOLD, 14));
        statusLabel.setForeground(Color.RED);

        add(statusLabel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocation(950,340);
        setVisible(true);
    }

    public void setalarmLevel(int threshold) {
        this.alarmLevel = threshold;
    }

    
    public void update(int waterLevel) {
        setTitle("Alarm - " + id);
        if (waterLevel > alarmLevel) {
            statusLabel.setText(id + " --> Alarm ON ( > " + alarmLevel + ")");
            getContentPane().setBackground(Color.RED);
            statusLabel.setForeground(Color.WHITE);
        } else {
            statusLabel.setText(id + " --> Alarm OFF (Limit: " + alarmLevel + ")");
            getContentPane().setBackground(Color.LIGHT_GRAY);
            statusLabel.setForeground(Color.BLACK);
        }
    }
}
class Splitter extends WaterLevelObserver{
    private JLabel spliLabel;
    private int spliLevel = 75;

    public Splitter(String id) {
		super(id);
        setTitle("Splitter ID  -"+ getId());
        setSize(320, 150);
        setLayout(new FlowLayout());

        spliLabel = new JLabel(getId()+ "--> Splitter OFF (Limit :)" +spliLevel+ ")",SwingConstants.CENTER);
        spliLabel.setFont(new Font("",Font.BOLD,14));
        spliLabel.setForeground(Color.GREEN);

        add(spliLabel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
	}
	
	public void update(int waterLevel){
		System.out.println(waterLevel>75 ? getId()+"-->Splitter ON":getId()+"-->Splitter OFF");

	}
}
class Display extends WaterLevelObserver{
	public Display(String id) {
		super(id);
	}

	public void update(int waterLevel){
		System.out.println(getId()+"-->Water Level : "+waterLevel);
	}
}
class SMSSender extends WaterLevelObserver{
	public SMSSender(String id) {
		super(id);
        setTitle("");

	}

    
	
	public void update(int waterLevel){
		System.out.println(getId()+"-->Sending SMS..."+waterLevel);
	}
}
class ControlRoom{
	private WaterLevelObserver[] waterLevelObserverArray=new WaterLevelObserver[0];
	private int waterLevel;

    
	public void addWaterLevelObserver(WaterLevelObserver waterLevelObserver){
		extendArray();
		waterLevelObserverArray[waterLevelObserverArray.length-1]=waterLevelObserver;
	}

	public void setWaterLevel(int waterLevel){
		if(this.waterLevel!=waterLevel){
			this.waterLevel=waterLevel;
			notifyDevices();
		}
	}
	private void notifyDevices(){
		for(WaterLevelObserver ob : waterLevelObserverArray){
			ob.update(waterLevel);
		}
	}
	private void extendArray(){
		WaterLevelObserver[] tempWaterLevelObserverArray=new WaterLevelObserver[waterLevelObserverArray.length+1];
		for (int i = 0; i < waterLevelObserverArray.length; i++){
			tempWaterLevelObserverArray[i]=waterLevelObserverArray[i];
		}
		waterLevelObserverArray=tempWaterLevelObserverArray;	
	}
}
class WaterLevelControler {
	public static void main(String[] args) {
		ControlRoom controlRoom=new ControlRoom();

		controlRoom.addWaterLevelObserver(new Alarm(""));
		controlRoom.addWaterLevelObserver(new Display(""));
		controlRoom.addWaterLevelObserver(new SMSSender(""));
		controlRoom.addWaterLevelObserver(new Splitter(""));

		Random r=new Random();
		while(true){
			int waterLevel=r.nextInt(101);
			controlRoom.setWaterLevel(waterLevel);
			System.out.println();
			try{Thread.sleep(1000);}catch(InterruptedException ex){}
		}
	}
}

