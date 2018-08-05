package com.kevinisabelle.dmxLive.processes;

import java.io.*;
import java.util.logging.Level;
import javax.bluetooth.*;
import javax.microedition.io.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author kevin
 */
public class BluetoothServer extends Thread {

	private static Logger log = LogManager.getLogger(BluetoothServer.class);
	private String availableText = "test text yeah yeah";
	boolean isRunning = false;
	UUID uuid = new UUID("1101", true);
	final String connectionString = "btspp://localhost:" + uuid + ";name=LyricsServer";
	StreamConnectionNotifier streamConnNotifier = null;

	private static BluetoothServer instance = new BluetoothServer();

	public static void StartBluetoothServer() {

		if (!instance.isRunning) {

			instance.start();
		}
	}

	public static void StopBluetoothServer() {

		if (instance.isRunning) {

			instance.stopBluetooth();

		}
	}

	@Override
	public void run() {

		//open server url
		isRunning = true;
		try {
			streamConnNotifier = (StreamConnectionNotifier) Connector.open(connectionString);
		} catch (IOException ex) {
			log.error(ex);
			return;
		}

		while (isRunning) {
			try {

				
				
				WaitForConnection();

			} catch (Exception ex) {
				log.error(ex);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ex1) {

				}
			}
		}
	}

	public void stopBluetooth() {

		isRunning = false;
		try {
			streamConnNotifier.close();
		} catch (IOException ex) {
			log.error(ex);
		}

	}

	public void WaitForConnection() {

		//Wait for client connection
		log.info("\nServer Started. Waiting for clients to connect...");

		while (isRunning) {

			try {
				StreamConnection connection = streamConnNotifier.acceptAndOpen();

				RemoteDevice dev = RemoteDevice.getRemoteDevice(connection);
				log.info("Remote device address: " + dev.getBluetoothAddress());
				log.info("Remote device name: " + dev.getFriendlyName(true));

				//read string from spp client
				InputStream inStream = connection.openInputStream();

				BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));

				boolean receivingData = true;
				OutputStream outStream = null;

				while (receivingData) {

					String lineRead = "";
					try {
						lineRead = bReader.readLine();
						
						while (bReader.ready()){
							bReader.readLine();
						}
						
					} catch (Exception e) {
						log.error(e);
						receivingData = false;
						break;
					}

					if (lineRead.equals("")) {
						receivingData = false;
					}

					log.info("Received: " + lineRead);

					//send response to spp client
					if (outStream == null) {
						outStream = connection.openOutputStream();
					}

					PrintWriter pWriter = new PrintWriter(new OutputStreamWriter(outStream));
					pWriter.write(getAvailableText()+ "\n");
					pWriter.flush();
					//pWriter.close();
				}

				if (outStream != null) {
					outStream.close();
				}
				connection.close();

			} catch (IOException ex) {
				log.error(ex);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ex1) {

				}
			}
		}

		try {
			streamConnNotifier.close();
		} catch (IOException ex) {
			log.error(ex);
		}

	}

	/**
	 * @return the availableText
	 */
	public String getAvailableText() {
		return availableText;
	}

	/**
	 * @param availableText the availableText to set
	 */
	public void setAvailableText(String availableText) {
		this.availableText = availableText;
	}
	
	public static void SetAvailableText(String text){
		instance.setAvailableText(text);
	}

}
