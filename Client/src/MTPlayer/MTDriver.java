package MTPlayer;

import javax.swing.JFrame;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class MTDriver {
	public static void main(String[] args)  throws IOException {
		Scanner sc = new Scanner(System.in);
		System.out.println("What is your name?: ");
		String name = sc.nextLine();
		MTClient player = new MTClient(name);
	}
}
