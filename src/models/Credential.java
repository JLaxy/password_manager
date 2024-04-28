package models;

/**
 * User Credentials
 */

public class Credential {
    final String credentialLabel, username, password, dateCreated, lastModified;

    public Credential(String credentialLabel, String username, String password, String dateCreated,
            String lastModified) {
        this.credentialLabel = credentialLabel;
        this.username = username;
        this.password = password;
        this.dateCreated = dateCreated;
        this.lastModified = lastModified;
    }

}