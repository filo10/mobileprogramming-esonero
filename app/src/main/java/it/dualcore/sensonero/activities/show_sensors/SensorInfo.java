package it.dualcore.sensonero.activities.show_sensors;

public class SensorInfo {

    /* getting and remembering some particular attributes about a sensor */
    String name;
    String type;
    String number;

    public SensorInfo(String name, String type, int number) {
        this.name = name;
        this.type = type;
        this.number = Integer.toString(number);
    }
}
