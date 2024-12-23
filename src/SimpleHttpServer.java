import java.io.*;
import java.net.*;

public class SimpleHttpServer {
    private final int port;

    public SimpleHttpServer(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Serveur démarré sur le port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connexion reçue de " + clientSocket.getInetAddress());

                new Thread(new RequestHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du démarrage du serveur : " + e.getMessage());
        }
    }

    static class RequestHandler implements Runnable {
        private final Socket clientSocket;

        public RequestHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

                // Lire la requête HTTP
                String requestLine = reader.readLine();
                System.out.println("Requête reçue : " + requestLine);

                // Envoyer la réponse HTTP
                writer.println("HTTP/1.1 200 OK");
                writer.println("Content-Type: text/html");
                writer.println();
                writer.println("<html><body><h1>Bienvenue sur le serveur Java!</h1></body></html>");
            } catch (IOException e) {
                System.err.println("Erreur lors du traitement de la requête : " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
