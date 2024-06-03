
package tictactoe_2_0_deluxe_reloaded;

import java.io.Serializable;
import tictactoe_2_0_deluxe_reloaded.Player.PlayerType;

public class PlayerMove implements Serializable {
    
    //---------------------------------------------------------
    // Class variables
    //---------------------------------------------------------

    public Coordinates section;
    public Coordinates square;
    public PlayerType playerType;

    //---------------------------------------------------------
    // Constructor
    //---------------------------------------------------------

    public PlayerMove(Coordinates section, Coordinates square, PlayerType playerType) {
        this.section = section;
        this.square = square;
        this.playerType = playerType;
    }
}
