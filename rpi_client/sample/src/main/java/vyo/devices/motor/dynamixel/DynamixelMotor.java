package vyo.devices.motor.dynamixel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;


/**
 * Dynamixel motor abstract class
 *
 * @author Benny on 9/11/2015.
 */
public abstract class DynamixelMotor {

    protected int id;
    protected int motorType;
    protected int mMaxPosition;
    protected int mRotationMode;
    protected boolean flipped;

    protected static ArrayList<int[]> syncMoveList;
    protected static int syncDataLength = -1;
    //TODO fix xl320
    protected final double encoderPerSecPerUnit = 1.58;
    protected int currentPosition = 0;
    protected boolean D = true;

    protected final byte[] mxHeader = new byte[] {(byte) 0xFF, (byte) 0xFF};
    protected final byte[] xlHeader = new byte[] {(byte) 0xFF, (byte) 0xFF, (byte) 0xFD, (byte) 0x00};

    public DynamixelMotor(int id, int motorType) {
        this.id = id;         // not verifying if the id is in use
        this.motorType = motorType;
        this.mMaxPosition = getMotorMaxPosition();
        this.currentPosition = getZeroPosition();
        if (syncMoveList != null) {syncMoveList = new ArrayList<>();}
    }

    protected abstract byte[] sendMessage(byte[] message) throws IOException;

    protected byte[] readAddress(int id, int address, int length) {
        ByteBuffer buffer = ByteBuffer.allocate(13);
        int messageLength = 0x04;
        byte[] params;

        try {
            switch (motorType) {
                case DynamixelMotorType.XL_320:
                    params = new byte[] {(byte) id, (byte) (messageLength + 2), (byte) 0x00, (byte) 0x02,
                            (byte) address, (byte) 0x00, (byte) length};

                    buffer.put(xlHeader);
                    buffer.put(params);

                    int crc16 = DynamixelUtil.generateDynamixelCRC16(buffer, buffer.position());
                    buffer.put((byte)(crc16 & 0x00FF));          // CRC low
                    buffer.put((byte)((crc16 >> 8) & 0x00FF));   // CRC high
                    break;
                default:
                    params = new byte[] {(byte) id, (byte) messageLength, (byte) 0x02, (byte) address, (byte) length};

                    byte checksum = DynamixelUtil.generateChecksum(params);

                    buffer.put(mxHeader);
                    buffer.put(params);
                    buffer.put(checksum);
                    break;
            }

            return sendMessage(buffer.array());
        } catch (IOException e) {
            e.printStackTrace();

        }
        return null;
    }

