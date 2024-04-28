package models;

import java.util.Random;

import helpers.JSONManager;

public class MainViewModel {
    private JSONManager appDataManager;

    // Setter of AppDataManager
    public void setAppDataManager(JSONManager appDataManager) {
        this.appDataManager = appDataManager;
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
        return this.appDataManager.saveNewCredential(credentialLabel, username, password);
    }

    // Returns True if password matches master password
    public boolean isPasswordCorrect(String password) {
        if (password == null) {
            return false;
        }
        return this.appDataManager.isPasswordCorrect(password);
    }
}
