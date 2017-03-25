package MT;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MTServer {
	private ArrayList<ArrayList<String>> instructionSet = new ArrayList<ArrayList<String>>();
	private ArrayList<ArrayList<String>> instructions = new ArrayList<ArrayList<String>>();
	ArrayList<InetAddress> IPAddresses = new ArrayList<InetAddress>();
	ArrayList<Integer> ports = new ArrayList<Integer>();
	private DatagramSocket serverSocket;
	private byte[] receiveData;
	private byte[] sendData;
	private DatagramPacket receivePacket, sendPacket;
	private InetAddress IPAddress;
	private int instructionSetNum, instructionNum, finalInstructionNum, userInput, counter, port;
	private boolean instructionSetFlag, gameFlag, ongoingGameFlag, profileFlag;
	
	public MTServer() throws IOException {
		counter = 0;
		profileFlag = false;
		instructionSetFlag= false;
		gameFlag = false;
		ongoingGameFlag = false;
		refreshData();
		initInstructions();
		initServerSocket();		
		run();
	}
	
	private String getNewInstructions() {
		instructionSetNum = (int) (Math.random() * instructions.size());
		instructionNum = (int) (Math.random() * instructions.get(instructionSetNum).size());
		finalInstructionNum = instructionSetNum * 10 + instructionNum;
		return fetchInstructions(instructionSetNum, instructionNum);
	}
	
	private String fetchInstructions(int setNum, int num) {
		//if instructionNum % 10 == ganito, say ganito
		return "Press the " + instructionSet.get(setNum).get(num) + " button";
	}
	
	private void sendInstructions() throws IOException {
		ArrayList<String> temp = instructionSet.get(counter);
		String response = Integer.toString(counter);
		sendData = response.getBytes();                   
		sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
		serverSocket.send(sendPacket);
		for (String s: temp) {
			sendData = s.getBytes();                   
			sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			serverSocket.send(sendPacket);
		}
	}
	
	private void requestProfiles() throws IOException {
		broadcast("request profile");
		//byte[] data = recievePacket.getData();
		//ByteArrayInputStream in = new ByteArrayInputStream(data);
		//ObjectInputStream is = new ObjectInputStream(in);
		//fileEvent = (FileEvent) is.readObject();
		//if (fileEvent.getStatus().equalsIgnoreCase("Error")) {
		//	System.out.println("Some issue happened while packing the data @ client side");
		//receive the images and names
		//store them
		
	}
	
	private void broadcast(String response) throws IOException {
		for (InetAddress i: IPAddresses) {
			int index = IPAddresses.indexOf(i);
			sendData = response.getBytes();                   
			sendPacket = new DatagramPacket(sendData, sendData.length, i, ports.get(index));
			serverSocket.send(sendPacket);
		}
	}
	
	private void sendData() throws IOException {
		String response = "";
		if (ongoingGameFlag) {
			if (userInput == finalInstructionNum)
				broadcast(getNewInstructions());
			else
				broadcast(fetchInstructions(instructionSetNum, instructionNum));
		} else if (gameFlag) {
			IPAddresses.add(IPAddress);
			ports.add(port);
			if (counter >= 2) {
				broadcast("OK");
				ongoingGameFlag = true;
			} else {
				broadcast("NOT ENOUGH PLAYERS");
			}
			gameFlag = false;
		} else if (instructionSetFlag) {
			counter++;
			instructionSetFlag = false;
			sendInstructions();
			response = "OK";
		} else if (profileFlag) {
			requestProfiles();
		}
		
		sendData = response.getBytes();                   
		sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
		serverSocket.send(sendPacket);
	}
	
	private void getUserData() {
		IPAddress = receivePacket.getAddress();                   
		port = receivePacket.getPort();
	}
	
	private void addInstructionSet() {
		instructions.add(instructionSet.get(counter));
		instructionSetFlag = true;
	}
	
	private void recieveData() throws IOException {
		serverSocket.receive(receivePacket);
		String sentence = new String(receivePacket.getData()).trim();
		if (sentence.contains("request instruction set")) {
			addInstructionSet();
		} else if (sentence.contains("request profile")) {
			profileFlag = true;
		} else if (sentence.contains("request game")) {
			gameFlag = true;
		} else if (sentence != null) {
			userInput = Integer.parseInt(sentence);
		} else {
			
		}
	}
	
	private void refreshData() {
		sendData = new byte[1024];       
		receiveData = new byte[1024];
		receivePacket = new DatagramPacket(receiveData, receiveData.length);
	}
	
	private void initServerSocket() throws SocketException {
		serverSocket = new DatagramSocket(9876);
	}
	
	private void initInstructions() throws FileNotFoundException {
		BufferedReader br = new BufferedReader(new FileReader("src/MT/Instructions.txt"));
		int iSet = 0, i = -1;
		ArrayList<String> ins = new ArrayList<String>();
		try {
			String x;
			while((x = br.readLine()) != null) {
				i++;
				if (i % 5 == 0) {
					instructionSet.add(ins);
					iSet++;
					ins = new ArrayList<String>();
				}
				ins.add(x);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		instructionSet.add(ins);
	}
	
	private void run() throws IOException {
		while(true)                
		{
			refreshData();
			recieveData();
			getUserData();
			sendData();		          
		}  
	}
}
