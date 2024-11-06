import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

class Lexer {
    private String file;
    private  StringHandler handler;
    private int line;
    private int position;
    private LinkedList<Token> tokens;
    public static Map<String, Token.TokenType> keywords;
    public static Map<String, Token.TokenType> twoCharSymbols;
    public static Map<Character, Token.TokenType> oneCharSymbols;


    /**
     * Constructor for Lexer, contructs the Lexer object
     *
     * @param file the AwkFile's contents
     */
    public Lexer(String file) {
        this.file = file;
        handler = new StringHandler(file);
        line = 1;
        position = 0;
        tokens = new LinkedList<>();
        keywords = new HashMap<>();
        twoCharSymbols = new HashMap<>();
        oneCharSymbols = new HashMap<>();
        AddtoKeywords();
        AddtoTwoCharSymbols();
        AddtoOneCharSymbols();

    }

    private void AddtoKeywords() {
        keywords.put("while", Token.TokenType.KEYWORD);
        keywords.put("if", Token.TokenType.KEYWORD);
        keywords.put("do", Token.TokenType.KEYWORD);
        keywords.put("for", Token.TokenType.KEYWORD);
        keywords.put("break", Token.TokenType.KEYWORD);
        keywords.put("continue", Token.TokenType.KEYWORD);
        keywords.put("else", Token.TokenType.KEYWORD);
        keywords.put("return", Token.TokenType.KEYWORD);
        keywords.put("BEGIN", Token.TokenType.KEYWORD);
        keywords.put("END", Token.TokenType.KEYWORD);
        keywords.put("print", Token.TokenType.KEYWORD);
        keywords.put("printf", Token.TokenType.KEYWORD);
        keywords.put("next", Token.TokenType.KEYWORD);
        keywords.put("in", Token.TokenType.KEYWORD);
        keywords.put("delete", Token.TokenType.KEYWORD);
        keywords.put("getline", Token.TokenType.KEYWORD);
        keywords.put("exit", Token.TokenType.KEYWORD);
        keywords.put("nextfile", Token.TokenType.KEYWORD);
        keywords.put("function", Token.TokenType.KEYWORD);
    }

    private void AddtoTwoCharSymbols() {
        twoCharSymbols.put(">=", Token.TokenType.RightArrowEqual);
        twoCharSymbols.put("++", Token.TokenType.PLUSPLUS);
        twoCharSymbols.put("--", Token.TokenType.MINUSMINUS);
        twoCharSymbols.put("<=", Token.TokenType.LEFTARROWEQUAL);
        twoCharSymbols.put("==", Token.TokenType.EQUALSEQUALS);
        twoCharSymbols.put("!=", Token.TokenType.NOTEQUAL);
        twoCharSymbols.put("^=", Token.TokenType.EXPEQUALS);
        twoCharSymbols.put("%=", Token.TokenType.PERCENTEQUALS);
        twoCharSymbols.put("*=", Token.TokenType.STAREQUALS);
        twoCharSymbols.put("/=", Token.TokenType.SLASHEQUALS);
        twoCharSymbols.put("+=", Token.TokenType.PLUSEQUALS);
        twoCharSymbols.put("-=", Token.TokenType.MINUSEQUALS);
        twoCharSymbols.put("!~", Token.TokenType.EXCLAMATIONTILDE);
        twoCharSymbols.put("&&", Token.TokenType.ANDAND);
        twoCharSymbols.put(">>", Token.TokenType.RIGHTSHIFT);
        twoCharSymbols.put("||", Token.TokenType.OROR);
    }

    private void AddtoOneCharSymbols() {
        oneCharSymbols.put('{', Token.TokenType.OPENBRACE);
        oneCharSymbols.put('}', Token.TokenType.CLOSEDBRACE);
        oneCharSymbols.put('[', Token.TokenType.OPENBRACKET);
        oneCharSymbols.put(']', Token.TokenType.CLOSEDBRACKET);
        oneCharSymbols.put('(', Token.TokenType.OPENPARENTHESIS);
        oneCharSymbols.put(')', Token.TokenType.CLOSEDPARENTHESIS);
        oneCharSymbols.put('$', Token.TokenType.DOLLARSIGN);
        oneCharSymbols.put('~', Token.TokenType.TILDE);
        oneCharSymbols.put('=', Token.TokenType.EQUALS);
        oneCharSymbols.put('<', Token.TokenType.LEFTARROW);
        oneCharSymbols.put('>', Token.TokenType.RIGHTARROW);
        oneCharSymbols.put('!', Token.TokenType.NOT);
        oneCharSymbols.put('+', Token.TokenType.PLUS);
        oneCharSymbols.put('^', Token.TokenType.EXP);
        oneCharSymbols.put('-', Token.TokenType.MINUS);
        oneCharSymbols.put('?', Token.TokenType.QUESTIONMARK);
        oneCharSymbols.put(':', Token.TokenType.COLON);
        oneCharSymbols.put('*', Token.TokenType.STAR);
        oneCharSymbols.put('/', Token.TokenType.SLASH);
        oneCharSymbols.put('%', Token.TokenType.PERCENT);
        oneCharSymbols.put(';', Token.TokenType.SEPARATOR);
        oneCharSymbols.put('\n', Token.TokenType.SEPARATOR);
        oneCharSymbols.put('|', Token.TokenType.OR);
        oneCharSymbols.put(',', Token.TokenType.COMMA);
    }

