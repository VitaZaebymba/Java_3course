import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executors;

/**
 * Серверная программа, которая принимает запросы от клиентов на обработку строк с заглавных букв.
 * Когда клиент подключается, то запускается новый поток
 * для его обработки. Получение клиентских данных, их 	  * использование и отправка ответа - все это делается в
 * потоке, что обеспечивает гораздо большую пропускную
 *способность, поскольку одновременно может
 * обрабатываться больше клиентов.
 */
public class CapitalizeServer {

    /**
     * Запускается сервер. Когда клиент подключается,
     * сервер создает новый поток для обслуживания и
     * немедленно возвращается к прослушиванию.
     * Приложение ограничивает количество потоков через
     * пул потоков (в противном случае миллионы клиентов
     * могут привести к исчерпанию ресурсов сервера из-за
     * выделения слишком большого количества потоков).
     */
    public static void main(String[] args) throws Exception {
        try (var listener = new ServerSocket(59898)) {
            System.out.println("Сервер запущен...");
            Message message=new Message();
            Thread sender= new Thread(new Runnable() {
                Message mes=message;
                PrintWriter out;
                @Override   // annotation to override the run method
                public void run() {
                    while(true){
                        try {
                            Thread.sleep(5000);
                            if(mes.getMessage().size()!=0){
                                for(Socket sock  : message.getSockets()){
                                    out = new PrintWriter(sock.getOutputStream());
                                    out.println(mes.getMessage());    // write data stored in msg in the clientSocket
                                    out.flush();   // forces the sending of the data
                                }
                                mes.reset();
                            }
                        } catch (InterruptedException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            sender.start();
            var pool = Executors.newFixedThreadPool(20);
            while (true) {
                pool.execute(new Capitalizer(listener.accept(),message));
            }
        }
    }

    private static class Capitalizer implements Runnable {
        private Socket socket;
        Message message;

        Capitalizer(Socket socket,Message message) {
            this.socket = socket;
            this.message=message;
            message.addSocket(socket);
        }

        @Override
        public void run() {
            System.out.println("Подключение: " + socket);
            final BufferedReader in;
            try {
                in = new BufferedReader (new InputStreamReader(socket.getInputStream()));
                Thread receive= new Thread(new Runnable() {
                    Message mes=message;
                    String msg ;
                    @Override
                    public void run() {
                        try {
                            msg = in.readLine();
                            //tant que le client est connecté
                            while(msg!=null){
                                System.out.println("Client : "+msg);
                                mes.add(msg);
                                msg = in.readLine();
                            }

                            System.out.println("Client déconecté");
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                receive.start();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}



