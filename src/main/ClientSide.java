package main;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientSide {
    static final int PORT = 9100;
    static final String HOST = "localhost";
    
    public static void main(String[] args) throws IOException {
        try (
            Socket socket = new Socket(HOST, PORT);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        ) {
            new UIThread(socket).start();
            String fromServer;
            while ((fromServer = in.readLine()) != null) {
                System.out.println(fromServer);
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + HOST);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Server is offline or not responding.  Please try again later.");
            System.exit(0);
        }
    }    
    private static class UIThread extends Thread{
        Socket socket = null;
        public UIThread(Socket s) {
            socket = s;
        }
        @Override public void run() {
            try (
                Scanner scan = new Scanner(System.in);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            ) {
                while (true) {
                    out.println(scan.nextLine());
                }      
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for the connection to " + HOST + " whilest in UIThread");
                e.printStackTrace();
            }
        }
    }
}