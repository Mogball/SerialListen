package com.mogball.serial;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;

public class SerialListener implements SerialPortEventListener, AutoCloseable {

    private static final String[] PORT_NAMES = {
            "/dev/tty.usbmodem1411",
            "/dev/ttyUSB0",
            "COM35"
    };

    private static final int SERIAL_READ_TIMEOUT = 2000;
    private static final int SERIAL_BAUD_RATE = 9600;

    private SerialPort serialPort;
    private BufferedReader input;
    private OutputStream output;

    public boolean begin() {
        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            for (String portName : PORT_NAMES) {
                if (portName.equals(currPortId.getName())) {
                    portId = currPortId;
                    break;
                }
            }
        }
        if (portId == null) {
            Log.info("Failed to find serial ports");
            return false;
        }
        try {
            serialPort = (SerialPort) portId.open("SerialListen", SERIAL_READ_TIMEOUT);
            serialPort.setSerialPortParams(
                    SERIAL_BAUD_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE
            );
            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            output = serialPort.getOutputStream();
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
            return true;
        } catch (Exception e) {
            Log.error(e);
            return false;
        }
    }

    public void close() throws Exception {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    public void serialEvent(SerialPortEvent evt) {
        if (evt.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                if (input.ready()) {
                    String result = input.readLine();
                    Log.info(result);
                }
            } catch (IOException e) {
                Log.error(e);
            }
        }
    }
}
