package model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import model.converters.CellStateConverter;

import java.util.UUID;

@DynamoDBTable(tableName = "Cell")
public class Cell {

    private String id;
    private String gameToken;
    private CellState cellState;
    private int row;
    private int col;

    public Cell(CellState cellState, int row, int col, String gameToken) {
        this.id = UUID.randomUUID().toString();
        this.cellState = cellState;
        this.row = row;
        this.col = col;
        this.gameToken = gameToken;
    }

    public Cell() { }

    @DynamoDBHashKey(attributeName = "id")
    public String getId() {
        return id;
    }

    @DynamoDBTypeConverted(converter = CellStateConverter.class)
    @DynamoDBAttribute(attributeName = "cellState")
    public CellState getCellState() {
        return cellState;
    }

    @DynamoDBAttribute(attributeName = "cellRow")
    public int getRow() {
        return row;
    }

    @DynamoDBAttribute(attributeName = "cellCol")
    public int getCol() {
        return col;
    }

    @DynamoDBAttribute(attributeName = "gameToken")
    public String getGameToken() {
        return gameToken;
    }

    public void setGameToken(String gameToken) {
        this.gameToken = gameToken;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCellState(CellState cellState) {
        this.cellState = cellState;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
