package models;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
        return this.appDataManager.saveNewCredential(credentialLabel, username, encryptPassword(password));
    }

    // Returns True if password matches master password
    public boolean isPasswordCorrect(String password) {
        if (password == null) {
            return false;
        }
        return this.appDataManager.isPasswordCorrect(password);
    }

    // Returns encrypted version of password
    private String encryptPassword(String password) {
        return this.encryptionManager.encryptMessage(password);
    }

    public boolean updateMasterPassword(String password) {
        return this.appDataManager.updateMasterPassword(password);
    }
}
