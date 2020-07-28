import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    public static void main(String[] args){
        ServerSocket server = null;
        Socket socket = null;
        try {
            server = new ServerSocket(8899);
            System.out.println("Сервер запущен!");
            socket = server.accept();
            System.out.println("Клиент подключился");

            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            System.out.println("Прием объекта...");
            ArrayList<ClassToTransfer> arrayList = (ArrayList<ClassToTransfer>)objectInputStream.readObject();

            for (ClassToTransfer o: arrayList){
                System.out.println(o.getSomeTextBox());
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (server != null) server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}