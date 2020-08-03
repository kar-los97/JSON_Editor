package lexing;

public class Lexem {
    private String value;

    public Lexem(){
    }

    public Lexem(String value){
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
        return this.value;
    }
}
