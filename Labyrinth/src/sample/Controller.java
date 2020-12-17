package sample;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


public class Controller {
    @FXML private GridPane actionCol;
    @FXML private GridPane boardCol;
    @FXML private ImageView currTile;
    @FXML private Label infoLable;
    @FXML private GridPane playerCol;
    @FXML private Label playerTurnLable;
    @FXML private Label turnLable;
    @FXML private Label turnsLable;
    @FXML private Button cancelButton;
    @FXML private Button endTurn;
    @FXML private Label wonLable;
    @FXML private AnchorPane gamePane;
    private Button[][] currentTiles = new Button[Game.board.length][Game.board[0].length];
    private Button[] actionButton = new Button[3];
    private Button[] playerButtons = new Button[Game.numOfPlayers];
    private ImageView[] playersOnBoard = new ImageView[Game.numOfPlayers];
    private int curAcion = 0;
    public static int xtile = -1;
    public static int ytile = -1;
    private int selectedActionTile = -1;
    private int chosenPlayer = -1;
    private String fire = "src\\sample\\IMG\\fire.png";
    private String ice = "src\\sample\\IMG\\ice.png";
    private String weedPath = "src\\sample\\IMG\\weed.png";
    private String arrowPath = "src\\sample\\IMG\\arrow.png";
    private Image iceIm;
    private Image fireIm;
    private Image weedImg;
    private Tile curentTile;
    private Image arrow;
    private boolean insertOnce;
    private boolean usedAction;
    private boolean madeAMove;
    private Game theGame;
    private int sizeOfTiles = 60;
    private int sizeOfActionTiles = 50;



    @FXML
    public void initialize() throws FileNotFoundException {
        theGame = Menu.theGame;
        wonLable.setText("");

        prepForUI();


        iceIm = new Image(new FileInputStream(ice));
        fireIm = new Image(new FileInputStream(fire));
        weedImg = new Image(new FileInputStream(weedPath));
        arrow = new Image(new FileInputStream(arrowPath));

        displayPlayers();

        refreshTheBoard();
    }

