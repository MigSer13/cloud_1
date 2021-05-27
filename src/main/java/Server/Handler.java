package Server;

import java.io.*;
import java.net.Socket;

public class Handler implements Runnable {
    private Socket socket = null;
    private DataInputStream is = null;
    private DataOutputStream os = null;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;
    private BufferedOutputStream bufOS = null;
    private String filesPackage = "./src/main/java/Server/ServerFiles/";

    public Handler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            is = new DataInputStream(socket.getInputStream());
            os = new DataOutputStream(socket.getOutputStream());
            try {
                os.writeUTF("OK");
                os.flush();
                System.out.println("Клиент подключился");
                while (true) {
                    String command = is.readUTF();
                    if (command.equalsIgnoreCase("download")) {
                        download();
                    } else {
                        os.writeUTF("вы отправили: " + command);
                        os.flush();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                    is.close();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void download() {
        try {
            //запрос имени
            os.writeUTF("fileName?");
            String fileName = null;
            fileName = is.readUTF();
            //должна быть проверка на тип файла

            //запрос файла
            os.writeUTF("bytes?");
            os.flush();
            byte[] bytes = new byte[8192];
            int numberBytes = 0;
            outputStream = new FileOutputStream(filesPackage + fileName);

            while ((numberBytes = is.read(bytes)) != -1) {
                byte[] bytesWrite = new byte[numberBytes];
                outputStream.write(bytesWrite, 0 , numberBytes);
            }
            outputStream.flush();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
