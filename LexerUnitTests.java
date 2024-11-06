import org.junit.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class LexerUnitTests {
    private Lexer lexer;

    /**
     * Test that the lexer accepts digit inputs and returns a NUMBER token
     */
    @Test
    public void lexNum() {
        String input = "123 456 789";
        lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.Lex();

        assertEquals(3, tokens.size());
        assertEquals(Token.TokenType.NUMBER, tokens.get(0).getType());
        assertEquals("123", tokens.get(0).getVal());
        assertEquals(Token.TokenType.NUMBER, tokens.get(1).getType());
        assertEquals("456", tokens.get(1).getVal());
        assertEquals(Token.TokenType.NUMBER, tokens.get(2).getType());
        assertEquals("789", tokens.get(2).getVal());
    }

    /**
     * Test that the lexer accepts word inputs and returns a WORD token
     */
    @Test
    public void lexWord()
    {
        String input = "Hey There";
        lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.Lex();

        assertEquals(2, tokens.size());
        assertEquals(Token.TokenType.WORD, tokens.get(0).getType());
        assertEquals("Hey", tokens.get(0).getVal());
        assertEquals(Token.TokenType.WORD, tokens.get(1).getType());
        assertEquals("There", tokens.get(1).getVal());


    }

    /**
     * Test that the lexer accepts separator, word, and digits inputs and returns the correct tokens
     */
    @Test
    public void testLexWithNewLine() {
        String input = "Word 1\nWord 2\n";
        lexer = new Lexer(input);
        LinkedList<Token> tokens = lexer.Lex();

        assertEquals(6, tokens.size());

        assertEquals(Token.TokenType.WORD, tokens.get(0).getType());
        assertEquals("Word", tokens.get(0).getVal());
        assertEquals(Token.TokenType.NUMBER, tokens.get(1).getType());
        assertEquals("1", tokens.get(1).getVal());
        assertEquals(Token.TokenType.SEPARATOR, tokens.get(2).getType());
        assertEquals(Token.TokenType.WORD, tokens.get(3).getType());
        assertEquals("Word", tokens.get(3).getVal());
        assertEquals(Token.TokenType.NUMBER, tokens.get(4).getType());
        assertEquals("2", tokens.get(4).getVal());
        assertEquals(Token.TokenType.SEPARATOR, tokens.get(5).getType());
    }

    /**
     * Test that the lexer accepts digits and word inputs and returns the correct tokens
     */
    @Test
    public void testLexWithNumAndWord()
    {
        String input = "1Word";
        lexer = new Lexer(input);

        LinkedList<Token> tokens = lexer.Lex();
        assertEquals(2, tokens.size());

        assertEquals(Token.TokenType.NUMBER, tokens.get(0).getType());
        assertEquals("1", tokens.get(0).getVal());
        assertEquals(Token.TokenType.WORD, tokens.get(1).getType());
        assertEquals("Word", tokens.get(1).getVal());
    }

    /**
     * Test that the lexer accepts digits and word inputs and returns the correct tokens
     */
    @Test
    public void testFullLine1() {
        Lexer lexer = new Lexer("$0 = tolower($0)");
        LinkedList<Token> tokens = lexer.Lex();

        assertEquals(8, tokens.size());

        assertEquals(Token.TokenType.DOLLARSIGN, tokens.get(0).getType());
        assertEquals(Token.TokenType.NUMBER, tokens.get(1).getType());
        assertEquals(Token.TokenType.EQUALS, tokens.get(2).getType());
        assertEquals(Token.TokenType.WORD, tokens.get(3).getType());
        assertEquals("tolower", tokens.get(3).getVal());
        assertEquals(Token.TokenType.OPENPARENTHESIS, tokens.get(4).getType());
        assertEquals(Token.TokenType.DOLLARSIGN, tokens.get(5).getType());
        assertEquals(Token.TokenType.NUMBER, tokens.get(6).getType());
        assertEquals(Token.TokenType.CLOSEDPARENTHESIS, tokens.get(7).getType());

    }

    /**
     * Test that test a stringliteral with a space in it
     */
    @Test
    public void StringLiteral() {
        String input = "\"Hello World\"";
        Lexer lexer = new Lexer(input);
        Token token = lexer.ProcessStringLiteral();


        assertEquals("Hello World", token.getVal());
        assertEquals(Token.TokenType.STRINGLITERAL, token.getType());
        System.out.println(token.getVal());
    }

    /**
     * Test that test a stringliteral with escaped quotes
     */
    @Test
    public void StringLiteralWithEscapeQuotes() {
        String input2 = "She said, \"hello there\" and then she left.";
        Lexer lexer = new Lexer(input2);
        Token token = lexer.ProcessStringLiteral();

        assertEquals("She said, \"hello there\" and then she left.", token.getVal());
        assertEquals(Token.TokenType.STRINGLITERAL, token.getType());
        System.out.println(token.getVal());
    }


    /**
     * Test that tests a pattern
     */

    @Test
    public void HandlePattern()
    {
        String input = "`1`";
        Lexer lexer = new Lexer(input);
        Token token = lexer.ProcessHandlePattern();
        assertEquals("1", token.getVal());
        assertEquals(Token.TokenType.PATTERN, token.getType());
        System.out.println(token.getVal());
    }

    /**
     * Test that tests two character symbols
     */
    @Test
    public void ProccessTwoSymbols()
    {
        String input = "++";

        Lexer lexer = new Lexer(input);
        Token token = lexer.ProcessSymbol();
        assertEquals(Token.TokenType.PLUSPLUS, token.getType());
        System.out.println(token.getVal());

    }

    /**
     * Test that tests one character symbols
     */
    @Test
    public void ProcessOneSymbol() {
        String input = "+";
        Lexer lexer = new Lexer(input);
        Token token = lexer.ProcessSymbol();
        assertEquals(Token.TokenType.PLUS, token.getType());
        System.out.println(token.getVal());
    }
}