    @FXML
    public void refreshTheBoard() {
        boardCol.setMinSize(sizeOfTiles,sizeOfTiles);
        boardCol.getChildren().clear();
        //boardCol.setGridLinesVisible(true);

        int x = 0;
        int y = 0;
        for(int j = -1; j < Game.board[0].length+1; j++){
            for (int i = -1; i < Game.board.length+1; i++) {
                if(i >= 0 && j >= 0 && j < Game.board[0].length && i < Game.board.length) {

                    ImageView imageView = new ImageView(Game.board[i][j].getImage());
                    imageView.setFitWidth(sizeOfTiles);
                    imageView.setPreserveRatio(true);
                    imageView.setBlendMode(BlendMode.MULTIPLY);

                    Group blend;


                    Button button = new Button();
                    button.setStyle("-fx-padding: 0");
                    button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    if(Game.board[i][j].isFrozen && Game.board[i][j].isOnFire){

                        ImageView imageViewIce = new ImageView(iceIm);
                        imageViewIce.setFitWidth(sizeOfTiles-15);
                        imageViewIce.setPreserveRatio(true);
                        imageViewIce.setBlendMode(BlendMode.MULTIPLY);

                        ImageView imageViewFire = new ImageView(fireIm);
                        imageViewFire.setFitWidth(sizeOfTiles-15);
                        imageViewFire.setPreserveRatio(true);
                        imageViewFire.setBlendMode(BlendMode.MULTIPLY);


                        blend = new Group(imageViewIce, imageViewFire,  imageView);




                    }else if(Game.board[i][j].isFrozen){

                        ImageView imageViewIce = new ImageView(iceIm);
                        imageViewIce.setFitWidth(sizeOfTiles-15);
                        imageViewIce.setPreserveRatio(true);

                        blend = new Group(imageViewIce, imageView);


                    }else if(Game.board[i][j].isOnFire) {

                        ImageView imageViewFire = new ImageView(fireIm);
                        imageViewFire.setFitWidth(sizeOfTiles-15);
                        imageViewFire.setPreserveRatio(true);
                        blend = new Group(imageViewFire, imageView);


                    }else {
                        blend = new Group(imageView);
                    }
                    button.setGraphic(blend);

                    int finalI = i;
                    int finalJ = j;
                    button.setOnAction(value -> {
                        xtile = finalJ;
                        ytile = finalI;
                        if(Game.players[Game.currentTurn % Game.numOfPlayers].getHand(selectedActionTile).ActionTile(Game.players[Game.currentTurn % Game.numOfPlayers])) {
                            infoLable.setText("");
                            if(curAcion > 0) {
                                curAcion--;
                            }
                            Game.players[Game.currentTurn % Game.numOfPlayers].removeHandTile(Game.players[Game.currentTurn % Game.numOfPlayers].getHand(selectedActionTile));
                            updateActionRow(Game.currentTurn % Game.numOfPlayers, curAcion);
                            refreshTheBoard();
                        }else {
                            infoLable.setText("there are players in that area");
                            usedAction = false;

                        }
                        selectedActionTile = -1;
                        tileLoop(true);

                        xtile = -1;
                        ytile = -1;

                    });
                    button.setDisable(true);
                    currentTiles[i][j] = button;
                    boardCol.add(button, x, y, 1, 1);

                    double sizeOfPlayers = sizeOfTiles-15;
                    for(int k = 0; k < Game.players.length; k++) {
                        if(Game.players[k].getPosition() == Game.board[i][j]) {

                            ArrayList<Player> tempPlayersOnTile = new ArrayList<Player>();
                            GridPane temp = new GridPane();

                            for(int p = 0; p < Game.players.length; p++) {
                                if(Game.players[k].getPosition() == Game.players[p].getPosition()) {
                                    tempPlayersOnTile.add(Game.players[p]);
                                }
                            }

                            if(tempPlayersOnTile.size() == 1) {
                                sizeOfPlayers = sizeOfTiles-15;
                            }else if(tempPlayersOnTile.size() == 2) {
                                sizeOfPlayers = (sizeOfTiles-15)/1.5;
                            }else if(tempPlayersOnTile.size() > 2) {
                                sizeOfPlayers = (sizeOfTiles-15)/2.2;
                            }


                            int col = 0;
                            int row = 0;
                            for(int p = 0; p < tempPlayersOnTile.size(); p++) {
                                ImageView imageViewPlayer = new ImageView(tempPlayersOnTile.get(p).getPlayerImage());
                                imageViewPlayer.setFitWidth(sizeOfPlayers);
                                imageViewPlayer.setPreserveRatio(true);
                                temp.add(imageViewPlayer, col,row , 1, 1);
                                if(col > 0) {
                                    col = 0;
                                    row = 1;
                                } else {
                                    col = 1;
                                }
                            }





                            //temp.add(imageViewPlays, 1,0 , 1, 1);
                            //temp.add(imageViewPla, 0,1 , 1, 1);
                            temp.toFront();
                            //playersOnBoard[k] = imageViewPlay;
                            boardCol.add( temp, x, y, 1, 1);
                        }
                    }

                }else if(!(x == 0 && y == 0) && !(x == 0 && y == Game.board.length+1) &&
                        (!(x == Game.board[0].length+1 && y == 0) && !(x == Game.board[0].length+1 && y == Game.board.length+1))){
                    boolean willChange = true;
                    if((x == 0 || x == Game.board[0].length+1) && y < Game.board.length+1 && y > 0){

                        for(int k = 0; k < Game.board[0].length; k++){

                            if(Game.board[y-1][k].isFrozen || Game.board[y-1][k].isFixedTile()){
                                willChange = false;
                            }
                        }
                    }
                    else if((y == 0 || y == Game.board.length+1) && x < Game.board[0].length+1 && x > 0){

                        for(int k = 0; k < Game.board.length; k++){

                            if(Game.board[k][x-1].isFrozen || Game.board[k][x-1].isFixedTile()){
                                willChange = false;
                            }
                        }
                    }
                    if(willChange) {
                        if(!madeAMove && !usedAction && !(curentTile instanceof ActionTile) && !insertOnce) {
                            insertOnce = true;
                        }

                        ImageView arrowImg = new ImageView(arrow);
                        arrowImg.setFitHeight(30);
                        arrowImg.setPreserveRatio(true);
                        if(x == Game.board[0].length+1) {
                            arrowImg.setRotate(arrowImg.getRotate() + 180);
                        } else if(y == 0) {
                            arrowImg.setRotate(arrowImg.getRotate() + 90);
                        } else if(y == Game.board.length+1) {
                            arrowImg.setRotate(arrowImg.getRotate() - 90);
                        }


                        Button button1 = new Button("",arrowImg);

                        button1.setStyle("-fx-background-color: transparent;");
                        button1.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

                        int finalY = y;
                        int finalX = x;
                        button1.setOnAction(value -> {
                            int yy = finalY;
                            int xx = finalX;
                            if(finalY == 0){
                                //yy++;
                            }else if(yy == currentTiles.length+1){
                                yy = yy - 2;
                            }else {
                                yy = yy - 1;
                            }
                            if(xx == 0){
                                //xx++;
                            }else if(xx == currentTiles[0].length+1){
                                xx = xx - 2;
                            }else{
                                xx = xx - 1;
                            }
                            if(insertOnce && curentTile instanceof FloorTile) {
                                if (Game.slideTile(yy, xx, (FloorTile) curentTile)) {
                                    refreshTheBoard();
                                }
                                insertOnce = false;
                            }

                        });
                        boardCol.add(button1, x, y, 1, 1);
                    }else {
                        Button inv = new Button("");
                        inv.setVisible(false);
                        boardCol.add(inv, x, y, 1, 1);
                    }
                }
                y++;
            }
            y=0;
            x++;

        }


        if(!Game.canPlayerMove(Game.players[Game.currentTurn % Game.numOfPlayers]) && !madeAMove){
            madeAMove = true;
        }

    }

