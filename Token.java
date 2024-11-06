import java.util.LinkedList;
import java.util.Optional;

class Token{

    private TokenType type;
    private String val;
    private int line;
    private int position;



    public enum TokenType {
        WORD, NUMBER, SEPARATOR, KEYWORD, STRINGLITERAL, PATTERN, RightArrowEqual, PLUSPLUS, MINUSMINUS, LEFTARROWEQUAL,
        EQUALSEQUALS, NOTEQUAL, EXPEQUALS, PERCENTEQUALS, STAREQUALS, SLASHEQUALS, PLUSEQUALS, MINUSEQUALS,
        EXCLAMATIONTILDE, ANDAND, RIGHTSHIFT, OROR, OPENBRACE, CLOSEDBRACE,CLOSEDBRACKET, OPENBRACKET, OPENPARENTHESIS,
        CLOSEDPARENTHESIS, DOLLARSIGN, TILDE, EQUALS, LEFTARROW, RIGHTARROW, PLUS, EXP, MINUS, QUESTIONMARK,
        COLON, STAR, SLASH, PERCENT, SEMICOLON, NEWLINE, OR, COMMA, NOT,
    }
    /**
     * Constructor for Token
     * @param type
     * @param line
     * @param position
     */
    public Token(TokenType type, int line, int position)
    {
        this.type = type;
        this.val = null;
        this.line = line;
        this.position = position;
    }

    /**
     * Constructor for Token that takes in a value
     * @param type
     * @param val
     * @param line
     * @param position
     */
    public Token(TokenType type, String val, int line, int position)
    {
        this.type = type;
        this.val = val;
        this.line = line;
        this.position = position;
    }

    /**
     * Getter methods for Token
     * @return
     */
    public TokenType getType()
    {
        return type;
    }
    public String getVal()
    {
        return val;
    }
    public int getLine()
    {
        return line;
    }
    public int getPosition()
    {
        return position;
    }
    /**
     * toString method for Token
     * @return the type and value of the token if it has a value, or just the type if it does not
     */
    public  String toString() {
        if (val != null) {
            if(type == TokenType.KEYWORD)
                return val;
            else
            {
                return type + " (" + val + ")";
            }
        } else {
            return type.toString();
        }
    }
}
/**
 * TokenManager class manages the token stream by taking in a LinkedList of tokens and having methods that allow the parser to look at a token at a specific index,
 * verifies if there are more tokens, and matches and removes a token.
 */
class TokenManager {
    LinkedList<Token> tokens;

    /**
     * TokenManager constructor that takes in a LinkedList of tokens and sets it to the tokens variable.
     * @param tokens
     */
    public TokenManager(LinkedList<Token> tokens) {
        this.tokens = tokens;
    }

    /**
     * Peek method allows the parser to look at a token at a specific index. It first checks if tokens.size() is greater than 0.
     * If it is, it returns an Optional containing the token. If it is not, it returns an Optional.empty().
     * @param j
     * @return Optional<Token>
     */
    Optional<Token> Peek(int j) {
        if (tokens.size() > 0) {
            return Optional.of(tokens.get(j));
        } else {
            return Optional.empty();
        }
    }

    /**
     * MoreTokens method that checks if tokens.size() is greater than 0. If it is, it returns true. If it is not, it returns false.
     * @return true if tokens.size() > 0
     */
    boolean MoreTokens() {
        return tokens.size() > 0;
    }

    /**
     * MatchandRemove method that takes in a token type and removes it from the list. It first checks if tokens.size() is greater than 0.
     * If it is, it checks if the token type is equal to the token type being passed. If it is, it removes from the list and returns the token.
     * If it is not, it returns an empty optional.
     * @param t
     * @return Optional<Token>
     */
    Optional<Token> MatchandRemove(Token.TokenType t) {
        if (tokens.size() > 0) {
            Token token = tokens.get(0);
            if (token.getType() == t) {
                tokens.remove(0);
                return Optional.of(token);

            }
        }
        return Optional.empty();
    }
}


