
package tictactoe_2_0_deluxe_reloaded;

import tictactoe_2_0_deluxe_reloaded.AIPlayer.AIPlayerMinimax;
import tictactoe_2_0_deluxe_reloaded.AIPlayer.AIPlayerTableLookup;
import tictactoe_2_0_deluxe_reloaded.Board.BoardState;
import tictactoe_2_0_deluxe_reloaded.Networking.Network;
import tictactoe_2_0_deluxe_reloaded.Networking.NetworkPlayer;
import tictactoe_2_0_deluxe_reloaded.Player.PlayerType;

public class Game {
    
    //---------------------------------------------------------
    // Constants and Enumurations
    //---------------------------------------------------------

    public static final int PLAYER_COUNT = 2;

    public enum OpponentType {
        LocalHuman, 
        NetworkHumanHost,
        NetworkHumanClient,
        IdiotCPU,
        EasyCPU,
        MediumCPU,
        HardCPU
    }
    
    public enum GameState {
        Playing, 
        X_Won,
        O_Won,
        Tie;
        
        public static GameState fromBoardState(BoardState state) {
            switch(state) {
            case Playing:
                return Playing;
            case X_Won:
                return X_Won;
            case O_Won:
                return O_Won;
            case Tie:
                return Tie;
            }
            return null;
        }
    }
    
    //---------------------------------------------------------
    // Class variables
    //---------------------------------------------------------

    private GameState state;
    private final Board board;
    private final Player[] players = new Player[PLAYER_COUNT];
    private Player currentPlayer;
    private Network network;

    //---------------------------------------------------------
    // Constructor
    //---------------------------------------------------------

    public Game(OpponentType opponent) {
        this.state = GameState.Playing;
        
        this.board = new Board();
        
        
        switch (opponent) {
            case LocalHuman:
                this.players[0] = new Player(PlayerType.X);
                this.players[1] = new Player(PlayerType.O);
                break;
            case NetworkHumanHost:
                this.network = new Network(true);
                this.players[0] = new Player(PlayerType.X);
                this.players[1] = new NetworkPlayer(PlayerType.O, this.network);
                break;
            case NetworkHumanClient:
                this.network = new Network(false);
                this.players[0] = new NetworkPlayer(PlayerType.X, this.network);
                this.players[1] = new Player(PlayerType.O);
                break;
            case IdiotCPU:
                this.players[0] = new Player(PlayerType.X);
                this.players[1] = new AIPlayerTableLookup(PlayerType.O, this.board);
                break;
            case EasyCPU:
                this.players[0] = new Player(PlayerType.X);
                this.players[1] = new AIPlayerMinimax(PlayerType.O, this.board, 2);
                break;
            case MediumCPU:
                this.players[0] = new Player(PlayerType.X);
                this.players[1] = new AIPlayerMinimax(PlayerType.O, this.board, 4);
                break;
            case HardCPU:
                this.players[0] = new Player(PlayerType.X);
                this.players[1] = new AIPlayerMinimax(PlayerType.O, this.board, 8);
                break;
        }

        this.currentPlayer = players[0];
    }
    
    //---------------------------------------------------------
    // General purpose methods
    //---------------------------------------------------------

    public void play() {
        
        this.drawGameState();

        do {
            this.askForPlayerMove(this.currentPlayer);
            this.state = GameState.fromBoardState(this.board.getGlobalBoardState());
            this.changePlayer();
            this.drawGameState();
        } while (this.state == GameState.Playing);
        
        if(this.network != null) {
            this.network.closeConnection();
        }
    }
    
    private void drawGameState() {
        
        for(int i = 0; i < 75; i++) {
            System.out.println("");
        }

        if(this.state == GameState.Playing) {
            System.out.println("Current player: " + this.currentPlayer.type.toString());

            Coordinates currentSection = this.board.getCurrentSection();

            String currentSectionRow = (currentSection == null) ? "Undefined" : ""+(currentSection.row + 1)+"";
            System.out.println("Current section row: " + currentSectionRow);

            String currentSectionCol = (currentSection == null) ? "Undefined" : ""+(currentSection.column + 1)+"";
            System.out.println("Current section col: " + currentSectionCol);
        } else {
            switch (this.state) {
                case X_Won:
                    System.out.println("Congratulation! Player X has won the game!");
                    break;
                case O_Won:
                    System.out.println("Congratulation! Player O has won the game!");
                    break;
                default:
                    System.out.println("It's a tie!");
                    break;
            }
        }
    
        String boardState = this.board.printBoard();
        System.out.println(boardState);
    }

    //---------------------------------------------------------
    // Player related methods
    //---------------------------------------------------------

    private void askForPlayerMove(Player player) {
        
        boolean validMove;
        
        Coordinates currentSection = this.board.getCurrentSection();
        
        PlayerMove move = null;
        
        do {
            move = player.makeMoveInSection(currentSection);
            validMove = this.board.applyPlayerMove(move);
        } while (validMove == false);
        
        if(validMove == true && this.network != null) {
            this.network.writeToNetworkPlayer(move);
        }
    }

    private void changePlayer() {
        
        for(int i = 0; i < this.players.length; i++) {
            if(this.players[i] == this.currentPlayer) {
                if(++i == this.players.length) {
                    this.currentPlayer = this.players[0];
                } else {
                    this.currentPlayer = this.players[i++];
                }
                
                break;
            }
        }
    }
 }
