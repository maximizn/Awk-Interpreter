import org.junit.Assert;
import org.junit.Test;
import java.util.LinkedList;
import java.util.Optional;
import static org.junit.Assert.*;


public class ParserUnitTests {

    /**
     * Test that tests method MatchandRemove
     */
    @Test
    public void MatchandRemove() {
        LinkedList<Token> tokens = new LinkedList<>();
        tokens.add(new Token(Token.TokenType.KEYWORD, "BEGIN", 0, 0));
        TokenManager tokenManager = new TokenManager(tokens);
        Optional<Token> removedToken = tokenManager.MatchandRemove(Token.TokenType.KEYWORD);
        assertTrue(removedToken.isPresent());
        assertEquals("BEGIN", removedToken.get().getVal());
        assertEquals(Token.TokenType.KEYWORD, removedToken.get().getType());
        assertEquals(0, tokens.size());
        System.out.println(removedToken);
        System.out.println(tokens);
    }

    /**
     * Test that tests method MatchandRemove with empty list
     */
    @Test
    public void MatchandRemoveEmpty() {
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tokenManager = new TokenManager(tokens);
        assertFalse(tokenManager.MatchandRemove(Token.TokenType.KEYWORD).isPresent());
        System.out.println(tokens);

    }

    /**
     * Test that test method acceptSeparartors with separators
     */
    @Test
    public void acceptSeparators() {
        LinkedList<Token> tokens = new LinkedList<>();
        tokens.add(new Token(Token.TokenType.NUMBER, "123", 0, 0));
        tokens.add(new Token(Token.TokenType.SEPARATOR, ";", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "456", 0, 0));
        tokens.add(new Token(Token.TokenType.SEPARATOR, "\n", 0, 0));
        TokenManager TokenManager = new TokenManager(tokens);
        Parser parser = new Parser(TokenManager);
        boolean result = parser.AcceptSeparators();
        assertEquals(4, tokens.size());
        System.out.println(result);
        System.out.println(tokens);


    }

    /**
     * Test that test method acceptSeparartors without separators
     */
    @Test
    public void acceptSeparatorsWithoutSeperators() {
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tokenManager = new TokenManager(tokens);
        Parser parser = new Parser(tokenManager);
        boolean result = parser.AcceptSeparators();
        assertFalse(parser.AcceptSeparators());
        assertEquals(0, tokens.size());
        System.out.println(result);
        System.out.println(tokens);
    }

    /**
     * Test that tests method Parse. It should throw an exception because it is empty list when parseFunction and ParseAction are done being called
     */
    @Test
    @SuppressWarnings("Parsing error")
    public void Parse() {
        LinkedList<Token> tokens = new LinkedList<>();

        tokens.add(new Token(Token.TokenType.KEYWORD, "function", 0, 0));
        tokens.add(new Token(Token.TokenType.WORD, "Bills", 0, 0));
        TokenManager tokenManager = new TokenManager(tokens);
        Parser parser = new Parser(tokenManager);

        try {
            ProgramNode programNode = parser.Parse();
            assertEquals(1, programNode.function.size());
            assertEquals("Bills", programNode.function.get(0).functionName);

        } catch (Exception e) {
            e.getMessage();
        }
    }


    /**
     * Test that tests method ParseAction with keyword END
     */
    @Test
    public void ParseActionEND() throws Exception {
        LinkedList<Token> tokens = new LinkedList<>();
        tokens.add(new Token(Token.TokenType.KEYWORD, "END", 0, 0));
        tokens.add(new Token(Token.TokenType.OPENBRACKET, "{", 0, 0));
        tokens.add(new Token(Token.TokenType.CLOSEDBRACKET, "}", 0, 0));
        ParseAction(tokens);

    }

