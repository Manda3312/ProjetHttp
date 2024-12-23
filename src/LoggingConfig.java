
// LoggingConfig.java
public class LoggingConfig {
    private String accessLogPath;
    private String errorLogPath;
    private String logLevel;
    private boolean enableAccessLog;
    private boolean enableErrorLog;
    
    public LoggingConfig() {
        // Valeurs par défaut
        this.accessLogPath = "./logs/access.log";
        this.errorLogPath = "./logs/error.log";
        this.logLevel = "INFO";
        this.enableAccessLog = true;
        this.enableErrorLog = true;
    }
    
    // Getters et Setters
    public String getAccessLogPath() {
        return accessLogPath;
    }
    
    public void setAccessLogPath(String accessLogPath) {
        this.accessLogPath = accessLogPath;
    }
    
    public String getErrorLogPath() {
        return errorLogPath;
    }
    
    public void setErrorLogPath(String errorLogPath) {
        this.errorLogPath = errorLogPath;
    }
    
    public String getLogLevel() {
        return logLevel;
    }
    
    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }
    
    public boolean isEnableAccessLog() {
        return enableAccessLog;
    }
    
    public void setEnableAccessLog(boolean enableAccessLog) {
        this.enableAccessLog = enableAccessLog;
    }
    
    public boolean isEnableErrorLog() {
        return enableErrorLog;
    }
    
    public void setEnableErrorLog(boolean enableErrorLog) {
        this.enableErrorLog = enableErrorLog;
    }
}