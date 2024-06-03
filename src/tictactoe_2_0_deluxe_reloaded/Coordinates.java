
package tictactoe_2_0_deluxe_reloaded;

import java.io.Serializable;

public class Coordinates implements Serializable {
    
    //---------------------------------------------------------
    // Class variables
    //---------------------------------------------------------

    public int row;
    public int column;
    
    //---------------------------------------------------------
    // Constructor
    //---------------------------------------------------------

    public Coordinates(int row, int column) {
        this.row = row;
        this.column = column;
    }
}
