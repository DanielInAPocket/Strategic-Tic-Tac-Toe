
package tictactoe_2_0_deluxe_reloaded;

import tictactoe_2_0_deluxe_reloaded.Player.PlayerType;

public class Square {
    
    //---------------------------------------------------------
    // Constants and Enumurations
    //---------------------------------------------------------

    public enum SquareState {
        Empty, 
        X_State, 
        O_State,
        Tie;
        
        public static SquareState fromPlayerType(PlayerType type) {
            switch(type) {
            case X:
                return X_State;
            case O:
                return O_State;
            }
            return Empty;
        }
    }

    //---------------------------------------------------------
    // Class variables
    //---------------------------------------------------------

    private SquareState state;
    
    //---------------------------------------------------------
    // Constructor
    //---------------------------------------------------------

    public Square() {
        this.state = SquareState.Empty;
    }

    //---------------------------------------------------------
    // Square state setters
    //---------------------------------------------------------

    public void changeState(SquareState state) {
        this.state = state;
    }
    
    public SquareState getState() {
        return state;
    }

    //---------------------------------------------------------
    // Square state getters
    //---------------------------------------------------------

    public String getStateString() {
        String stateStr;
        
        switch (this.state)
        {
            case X_State:
                stateStr = " X ";
                break;
            case O_State:
                stateStr = " O ";
                break;
            default:
                stateStr = "   ";
                break;
        }
        
        return stateStr;
    }
}
