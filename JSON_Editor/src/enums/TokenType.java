package enums;

public enum TokenType {
    WHITE_SPACE(" "),
    CURLY_BRACKET_START ("{"),
    CURLY_BRACKET_END("}"),
    SQUARE_BRACKET_START("["),
    SQUARE_BRACKET_END("]"),
    BRACKET_START("("),
    BRACKET_END(")"),
    COMMA(","),
    APOSTROPHE("'"),
    QUONTATION_MARKS("\""),
    COLON(":"),
    NUMBER(""),
    BOOLEAN(""),
    STRING(""),
    OTHER("");

    private String value;
    public String getValue(){
        return value;
    }
    TokenType(String value) {
        this.value = value;
    }
}
