
package tictactoe_2_0_deluxe_reloaded.Networking;

import java.net.*;
import java.io.*;
import java.util.Scanner;
import tictactoe_2_0_deluxe_reloaded.PlayerMove;

public class Network {
    
    //---------------------------------------------------------
    // Class variables
    //---------------------------------------------------------

    private ServerSocket serverSocket = null;
    private Socket clientSocket = null;

    private ObjectInputStream is = null;
    private ObjectOutputStream os = null;

    private static final Scanner in = new Scanner(System.in);

    //---------------------------------------------------------
    // Constructor
    //---------------------------------------------------------
    
    public Network(boolean isServer) {
                
        try {
            if(isServer) {
                System.out.println("Waiting for another player to connect...");
                serverSocket = new ServerSocket(9999);
                clientSocket = serverSocket.accept();
                os = new ObjectOutputStream(clientSocket.getOutputStream());
                is = new ObjectInputStream(clientSocket.getInputStream());
                System.out.println("Player connected. Starting game!");
            } else {
                System.out.println("Input host name to connect:");
                String ip = in.nextLine();
                clientSocket = new Socket(ip, 9999);
                os = new ObjectOutputStream(clientSocket.getOutputStream());
                is = new ObjectInputStream(clientSocket.getInputStream());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    //---------------------------------------------------------
    // Methods
    //---------------------------------------------------------

    public boolean writeToNetworkPlayer(PlayerMove move) {
        
        try {
            this.os.writeObject(move);
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    public PlayerMove readFromNetworkPlayer() throws ClassNotFoundException {
        System.out.println("Waiting for network player move...");

        try {
            PlayerMove move = (PlayerMove) this.is.readObject();
            return move;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    
    public void closeConnection() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