    @FXML
    private void cancelAction(){
        usedAction = false;
        cancelButton.setVisible(false);
        infoLable.setText("");
        tileLoop(true);
        playerLoop(true);
    }

    @FXML
    private void goToMenu() throws IOException {
        AnchorPane temp = FXMLLoader.load(getClass().getResource("Menu.fxml"));
        gamePane.getChildren().setAll(temp);
    }

    @FXML
    private void nextTurn() {

        if((insertOnce && curentTile instanceof FloorTile) ){
            infoLable.setText("You must insert the FloorTile that you have!");
        } else {
            if(!madeAMove){
                infoLable.setText("You must make a move!");
            }else if(curentTile instanceof ActionTile){
                Game.players[Game.currentTurn % Game.numOfPlayers].addHand((ActionTile) curentTile);
                Game.currentTurn++;
                prepForUI();
                Game.removeIceAndFire();
                refreshTheBoard();
            }else{
                Game.currentTurn++;
                prepForUI();
                Game.removeIceAndFire();
                refreshTheBoard();
            }

        }
    }

    @FXML
    private void keyPressed(KeyEvent evt) {

        if(Game.players[Game.currentTurn % Game.numOfPlayers].getMovesPerTurn() > 0 && !madeAMove && !insertOnce) {
            switch (evt.getCode()) {
                case UP:
                    if(Game.movePlayer(Game.players[Game.currentTurn % Game.numOfPlayers], false, false, false, true)){
                        movmentUpdate();
                    }
                    break;
                case DOWN:
                    if(Game.movePlayer(Game.players[Game.currentTurn % Game.numOfPlayers], false, false, true, false)){
                        movmentUpdate();
                    }
                    break;
                case LEFT:
                    if(Game.movePlayer(Game.players[Game.currentTurn % Game.numOfPlayers], false, true, false, false)){
                        movmentUpdate();
                    }
                    break;
                case RIGHT:
                    if(Game.movePlayer(Game.players[Game.currentTurn % Game.numOfPlayers], true, false, false, false)){
                        movmentUpdate();
                    }
                    break;
            }

        }

        if(insertOnce && Game.players[Game.currentTurn % Game.numOfPlayers].getMovesPerTurn() > 0){
            infoLable.setText("You must insert a tile before moving");
        }
    }

