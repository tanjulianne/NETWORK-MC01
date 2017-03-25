package MTPlayer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import javafx.scene.layout.Border;

public class MTClient {
	private JFrame mainFrame, profileFrame, gameFrame, waitingFrame;
	private JScrollPane profilePane;
	private JLabel headerLabel, connectingLabel;
	private JTextArea instructionArea;
	private JPanel controlPanel, startPanel;
	private BufferedReader inFromUser;
	private DatagramSocket clientSocket;
	private InetAddress IPAddress;
	private int instructionNum, input;
	private DatagramPacket sendPacket, receivePacket;
	private byte[] sendData, receiveData;
	private ArrayList<String> instructions;
	private String path;
	
	public MTClient() throws UnknownHostException, IOException {
		initSocket();
		instructions = new ArrayList<String>();
		prepareGUI();
	}
	
	private void initSocket() throws SocketException, UnknownHostException {
		inFromUser = new BufferedReader(new InputStreamReader(System.in));       
		clientSocket = new DatagramSocket();       
		IPAddress = InetAddress.getByName("localhost");  
	}
	
	private void runProfileGUI() {
		profileFrame = new JFrame("Profile");
		profileFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		profileFrame.setSize(1000, 500);
		profileFrame.setResizable(false);
		profileFrame.getContentPane().setBackground(Color.BLACK);
		
		JPanel profilePanels = new JPanel();
		profilePanels.setLayout(new BoxLayout(profilePanels, BoxLayout.Y_AXIS));
		//request profiles
		ArrayList<JLabel> names = new ArrayList<JLabel>();
		names.add(new JLabel("Ashley"));
		names.add(new JLabel("Martin"));
		for (JLabel l: names) {
			l.setFont(new Font("Helvetica", Font.BOLD, 50));
			l.setAlignmentX(Component.CENTER_ALIGNMENT);
			l.setBorder(new EmptyBorder(20, 20, 20, 20));
			profilePanels.add(l, BorderLayout.CENTER);
		}
		profilePanels.setBackground(Color.WHITE);
		profilePane = new JScrollPane(profilePanels);
		profilePane.setPreferredSize(new Dimension(1000, 500));
		profilePane.setOpaque(true);
		profilePane.setBorder(new MatteBorder(0, 100, 0, 100, Color.BLACK));
		
		profileFrame.add(profilePane, BorderLayout.CENTER);
		profileFrame.pack();
		profileFrame.setVisible(true);
	}
	
	private void runGameGUI() {
		gameFrame = new JFrame("Game");
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setSize(1000, 500);
		gameFrame.setResizable(false);
		instructionArea = new JTextArea();
		gameFrame.add(instructionArea, BorderLayout.NORTH);
		
		JPanel gamePanel = new JPanel();
		gamePanel.setBackground(Color.BLACK);
		gamePanel.setLayout(new FlowLayout());
		
		gameFrame.add(gamePanel, BorderLayout.SOUTH);
		
		JButton button1, button2, button3, button4, button5;
		button1 = new JButton(instructions.get(0));
		button2 = new JButton(instructions.get(1));
		button3 = new JButton(instructions.get(2));
		button4 = new JButton(instructions.get(3));
		button5 = new JButton(instructions.get(4));
		
		gamePanel.add(button1);
		gamePanel.add(button2);
		gamePanel.add(button3);
		gamePanel.add(button4);
		gamePanel.add(button5);
		
		gameFrame.setVisible(true);
	}
	
	private void runRequestGameGUI() {
		waitingFrame = new JFrame("Lobby");
		waitingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		waitingFrame.setSize(1000, 500);
		waitingFrame.setResizable(false);
		waitingFrame.setLayout(new GridLayout(2,2));
		waitingFrame.getContentPane().setBackground(Color.BLACK);
		
		connectingLabel = new JLabel("Waiting for more players...", JLabel.CENTER);
		connectingLabel.setForeground(Color.WHITE);
		connectingLabel.setFont(new Font("Helvetica", Font.BOLD, 60));
		connectingLabel.setSize(350, 100);
		
		startPanel = new JPanel();
		startPanel.setBackground(Color.BLACK);
		startPanel.setLayout(new FlowLayout());
		
		waitingFrame.add(connectingLabel);
		waitingFrame.add(startPanel);
		
		JButton startGameButton = new JButton("Start Game");
		startPanel.add(startGameButton);
		startGameButton.setPreferredSize(new Dimension(400, 100));
		startGameButton.setForeground(Color.BLACK);
		startGameButton.setBackground(Color.WHITE);
		startGameButton.setBorder(null);
		startGameButton.setActionCommand("Game");
		startGameButton.setFont(new Font("Helvetica", Font.PLAIN, 40));
		startGameButton.addActionListener(new ButtonClickListener());

		waitingFrame.setVisible(true);
	}
	
