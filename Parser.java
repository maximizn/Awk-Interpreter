import java.util.LinkedList;
import java.util.Optional;

/**
 * Parser class that takes in a TokenManager and parses the tokens. Using methods such as AcceptSeparators, Parse, ParseFunction ,ParseAction, ParseBlock, and ParseOption.
 */
/**
 * Parser class that takes in a TokenManager and parses the tokens. Using methods such as AcceptSeparators, Parse, ParseFunction ,ParseAction, ParseBlock, and ParseOption.
 */
class Parser {
    private final TokenManager tm;

    public Parser(TokenManager tm) {
        this.tm = tm;
    }
    /**
     * AcceptSeparators method that checks if the tokens LinkedList is greater than 0. If it is, it checks if the first token is a separator.
     * If it is, it removes the token and returns true. If it is not, it returns false.
     * @return true if the first token is a separator and false if it is not.
     */
    boolean AcceptSeparators() {
        if(tm.tokens.size() > 0) {
            while (tm.tokens.get(0).getType() == Token.TokenType.SEPARATOR) {
                tm.MatchandRemove(Token.TokenType.SEPARATOR);
            }
            return true;
        } else {
            return false;
        }

    }
    /**
     * Parse method that creates a ProgramNode and while there are more tokens. It calls ParseFunction and ParseAction. If neither of those
     * return true, it throws an exception.
     * @return ProgramNode
     * @throws Exception
     */
    public ProgramNode Parse() throws Exception {
        ProgramNode programNode = new ProgramNode( new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>() );

        while (tm.MoreTokens()) {
            if (!ParseFunction(programNode) && !ParseAction(programNode)) {
                throw new Exception("Parsing error.");
            }
        }

        return programNode;
    }
    /**
     * ParseAction method takes in a ProgramNode, initializes LinkedList <String> parameters and functionDefinitionNode. It first peeks at the first token
     * and if it is present, it sets to functionName. If it is not present, it returns false. If functionName is "function" it matches and removes the token keyword.
     * It then peeks at the next token word and if it is present, it sets it to functionName. If it is not present, it returns false. It then matches and removes the
     * open parenthesis token. It then peeks at the next token and if it is present, it sets it to parameter. If it is not present, it returns false. It then loops while
     * a comma is present. It then matches and removes the comma token and peeks at the next token and if it is present, it sets it to parameter. If it is not present,
     * it returns false. It then adds the parameter to the parameters LinkedList. It then matches and removes the close parenthesis token and breaks out of the loop.
     * it then matches and removes the open curly brace token. It then parses the function block and creates a new FunctionDefinitionNode with the functionName, parameters,
     * and statements. It then adds the functionDefinitionNode to the function LinkedList in the ProgramNode. It then matches and removes the close curly brace token and
     * returns true. If the first token is not present, it returns false.
     *
     * @param programNode
     * @return true if the first token is present and false if it is not.
     */
    boolean ParseFunction(ProgramNode programNode) {
        AcceptSeparators();
        LinkedList<String> parameters = new LinkedList<>();
        FunctionDefinitionNode functionDefinitionNode;
        Optional<Token> function = tm.Peek(0);
        if(function.get().getType().equals(Token.TokenType.SEPARATOR))
        {
            AcceptSeparators();
        }
        String functionName = "";
        if (function.isPresent()) {
            functionName = function.get().getVal();
        } else {
            return false;
        }
        if ("function".equals(functionName)) {
            tm.MatchandRemove(Token.TokenType.KEYWORD);
            Optional<Token> name = tm.MatchandRemove(Token.TokenType.WORD);
            if (name.isPresent()) {
                functionName = name.get().getVal();
            } else {
                return false;
            }

            if (tm.MatchandRemove(Token.TokenType.OPENPARENTHESIS).isPresent()) {
                Optional<Token> parameter = tm.MatchandRemove(Token.TokenType.WORD);
                if (parameter.isPresent()) {
                    parameters.add(parameter.get().getVal());
                } else {
                    return false;
                }
                while (tm.MatchandRemove(Token.TokenType.COMMA).isPresent()) {
                    AcceptSeparators();
                    parameter = tm.MatchandRemove(Token.TokenType.WORD);
                    if (parameter.isPresent()) {
                        parameters.add(parameter.get().getVal());
                    } else {
                        return false;
                    }

                    if (tm.MatchandRemove(Token.TokenType.CLOSEDPARENTHESIS).isPresent()) {
                        BlockNode blockNode = ParseBlock();
                        functionDefinitionNode = new FunctionDefinitionNode(functionName, parameters, blockNode.statements);
                        programNode.function.add(functionDefinitionNode);
                        return true;

                    } else {
                        return false;
                    }
                }
            }
        }
        return false;
    }
    /**
     * ParseAction method that takes in a ProgramNode. It first peeks at the first token and if it is present, it sets it to actionName. If it is not present,
     * it returns false. If actionName is "BEGIN" it matches and removes the token keyword. It then parses the block and adds it to the Begin LinkedList in the
     * ProgramNode. It then returns true. If actionName is "END" it matches and removes the token keyword. It then parses the block and adds it to the End LinkedList
     * in the ProgramNode. It then returns true. If actionName is not "BEGIN" or "END" it calls ParseOption and sets it to condition and parses the block and sets it
     * to blockNode.
     * @param programNode
     * @return true if actionName is "BEGIN" or "END" and false if it is not.
     */
    boolean ParseAction(ProgramNode programNode) throws Exception {
        boolean result = false;
        Optional<Token> action = tm.Peek(0);
        String actionName = "";
        if (!action.isEmpty()) {
            actionName = action.get().getVal();
        }
        if ("BEGIN".equals(actionName)) {
            tm.MatchandRemove(Token.TokenType.KEYWORD);
            BlockNode blockNode = ParseBlock();
            programNode.Begin.add(blockNode);
            result = true;
        }
        else if ("END".equals(actionName)) {
            tm.MatchandRemove(Token.TokenType.KEYWORD);
            BlockNode blockNode = ParseBlock();
            programNode.End.add(blockNode);
            result = true;
        } else {
            Optional<Node> condition = ParseOperation();
            BlockNode blockNode = ParseBlock();
        }
        return result;
    }

