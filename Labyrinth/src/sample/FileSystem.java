package sample;
import java.io.*;
import java.util.Scanner;
import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;

/* Class that hanldes the reading and writing to files associated to:
 * - The Game (readNew, save, load)
 * - The Player Profiles (read, delete, create, updateAll)
 * @Author Alex Warren 851864, Oliver Hart 990839 
 */

public class FileSystem {
	
	/**
	 * Method to read new game
	 * @param selectedMap - the map specified to load 
	 * @return an instance of the Game class instatiated from the selectedMap argument
	 */

	public final static String MAPS = "src\\sample\\Maps\\";
	public final static String LEADERBOARDS = MAPS+"MapsForPlayers\\";
	public final static String localisedDirStruct_PlayerProfile = "src\\sample\\PlayerProfiles\\";
	public final static String localisedDirStruct_SavedGames = MAPS+"SavedMaps\\";
	public final static String localisedDirStruct_MAPS = MAPS+"NewMaps\\";
	public final static String Images = "src\\sample\\IMG\\";

	public static String straightImgPath = Images + "straight-tile.jpg";
	public static String cornerImgPath = Images + "Corner-tile.jpg";
	public static String tshapeImgPath = Images + "T-tile.jpg";
	public static String fireImgPath = Images + "fire_tile.png";
	public static String iceImgPath = Images + "ice_tile.png";
	public static String backtrackImgPath = Images + "backtrack_tile.png";
	public static String doubleMoveImgPath = Images + "double_move_tile.png";

	public FloorTile goalTile;

	{
		try {
			goalTile = new FloorTile(tshapeImgPath, new Image(new FileInputStream(tshapeImgPath)),true,true, true, true, true,"goal");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}


	public static ArrayList<PlayerProfile> AllPlayerProfiles = new ArrayList<PlayerProfile>();

	public static ArrayList<PlayerProfile> getAllPlayerProfiles() {
		File folder = new File(localisedDirStruct_PlayerProfile);
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
			if (file.isFile()) {
				AllPlayerProfiles.add(readPlayerProfile(file.getName().substring(0, file.getName().length()-4)));
				System.out.println("");
			}
		}

		return AllPlayerProfiles;
	}

