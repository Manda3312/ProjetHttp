// Exemple d'utilisation dans le HttpServer
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;

public class StaticResourcesContext implements HttpHandler {
    private final StaticResourceHandler resourceHandler;
    
    public StaticResourcesContext(String rootDirectory) {
        this.resourceHandler = new StaticResourceHandler(rootDirectory);
    }
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        resourceHandler.handleRequest(exchange);
    }
}