    /**
     * ParseLValue method first macthes and removes a $ and word token. If the $ token is present, it calls ParseBottomLevel and sets it to bottomLevel.
     * It then returns a new OperationNode with the bottomLevel and MathOperation.DOLLAR. If the word token is present, it matches and removes the open bracket token.
     * It then calls ParseOperation and sets it to array. It then matches and removes the close bracket token. It then calls ParseOperation and sets it to index.
     * It then returns a new VariableReference with the word and array. If not $ or word token is present, it returns an empty optional.
     * @return an optional containing a Node if the LValue is present
     * @throws Exception if there is an error parsing the LValue
     */
    private Optional<Node> ParseLValue(){
        Optional<Token> dollar = tm.MatchandRemove(Token.TokenType.DOLLARSIGN);

        if(dollar.isPresent())
        {
            Optional<Node> bottomLevel = ParseBottomLevel();
            return Optional.of(new OperationNode(bottomLevel.get(), OperationNode.MathOperation.DOLLAR, Optional.empty()));

        }
        Optional<Token> word = tm.MatchandRemove(Token.TokenType.WORD);
        if(word.isPresent()) {
            Optional<Token> openArray = tm.MatchandRemove(Token.TokenType.OPENBRACKET);
            if (openArray.isPresent()) {
                Optional<Node> array = ParseOperation();
                if (array.isPresent()) {
                    Optional<Token> closeArray = tm.MatchandRemove(Token.TokenType.CLOSEDBRACKET);
                    return Optional.of(new VariableReferenceNode(word.get().getVal(), array));
                } else {
                    System.out.println("No closed bracket");
                }

            }else {
                return Optional.of(new VariableReferenceNode(word.get().getVal(), Optional.empty()));
            }
        }
        return Optional.empty();
    }

    /**
     * ParseBottomLevel method first peeks at the first token and if it is present, it sets it to bottomLevel. It then checks if the bottomLevel is a stringLiteral, number, or pattern.
     * If it is a stringLiteral, it matches and removes the stringLiteral token and returns a new ConstantNode with the stringLiteral. If it is a number, it matches and removes the number
     * token and returns a new ConstantNode with the number. If it is a pattern, it matches and removes the pattern token and returns a new PatternNode with the pattern. If it is not a
     * stringLiteral, number, or pattern, it returns an empty optional. If the bottomLevel is not present, it returns an LValue.
     * @return an optional containing a Node if the bottomLevel isnt present
     * @throws Exception if there is an error parsing the bottomLevel
     */
    private Optional<Node> ParseBottomLevel() {
        Optional<Token> bottomLevel = tm.Peek(0);
        Optional<Token> stringLiteral = tm.MatchandRemove(Token.TokenType.STRINGLITERAL);
        Optional<Token> number = tm.MatchandRemove(Token.TokenType.NUMBER);
        Optional<Token> pattern = tm.MatchandRemove(Token.TokenType.PATTERN);
        if(bottomLevel.isPresent()) {
            if (stringLiteral.isPresent()) {
                return Optional.of(new ConstantNode(stringLiteral.get().getVal()));
            } else if (bottomLevel.get().getVal().equals("function")) {
                tm.MatchandRemove(Token.TokenType.KEYWORD);
                ParseFunctionCall();
            } else if (number.isPresent()) {
                tm.MatchandRemove(Token.TokenType.NUMBER);
                return Optional.of(new ConstantNode(number.get().getVal()));
            } else if (pattern.isPresent()) {
                return Optional.of(new PatternNode(pattern.get().getVal()));
            } else if (tm.MatchandRemove(Token.TokenType.OPENPARENTHESIS).isPresent()) {
                Optional<Node> operation = ParseOperation();
                tm.MatchandRemove(Token.TokenType.CLOSEDPARENTHESIS);
                return Optional.of(operation.get());
            } else if (tm.MatchandRemove(Token.TokenType.NOT).isPresent()) {
                Optional<Node> result = ParseOperation();
                return Optional.of(new OperationNode(result.get(), OperationNode.MathOperation.NOT, Optional.empty()));
            } else if (tm.MatchandRemove(Token.TokenType.MINUS).isPresent()) {
                Optional<Node> result = ParseOperation();
                return Optional.of(new OperationNode(result.get(), OperationNode.MathOperation.UNARYNEG, Optional.empty()));
            } else if (tm.MatchandRemove(Token.TokenType.PLUS).isPresent()) {
                Optional<Node> result = ParseOperation();
                return Optional.of(new OperationNode(result.get(), OperationNode.MathOperation.UNARYPOS, Optional.empty()));
            } else if (tm.MatchandRemove(Token.TokenType.PLUSPLUS).isPresent()) {
                Optional<Node> result = ParseOperation();
                return Optional.of(new AssignmentNode(result.get(), new OperationNode(result.get(), OperationNode.MathOperation.PREINC, Optional.empty())));
            } else if (tm.MatchandRemove(Token.TokenType.MINUSMINUS).isPresent()) {
                Optional<Node> result = ParseOperation(); //new OperationNode(result.get(), OperationNode.MathOperation.POSTINC, Optional.empty())))
                return Optional.of(new AssignmentNode(result.get(), new OperationNode(result.get(), OperationNode.MathOperation.PREDEC, Optional.empty())));
            } else if (tm.MoreTokens() && tm.Peek(1).get().getType().equals(Token.TokenType.OPENPARENTHESIS)) {
                Optional<Node> functionCall = ParseFunctionCall();
                if (functionCall.isPresent()) {
                    return functionCall;
                }
            }
        }
        return ParseLValue();
    }

