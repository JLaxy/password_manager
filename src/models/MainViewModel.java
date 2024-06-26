package models;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import helpers.DateHelper;
import helpers.JSONManager;
import helpers.RSAManager;

public class MainViewModel {
    private JSONManager appDataManager;
    private RSAManager encryptionManager;
    private ArrayList<Credential> credentialsList;

    // Constructor
    public MainViewModel() {
        this.encryptionManager = new RSAManager();
    }

    // Setter of AppDataManager
    public void setAppDataManager(JSONManager appDataManager) {
        this.appDataManager = appDataManager;
    }

    // Initializes saved credentials then returns list
    public ArrayList<Credential> initializeCredentials() {
        credentialsList = new ArrayList<Credential>();
        // Retrieving data
        JsonObject savedCredentials = this.appDataManager.getSavedCredentials();

        // Iterating through each saved credential
        for (Map.Entry<String, JsonElement> credential : savedCredentials.entrySet()) {
            String username = credential.getValue().getAsJsonObject().get("username").getAsString();
            String password = credential.getValue().getAsJsonObject().get("password").getAsString();
            String dateCreated = credential.getValue().getAsJsonObject().get("date_created").getAsString();
            String lastModified = credential.getValue().getAsJsonObject().get("last_modified").getAsString();

            // Adding to list
            this.credentialsList
                    .add(new Credential(credential.getKey(), username, password, dateCreated, lastModified));
        }

        // Returning updated list
        return getCredentialsList();
    }

    // Returns credential list
    public ArrayList<Credential> getCredentialsList() {
        return this.credentialsList;
    }

    // Returns Generated Random Password
    public String generateRandomPassword() {
        int length = 16;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+<>?:{}|";
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder(length);

        // While length is not yet 14
        while (stringBuilder.toString().length() != length) {
            int index = random.nextInt(characters.length());
            stringBuilder.append(characters.charAt(index));
        }

        // Write random password in text area
        return stringBuilder.toString();
    }

    // Returns true if credential label already exists
    public boolean doesCredentialLabelExist(String label) {
        return this.appDataManager.doesCredentialLabelExist(label);
    }

    // Tells JSONManager to save new credential
    public boolean saveNewCredential(String credentialLabel, String username, String password) {
        String encryptedPassword = encryptPassword(password);

        // If successfully saved
        if (this.appDataManager.saveNewCredential(credentialLabel, username, encryptPassword(password), null, null)) {
            // Add to list
            credentialsList.add(new Credential(credentialLabel, username, encryptedPassword,
                    DateHelper.getCurrentDateTimeString(), DateHelper.getCurrentDateTimeString()));
            return true;
        }
        return false;
    }

    public boolean editCredential(Credential oldCredential, Credential newCredential) {
        return this.appDataManager.editCredential(oldCredential, newCredential);
    }

    public boolean deleteCredential(Credential credential) {
        return this.appDataManager.deleteCredential(credential.credentialLabel);
    }

    // Returns True if password matches master password
    public boolean isPasswordCorrect(String password) {
        if (password == null) {
            return false;
        }
        return this.appDataManager.isPasswordCorrect(password);
    }

    // Returns encrypted version of password
    public String encryptPassword(String password) {
        return this.encryptionManager.encryptMessage(password);
    }

    // Returns decrypted password from RSA Manager
    public String decryptPassword(String password) {
        return this.encryptionManager.decryptMessage(password);
    }

    // Returns true if successfully updated master password
    public boolean updateMasterPassword(String password) {
        return this.appDataManager.updateMasterPassword(password);
    }
}
