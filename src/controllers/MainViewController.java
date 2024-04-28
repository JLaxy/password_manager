/*
 * Controller of MainView
 */

package controllers;

import helpers.JSONManager;
import helpers.PopupDialog;
import helpers.ProgramState;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import models.MainViewModel;

public class MainViewController {
    // Initial state
    private ProgramState.State programState = ProgramState.State.SELECTING_CREDENTIALS;
    // Model
    private MainViewModel model;

    // Injecting FXML elements
    @FXML
    private Button addButton, editButton, deleteButton, editMasterPassButton, saveButton, decryptButton, generateButton;
    @FXML
    private TextField credentialTextField, userTextField;
    @FXML
    private TextArea passwordTextArea;
    @FXML
    private Text dateCreatedLabel, lastModifiedLabel, dateCreatedValueLabel, lastModifiedValueLabel;

    // Constructor
    public MainViewController() {
        System.out.println("Initialized!");
        this.model = new MainViewModel();
    }

    // Syncing references
    public void initReferences(JSONManager appDataManager) {
        this.model.setAppDataManager(appDataManager);
        // Syncing elements to program state
        syncElementStates();
    }

    // Updates states of element according to program state
    private void syncElementStates() {
        switch (this.programState) {
            // Selecting Credentials State
            case ProgramState.State.SELECTING_CREDENTIALS:
                // Disabling Buttons
                this.generateButton.setDisable(true);
                this.decryptButton.setDisable(true);
                this.saveButton.setDisable(true);
                // Disabling Fields
                this.credentialTextField.setDisable(true);
                this.userTextField.setDisable(true);
                this.passwordTextArea.setDisable(true);
                // Disabling Labels
                this.dateCreatedLabel.setVisible(false);
                this.dateCreatedValueLabel.setVisible(false);
                this.lastModifiedLabel.setVisible(false);
                this.lastModifiedValueLabel.setVisible(false);
                break;

            case ProgramState.State.ADDING_CREDENTIALS:
                // Enabling Buttons
                this.generateButton.setDisable(false);
                this.decryptButton.setDisable(false);
                this.saveButton.setDisable(false);
                // Enabling Fields
                this.credentialTextField.setDisable(false);
                this.userTextField.setDisable(false);
                this.passwordTextArea.setDisable(false);
                // Disabling Buttons
                this.decryptButton.setDisable(true);
                this.editButton.setDisable(true);
                this.deleteButton.setDisable(true);
                break;

            default:
                break;
        }
    }

    // Generates Random Password
    public void generateRandomPassword() {
        // Set Generated Random Password to Text Area
        this.passwordTextArea.setText(this.model.generateRandomPassword());
    }

    // Returns true if all fields have values
    public boolean areFieldsFilled() {
        return (this.credentialTextField.getText().length() != 0) && (this.userTextField.getText().length() != 0)
                && (this.passwordTextArea.getText().length() != 0);
    }

    // Save credential to file
    public void saveCredentials() {
        // Validations
        // Checks if all fields are filled
        if (!areFieldsFilled()) {
            PopupDialog.showCustomErrorDialog("Please fill out all fields!");
            return;
        }
        // Checks if Credential Label is unique
        if (model.doesCredentialLabelExist(this.credentialTextField.getText())) {
            PopupDialog.showCustomErrorDialog("That credential label already exists!");
            return;
        }

        // Check if password matches master password
        if (this.model.isPasswordCorrect(
                PopupDialog.getUserInput("Master Password", "Please enter your Master Password for this app"))) {
            // Save New Credentials
            if (this.model.saveNewCredential(this.credentialTextField.getText(), this.userTextField.getText(),
                    this.passwordTextArea.getText().replaceAll("\\s+", "")))
                PopupDialog.showInfoDialog("Success!", "New credentials saved successfully");
            else
                PopupDialog.showCustomErrorDialog("Failed to save new credentials!");
            // Else
        } else {
            PopupDialog.showCustomErrorDialog("Incorrect Master Password!");
        }

    }

    public void addCredentials() {
        System.out.println("adding credentials...");
        this.programState = ProgramState.State.ADDING_CREDENTIALS;
        syncElementStates();
    }

}