    /**
     * ParseOption method calls ParseAssignment and starts the recursive calling of all the other methods.
     * @return result of said method
     */
    public Optional<Node> ParseOperation(){
        Optional <Node> result = ParseAssignment();
        return result;
    }


    /**
     * ParseAssignment method parses assignment expressions. It handles =, -=, +=, *=, /=, %=, ^=. After calling ParseTernary, it peeks at the next token without consuming it.
     * It then checks if the token is a valid assignment operator. If it is, it consumes the token and calls ParseTernary again. It then returns a new AssignmentNode with the lvalue if the
     * token was an '=', but for the rest it assigns a new OperationNode with the lvalue, the math operation, and the result of ParseTernary.
     * @return lvlaue of ParseTernary, or new assignment node wih math operation
     */
    private Optional<Node> ParseAssignment() {
        Optional<Node> lvalue = ParseTernary();
        if (lvalue.isPresent()) {
            Optional<Token> assign = tm.Peek(0);
            if (assign.isPresent()) {
                if (assign.get().getType() == Token.TokenType.EQUALS) {
                    tm.MatchandRemove(Token.TokenType.EQUALS);
                    Optional<Node> num = ParseTernary();
                    return Optional.of(new AssignmentNode(lvalue.get(), num.get()));
                }else if (assign.get().getType() == Token.TokenType.MINUSEQUALS) {
                    tm.MatchandRemove(Token.TokenType.MINUSEQUALS);
                    Optional<Node> result = ParseTernary();
                    return Optional.of(new AssignmentNode(lvalue.get(), new OperationNode(lvalue.get(), OperationNode.MathOperation.SUBTRACT, Optional.of(result.get()))));
                }else if (assign.get().getType() == Token.TokenType.PLUSEQUALS) {
                    tm.MatchandRemove(Token.TokenType.PLUSEQUALS);
                    Optional<Node> result = ParseTernary();
                    return Optional.of(new AssignmentNode(lvalue.get(), new OperationNode(lvalue.get(), OperationNode.MathOperation.ADD, Optional.of(result.get()))));
                }else if( assign.get().getType() == Token.TokenType.STAREQUALS) {
                    tm.MatchandRemove(Token.TokenType.STAREQUALS);
                    Optional<Node> result = ParseTernary();
                    return Optional.of(new AssignmentNode(lvalue.get(), new OperationNode(lvalue.get(), OperationNode.MathOperation.MULTIPLY, Optional.of(result.get()))));
                } else if (assign.get().getType() == Token.TokenType.SLASHEQUALS) {
                    tm.MatchandRemove(Token.TokenType.SLASHEQUALS);
                    Optional<Node> result = ParseTernary();
                    return Optional.of(new AssignmentNode(lvalue.get(), new OperationNode(lvalue.get(), OperationNode.MathOperation.DIVIDE,Optional.of(result.get()))));
                } else if (assign.get().getType() == Token.TokenType.PERCENTEQUALS) {
                    tm.MatchandRemove(Token.TokenType.PERCENTEQUALS);
                    Optional<Node> result = ParseTernary();
                    return Optional.of(new AssignmentNode(lvalue.get(), new OperationNode(lvalue.get(), OperationNode.MathOperation.MODULO, Optional.of(result.get()))));
                } else if (assign.get().getType() == Token.TokenType.EXPEQUALS) {
                    tm.MatchandRemove(Token.TokenType.EXPEQUALS);
                    Optional<Node> result = ParseTernary();
                    return Optional.of(new AssignmentNode(lvalue.get(), new OperationNode(lvalue.get(), OperationNode.MathOperation.EXPONENT, Optional.of(result.get()))));
                }
            }
        }
        return lvalue;
    }

    /**
     * ParseTernary method parses a ternary expression. It handles the conditional operator ? and the colon operator :. After calling Parse_Or
     * it matches and removes the QUESTIONMARK token. If it exists, it calls ParseTernary again and matches and removes the COLON token. If it
     * exists, it calls ParseTernary again and returns a new TernaryNode. If it doesn't exist, it returns the conditionExpr.
     * @Throws IllegalArgumentException if ternary expression is invalid
     * @return conditionExpr of Parse_Or or empty optional
     */
    private Optional<Node>  ParseTernary()
    {
        Optional<Node> conditionExpr = Parse_Or();

        if (tm.MatchandRemove(Token.TokenType.QUESTIONMARK).isPresent()) {
            Optional<Node> consequentExpr = ParseTernary();

            if (tm.MatchandRemove(Token.TokenType.COLON).isPresent()) {
                Optional<Node> alternateExpr = ParseTernary();
                return Optional.of(new TernaryNode(conditionExpr.get(), consequentExpr.get(), alternateExpr.get()));
            } else {
                throw new IllegalArgumentException("Ternary expression is invalid");
            }
        }

        return conditionExpr;
    }

