package net.defensesdown.framework.network;

import net.defensesdown.framework.network.messages.Message;
import net.defensesdown.player.Unit;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author Riseremi
 */
public class Client {

    private static Client instance;
    private final ArrayList<Unit> players = new ArrayList<>();
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private int id;

    private Client(int port, String ip) throws IOException {
        Socket s = new Socket(ip, port);

        out = new ObjectOutputStream(s.getOutputStream());
        out.flush();
        in = new ObjectInputStream(s.getInputStream());

        Thread t = new Thread() {
            @Override
            public void run() {
//                System.out.println("Client started.");
                while (true) {
                    try {
                        Message message = (Message) in.readObject();
                        System.out.println("CLIENT RECEIVED: " + message.getType().name());
                        Protocol.processOnClient(message);
                    } catch (IOException | ClassNotFoundException ex) {
                    }
                }
            }
        };
        t.start();
    }

    public static Client getInstance() {
        if (instance == null) {
            try {
                instance = new Client(7777, Server.SERVER_IP);
                return instance;
            } catch (IOException ex) {
            }
        }
        return instance;
    }

    public ArrayList<Unit> getPlayers() {
        return players;
    }

    public void send(Object message) throws IOException {
        out.writeObject(message);
    }
}
