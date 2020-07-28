import java.io.*;
import java.util.*;
import java.net.Socket;

public class Client {

    public static void main(String[] args){
        Socket socket = null;
        try {
            System.out.println("Подключение...");
            socket = new Socket("localhost", 8899);
            System.out.println("Передача объекта...");

            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            ArrayList<ClassToTransfer> arrayList = new ArrayList<>();
            arrayList.add(new ClassToTransfer("Some text 1"));
            arrayList.add(new ClassToTransfer("Some text 2"));
            arrayList.add(new ClassToTransfer("Some text 3"));
            objectOutputStream.writeObject(arrayList);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}