    /**
     * Parse_Or method parses a logical OR expression. After calling Parse_And it matches and removes the OROR token. If it exists, it calls
     * Parse_And again and returns a new OperationNode with the left operand, the OR operator, and the right operand. If it doesnt exist, it returns
     * the left operand.
     * @return leftOperand of Parse_And or empty optional
     */
    private Optional<Node> Parse_Or()
    {
        Optional<Node> leftOperand = Parse_And();
        while(tm.MatchandRemove(Token.TokenType.OROR).isPresent())
        {
            Optional<Node> right = Parse_And();
            leftOperand = Optional.of(new OperationNode(leftOperand.get(), OperationNode.MathOperation.OR, Optional.of(right.get())));
        }
        return leftOperand;
    }

    /**
     * Parse_And method parses a logical AND expression. After calling ParseArraymembership it matches and removes the ANDAND token. If it exists, it calls
     * ParseArraymembership again and returns a new OperationNode with the left operand, the AND operator, and the right operand. If it doesnt exist, it returns
     * the left operand.
     * @return leftOperand of ParseArraymembership or empty optional
     */
    private Optional<Node> Parse_And() {
        Optional<Node> leftOperand = ParseArraymembership();
        while (tm.MatchandRemove(Token.TokenType.ANDAND).isPresent()) {
            Optional<Node> rightOperand = ParseArraymembership();
            leftOperand = Optional.of(new OperationNode(leftOperand.get(), OperationNode.MathOperation.AND, rightOperand));
        }

        return leftOperand;
    }

    /**
     * ParseArraymembership method parses an array membership expression. It handles the open bracket token [ and the closed bracket token ]. After calling
     * ParseMatch it matches and removes the open bracket token, and calls ParseExpression. If the closed bracket token doesnt exist, it throws an exception.
     * If it does exist, it gets matched and removed and returns a new OperationNode with the left operand, the IN operator, and the index.
     * @return leftOperand of ParseMatch or empty optional
     */
    private Optional<Node> ParseArraymembership()
    {
        Optional<Node> leftOperand = ParseMatch();

        while (tm.MatchandRemove(Token.TokenType.OPENBRACE).isPresent()) {
            Optional<Node> index = ParseExpression();
            Optional<Token> closeBracket = tm.MatchandRemove(Token.TokenType.CLOSEDBRACE);
            if (!closeBracket.isPresent()) {
                throw new IllegalArgumentException("Expected ] in array ");
            }

            leftOperand = Optional.of(new OperationNode(leftOperand.get(), OperationNode.MathOperation.IN, Optional.of(index.get())));
        }

        return leftOperand;
    }

    /**
     * ParseMatch method parses a match expression. It handles the match operator ~ and the not match operator !~. After calling ParseBoolean,
     * it matches and removes the tilde token, if tilde doesnt exist match and remove the exclamation tilde token. It it does exist, it parses the right expression.
     * Initializes a MathOperation operation to store the operation type. Then determines the operation type based on the tilde token type. Returns a new OperationNode
     * @return leftexpr of ParseBoolean or empty optional
     */
    private Optional<Node> ParseMatch()
    {

        Optional<Node> leftexpr = ParseBoolean();
        Optional<Token> tilde = tm.MatchandRemove(Token.TokenType.TILDE);
        if (!tilde.isPresent()) {
            tilde = tm.MatchandRemove(Token.TokenType.EXCLAMATIONTILDE);
        }

        if (tilde.isPresent()) {
            Optional<Node> rightexpr = ParseExpression();

            OperationNode.MathOperation operation;
            if (tilde.get().getType() == Token.TokenType.TILDE) {
                operation = OperationNode.MathOperation.MATCH;
            } else {
                operation = OperationNode.MathOperation.NOTMATCH;
            }

            return Optional.of(new OperationNode(leftexpr.get(), operation, Optional.of(rightexpr.get())));
        }

        return leftexpr;
    }

    /**
     * ParseBoolean method parses the boolean expression. It handles the comparison operators. After calling ParseBoolean,
     * it peeks at the token without consuming and checks if it is a comparison operator. If it is, it consumes the token then parses the right operand of the boolean expression.
     * Initializes a variable to store the operation type and checks if the operatorType is a valid MathOperation type. If it is, returns a new OperationNode with the leftOperand, operation, and rightOperand.
     * @Throws IllegalArgumentException if the operator is not a comparison operator
     * @return leftOperand of ParseExpression or empty optional
     */
    private Optional<Node> ParseBoolean() {
        Optional<Node> leftOperand = ParseCat();
        Optional<Token> operator = tm.Peek(0);

        if (operator.isPresent()) {
            Token.TokenType operatorType = operator.get().getType();

            if (operatorType == Token.TokenType.EQUALSEQUALS|| operatorType == Token.TokenType.NOTEQUAL ||
                    operatorType == Token.TokenType.LEFTARROW || operatorType == Token.TokenType.RIGHTARROW ||
                    operatorType == Token.TokenType.LEFTARROWEQUAL || operatorType == Token.TokenType.RightArrowEqual) {

                tm.MatchandRemove(operatorType);
                Optional<Node> rightOperand = ParseCat();
                OperationNode.MathOperation operation;

                if (operatorType == Token.TokenType.EQUALSEQUALS) {
                    operation = OperationNode.MathOperation.EQ;
                } else if (operatorType == Token.TokenType.NOTEQUAL) {
                    operation = OperationNode.MathOperation.NE;
                } else if (operatorType == Token.TokenType.LEFTARROW) {
                    operation = OperationNode.MathOperation.LT;
                } else if (operatorType == Token.TokenType.RIGHTARROW) {
                    operation = OperationNode.MathOperation.GT;
                } else if (operatorType == Token.TokenType.LEFTARROWEQUAL) {
                    operation = OperationNode.MathOperation.LE;
                } else if (operatorType == Token.TokenType.RightArrowEqual) {
                    operation = OperationNode.MathOperation.GE;
                } else {
                    throw new IllegalArgumentException("Invalid operator type: ");
                }

                return Optional.of(new OperationNode(leftOperand.get(), operation, Optional.of(rightOperand.get())));
            }
        }

        return leftOperand;
    }


