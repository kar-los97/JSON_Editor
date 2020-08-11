package tokens;

import enums.TokenType;

public class Token {
    private int row;
    private int column;
    private TokenType typeOfToken;
    public TokenType getTypeOfToken(){
        return  typeOfToken;
    }
    private String value;

    public Token(String value,int row, int column) {
        this.row = row;
        this.column = column;
        this.value = value;
        typeOfToken = TokenType.STRING;
        try{
            Double.parseDouble(value);
            typeOfToken = TokenType.NUMBER;
        }catch (NumberFormatException ex){
            if(value.equals("true")||value.equals("false")){
                typeOfToken = TokenType.BOOLEAN;
            }else {
                for (TokenType t : TokenType.values()) {
                    if (value.equals(t.getValue())) {
                        typeOfToken = t;
                        break;
                    }
                }
            }
        }


    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public String getValue() {
        return value;
    }

    public Token(TokenType type, String value, int row, int column) {
        this.typeOfToken = type;
        this.value = value;
        this.row = row;
        this.column = column;
    }

    @Override
    public String toString() {
        return typeOfToken + "("+value+")"+" Position: ("+row+", "+column+")";
    }
}
