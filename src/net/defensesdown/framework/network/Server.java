package net.defensesdown.framework.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import net.defensesdown.framework.network.messages.Message;
import net.defensesdown.player.Unit;

/**
 * @author Riseremi
 */
public class Server {

    //
    private static final ArrayList<Unit> units = new ArrayList<>();
    public static String SERVER_IP;
    private static Server instance;
    private ServerSocket serverSocket;
    //
    private ArrayList<Connection> clients = new ArrayList<>();
    //
    private int i;

    private Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);

        Thread t = new Thread() {
            @Override
            public void run() {
                System.out.println("Server started.");
                while (true) {
                    try {
                        Socket socket = serverSocket.accept();
                        final Connection connection = new Connection(socket, i++);
                        clients.add(connection);
                        System.out.println("Client received.");
                        //connection.send(connection.getId());
                    } catch (IOException ex) {
                    }
                }
            }
        };
        t.start();
    }

    public static Server getInstance() {
        if (instance == null) {
            try {
                instance = new Server(7777);
                return instance;
            } catch (IOException ex) {
            }
        }
        return instance;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public void sendToAll(Object message) throws IOException {
        for (Connection connection : clients) {
            connection.send(message);
        }
    }

    public void sendToAllExcludingOne(Object message, int id) throws IOException {
        for (Connection connection : clients) {
            if (connection.getId() != id) {
                connection.send(message);
            }
        }
    }

    public void sendToOne(Object message, int index) throws IOException {
        clients.get(index).send(message);
    }

    public boolean isAllPlayersConnected() {
        return clients.size() == 2;
    }

    static class Connection {

        private final int id;
        private ObjectInputStream in;
        private ObjectOutputStream out;

        public Connection(Socket socket, final int id) throws IOException {
            this.id = id;
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());

            Thread t = new Thread() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Message s = (Message) in.readObject();
                            System.out.println("SERVER RECEIVED: " + s.getType().name());
                            Protocol.processOnServer(s, id);
                        } catch (IOException | ClassNotFoundException ex) {
                        }
                    }
                }
            };
            t.start();
        }

        public int getId() {
            return id;
        }

        public void send(Object message) throws IOException {
            out.writeObject(message);
        }

    }

}
