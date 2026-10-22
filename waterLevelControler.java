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
        setTitle("Alarm - " + getId());
        if (waterLevel > alarmLevel) {
            statusLabel.setText(getId() + " --> Alarm ON ( > " + alarmLevel + ")");
            getContentPane().setBackground(Color.RED);
            statusLabel.setForeground(Color.WHITE);
        } else {
            statusLabel.setText(getId() + " --> Alarm OFF (Limit: " + alarmLevel + ")");
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

        spliLabel = new JLabel(getId() + " --> Splitter OFF (Limit: " + spliLevel + ")", SwingConstants.CENTER);
        spliLabel.setFont(new Font("",Font.BOLD,14));
        spliLabel.setForeground(Color.GREEN);

        add(spliLabel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
	}
	
    public void setSplitterLevel(int threshold) {
        this.spliLevel = threshold;
    }

    public void update(int waterLevel) {
        boolean isOn = waterLevel > spliLevel;
        String status = getId() + (isOn ? " --> Splitter ON" : " --> Splitter OFF");
        spliLabel.setText(status + " (Limit: " + spliLevel + ")");
        System.out.println(status);
    }
}
class Display extends WaterLevelObserver {
    private JLabel levelLabel;

    public Display(String id) {
        super(id);
        setTitle("Display - " + id);
        setSize(320, 150);
        setLayout(new FlowLayout());

        levelLabel = new JLabel(id + " --> Water Level : 0", SwingConstants.CENTER);
        levelLabel.setFont(new Font("", Font.BOLD, 15));

        add(levelLabel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocation(1000,500);
        setVisible(true);
    }

    
    public void update(int waterLevel) {
        setTitle("Display - " + getId());
        levelLabel.setText(getId() + " --> Water Level : " + waterLevel);
    }
}
class SMS extends WaterLevelObserver {
    private JLabel smsLabel;

    public SMS(String id) {
        super(id);
        setTitle("SMS Sender - " + id);
        setSize(320, 150);
        setLayout(new FlowLayout());

        smsLabel = new JLabel(id + " --> Waiting...", SwingConstants.CENTER);
        smsLabel.setFont(new Font("", Font.PLAIN, 14));

        add(smsLabel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocation(900,500);
        setVisible(true);
    }

    public void update(int waterLevel) {
        setTitle("SMS Sender - " + getId());
        smsLabel.setText(getId() + " --> Sending SMS Now Water Level... " + waterLevel);
    }
}
class ControlRoom extends JFrame {
    private WaterLevelObserver[] waterLevelObserverArray = new WaterLevelObserver[0];
    private int waterLevel;

    private JSlider levelSlider;
    private JLabel currentLevelLabel;

    private JSpinner spinnerAlarm;
    private JSpinner spinnerSplitter;
    private JButton btnUpdateLimits;

    private JTextField txtCommonId;
    private JButton btnUpdateId;

    public ControlRoom() {
        setTitle("Water Level Control Room");
        setSize(480, 380);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 1, 5, 5));

        JLabel titleLabel = new JLabel("Control Room Window", SwingConstants.CENTER);
        titleLabel.setFont(new Font("", Font.BOLD, 16));
        add(titleLabel);

        levelSlider = new JSlider(0, 100, 0);
        levelSlider.setMajorTickSpacing(20);
        levelSlider.setPaintTicks(true);
        levelSlider.setPaintLabels(true);

        levelSlider.addChangeListener(e -> {
            setWaterLevel(levelSlider.getValue());
        });
        add(levelSlider);

        currentLevelLabel = new JLabel("Current Water Level: 0", SwingConstants.CENTER);
        currentLevelLabel.setFont(new Font("", Font.BOLD, 14));
        add(currentLevelLabel);

        JPanel idPanel = new JPanel(new FlowLayout());
        txtCommonId = new JTextField("", 10);
        btnUpdateId = new JButton("Set ID");

        idPanel.add(new JLabel("Device ID:"));
        idPanel.add(txtCommonId);
        idPanel.add(btnUpdateId);
        add(idPanel);

        JPanel inputPanel = new JPanel(new FlowLayout());
        spinnerAlarm = new JSpinner(new SpinnerNumberModel(50, 0, 100, 1));
        spinnerSplitter = new JSpinner(new SpinnerNumberModel(75, 0, 100, 1));
        btnUpdateLimits = new JButton("Set Limits");

        inputPanel.add(new JLabel("Alarm >"));
        inputPanel.add(spinnerAlarm);
        inputPanel.add(new JLabel("Splitter >"));
        inputPanel.add(spinnerSplitter);
        inputPanel.add(btnUpdateLimits);
        add(inputPanel);

        btnUpdateId.addActionListener(e -> {
            String newId = txtCommonId.getText().trim();
            if (!newId.isEmpty()) {
                for (WaterLevelObserver ob : waterLevelObserverArray) {
                    ob.setId(newId);
                }
                JOptionPane.showMessageDialog(this, "ID Updated to '" + newId + "' for all devices!");
                notifyDevices();
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a valid ID!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnUpdateLimits.addActionListener(e -> {
            int newAlarmLimit = (Integer) spinnerAlarm.getValue();
            int newSplitterLimit = (Integer) spinnerSplitter.getValue();

            for (WaterLevelObserver ob : waterLevelObserverArray) {
                if (ob instanceof Alarm) {
                    ((Alarm) ob).setalarmLevel(newAlarmLimit);
                } else if (ob instanceof Splitter) {
                    ((Splitter) ob).setSplitterLevel(newSplitterLimit);
                }
            }
            JOptionPane.showMessageDialog(this, "Limits Updated Successfully!");
            notifyDevices();
        });

        setLocation(100, 100);
        setVisible(true);
    }

    public void addWaterLevelObserver(WaterLevelObserver observer) {
        extendArray();
        waterLevelObserverArray[waterLevelObserverArray.length - 1] = observer;
    }

    public void setWaterLevel(int waterLevel) {
        if (this.waterLevel != waterLevel) {
            this.waterLevel = waterLevel;
            currentLevelLabel.setText("Current Water Level: " + waterLevel);
            notifyDevices();
        }
    }

    private void notifyDevices() {
        for (WaterLevelObserver ob : waterLevelObserverArray) {
                ob.update(waterLevel);
        }
    }

    private void extendArray() {
        WaterLevelObserver[] tempArray = new WaterLevelObserver[waterLevelObserverArray.length + 1];
        for (int i = 0; i < waterLevelObserverArray.length; i++) {
            tempArray[i] = waterLevelObserverArray[i];
        }
        waterLevelObserverArray = tempArray;
    }
}
class WaterLevelControler {
	public static void main(String[] args) {
		ControlRoom controlRoom=new ControlRoom();

		controlRoom.addWaterLevelObserver(new Alarm(""));
		controlRoom.addWaterLevelObserver(new Display(""));
		controlRoom.addWaterLevelObserver(new SMS(""));
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
