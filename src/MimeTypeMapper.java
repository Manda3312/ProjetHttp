
// MimeTypeMapper.java
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class MimeTypeMapper {
    private final Map<String, String> mimeTypes;
    
    public MimeTypeMapper() {
        mimeTypes = new HashMap<>();
        initializeMimeTypes();
    }
    
    private void initializeMimeTypes() {
        // Text
        mimeTypes.put("html", "text/html");
        mimeTypes.put("htm", "text/html");
        mimeTypes.put("css", "text/css");
        mimeTypes.put("js", "application/javascript");
        mimeTypes.put("txt", "text/plain");
        mimeTypes.put("csv", "text/csv");
        
        // Images
        mimeTypes.put("jpg", "image/jpeg");
        mimeTypes.put("jpeg", "image/jpeg");
        mimeTypes.put("png", "image/png");
        mimeTypes.put("gif", "image/gif");
        mimeTypes.put("ico", "image/x-icon");
        mimeTypes.put("svg", "image/svg+xml");
        mimeTypes.put("webp", "image/webp");
        
        // Documents
        mimeTypes.put("pdf", "application/pdf");
        mimeTypes.put("json", "application/json");
        mimeTypes.put("xml", "application/xml");
        
        // Archives
        mimeTypes.put("zip", "application/zip");
        mimeTypes.put("gz", "application/gzip");
        
        // Fonts
        mimeTypes.put("ttf", "font/ttf");
        mimeTypes.put("woff", "font/woff");
        mimeTypes.put("woff2", "font/woff2");
        mimeTypes.put("eot", "application/vnd.ms-fontobject");
        
        // Audio/Video
        mimeTypes.put("mp3", "audio/mpeg");
        mimeTypes.put("wav", "audio/wav");
        mimeTypes.put("mp4", "video/mp4");
        mimeTypes.put("webm", "video/webm");
    }
    
    public String getMimeType(Path filePath) {
        String fileName = filePath.getFileName().toString();
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            String extension = fileName.substring(lastDotIndex + 1).toLowerCase();
            return mimeTypes.getOrDefault(extension, "application/octet-stream");
        }
        return "application/octet-stream";
    }
}
