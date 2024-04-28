/*
 * Controller of MainView
 */

package controllers;

import helpers.JSONManager;
import helpers.ProgramState;

public class MainViewController {
    // References
    JSONManager appDataManager;
    // Initial state
    ProgramState.State programState = ProgramState.State.SELECTING_CREDENTIALS;

    public MainViewController() {
        System.out.println("Initialized!");
        System.out.println(programState);
    }

    // Syncing references
    public void initReferences(JSONManager appDataManager) {
        this.appDataManager = appDataManager;
    }
}
