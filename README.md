# Strategic Tic-Tac-Toe
Strategic Tic-Tac-Toe is a two player game where you have to win a standard game of Tic-Tac-Toe to place your token on the global gaming board. Game was coded as part of university studies.

# Features
- Local 2-player game, when game is played on same PC and players take turns
- Host / Join multiplayer. Game uses sockets and for players to be able to connect to each other they need to know IP of the host. Host is listening on 9999 port.
- Local game vs AI player. AI player have several levels of difficulty, courtesy of using different algorithms:
  - So called "Idiot CPU" utilizes lookup table with predefined set of moves that are ordered per "most-optimal" value move (it is always best to place your token in center, then in corners and so on).
  - Easy, medium and hard CPU opponents utilize MinMax algorithm with different depth value.
- Game has console-only user interface as main concern of the task was the code itself :)
