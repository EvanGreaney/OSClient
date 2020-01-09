
IMPORT JAVA.IO.IOEXCEPTION;
IMPORT JAVA.IO.OBJECTINPUTSTREAM;
IMPORT JAVA.IO.OBJECTOUTPUTSTREAM;
IMPORT JAVA.NET.*;
IMPORT JAVA.UTIL.ARRAYLIST;

PUBLIC CLASS SERVER {

	PUBLIC STATIC VOID MAIN(STRING[] ARGS) {
		// TODO AUTO-GENERATED METHOD STUB

		SERVERSOCKET LISTENER;
		INT CLIENTID = 0;
		TRY {
			LISTENER = NEW SERVERSOCKET(10000, 10);

			WHILE (TRUE) {
				SYSTEM.OUT.PRINTLN("MAIN THREAD LISTENING FOR INCOMING NEW CONNECTIONS");
				SOCKET NEWCONNECTION = LISTENER.ACCEPT();

				SYSTEM.OUT.PRINTLN("NEW CONNECTION RECEIVED AND SPANNING A THREAD");
				CONNECTHANDLER T = NEW CONNECTHANDLER(NEWCONNECTION, CLIENTID);
				CLIENTID++;
				T.START();
			}

		}

		CATCH (IOEXCEPTION E) {
			SYSTEM.OUT.PRINTLN("SOCKET NOT OPENED");
			// TODO AUTO-GENERATED CATCH BLOCK
			E.PRINTSTACKTRACE();
		}
	}

}

