/*
 * Controller of MainView
 */

package controllers;

import java.util.ArrayList;

import helpers.JSONManager;
import helpers.PopupDialog;
import helpers.ProgramState;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import models.Credential;
import models.MainViewModel;

public class MainViewController {
    // Initial state
    private ProgramState.State programState = ProgramState.State.SELECTING_CREDENTIALS;
    // Model
    private MainViewModel model;

    // Injecting FXML elements
    @FXML
    private Button addButton, editButton, deleteButton, editMasterPassButton, cancelButton, saveButton, decryptButton,
            generateButton;
    @FXML
    private TextField credentialTextField, userTextField;
    @FXML
    private TextArea passwordTextArea;
    @FXML
    private Text dateCreatedLabel, lastModifiedLabel, dateCreatedValueLabel, lastModifiedValueLabel;
    @FXML
    private ListView<String> credentialListView;

    // Constructor
    public MainViewController() {
        System.out.println("Initialized!");
        this.model = new MainViewModel();
    }

    // Ran after loading root
    @FXML
    public void initialize() {
        // Syncing elements to program state
        syncElementStates();

        // Function triggered when selecting on list view
        credentialListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
                if (credentialListView.getSelectionModel().getSelectedItem() == null)
                    return;

                // Update State
                programState = ProgramState.State.SELECTED_CREDENTIALS;
                syncElementStates();

                // Iterate through each credential
                for (Credential credential : model.getCredentialsList()) {
                    // If has same credentialLabel
                    if (credential.credentialLabel
                            .compareTo(credentialListView.getSelectionModel().getSelectedItem()) == 0) {
                        credentialTextField.setText(credential.credentialLabel);
                        userTextField.setText(credential.username);
                        passwordTextArea.setText(credential.password);
                        dateCreatedValueLabel.setText(credential.dateCreated);
                        lastModifiedValueLabel.setText(credential.lastModified);
                        break;
                    }
                }
            }

        });
    }

    // Syncing references
    public void initReferences(JSONManager appDataManager) {
        this.model.setAppDataManager(appDataManager);
        // TODO: Initialize List View
        initListView();
    }

    // Initilizes List View and Loads Data
    private void initListView() {
        ArrayList<Credential> credentialsList = this.model.initializeCredentials();

        // Iterating through each credential in list
        for (Credential credential : credentialsList) {
            credentialListView.getItems().add(credential.credentialLabel);
        }
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
                this.cancelButton.setDisable(true);
                this.editButton.setDisable(true);
                this.deleteButton.setDisable(true);
                // Enabling Buttons
                this.addButton.setDisable(false);
                // Disabling Fields
                this.credentialTextField.setDisable(true);
                this.credentialTextField.setEditable(false);
                this.userTextField.setDisable(true);
                this.userTextField.setEditable(false);
                this.passwordTextArea.setDisable(true);
                this.passwordTextArea.setEditable(false);
                // Hiding Labels
                this.dateCreatedLabel.setVisible(false);
                this.dateCreatedValueLabel.setVisible(false);
                this.lastModifiedLabel.setVisible(false);
                this.lastModifiedValueLabel.setVisible(false);
                // Enabling list view
                credentialListView.setDisable(false);
                break;

            case ProgramState.State.ADDING_CREDENTIALS:
                // Enabling Buttons
                this.generateButton.setDisable(false);
                this.saveButton.setDisable(false);
                this.cancelButton.setDisable(false);
                // Enabling Fields
                this.credentialTextField.setDisable(false);
                this.credentialTextField.setEditable(true);
                this.userTextField.setDisable(false);
                this.userTextField.setEditable(true);
                this.passwordTextArea.setDisable(false);
                this.passwordTextArea.setEditable(true);
                // Disabling Buttons
                this.decryptButton.setDisable(true);
                this.addButton.setDisable(true);
                this.editButton.setDisable(true);
                this.deleteButton.setDisable(true);
                // Enabling
                credentialListView.setDisable(true);
                break;

            case ProgramState.State.SELECTED_CREDENTIALS:
                // Enabling Buttons
                this.editButton.setDisable(false);
                this.deleteButton.setDisable(false);
                this.decryptButton.setDisable(false);
                this.cancelButton.setDisable(false);
                // Disabling Buttons
                this.addButton.setDisable(true);
                this.generateButton.setDisable(true);
                this.saveButton.setDisable(true);
                // Enabling Fields
                this.credentialTextField.setDisable(false);
                this.credentialTextField.setEditable(false);
                this.userTextField.setDisable(false);
                this.userTextField.setEditable(false);
                this.passwordTextArea.setDisable(false);
                this.passwordTextArea.setEditable(false);
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

    private void clearFields() {
        this.credentialTextField.clear();
        this.userTextField.clear();
        this.passwordTextArea.clear();
    }

    // Returns true if all fields have values
    public boolean areFieldsFilled() {
        return (this.credentialTextField.getText().length() != 0) && (this.userTextField.getText().length() != 0)
                && (this.passwordTextArea.getText().length() != 0);
    }

    // Sets state to adding credentials
    public void addCredentials() {
        System.out.println("adding credentials...");
        this.programState = ProgramState.State.ADDING_CREDENTIALS;
        syncElementStates();
    }

    // Sets state back to selecting credentials
    public void cancelAddingCredentials() {
        // Clear Text Fields
        clearFields();
        // Update State
        this.programState = ProgramState.State.SELECTING_CREDENTIALS;
        syncElementStates();
        // Clearing selected from list view
        credentialListView.getSelectionModel().clearSelection();
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
        if (isMasterPasswordVerified()) {
            // Save New Credentials
            if (this.model.saveNewCredential(this.credentialTextField.getText(), this.userTextField.getText(),
                    this.passwordTextArea.getText().replaceAll("\\s+", ""))) {
                // Show success dialog
                PopupDialog.showInfoDialog("Success!", "New credentials saved successfully");
                // Clear Text Fields
                clearFields();
                // Update Program state
                this.programState = ProgramState.State.SELECTING_CREDENTIALS;
            } else
                PopupDialog.showCustomErrorDialog("Failed to save new credentials!");
        }

    }

    // Edits master passsword
    public void editMasterPassword() {
        // Checks if user will supply correct master password
        if (isMasterPasswordVerified()) {
            String newPassword = PopupDialog.getUserInput("New Master Password", "Enter New Master Password");

            // Cancels operation if user left dialog box blank
            if (newPassword == null)
                return;

            if (this.model.updateMasterPassword(newPassword))
                PopupDialog.showInfoDialog("Master Password Updated", "Successfully updated Master Password!");
        }
    }

    // Returns true if password matches master password
    private boolean isMasterPasswordVerified() {
        String password = PopupDialog.getUserInput("Master Password", "Enter Master Password");

        // Returns false if left blank
        if (password == null)
            return false;

        // Return true if correct
        if (this.model.isPasswordCorrect(password))
            return true;

        // Else, return false
        PopupDialog.showCustomErrorDialog("Incorrect Master Password!");
        return false;
    }
}