    private void movmentUpdate() {
        Game.players[Game.currentTurn % Game.numOfPlayers].setMovesPerTurn(Game.players[Game.currentTurn % Game.numOfPlayers].getMovesPerTurn() -1);
        turnsLable.setText("Moves left:" + Game.players[Game.currentTurn % Game.numOfPlayers].getMovesPerTurn());
        if(Game.players[Game.currentTurn % Game.numOfPlayers].getMovesPerTurn() == 0) {
            madeAMove = true;
        }
        refreshTheBoard();
        if(Game.hasWonTheGame(Game.players[Game.currentTurn % Game.numOfPlayers])) {
            wonLable.setText(Game.players[Game.currentTurn % Game.numOfPlayers].getPlayerProfile().getName() + " has won!");
            wonLable.setStyle("-fx-font: 30 arial;");

            endTurn.setDisable(true);

        }

    }

    private void prepForUI() {
        Game.players[Game.currentTurn % Game.numOfPlayers].setMovesPerTurn(1);


        infoLable.setText("");
        turnsLable.setText("Moves left:" + Game.players[Game.currentTurn % Game.numOfPlayers].getMovesPerTurn());
        turnLable.setText("Turn: " + Game.currentTurn);
        playerTurnLable.setText("It's " + Game.players[Game.currentTurn % Game.numOfPlayers].getPlayerProfile().getName() + " turn");

        curentTile = Game.silkBag.get(0);
        currTile.setImage(curentTile.getImage());
        Game.silkBag.remove(0);
        currTile.setFitWidth(75);
        currTile.setPreserveRatio(true);
        cancelButton.setVisible(false);

        insertOnce = false;
        usedAction = false;
        madeAMove = false;
        //insertOnce = true;
        curAcion = 0;


        updateActionRow(Game.currentTurn % Game.numOfPlayers, curAcion);
    }

    private void displayPlayers() {
        //playerCol
        for (int k = 0; k < Game.players.length; k++) {

            ImageView imageView = new ImageView(Game.players[k].getPlayerImage());
            imageView.setFitHeight(sizeOfActionTiles);
            imageView.setPreserveRatio(true);
            Button button = new Button(Game.players[k].getPlayerProfile().getName(), imageView);

            int finalK = k;
            button.setOnAction(value -> {
                chosenPlayer = finalK;
                playerLoop(true);
                runbackTrack();
                infoLable.setText("");
                Game.players[Game.currentTurn % Game.numOfPlayers].removeHandTile(Game.players[Game.currentTurn % Game.numOfPlayers].getHand(selectedActionTile));
                if(curAcion > 0) {
                    curAcion--;
                }
                updateActionRow(Game.currentTurn % Game.numOfPlayers, curAcion);
                refreshTheBoard();
            });
            button.setStyle("-fx-padding: 0");
           // button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            button.setDisable(true);
            playerCol.add(button, 0, k, 1, 1);
            playerButtons[k] = button;
        }
    }


    @FXML
    public void nextAct() {
        if(Game.players[Game.currentTurn % Game.numOfPlayers].sizeOfHand() > curAcion + 3) {
            curAcion++;
        }
        updateActionRow(Game.currentTurn % Game.numOfPlayers, curAcion);
    }