CLASS CONNECTHANDLER EXTENDS THREAD {

	SOCKET INDIVIDUALCONNECTION;
	INT SOCKETID;
	OBJECTOUTPUTSTREAM OUT;
	OBJECTINPUTSTREAM IN;
	STRING MESSAGE;

	INT AGENTCHOICE;
	INT IDCHECK;
	ARRAYLIST<PLAYER> PLAYERS = NEW ARRAYLIST<>();
	ARRAYLIST<USERS> USER = NEW ARRAYLIST<>();
	ARRAYLIST<CLUB> CLUBREG = NEW ARRAYLIST<>();

	BOOLEAN FOUNDUSER = FALSE;
	BOOLEAN FOUNDPASS = FALSE;
	STRING PASS;
	STRING NAME;

	PUBLIC CONNECTHANDLER(SOCKET S, INT I) {
		INDIVIDUALCONNECTION = S;
		SOCKETID = I;
	}

	VOID SENDMESSAGE(STRING MSG) {
		TRY {
			OUT.WRITEOBJECT(MSG);
			OUT.FLUSH();
			SYSTEM.OUT.PRINTLN("CLIENT>" + MSG);
		} CATCH (IOEXCEPTION IOEXCEPTION) {
			IOEXCEPTION.PRINTSTACKTRACE();
		}
	}

	PUBLIC VOID RUN() {

		TRY {

			OUT = NEW OBJECTOUTPUTSTREAM(INDIVIDUALCONNECTION.GETOUTPUTSTREAM());
			OUT.FLUSH();
			IN = NEW OBJECTINPUTSTREAM(INDIVIDUALCONNECTION.GETINPUTSTREAM());
			SYSTEM.OUT.PRINTLN("CONNECTION" + SOCKETID + " FROM IP ADDRESS " + INDIVIDUALCONNECTION.GETINETADDRESS());

			// COMMENCE
			DO {
				SENDMESSAGE(
						"\NPLEASE ENTER 1 IF YOU ARE AN AGENT" + "\NENTER 2 IF YOU ARE A CLUB\N" + "PRESS 3 TO EXIT\N");
				MESSAGE = (STRING) IN.READOBJECT();

				IF (MESSAGE.EQUALS("1")) {

					DO {
						SENDMESSAGE("\NPRESS 1 TO REGISTER" + "\NPRESS 2 TO LOG-IN\N"
								+ "PRESS 404 TO EXIT\N");
						MESSAGE = (STRING) IN.READOBJECT();

						IF (MESSAGE.EQUALS("1")) {

							AGENTREGISTER();
							
						}

						IF (MESSAGE.EQUALS("2")) {

							AGENTLOGIN();

						}

						IF (FOUNDUSER == TRUE && FOUNDPASS == TRUE) {
							SENDMESSAGE("LOGIN SUCCESFULL!!\N");

							DO {

								SENDMESSAGE("\NPRESS 1 TO ADD A PLAYER" + "\NPRESS 2 TO UPDATE A PLAYER'S VALUATION"
										+ "\NPRESS 3 TO UPDATE A PLAYERS STATUS" + "PRESS 4 TO PRINT OUT ALL PLAYERS\N"
												+ "\NPRESS 404 TO EXIT\N");
								MESSAGE = (STRING) IN.READOBJECT();
								AGENTCHOICE = INTEGER.PARSEINT(MESSAGE);

								// ADD A PLAYER
								IF (AGENTCHOICE == 1) {

									ADDPLAYER();

								} ELSE IF (AGENTCHOICE == 2) {
									
									UPDATEVALUATION();

								} ELSE IF (AGENTCHOICE == 3) {
									
									UPDATESTATUS();

								}ELSE IF (AGENTCHOICE == 4) {
									
									PRINTALL();

								} ELSE {
									SENDMESSAGE("\NSELECT ONE OF THE ABOVE OPTIONS!!!: ");
								}

							} WHILE (!MESSAGE.EQUALS("404"));

						} ELSE {
							SENDMESSAGE("THE LOGIN HAS FAILED!!\N");
						} // DELETE

					} WHILE (!MESSAGE.EQUALS("404"));

				} ELSE IF (MESSAGE.EQUALS("2")) {
					DO {
						SENDMESSAGE("\NPRESS 1 TO REGISTER" + "\NPRESS 2 TO LOG-IN\N");
						MESSAGE = (STRING) IN.READOBJECT();

						IF (MESSAGE.EQUALS("1")) {

							CLUBREGISTER();
							
						}

						IF (MESSAGE.EQUALS("2")) {

							CLUBLOGIN();

						}

						IF (FOUNDUSER == TRUE && FOUNDPASS == TRUE) {
							SENDMESSAGE("SUCCESFULL!!\N");

							DO {

								SENDMESSAGE("\NPRESS 1 TO SEARCH FOR A PLAYER BY POSITION"
										+ "\NPRESS 2 TO SEARCH FOR A PLAYER FOR SALE"
										+ "\NPRESS 3 TO SUSPEND/ RESUME SALE OF A PLAYER\N"
										+ "\NPRESS 4 PURCHASE A PLAYER\N" + "PRESS 404 TO EXIT\N");
								MESSAGE = (STRING) IN.READOBJECT();

								// ADD A PLAYER
								IF (MESSAGE.EQUALS("1")) {

									POSSEARCH();
									
								} ELSE IF (MESSAGE.EQUALS("2")) {

									PLAYERSALE();

								} ELSE IF (MESSAGE.EQUALS("3")) {

									SUSPENDPLAYER();

								}ELSE IF(MESSAGE.EQUALS("4"))
								{
									BUYPLAYER();
								}

							} WHILE (!MESSAGE.EQUALS("404"));

						} ELSE {
							SENDMESSAGE("THE LOGIN HAS FAILED!!\N");
						} // DELETE
					} WHILE (!MESSAGE.EQUALS("404"));

				} ELSE {
					SENDMESSAGE("\NSELECT ONE OF THE ABOVE OPTIONS!!!: ");
				}

			} WHILE (!MESSAGE.EQUALS("3"));

			SENDMESSAGE("\NSERVER CLIENT FINISHED!!\N");

		} CATCH (

		IOEXCEPTION E) {
			// TODO AUTO-GENERATED CATCH BLOCK
			E.PRINTSTACKTRACE();
		} CATCH (CLASSNOTFOUNDEXCEPTION E) {
			// TODO AUTO-GENERATED CATCH BLOCK
			E.PRINTSTACKTRACE();
		}

		FINALLY {
			TRY {
				OUT.CLOSE();
				IN.CLOSE();
				INDIVIDUALCONNECTION.CLOSE();
			}

			CATCH (IOEXCEPTION E) {
				// TODO AUTO-GENERATED CATCH BLOCK
				E.PRINTSTACKTRACE();
			}
		}

	}
	
	//METHODS
	PUBLIC VOID AGENTREGISTER()
	{
		TRY {
			USERS USERS = NEW USERS();

			SENDMESSAGE("\NTHE AGENT WOULD LIKE TO REGISTER!!\N");

			SENDMESSAGE("PLEASE ENTER THE AGENT NAME: ");
			USERS.SETAGENTNAME((STRING) IN.READOBJECT());

			SENDMESSAGE("PLEASE ENTER THE AGENT ID: ");
			USERS.SETAGENTID((STRING) IN.READOBJECT());

			SENDMESSAGE("PLEASE ENTER THE AGENT EMAIL: ");
			USERS.SETEMAIL((STRING) IN.READOBJECT());

			USER.ADD(USERS);
		} CATCH (EXCEPTION E) {
			// TODO: HANDLE EXCEPTION
		}
	}
	
	public void agentLogin()
	{
		try {
			sendMessage("\nThe Agent would like to Log-in!!\n");

			sendMessage("Please Enter the Agent Name: ");
			String userName = (String) in.readObject();

			sendMessage("Please Enter the Agent Id: ");
			String password = (String) in.readObject();

			for (Users u : uclubser) {

				if (password.equalsIgnoreCase(u.getAgentId())
						&& userName.equalsIgnoreCase(u.getAgentName())) {

					System.out.println("user found");
					foundPass = true;
					foundUser = true;

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void clubRegister()
	{
		try {
			Club club = new Club();

			sendMessage("\nThe Club would like to Register!!\n");

			sendMessage("Please Enter the Club Name: ");
			club.setClubName((String) in.readObject());

			sendMessage("Please Enter the Club Id: ");
			club.setClubId((String) in.readObject());

			sendMessage("Please Enter the Club Email: ");
			club.setEmail((String) in.readObject());

			sendMessage("Please Enter the Club Funds: ");
			club.setFunds((String) in.readObject());

			clubReg.add(club);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void clubLogin()
	{
		try {
			sendMessage("\nThe Club would like to Log-in!!\n");

			sendMessage("Please Enter the Club Name: ");
			String userName = (String) in.readObject();

			// sendMessage("Please Enter the Club Id: ");
			// String password = (String) in.readObject();

			for (Club c : clubReg) {

				if (userName.equals(c.getClubName())) {

					System.out.println("user found");
					foundPass = true;
					foundUser = true;

				} else {
					System.out.println("user not found");
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void addPlayer()
	{
		try
		{
			Player player = new Player();
	
			sendMessage("\nPlease enter the player name: ");
			player.setName((String) in.readObject());
	
			sendMessage("\nPlease enter the player age: ");
			player.setAge(Integer.parseInt((String) in.readObject()));
	
			sendMessage("\nPlease enter the player id: ");
			player.setPlayerId(Integer.parseInt((String) in.readObject()));
	
			sendMessage("\nPlease enter the club id: ");
			player.setClubId(Integer.parseInt((String) in.readObject()));
	
			sendMessage("\nPlease enter the agent id: ");
			player.setAgentId(Integer.parseInt((String) in.readObject()));
	
			sendMessage("\nPlease enter the player valuation: ");
			player.setValue(Double.parseDouble((String) in.readObject()));
	
			sendMessage("\nPlease enter the player status: ");
			player.setStatus((String) in.readObject());
	
			sendMessage("\nPlease enter the player position: ");
			player.setPosition((String) in.readObject());
	
			// add the player to the list
	
			players.add(player);
	
			sendMessage(String.valueOf(players.size()));
	
			for (Player p : players) {
				sendMessage("\nName: " + p.getName() + "\nAge: " + p.getAge() + "\nPlayer Id: "
						+ p.getPlayerId() + "\nClub Id: " + p.getClubId() + "\nAgent Id: "
						+ p.getAgentId() + "\nValuation: " + p.getValue() + "\nStatus: "
						+ p.getStatus() + "\nPosition: " + p.getPosition());
			}
	}catch (Exception e) {
		// TODO: handle exception
	}

}

	public void updateValuation()
	{
		try {
			sendMessage("Please enter the Id of the player you would like to update: ");
			message = (String) in.readObject();
			idCheck = Integer.parseInt(message);
			String s = "Not Found";

			for (Player p : players) {
				if (idCheck == p.getPlayerId()) {
					sendMessage("Found");
					sendMessage("\nPlease enter the new player valuation: ");
					p.setValue(Double.parseDouble((String) in.readObject()));
					s = "The Value has been updated to " + p.getValue();
				}
			}

			sendMessage(s);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void updateStatus()
	{
		try {
			sendMessage("Please enter the Id of the player you would like to update: ");
			message = (String) in.readObject();
			idCheck = Integer.parseInt(message);
			String s = "Not Found";

			for (Player p : players) {
				if (idCheck == p.getPlayerId()) {
					sendMessage("Found");
					sendMessage("\nPlease enter the new player Status: ");
					p.setStatus((String) in.readObject());
					s = "The Status has been updated to " + p.getStatus();
				}
			}

			sendMessage(s);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void posSearch()
	{
		try {
			sendMessage("Please enter the position of the player!");
			String position = (String) in.readObject();

			for (Player p : players) {
				if (position.equals(p.getPosition())) {

					sendMessage("\nName: " + p.getName() + "\nAge: " + p.getAge()
							+ "\nPlayer Id: " + p.getPlayerId() + "\nClub Id: " + p.getClubId()
							+ "\nAgent Id: " + p.getAgentId() + "\nValuation: " + p.getValue()
							+ "\nStatus: " + p.getStatus() + "\nPosition: " + p.getPosition());

				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void playerSale()
	{
		try {
			sendMessage("Please enter the Club Id of the player!");
			String clubId = (String) in.readObject();

			for (Player p : players) {
				if (clubId.equals(p.getClubId())) {

					sendMessage("\nName: " + p.getName() + "\nAge: " + p.getAge()
							+ "\nPlayer Id: " + p.getPlayerId() + "\nClub Id: " + p.getClubId()
							+ "\nAgent Id: " + p.getAgentId() + "\nValuation: " + p.getValue()
							+ "\nStatus: " + p.getStatus() + "\nPosition: " + p.getPosition());

				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void suspendPlayer()
	{
		try {
			sendMessage("Please enter the player Id you would like to suspend/resume!");
			String id = (String) in.readObject();

			for (Player p : players) {
				if (id.equals(p.getStatus())) {

					sendMessage("\nPlease enter the new player Status: ");
					p.setStatus((String) in.readObject());
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void buyPlayer()
	{
		try {
			sendMessage("Please enter the player Id you would like to buy!");
			String id = (String) in.readObject();

			for (Player p : players) {
				if (id.equals(p.getPlayerId())) {

					sendMessage("\nName: " + p.getName() + "\nAge: " + p.getAge()
					+ "\nPlayer Id: " + p.getPlayerId() + "\nClub Id: " + p.getClubId()
					+ "\nAgent Id: " + p.getAgentId() + "\nValuation: " + p.getValue()
					+ "\nStatus: " + p.getStatus() + "\nPosition: " + p.getPosition());

				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void printAll()
	{
		sendMessage(String.valueOf(players.size()));
		
		for (Player p : players) {
			sendMessage("\nName: " + p.getName() + "\nAge: " + p.getAge() + "\nPlayer Id: "
					+ p.getPlayerId() + "\nClub Id: " + p.getClubId() + "\nAgent Id: "
					+ p.getAgentId() + "\nValuation: " + p.getValue() + "\nStatus: "
					+ p.getStatus() + "\nPosition: " + p.getPosition());
		}
	}
}

