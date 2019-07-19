public class Token {
    String value;
    TokenType type;

    @Override
    public String toString() {
        return value + " ,   Type = " + type.toString();
    }
}
