package ap.scrabble.gameclient.view;

import ap.scrabble.gameclient.model.board.GameData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.*;

public class BoardController implements Initializable {

    MyView myView;

    public void setMyView(MyView v){
        this.myView = v;
    }

    // ------------------------ Game grid ------------------------
    @FXML
    GameGrid gameGrid;
    @FXML
    Label p1name;
    @FXML
    Label p2name;
    @FXML
    Label p3name;
    @FXML
    Label p4name;
    @FXML
    Label p1score;
    @FXML
    Label p2score;
    @FXML
    Label p3score;
    @FXML
    Label p4score;
    @FXML
    Label p1scoreTitle;
    @FXML
    Label p2scoreTitle;
    @FXML
    Label p3scoreTitle;
    @FXML
    Label p4scoreTitle;


    final private byte dl = 1;    // double letter
    final private byte tl = 2;    // triple letter
    final private byte dw = 3;    // double word
    final private byte tw = 4;    // triple word


    private byte[][] boardLayout = {
            {tw, 0, 0, dl, 0, 0, 0, tw, 0, 0, 0, dl, 0, 0, tw},
            {0, dw, 0, 0, 0, tl, 0, 0, 0, tl, 0, 0, 0, dw, 0},
            {0, 0, dw, 0, 0, 0, dl, 0, dl, 0, 0, 0, dw, 0, 0},
            {dl, 0, 0, dw, 0, 0, 0, dl, 0, 0, 0, dw, 0, 0, dl},
            {0, 0, 0, 0, dw, 0, 0, 0, 0, 0, dw, 0, 0, 0, 0},
            {0, tl, 0, 0, 0, tl, 0, 0, 0, tl, 0, 0, 0, tl, 0},
            {0, 0, dl, 0, 0, 0, dl, 0, dl, 0, 0, 0, dl, 0, 0},
            {tw, 0, 0, dl, 0, 0, 0, dw, 0, 0, 0, dl, 0, 0, tw},
            {0, 0, dl, 0, 0, 0, dl, 0, dl, 0, 0, 0, dl, 0, 0},
            {0, tl, 0, 0, 0, tl, 0, 0, 0, tl, 0, 0, 0, tl, 0},
            {0, 0, 0, 0, dw, 0, 0, 0, 0, 0, dw, 0, 0, 0, 0},
            {dl, 0, 0, dw, 0, 0, 0, dl, 0, 0, 0, dw, 0, 0, dl},
            {0, 0, dw, 0, 0, 0, dl, 0, dl, 0, 0, 0, dw, 0, 0},
            {0, dw, 0, 0, 0, tl, 0, 0, 0, tl, 0, 0, 0, dw, 0},
            {tw, 0, 0, dl, 0, 0, 0, tw, 0, 0, 0, dl, 0, 0, tw}
    };

    public void handleBoardClick(MouseEvent event) {
        // check if turn is valid, check if tile is not taken already
        if (gameGrid.getClickedRect().getChildren().stream().noneMatch(child -> child instanceof TileImage)) {
            TileImage temp = null;
            if (selectedTile != null) {
                temp = selectedTile;
                // remove from rack
//                ((StackPane) selectedTile.getParent()).getChildren().clear();
                this.TileRack.getChildren().remove(selectedTile.getParent());
                selectedTile = null;
            }

            // add to grid
            if (temp != null) {
                addTileToGrid(temp);
            }
        }

    }

    private void addTileToGrid(TileImage t){
        t.setOnMouseClicked(this::handleTileOnBoardClick);
        t.setFitHeight(gameGrid.getSquareSize());
        t.setFitWidth(gameGrid.getSquareSize());
        gameGrid.getClickedRect().getChildren().add(t);
        t.setXgrid(gameGrid.getClickedRectX());
        t.setYgrid(gameGrid.getClickedRectY());
//        System.out.println("Letter: " + t.getLetter() + " @ " + gameGrid.getClickedRectX() + "," + gameGrid.getClickedRectY());
        tempPlacedTiles.add(t);
    }

