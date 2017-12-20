# Final Project Milestone 2

## Overview: 
### System Architecture
* Using the common API as the message passing infrastructure, we implemented our game following the API guidelines and communication protocols.
* Our game are based on the server-client architecture, which are implemented as a set of customized commands in the server side.
* The communication between various of commands are done using ICmd2ModelAdaper.put() and get() method via a common local data storage.
* The communication from the client back to server are done using ICmd2ModelAdaper.sendTo() method, with the target recipient being the server.
* The communication within the team (intra-team communication) are done using ICRCmd2ModelAdaper.sendToChatRoom() method, i.e. a non-routing way.
* The communication between different teams (inter-team communication) are done by sending the message back to the server.

## Start the program
1. Start the client
 1.1 Run "FinalProject/GameClient.launch" to start the client;
 1.2 enter desired username; click "Start" button to initiate the client.
 
2. Start the server 
 2.1 Run "FinalProject/GameServer.launch" to start the server.
 
3. Client connect to server
 On game client side, input the Server's IP address in "Remote IP" and click "Connect to" to connect to the Game Server.
 
4. Game Installation
 4.1 Team assignment
 	Team assignment is auto-done when clients connected to the server.
 4.2 Game installation
 	On the game server side, click "Send Sengoku Game" to send the sengoku game to all players; click "Send Game" to send the demo game to all players.

5. Game user manual (Two Games)
	
	5.1 Demo game (Guess the number)
	
	Note : Demo game is a game we used to test our API, which is included in the milestone one and is not a part of our final game.
		* In the client game GUI, Input your guess on the text field and click "guess" to make a guess. 
		* The correct answer is 50. 
		* Server will notify within you team (you can check this if you carry out Step 3) "10 too small!" if your guess is 10 which is smaller than 50
		  and "70 too big!" if your guess is 70 which is greater than 50.
		* Server will nofity the win/lose message among all teams -- you'll either receive "50 Bingo! You have won the game!" or "You lose the game!".
	
	5.2 Sengoku Game
	
	### Back ground
	* This is a real-time-strategy (RTS) game.
	* Each team is a Kingdom consisting of several war lords (team members). 
	* While belonging to the same country, war lords are independent from each other in making their own game decision to optimize the Kingdom’s overall population growth and land conquering. 
		* There dozens of cities in the map, NYC, LA, Houston, etc.
		* At the beginning of the game, each player (war lord) is assigned to a random city with 500 soldiers. Other cities are occupied by creeps (controlled by computer).
		* war lords can take ACTION with their armed forces! 
		* Each action includes: 
			* 1. selecting a base city -- in which you MUST have armed forces)
			* 2. selecting a target city -- which MUST to be within your attacking/moving range. In the game, the range is 1000 km and is marked with a red circle on the map.
			* 3. selecting the number of soldiers to participate in the action.
			* 4. confirm your action.
	* If the target city belongs to your own Kingdom, then your are MIGRATING your army from base city to target city.
	* else if the target city belongs to the enemy (another team or creep), you are Attacking the target city.
	* Battles occurs when you attack the city. You may lose all your dispatched soldiers if you fail the battle, otherwise you will conquer the city.
	* The game ends when a team conquers all the cities.
	
	
	### Detailed Instructions on game Play
	* Team assignment is automatic. When the game start you'll see your base city
	* In the top drop down list, choose your base city (Or left click your base city in the map), a red circle will appears as your attacking(moving) range;
	* In the drop down list below, choose your target city (Or right click your destination in the map), enter the number of soldiers to move in the text field,
	click "Take action" button to move your army from the base to the destination.
	
	### Communication
	In the text field below the map, enter your message and press "Enter", the message will be sent to your team; 
	press "Shift + Enter", the message will be sent to every player in the game. 
	The messages received from other players will be displayed in the left text panel, with the message for every player being red.
	
	### Game end
	The game will end when a team has conquered every city in the map. A notification will appear in the game GUI for every player.
	
	### Exit the game
	Click the Cross to exit the whole program, note that your exiting the game will not affect other players.
	
	
	
	
	
	