    @FXML
    public void prewAct() {
        if(curAcion > 0) {
            curAcion--;
        }
        updateActionRow(Game.currentTurn % Game.numOfPlayers, curAcion);
    }




    private void tileLoop(boolean statement){
        for(int k = 0; k < Game.board.length; k++) {
            for (int j = 0; j < Game.board[0].length; j++) {
                currentTiles[k][j].setDisable(statement);
            }
        }
    }


    private void updateActionRow(int player, int hand) {
        actionCol.getChildren().clear();
        actionCol.setGridLinesVisible(true);

        for(int i = 0; i < Game.players[player].sizeOfHand() && i < 3; i++){

            ImageView imageView = new ImageView(Game.players[player].getHand(i + hand).getImage());
            imageView.setFitWidth(sizeOfActionTiles);
            imageView.setPreserveRatio(true);
            Button button = new Button();
            button.setGraphic(imageView);
            int finalI = i;
            button.setOnAction(value -> {
                if(!usedAction && !madeAMove && !insertOnce) {
                    selectedActionTile = finalI + hand;
                    if (Game.players[player].getHand(finalI + hand) instanceof Fire ||
                            Game.players[player].getHand(finalI + hand) instanceof Ice) {
                        if (xtile == -1 || ytile == -1) {
                            infoLable.setText("select tile on which you will cast your action");
                            tileLoop(false);

                        }
                        cancelButton.setVisible(true);
                    } else if (Game.players[player].getHand(finalI + hand) instanceof Backtrack) {

                        if (playerLoop(false)) {
                            infoLable.setText("select the player");
                        } else {
                            infoLable.setText("you have used backTrack on everyone");
                        }
                        cancelButton.setVisible(true);
                    } else {
                        Game.players[Game.currentTurn % Game.numOfPlayers].getHand(selectedActionTile).ActionTile(Game.players[Game.currentTurn % Game.numOfPlayers]);
                        Game.players[Game.currentTurn % Game.numOfPlayers].removeHandTile(Game.players[Game.currentTurn % Game.numOfPlayers].getHand(selectedActionTile));

                        if (curAcion > 0) {
                            curAcion--;
                        }
                        updateActionRow(Game.currentTurn % Game.numOfPlayers, curAcion);
                        turnsLable.setText("Moves left:" + Game.players[Game.currentTurn % Game.numOfPlayers].getMovesPerTurn());
                    }
                    usedAction = true;

                }else if(usedAction) {
                    infoLable.setText("you have used action tile already");
                }else if(madeAMove){
                    infoLable.setText("you cant use action tile after you moved");
                }else {
                    infoLable.setText("you must first insert a tile");
                }
            });
            actionButton[i] = button;
            actionCol.add(button, i, 0, 1, 1);
        }
    }

    private boolean playerLoop(boolean statement) {
        boolean canIperform = false;
        Player[] temp = Game.players[Game.currentTurn % Game.numOfPlayers].getPrevBtPlayers();
        for(int i = 0; i < Game.numOfPlayers; i++) {
            if(Game.currentTurn % Game.numOfPlayers != i){
                boolean doseNotExist = true;
                for(int j = 0; j < temp.length; j++){
                    if(temp[j] != null){
                        if(Game.players[i] == temp[j]){
                            doseNotExist = false;
                        }
                    }
                }
                if(doseNotExist) {
                    playerButtons[i].setDisable(statement);
                    canIperform = true;
                }
            }

        }
        return canIperform;
    }

    private void runbackTrack() {
        Game.players[Game.currentTurn % Game.numOfPlayers].getHand(selectedActionTile).ActionTile(Game.players[chosenPlayer]);
        Game.players[Game.currentTurn % Game.numOfPlayers].addPrevBtPlayers(Game.players[chosenPlayer]);
        chosenPlayer = -1;
    }


}
