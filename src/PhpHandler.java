import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class PhpHandler {
    private final String phpPath;
    private final String documentRoot;
    
    public PhpHandler(String phpPath, String documentRoot) {
        this.phpPath = phpPath;
        this.documentRoot = documentRoot;
    }
    
    public void handleRequest(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String requestPath = exchange.getRequestURI().getPath();
        Path filePath = Paths.get(documentRoot, requestPath).normalize();
        
        // Vérification de sécurité pour éviter la traversée de répertoires
        if (!filePath.startsWith(Paths.get(documentRoot))) {
            sendError(exchange, 403, "Accès interdit");
            return;
        }
        
        if (!Files.exists(filePath)) {
            sendError(exchange, 404, "Fichier non trouvé");
            return;
        }
        
        // Préparation des variables d'environnement pour PHP
        Map<String, String> env = prepareEnvironmentVariables(exchange);
        
        // Gestion des données POST
        byte[] postData = null;
        if ("POST".equals(requestMethod)) {
            postData = exchange.getRequestBody().readAllBytes();
            env.put("CONTENT_LENGTH", String.valueOf(postData.length));
            env.put("CONTENT_TYPE", exchange.getRequestHeaders().getFirst("Content-Type"));
        }
        
        // Exécution du script PHP
        executePhp(exchange, filePath, env, postData);
    }
    
    private Map<String, String> prepareEnvironmentVariables(HttpExchange exchange) {
        Map<String, String> env = new HashMap<>();
        
        // Variables de base
        env.put("DOCUMENT_ROOT", documentRoot);
        env.put("SCRIPT_FILENAME", Paths.get(documentRoot, exchange.getRequestURI().getPath()).toString());
        env.put("REQUEST_URI", exchange.getRequestURI().toString());
        env.put("REQUEST_METHOD", exchange.getRequestMethod());
        env.put("QUERY_STRING", exchange.getRequestURI().getQuery() != null ? exchange.getRequestURI().getQuery() : "");
        env.put("SERVER_PROTOCOL", "HTTP/1.1");
        env.put("REMOTE_ADDR", exchange.getRemoteAddress().getAddress().getHostAddress());
        env.put("SERVER_SOFTWARE", "JavaHttpServer/1.0");
        
        // En-têtes HTTP
        Headers headers = exchange.getRequestHeaders();
        headers.forEach((key, values) -> {
            String headerName = "HTTP_" + key.toUpperCase().replace('-', '_');
            env.put(headerName, String.join(", ", values));
        });
        
        // Variables GET (si présentes)
        if (exchange.getRequestURI().getQuery() != null) {
            env.put("QUERY_STRING", exchange.getRequestURI().getQuery());
        }
        
        return env;
    }
    
    private void executePhp(HttpExchange exchange, Path filePath, Map<String, String> env, byte[] postData) 
            throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(phpPath, "-f", filePath.toString());
        processBuilder.environment().putAll(env);
        
        // Démarrage du processus PHP
        Process process = processBuilder.start();
        
        // Envoi des données POST si présentes
        if (postData != null) {
            try (OutputStream processInput = process.getOutputStream()) {
                processInput.write(postData);
            }
        }
        
        // Lecture de la sortie PHP
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            
            // Séparation des headers et du contenu du body
            List<String> responseHeaders = new ArrayList<>();
            StringBuilder content = new StringBuilder();
            String line;
            boolean isBody = false;
            
            while ((line = reader.readLine()) != null) {
                if (!isBody && line.trim().isEmpty()) {
                    isBody = true;
                    continue;
                }
                
                if (isBody) {
                    content.append(line).append("\n");
                } else {
                    responseHeaders.add(line);
                }
            }
            
            // Traitement des headers PHP
            Headers headers = exchange.getResponseHeaders();
            boolean contentTypeSet = false;
            
            for (String header : responseHeaders) {
                int colonPos = header.indexOf(':');
                if (colonPos > 0) {
                    String name = header.substring(0, colonPos).trim();
                    String value = header.substring(colonPos + 1).trim();
                    headers.add(name, value);
                    
                    if (name.equalsIgnoreCase("Content-Type")) {
                        contentTypeSet = true;
                    }
                }
            }
            
            // Content-Type par défaut si non défini
            if (!contentTypeSet) {
                headers.set("Content-Type", "text/html; charset=UTF-8");
            }
            
            // Envoi de la réponse
            byte[] response = content.toString().getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, response.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response);
            }
        }
        
        // Gestion des erreurs PHP
        try (BufferedReader errorReader = new BufferedReader(
                new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8))) {
            StringBuilder errorContent = new StringBuilder();
            String line;
            while ((line = errorReader.readLine()) != null) {
                errorContent.append(line).append("\n");
            }
            
            if (errorContent.length() > 0) {
                System.err.println("Erreur PHP : " + errorContent);
            }
        }
        
        // Attente de la fin du processus PHP
        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Le processus PHP s'est terminé avec le code : " + exitCode);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Exécution PHP interrompue", e);
        }
    }
    
    private void sendError(HttpExchange exchange, int code, String message) throws IOException {
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(code, messageBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(messageBytes);
        }
    }
}
