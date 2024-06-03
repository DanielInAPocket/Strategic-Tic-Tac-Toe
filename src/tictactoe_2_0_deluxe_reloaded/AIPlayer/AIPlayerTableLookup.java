
package tictactoe_2_0_deluxe_reloaded.AIPlayer;

import tictactoe_2_0_deluxe_reloaded.Board;
import tictactoe_2_0_deluxe_reloaded.Coordinates;
import tictactoe_2_0_deluxe_reloaded.Player;
import tictactoe_2_0_deluxe_reloaded.PlayerMove;

public class AIPlayerTableLookup extends AIPlayer {

    //---------------------------------------------------------
    // Class variables
    //---------------------------------------------------------

    private final Coordinates[] preferredMoves = {
        new Coordinates(1,1), new Coordinates(0,0), new Coordinates(0,2), 
        new Coordinates(2,0), new Coordinates(2,2), new Coordinates(0,1), 
        new Coordinates(1,0), new Coordinates(1,2), new Coordinates(2,1)
    };

    //---------------------------------------------------------
    // Constructor
    //---------------------------------------------------------

    public AIPlayerTableLookup(Player.PlayerType type, Board board) {
        super(type, board);
    }

    //---------------------------------------------------------
    // Move making methods
    //---------------------------------------------------------

    @Override
    public PlayerMove makeMoveInSection(Coordinates section) {
        
        if(section == null) {
            for (Coordinates newSection : preferredMoves) {
                if(this.board.isPlayableSection(newSection)) {
                    section = newSection;
                    break;
                }
            }
        }
        
        Coordinates moveSquare = null;
        
        for (Coordinates square : preferredMoves) {
            if(this.board.isPlayableSquareInSection(square, section)) {
                moveSquare = square;
                break;
            }
        }

        if(section != null && moveSquare != null) {
            return new PlayerMove(section, moveSquare, this.type);
        } else {
            assert false : "No empty cell?!";
            return null;
        }
    }
}
