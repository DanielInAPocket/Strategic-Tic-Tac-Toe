
package tictactoe_2_0_deluxe_reloaded.AIPlayer;

import tictactoe_2_0_deluxe_reloaded.Board;
import tictactoe_2_0_deluxe_reloaded.Coordinates;
import tictactoe_2_0_deluxe_reloaded.Player;
import tictactoe_2_0_deluxe_reloaded.PlayerMove;
import tictactoe_2_0_deluxe_reloaded.Player.PlayerType;

public abstract class AIPlayer extends Player {
    
    //---------------------------------------------------------
    // Class variables
    //---------------------------------------------------------

    protected final Board board;

    protected final PlayerType oppType;

    //---------------------------------------------------------
    // Constructor
    //---------------------------------------------------------

    public AIPlayer(PlayerType type, Board board) {
        
        super(type);
        this.oppType = (type == PlayerType.X) ? PlayerType.O : PlayerType.X;
        
        this.board = board;
    }
    
    //---------------------------------------------------------
    // Move making methods
    //---------------------------------------------------------

    @Override
    public abstract PlayerMove makeMoveInSection(Coordinates section);
}
