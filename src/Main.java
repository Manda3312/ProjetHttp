import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ConfigurationManager configManager = new ConfigurationManager();
            new ConfigurationGUI(configManager).setVisible(true);
        });
    }
}
