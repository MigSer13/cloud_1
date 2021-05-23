import Clent.Client;
import Server.Server;

import java.io.IOException;

public class StartServer {

    public static void main(String[] args)
    {
        try {
            new Server();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
