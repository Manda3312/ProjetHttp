import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.JTabbedPane;
import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;



public class ConfigurationGUI extends JFrame {
    private final ConfigurationManager configManager;
    private ServerConfig config;
    private JTextField portField;
    private JTextField htdocsField;
    private JCheckBox phpEnabledBox;
    private JTextField phpPathField;
    private JSpinner maxThreadsSpinner;
    private JTextField accessLogField;
    private JTextField errorLogField;
    private JComboBox<String> logLevelCombo;
    private JTextField uploadTempDirField;
    private JTextField sessionPathField;
    private JCheckBox displayErrorsBox;
    private JButton saveButton;
    private JButton applyButton;
    private JButton startButton;
    private JButton stopButton;
    private JLabel statusLabel;
    private boolean serverRunning = false;

    public ConfigurationGUI(ConfigurationManager configManager) {
        this.configManager = configManager;
        this.config = configManager.getConfig();
        this.setupGUI();
        this.loadConfigValues();
    }

    private void setupGUI() {
        this.setTitle("Configuration du Serveur");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout(10, 10));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Général", this.createGeneralPanel());
        tabbedPane.addTab("PHP", this.createPhpPanel());
        tabbedPane.addTab("Logs", this.createLogsPanel());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        this.saveButton = new JButton("Sauvegarder");
        this.applyButton = new JButton("Appliquer");
        buttonPanel.add(this.saveButton);
        buttonPanel.add(this.applyButton);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Contrôle du Serveur"));
        this.startButton = new JButton("Démarrer");
        this.stopButton = new JButton("Arrêter");
        this.statusLabel = new JLabel("Statut: Arrêté");
        this.stopButton.setEnabled(false);
        controlPanel.add(this.startButton);
        controlPanel.add(this.stopButton);
        controlPanel.add(this.statusLabel);

        this.add(controlPanel, BorderLayout.NORTH);
        this.add(tabbedPane, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);

        this.setupEventHandlers();
        this.setSize(600, 500);
        this.setLocationRelativeTo(null);
    }

    private JPanel createGeneralPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Port:"), gbc);

        this.portField = new JTextField(10);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(this.portField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Répertoire htdocs:"), gbc);

        this.htdocsField = new JTextField();
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(this.htdocsField, gbc);

        JButton browseButton = new JButton("Parcourir");
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        panel.add(browseButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Threads max:"), gbc);

        this.maxThreadsSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        gbc.gridx = 1;
        panel.add(this.maxThreadsSpinner, gbc);

        browseButton.addActionListener(e -> browseFolderDialog(this.htdocsField));

        return panel;
    }

    private JPanel createPhpPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Activer PHP:"), gbc);

        this.phpEnabledBox = new JCheckBox();
        gbc.gridx = 1;
        panel.add(this.phpEnabledBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Chemin PHP:"), gbc);

        this.phpPathField = new JTextField();
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(this.phpPathField, gbc);

        JButton browseButton = new JButton("Parcourir");
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        panel.add(browseButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Dossier temp upload:"), gbc);

        this.uploadTempDirField = new JTextField();
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(this.uploadTempDirField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Dossier sessions:"), gbc);

        this.sessionPathField = new JTextField();
        gbc.gridx = 1;
        panel.add(this.sessionPathField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Afficher les erreurs:"), gbc);

        this.displayErrorsBox = new JCheckBox();
        gbc.gridx = 1;
        panel.add(this.displayErrorsBox, gbc);

        browseButton.addActionListener(e -> browseFileDialog(this.phpPathField));

        return panel;
    }

    private JPanel createLogsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Fichier log accès:"), gbc);

        this.accessLogField = new JTextField();
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(this.accessLogField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Fichier log erreurs:"), gbc);

        this.errorLogField = new JTextField();
        gbc.gridx = 1;
        panel.add(this.errorLogField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Niveau de log:"), gbc);

        this.logLevelCombo = new JComboBox<>(new String[]{"DEBUG", "INFO", "WARN", "ERROR"});
        gbc.gridx = 1;
        panel.add(this.logLevelCombo, gbc);

        return panel;
    }

    private void setupEventHandlers() {
        this.saveButton.addActionListener(e -> {
            saveConfiguration();
            configManager.saveConfiguration();
            JOptionPane.showMessageDialog(this, "Configuration sauvegardée avec succès", "Sauvegarde", JOptionPane.INFORMATION_MESSAGE);
        });

        this.applyButton.addActionListener(e -> {
            saveConfiguration();
            JOptionPane.showMessageDialog(this, "Configuration appliquée", "Application", JOptionPane.INFORMATION_MESSAGE);
        });

        this.startButton.addActionListener(e -> {
         System.out.println("Démarrage du serveur...");
         try {
             saveConfiguration();
             serverRunning = true;
             updateServerControlState();
             statusLabel.setText("Statut: En cours d'exécution");
             JOptionPane.showMessageDialog(this, "Serveur démarré sur le port " + config.getPort(), "Serveur Démarré", JOptionPane.INFORMATION_MESSAGE);
         } catch (Exception ex) {
             JOptionPane.showMessageDialog(this, "Erreur lors du démarrage du serveur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
         }
     });
     

        this.stopButton.addActionListener(e -> {
            try {
                serverRunning = false;
                updateServerControlState();
                statusLabel.setText("Statut: Arrêté");
                JOptionPane.showMessageDialog(this, "Serveur arrêté", "Serveur Arrêté", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'arrêt du serveur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void loadConfigValues() {
      this.portField.setText(String.valueOf(this.config.getPort()));
      this.htdocsField.setText(this.config.getHtdocs());
      this.maxThreadsSpinner.setValue(this.config.getMaxThreads());

      PhpConfig phpConfig = this.config.getPhpConfig();
      this.phpEnabledBox.setSelected(this.config.isPhpEnabled());
      this.phpPathField.setText(phpConfig.getPhpPath());
      this.uploadTempDirField.setText(phpConfig.getUploadTempDir());
      this.sessionPathField.setText(phpConfig.getSessionPath());
      this.displayErrorsBox.setSelected(phpConfig.isDisplayErrors());

      LoggingConfig loggingConfig = this.config.getLogging();
      this.accessLogField.setText(loggingConfig.getAccessLogPath());
      this.errorLogField.setText(loggingConfig.getErrorLogPath());
      this.logLevelCombo.setSelectedItem(loggingConfig.getLogLevel());
   }

   private void saveConfiguration() {
      this.config.setPort(Integer.parseInt(this.portField.getText()));
      this.config.setHtdocs(this.htdocsField.getText());
      this.config.setMaxThreads((Integer) this.maxThreadsSpinner.getValue());
      this.config.setPhpEnabled(this.phpEnabledBox.isSelected());

      PhpConfig phpConfig = this.config.getPhpConfig();
      phpConfig.setPhpPath(this.phpPathField.getText());
      phpConfig.setUploadTempDir(this.uploadTempDirField.getText());
      phpConfig.setSessionPath(this.sessionPathField.getText());
      phpConfig.setDisplayErrors(this.displayErrorsBox.isSelected());

      LoggingConfig loggingConfig = this.config.getLogging();
      loggingConfig.setAccessLogPath(this.accessLogField.getText());
      loggingConfig.setErrorLogPath(this.errorLogField.getText());
      loggingConfig.setLogLevel((String) this.logLevelCombo.getSelectedItem());
   }

    private void browseFolderDialog(JTextField textField) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void browseFileDialog(JTextField textField) {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void updateServerControlState() {
        this.startButton.setEnabled(!serverRunning);
        this.stopButton.setEnabled(serverRunning);
    }
}
