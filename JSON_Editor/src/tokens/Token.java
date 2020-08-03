package tokens;

import enums.TokenType;

public class Token {
    private TokenType typeOfToken;
    private String value;

    public Token(String value) {
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

    public Token(TokenType type, String value) {
        this.typeOfToken = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return typeOfToken + "("+value+")";
    }
}
