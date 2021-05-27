package Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final String IP_address = "localhost";
    private final int PORT = 8188;
    private Socket socket = null;
    private DataInputStream is = null;
    private DataOutputStream os = null;
    private Scanner sc = null;
    private InputStream inputStream = null;
    private BufferedInputStream bufIS = null;
    private BufferedOutputStream bufOS = null;

    public Client()
    {
        //new Thread(()->{
        try {
            socket = new Socket(IP_address, PORT);
            is = new DataInputStream(socket.getInputStream());
            os = new DataOutputStream(socket.getOutputStream());
            String answerServer = "";
            answerServer = is.readUTF();
            sc = new Scanner(System.in);
            while (true) {
                if (answerServer.equalsIgnoreCase("OK")) {
                    System.out.println("Подключились к серверу:");
                    answerServer = "";
                }
                if (answerServer == "" || answerServer.startsWith("вы отправили")) {
                    System.out.println("Введите команду:");
                    String command = sc.nextLine();
                    os.writeUTF(command);
                    os.flush();
                    answerServer = is.readUTF();
                }

                if (answerServer.equalsIgnoreCase("fileName?")) {
                    System.out.println("напишите имя нового файла в хранилище (с расширением) ");
                    String fileName = sc.nextLine();
                    os.writeUTF(fileName);
                    os.flush();
                    answerServer = is.readUTF();
                }
                if (answerServer.equalsIgnoreCase("bytes?")) {
                    System.out.println("укажите путь к вашему файлу");
                    String path = sc.nextLine();
//                    os.writeUTF("bytes");
//                    os.flush();

                    inputStream = new FileInputStream(path);
//                    bufIS = new BufferedInputStream(fileIS);
//                    bufOS = new BufferedOutputStream(socket.getOutputStream());
                    byte[] bytes = new byte[8192];
                    int count = 0;
                    while ((count = inputStream.read(bytes)) != -1) {
                        os.write(bytes, 0, count);
                    }
                    os.flush();
                    inputStream.close();
                    //bufIS.close();
                    //bufOS.close();
                    //break;
                    answerServer = "";
                }
            }

    } catch(IOException e){
        e.printStackTrace();
    } finally {
        try {
            socket.close();
            os.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //}).start();
}

}