    /**
     * The lex method tokenizes the input text and creates a list of tokens
     * to the corresponding Token type
     *
     * @return A linked list containing the tokens
     */
    public LinkedList<Token> Lex() {
        while (handler.isDone()) {
            char c = handler.Peek(0);
            switch (c) {
                case ' ':
                case '\t':
                    handler.GetChar();
                    position++;
                    break;

                case '\n':
                case ';':
                    while (handler.Peek(0) == '\n' || handler.Peek(0) == ';') {
                        handler.GetChar();
                    }
                    tokens.add(new Token(Token.TokenType.SEPARATOR, null, line, position));
                    line++;
                    position = 0;
                    break;

                case '#':
                    while (handler.Peek(0) != '\n' && handler.isDone()) {
                        handler.Swallow(1);
                    }
                    line++;
                    break;

                case '"':
                    tokens.add(ProcessStringLiteral());
                    break;

                case '`':
                    tokens.add(ProcessHandlePattern());
                    break;

                case '\r':
                    handler.GetChar();
                    break;

                default:
                    Token token = ProcessSymbol();
                    if (token != null) {
                        tokens.add(token);
                    } else if (Character.isLetter(c)) {
                        tokens.add(ProcessWord());

                    } else if (Character.isDigit(c)) {
                        tokens.add(ProcessNumber());
                    } else {
                        tokens.add(new Token(Token.TokenType.SEPARATOR,  line, position++));
                        handler.GetChar();
                    }
                    break;
            }
        }
        return tokens;
    }

    /**
     * ProcessWord method for Lexer creates a token of type WORD
     * and adds it to the list of tokens. If the word is empty, it creates a token of type SEPARATOR
     */
    private Token ProcessWord() {
        String word = "";
        while (Character.isLetterOrDigit(handler.Peek(0)) || handler.Peek(0) == '_') {
            word = word + handler.GetChar();
        }
        if (keywords.containsKey(word)) {
            return new Token(Token.TokenType.KEYWORD, word, line, position++);
        } else if (word.isEmpty()) {
            return new Token(Token.TokenType.SEPARATOR, null, line, position++);

        } else {
            return new Token(Token.TokenType.WORD, word, line, position++);

        }
    }

    /**
     * ProcessNumber method for Lexer creates a token of type NUMBER
     * and adds it to the list of tokens. If the number includes more than one decimal point,
     * it stops and puts the first number in the token list
     */
    private Token ProcessNumber() {

        String num = "";
        while (Character.isDigit(handler.Peek(0)) || handler.Peek(0) == '.') {
            num = num + handler.GetChar();
            if (num.contains(".")) {
                if (num.indexOf(".") != num.lastIndexOf(".")) {
                    break;
                }

            }
        }
        return new Token(Token.TokenType.NUMBER, num, line, position++);
    }

    /**
     * ProcessSymbol method for Lexer creates a token of type STRINGLITERAL.
     * It reads the first double quote and then reads characters until it finds the second double quote.
     * When reading if it finds a backslash, it continues reading the next character until it finds the second backslash
     * If it does it adds a token of type STRINGLITERAL to the list of tokens. If it doesn't it throws an exception
     */
    Token ProcessStringLiteral() {
        handler.GetChar();
        String string = "";
        boolean escaped = false;

        while (handler.isDone()) {
            char c = handler.GetChar();

            if (escaped) {
                if (c != '"') {
                    string += "\\" + c;
                } else {
                    string += "\"";
                }
                escaped = false;
            } else if (c == '"') {
                return new Token(Token.TokenType.STRINGLITERAL, string, line, position++);
            } else if (c == '\\') {
                escaped = true;
            } else {
                string += c;
            }
        }
        throw new IllegalArgumentException("String at line " + line + " and position " + position + " is not closed");
    }





    /**
     * ProcessHandlePattern method for Lexer creates a token of type PATTERN '`'
     * It reads the first backtick and then reads characters until it finds another backtick
     * If it doesn't find another backtick, it throws an exception
     * @return A token of type PATTERN ` or exception
     */
    Token ProcessHandlePattern() {
        handler.GetChar();
        StringBuilder pattern = new StringBuilder();
        while (handler.isDone()) {
            char c = handler.GetChar();
            if (c == '`') {
                return new Token(Token.TokenType.PATTERN, pattern.toString(), line, position++);
            } else {
                pattern.append(c);
            }

        }
        throw new IllegalArgumentException("Pattern at line " + line + " and position " + position + " is not closed");
    }

    /**
     * ProcessSymbol method for Lexer creates a token of type SYMBOL
     * It reads the first character and then reads the next character.
     * If the two characters are in the twoCharSymbols map, it adds a token of type SYMBOL to the list of tokens.
     * If the first character is in the oneCharSymbols map, it adds a token of type SYMBOL to the list of tokens.
     * If the first character and second character are not in the maps, it returns null.
     * @return A token of type SYMBOL or null
     */
    Token ProcessSymbol() {
        String symbol = " ";
        String peeksymbol = " ";
        while (handler.isDone()) {
            char c = handler.Peek(0);
            char next = handler.Peek(1);
            symbol = "" + c + next;
            if (twoCharSymbols.containsKey(symbol)) {
                handler.GetChar();
                handler.GetChar();
                return new Token(twoCharSymbols.get(symbol), symbol, line, position++);
            } else if (oneCharSymbols.containsKey(c)) {
                handler.GetChar();
                return new Token(oneCharSymbols.get(c), symbol, line, position++);
            }
            else{
                break;
            }
        }
        return null;
    }
}