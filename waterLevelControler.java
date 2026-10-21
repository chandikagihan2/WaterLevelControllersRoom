import java.util.*;
import javax.swing.JFrame;

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
	public Alarm(String id) {
		super(id);
	}
	
	public void update(int waterLevel){
		System.out.println(waterLevel>50 ? getId()+"-->Alarm ON":getId()+"-->Alarm OFF");
	}
}
class Splitter extends WaterLevelObserver{
	public Splitter(String id) {
		super(id);
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

