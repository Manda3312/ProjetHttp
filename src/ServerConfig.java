
// ServerConfig.java
public class ServerConfig {
    private int port;
    private String htdocs;
    private boolean phpEnabled;
    private PhpConfig phpConfig;
    private String serverName;
    private int maxThreads;
    private LoggingConfig logging;
    
    // Getters et Setters
    public int getPort() {
        return port;
    }
    
    public void setPort(int port) {
        this.port = port;
    }
    
    public String getHtdocs() {
        return htdocs;
    }
    
    public void setHtdocs(String htdocs) {
        this.htdocs = htdocs;
    }
    
    public boolean isPhpEnabled() {
        return phpEnabled;
    }
    
    public void setPhpEnabled(boolean phpEnabled) {
        this.phpEnabled = phpEnabled;
    }
    
    public PhpConfig getPhpConfig() {
        return phpConfig;
    }
    
    public void setPhpConfig(PhpConfig phpConfig) {
        this.phpConfig = phpConfig;
    }
    
    public String getServerName() {
        return serverName;
    }
    
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
    
    public int getMaxThreads() {
        return maxThreads;
    }
    
    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }
    
    public LoggingConfig getLogging() {
        return logging;
    }
    
    public void setLogging(LoggingConfig logging) {
        this.logging = logging;
    }
}
