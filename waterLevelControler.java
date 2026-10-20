import java.awt.*;
import javax.swing.*;

class waterLevelObserver extends JFrame {
    public void update(int waterLevel) {
    }
}

class Alarm extends waterLevelControler {
    private String id;
    private JLabel AlarmLabel;

    public Alarm(String id) {
        this.id = id;

    }

}

class Display extends waterLevelControler {

}

class Splitter extends waterLevelObserver {

}

class SMS extends waterLevelObserver {

}

class ControlRoom extends JFrame {
    private waterLevelObserver[] waterLevelObserverArray = new waterLevelObserver[0];
    private int waterLevel;

    private JSlider waterLevelJSlider;
    private JLabel liveWaterLevelLabel;

    public ControlRoom() {
        setTitle("Water Level Control Room");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 1, 5, 5));

        JLabel titleLabel = new JLabel("Control Room ", SwingConstants.CENTER);
        titleLabel.setFont(new Font("", Font.BOLD, 16));
        add(titleLabel);

        waterLevelJSlider = new JSlider(0, 100, 0);
        waterLevelJSlider.setMajorTickSpacing(20);
        waterLevelJSlider.setPaintTicks(true);
        waterLevelJSlider.setPaintLabels(true);

        waterLevelJSlider.addChangeListener(e -> {
            setWaterLevel(waterLevelJSlider.getValue());
        });
    }
}

class waterLevelControler {
    public static void main(String[] args) {
                SwingUtilities.invokeLater(() -> {
            ControlRoom controlRoom = new ControlRoom();

            controlRoom.addWaterLevelObserver(new Alarm("A0001"));
            controlRoom.addWaterLevelObserver(new Display("D0001"));
            controlRoom.addWaterLevelObserver(new SMSsender("SM0001"));
            controlRoom.addWaterLevelObserver(new Splitter("SP001"));
        });
    }
}
