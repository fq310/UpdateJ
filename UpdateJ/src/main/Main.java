package main;


import file.Updater;
import socket.SocketService;

public class Main {
	public static void main(String[] args) {
		SocketService ss = new SocketService();
		ss.start(new Updater());
	}
}
