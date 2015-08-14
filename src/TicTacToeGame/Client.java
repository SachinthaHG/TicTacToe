package TicTacToeGame;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Client {
    private static int PORT = 12345;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Client(String serverAddress) throws Exception {

        // Setup networking
        socket = new Socket(serverAddress, PORT);
        in = new BufferedReader(new InputStreamReader(
            socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

       
        
    }

    public void play() throws Exception {
        String response;
        try {
            response = in.readLine();
        	int[] board = new int[]{0, 0 ,0, 0, 0, 0, 0, 0, 0};
        	int count = 0;
            
            while (true) 
            {
                response = in.readLine();
                System.out.println(response);
                
                if (response.startsWith("OPPONENT_MOVED"))
                {
                	count++;
                	board[Integer.parseInt(response.substring(15))] = -1;
                	
                	int location;
                	
                	if(count == 1)
                	{
                		if(board[4] == 0)
                		{
	                		location = 4;
                		}
                		else
                		{
                			if(Integer.parseInt(response.substring(15))<6)
	                		{
	                			location = Integer.parseInt(response.substring(15)) + 3;
	                		}
	                		else
	                		{
	                			location = Integer.parseInt(response.substring(15)) - 3;
	                		}
                		}
                	}
                	
                	
                	else if(((board[0]==1 && board[1]==1) || (board[5]==1 && board[8]==1) || (board[4]==1 && board[6]==1)) && board[2]==0)
                	{
                		location = 2;
                	}
                	else if(((board[6]==1 && board[7]==1) || (board[5]==1 && board[2]==1) || (board[4]==1 && board[0]==1)) && board[8]==0)
                	{                		
                		location = 8;
                	}
                	else if(((board[1]==1 && board[2]==1) || (board[3]==1 && board[6]==1) || (board[4]==1 && board[8]==1)) && board[0]==0)
                	{
                		location = 0;
                	}
                	else if(((board[7]==1 && board[8]==1) || (board[3]==1 && board[0]==1) || (board[4]==1 && board[2]==1)) && board[6]==0)
                	{
                		location = 6;
                	}
                	else if(((board[3]==1 && board[4]==1) || (board[2]==1 && board[8]==1))&& board[5]==0)
                	{
                		
                		location = 5;
                	}
                	else if(((board[5]==1 && board[4]==1) || (board[0]==1 && board[6]==1))&& board[3]==0)
                	{
                		
                		location = 3;
                	}
                	else if(((board[1]==1 && board[4]==1) || (board[6]==1 && board[8]==1))&& board[7]==0)
                	{
                		
                		location = 7;
                	}
                	else if(((board[4]==1 && board[7]==1) || (board[0]==1 && board[2]==1)) && board[7]==0)
                	{
                		
                		location = 1;
                	}
                	else if(((board[0]==1 && board[8]==1) || (board[2]==1 && board[6]==1) || (board[1]==1 && board[7]==1) || (board[3]==1 && board[5]==1)) && board[4]==0)
                	{
                		location = 4;
                	}
                	
                	
                	
                	
                	
                	
                	else if(((board[0]==-1 && board[1]==-1) || (board[5]==-1 && board[8]==-1) || (board[4]==-1 && board[6]==-1)) && board[2]==0)
                	{
                		location = 2;
                	}
                	else if(((board[6]==-1 && board[7]==-1) || (board[5]==-1 && board[2]==-1) || (board[4]==-1 && board[0]==-1)) && board[8]==0)
                	{                		
                		location = 8;
                	}
                	else if(((board[1]==-1 && board[2]==-1) || (board[3]==-1 && board[6]==-1) || (board[4]==-1 && board[8]==-1)) && board[0]==0)
                	{
                		location = 0;
                	}
                	else if(((board[7]==-1 && board[8]==-1) || (board[3]==-1 && board[0]==-1) || (board[4]==-1 && board[2]==-1)) && board[6]==0)
                	{
                		location = 6;
                	}
                	else if(((board[3]==-1 && board[4]==-1) || (board[2]==-1 && board[8]==-1))&& board[5]==0)
                	{
                		
                		location = 5;
                	}
                	else if(((board[5]==-1 && board[4]==-1) || (board[0]==-1 && board[6]==-1))&& board[3]==0)
                	{
                		
                		location = 3;
                	}
                	else if(((board[1]==-1 && board[4]==-1) || (board[6]==-1 && board[8]==-1))&& board[7]==0)
                	{
                		
                		location = 7;
                	}
                	else if(((board[4]==-1 && board[7]==-1) || (board[0]==-1 && board[2]==-1)) && board[7]==0)
                	{
                		
                		location = 1;
                	}
                	else if(((board[0]==-1 && board[8]==-1) || (board[2]==-1 && board[6]==-1) || (board[1]==-1 && board[7]==-1) || (board[3]==-1 && board[5]==-1)) && board[4]==0)
                	{
                		location = 4;
                	}
                	else
                	{
                		Random randomGenerator = new Random();
                		ArrayList<Integer> availableSlots = new ArrayList<Integer>();
                		for(int i=0; i<9; i++)
                		{
                			if(board[i] == 0)
                			{
                				availableSlots.add(i);
                			}
                		}
                		if(!availableSlots.isEmpty())
                			location = availableSlots.get(randomGenerator.nextInt(availableSlots.size()));
                		else
                			location = -10;
                	}
                	if(location != -10)
                	{
                		board[location] = 1;
                    	out.println("MOVE " + location);
                	}
                	
                }
                else if (response.startsWith("VICTORY")) {
                    break;
                } 
                else if (response.startsWith("DEFEAT")) {
                    break;
                } 
                else if (response.startsWith("TIE")) {
                    break;
                } 
            }
            out.println("QUIT");
        }
        finally
        {
            socket.close();
        }
    }

  
    public static void main(String[] args) throws Exception {
        while (true) {
            String serverAddress = (args.length == 0) ? "localhost" : args[0];
            TicTacToeComputer client = new TicTacToeComputer(serverAddress);
            client.play();
           
        }
    }


}
