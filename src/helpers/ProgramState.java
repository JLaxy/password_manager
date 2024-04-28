/*
 * Contains all of the pre-defined states of the program
 * 
 * For better readability
 */

package helpers;

public class ProgramState {
    public enum State {
        // PROGRAM_STATE(state)
        EDIT_CREDENTIALS("editing"),
        SELECTED_CREDENTIALS("selected"),
        SELECTING_CREDENTIALS("selecting"),
        ADDING_CREDENTIALS("adding");

        private final String programState;

        // Constructor for ENUM
        State(String programState) {
            this.programState = programState;
        }

        // Returns the value of the action_id equivalent to database
        public String getValue() {
            return this.programState;
        }
    }
}
