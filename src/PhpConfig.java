
// PhpConfig.java
public class PhpConfig {
    private String phpPath;
    private String phpIniPath;
    private int maxExecutionTime;
    private int maxInputTime;
    private String uploadTempDir;
    private String sessionPath;
    private boolean displayErrors;
    
    public PhpConfig() {
        // Valeurs par défaut
        this.phpPath = "/usr/bin/php";
        this.maxExecutionTime = 30;
        this.maxInputTime = 60;
        this.uploadTempDir = "./temp";
        this.sessionPath = "./sessions";
        this.displayErrors = false;
    }
    
    // Getters et Setters
    public String getPhpPath() {
        return phpPath;
    }
    
    public void setPhpPath(String phpPath) {
        this.phpPath = phpPath;
    }
    
    public String getPhpIniPath() {
        return phpIniPath;
    }
    
    public void setPhpIniPath(String phpIniPath) {
        this.phpIniPath = phpIniPath;
    }
    
    public int getMaxExecutionTime() {
        return maxExecutionTime;
    }
    
    public void setMaxExecutionTime(int maxExecutionTime) {
        this.maxExecutionTime = maxExecutionTime;
    }
    
    public int getMaxInputTime() {
        return maxInputTime;
    }
    
    public void setMaxInputTime(int maxInputTime) {
        this.maxInputTime = maxInputTime;
    }
    
    public String getUploadTempDir() {
        return uploadTempDir;
    }
    
    public void setUploadTempDir(String uploadTempDir) {
        this.uploadTempDir = uploadTempDir;
    }
    
    public String getSessionPath() {
        return sessionPath;
    }
    
    public void setSessionPath(String sessionPath) {
        this.sessionPath = sessionPath;
    }
    
    public boolean isDisplayErrors() {
        return displayErrors;
    }
    
    public void setDisplayErrors(boolean displayErrors) {
        this.displayErrors = displayErrors;
    }
}
