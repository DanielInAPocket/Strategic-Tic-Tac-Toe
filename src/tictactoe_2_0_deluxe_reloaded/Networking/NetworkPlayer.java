
package tictactoe_2_0_deluxe_reloaded.Networking;

import java.util.logging.Level;
import java.util.logging.Logger;
import tictactoe_2_0_deluxe_reloaded.Coordinates;
import tictactoe_2_0_deluxe_reloaded.Player;
import tictactoe_2_0_deluxe_reloaded.PlayerMove;

public class NetworkPlayer extends Player {
    
    private final Network network;

    public NetworkPlayer(PlayerType type, Network network) {
        super(type);
        this.network = network;
    }
    
    @Override
    public PlayerMove makeMoveInSection(Coordinates section) {
        PlayerMove move = null;
        
        try {
            move = network.readFromNetworkPlayer();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(NetworkPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return move;
    }
}
