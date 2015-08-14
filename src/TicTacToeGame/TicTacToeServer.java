package TicTacToeGame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TicTacToeServer {
    public static void main(String[] args) throws Exception {
        ServerSocket listener = new ServerSocket(12345);
        System.out.println("Tic Tac Toe Server is Running");
        try {
            while (true) {
                Game game = new Game();
                Game.Player playerX = game.new Player(listener.accept(), 'X');
                Game.Player playerO = game.new Player(listener.accept(), 'O');
                playerX.setOpponent(playerO);
                playerO.setOpponent(playerX);
                game.currentPlayer = playerX;
                playerX.start();
                playerO.start();
            }
        } finally {
            listener.close();
        }
    }
}

class Game {
    private Player[] board = {
        null, null, null,
        null, null, null,
        null, null, null};

    Player currentPlayer;

    public boolean hasWinner() {
        return
            (board[0] != null && board[0] == board[1] && board[0] == board[2])
          ||(board[3] != null && board[3] == board[4] && board[3] == board[5])
          ||(board[6] != null && board[6] == board[7] && board[6] == board[8])
          ||(board[0] != null && board[0] == board[3] && board[0] == board[6])
          ||(board[1] != null && board[1] == board[4] && board[1] == board[7])
          ||(board[2] != null && board[2] == board[5] && board[2] == board[8])
          ||(board[0] != null && board[0] == board[4] && board[0] == board[8])
          ||(board[2] != null && board[2] == board[4] && board[2] == board[6]);
    }

    public boolean boardFilledUp() {
        for (int i = 0; i < board.length; i++) {
            if (board[i] == null) {
                return false;
            }
        }
        return true;
    }

    public synchronized boolean legalMove(int location, Player player) {
        if (player == currentPlayer && board[location] == null) {
            board[location] = currentPlayer;
            currentPlayer = currentPlayer.opponent;
            currentPlayer.otherPlayerMoved(location);
            return true;
        }
        return false;
    }

    class Player extends Thread {
        char mark;
        Player opponent;
        Socket socket;
        BufferedReader input;
        PrintWriter output;

        public Player(Socket socket, char mark) {
            this.socket = socket;
            this.mark = mark;
            try {
                input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);
                output.println("WELCOME " + mark);
                output.println("MESSAGE Waiting for opponent to connect");
            } catch (IOException e) {
                System.out.println("Player died: " + e);
            }
        }

        public void setOpponent(Player opponent) {
            this.opponent = opponent;
        }

        public void otherPlayerMoved(int location) {
            output.println("OPPONENT_MOVED " + location);
            output.println(
                hasWinner() ? "DEFEAT" : boardFilledUp() ? "TIE" : "");
        }
        
        public void saveToDB(String x, String statusX, String o, String statusO)
        {
        	   final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
        	   final String DB_URL = "jdbc:mysql://localhost/tictactoe";

        	   final String USER = "root";
        	   final String PASS = "";
        	   
        	   Connection conn = null;
        	   Statement stmt = null;
        	   try{
        	      Class.forName("com.mysql.jdbc.Driver");

        	      System.out.println("Connecting to database...");
        	      conn = DriverManager.getConnection(DB_URL,USER,PASS);

        	      System.out.println("Creating statement...");
        	      stmt = conn.createStatement();
        	      String sql;
        	      
        	      sql = "INSERT INTO statistic (player_x,player_o) VALUES (\"" +statusX+ "\",\"" +statusO+"\")";
        	      stmt.executeUpdate(sql);

        	      stmt.close();
        	      conn.close();
        	   }catch(SQLException se){
        	      //Handle errors for JDBC
        	      se.printStackTrace();
        	   }catch(Exception e){
        	      //Handle errors for Class.forName
        	      e.printStackTrace();
        	   }finally{
        	      //finally block used to close resources
        	      try{
        	         if(stmt!=null)
        	            stmt.close();
        	      }catch(SQLException se2){
        	      }// nothing we can do
        	      try{
        	         if(conn!=null)
        	            conn.close();
        	      }catch(SQLException se){
        	         se.printStackTrace();
        	      }//end finally try
        	   }//end try
        	   System.out.println("Goodbye!");
        }


        public void run() {
            try {
                output.println("MESSAGE All players connected");
                int count = 0;

                if (mark == 'X') {
                    output.println("MESSAGE Your move");
                }

                while (true) {
                    String command = input.readLine();
                    if (command.startsWith("MOVE")) {
                    	count++;
                        int location = Integer.parseInt(command.substring(5));
                        if (legalMove(location, this)) {
                            output.println("VALID_MOVE");
                            if(hasWinner())
                            {
                            	output.println("VICTORY");
                            	if(count%2==0)
                            	{
                            		saveToDB("x", "lose", "o", "win");
                            	}
                            	else
                            	{
                            		saveToDB("x", "win", "o", "lose");
                            	}	
                            }
                            else if(boardFilledUp())
                            {
                            	output.println("TIE");
                            	saveToDB("x", "tie", "o", "tie");
                            }
//                            output.println(hasWinner() ? "VICTORY"
//                                         : boardFilledUp() ? "TIE"
//                                         : "");
                        } else {
                            output.println("MESSAGE ?");
                        }
                    } else if (command.startsWith("QUIT")) {
                        return;
                    }
                }
            } catch (IOException e) {
                System.out.println("Player died: " + e);
            } finally {
                try {socket.close();} catch (IOException e) {}
            }
        }
    }


}
