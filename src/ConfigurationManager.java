import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationManager {
   private ServerConfig config;
   private final String configPath;
   private static final String DEFAULT_CONFIG_PATH = "C:\\Users\\ACER\\Documents\\Nouveau_Projet\\server.properties";

   public ConfigurationManager() {
      this("C:\\Users\\ACER\\Documents\\Nouveau_Projet\\server.properties");
   }

   public ConfigurationManager(String var1) {
      this.configPath = var1;
      this.loadConfiguration();
   }

   public void loadConfiguration() {
      Properties var1 = new Properties();

      try {
         FileInputStream var2 = new FileInputStream(this.configPath);

         try {
            var1.load(var2);
            this.config = this.fromProperties(var1);
         } catch (Throwable var6) {
            try {
               var2.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }

            throw var6;
         }

         var2.close();
      } catch (IOException var7) {
         System.err.println("Erreur lors du chargement de la configuration : " + var7.getMessage());
         this.config = this.createDefaultConfig();
         this.saveConfiguration();
      }

   }

   public void saveConfiguration() {
      Properties var1 = this.toProperties(this.config);

      try {
         FileOutputStream var2 = new FileOutputStream(this.configPath);

         try {
            var1.store(var2, "Server Configuration");
         } catch (Throwable var6) {
            try {
               var2.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }

            throw var6;
         }

         var2.close();
      } catch (IOException var7) {
         System.err.println("Erreur lors de la sauvegarde de la configuration : " + var7.getMessage());
      }

   }

   private ServerConfig createDefaultConfig() {
      ServerConfig var1 = new ServerConfig();
      var1.setPort(8080);
      var1.setHtdocs("./htdocs");
      var1.setPhpEnabled(true);
      var1.setPhpConfig(new PhpConfig());
      var1.setServerName("JavaHttpServer");
      var1.setMaxThreads(10);
      return var1;
   }

   private ServerConfig fromProperties(Properties var1) {
      ServerConfig var2 = new ServerConfig();
      var2.setPort(Integer.parseInt(var1.getProperty("port", "8080")));
      var2.setHtdocs(var1.getProperty("htdocs", "./htdocs"));
      var2.setPhpEnabled(Boolean.parseBoolean(var1.getProperty("phpEnabled", "true")));
      var2.setServerName(var1.getProperty("serverName", "JavaHttpServer"));
      var2.setMaxThreads(Integer.parseInt(var1.getProperty("maxThreads", "10")));
      PhpConfig var3 = new PhpConfig();
      var3.setPhpPath(var1.getProperty("phpConfig.phpPath", ""));
      var3.setPhpIniPath(var1.getProperty("phpConfig.phpIniPath", ""));
      var3.setMaxExecutionTime(Integer.parseInt(var1.getProperty("phpConfig.maxExecutionTime", "30")));
      var3.setMaxInputTime(Integer.parseInt(var1.getProperty("phpConfig.maxInputTime", "60")));
      var3.setUploadTempDir(var1.getProperty("phpConfig.uploadTempDir", "./temp"));
      var3.setSessionPath(var1.getProperty("phpConfig.sessionPath", "./sessions"));
      var3.setDisplayErrors(Boolean.parseBoolean(var1.getProperty("phpConfig.displayErrors", "false")));
      var2.setPhpConfig(var3);
      LoggingConfig var4 = new LoggingConfig();
      var4.setAccessLogPath(var1.getProperty("logging.accessLogPath", "./logs/access.log"));
      var4.setErrorLogPath(var1.getProperty("logging.errorLogPath", "./logs/error.log"));
      var4.setLogLevel(var1.getProperty("logging.logLevel", "INFO"));
      var4.setEnableAccessLog(Boolean.parseBoolean(var1.getProperty("logging.enableAccessLog", "true")));
      var4.setEnableErrorLog(Boolean.parseBoolean(var1.getProperty("logging.enableErrorLog", "true")));
      var2.setLogging(var4);
      return var2;
   }

   private Properties toProperties(ServerConfig var1) {
      Properties var2 = new Properties();
      var2.setProperty("port", String.valueOf(var1.getPort()));
      var2.setProperty("htdocs", var1.getHtdocs());
      var2.setProperty("phpEnabled", String.valueOf(var1.isPhpEnabled()));
      var2.setProperty("serverName", var1.getServerName());
      var2.setProperty("maxThreads", String.valueOf(var1.getMaxThreads()));
      PhpConfig var3 = var1.getPhpConfig();
      if (var3 != null) {
         var2.setProperty("phpConfig.phpPath", var3.getPhpPath());
         var2.setProperty("phpConfig.phpIniPath", var3.getPhpIniPath());
         var2.setProperty("phpConfig.maxExecutionTime", String.valueOf(var3.getMaxExecutionTime()));
         var2.setProperty("phpConfig.maxInputTime", String.valueOf(var3.getMaxInputTime()));
         var2.setProperty("phpConfig.uploadTempDir", var3.getUploadTempDir());
         var2.setProperty("phpConfig.sessionPath", var3.getSessionPath());
         var2.setProperty("phpConfig.displayErrors", String.valueOf(var3.isDisplayErrors()));
      }

      LoggingConfig var4 = var1.getLogging();
      if (var4 != null) {
         var2.setProperty("logging.accessLogPath", var4.getAccessLogPath());
         var2.setProperty("logging.errorLogPath", var4.getErrorLogPath());
         var2.setProperty("logging.logLevel", var4.getLogLevel());
         var2.setProperty("logging.enableAccessLog", String.valueOf(var4.isEnableAccessLog()));
         var2.setProperty("logging.enableErrorLog", String.valueOf(var4.isEnableErrorLog()));
      }

      return var2;
   }

   public ServerConfig getConfig() {
      return this.config;
   }

   public void updateConfig(ServerConfig var1) {
      this.config = var1;
      this.saveConfiguration();
   }
}