    /**
     * ParseCat method parse strng concatenation. It concatenates two strings using the concatenation operator. After calling ParseExpression, it calls ParseExpression again
     * to get the expression on the right side of the concatenation operator. It then creates a new OperationNode with the left and right operands.
     * @throws IllegalArgumentException if the input is invalid
     * @return left of ParseExpression or empty optional
     */
    private Optional<Node> ParseCat()
    {

        Optional<Node> left = ParseExpression();
        if(tm.MoreTokens()&& tm.Peek(0).get().getType() == Token.TokenType.STRINGLITERAL) {
            Optional<Node> right = ParseExpression();
            left = Optional.of(new OperationNode(left.get(), OperationNode.MathOperation.CONCATENATION, right));
        }
        else if (!left.isPresent()) {
            throw new IllegalArgumentException("Error: Expected expression");
        }

        return left;
    }

    /**
     * ParseExpression method parses addition and subtraction operations. It handles the + and - operators. After calling ParseTerm, it loops through the
     * while loop to check if the operator is + or -. If it is, it calls ParseTerm again and creates a new OperationNode with the left and right operands.
     * @return left of ParseTerm or empty optional
     */
    private Optional<Node> ParseExpression()
    {
        Optional<Node> left = ParseTerm();
        while (true) {
            Optional<Token> op = tm.MatchandRemove(Token.TokenType.PLUS);
            if (op.isEmpty()) {
                op = tm.MatchandRemove(Token.TokenType.MINUS);
            }
            if (op.isPresent()) {
                Optional<Node> right = ParseTerm();
                left = Optional.of(new OperationNode(left.get(), (op.get().getType() == Token.TokenType.PLUS) ? OperationNode.MathOperation.ADD : OperationNode.MathOperation.SUBTRACT, Optional.of(right.get())));
            } else {
                return left;
            }
        }
    }

    /**
     * ParseTerm method parses multiplication and division operations. It handles the * and / operators. After calling ParseFactor,
     * It inilitizes the OperationNode.MathOperation operation. This is done to assign the correct MathOperation to operation.
     * It loops the while loop until the operator is not present. If the operator is present, it calls ParseFactor again and assigns the right node.
     * Then it creates a new OperationNode and assigns it to left.
     * @return left of ParseFactor or empty optional
     */
    private Optional<Node> ParseTerm()
    {
        Optional<Node> left = ParseFactor();
        OperationNode.MathOperation operation;
        while (true) {
            Optional<Token> op = tm.MatchandRemove(Token.TokenType.STAR);
            if (op.isEmpty()) {
                op = tm.MatchandRemove(Token.TokenType.SLASH);
            }
            if(op.isEmpty())
            {
                op = tm.MatchandRemove(Token.TokenType.PERCENT);
            }
            if (op.isPresent()) {
                Optional<Node> right = ParseFactor();
                operation = OperationNode.MathOperation.MULTIPLY;
                if (op.get().getType() == Token.TokenType.SLASH) {
                    operation = OperationNode.MathOperation.DIVIDE;
                }
                else if(op.get().getType() == Token.TokenType.PERCENT)
                {
                    operation = OperationNode.MathOperation.MODULO;
                }
                left = Optional.of(new OperationNode(left.get(), operation, Optional.of(right.get())));
            } else {
                return left;
            }
        }
    }

    /**
     * ParseFactor parse a number first and creates a constant node, then it checks for a left parenthesis and calls
     * ParseExpression to parse the expression inside the parenthesis. If there is no expression inside the parenthesis
     * it returns an empty optional. If there is no number or parenthesis it returns an empty optional.
     * @throws IllegalArgumentException if the input is invalid
     * @return result of ParseExponent or empty optional
     */
    private Optional<Node> ParseFactor() {
        Optional<Node> result = ParseExponent();
        if(result.isPresent())
        {
            return result;
        }
        Optional<Token> num = tm.Peek(0);
        if (num.isPresent() && num.get().getType() == Token.TokenType.NUMBER) {
            tm.MatchandRemove(Token.TokenType.NUMBER);
            return Optional.of(new ConstantNode(num.get().getVal()));
        }
        Optional<Token> lparen = tm.MatchandRemove(Token.TokenType.OPENPARENTHESIS);
        if(lparen.isPresent())
        {
            Optional<Node> exp = ParseExpression();
            if(!exp.isPresent())
            {
                throw new IllegalArgumentException("Error: Expected expression after (");
            }
            Optional<Token> rparen = tm.MatchandRemove(Token.TokenType.CLOSEDPARENTHESIS);
            if(!rparen.isPresent())
            {
                throw new IllegalArgumentException("Error: Expected ) after expression");
            }
            return exp;
        }
        return result;
    }
    /**
     * ParseExponent method creates a new OperationNode for exponent. It handles the ^ operator.
     * @return result of ParsePostIncDec or an empty optional
     */
    private Optional<Node> ParseExponent()
    {
        Optional<Node> result = ParsePostIncDec();
        if(tm.MatchandRemove(Token.TokenType.EXP).isPresent())
        {
            Optional<Node> exponent = ParseOperation();
            return Optional.of(new OperationNode(result.get(), OperationNode.MathOperation.EXPONENT, exponent));
        }
        return result;

    }

