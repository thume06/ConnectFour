package game;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

//NOTE: Columns will always come before rows in this code.

public class GameScreen implements Initializable {
    private Main mainClass;

    String gameTable[][] = new String[7][6]; //7 columns, 6 rows
    ImageView imageTable[][] = new ImageView[7][6]; //7 columns, 6 rows
    String turn = "Black";

    @FXML GridPane gameGrid;
    @FXML Label winLabel;

    public void initialize(URL url, ResourceBundle rb) {
        mainClass = Main.getInstance();
        DeclareStringArray();
        hover.setImage(new Image("hover.png"));
        hover.setVisible(false);
        winLabel.setDisable(true);
    }

    //Creates a 2D array of strings that all equal "Empty" Called in the initialize method.
    public void DeclareStringArray(){
        int columnCount = 0;
        while(columnCount < 7){
            int rowCount = 0;
            while(rowCount < 6){
                gameTable[columnCount][rowCount] = "Empty";
                rowCount++;
            }
            columnCount++;
        }
    }

    //-----Column ImageViews. These are what will be clicked/hovered rather than the individual ImageViews in the GridPane.-----//
    @FXML ImageView column0;
    @FXML ImageView column1;
    @FXML ImageView column2;
    @FXML ImageView column3;
    @FXML ImageView column4;
    @FXML ImageView column5;
    @FXML ImageView column6;

    //--------------Column pressed methods. Execute when that column's ImageView is clicked on. Column(column#)Pressed.------------//
    public void Column0Pressed(){
        ColumnPressed(0);
    }
    public void Column1Pressed(){
        ColumnPressed(1);
    }
    public void Column2Pressed(){
        ColumnPressed(2);
    }
    public void Column3Pressed(){
        ColumnPressed(3);
    }
    public void Column4Pressed(){
        ColumnPressed(4);
    }
    public void Column5Pressed(){
        ColumnPressed(5);
    }
    public void Column6Pressed(){
        ColumnPressed(6);
    }

    //All of the above column pressed methods use this method and input a different value depending on the column#
    public void ColumnPressed(int columnNum){
        //First find the lowest open slot, if there is one
        int lowestSlot = LowestSlot(columnNum);
        if(lowestSlot == 99){ //LowestSlot method returns 99 if it is full. This should not happen though. ImageView
            return;           //should be disabled as soon as the column is full... it is no longer hoverable/clickable
        }
        if(turn.equals("Black")){
            imageTable[columnNum][lowestSlot] = new ImageView(new Image("testBlack.png"));
            imageTable[columnNum][lowestSlot].setFitHeight(100);
            imageTable[columnNum][lowestSlot].setFitWidth(100);
            gameGrid.add(imageTable[columnNum][lowestSlot], columnNum, lowestSlot);
            gameTable[columnNum][lowestSlot] = "Black";
            if(IsDraw()){
                gameGrid.setOpacity(.4);
                winLabel.setOpacity(1);
                winLabel.setText("Draw!");
            }
            else if(IsWon()){
                DisableAllColumns();
                gameGrid.setOpacity(.4);
                winLabel.setOpacity(1);
                winLabel.setText("Black Wins!");
            }
            if(lowestSlot == 0){
                DisableColumn(columnNum);
            }
            turn = "Red";
        }
        else if(turn.equals("Red")){
            imageTable[columnNum][lowestSlot] = new ImageView(new Image("testRed.png"));
            imageTable[columnNum][lowestSlot].setFitHeight(100);
            imageTable[columnNum][lowestSlot].setFitWidth(100);
            gameGrid.add(imageTable[columnNum][lowestSlot], columnNum, lowestSlot);
            gameTable[columnNum][lowestSlot] = "Red";
            if(IsDraw()){
                gameGrid.setOpacity(.4);
                winLabel.setOpacity(1);
                winLabel.setText("Draw!");
            }
            else if(IsWon()){
                DisableAllColumns();
                gameGrid.setOpacity(.4);
                winLabel.setOpacity(1);
                winLabel.setText("Red Wins!");
            }
            if(lowestSlot == 0){
                DisableColumn(columnNum);
            }
            turn = "Black";
        }
    }