    public void handleTileOnBoardClick(MouseEvent event) {
        TileImage clickedTile = (TileImage) event.getSource();
        tempPlacedTiles.remove(clickedTile);
        ((StackPane) clickedTile.getParent()).getChildren().remove(clickedTile);
        addTileToRack(clickedTile);
    }


    // ------------------------ Tile rack ------------------------
    @FXML
    HBox TileRack;

//    char []tiles = new char[]{'t','s','b', 'u','k','a','l',};
    char []tiles = new char[7];

    final private int tileW = 80;
    final private int tileH = 90;

    TileImage selectedTile;

    ArrayList<TileImage> tempPlacedTiles = new ArrayList<>();

    private void drawTileStack(char[] tiles){
        try {
//            TileImage tileView;
//            StackPane sp;
            for (int i = 0; i < tiles.length; i++) {
                addTileToRack(tiles[i]);
            }
        }catch (Exception e) {
            System.out.println(e);
        }
    }

    private void clearTileStack(){
        TileRack.getChildren().clear();
    }

    private void addTileToRack(TileImage t) {
        addTileToRack(t.getLetter());
    }
    private void addTileToRack(char c){
        TileImage tileView;
        StackPane sp;
        // define tile highlight
        Rectangle tileHighlight = new Rectangle(tileW + 10, tileH + 10, Color.TRANSPARENT);
        tileHighlight.setArcWidth(20);
        tileHighlight.setArcHeight(20);

        // define tile image
//        tileView = new TileImage(new Image("./Tiles/" + c + ".png"), c, tileW, tileH, tileHighlight);
        tileView = new TileImage(new Image("C:\\Users\\edent\\Documents\\GitHub\\advancedprograming\\gameClient\\src\\main\\resources\\Tiles\\" + c + ".png"), c, tileW, tileH, tileHighlight);
        tileView.setOnMouseClicked(this::handleTileClick);

        // define stack and add stack to the tile rack
        sp = new StackPane(tileHighlight, tileView);
        TileRack.getChildren().add(sp);
    }

    public void handleTileClick(MouseEvent event) {
        TileImage clickedTile = (TileImage) event.getSource();
        // toggle highlight character
        clickedTile.toggleHighlight();
        if (selectedTile != null) {
            selectedTile.toggleHighlight();
        }
        selectedTile = clickedTile;

        // delete tile
//        ((StackPane) selectedTile.getParent()).getChildren().clear();

        // get letter
//        System.out.println("Card clicked: " + clickedTile.getLetter());
    }

    public void updatePlayerTiles(char[] c) {
        this.tiles = c;
        clearTileStack();
        drawTileStack(c);
    }

    // ------------------------ Button clicks ------------------------


    // Returns all letters in line with an indentation in the middle for spaces
    public char[] getLettersInLine(int direction, WordInfo wi) {
        // Sort based on direction
        tempPlacedTiles.sort((tile1, tile2) -> {
            if (direction == 1) { // horizontal
                return Integer.compare(tile1.getCoord()[0], tile2.getCoord()[0]); // compare x-coordinates
            } else { // vertical
                return Integer.compare(tile1.getCoord()[1], tile2.getCoord()[1]); // compare y-coordinates
            }
        });

        // Get first and last coordinates
        int[] firstCoordinate = tempPlacedTiles.get(0).getCoord();
        int[] lastCoordinate = tempPlacedTiles.get(tempPlacedTiles.size() - 1).getCoord();

        // Calculate total length and initialize array
        int length = direction == 1 ? lastCoordinate[0] - firstCoordinate[0] + 1 : lastCoordinate[1] - firstCoordinate[1] + 1;
        char[] letters = new char[length];

        // Fill array with letters or nulls
        for (int i = 0; i < length; i++) {
            // Calculate current coordinate
            int col = direction == 1 ? firstCoordinate[0] + i : firstCoordinate[0];
            int row = direction == 1 ? firstCoordinate[1] : firstCoordinate[1] + i;

            // Find tile at current coordinate
            TileImage foundTile = null;
            for (TileImage tile : tempPlacedTiles) {
                if (tile.getCoord()[0] == col && tile.getCoord()[1] == row) {
                    foundTile = tile;
                    break;
                }
            }

            if (i == 0){
                wi.setX(row);
                wi.setY(col);
            }
            // Assign letter to array or null if tile not found
//            letters[i] = (foundTile != null) ? foundTile.getLetter() : null;
            letters[i] = (foundTile != null) ? foundTile.getLetter() : '_';
        }

        return letters;
    }