    /**
     * ParsePostIncDec method that creates a new OperationNode for post increment and decrement. It handles the ++ and --
     * @return result of ParseBottomLevel or an empty optional
     */
    private Optional<Node> ParsePostIncDec()
    {
        Optional<Node> result = ParseBottomLevel();
        if(result.isPresent())
        {
            Optional<Token> op = tm.MatchandRemove(Token.TokenType.PLUSPLUS);
            if(op.isPresent())
            {
                return Optional.of(new AssignmentNode(result.get(), new OperationNode(result.get(), OperationNode.MathOperation.POSTINC)));
            }
            op = tm.MatchandRemove(Token.TokenType.MINUSMINUS);
            if(op.isPresent())
            {
                return Optional.of(new AssignmentNode(result.get(), new OperationNode(result.get(), OperationNode.MathOperation.POSTDEC)));
            }
        }
        return result;
    }

    /**
     * ParseBlock method parses a block of code. It checks for an open bracket and if it is present, it loops through the block of code
     * until it finds a closed bracket. If there is no closed bracket it throws an exception. If there is no open bracket it calls ParseStatement.
     * ParseBlock parses multi line statements and single line statements.
     * @return BlockNode
     */
    BlockNode ParseBlock() {
        AcceptSeparators();
        Optional<Token> openBracket = tm.MatchandRemove(Token.TokenType.OPENBRACE);
        AcceptSeparators();
        LinkedList<StatementNode> statements = new LinkedList<>();
        if (openBracket.isPresent()) {
            while (!tm.MatchandRemove(Token.TokenType.CLOSEDBRACE).isPresent()) {
                AcceptSeparators();
                if(tm.MatchandRemove(Token.TokenType.CLOSEDBRACE).isPresent())
                {
                    break;
                }
                Optional<Node> result = ParseStatement();
                if (result.isPresent()) {
                    statements.add((StatementNode) result.get());
                } else {
                    AcceptSeparators();
                }
            }
        } else {
            Optional<Node> result = ParseStatement();
            result.ifPresent(node -> statements.add((StatementNode) node));
        }

        return new BlockNode(statements,Optional.empty());
    }

    /**
     * ParseStatement method is used to parse statements.
     * It peeks at the token and checks if it is an if, while, do, for, return, break, continue, or delete statement.
     * Then calls the appropriate method to parse the statement.
     * @return the result of the method called or parseOperation
     */
    public Optional<Node> ParseStatement()
    {
        switch(tm.Peek(0).get().getVal())
        {
            case "if":
                return ParseIf();

            case "while":
                return ParseWhile();

            case "do":
                return ParseDoWhile();

            case "for":
                return ParseFor();

            case "return":
                return ParseReturn();

            case "break":
                return ParseBreak();

            case "continue":
                return ParseContinue();

            case "delete":
                return ParseDelete();


        }
        return ParseOperation();
    }

    /**
     * ParseIf method that creates a new IfNode. It checks for an If statement, and ElseIf statement,
     * and Else statement. It takes the conditions and blocks and creates a new IfNode.
     * @throws IllegalArgumentException if the 'if' statement is invalid
     * @return IfNode or "Invalid if statement"
     */
    public Optional<Node> ParseIf() {
        tm.MatchandRemove(Token.TokenType.KEYWORD);
        tm.MatchandRemove(Token.TokenType.OPENPARENTHESIS);
        Optional<Node> condition = ParseOperation();
        tm.MatchandRemove(Token.TokenType.CLOSEDPARENTHESIS);
        AcceptSeparators();
        BlockNode statements = ParseBlock();
        Optional<Node> elseif = Optional.empty();
        AcceptSeparators();

        if (tm.Peek(0).isPresent() && tm.Peek(0).get().getVal().equals("else")) {
            tm.MatchandRemove(Token.TokenType.KEYWORD);
            AcceptSeparators();
            if (tm.Peek(0).isPresent() && tm.Peek(0).get().getVal().equals("if")) {

                elseif = ParseIf();
            } else {
                elseif = Optional.of(ParseBlock());
            }
        }

        return Optional.of(new IfNode(condition, statements, elseif));
    }

    /**
     * ParseWhile method that creates a new WhileNode. It checks for a while statement
     * and takes the condition and block and creates a new WhileNode.
     * @throws IllegalArgumentException if the 'while' statement is invalid
     * @return WhileNode or "Invalid while loop"
     */
    public Optional<Node> ParseWhile()
    {
        if(tm.Peek(0).get().getVal().equals("while"))
        {
            tm.MatchandRemove(Token.TokenType.KEYWORD);
            tm.MatchandRemove(Token.TokenType.OPENPARENTHESIS);
            Optional<Node> exp = ParseOperation();
            tm.MatchandRemove(Token.TokenType.CLOSEDPARENTHESIS);
            Optional<BlockNode> block = Optional.of(ParseBlock());
            return Optional.of(new WhileNode(exp.get(), block.get()));
        }else{
            throw new IllegalArgumentException("Invalid while loop");
        }
    }

