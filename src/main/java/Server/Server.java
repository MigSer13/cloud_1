package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final static int PORT = 8188;
    ServerSocket serverSocket = null;
    Socket socket = null;

    public Server() throws IOException
    {
        serverSocket = new ServerSocket(PORT);
        System.out.println("Сервер запущен");
        try {
            while (true) {
                socket = serverSocket.accept();
                Thread.sleep(2000);
                new Thread(new Handler(socket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