	private void prepareGUI() throws IOException {
		mainFrame = new JFrame("SpaceTeam");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(1000, 500);
		mainFrame.setLayout(new GridLayout(2,2));
		mainFrame.setResizable(false);
		mainFrame.getContentPane().setBackground(Color.BLACK);
		
		headerLabel = new JLabel("SpaceTeam", JLabel.CENTER);
		headerLabel.setForeground(Color.WHITE);
		headerLabel.setFont(new Font("Helvetica", Font.BOLD, 80));
		headerLabel.setSize(350, 100);
		
		controlPanel = new JPanel();
		controlPanel.setBackground(Color.BLACK);
		controlPanel.setLayout(new FlowLayout());
		
		mainFrame.add(headerLabel);
		mainFrame.add(controlPanel);
		
		JButton viewButton = new JButton("View Profile");
		JButton startButton = new JButton("Start Game");
		
		controlPanel.add(viewButton);
		controlPanel.add(startButton);
		
		((FlowLayout)controlPanel.getLayout()).setHgap(30);
		((FlowLayout)controlPanel.getLayout()).setVgap(30);
		
		viewButton.setPreferredSize(new Dimension(400, 100));
		startButton.setPreferredSize(new Dimension(400, 100));
		
		viewButton.setForeground(Color.BLACK);
		startButton.setForeground(Color.BLACK);
		
		viewButton.setBackground(Color.WHITE);
		startButton.setBackground(Color.WHITE);
		
		viewButton.setBorder(null);
		startButton.setBorder(null);
		
		viewButton.setActionCommand("View");
		startButton.setActionCommand("Start");
		
		viewButton.setFont(new Font("Helvetica", Font.PLAIN, 40));
		startButton.setFont(new Font("Helvetica", Font.PLAIN, 40));
		
		viewButton.addActionListener(new ButtonClickListener());
		startButton.addActionListener(new ButtonClickListener());
		
		mainFrame.setVisible(true);
	}
	
	private class ButtonClickListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			
			if (command.equals("View")) {
				runProfileGUI(); 
				try {
					requestProfile();
					refreshData();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else if (command.equals("Start")) {
				runRequestGameGUI();
				try {
					if(instructions.isEmpty()) {
						requestInstructionSet(); 
						refreshData();
						//waitForGame();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else if (command.equals("Game")) {
				try {
					requestGame();
					refreshData();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	private void waitForGame() throws IOException {
		//server throws somebody engaged a game or something
		//you either reply with yes or no
	}
	
	private void requestGame() throws IOException {
		refreshData();
		sendData("request game");   
		String response = recieveData();
		response = recieveData();
		if (response.contains("OK")) {
			//initiate
			//server sends OK to all players in lobby which forces the game to start for them
			runGameGUI();
			//runGame();
		} else {
			System.out.println("Not enough players");
			//pop up not enough players
		}
	}
	
	private void requestProfile() throws IOException {
		
	}
	
	private void requestInstructionSet() throws IOException {
		refreshData();
		sendData("request instruction set");   
		instructionNum = Integer.parseInt(recieveData());
		for (int i = 0; i < 5; i++)
			instructions.add(recieveData());
	}
	
	private void setText(String text) {
		instructionArea.setText(text);
	}
	
	private void runGame() throws IOException {
		
		/*while(true) {
			refreshData();
			sendData(Integer.toString(instructionNum * 10 + input));
			String serverResponse = recieveData();
			setText(serverResponse);
		}*/
	}
	
	private void sendImage() throws IOException {
		FileOutputStream FOS = new FileOutputStream(path);
		FOS.write(sendData);
		sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);       
		clientSocket.send(sendPacket);  
		FOS.close();
	}
	
	private void sendData(String sentence) throws IOException {
		sendData = sentence.getBytes();       
		sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);       
		clientSocket.send(sendPacket);  
	}
	
	private String recieveData() throws IOException {
		receivePacket = new DatagramPacket(receiveData, receiveData.length);       
		clientSocket.receive(receivePacket);       
		String serverResponse = new String(receivePacket.getData());
		return serverResponse.trim();
	}
	
	private void refreshData() {
		sendData = new byte[1024];       
		receiveData = new byte[1024]; 
	}
}