    private void printLettersInLine(char[] letters) {
        System.out.print("Letters in line: ");
        for (Character letter : letters) {
            if (letter == null) {
                System.out.print("null ");
            } else {
                System.out.print(letter + " ");
            }
        }
        System.out.println();
    }

    private void printDirection() {
        int direction = getWordDirection();
        switch (direction) {
            case 0:
                System.out.println("not valid");
                break;
            case 1:
                System.out.println("horizontal");
                break;
            case 2:
                System.out.println("vertical");
                break;
            default:
                System.out.println("Unexpected value: " + direction);
                break;
        }
    }

    // returns 1 if word is horizontal, 2 if vertical, otherwise 0
    private int getWordDirection() {
        if (tempPlacedTiles.size() < 2) {
            return 0; // 0 or 1 points doesn't define a line direction
        }

        // get the coordinates of the first TileImage
        int[] firstCoordinate = tempPlacedTiles.get(0).getCoord();

        // check vertical or horizontal line
        boolean isVertical = tempPlacedTiles.stream().allMatch(tile -> tile.getCoord()[0] == firstCoordinate[0]);
        if (isVertical) return 2;

        boolean isHorizontal = tempPlacedTiles.stream().allMatch(tile -> tile.getCoord()[1] == firstCoordinate[1]);
        if (isHorizontal) return 1;

        return 0; // not in a line
    }

    private int getEntryLength() {
        if (tempPlacedTiles.size() == 1) {
            return 1;
        }
        // Determine the direction
        int direction = getWordDirection();

        // Sort based on the direction
        tempPlacedTiles.sort((tile1, tile2) -> {
            if (direction == 1) { // horizontal
                return Integer.compare(tile1.getCoord()[0], tile2.getCoord()[0]); // compare x-coordinates
            } else { // vertical
                return Integer.compare(tile1.getCoord()[1], tile2.getCoord()[1]); // compare y-coordinates
            }
        });

        // Get first and last coordinates
        int[] firstCoordinate = tempPlacedTiles.get(0).getCoord();
        int[] lastCoordinate = tempPlacedTiles.get(tempPlacedTiles.size() - 1).getCoord();

        // Calculate total distance
        if (direction == 1) { // horizontal
            return lastCoordinate[0] - firstCoordinate[0] + 1;
        } else { // vertical
            return lastCoordinate[1] - firstCoordinate[1] + 1;
        }
    }

    public void submit(ActionEvent actionEvent) { // send x, y, chars, bool vertical
        System.out.println("Submit clicked");
        if (tempPlacedTiles.size() == 0)
            return;
        printDirection();
        System.out.println("word length: " + getEntryLength());
        char[] c = new char[getEntryLength()];
        if (c.length == 1){
            c[0] = tempPlacedTiles.get(0).getLetter();
            this.myView.submitLetters(c, tempPlacedTiles.get(0).getXgrid(),  tempPlacedTiles.get(0).getYgrid(), true);
            return;
        }
        WordInfo wi = new WordInfo();

        if (getWordDirection() > 0) {
            boolean vertical = false;
            if (getWordDirection() == 2)
                vertical = true;
            c = getLettersInLine(getWordDirection(), wi);
            printLettersInLine(c);

            wi.setVertical(vertical);
            wi.setLetters(c);

            this.myView.submitLetters(wi.getLetters(), wi.getX(), wi.getY(), wi.isVertical());
        }
    }
    public void clear(ActionEvent actionEvent) {
        gameGrid.getChildren().clear();
    }