    //This method is used by ColumnPressed above. It returns the row# of the lowest open slot, or 99 if there are none
    public int LowestSlot(int columnNum){
        int cNum = columnNum;
        int count = 5;
        while(count >= 0){
            if(gameTable[columnNum][count].equals("Empty")){
                return count;
            }
            count--;
        }
        return 99;
    }

    //This method is called once a column is filled up too far
    public void DisableColumn(int columnNum){
        if(columnNum == 0){
            column0.setDisable(true);
        }
        else if(columnNum == 1){
            column1.setDisable(true);
        }
        else if(columnNum == 2){
            column2.setDisable(true);
        }
        else if(columnNum == 3){
            column3.setDisable(true);
        }
        else if(columnNum == 4){
            column4.setDisable(true);
        }
        else if(columnNum == 5){
            column5.setDisable(true);
        }
        else{
            column6.setDisable(true);
        }
    }

    //This method is called in the ColumnPressed method. Always done to check if someone has won.
    public boolean IsWon(){
        for (int r = 0; r < 6; r++) { // iterate rows, top to bottom
            for (int c = 0; c < 7; c++) { // iterate columns, left to right
                String color = gameTable[c][r];
                if (color.equals("Empty"))
                    continue; // don't check empty slots

                if (r + 3 < 6 &&
                        color.equals(gameTable[c][r+1]) && // look up
                        color.equals(gameTable[c][r+2]) &&
                        color.equals(gameTable[c][r+3]))
                    return true;
                if (c + 3 < 7) {
                    if (color.equals(gameTable[c+1][r]) && // look right
                            color.equals(gameTable[c+2][r]) &&
                            color.equals(gameTable[c+3][r]))
                        return true;
                    if (r - 3 >= 0 &&
                            color.equals(gameTable[c+1][r-1]) && // look up & right
                            color.equals(gameTable[c+2][r-2]) &&
                            color.equals(gameTable[c+3][r-3]))
                        return true;
                    if (c - 3 >= 0 && r - 3 >= 0 &&
                            color.equals(gameTable[c-1][r-1]) && // look up & left
                            color.equals(gameTable[c-2][r-2]) &&
                            color.equals(gameTable[c-3][r-3]))
                        return true;
                }
            }
        }
        return false;
    }

    //Called after every move. Checks if the match is a draw
    public boolean IsDraw(){
        if(column0.isDisabled() &&
                column1.isDisabled() &&
                column2.isDisabled() &&
                column3.isDisabled() &&
                column4.isDisabled() &&
                column5.isDisabled() &&
                column6.isDisabled()){
            return true;
        }
        else{
            return false;
        }
    }

    //Disables all column ImageViews. Called when the match is won.
    public void DisableAllColumns(){
        column0.setDisable(true);
        column1.setDisable(true);
        column2.setDisable(true);
        column3.setDisable(true);
        column4.setDisable(true);
        column5.setDisable(true);
        column6.setDisable(true);
    }

    //--------Column hovered methods. Execute when the mouse is over that column. Column(column#)Hovered---------//
    public void Column0Hovered(){
        ColumnHovered(0);
    }
    public void Column1Hovered(){
        ColumnHovered(1);
    }
    public void Column2Hovered(){
        ColumnHovered(2);
    }
    public void Column3Hovered(){
        ColumnHovered(3);
    }
    public void Column4Hovered(){
        ColumnHovered(4);
    }
    public void Column5Hovered(){
        ColumnHovered(5);
    }
    public void Column6Hovered(){
        ColumnHovered(6);
    }

    //The image displayed when hovering
    @FXML ImageView hover;

    //All of the above column hovered methods use this method and input a different value depending on the column#
    public void ColumnHovered(int columnNum){
        hover.setLayoutX(100 * columnNum);
        hover.setVisible(true);
    }

    //This method is used when the mouse stops hovering over any column ImageView with a hover method.
    public void ColumnExited(){
        hover.setVisible(false);
    }
}