    /**
     * ParseDoWhile method that creates a new DoWhileNode. It checks for a do statement
     * and takes the block statements of "do" and takes the condition of "while" and creates a new DoWhileNode.
     * @throws IllegalArgumentException if the 'do while' statement is invalid
     * @return DoWhileNode or "Invalid do while loop"
     */
    public Optional<Node>ParseDoWhile()
    {
        if(tm.Peek(0).get().getVal().equals("do"))
        {
            tm.MatchandRemove(Token.TokenType.KEYWORD);
            AcceptSeparators();
            tm.MatchandRemove(Token.TokenType.OPENBRACKET);
            Optional<BlockNode> statements = Optional.of(ParseBlock());
            tm.MatchandRemove(Token.TokenType.CLOSEDBRACKET);
            if(tm.Peek(0).get().getVal().equals("while"))
            {
                tm.MatchandRemove(Token.TokenType.KEYWORD);
                Optional<Node> condition = ParseOperation();
                return Optional.of(new DoWhileNode((StatementNode) condition.get(), statements.get()));
            }else{
                throw new IllegalArgumentException("Invalid do while loop");
            }

        }
        throw new IllegalArgumentException("Invalid do while loop");
    }

    /**
     * ParseFor method that creates a new ForNode or ForeachNode. It checks for a for statement
     * and takes initialization expression, condition expression, increment expression, and block statements and creates a new ForNode.
     * It also checks for a foreach statement by taking the variable, 'in', and block statements and creates a new ForeachNode.
     * @throws IllegalArgumentException if the 'for' statement is invalid
     * @return ForNode or ForeachNode or "Invalid for loop"
     */
    public Optional<Node> ParseFor() {
        if (tm.Peek(0).get().getVal().equals("for")) {
            tm.MatchandRemove(Token.TokenType.KEYWORD);
            if (tm.Peek(0).get().getType().equals(Token.TokenType.OPENPARENTHESIS)) {
                tm.MatchandRemove(Token.TokenType.OPENPARENTHESIS);
                Optional<Node> init = ParseOperation();

                if (!tm.Peek(0).get().getType().equals(Token.TokenType.SEPARATOR)) {

                    if (tm.Peek(0).get().getVal().equals("in")) {
                        tm.MatchandRemove(Token.TokenType.KEYWORD);
                        Optional<Node> expression = ParseOperation();
                        tm.MatchandRemove(Token.TokenType.CLOSEDPARENTHESIS);
                        Optional<BlockNode> block = Optional.of(ParseBlock());
                        return Optional.of(new ForeachNode((VariableReferenceNode) init.get(), expression.get(), block.get()));
                    } else {
                        throw new IllegalArgumentException("Invalid foreach loop");
                    }
                } else {
                    tm.MatchandRemove(Token.TokenType.SEPARATOR);
                    Optional<Node> condition = ParseOperation();
                        tm.MatchandRemove(Token.TokenType.SEPARATOR);
                    Optional<Node> increment = ParseOperation();
                    AcceptSeparators();
                    tm.MatchandRemove(Token.TokenType.CLOSEDPARENTHESIS);
                    Optional<BlockNode> block = Optional.of(ParseBlock());
                    return Optional.of(new ForNode((StatementNode) init.get(), (StatementNode) condition.get(), (StatementNode) increment.get(), block.get()));
                }
            }
        }
        throw new IllegalArgumentException("Invalid for loop");
    }


    /**
     * ParseReturn method that creates a new ReturnNode. It checks for a return statement
     * and takes the expression and creates a new ReturnNode.
     * @Throws IllegalArgumentException if the 'return' statement is invalid
     * @return ReturnNode or "Invalid return statement"
     */
    public Optional<Node>ParseReturn()
    {
        if(tm.Peek(0).get().getVal().equals("return"))
        {
            tm.MatchandRemove(Token.TokenType.KEYWORD);
            Optional<Node> exp = ParseOperation();
            AcceptSeparators();
            return Optional.of(new ReturnNode(exp.get()));
        }
        throw new IllegalArgumentException("Invalid return statement");
    }

    /**
     * ParseBreak method that creates a new BreakNode. It checks for a break statement
     * and creates a new BreakNode.
     * @Throws IllegalArgumentException if the 'break' statement is invalid
     * @return BreakNode or "Invalid break statement"
     */
    public Optional<Node>ParseBreak()
    {
        if(tm.Peek(0).get().getVal().equals("break"))
        {
            tm.MatchandRemove(Token.TokenType.KEYWORD);
            return Optional.of(new BreakNode());
        }
        throw new IllegalArgumentException("Invalid break statement");
    }

    /**
     * ParseContinue method that creates a new ContinueNode. It checks for a continue statement
     * and creates a new ContinueNode.
     * @Throws IllegalArgumentException if the 'continue' statement is invalid
     * @return ContinueNode or "Invalid continue statement"
     */
    public Optional<Node>ParseContinue()
    {
        if(tm.Peek(0).get().getVal().equals("continue"))
        {
            tm.MatchandRemove(Token.TokenType.KEYWORD);
            return Optional.of(new ContinueNode());
        }
        throw new IllegalArgumentException("Invalid continue statement");
    }

