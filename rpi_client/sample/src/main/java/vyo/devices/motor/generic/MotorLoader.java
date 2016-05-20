package vyo.devices.motor.generic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import vyo.devices.motor.generic.MotorController.Motor;


/**
 * This class loads the motor data from a configuration file (XML)
 * each motor has its limits, name and id.
 *
 * @author hoffman
 */
public class MotorLoader extends DefaultHandler {

    // Constant keys for the map
    public static final String MOTOR_NAME = "name";
    public static final String MOTOR_ID = "id";
    public static final String MOTOR_MIN_POS = "minPos";
    public static final String MOTOR_MAX_POS = "maxPos";
    public static final String MOTOR_FLIPPED = "flipped";
    public static final String MOTOR_TYPE = "motorType";
    public static final String MOTOR_DEFAULT_ACC = "defaultAcc";
    public static final String MOTOR_DEFAULT_VEL = "defaultVel";
    public static final String MOTOR_MAX_VEL = "maxVel";
    public static final String MOTOR_ZERO_POS = "zero";

    private final boolean D = false;
    private Map<String, String> motor;
    private List<Map<String, String>> motorMaps = new ArrayList<>();


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attr) throws SAXException {

        if (D) {
            System.out.println("startElement " + qName);
        }

        if (qName.equalsIgnoreCase("Motor")) {
            motor = new HashMap<>();
            motor.put(MOTOR_NAME, attr.getValue("name"));
            motor.put(MOTOR_ID, attr.getValue("id"));
            motor.put(MOTOR_MAX_POS, attr.getValue("maxPos"));
            motor.put(MOTOR_MIN_POS, attr.getValue("minPos"));
            motor.put(MOTOR_FLIPPED, attr.getValue("flipped"));
            motor.put(MOTOR_TYPE, attr.getValue("motorType"));
            motor.put(MOTOR_DEFAULT_ACC, attr.getValue("defaultAcc"));
            motor.put(MOTOR_DEFAULT_VEL, attr.getValue("defaultVel"));
            motor.put(MOTOR_MAX_VEL, attr.getValue("maxVel"));
            motor.put(MOTOR_ZERO_POS, attr.getValue("zero"));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (D) {
            System.out.println("endElement " + qName);
        }

        if (qName.equalsIgnoreCase("Motor")) {
            motorMaps.add(motor);
        }
    }

    public Map<String, Motor> load(MotorController mc, String motorConfigURL) {
        InputStream is;

        try {
        	File motorConfigFile = new File(motorConfigURL);
        	
            is = new FileInputStream(motorConfigFile);

            SAXParserFactory sf = SAXParserFactory.newInstance();
            SAXParser sp = sf.newSAXParser();
            sp.parse(is, this);
        } catch (IOException e) {
            System.out.println("Couldn't open resource " + motorConfigURL);
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Couldn't parse motor config file: " + e.getMessage());
        }

        Map<String, Motor> motorList = new HashMap<>();

        for (Map<String, String> motorMap : motorMaps) {
            Motor m = mc.createMotor(motorMap);
            motorList.put(m.getName(), m);
        }

        return motorList;
    }
}
