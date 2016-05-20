package vyo.devices.motor.dynamixel.conn.rpi;

import java.io.IOException;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataListener;

import vyo.devices.motor.dynamixel.DynamixelMotor;
import vyo.devices.motor.dynamixel.DynamixelUtil;
 

/**
 * Control all the basic functionalities of a Dynamixel motor
 *
 * @author Benny
 */
public class DynamixelPiMotor extends DynamixelMotor {
    private Serial serial;

    // semaphore that locks incoming Rx while Tx
    private GpioPinDigitalOutput comLock;
    private static boolean isLocked = false;


    public DynamixelPiMotor(int id, int motorType, Serial serial, GpioPinDigitalOutput comLock) {
        super(id, motorType);

        if (serial == null) throw new NullPointerException("Com obj can't be null");

        this.serial = serial;   // may be disconnected
        this.comLock = comLock; // should be initialized

        // create and register the serial data listener
        this.serial.addListener(new SerialDataListener() {
            @Override
            public void dataReceived(SerialDataEvent event) {
                // print out the data received to the console
                System.out.print("Data received: " + event.getData());
            }
        });
    }

    protected byte[] sendMessage(byte[] message) throws IOException {

        lockCommunication(true);

        DynamixelUtil.sleep(10);
        serial.write(message);

        lockCommunication(false);

        if (D) {
            System.out.printf("%d Bytes sent to %s motor #%d:\n", message.length, motorType, id);
            DynamixelUtil.printHexByteArray(message);
        }

        return null;  //return receiveMessage();
    }

    private void lockCommunication(boolean lock) {
        if (comLock != null && (isLocked != lock)) {
            comLock.toggle();
            isLocked = !isLocked;
        }
    }
}