    public void skip(ActionEvent actionEvent) {
        char randomChar = (char) ('A' + Math.random() * ('Z' - 'A' + 1));

        addTileToRack(randomChar);
        System.out.println(this.TileRack.getChildren());
    }

    // -------------------------------Init-----------------------------------
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.gameGrid.setBoardLayout(this.boardLayout);
        this.gameGrid.setOnMouseClicked(mouseEvent -> {
//            System.out.println("clicked on board");
            handleBoardClick(mouseEvent);
        });
        drawTileStack(this.tiles);
        this.TileRack.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.DOTTED, CornerRadii.EMPTY, BorderStroke.MEDIUM)));

    }



    // -------------------------------Init-----------------------------------

    /**

     Sets the names of the board windows for each player.

     This method retrieves the player names from the view and assigns them to the corresponding board windows.

     The visibility of the board windows and related components is also adjusted based on the number of players.
     */
    public void setBoardWindowNames() {
        List<String> playerNames = this.myView.ViewGetPlayerNames();
        int numOfPlayers = playerNames.size();

        switch (numOfPlayers) {
            case 4:
                p4name.setText(playerNames.get(3));
                p4name.setVisible(true);
                p4scoreTitle.setVisible(true);
                p4score.setVisible(true);
            case 3:
                p3name.setText(playerNames.get(2));
                p3name.setVisible(true);
                p3scoreTitle.setVisible(true);
                p3score.setVisible(true);
            case 2:
                p2name.setText(playerNames.get(1));
                p2name.setVisible(true);
                p2scoreTitle.setVisible(true);
                p2score.setVisible(true);
            case 1:
                p1name.setText(playerNames.get(0));
                p1name.setVisible(true);
                p1scoreTitle.setVisible(true);
                p1score.setVisible(true);
                break;
        }
    }

    /**
     * Retrieves the score for the specified player from the view.
     * @param playerName The name of the player.
     */
    public void getScore(String playerName) {
        // Call the view to get the score for the player
        Integer playerScore = this.myView.ViewGetScore(playerName);
    }

    /**
     * Sets the scores on the board for all players.
     * It retrieves the player names from the view and updates the score labels accordingly.
     */
    public void setScoreOnBoard() {
        // Get the list of player names from the view
        List<String> playerNames = this.myView.ViewGetPlayerNames();

        // Iterate over the player names and update the score labels
        for (String playerName : playerNames) {

            // Get the score for the current player
            int playerScore = this.myView.ViewGetScore(playerName);

            // Update the score label based on the player index
            switch (playerNames.indexOf(playerName)) {
                case 0:
                    p1score.setText(String.valueOf(playerScore));
                    break;
                case 1:
                    p2score.setText(String.valueOf(playerScore));
                    break;
                case 2:
                    p3score.setText(String.valueOf(playerScore));
                    break;
                case 3:
                    p4score.setText(String.valueOf(playerScore));
                    break;
            }
        }
    }


    /**
     Updates the score of a player.
     @param playerName The name of the player whose score needs to be updated.
     @param score The new score to be assigned to the player.
     */
    public void updatePlayerScore(String playerName, int score) {
        if (playerName.equals(p1name.getText())) {
            p1score.setText(Integer.toString(score));
        } else if (playerName.equals(p2name.getText())) {
            p2score.setText(Integer.toString(score));
        } else if (playerName.equals(p3name.getText())) {
            p3score.setText(Integer.toString(score));
        } else if (playerName.equals(p4name.getText())) {
            p4score.setText(Integer.toString(score));
        }
    }



}