    protected byte[] writeAddress(int id, int address, int value) {
        ByteBuffer buffer = ByteBuffer.allocate(13);
        int length = 0x04;
        byte[] params;

        try {
            switch (motorType) {
                case DynamixelMotorType.XL_320:
                    params = new byte[] {(byte) id, (byte) (length + 2), (byte) 0x00, (byte) 0x03, (byte) address,
                            (byte) 0x00, (byte) value};

                    buffer.put(xlHeader);
                    buffer.put(params);

                    int crc16 = DynamixelUtil.generateDynamixelCRC16(buffer, buffer.position());
                    buffer.put((byte)(crc16 & 0x00FF));          // CRC low
                    buffer.put((byte)((crc16 >> 8) & 0x00FF));   // CRC high
                    break;
                default:
                    params = new byte[] {(byte) id, (byte) length, (byte) 0x03, (byte) address, (byte) value};
                    byte checksum = DynamixelUtil.generateChecksum(params);

                    buffer.put(mxHeader);
                    buffer.put(params);
                    buffer.put(checksum);
                    break;

            }

            sendMessage(buffer.array());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected byte[] writeAddress(int id, int address, int lowByte, int highByte) {
        ByteBuffer buffer = ByteBuffer.allocate(14);
        int length = 0x05;
        byte[] params;

        try {
            //System.out.println("Motor " + id + " Type " + motorType + " / " + this.hashCode());

            switch (motorType) {
                case DynamixelMotorType.XL_320:
                    params = new byte[] {(byte) id, (byte) (length + 2), (byte) 0x00, (byte) 0x03,
                            (byte) address, (byte) 0x00, (byte) lowByte, (byte) highByte};

                    buffer.put(xlHeader);
                    buffer.put(params);

                    int crc16 = DynamixelUtil.generateDynamixelCRC16(buffer, buffer.position());

                    buffer.put((byte)(crc16 & 0x00FF));          // CRC low
                    buffer.put((byte)((crc16 >> 8) & 0x00FF));   // CRC high
                    break;
                default:
                    params = new byte[] {(byte) id, (byte) length, (byte) 0x03, (byte) address,
                            (byte) lowByte, (byte) highByte};
                    byte checksum = DynamixelUtil.generateChecksum(params);

                    buffer.put(mxHeader);
                    buffer.put(params);
                    buffer.put(checksum);
                    break;
            }
            if (D) DynamixelUtil.printHexByteArray(buffer.array());
            sendMessage(buffer.array());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected byte[] writeAddress(int id, int address, byte[] dataArr) {
        ByteBuffer buffer = ByteBuffer.allocate(12 + dataArr.length);
        int length = 0x03 + dataArr.length;
        byte[] params;

        try {
            switch (motorType) {
                case DynamixelMotorType.XL_320:
                    params = new byte[] {(byte) id, (byte) (length + 2), (byte) 0x00, (byte) 0x03,
                            (byte) address, (byte) 0x00};

                    buffer.put(xlHeader);
                    buffer.put(params);
                    buffer.put(dataArr);

                    int crc16 = DynamixelUtil.generateDynamixelCRC16(buffer, buffer.position());

                    buffer.put((byte)(crc16 & 0x00FF));          // CRC low
                    buffer.put((byte)((crc16 >> 8) & 0x00FF));   // CRC high
                    break;
                default:
                    params = new byte[] {(byte) id, (byte) length, (byte) 0x03, (byte) address};

                    buffer.put(mxHeader);
                    buffer.put(params);
                    buffer.put(dataArr);

                    byte checksum = DynamixelUtil.generateChecksum(buffer, 2, buffer.position());
                    buffer.put(checksum);
                    break;
            }

            if (D) DynamixelUtil.printHexByteArray(dataArr);
            if (D) DynamixelUtil.printHexByteArray(buffer.array());
            sendMessage(buffer.array());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private byte[] syncWriteAddress() {
        int[] length = getMotorPerController();

        // ((L + 1) * N) + headers   (L: Data Length per motor [2], N: the number of motors[2 / 3])
        ByteBuffer xlBuffer = ByteBuffer.allocate((length[0] * (syncDataLength + 1)) + 9);  // 2 motors
        ByteBuffer mxBuffer = ByteBuffer.allocate((length[1] * (syncDataLength + 1)) + 8);  // 3 motors

        byte[] xlSyncHeader = new byte[] {(byte) 0xFE, (byte) 0x1E, (byte) 0x00,
                (byte)((length[1] * (syncDataLength + 1)) % 256), (byte) 0x00};
        byte[] mxSyncHeader = new byte[] {(byte) 0xFE, (byte)(((length[0] * (syncDataLength + 1)) + 4) % 256),
                (byte) 0X83, (byte) 0x1E, (byte) 0x02};

        xlBuffer.put(xlHeader).put(xlSyncHeader);
        xlBuffer.put(mxHeader).put(mxSyncHeader);
        int motorType;

        try {
            for (int[] arr : syncMoveList) {
                motorType = DynamixelMotorType.getMotorType(arr[0]);

                switch (motorType) {
                    case DynamixelMotorType.XL_320:
                        xlBuffer.put((byte) arr[0]);  // motor id
                        for (int i = 2; i < syncMoveList.size(); i++) {
                            xlBuffer.put((byte) arr[i]);
                        }

                        break;
                    default:
                        mxBuffer.put((byte) arr[0]);  // motor id
                        for (int i = 2; i < syncMoveList.size(); i++) {
                            xlBuffer.put((byte) arr[i]);
                        }
                        break;
                }
            }

            // calc checksum / crc16

            int crc16 = DynamixelUtil.generateDynamixelCRC16(xlBuffer, xlBuffer.position());
            xlBuffer.put((byte)(crc16 & 0x00FF));          // CRC low
            xlBuffer.put((byte)((crc16 >> 8) & 0x00FF));   // CRC high

            byte checksum = DynamixelUtil.generateChecksum(mxBuffer, 2, mxBuffer.position());
            mxBuffer.put(checksum);

            sendMessage(xlBuffer.array());
            DynamixelUtil.sleep(12); // todo: shrink if possible
            sendMessage(mxBuffer.array());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            syncMoveList.clear();
            syncDataLength = 0;
        }

        return null;
    }

    public byte[] ping() {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        int length = 0x02;
        byte[] params;

        try {
            switch (motorType) {
                case DynamixelMotorType.XL_320:
                    params = new byte[] {(byte) id, (byte) (length + 1), (byte) 0x00, (byte) 0x01};

                    buffer.put(xlHeader);
                    buffer.put(params);

                    int crc16 = DynamixelUtil.generateDynamixelCRC16(buffer, buffer.position());
                    buffer.put((byte)(crc16 & 0x00FF));          // CRC low
                    buffer.put((byte)((crc16 >> 8) & 0x00FF));   // CRC high
                    break;
                default:
                    params = new byte[] {(byte) id, (byte) length, (byte) 0x01};
                    byte checksum = DynamixelUtil.generateChecksum(params);

                    buffer.put(mxHeader);
                    buffer.put(params);
                    buffer.put(checksum);
                    break;
            }

            return sendMessage(buffer.array());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void reset() {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        int length = 0x02;
        byte[] params;

        try {
            switch (motorType) {
                case DynamixelMotorType.XL_320:
                    params = new byte[] {(byte) id, (byte) (length + 1), (byte) 0x00, (byte) 0x06};

                    buffer.put(xlHeader);
                    buffer.put(params);

                    int crc16 = DynamixelUtil.generateDynamixelCRC16(buffer, buffer.position());
                    buffer.put((byte)(crc16 & 0x00FF));          // CRC low
                    buffer.put((byte)((crc16 >> 8) & 0x00FF));   // CRC high
                    break;
                default:
                    params = new byte[] {(byte) id, (byte) length, (byte) 0x06};
                    byte checksum = DynamixelUtil.generateChecksum(params);

                    buffer.put(mxHeader);
                    buffer.put(params);
                    buffer.put(checksum);
                    break;
            }

            sendMessage(buffer.array());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reboot() {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        int length = 0x02;
        byte[] params;

        try {
            switch (motorType) {
                case DynamixelMotorType.XL_320:
                    params = new byte[] {(byte) id, (byte) (length + 1), (byte) 0x00, (byte) 0x08};

                    buffer.put(xlHeader);
                    buffer.put(params);

                    int crc16 = DynamixelUtil.generateDynamixelCRC16(buffer, buffer.position());
                    buffer.put((byte)(crc16 & 0x00FF));          // CRC low
                    buffer.put((byte)((crc16 >> 8) & 0x00FF));   // CRC high
                    break;
                default:
                    params = new byte[] {(byte) id, (byte) length, (byte) 0x08};

                    byte checksum = DynamixelUtil.generateChecksum(params);

                    buffer.put(mxHeader);
                    buffer.put(params);
                    buffer.put(checksum);
                    break;
            }

            sendMessage(buffer.array());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   /*
	*  SETTERS
	*/

    public void setID(int newID) {
        writeAddress(id, 0x03, newID);
        this.id = newID;
    }

    public void setFlipped(boolean shouldFlip) { this.flipped = shouldFlip; }

    public void setMotorType(int motor) {
        this.motorType = motor;
    }

    public void setLEDColor(int color) {
        writeAddress(id, 0x19, color);
    }

    public void addSyncMove(int position) {
        addSyncMove(position, -1);
    }

    public void addSyncMove(int position, int speed) {
        if (position < 0 || position > mMaxPosition) {
            System.out.println("Position [" + position + "] out of bounds!");
            return;
        }

        if (speed > 1023) { speed = 1023; }

        // make sure all the data is in the same length
        if (syncMoveList.isEmpty()) {
            syncDataLength = (speed == -1) ? 2 : 4;
        } else if ((syncDataLength == 2 && speed != -1) || (syncDataLength == 4 && speed == -1)) {
            System.out.println("Sync move data length mismatch, it will not be added!");
            return;
        } else if (syncDataLength == 4) {
            syncMoveList.add(new int[] {id, 0x1E, position % 256, position / 256, speed % 256, speed / 256});
        } else {
            syncMoveList.add(new int[] {id, 0x1E, position % 256, position / 256});
        }

        // update current position
        this.currentPosition = position;
    }

    public void syncMove() {
        if (syncMoveList != null && !syncMoveList.isEmpty()) {
            syncWriteAddress();
        }
    }

    public void moveToPosition(int position) {
        if (position < 0 || position > mMaxPosition) {
            System.out.println("Position [" + position + "] out of bounds!");
            return;
        }

        this.currentPosition = position;
        if (D) {
            System.out.println("Move to position " + position
                    + ";   low: " + position % 256 + ",  hi: " + position / 256);
        }
        writeAddress(id, 0x1E, position % 256, position / 256);
    }

    public void moveToPosition(int position, int speed) {
        if (position < 0 || position > mMaxPosition) {
            System.out.println("Position [" + position + "] out of bounds!");
            return;
        }

        if (speed > 1023) { speed = 1023; }

        if (speed <= 0) { speed = 1; }

        this.currentPosition = position;
        if (D) {
            System.out.println("Move to position " + position
                    + ";   low: " + position % 256 + ",  hi: " + position / 256
                    + " at speed " + speed + ";   low: " + speed % 256 + ",  hi: " + speed / 256);
        }

        writeAddress(id, 0x1E, new byte[] {(byte) (position % 256), (byte) (position / 256),
                (byte) (speed % 256), (byte) (speed / 256)});
    }

    public void moveToPositionInTime(int newPos, double time) {
        System.out.println("currentPosition: " + currentPosition);
        System.out.println("newPosition: " + newPos);
        System.out.println("distance: " + (currentPosition - newPos));
        System.out.println("time: " +time);
        System.out.println("speedFromEnc: " + getSpeedFromEncoder(currentPosition - newPos, time));


        setSpeed(getSpeedFromEncoder(currentPosition - newPos, time));
        //moveToPosition(getEncoderFromCoefficient(newPosition));
        moveToPosition(newPos);
    }

    public void moveContinuously(int offset, int position) {
        if (offset < -24576 || offset > 24576) {
            System.out.println("Offset [" + offset + "] out of bounds!");
            return;
        }

        this.currentPosition = position; // new position after the offset
        if (D) {
            System.out.println("Move with offset " + offset
                    + ";   low: " + offset % 256 + ",  hi: " + offset / 256);
        }
        // todo cng mode
        writeAddress(id, 0x14, offset % 256, offset / 256);
    }

    public void setSpeed(int speed) {
        if (speed > 1023) {
            speed = 1023;
        } else if (speed <= 0) {
            speed = 1;
        }

        if (D) { System.out.println("Move at speed " + speed + ";   low: " + speed % 256 + ",  hi: " + speed / 256); }
        writeAddress(id, 0x20, speed % 256, speed / 256);
    }

    public void setAcceleration(int acceleration) {
        if (acceleration > 254) {
            acceleration = 254;
        } else if (acceleration < 0) {
            return;
        }

        if (D) {
            System.out.println("Move at acceleration " + acceleration
                    + ";   low: " + acceleration % 256 + ",  hi: " + acceleration / 256);
        }
        writeAddress(id, 0x49, acceleration);
    }

    public void setResponseType(int type) {
        // 0 = no return, 1 = return only read, 2 = return all
        if (type >= 0 && type <= 2) {
            writeAddress(id, 0x10, (byte) type);

            String responseType = "";
            switch (type) {
                case 0:
                    responseType = "No response";
                    break;
                case 1:
                    responseType = "Read only";
                    break;
                case 2:
                    responseType = "All";
                    break;
            }

            if (D) { System.out.println("ResponseType: " + responseType); }
        }
    }

    public void setTorqueEnable(boolean enable) {
        if (D) { System.out.println("EnableTorque: " + (enable ? "True" : "False")); }

        writeAddress(id, 0x18, enable ? 1 : 0);
    }

    public void setMinAngle(int angle) {
        writeAddress(id, 0x06, angle % 256, angle / 256);
    }

    public void setMaxAngle(int angle) {
        writeAddress(id, 0x08, angle % 256, angle / 256);
    }

    public void setContinousRotation(int enable) {
        if (enable == 0) {
            setMinAngle(1);
            setMaxAngle(mMaxPosition);
        } else {
            setMinAngle(0);
            setMaxAngle(0);
        }
    }

    public void setBaudRate(int newBaudRate) {
        writeAddress(id, 0x04, convertBaudRate(newBaudRate));
    }

    // converts BaudRate from integer to byte
    private byte convertBaudRate(int rate) {

        if (motorType == DynamixelMotorType.XL_320) {
            switch (rate) {
                case 9600:
                    return 0;
                case 57600:
                    return 1;
                case 115200:
                    return 2;
                case 1000000:
                    return 3;
                default:
                    return 3;
            }
        } else {
            switch (rate) {
                case 9600:
                    return (byte) 207;
                case 19200:
                    return 103;
                case 38400:
                    return 51;
                case 57600:
                    return 34;
                case 115200:
                    return 16;
                case 200000:
                    return 9;
                case 250000:
                    return 7;
                case 400000:
                    return 4;
                case 500000:
                    return 3;
                case 1000000:
                    return 1;
                default:
                    return 1;
            }
        }
    }

    public void setRotationMode(int rotationMode) {
        if (isRotationModeSupported(motorType, rotationMode)) {
            switch (rotationMode) {
                case DynamixelMotorType.JOINT_MODE:
                    writeAddress(id, 0x1E, new byte[] {(byte) (2047 % 256), (byte) (2047 / 256),
                            (byte) (2047 % 256), (byte) (2047 / 256)});
                    mRotationMode = rotationMode;
                    break;
                case DynamixelMotorType.WHEEL_MODE:
                    writeAddress(id, 0x1E, new byte[] {(byte) 0, (byte) 0, (byte) 0, (byte) 0});
                    mRotationMode = rotationMode;
                    break;
                case DynamixelMotorType.MULTI_TURN_MODE:
                    writeAddress(id, 0x1E, new byte[] {(byte) (4095 % 256), (byte) (4095 / 256),
                            (byte) (4095 % 256), (byte) (4095 / 256)});
                    mRotationMode = rotationMode;
                    break;
            }
        }
    }

    public boolean isRotationModeSupported(int motorType, int rotationMode) {
        if (rotationMode == DynamixelMotorType.JOINT_MODE || rotationMode == DynamixelMotorType.WHEEL_MODE) {
            switch (motorType) {
                case DynamixelMotorType.XL_320:
                case DynamixelMotorType.AX_12W:
                case DynamixelMotorType.AX_12:
                case DynamixelMotorType.AX_18:
                case DynamixelMotorType.MX_12:
                case DynamixelMotorType.MX_28:
                case DynamixelMotorType.MX_64:
                case DynamixelMotorType.MX_106:
                    return true;
            }
        } else if (rotationMode == DynamixelMotorType.MULTI_TURN_MODE) {
            switch (motorType) {
                case DynamixelMotorType.XL_320:
                case DynamixelMotorType.AX_12:
                case DynamixelMotorType.AX_18:
                    return false;

                case DynamixelMotorType.AX_12W:
                case DynamixelMotorType.MX_12:
                case DynamixelMotorType.MX_28:
                case DynamixelMotorType.MX_64:
                case DynamixelMotorType.MX_106:
                    return true;
            }
        }

        return false;
    }

   /*
	*  GETTERS
	*/

    public int getId() {
        return id;
    }

    public int getMotorType() {
        return motorType;
    }

    private int[] getMotorPerController() {
        // returns array: idx0 = mxCtrl count, idx1 = xlCtrl count
        int[] counter = new int[2];

        for (int[] arr : syncMoveList) {
            if (DynamixelMotorType.getMotorType(arr[0]) == DynamixelMotorType.MX_28) {
                counter[0]++;
            } else if (DynamixelMotorType.getMotorType(arr[0]) == DynamixelMotorType.XL_320) {
                counter[1]++;
            }
        }

        return counter;
    }

    private int getEncoderFromCoefficient(double coefficient) {
        return (int) ((1 + coefficient) * getMotorMaxEncoder() / 2);
    }

    private int getSpeedFromCoeficient(double coefficient, double time) {

        time *= getMotorMaxEncoder();
        time /= 1000000;
        double encodePerSec = coefficient * getMotorMaxEncoder() / time;
        encodePerSec = Math.abs(encodePerSec);
        return (int) (encodePerSec / encoderPerSecPerUnit);
    }

    private int getSpeedFromEncoder(int encoderDistance, double time) {

        time *= getMotorMaxEncoder();
        time /= 1000000;
        double encodePerSec = encoderDistance / time;
        encodePerSec = Math.abs(encodePerSec);
        return (int) (encodePerSec / encoderPerSecPerUnit);
    }

    private int getMotorMaxEncoder() {
        return getMotorMaxPosition();
    }

    private int getZeroPosition() {

        switch (motorType) {
            case DynamixelMotorType.XL_320:
            case DynamixelMotorType.AX_12W:
            case DynamixelMotorType.AX_12:
            case DynamixelMotorType.AX_18:
                return 511;

            case DynamixelMotorType.MX_12:
            case DynamixelMotorType.MX_28:
            case DynamixelMotorType.MX_64:
            case DynamixelMotorType.MX_106:
                return 2047;

            default:
                return 511;
        }
    }

    private int getMotorMaxAngle() {

        switch (motorType) {
            case DynamixelMotorType.XL_320:
            case DynamixelMotorType.AX_12W:
            case DynamixelMotorType.AX_12:
            case DynamixelMotorType.AX_18:
                return 300;

            case DynamixelMotorType.MX_12:
            case DynamixelMotorType.MX_28:
            case DynamixelMotorType.MX_64:
            case DynamixelMotorType.MX_106:
                return 360;

            default:
                return 300;
        }
    }

    private int getMotorMaxPosition() {

        switch (motorType) {
            case DynamixelMotorType.XL_320:
            case DynamixelMotorType.AX_12W:
            case DynamixelMotorType.AX_12:
            case DynamixelMotorType.AX_18:
                return 1024;

            case DynamixelMotorType.MX_12:
            case DynamixelMotorType.MX_28:
            case DynamixelMotorType.MX_64:
            case DynamixelMotorType.MX_106:
                return 4096;

            default:
                return 1024;
        }
    }

    public int readPosition() {

        byte[] data = readAddress(id, 0x24, 2);
        if (data != null) {
            if (data.length != 2) {
                return -1;
            }

            return data[0] + 256 * data[1];
        }

        return -1;
    }

    @Override
    public String toString() {
        return "DynamixelMotor{" +
                "id=" + id +
                ", motorType=" + motorType +
                ", mMaxPosition=" + mMaxPosition +
                '}';
    }
}
