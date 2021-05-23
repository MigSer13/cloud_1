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
    private FileInputStream fileIS = null;
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
            sc = new Scanner(System.in);
            while (true) {
                if (answerServer.equalsIgnoreCase("OK")) {
                    System.out.println("OK:");
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
                    os.writeUTF("bytes");
                    os.flush();
                    fileIS = new FileInputStream(path);
                    bufIS = new BufferedInputStream(fileIS);
                    bufOS = new BufferedOutputStream(socket.getOutputStream());
                    byte[] bytes = new byte[8192];
                    int count = 0;
                    while ((count = bufIS.read(bytes)) != -1) {
                        bufOS.write(bytes, 0, count);
                    }
                    bufOS.flush();
                    fileIS.close();
                    bufIS.close();
                    bufOS.close();
                    break;
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
