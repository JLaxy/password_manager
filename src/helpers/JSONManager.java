package helpers;

/*
 * Class that handles JSON files used in the program
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JSONManager {

    // Easy to change values
    private final String SETTINGS_PATH = "src/appsettings.json";

    // Constructor
    public JSONManager() {
        // Initializing Appdata file
        initializeAppdataFile();
    }

    // Initializies settings file; makes sure it exists at the start of the program
    private void initializeAppdataFile() {
        if (!doesAppdataFileExist())
            createAppdataFile();
    }

    // Returns true if settings file exists
    private Boolean doesAppdataFileExist() {
        try (Reader myReader = new BufferedReader(new InputStreamReader(new FileInputStream(SETTINGS_PATH)))) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Creating settings file if it does not exist
    private void createAppdataFile() {
        String masterPassword;
        PopupDialog.showInfoDialog("No Appdata File", "Creating Appdata File for the first time");
        try (Writer myWriter = new BufferedWriter(new FileWriter(SETTINGS_PATH))) {
            masterPassword = PopupDialog.getUserInput("Enter Master Password",
                    "Please enter your new Master Password. \n\nNOTE THAT THIS CANNOT BE CHANGED ONCE YOU FORGET IT.")
                    .replaceAll("\\s+", "");
            Map<String, Object> settingsFile = new HashMap<String, Object>();
            settingsFile.put("program_settings", getJSONPair("masterPassword", masterPassword));
            new Gson().toJson(settingsFile, myWriter);
        } catch (Exception e) {
            PopupDialog.showErrorDialog(e, this.getClass().getName());
        }
    }

    // Returns a key-value pair which is used in JSON Files; number of arguments
    // must be even!
    private Map<String, Object> getJSONPair(Object... values) throws Exception {
        if (!(values.length % 2 == 0))
            throw new Exception("Invalid custom JSONPairs in " + getClass().getName());

        Map<String, Object> myJSONPair = new HashMap<String, Object>();

        int pair = 0;
        String key = "";
        // Iterating through each values
        for (Object value : values) {
            ++pair;
            // If iterating through key
            if (pair % 2 == 0) {
                // Put into map
                myJSONPair.put(key, value);
                pair = 0;
                // Skip
                continue;
            }
            // Get key value
            key = value.toString();
        }
        return myJSONPair;
    }

    // // Retrieves Login Cooldown stored in settings file
    // public String getLoginCooldown() {
    // try (Reader myReader = new BufferedReader(new InputStreamReader(new
    // FileInputStream(SETTINGS_PATH)))) {
    // JsonObject myObject = JsonParser.parseReader(myReader).getAsJsonObject();
    // JsonObject program_settings = myObject.getAsJsonObject("program_settings");
    // return program_settings.get("cooldown").getAsString();
    // } catch (Exception e) {
    // PopupDialog.showErrorDialog(e, this.getClass().getName());
    // return null;
    // }
    // }

    // // Updates Login Cooldown stored in settings file
    // public void updateLoginCooldown() {
    // // CANNOT READ AND WRITE FILE AT THE SAME
    // try (Reader myReader = new BufferedReader(
    // new InputStreamReader(new FileInputStream(SETTINGS_PATH)))) {
    // // Creating builder
    // Gson gson = new GsonBuilder().setPrettyPrinting().create();
    // // Reading existing file
    // JsonObject root = gson.fromJson(myReader, JsonObject.class);
    // myReader.close();

    // Writer myWriter = new BufferedWriter(new FileWriter(SETTINGS_PATH));
    // JsonObject program_settings = root.getAsJsonObject("program_settings");
    // JsonObject cooldown = program_settings.getAsJsonObject();

    // // Updating Cooldown
    // cooldown.addProperty("cooldown",
    // DateHelper.dateToString(DateHelper.addMinutes(DateHelper.getCurrentDateTime(),
    // 5)));
    // gson.toJson(root, myWriter);
    // myWriter.close();
    // } catch (Exception e) {
    // PopupDialog.showErrorDialog(e, this.getClass().getName());
    // }
    // }
}
