package Server;

import java.io.*;
import java.net.Socket;

public class Handler {
    private Socket socket = null;
    private DataInputStream is = null;
    private DataOutputStream os = null;
    private InputStream inputStream = null;
    private FileOutputStream fileOutputStream = null;
    private BufferedOutputStream bufOS = null;
    private String filesPackage = "Files/";

    public Handler(Socket socket)
    {
        try {
            this.socket = socket;
            is = new DataInputStream(socket.getInputStream());
            os = new DataOutputStream(socket.getOutputStream());
            new Thread(() -> {
                try {
                    //os.writeUTF("OK");
                    System.out.println("Клиент подключился");
                    while (true) {
                        String command = is.readUTF();
                        if (command.equalsIgnoreCase("download")) {
                            download();
                        } else if (command.isEmpty()) {
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
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void download()
    {
        try {
            //запрос имени
            os.writeUTF("fileName?");
            String fileName = null;
            while (true) {
                fileName = is.readUTF();
                //должна быть проверка на тип файла
                break;
            }
            //запрос файла
            os.writeUTF("bytes?");
            os.flush();
            inputStream = socket.getInputStream();
            fileOutputStream = new FileOutputStream(filesPackage + fileName);
            bufOS = new BufferedOutputStream(fileOutputStream);
            while (true) {
                byte[] bytes = new byte[8192];
                int count = 0;
                while ((count = inputStream.read(bytes)) != -1) {
                    bufOS.write(bytes, 0, count);
                }
                bufOS.flush();
                bufOS.close();
                inputStream.close();
                fileOutputStream.close();
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
