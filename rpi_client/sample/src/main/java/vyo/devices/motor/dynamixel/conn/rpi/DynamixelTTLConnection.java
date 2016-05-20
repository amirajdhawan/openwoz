package vyo.devices.motor.dynamixel.conn.rpi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialFactory;
import com.pi4j.io.serial.SerialPortException;

import vyo.devices.motor.dynamixel.DynamixelMotorType;

/**
 * Connects to Dynamixel motors
 *
 * @author Eric Hochendoner (refactored by Benny)
 */
public class DynamixelTTLConnection {
	
	public final static String SERIAL_PORT = "/dev/ttyUSB0";
	
    public final static int[] BAUD_RATES = {9600, 19200, 38400, 57600, 115200, 200000, 250000, 400000, 500000, 1000000};
    public final static int DEFAULT_BAUD_RATE = 57600; // 34

    public DynamixelPiMotor[] connectedMotors;
    Serial serial;
    GpioPinDigitalOutput comLock;


    public DynamixelTTLConnection(GpioPinDigitalOutput comLock) {
        // using settings: PARITY = false, STOP_BITS = 1
        this(SerialFactory.createInstance(), comLock);
    }

    public DynamixelTTLConnection(Serial serial, GpioPinDigitalOutput comLock) throws NullPointerException {
        //if (serial == null || comLock == null) {
        //    throw new NullPointerException("Com obj can't be null");
        //}

        this.serial = serial;
        this.comLock = comLock;
    }

    public DynamixelPiMotor createMotor(int id) {
        return createMotor(id, DynamixelMotorType.AX_12, true);
    }

    public DynamixelPiMotor createMotor(int id, int motorType) {
        return createMotor(id, motorType, true);
    }

    public DynamixelPiMotor createMotor(int id, int motorType, boolean enableTorque) {
        // use that line when the motor are connected directly to the pi
        DynamixelPiMotor motor = new DynamixelPiMotor(id, motorType, serial, comLock);

        // use that line when the motor are connected to the arbotix
        //DynamixelPiMotor motor = new DynamixelPiMotor(id, serial, comLock);

        if (enableTorque) {
            motor.setTorqueEnable(true);
        }

        return motor;
    }

    public boolean isConnected() {
        return serial.isOpen();
    }

    public Serial connect(int baudRate) throws SerialPortException, IllegalStateException {
        if (serial.isClosed()) {
            // open the default serial port provided on the GPIO header
            serial.open(SERIAL_PORT, baudRate);
        }

        return serial;
    }

    public Serial connect() throws SerialPortException, IllegalStateException {
        return connect(DEFAULT_BAUD_RATE);
    }

    public void disconnect() {
        serial.close();
    }

    /**
     * ONLY TO BE USED WHEN A SINGLE MOTOR IS CONNECTED
     * This will reset the motor to its factory default settings,
     * This will take a while because it scans through all 65535 possible connections (256 baud rates * 256 motor ids)
     */
    public void resetToDefault() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("you are about to reset all the motor to their factory default settings");
        System.out.println("MAKE SURE THAT ONLY A SINGLE MOTOR IS CONNECTED and press any key to continue");

        try {
            br.read();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }


        for (int i = 0; i < 254; i++) {
            try {
                serial.close();
                serial = connect(2000000 / (i + 1));
                for (int j = 1; j < 253; j++) {

                    DynamixelPiMotor testMotor = createMotor(j);
                    System.out.println("Current baud = " + 2000000 / (i + 1) + " (" + i + ") resetting motor " + j);
                    testMotor.reset();

                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SerialPortException e) {
                System.out.println("Creating uart connection failed for baud rate " + BAUD_RATES[i]);
                continue;
            }
        }

    }

    // sets the state of the LEDs of all the connected motor to on
    public DynamixelPiMotor[] findAllMotors() {

        for (int i = 0; i < BAUD_RATES.length; i++) {

            try {
                serial.close();
                serial = connect(BAUD_RATES[i]);
            } catch (SerialPortException e) {
                System.out.println("Creating uart connection failed");
                continue;
            }

            for (int j = 0; j < 254; j++) {
                DynamixelPiMotor testMotor = createMotor(j);
                System.out.println("Current baud = " + BAUD_RATES[i] + " pinging motor " + j);
                testMotor.setLEDColor(0xFE);
                System.out.println("Color set");
            }
        }

        return null;
    }

    @Override
    public String toString() {
        String comLockPin = (comLock == null) ? null : comLock.getName();

        return "{" +
                "connectedMotors=" + Arrays.toString(connectedMotors) +
                ", serial=" + serial +
                ", comLock=" + comLockPin +
                '}';
    }
}