	public static void addPlayersToLeaderboard(String SelectedMap, Player[] players){
		File leaderboardFile = new File(LEADERBOARDS+SelectedMap.substring(0, SelectedMap.length() - 4)+"_leaderboard.txt");
		if(!leaderboardFile.exists()){
			try {
				leaderboardFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileWriter fileWriterLeaderboard = null;
		try {
			fileWriterLeaderboard = new FileWriter(leaderboardFile,true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedWriter bufferedWriterLeaderboard = new BufferedWriter(fileWriterLeaderboard);
		PrintWriter printWriterLeaderboard = new PrintWriter(bufferedWriterLeaderboard);

		for (Player currentPlayer: players){
			printWriterLeaderboard.println(currentPlayer.getPlayerProfile().getName());
		}
		printWriterLeaderboard.close();
	}

	public static ArrayList<PlayerProfile> getPlayerProfilesFromLeaderboard(String SelectedMap) {
		ArrayList<PlayerProfile> leaderboard = new ArrayList<PlayerProfile>();
		File leaderboardFile = new File(LEADERBOARDS+SelectedMap+"_leaderboard.txt");

		Scanner leaderboardScanner = null;
		try {
			leaderboardScanner = new Scanner(leaderboardFile);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		while(leaderboardScanner.hasNextLine()) {
			String currPlayerProfile = leaderboardScanner.nextLine();
			if(currPlayerProfile != null && !(currPlayerProfile.equals(""))){
				leaderboard.add(readPlayerProfile(currPlayerProfile));
			}
		}

		return leaderboard;
	}

	public static Game readNewGame(String selectedMap, Player[] players) throws FileNotFoundException {
		//game arguments / game data
		List<Player> Players  = new ArrayList<Player>();
		//FloorTile goalTile = new FloorTile(true, true, true, true);
		ArrayList<Tile> silkBag = new ArrayList<Tile>();

		//ArrayList<int[]> playerLocations = new ArrayList<int[]>();

		//board data
		int xSize, ySize;
		int numberOfStraights = 0;
		int numberOfCorners = 0;
		int numberOfTShapes = 0;
		int numberOfFireTiles = 0;
		int numberOfIceTiles = 0;
		int numberOfBacktrackTiles = 0;
		int numberOfDoubleMoveTiles = 0;


		//scanner file start
		File mapData = new File(localisedDirStruct_MAPS + selectedMap );
		Scanner mapScanner = null;
		try {
			mapScanner = new Scanner(mapData);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}


		//read size of board (x,y)
		String boardSizeData = mapScanner.nextLine();
		String[] xySizeData = boardSizeData.split(",");
		xSize = Integer.parseInt(xySizeData[0]);
		ySize = Integer.parseInt(xySizeData[1]);

		System.out.println(xSize);
		System.out.println(ySize);

		FloorTile[][] board = new FloorTile[xSize][ySize];

		//read silk bag contents
		String silkBagData = mapScanner.nextLine();
		String[] silkBagContents = silkBagData.split(",");

		System.out.println(silkBagContents[0]);
		System.out.println(silkBagContents[2]);

		numberOfStraights = Integer.parseInt(silkBagContents[0]);
		numberOfCorners = Integer.parseInt(silkBagContents[1]);
		numberOfTShapes = Integer.parseInt(silkBagContents[2]);
		numberOfFireTiles = Integer.parseInt(silkBagContents[3]);
		numberOfIceTiles = Integer.parseInt(silkBagContents[4]);
		numberOfBacktrackTiles = Integer.parseInt(silkBagContents[5]);
		numberOfDoubleMoveTiles = Integer.parseInt(silkBagContents[6]);



		//read and create silk bag
		//straight

		Random ran = new Random();

		for (int i = 0; i < numberOfStraights; i++) {
			int chanceOfOrientation = ran.nextInt((1 - 0) + 1) + 0;
			if (chanceOfOrientation == 0) {
				silkBag.add(new FloorTile(straightImgPath, new Image(new FileInputStream(straightImgPath)),false, true, false , true , false, "Straight"));
			} else {
				silkBag.add(new FloorTile(straightImgPath, new Image(new FileInputStream(straightImgPath)),false, false, true , false , true, "Straight"));
			}

		}


		//corner
		for (int i = 0; i < numberOfCorners; i++) {
			int chanceOfOrientation = ran.nextInt((3 - 0) + 1) + 0;
			if (chanceOfOrientation == 0) {
				silkBag.add(new FloorTile(cornerImgPath, new Image(new FileInputStream(cornerImgPath)),false, false, false, true, true, "Corner"));
			}
			else if (chanceOfOrientation == 1) {
				silkBag.add(new FloorTile(cornerImgPath, new Image(new FileInputStream(cornerImgPath)),false,false, true, true, false,"Corner"));
			}
			else if (chanceOfOrientation == 2) {
				silkBag.add(new FloorTile(cornerImgPath, new Image(new FileInputStream(cornerImgPath)),false,true, true, false, false,"Corner"));
			}
			else {
				silkBag.add(new FloorTile(cornerImgPath, new Image(new FileInputStream(cornerImgPath)),false,true, false, true, true,"Corner"));
			}
		}

		//tShape
		for (int i = 0; i < numberOfTShapes; i++) {

			int chanceOfOrientation = ran.nextInt((3 - 0) + 1) + 0;
			if (chanceOfOrientation == 0) {
				silkBag.add(new FloorTile(tshapeImgPath, new Image(new FileInputStream(tshapeImgPath)),false,false, true, true, true,"TShape"));
			}
			else if (chanceOfOrientation == 1) {
				silkBag.add(new FloorTile(tshapeImgPath, new Image(new FileInputStream(tshapeImgPath)),false,false, true, true, false,"TShape"));
			}
			else if (chanceOfOrientation == 2) {
				silkBag.add(new FloorTile(tshapeImgPath, new Image(new FileInputStream(tshapeImgPath)),false,true, true, false, true,"TShape"));
			}
			else {
				silkBag.add(new FloorTile(tshapeImgPath, new Image(new FileInputStream(tshapeImgPath)),false,true, false, true, true,"TShape"));
			}
		}

		//Fire
		for (int i = 0; i < numberOfFireTiles; i++) {
			silkBag.add(new ActionTile(fireImgPath, new Image(new FileInputStream(fireImgPath)), "Fire") {
				@Override
				public boolean ActionTile(Player player) {
					return true;
				}
			});
		}

		//Ice
		for (int i = 0; i < numberOfIceTiles; i++) {
			silkBag.add(new ActionTile(iceImgPath, new Image(new FileInputStream(iceImgPath)), "Ice") {
				@Override
				public boolean ActionTile(Player player) {
					return true;
				}
			});
		}

		//Backtrack
		for (int i = 0; i < numberOfBacktrackTiles; i++) {
			silkBag.add(new ActionTile(backtrackImgPath, new Image(new FileInputStream(backtrackImgPath)), "Backtrack") {
				@Override
				public boolean ActionTile(Player player) {
					return true;
				}
			});
		}

		//doubleMove
		for (int i = 0; i < numberOfDoubleMoveTiles; i++) {
			silkBag.add(new ActionTile(doubleMoveImgPath, new Image(new FileInputStream(doubleMoveImgPath)), "doublemove") {
				@Override
				public boolean ActionTile(Player player) {
					return true;
				}
			});
		}

		//create board from silk bag
		//add tile to board then remove it from silk bag
		for (int i = 0; i < xSize; i++) {

			List<FloorTile> currentRowOfBoard = new ArrayList<FloorTile>();
			int tileCount = 0;

			System.out.println("new row");
			int indexOfSilkBag = 0;
			for (int j = 0; j < ySize; j++) {

				while (tileCount != ySize) {
					int currentTile_index = (int) (i * xSize + j);

					Tile tileToBoard = silkBag.get(indexOfSilkBag);
					indexOfSilkBag++;

					if (tileToBoard instanceof FloorTile) {
						System.out.println("sb: " + silkBag.size() + "|" + indexOfSilkBag + " " + tileToBoard.getName());
						System.out.println();
						currentRowOfBoard.add((FloorTile) tileToBoard);

						silkBag.remove(tileToBoard);
						tileCount++;
					}
				}
			}
			FloorTile[] currentRowToBoardArray = currentRowOfBoard.toArray(new FloorTile[currentRowOfBoard.size()]);
			board[i] = currentRowToBoardArray;
		}

		String fixedTilesData = mapScanner.nextLine();
		String[] fixedTilesContents = fixedTilesData.split("!");
		FloorTile goalTile = new FloorTile("C:\\Users\\warre\\Downloads\\stick.png", new Image(new FileInputStream("C:\\Users\\warre\\Downloads\\stick.png")),true,true, true, true, true,"goal");

		for (String fixedTileString : fixedTilesContents){

			String[] fixedTileData = fixedTileString.split(",");

			String imgPath = fixedTileData[0];
			String name = fixedTileData[1];
			Boolean north = Boolean.parseBoolean(fixedTileData[2]);
			Boolean east = Boolean.parseBoolean(fixedTileData[2]);
			Boolean south = Boolean.parseBoolean(fixedTileData[2]);
			Boolean west = Boolean.parseBoolean(fixedTileData[2]);
			Boolean fixed = Boolean.parseBoolean(fixedTileData[2]);
			int posX = Integer.parseInt(fixedTileData[7]);
			int posY = Integer.parseInt(fixedTileData[8]);


			if (name == "Goal" || name == "goal") {
				goalTile = new FloorTile(imgPath, new Image(new FileInputStream(imgPath)),fixed,north, east, south, west,name);
			}

			board[posX][posY] = new FloorTile(imgPath, new Image(new FileInputStream(imgPath)),fixed,north, east, south, west,name);
		}

		//closeMapScanner
		mapScanner.close();


		return new Game(players, board,
				goalTile,
				silkBag, 0, players.length);
	}

	/**
	 * Method to save the game
	 * @param currentGame - The current game which will be saved
	 * @param saveName - The save name of the file
	 */
	static public void saveGame(Game currentGame, String saveName) {
		PrintWriter gameWriter = null;
		try {
			gameWriter = new PrintWriter(new File(localisedDirStruct_SavedGames+saveName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		//width and height of the board
		gameWriter.println(String.valueOf(currentGame.getWidthOfBoard()) + "," + String.valueOf(currentGame.getHeightOfBoard()) );

		//board
		FloorTile[][] gameBoard = currentGame.getBoard();
		for (FloorTile[] currentRow : gameBoard) {
			String boardString = "";
			for (FloorTile currentFloorTile : currentRow) {
				String imagePath = currentFloorTile.getImgPath();
				String name = currentFloorTile.getName();
				boolean direction_north = currentFloorTile.isNorth();
				boolean direction_east = currentFloorTile.isEast();
				boolean direction_south = currentFloorTile.isSouth();
				boolean direction_west = currentFloorTile.isWest();
				boolean isFixed = currentFloorTile.isFixedTile();
				boardString +=	(imagePath + "," +
						name + "," +
						String.valueOf(isFixed) + "," +
						String.valueOf(direction_north) + "," +
						String.valueOf(direction_east) + "," +
						String.valueOf(direction_south) + "," +
						String.valueOf(direction_west) +
						"#"
				);

			}
			gameWriter.print(boardString);
			gameWriter.print("!");
			gameWriter.println();
		}


		//silkbag
		ArrayList<Tile> silkBag = currentGame.getSilkBag();

		int numOfStraight = 0;
		int numOfCorners = 0;
		int numOfTShape = 0;
		int numOfFireTile = 0;
		int numOfIceTile = 0;
		int numOfBacktrackTile = 0;
		int numOfDoubleMoveTiles = 0;

		for (Tile currentTile : silkBag) {
			if (currentTile.getName() == "Straight") {
				numOfStraight++;
			}
			else if (currentTile.getName() == "Corner") {
				numOfCorners++;
			}
			else if (currentTile.getName() == "TShape") {
				numOfTShape++;
			}
			else if (currentTile.getName() == "Fire") {
				numOfFireTile++;
			}
			else if (currentTile.getName() == "Ice") {
				numOfIceTile++;
			}
			else if (currentTile.getName() == "Backtrack") {
				numOfBacktrackTile++;
			}
			else if (currentTile.getName() == "DoubleMove") {
				numOfDoubleMoveTiles++;
			}
		}

		gameWriter.println(String.valueOf(numOfStraight)+","+String.valueOf(numOfCorners)+","+String.valueOf(numOfTShape)+","+
				String.valueOf(numOfFireTile)+","+String.valueOf(numOfFireTile)+","+String.valueOf(numOfBacktrackTile)+","+String.valueOf(numOfDoubleMoveTiles));

		String playersString = "";
		String currPlayerString = "";
		Player[] players = currentGame.getPlayers();

		addPlayersToLeaderboard(saveName, players);

		for (Player currentPlayer : players){
			int[] curPlayerLocal = currentGame.findPlayerLocation(currentPlayer);
			currPlayerString += currentPlayer.getPlayerProfile().getName()+","+curPlayerLocal[0]+","+curPlayerLocal[1]+","+currentPlayer.getImage()+"!";
			for (Tile currTile : currentPlayer.getWholeHand()) {
				currPlayerString += currTile.getName();
			}
			currPlayerString += "#";
			playersString += currPlayerString;
		}

		gameWriter.println(playersString);
		gameWriter.println(currentGame.getCurrentTurn());

		ArrayList<FloorTile> fixedTilesFromBoard = currentGame.getFixedTilesFromBoard();

		String fixedTilesString = "";
		for (FloorTile currentFixedTile : fixedTilesFromBoard){
			String imgPath = currentFixedTile.getImgPath();
			String name = currentFixedTile.getName();
			boolean north = currentFixedTile.isNorth();
			boolean east = currentFixedTile.isEast();
			boolean south = currentFixedTile.isSouth();
			boolean west = currentFixedTile.isWest();
			boolean fixedTile = currentFixedTile.isFixedTile();
			int[] tilePos = currentGame.findTileLocation(currentFixedTile);


			fixedTilesString += imgPath+","+name+","+String.valueOf(north)+","+String.valueOf(east)+","+String.valueOf(south)+","+String.valueOf(west)+","+String.valueOf(fixedTile)+","+String.valueOf(tilePos[0])+","+String.valueOf(tilePos[1])+"!";
		}

		gameWriter.print(fixedTilesString);
		gameWriter.flush();
		gameWriter.close();
	}

	/**
	 * Method to load the game
	 * @param gameName - The name of the game to be loaded
	 * @return an instance of the Game class instatiated from the game relative to the gameName argument
	 */
	static public Game loadGame(String gameName) throws FileNotFoundException {
		//scanner file start
		File mapData = new File(localisedDirStruct_SavedGames + gameName + ".txt");
		Scanner mapScanner = null;
		try {
			mapScanner = new Scanner(mapData);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}


		//read size of board (x,y)
		String boardSizeData = mapScanner.nextLine();
		String[] xySizeData = boardSizeData.split(",");
		int xSize = Integer.parseInt(xySizeData[0]);
		int ySize = Integer.parseInt(xySizeData[1]);

		FloorTile[][] board = new FloorTile[xSize][ySize];
		int rowCounter = 0;
		//create board from silk bag
		//add tile to board then remove it from silk bag
		for (int i = 0; i < xSize; i++) {
			//System.out.println(i);
			String row = mapScanner.nextLine();
			String[] rowData = row.split("!");


			FloorTile[] boardCurRow = new FloorTile[ySize];
			for (String rowRead: rowData) {
				String[] rowOutputData = rowRead.split("#");
				//System.out.println(rowRead);
				int indexOfTileOnRow = 0;



				for (String TileOutputData: rowOutputData) {
					//System.out.println(TileOutputData);
					String[] tileSplitData = TileOutputData.split(",");


					/*
					System.out.println(tileSplitData[0]);
					System.out.println(tileSplitData[1]);
					System.out.println(tileSplitData[2]);
					System.out.println(tileSplitData[3]);
					System.out.println(tileSplitData[4]);
					System.out.println(tileSplitData[5]);
					System.out.println(tileSplitData[6]);

					 */

					FloorTile currentTile = new FloorTile(tileSplitData[0], new Image(new FileInputStream(tileSplitData[0])), Boolean.parseBoolean(tileSplitData[2]),
							Boolean.parseBoolean(tileSplitData[3]), Boolean.parseBoolean(tileSplitData[4]), Boolean.parseBoolean(tileSplitData[5]),
							Boolean.parseBoolean(tileSplitData[6]), tileSplitData[1]);

					boardCurRow[indexOfTileOnRow] = currentTile;
					indexOfTileOnRow++;
				}
			}
			board[rowCounter] = boardCurRow;
			rowCounter++;
		}


		//read silk bag contents
		String silkBagData = mapScanner.nextLine();
		System.out.println(silkBagData);
		String[] silkBagContents = silkBagData.split(",");
		int numberOfStraights = Integer.parseInt(silkBagContents[0]);
		int numberOfCorners = Integer.parseInt(silkBagContents[1]);
		int numberOfTShapes = Integer.parseInt(silkBagContents[2]);
		int numberOfFireTiles = Integer.parseInt(silkBagContents[3]);
		int numberOfIceTiles = Integer.parseInt(silkBagContents[4]);
		int numberOfBacktrackTiles = Integer.parseInt(silkBagContents[5]);
		int numberOfDoubleMoveTiles = Integer.parseInt(silkBagContents[6]);

		//read and create silk bag
		ArrayList<Tile> silkBag = new ArrayList<Tile>();
		//straight
		for (int i = 0; i < numberOfStraights; i++) {
			int chanceOfOrientation = (int) (Math.random() * 1);
			if (chanceOfOrientation == 0) {
				silkBag.add(new FloorTile(straightImgPath, new Image(new FileInputStream(straightImgPath)),false, true, false , true , false, "Straight"));
			} else {
				silkBag.add(new FloorTile(straightImgPath, new Image(new FileInputStream(straightImgPath)),false, false, true , false , true, "Straight"));
			}
		}

		//corner
		for (int i = 0; i < numberOfCorners; i++) {
			int chanceOfOrientation = (int) (Math.random() * 3);
			if (chanceOfOrientation == 0) {
				silkBag.add(new FloorTile(cornerImgPath, new Image(new FileInputStream(cornerImgPath)),false, false, false, true, true, "Corner"));
			}
			else if (chanceOfOrientation == 1) {
				silkBag.add(new FloorTile(cornerImgPath, new Image(new FileInputStream(cornerImgPath)),false,false, true, true, false,"Corner"));
			}
			else if (chanceOfOrientation == 2) {
				silkBag.add(new FloorTile(cornerImgPath, new Image(new FileInputStream(cornerImgPath)),false,true, true, false, false,"Corner"));
			}
			else {
				silkBag.add(new FloorTile(cornerImgPath, new Image(new FileInputStream(cornerImgPath)),false,true, false, true, true,"Corner"));
			}
		}

		//tShape
		for (int i = 0; i < numberOfTShapes; i++) {
			int chanceOfOrientation = (int) (Math.random() * 3);
			if (chanceOfOrientation == 0) {
				silkBag.add(new FloorTile(tshapeImgPath, new Image(new FileInputStream(tshapeImgPath)),false,false, true, true, true,"TShape"));
			}
			else if (chanceOfOrientation == 1) {
				silkBag.add(new FloorTile(tshapeImgPath, new Image(new FileInputStream(tshapeImgPath)),false,false, true, true, false,"TShape"));
			}
			else if (chanceOfOrientation == 2) {
				silkBag.add(new FloorTile(tshapeImgPath, new Image(new FileInputStream(tshapeImgPath)),false,true, true, false, true,"TShape"));
			}
			else {
				silkBag.add(new FloorTile(tshapeImgPath, new Image(new FileInputStream(tshapeImgPath)),false,true, false, true, true,"TShape"));
			}
		}

		//Fire
		for (int i = 0; i < numberOfFireTiles; i++) {
			silkBag.add(new ActionTile(fireImgPath, new Image(new FileInputStream(fireImgPath)), "Fire") {
				@Override
				public boolean ActionTile(Player player) {
					return true;
				}
			});
		}

		//Ice
		for (int i = 0; i < numberOfIceTiles; i++) {
			silkBag.add(new ActionTile(iceImgPath, new Image(new FileInputStream(iceImgPath)), "Ice") {
				@Override
				public boolean ActionTile(Player player) {
					return true;
				}
			});
		}

		//Backtrack
		for (int i = 0; i < numberOfBacktrackTiles; i++) {
			silkBag.add(new ActionTile(backtrackImgPath, new Image(new FileInputStream(backtrackImgPath)), "Backtrack") {
				@Override
				public boolean ActionTile(Player player) {
					return true;
				}
			});
		}

		//doubleMove
		for (int i = 0; i < numberOfDoubleMoveTiles; i++) {
			silkBag.add(new ActionTile(doubleMoveImgPath, new Image(new FileInputStream(doubleMoveImgPath)), "doublemove") {
				@Override
				public boolean ActionTile(Player player) {
					return true;
				}
			});
		}


		//Players

		String playersInGame = mapScanner.nextLine();
		String[] playerDataInGame = playersInGame.split("#");
		Player[] playerObjs = new Player[playerDataInGame.length];
		int playerTracker = 0;

		for (String player: playerDataInGame) {
			//System.out.println(player);
			String[] playerData = player.split("!");
			//System.out.println(playerData[0]);
			//System.out.println(playerData[1]);
			//player Game Data
			String[] playerGameData = playerData[0].split(",");
			//System.out.println(playerData[0]);
			//System.out.println(playerData[1]);
			FloorTile playerCurrentTile = board[Integer.parseInt(playerGameData[1])][Integer.parseInt(playerGameData[2])];

			//player hand
			String playerHandString = playerData[1];
			String[] playerHand = playerHandString.split(",");

			Player currentPlayer = new Player(playerGameData[3], readPlayerProfile(playerGameData[0]), playerCurrentTile, new Image(new FileInputStream(playerGameData[3])));

			for (String TileString : playerHand) {
				//Fire
				if (TileString == "Fire") {
					currentPlayer.addHand(new ActionTile(fireImgPath, new Image(new FileInputStream(fireImgPath)), "Fire") {
						@Override
						public boolean ActionTile(Player player) {
							return true;
						}
					});
				}

				else if (TileString == "Ice") {
					currentPlayer.addHand(new ActionTile(fireImgPath, new Image(new FileInputStream(fireImgPath)), "Ice") {
						@Override
						public boolean ActionTile(Player player) {
							return true;
						}
					});
				}

				else if (TileString == "Backtrack") {
					currentPlayer.addHand(new ActionTile(fireImgPath, new Image(new FileInputStream(fireImgPath)), "Backtrack") {
						@Override
						public boolean ActionTile(Player player) {
							return true;
						}
					});
				}

				else if (TileString == "doublemove") {
					currentPlayer.addHand(new ActionTile(fireImgPath, new Image(new FileInputStream(fireImgPath)), "doublemove") {
						@Override
						public boolean ActionTile(Player player) {
							return true;
						}
					});
				}
			}

			playerObjs[playerTracker] = currentPlayer;
		}

		String turn = mapScanner.nextLine();
		System.out.println(turn);



		FloorTile goalTile = new FloorTile("C:\\Users\\warre\\Downloads\\stick.png", new Image(new FileInputStream("C:\\Users\\warre\\Downloads\\stick.png")),true,true, true, true, true,"goal");

		String fixedTilesData = mapScanner.nextLine();
		String[] fixedTilesContents = fixedTilesData.split("!");
		for (String fixedTileString : fixedTilesContents){

			String[] fixedTileData = fixedTileString.split(",");

			String imgPath = fixedTileData[0];
			String name = fixedTileData[1];
			Boolean north = Boolean.parseBoolean(fixedTileData[2]);
			Boolean east = Boolean.parseBoolean(fixedTileData[2]);
			Boolean south = Boolean.parseBoolean(fixedTileData[2]);
			Boolean west = Boolean.parseBoolean(fixedTileData[2]);
			Boolean fixed = Boolean.parseBoolean(fixedTileData[2]);
			int posX = Integer.parseInt(fixedTileData[7]);
			int posY = Integer.parseInt(fixedTileData[8]);

			if (name == "Goal" || name == "goal") {
				goalTile = new FloorTile(imgPath, new Image(new FileInputStream(imgPath)),fixed,north, east, south, west,name);
			}

			board[posX][posY] = new FloorTile(imgPath, new Image(new FileInputStream(imgPath)),fixed,north, east, south, west,name);
		}

		mapScanner.close();

		return new Game(playerObjs, board,
				goalTile,
				silkBag, Integer.parseInt(turn), playerObjs.length);
	}

	/**
	 * Method to read a player profrile
	 * @param ProfileName - the player profile to read
	 */
	static public PlayerProfile readPlayerProfile(String ProfileName) {
		try {
			File playerProfileFile = new File(localisedDirStruct_PlayerProfile+ProfileName+".txt");
			Scanner scanner = new Scanner(playerProfileFile);
			String name = scanner.nextLine();
			int wins = Integer.parseInt(scanner.nextLine());
			int losses = Integer.parseInt(scanner.nextLine());
			int gamesPlayed = Integer.parseInt(scanner.nextLine());
			scanner.close();
			return new PlayerProfile(losses, wins, name, gamesPlayed);

		} catch (FileNotFoundException e) {
			System.out.println("Player profile specified does not exist.");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Method to delete a player profile
	 * @param profileName - the player profile to be deleted
	 * @return a boolean value to show if the operation has been successful
	 */
	static public void deletePlayerProfile(String profileName) {
		File playerProfileFile = new File(localisedDirStruct_PlayerProfile + profileName +".txt");
		playerProfileFile.delete();
	}

	/**
	 * Method to create a player profile
	 * @param name - the desired name of the plyaer profile
	 * @return An instance of PlayerProfile
	 */
	public static void createPlayerProfile(String name) {
		File playerProfileFile = new File(localisedDirStruct_PlayerProfile + name + ".txt");

		PrintWriter playerProfileWriter;
		try {
			playerProfileWriter = new PrintWriter(playerProfileFile);
			playerProfileWriter.println(name);
			playerProfileWriter.println(0);
			playerProfileWriter.println(0);
			playerProfileWriter.println(0);
			playerProfileWriter.flush();
			playerProfileWriter.close();

			AllPlayerProfiles.add(new PlayerProfile(0, 0, name, 0));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/*
	 *
	 * Method to update all playerProfiles
	 * @param players - An array of Players
	 * @return a boolean value to show if the operation has been successful
	 */
	public Boolean updateAllPlayerProfiles(Game currentGame) {
		Player[] players = currentGame.getPlayers();

		for (Player currentPlayer : players) {

			overwritePlayerProfile(currentPlayer);

		}
		return true;
	}

	/*
	 * Method to overwrite player
	 * @param currentPlayer
	 */
	private static void overwritePlayerProfile(Player currentPlayer) {
		PlayerProfile currentPlayerProfile = currentPlayer.getPlayerProfile();

		String name = currentPlayerProfile.getName();
		int wins = currentPlayerProfile.getWins();
		int losses = currentPlayerProfile.getLosses();
		int gamesPlayed = currentPlayerProfile.getGamesPlayed();

		File playerProfileFile = new File(localisedDirStruct_PlayerProfile + name + ".txt");

		PrintWriter playerProfileWriter;
		try {
			playerProfileWriter = new PrintWriter(playerProfileFile);
			playerProfileWriter.println(name);
			playerProfileWriter.println(wins);
			playerProfileWriter.println(losses);
			playerProfileWriter.println(gamesPlayed);
			playerProfileWriter.flush();
			playerProfileWriter.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	//test
	public static void main(String[] args) throws FileNotFoundException {
		getAllPlayerProfiles();


		/*
		Player[] Players = new Player[]{new Player("test.png",  readPlayerProfile("LukeSkywalker"),
				new FloorTile("C:\\Users\\warre\\Downloads\\stick.png",
						new Image(new FileInputStream("C:\\Users\\warre\\Downloads\\stick.png")),
						true, true, true, true, true, "FloorTile"),
				new Image(new FileInputStream("C:\\Users\\warre\\Downloads\\stick.png")))};
		Game currentTest = readNewGame("coolWorld.txt", Players);

		System.out.println(currentTest.getPlayers());
		System.out.println(currentTest.getHeightOfBoard());
		System.out.println(currentTest.getCurrentTurn());
		System.out.println(currentTest.getHeightOfBoard());
		System.out.println(currentTest.getPlayers()[0].getPlayerProfile().getName());

		FloorTile[][] board = currentTest.getBoard();

		System.out.println(board[0][0]);

		for (FloorTile[] row: board){
			for (FloorTile curTile: row){
				System.out.println(curTile.getName());
			}
		}
 */

		//TEST FOR saveGame -----> loadGame -----> getPlayerProfilesFromLeaderboard ACTION : WORKING

		Player[] Players = new Player[]{new Player("C:\\Users\\warre\\Downloads\\qq.png",  readPlayerProfile("LukeSkywalker"),
				new FloorTile("C:\\Users\\warre\\Downloads\\stick.png",
						new Image(new FileInputStream("C:\\Users\\warre\\Downloads\\stick.png")),
						true, true, true, true, true, "ImConfusedTile"),
				new Image(new FileInputStream("C:\\Users\\warre\\Downloads\\stick.png")))};

		Players[0].addHand(new ActionTile("C:\\Users\\warre\\Downloads\\stick.png", new Image(new FileInputStream("C:\\Users\\warre\\Downloads\\stick.png")), "TheHandActionTile") {
			@Override
			public boolean ActionTile(Player player) {
				return false;
			}
		});



		saveGame(new Game(Players, new FloorTile[][]{
				{new FloorTile("C:\\Users\\warre\\Downloads\\stick.png",
						new Image(new FileInputStream("C:\\Users\\warre\\Downloads\\stick.png")),
						true, true, true, true, true, "FloorTile1"),
						new FloorTile("C:\\Users\\warre\\Downloads\\stick.png",
								new Image(new FileInputStream("C:\\Users\\warre\\Downloads\\stick.png")),
								true, true, true, true, true, "FloorTile2")},
				{new FloorTile("C:\\Users\\warre\\Downloads\\stick.png",
						new Image(new FileInputStream("C:\\Users\\warre\\Downloads\\stick.png")),
						true, true, true, true, true, "FloorTile3"),
						new FloorTile("C:\\Users\\warre\\Downloads\\stick.png",
								new Image(new FileInputStream("C:\\Users\\warre\\Downloads\\stick.png")),
								true, true, true, true, true, "FloorTile4")}},

				new FloorTile("C:\\Users\\warre\\Downloads\\stick.png",
						new Image(new FileInputStream("C:\\Users\\warre\\Downloads\\stick.png")),
						true, true, true, true, true, "CrazyTile"),
				new ArrayList<Tile>(),
				0,
				1), "saveAndThenLoad.txt");


		Game currentTest = loadGame("saveAndThenLoad");
		System.out.println(currentTest.getPlayers());
		System.out.println(currentTest.getHeightOfBoard());
		System.out.println(currentTest.getCurrentTurn());
		System.out.println(currentTest.getHeightOfBoard());
		System.out.println(currentTest.getPlayers()[0].getPlayerProfile().getName());

		FloorTile[][] board = currentTest.getBoard();

		for (FloorTile[] row: board){
			for (FloorTile curTile: row){
				System.out.println(curTile.getName());
			}
		}

		System.out.println("==================");

		ArrayList<PlayerProfile> playerProfiles = getPlayerProfilesFromLeaderboard("saveAndThenLoad");

		System.out.println(playerProfiles.get(0));
		System.out.println(playerProfiles.size());




		/*
		TEST FOR saveGame : WORKING

		Player[] Players = new Player[]{new Player("test.png",  readPlayerProfile("LukeSkywalker"),
				new FloorTile("C:\\Users\\warre\\Downloads\\stick.png",
						new Image(new FileInputStream("C:\\Users\\warre\\Downloads\\stick.png")),
						true, true, true, true, true, "ImConfusedTile"),
				new Image(new FileInputStream("C:\\Users\\warre\\Downloads\\stick.png")))};

		Players[0].addHand(new ActionTile("C:\\Users\\warre\\Downloads\\stick.png", new Image(new FileInputStream("C:\\Users\\warre\\Downloads\\stick.png")), "TheHandActionTile") {
			@Override
			public boolean ActionTile(Player player) {
				return false;
			}
		});

		saveGame(new Game(Players, new FloorTile[][]{
				{new FloorTile("C:\\Users\\warre\\Downloads\\stick.png",
						new Image(new FileInputStream("C:\\Users\\warre\\Downloads\\stick.png")),
						true, true, true, true, true, "FloorTile"),
						new FloorTile("C:\\Users\\warre\\Downloads\\stick.png",
								new Image(new FileInputStream("C:\\Users\\warre\\Downloads\\stick.png")),
								true, true, true, true, true, "TheOtherFloorTile")},
				{new FloorTile("C:\\Users\\warre\\Downloads\\stick.png",
						new Image(new FileInputStream("C:\\Users\\warre\\Downloads\\stick.png")),
						true, true, true, true, true, "TestOtherTile")}},

				new FloorTile("C:\\Users\\warre\\Downloads\\stick.png",
						new Image(new FileInputStream("C:\\Users\\warre\\Downloads\\stick.png")),
						true, true, true, true, true, "CrazyTile"),
				new ArrayList<Tile>(),
				0,
				1), "theSavedGame.txt");

 		*/

		/*
		TEST FOR loadGame and readPlayerProfile : WORKING
		Game currentTest = loadGame("ultraCoolWorld");
		System.out.println(currentTest.getPlayers());
		System.out.println(currentTest.getHeightOfBoard());
		System.out.println(currentTest.getCurrentTurn());
		System.out.println(currentTest.getHeightOfBoard());
		System.out.println(currentTest.getPlayers()[0].getPlayerProfile().getName());

		FloorTile[][] board = currentTest.getBoard();

		for (FloorTile[] row: board){
			for (FloorTile curTile: row){
				System.out.println(curTile.getName());
			}
		}
		 */

		/*
		TEST FOR createPlayerProfile and deletePlayerProfile : WORKING
		createPlayerProfile("Baby Yoda");
		deletePlayerProfile("Baby Yoda");
		*/

		/*
		TEST FOR overwritePlayerProfile : WORKING
				overwritePlayerProfile(new Player("test.png",  readPlayerProfile("LukeSkywalker"),
				new FloorTile("C:\\Users\\warre\\Downloads\\stick.png",
						new Image(new FileInputStream("C:\\Users\\warre\\Downloads\\stick.png")),
						true, true, true, true, true, "FloorTile"),
				new Image(new FileInputStream("C:\\Users\\warre\\Downloads\\stick.png"))));
		 */





		/*

		TEST FOR readPlayerProfile and readNewGame : WORKING

		Player[] Players = new Player[]{new Player("test.png",  readPlayerProfile("LukeSkywalker"),
																								new FloorTile("C:\\Users\\warre\\Downloads\\stick.png",
																										new Image(new FileInputStream("C:\\Users\\warre\\Downloads\\stick.png")),
																									true, true, true, true, true, "FloorTile"),
																								new Image(new FileInputStream("C:\\Users\\warre\\Downloads\\stick.png")))};
		Game currentTest = readNewGame("coolWorld.txt", Players);

		System.out.println(currentTest.getPlayers());
		System.out.println(currentTest.getHeightOfBoard());
		System.out.println(currentTest.getCurrentTurn());
		System.out.println(currentTest.getHeightOfBoard());
		System.out.println(currentTest.getPlayers()[0].getPlayerProfile().getName());

		FloorTile[][] board = currentTest.getBoard();

		System.out.println(board[0][0]);

		for (FloorTile[] row: board){
			for (FloorTile curTile: row){
				System.out.println(curTile.getName());
			}
		}
		 */
	}
}