    /**
     * Test that tests method ParseFunction including statements
     */
    @Test
    public void ParseFunction() {
        LinkedList<Token> tokens = new LinkedList<>();
        tokens.add(new Token(Token.TokenType.KEYWORD, "function", 0, 0));
        tokens.add(new Token(Token.TokenType.WORD, "Bills", 0, 0));
        tokens.add(new Token(Token.TokenType.OPENPARENTHESIS, "(", 0, 0));
        tokens.add(new Token(Token.TokenType.WORD, "x", 0, 0));
        tokens.add(new Token(Token.TokenType.COMMA, ",", 0, 0));
        tokens.add(new Token(Token.TokenType.WORD, "y", 0, 0));
        tokens.add(new Token(Token.TokenType.CLOSEDPARENTHESIS, ")", 0, 0));
        tokens.add(new Token(Token.TokenType.OPENBRACKET, "{", 0, 0));
        tokens.add(new Token(Token.TokenType.WORD, "x", 0, 0));
        tokens.add(new Token(Token.TokenType.PERCENTEQUALS, "%=", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "5", 0, 0));
        tokens.add(new Token(Token.TokenType.CLOSEDBRACKET, "}", 0, 0));
        ProgramNode programNode = new ProgramNode(new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
        Parser parser = new Parser(new TokenManager(tokens));
        boolean result = parser.ParseFunction(programNode);
        assertEquals(1, programNode.function.size());
        FunctionDefinitionNode functionDefinitionNode = programNode.function.get(0);
        assertEquals("Bills", functionDefinitionNode.functionName);
        assertEquals(2, functionDefinitionNode.parameters.size());
        assertEquals("x", functionDefinitionNode.parameters.get(0));
        assertEquals("y", functionDefinitionNode.parameters.get(1));
        assertEquals(0, functionDefinitionNode.statements.size());
        assertTrue(result);
        System.out.println(functionDefinitionNode);

    }

    /**
     * ParseAction Method that is used in test ParseActionBEGIN and test ParseActionEND
     * @param tokens LinkedList of tokens
     */
    private void ParseAction(LinkedList<Token> tokens) throws Exception{
        TokenManager tokenManager = new TokenManager(tokens);
        ProgramNode programNode = new ProgramNode(new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
        Parser parser = new Parser(tokenManager);
        boolean result = parser.ParseAction(programNode);
        assertTrue(result);
        System.out.println(programNode);
    }
    /**
     * Tests for a PreIncrement with a $ and Word
     */
    @Test
    public void PreIncrement$() throws Exception {
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tokenManager = new TokenManager(tokens);
        Parser parser = new Parser(tokenManager);
        tokens.add(new Token(Token.TokenType.KEYWORD, "END", 0, 0));
        tokens.add(new Token(Token.TokenType.OPENBRACKET, "{", 0, 0));
        tokens.add(new Token(Token.TokenType.PLUSPLUS, "++", 0, 0));
        tokens.add(new Token(Token.TokenType.DOLLARSIGN, "$", 0, 0));
        tokens.add(new Token(Token.TokenType.WORD, "b", 0, 0));
        tokens.add(new Token(Token.TokenType.CLOSEDBRACKET, "}", 0, 0));
        Optional<Node> result = Optional.ofNullable(parser.Parse());
        System.out.println(result);
        Assert.assertTrue(result.get() instanceof ProgramNode);

    }

    /**
     * Tests for a Parentheses with a PostIncrement
     */
    @Test
    public void PreIncrementParantheses() throws Exception {
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tokenManager = new TokenManager(tokens);
        Parser parser = new Parser(tokenManager);
        tokens.add(new Token(Token.TokenType.KEYWORD, "BEGIN", 0, 0));
        tokens.add(new Token(Token.TokenType.OPENBRACKET, "{", 0, 0));
        tokens.add(new Token(Token.TokenType.PLUSPLUS, "++", 0, 0));
        tokens.add(new Token(Token.TokenType.WORD, "d", 0, 0));
        tokens.add(new Token(Token.TokenType.CLOSEDBRACKET, "}", 0, 0));
        Optional<Node> result = Optional.ofNullable(parser.Parse());
        System.out.println(result);
        Assert.assertTrue(result.get() instanceof ProgramNode);


    }

    /**
     * Tests for UnaryNeg
     */
    @Test
    public void Minus(){
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tokenManager = new TokenManager(tokens);
        Parser parser = new Parser(tokenManager);
        tokens.add(new Token(Token.TokenType.MINUS, "-", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "5", 0, 0));
        Optional<Node> result = parser.ParseOperation();
        System.out.println(result);
    }

    /**
     * Tests a pattern array
     */
    @Test
    public void PatternArray(){
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tokenManager = new TokenManager(tokens);
        Parser parser = new Parser(tokenManager);
        tokens.add(new Token(Token.TokenType.PATTERN, "`", 0, 0));
        tokens.add(new Token(Token.TokenType.OPENBRACKET, "[", 0, 0));
        tokens.add(new Token(Token.TokenType.WORD, "a", 0, 0));
        tokens.add(new Token(Token.TokenType.CLOSEDBRACKET, "]", 0, 0));
        tokens.add(new Token(Token.TokenType.PATTERN, "`", 0, 0));
        Optional<Node> result = parser.ParseOperation();
        System.out.println(result);
    }

    /**
     * Test for the array name and array conditions
     */
    @Test
    public void Array()  {
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tokenManager = new TokenManager(tokens);
        Parser parser = new Parser(tokenManager);
        tokens.add(new Token(Token.TokenType.WORD, "e", 0, 0));
        tokens.add(new Token(Token.TokenType.OPENBRACKET, "[", 0, 0));
        tokens.add(new Token(Token.TokenType.PLUSPLUS, "++", 0, 0));
        tokens.add(new Token(Token.TokenType.WORD, "a", 0, 0));
        tokens.add(new Token(Token.TokenType.CLOSEDBRACKET, "]", 0, 0));
        Optional<Node> result = parser.ParseOperation();
        System.out.println(result);
    }

    /**
     * Test for the dollar sign and number after it
     */
    @Test
    public void dollarSign() {
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tokenManager = new TokenManager(tokens);
        Parser parser = new Parser(tokenManager);
        tokens.add(new Token(Token.TokenType.DOLLARSIGN, "$", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "7", 0, 0));
        Optional<Node> result = parser.ParseOperation();
        System.out.println(result);
    }

    /**
     * Test for post increment
     */
    @Test
    public void postIncrement() {
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tokenManager = new TokenManager(tokens);
        Parser parser = new Parser(tokenManager);
        tokens.add(new Token(Token.TokenType.WORD, "x", 0, 0));
        tokens.add(new Token(Token.TokenType.PLUSPLUS, "++", 0, 0));
        Optional<Node> result = parser.ParseOperation();
        System.out.println(result);
        Assert.assertTrue(result.get() instanceof AssignmentNode);

    }

    /**
     * Test for assignment operations
     */
    @Test
    public void Assignment()
    {
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tokenManager = new TokenManager(tokens);
        Parser parser = new Parser(tokenManager);
        tokens.add(new Token(Token.TokenType.WORD, "x", 0, 0));
        tokens.add(new Token(Token.TokenType.PERCENTEQUALS, "%=", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "5", 0, 0));
        Optional<Node> result = parser.ParseOperation();
        System.out.println(result);
    }

    /**
     * Test for a simple expression. 5^5
     */
    @Test
    public void Expression()
    {
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tokenManager = new TokenManager(tokens);
        Parser parser = new Parser(tokenManager);
        tokens.add(new Token(Token.TokenType.OPENPARENTHESIS, "(", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "5", 0, 0));
        tokens.add(new Token(Token.TokenType.EXP, "^", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "5", 0, 0));
        tokens.add(new Token(Token.TokenType.CLOSEDPARENTHESIS, ")", 0, 0));
        Optional<Node> result = parser.ParseOperation();
        System.out.println(result);
    }

    /**
     * Test for a ternary operation
     */
    @Test
    public void Ternary()
    {
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tokenManager = new TokenManager(tokens);
        Parser parser = new Parser(tokenManager);
        tokens.add(new Token(Token.TokenType.WORD, "x", 0, 0));
        tokens.add(new Token(Token.TokenType.RIGHTARROW, ">", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "5", 0, 0));
        tokens.add(new Token(Token.TokenType.QUESTIONMARK, "?", 0, 0));
        tokens.add(new Token(Token.TokenType.STRINGLITERAL, "Greater than 10", 0, 0));
        tokens.add(new Token(Token.TokenType.COLON, ":", 0, 0));
        tokens.add(new Token(Token.TokenType.STRINGLITERAL, "Less than 10", 0, 0));
        Optional<Node> result = parser.ParseOperation();
        System.out.println(result);
    }

    /**
     * Test for a simple expression. 5+5
     */
    @Test
    public void ExpressionPlus()
    {
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tokenManager = new TokenManager(tokens);
        Parser parser = new Parser(tokenManager);
        tokens.add(new Token(Token.TokenType.OPENPARENTHESIS, "(", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "5", 0, 0));
        tokens.add(new Token(Token.TokenType.PLUS, "+", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "5", 0, 0));
        tokens.add(new Token(Token.TokenType.CLOSEDPARENTHESIS, ")", 0, 0));
        Optional<Node> result = parser.ParseOperation();
        System.out.println(result);
    }
    @Test
    public void ExpressionMinus()
    {
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tokenManager = new TokenManager(tokens);
        Parser parser = new Parser(tokenManager);
        tokens.add(new Token(Token.TokenType.OPENPARENTHESIS, "(", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "5", 0, 0));
        tokens.add(new Token(Token.TokenType.MINUS, "-", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "5", 0, 0));
        tokens.add(new Token(Token.TokenType.CLOSEDPARENTHESIS, ")", 0, 0));
        Optional<Node> result = parser.ParseOperation();
        System.out.println(result);

    }


    /**
     * Test for a simple expression. 5*5
     */
    @Test
    public void Term()
    {
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tokenManager = new TokenManager(tokens);
        Parser parser = new Parser(tokenManager);
        tokens.add(new Token(Token.TokenType.OPENPARENTHESIS, "(", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "5", 0, 0));
        tokens.add(new Token(Token.TokenType.STAR, "*", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "5", 0, 0));
        tokens.add(new Token(Token.TokenType.CLOSEDPARENTHESIS, ")", 0, 0));
        Optional<Node> result = parser.ParseOperation();
        System.out.println(result);
    }

    /**
     * Test for or operation. 5 or 5
     */
    @Test
    public void Or()
    {
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tokenManager = new TokenManager(tokens);
        Parser parser = new Parser(tokenManager);
        tokens.add(new Token(Token.TokenType.OPENPARENTHESIS, "(", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "5", 0, 0));
        tokens.add(new Token(Token.TokenType.OROR, "||", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "5", 0, 0));
        tokens.add(new Token(Token.TokenType.CLOSEDPARENTHESIS, ")", 0, 0));
        Optional<Node> result = parser.ParseOperation();
        System.out.println(result);
    }

    /**
     * Test for and operation. 5 and 5
     */
    @Test
    public void And()
    {
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tokenManager = new TokenManager(tokens);
        Parser parser = new Parser(tokenManager);
        tokens.add(new Token(Token.TokenType.OPENPARENTHESIS, "(", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "5", 0, 0));
        tokens.add(new Token(Token.TokenType.ANDAND, "&&", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "5", 0, 0));
        tokens.add(new Token(Token.TokenType.CLOSEDPARENTHESIS, ")", 0, 0));
        Optional<Node> result = parser.ParseOperation();
        System.out.println(result);
    }

    /**
     * Test for match operation.
     */
    @Test
    public void Match()
    {
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tokenManager = new TokenManager(tokens);
        Parser parser = new Parser(tokenManager);
        tokens.add(new Token(Token.TokenType.WORD, "x", 0, 0));
        tokens.add(new Token(Token.TokenType.TILDE, "~", 0, 0));
        tokens.add(new Token(Token.TokenType.STRINGLITERAL, "Hello", 0, 0));
        Optional<Node> result = parser.ParseOperation();
        System.out.println(result);
    }

    /**
     * Test for non match operation.
     */
    @Test
    public void ParseNonMatch() {
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tokenManager = new TokenManager(tokens);
        Parser parser = new Parser(tokenManager);
        tokens.add(new Token(Token.TokenType.NUMBER, "10", 0, 0));
        tokens.add(new Token(Token.TokenType.EXCLAMATIONTILDE, "!~", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "5", 0, 0));
        Optional<Node> result = parser.ParseOperation();
        System.out.println(result);

    }

    /**
     * Test for boolean expression and comparison operation.
     */
    @Test
    public void ParseBooleanExpression()
    {
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tokenManager = new TokenManager(tokens);
        Parser parser = new Parser(tokenManager);
        tokens.add(new Token(Token.TokenType.WORD, "x", 0, 0));
        tokens.add(new Token(Token.TokenType.RIGHTARROW, ">", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "5", 0, 0));
        tokens.add(new Token(Token.TokenType.ANDAND, "&&", 0, 0));
        tokens.add(new Token(Token.TokenType.WORD, "x", 0, 0));
        tokens.add(new Token(Token.TokenType.LEFTARROW, "<", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "8", 0, 0));
        Optional<Node> result = parser.ParseOperation();
        System.out.println(result);


    }

    /**
     * Test for if statement and else statement.
     */
    @Test
    public void ParseIfElse()
    {

        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tokenManager = new TokenManager(tokens);
        Parser parser = new Parser(tokenManager);
        tokens.add(new Token (Token.TokenType.OPENBRACKET, "{", 0, 0));
        tokens.add(new Token(Token.TokenType.KEYWORD, "if", 0, 0));
        tokens.add(new Token(Token.TokenType.OPENPARENTHESIS, "(", 0, 0));
        tokens.add(new Token(Token.TokenType.WORD, "x", 0, 0));
        tokens.add(new Token(Token.TokenType.RIGHTARROW, ">", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "5", 0, 0));
        tokens.add(new Token(Token.TokenType.CLOSEDPARENTHESIS, ")", 0, 0));
        tokens.add(new Token(Token.TokenType.OPENBRACKET, "{", 0, 0));
        tokens.add(new Token(Token.TokenType.CLOSEDBRACKET, "}", 0, 0));
        tokens.add(new Token(Token.TokenType.CLOSEDBRACKET, "}", 0, 0));
        tokens.add(new Token(Token.TokenType.KEYWORD, "else", 1, 0));
        tokens.add(new Token(Token.TokenType.OPENBRACKET, "{", 1, 0));
        tokens.add(new Token(Token.TokenType.WORD, "x", 1, 0));
        tokens.add(new Token(Token.TokenType.PLUSPLUS, "++", 1, 0));
        tokens.add(new Token(Token.TokenType.CLOSEDBRACKET, "}", 1, 0));
        Optional<Node> result = Optional.ofNullable(parser.ParseBlock());
        System.out.println(result);
        Assert.assertTrue(result.get() instanceof BlockNode);

    }

    /**
     * Test for if statement, else if statement and else statement.
     */
    @Test
    public void ParseIfElseIfElse()
    {
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tokenManager = new TokenManager(tokens);
        Parser parser = new Parser(tokenManager);
        tokens.add(new Token(Token.TokenType.KEYWORD, "if", 0, 0));
        tokens.add(new Token(Token.TokenType.OPENPARENTHESIS, "(", 0, 0));
        tokens.add(new Token(Token.TokenType.WORD, "x", 0, 0));
        tokens.add(new Token(Token.TokenType.RIGHTARROW, ">", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "5", 0, 0));
        tokens.add(new Token(Token.TokenType.CLOSEDPARENTHESIS, ")", 0, 0));
        tokens.add(new Token(Token.TokenType.OPENBRACKET, "{", 0, 0));
        tokens.add(new Token(Token.TokenType.WORD, "x", 0, 0));
        tokens.add(new Token(Token.TokenType.PLUSPLUS, "++", 0, 0));
        tokens.add(new Token(Token.TokenType.CLOSEDBRACKET, "}", 0, 0));
        tokens.add(new Token(Token.TokenType.KEYWORD, "else", 0, 0));
        tokens.add(new Token(Token.TokenType.KEYWORD, "if", 0, 0));
        tokens.add(new Token(Token.TokenType.OPENPARENTHESIS, "(", 0, 0));
        tokens.add(new Token(Token.TokenType.WORD, "x", 0, 0));
        tokens.add(new Token(Token.TokenType.LEFTARROW, "<", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "2", 0, 0));
        tokens.add(new Token(Token.TokenType.CLOSEDPARENTHESIS, ")", 0, 0));
        tokens.add(new Token(Token.TokenType.OPENBRACKET, "{", 0, 0));
        tokens.add(new Token(Token.TokenType.WORD, "y", 0, 0));
        tokens.add(new Token(Token.TokenType.MINUSMINUS, "--", 0, 0));
        tokens.add(new Token(Token.TokenType.CLOSEDBRACKET, "}", 0, 0));
        tokens.add(new Token(Token.TokenType.KEYWORD, "else", 0, 0));
        tokens.add(new Token(Token.TokenType.OPENBRACKET, "{", 0, 0));
        tokens.add(new Token(Token.TokenType.WORD, "z", 0, 0));
        tokens.add(new Token(Token.TokenType.PLUSPLUS, "++", 0, 0));
        tokens.add(new Token(Token.TokenType.CLOSEDBRACKET, "}", 0, 0));
        Optional<Node> result = parser.ParseIf();
        System.out.println(result);
        Assert.assertTrue(result.get() instanceof IfNode);

    }

    /**
     * Test for if statement.
     */
    @Test
    public void ParseIF()
    {
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tokenManager = new TokenManager(tokens);
        Parser parser = new Parser(tokenManager);
        tokens.add(new Token(Token.TokenType.KEYWORD, "if", 0, 0));
        tokens.add(new Token(Token.TokenType.OPENPARENTHESIS, "(", 0, 0));
        tokens.add(new Token(Token.TokenType.WORD, "x", 0, 0));
        tokens.add(new Token(Token.TokenType.RIGHTARROW, ">", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "5", 0, 0));
        tokens.add(new Token(Token.TokenType.CLOSEDPARENTHESIS, ")", 0, 0));
        tokens.add(new Token(Token.TokenType.OPENBRACKET, "{", 0, 0));
        tokens.add(new Token(Token.TokenType.WORD, "x", 0, 0));
        tokens.add(new Token(Token.TokenType.PLUSPLUS, "++", 0, 0));
        tokens.add(new Token(Token.TokenType.CLOSEDBRACKET, "}", 0, 0));
        Optional<Node> result = parser.ParseIf();
        System.out.println(result);
    }

    /**
     * Test for statement
     */
    @Test
    public void ParseFor()
    {
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tokenManager = new TokenManager(tokens);
        Parser parser = new Parser(tokenManager);

        tokens.add(new Token(Token.TokenType.KEYWORD, "for", 0, 0));
        tokens.add(new Token(Token.TokenType.OPENPARENTHESIS, "(", 0, 0));
        tokens.add(new Token(Token.TokenType.WORD, "x", 0, 0));
        tokens.add(new Token(Token.TokenType.EQUALS, "=", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "5", 0, 0));
        tokens.add(new Token(Token.TokenType.SEMICOLON, ";", 0, 0));
        tokens.add(new Token(Token.TokenType.WORD, "x", 0, 0));
        tokens.add(new Token(Token.TokenType.LEFTARROW, "<", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "10", 0, 0));
        tokens.add(new Token(Token.TokenType.SEMICOLON, ";", 0, 0));
        tokens.add(new Token(Token.TokenType.WORD, "x", 0, 0));
        tokens.add(new Token(Token.TokenType.PLUSPLUS, "++", 0, 0));
        tokens.add(new Token(Token.TokenType.CLOSEDPARENTHESIS, ")", 0, 0));
        tokens.add(new Token(Token.TokenType.OPENBRACKET, "{", 0, 0));
        tokens.add(new Token(Token.TokenType.WORD, "x", 0, 0));
        tokens.add(new Token(Token.TokenType.PLUSPLUS, "++", 0, 0));
        tokens.add(new Token(Token.TokenType.CLOSEDBRACKET, "}", 0, 0));
        Optional<Node> result = parser.ParseFor();
        System.out.println(result);

    }
    /*
     * Test for for in statement
     */
    @Test
    public void ParseForEach()
    {
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tokenManager = new TokenManager(tokens);
        Parser parser = new Parser(tokenManager);
        tokens.add(new Token(Token.TokenType.KEYWORD, "for", 0, 0));
        tokens.add(new Token(Token.TokenType.OPENPARENTHESIS, "(", 0, 0));
        tokens.add(new Token(Token.TokenType.WORD, "x", 0, 0));
        tokens.add(new Token(Token.TokenType.KEYWORD, "in", 0, 0));
        tokens.add(new Token(Token.TokenType.WORD, "Customer", 0, 0));
        tokens.add(new Token(Token.TokenType.CLOSEDPARENTHESIS, ")", 0, 0));
        tokens.add(new Token(Token.TokenType.OPENBRACKET, "{", 0, 0));
        tokens.add(new Token(Token.TokenType.WORD, "x", 0, 0));
        tokens.add(new Token(Token.TokenType.PLUSPLUS, "++", 0, 0));
        tokens.add(new Token(Token.TokenType.CLOSEDBRACKET, "}", 0, 0));
        Optional<Node> result = parser.ParseFor();
        System.out.println(result);
    }

    /**
     * Test for delete statement
     * @Throws exception
     */
    @Test
    public void ParseDelete() throws Exception {
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tm = new TokenManager(tokens);
        ProgramNode programNode = new ProgramNode(new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
        Parser parser = new Parser(tm);

        tokens.add(new Token (Token.TokenType.KEYWORD, "delete", 0, 0));
        tokens.add(new Token (Token.TokenType.WORD, "a", 0, 0));
        tokens.add(new Token (Token.TokenType.OPENBRACE, "[", 0, 0));
        tokens.add(new Token (Token.TokenType.NUMBER, "1", 0, 0));
        tokens.add(new Token (Token.TokenType.CLOSEDBRACE, "]", 0, 0));
        Optional<Node>result = parser.ParseDelete();

        System.out.println(result);
    }

    /**
     * Test for while statement
     */
    @Test
    public void ParseWhile() {
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tm = new TokenManager(tokens);
        ProgramNode programNode = new ProgramNode(new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
        Parser parser = new Parser(tm);

        tokens.add(new Token (Token.TokenType.KEYWORD, "while", 0, 0));
        tokens.add(new Token (Token.TokenType.OPENPARENTHESIS, "(", 0, 0));
        tokens.add(new Token (Token.TokenType.WORD, "x", 0, 0));
        tokens.add(new Token (Token.TokenType.RIGHTARROW, ">", 0, 0));
        tokens.add(new Token (Token.TokenType.NUMBER, "5", 0, 0));
        tokens.add(new Token (Token.TokenType.CLOSEDPARENTHESIS, ")", 0, 0));
        tokens.add(new Token (Token.TokenType.OPENBRACKET, "{", 0, 0));
        tokens.add(new Token (Token.TokenType.CLOSEDBRACKET, "}", 0, 0));
        Optional<Node>result = parser.ParseWhile();

        System.out.println(result);
    }

    /**
     * Test for Concat operation
     */
    @Test
    public void ParseConcat()
    {
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tokenManager = new TokenManager(tokens);
        Parser parser = new Parser(tokenManager);
        tokens.add(new Token(Token.TokenType.STRINGLITERAL, "Hello", 0, 0));
        tokens.add(new Token(Token.TokenType.STRINGLITERAL, "World", 0, 0));
        Optional<Node> result = parser.ParseOperation();
        System.out.println(result);
    }

    /**
     * Test for ParseFunctionCall
     * @throws Exception
     */
    @Test
    public void ParseFunctionCall() throws Exception {
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tokenManager = new TokenManager(tokens);
        Parser parser = new Parser(tokenManager);
        tokens.add(new Token(Token.TokenType.WORD, "FS", 0, 0));
        tokens.add(new Token(Token.TokenType.OPENPARENTHESIS, "(", 0, 0));
        tokens.add(new Token(Token.TokenType.CLOSEDPARENTHESIS, ")", 0, 0));
        tokens.add(new Token(Token.TokenType.SEMICOLON, ";", 0, 0));
        Optional<Node> result = parser.ParseFunctionCall();
        System.out.println(result);
    }

    /**
     * Test for ParseDoWhile
     */
    @Test
    public void ParseDoWhile(){
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tokenManager = new TokenManager(tokens);
        Parser parser = new Parser(tokenManager);
        tokens.add(new Token(Token.TokenType.KEYWORD, "do", 0, 0));
        tokens.add(new Token(Token.TokenType.OPENBRACKET, "{", 0, 0));
        tokens.add(new Token(Token.TokenType.WORD, "x", 0, 0));
        tokens.add(new Token(Token.TokenType.PLUSPLUS, "++", 0, 0));
        tokens.add(new Token(Token.TokenType.CLOSEDBRACKET, "}", 0, 0));
        tokens.add(new Token(Token.TokenType.KEYWORD, "while", 0, 0));
        tokens.add(new Token(Token.TokenType.OPENPARENTHESIS, "(", 0, 0));
        tokens.add(new Token(Token.TokenType.WORD, "x", 0, 0));
        tokens.add(new Token(Token.TokenType.RIGHTARROW, ">", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "5", 0, 0));
        tokens.add(new Token(Token.TokenType.CLOSEDPARENTHESIS, ")", 0, 0));
        Optional<Node> result = parser.ParseDoWhile();
        System.out.println(result);
    }

    /**
     * Test for ParseContinue
     */
    @Test
    public void ParseContinue()
    {
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tokenManager = new TokenManager(tokens);
        Parser parser = new Parser(tokenManager);
        tokens.add(new Token(Token.TokenType.KEYWORD, "continue", 0, 0));
        tokens.add(new Token(Token.TokenType.SEMICOLON, ";", 0, 0));
        Optional<Node> result = parser.ParseContinue();
        System.out.println(result);
    }
    /**
     * Test for ParseReturn
     */
    @Test
    public void ParseReturn()
    {
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tokenManager = new TokenManager(tokens);
        Parser parser = new Parser(tokenManager);
        tokens.add(new Token(Token.TokenType.KEYWORD, "return", 0, 0));
        tokens.add(new Token(Token.TokenType.WORD, "x", 0, 0));
        tokens.add(new Token(Token.TokenType.SEMICOLON, ";", 0, 0));
        Optional<Node> result = parser.ParseReturn();
        System.out.println(result);
    }

    /**
     * Test for ParseBreak
     */
    @Test
    public void ParseBreak()
    {
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tokenManager = new TokenManager(tokens);
        Parser parser = new Parser(tokenManager);
        tokens.add(new Token(Token.TokenType.KEYWORD, "break", 0, 0));
        tokens.add(new Token(Token.TokenType.SEMICOLON, ";", 0, 0));
        Optional<Node> result = parser.ParseBreak();
        System.out.println(result);

    }
    @Test
    public void ParsePrintf()
    {
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tokenManager = new TokenManager(tokens);
        Parser parser = new Parser(tokenManager);
        tokens.add(new Token(Token.TokenType.KEYWORD,"printf", 0, 0));
        tokens.add(new Token(Token.TokenType.OPENPARENTHESIS, "(",0,0));
        tokens.add(new Token(Token.TokenType.STRINGLITERAL, "%d is low", 0, 0));
        tokens.add(new Token(Token.TokenType.SEPARATOR, ",", 0, 0));
        tokens.add(new Token(Token.TokenType.WORD, "word", 0 ,0));
        tokens.add(new Token(Token.TokenType.CLOSEDPARENTHESIS, ")", 0, 0));

        Optional<Node> result = parser.ParseFunctionCall();
        System.out.println(result);
    }
    @Test
    public void ParsePrint()
    {
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tokenManager = new TokenManager(tokens);
        Parser parser = new Parser(tokenManager);
        tokens.add(new Token(Token.TokenType.KEYWORD, "print", 0, 0));
        tokens.add(new Token(Token.TokenType.STRINGLITERAL, "Hello World!", 0, 0));
        Optional<Node> result = parser.ParseFunctionCall();
        System.out.println(result);
    }
    @Test
    public void ParseGetline()
    {
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tokenManager = new TokenManager(tokens);
        Parser parser = new Parser(tokenManager);
        tokens.add(new Token(Token.TokenType.KEYWORD,"getline", 0 ,0));
        tokens.add(new Token(Token.TokenType.STRINGLITERAL,"hello", 0, 0 ));
        Optional<Node> result = parser.ParseFunctionCall();
        System.out.println(result);
    }
    @Test
    public void ParseNext()
    {
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tokenManager = new TokenManager(tokens);
        Parser parser = new Parser(tokenManager);
        tokens.add(new Token(Token.TokenType.KEYWORD, "next",0,0));
        tokens.add(new Token(Token.TokenType.SEPARATOR, ";", 0, 0));
        Optional<Node> result = parser.ParseFunctionCall();
        System.out.println(result);
    }
    @Test
    public void ParseNextFile()
    {
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tokenManager = new TokenManager(tokens);
        Parser parser = new Parser(tokenManager);
        tokens.add(new Token(Token.TokenType.KEYWORD, "nextfile",0,0));
        tokens.add(new Token(Token.TokenType.SEMICOLON, ";", 0, 0));
        Optional<Node> result = parser.ParseFunctionCall();
        System.out.println(result);
    }
    @Test
    public void ParseExit()
    {
        LinkedList<Token> tokens = new LinkedList<>();
        TokenManager tokenManager = new TokenManager(tokens);
        Parser parser = new Parser(tokenManager);
        tokens.add(new Token(Token.TokenType.KEYWORD, "exit",0,0));
        //   tokens.add(new Token(Token.TokenType.SEMICOLON, ";", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "0", 0, 0));
        //     tokens.add(new Token(Token.TokenType.SEMICOLON, ";", 0, 0));
        Optional<Node> result =  parser.ParseFunctionCall();
        System.out.println(result);
        Assert.assertTrue(result.get() instanceof StatementNode);
    }




}