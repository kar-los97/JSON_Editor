package lexing;

public class Lexem {
    private int row;
    private int column;

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    private String value;

    public Lexem(){
    }

    public Lexem(String value){
        this.value = value;
    }

    public Lexem(int row, int column, String value) {
        this.row = row;
        this.column = column;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value + "("+row+", "+column+")";
    }
}