    /**
     * ParseDelete method that creates a new DeleteNode. It checks for a delete statement and matches and removes "delete".
     * It then takes a variableName(array) and checks for an open bracket. If there is an open bracket, it will call ParseOperation to get the expresions and to a linkedlist of indices, and creates a DeleteNode.
     * If there isnt an open bracket, it takes the variableName(array) and creates a new DeleteNode.
     * @Throws IllegalArgumentException if the 'delete' statement is invalid
     * @return DeleteNode or "Invalid array index"
     */
    public Optional<Node> ParseDelete() {
        if (tm.Peek(0).get().getVal().equals("delete")) {
            tm.MatchandRemove(Token.TokenType.KEYWORD);

            String variableName = tm.Peek(0).get().getVal();
            tm.MatchandRemove(Token.TokenType.WORD);


            if (tm.Peek(0).get().getVal().equals("[")) {
                tm.MatchandRemove(Token.TokenType.OPENBRACE);
                Optional<LinkedList> indices = Optional.of(new LinkedList());
                Optional<Node> expression = ParseOperation();

                if (expression.isPresent()) {
                    indices.get().add(expression.get());

                    while (tm.MatchandRemove(Token.TokenType.COMMA).isPresent()) {
                        expression = ParseOperation();
                        if (expression.isPresent()) {
                            indices.get().add(expression.get());
                        } else {
                            throw new IllegalArgumentException("Invalid array index");
                        }
                    }
                }else{
                    throw new IllegalArgumentException("Invalid array index");
                }

                tm.MatchandRemove(Token.TokenType.CLOSEDBRACE);

                return Optional.of(new DeleteNode(new VariableReferenceNode(variableName, Optional.empty()) , indices));
            } else {
                return Optional.of(new DeleteNode(new VariableReferenceNode(variableName, Optional.empty()), Optional.empty()));
            }
        }

        return Optional.empty();
    }

    /**
     * ParseFunctionCall method creates a new FunctionCallNode. it checks for a function name and looks for left and right parenthesis.
     * It also checks for parameters and creates a new FunctionCallNode. If It is an built in function then it checks for correct syntax and creates a new
     * FunctionCallNode.
     * @Throws IllegalArgumentException if the function call syntax is incorrect
     * @return FunctionCallNode or "Function call syntax is incorrect"
     */

    public Optional<Node> ParseFunctionCall() {
        LinkedList<Node> arguments = new LinkedList<>();
        String functionName;

        // Check if the first token is present
        Optional<Token> firstToken = tm.Peek(0);


        // Get the function name
        functionName = firstToken.get().getVal();

        if (tm.MatchandRemove(Token.TokenType.WORD).isPresent() && tm.Peek(0).get().getType().equals(Token.TokenType.OPENPARENTHESIS)) {
            // Parsing logic for regular functions
            while (!tm.MatchandRemove(Token.TokenType.CLOSEDPARENTHESIS).isPresent()) {
                Optional<Node> arg = ParseOperation();
                arg.ifPresent(arguments::add);
                if (!tm.MatchandRemove(Token.TokenType.COMMA).isPresent()) {
                    break;
                }
                tm.MatchandRemove(Token.TokenType.CLOSEDPARENTHESIS);
            }
            return Optional.of(new FunctionCallNode(functionName, arguments));
        } else if (functionName.equals("print")) {
            tm.MatchandRemove(Token.TokenType.KEYWORD);
            Optional<Node> arg = ParseOperation();
            while (arg.isPresent()) {
                arguments.add(arg.get());
                if (!tm.MatchandRemove(Token.TokenType.COMMA).isPresent()) {
                    break;
                }
                arg = ParseOperation();
            }
            tm.MatchandRemove(Token.TokenType.CLOSEDPARENTHESIS);
            return Optional.of(new FunctionCallNode(functionName, arguments));
        } else if (functionName.equals("printf")) {
            tm.MatchandRemove(Token.TokenType.KEYWORD);
            if (tm.MatchandRemove(Token.TokenType.OPENPARENTHESIS).isPresent()) {
                Optional<Node> formatString = ParseOperation();
                if (formatString.isPresent()) {
                    arguments.add(formatString.get());

                    while (!tm.MatchandRemove(Token.TokenType.CLOSEDPARENTHESIS).isPresent()) {
                        AcceptSeparators();
                        Optional<Node> arg = ParseOperation();
                        arg.ifPresent(arguments::add);
                        if (!tm.MatchandRemove(Token.TokenType.COMMA).isPresent()) {
                            break;
                        }
                    }
                }
            }
            return Optional.of(new FunctionCallNode(functionName, arguments));
        } else if (functionName.equals("exit")) {
            tm.MatchandRemove(Token.TokenType.KEYWORD);

            if (tm.Peek(0).isPresent() && tm.Peek(0).get().getType() == Token.TokenType.NUMBER) {
                Optional<Node> arg = ParseOperation();
                arg.ifPresent(arguments::add);
            }
            tm.MatchandRemove(Token.TokenType.SEMICOLON);
            return Optional.of(new FunctionCallNode(functionName, arguments));
        } else if (functionName.equals("getline")) {
            tm.MatchandRemove(Token.TokenType.KEYWORD);
            if (tm.MatchandRemove(Token.TokenType.WORD).isPresent()) {
                String targetVariable = tm.MatchandRemove(Token.TokenType.WORD).get().getVal();

                if (tm.Peek(0).isPresent() && tm.Peek(0).get().getType() == Token.TokenType.LEFTARROW) {
                    tm.MatchandRemove(Token.TokenType.LEFTARROW);
                    if (tm.Peek(0).isPresent() && tm.Peek(0).get().getType() == Token.TokenType.STRINGLITERAL) {
                        Optional<Node> arg = ParseOperation();
                        arg.ifPresent(arguments::add);
                    } else {
                        throw new IllegalArgumentException("Invalid getline statement");
                    }
                }
            } else {
                Optional<Node> arg = ParseOperation();
                arg.ifPresent(arguments::add);
            }
            return Optional.of(new FunctionCallNode(functionName, arguments));
        } else if (functionName.equals("nextfile") || functionName.equals("next")) {
            tm.MatchandRemove(Token.TokenType.KEYWORD);
            tm.MatchandRemove(Token.TokenType.SEMICOLON);
            return Optional.of(new FunctionCallNode(functionName, arguments));
        } else {
            return Optional.of(new FunctionCallNode(functionName, arguments));
        }
    }
}

