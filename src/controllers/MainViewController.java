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
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.text.Text;
import models.Credential;
import models.MainViewModel;

public class MainViewController {
    // Instances of clipboard; allows to automatically copy password to clipboard
    private Clipboard clipboard = Clipboard.getSystemClipboard();
    private ClipboardContent clipboardContent = new ClipboardContent();

    // Initial state
    private ProgramState.State programState = ProgramState.State.SELECTING_CREDENTIALS;

    private MainViewModel model;
    private Credential selectedCredential;

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

        // Defining function triggered when selecting on list view
        credentialListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
                // Immediately return if selection was just unselected
                if (credentialListView.getSelectionModel().getSelectedItem() == null) {
                    selectedCredential = null;
                    return;
                }

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
                        // Setting current credential as selected credential
                        selectedCredential = credential;
                        break;
                    }
                }
            }

        });
    }

    // Syncing references
    public void initReferences(JSONManager appDataManager) {
        this.model.setAppDataManager(appDataManager);
        initListView();
    }

    // Initilizes List View and Loads Data
    private void initListView() {
        ArrayList<Credential> credentialsList = this.model.initializeCredentials();
        // Clearing list view
        credentialListView.getItems().clear();
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
                // Hiding Labels
                this.dateCreatedLabel.setVisible(false);
                this.dateCreatedValueLabel.setVisible(false);
                this.lastModifiedLabel.setVisible(false);
                this.lastModifiedValueLabel.setVisible(false);
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
                // Showing Labels
                this.dateCreatedLabel.setVisible(true);
                this.dateCreatedValueLabel.setVisible(true);
                this.lastModifiedLabel.setVisible(true);
                this.lastModifiedValueLabel.setVisible(true);
                break;

            case ProgramState.State.EDITING_CREDENTIALS:
                // Disabling Buttons
                this.addButton.setDisable(true);
                this.decryptButton.setDisable(true);
                this.editButton.setDisable(true);
                this.deleteButton.setDisable(true);
                // Enabling Buttons
                this.generateButton.setDisable(false);
                this.cancelButton.setDisable(false);
                this.saveButton.setDisable(false);
                // Enabling Fields
                this.credentialTextField.setDisable(false);
                this.credentialTextField.setEditable(true);
                this.userTextField.setDisable(false);
                this.userTextField.setEditable(true);
                this.passwordTextArea.setDisable(false);
                this.passwordTextArea.setEditable(true);
                // Hiding Labels
                this.dateCreatedLabel.setVisible(false);
                this.dateCreatedValueLabel.setVisible(false);
                this.lastModifiedLabel.setVisible(false);
                this.lastModifiedValueLabel.setVisible(false);
                // Disabling list view
                credentialListView.setDisable(true);
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
        if (isMasterPasswordVerified()) {
            System.out.println("adding credentials...");
            this.programState = ProgramState.State.ADDING_CREDENTIALS;
            syncElementStates();
        }
    }

    // Sets state back to selecting credentials
    public void cancelButton() {
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

        // If adding new credential
        if (this.selectedCredential == null) {
            // Checks if Credential Label is unique
            if (model.doesCredentialLabelExist(this.credentialTextField.getText())) {
                PopupDialog.showCustomErrorDialog("That credential label already exists!");
                return;
            }

            // Save New Credentials
            if (this.model.saveNewCredential(this.credentialTextField.getText(), this.userTextField.getText(),
                    this.passwordTextArea.getText().replaceAll("\\s+", ""))) {
                // Refreshing list view
                initListView();
                // Show success dialog
                PopupDialog.showInfoDialog("Success!", "New credentials saved successfully");
                // Clear Text Fields
                clearFields();
                // Update Program state
                this.programState = ProgramState.State.SELECTING_CREDENTIALS;
                syncElementStates();
                return;
            }
            // Editing new credentials
            // If sucessfully edited credential
        } else if (this.model.editCredential(this.selectedCredential, new Credential(this.credentialTextField.getText(),
                this.userTextField.getText(), this.model.encryptPassword(this.passwordTextArea.getText()), null,
                null))) {
            PopupDialog.showInfoDialog("Updated Credential", "Sucessfully edited credential!");
            // Clear fields
            clearFields();
            // Refresh list view
            initListView();
            // Update program state
            this.programState = ProgramState.State.SELECTING_CREDENTIALS;
            syncElementStates();
            return;
        }
        PopupDialog.showCustomErrorDialog("Failed to save new credentials!");
    }

    // Edit selected credentials
    public void editCredentials() {
        if (isMasterPasswordVerified()) {
            this.passwordTextArea.setText(this.model.decryptPassword(this.passwordTextArea.getText()));
            this.programState = ProgramState.State.EDITING_CREDENTIALS;
            syncElementStates();
        }
    }

    // Delete credential from saved data
    public void deleteCredentials() {
        // If supplied password is correct
        if (isMasterPasswordVerified()) {
            if (this.model.deleteCredential(this.selectedCredential)) {
                // Show success message
                PopupDialog.showInfoDialog("Success!", "Successfully removed credential");
                // Update list view
                initListView();
                // Clearing fields
                clearFields();
                // Updating state
                this.programState = ProgramState.State.SELECTING_CREDENTIALS;
                syncElementStates();
                return;
            }
            // Else
            PopupDialog.showCustomErrorDialog("Failed to remove credential");
        }
    }

    // Decrypts password of selected credential
    public void decryptPassword() {
        // If supplied password is correct
        if (isMasterPasswordVerified()) {
            // Disabling button
            this.decryptButton.setDisable(true);
            this.editButton.setDisable(true);
            // Retrieving decrypted password from model
            String decryptedPassword = this.model.decryptPassword(passwordTextArea.getText());
            // Setting decrypting password in password text area
            passwordTextArea.setText(decryptedPassword);
            // Adding decrypted password to clipboard
            this.clipboardContent.putString(decryptedPassword);
            this.clipboard.setContent(clipboardContent);
            // Inform user
            PopupDialog.showInfoDialog("Added to Clipboard", "Decrypted password has been copied to your clipboard!");
        }
    }

    // Edits master passsword
    public void editMasterPassword() {
        // Checks if user will supply correct master password
        if (isMasterPasswordVerified()) {
            String newPassword = PopupDialog.getUserInput("New Master Password", "Enter New Master Password");

            // Cancels operation if user left dialog box blank
            if (newPassword == null || newPassword.isEmpty()) {
                PopupDialog.showCustomErrorDialog("Failed to update Master Password!");
                return;
            }

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
