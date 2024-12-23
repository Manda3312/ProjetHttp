import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class StaticResourceHandler {
    private final String rootDirectory;
    private final MimeTypeMapper mimeMapper;
    
    public StaticResourceHandler(String rootDirectory) {
        this.rootDirectory = rootDirectory;
        this.mimeMapper = new MimeTypeMapper();
    }
    
    public void handleRequest(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String requestPath = exchange.getRequestURI().getPath();
        System.out.println("Requête reçue : Méthode = " + requestMethod + ", URL = " + requestPath); // Log de la requête

        Path filePath = Paths.get(rootDirectory, requestPath).normalize();
        
        // Vérification de sécurité pour éviter le path traversal
        if (!filePath.startsWith(Paths.get(rootDirectory))) {
            sendError(exchange, 403, "Accès interdit");
            return;
        }
        
        if (!Files.exists(filePath)) {
            sendError(exchange, 404, "Fichier non trouvé");
            return;
        }
        
        // Vérification de la mise en cache
        String ifModifiedSince = exchange.getRequestHeaders().getFirst("If-Modified-Since");
        if (ifModifiedSince != null && !hasBeenModified(filePath, ifModifiedSince)) {
            send304Response(exchange);
            return;
        }
        
        serveFile(exchange, filePath);
    }
    
    private void serveFile(HttpExchange exchange, Path filePath) throws IOException {
        // Préparation des headers
        Headers headers = exchange.getResponseHeaders();
        String contentType = mimeMapper.getMimeType(filePath);
        headers.set("Content-Type", contentType);
        
        // Headers de cache
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        headers.set("Last-Modified", dateFormat.format(new Date(Files.getLastModifiedTime(filePath).toMillis())));
        headers.set("Cache-Control", "public, max-age=86400"); // Cache d'un jour
        
        // Sécurité
        headers.set("X-Content-Type-Options", "nosniff");
        headers.set("X-Frame-Options", "SAMEORIGIN");
        headers.set("X-XSS-Protection", "1; mode=block");
        
        // Lecture du fichier
        byte[] fileContent = Files.readAllBytes(filePath);
        headers.set("Content-Length", String.valueOf(fileContent.length));
        
        if (isCompressible(contentType)) {
            String acceptEncoding = exchange.getRequestHeaders().getFirst("Accept-Encoding");
            if (acceptEncoding != null && acceptEncoding.contains("gzip")) {
                fileContent = compress(fileContent);
                headers.set("Content-Encoding", "gzip");
            }
        }
        
        exchange.sendResponseHeaders(200, fileContent.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(fileContent);
        }

        System.out.println("Réponse envoyée : Code de statut 200 OK"); // Log de la réponse
    }
    
    private boolean hasBeenModified(Path filePath, String ifModifiedSince) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date ifModifiedSinceDate = dateFormat.parse(ifModifiedSince);
            long lastModified = Files.getLastModifiedTime(filePath).toMillis();
            return lastModified > ifModifiedSinceDate.getTime();
        } catch (Exception e) {
            return true;
        }
    }
    
    private void send304Response(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(304, -1);
        exchange.close();
    }
    
    private void sendError(HttpExchange exchange, int code, String message) throws IOException {
        byte[] messageBytes = message.getBytes();
        exchange.sendResponseHeaders(code, messageBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(messageBytes);
        }
    }
    
    private boolean isCompressible(String contentType) {
        return contentType != null && (
            contentType.startsWith("text/") ||
            contentType.equals("application/javascript") ||
            contentType.equals("application/json") ||
            contentType.equals("application/xml") ||
            contentType.equals("application/x-yaml")
        );
    }
    
    private byte[] compress(byte[] data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (java.util.zip.GZIPOutputStream gzos = new java.util.zip.GZIPOutputStream(baos)) {
            gzos.write(data);
        }
        return baos.toByteArray();
    }
}
