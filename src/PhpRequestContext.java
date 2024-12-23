
// PhpRequestContext.java
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

public class PhpRequestContext implements HttpHandler {
    private final PhpHandler phpHandler;
    
    public PhpRequestContext(String phpPath, String documentRoot) {
        this.phpHandler = new PhpHandler(phpPath, documentRoot);
    }
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        phpHandler.handleRequest(exchange);
    }
}