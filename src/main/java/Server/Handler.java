package Server;

import java.io.*;
import java.net.Socket;

public class Handler implements Runnable {
    private Socket socket = null;
    private DataInputStream is = null;
    private DataOutputStream os = null;
    private InputStream inputStream = null;
    private FileOutputStream fileOutputStream = null;
    private BufferedOutputStream bufOS = null;
    private String filesPackage = "E:/Java/Files_copy/";

    public Handler(Socket socket)
    {
        this.socket = socket;
    }

    @Override
    public void run()
    {
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
            while (true) {
                String answer = is.readUTF();
                if (answer.equalsIgnoreCase("bytes")) {
                    //inputStream = socket.getInputStream();
                    fileOutputStream = new FileOutputStream(filesPackage + fileName);
                    bufOS = new BufferedOutputStream(fileOutputStream);
                    byte[] bytes = new byte[8192];
                    while (true) {
                        int count = 0;
                        while ((count = is.read(bytes)) != -1) {
                            bufOS.write(bytes, 0, count);
                        }
                        bufOS.flush();
                        bufOS.close();
                        inputStream.close();
                        fileOutputStream.close();
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
