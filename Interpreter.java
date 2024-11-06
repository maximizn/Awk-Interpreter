import java.io.IOException;
import java.util.HashMap;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

// Interpreter is used to interpret the AST
public class Interpreter {
    HashMap<String, InterpreterDataType> globalVariables;
    HashMap<String, FunctionDefinitionNode> functionVariables;
    private LineManager LineManager;


    public Interpreter(ProgramNode programNode, String filePath) throws IOException {
        globalVariables = new HashMap<>();
        functionVariables = new HashMap<>();
        // Populate function variables with function definitions
        for (FunctionDefinitionNode function : programNode.function) {
            functionVariables.put(function.functionName, function);
        }
        // filepath if not null read all lines and create a LineManager
        if (filePath != null) {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            LineManager = new LineManager(lines);

        } // else create an empty LineManager
        else {
            LineManager = new LineManager(new LinkedList<String>());
        }
        // Built in variables
        globalVariables.put("FILENAME", new InterpreterDataType(filePath));
        globalVariables.put("FS", new InterpreterDataType(" "));
        globalVariables.put("OFMT", new InterpreterDataType("%.6g"));
        globalVariables.put("OFS", new InterpreterDataType("\n"));
        // Built in functions and add their definitions to functionVariables
        BuiltInFunctionDefinitionNode printf = new BuiltInPrintf();
        functionVariables.put("printf", printf);
        BuiltInFunctionDefinitionNode print = new BuiltInPrint();
        functionVariables.put("print", print);
        BuiltInFunctionDefinitionNode getline = new BuiltInGetLine(LineManager);
        functionVariables.put("getline", getline);
        BuiltInFunctionDefinitionNode gsub = new BuiltInGsub();
        functionVariables.put("gsub", gsub);
        BuiltInFunctionDefinitionNode subs = new BuiltInSub();
        functionVariables.put("sub", subs);
        BuiltInFunctionDefinitionNode match = new BuiltInMatch();
        functionVariables.put("match", match);
        BuiltInFunctionDefinitionNode index = new BuiltInIndex();
        functionVariables.put("index", index);
        BuiltInFunctionDefinitionNode length = new BuiltInLength();
        functionVariables.put("length", length);
        BuiltInFunctionDefinitionNode split = new BuiltInSplit();
        functionVariables.put("split", split);
        BuiltInFunctionDefinitionNode substr = new BuiltInSubstr();
        functionVariables.put("substr", substr);
        BuiltInFunctionDefinitionNode tolower = new BuiltInToLower();
        functionVariables.put("tolower", tolower);
        BuiltInFunctionDefinitionNode toupper = new BuiltInToUpper();
        functionVariables.put("toupper", toupper);
        BuiltInFunctionDefinitionNode next = new BuiltInNext(LineManager);
        functionVariables.put("next", next);
    }

    public InterpreterDataType getIDT(Node node, HashMap<String, InterpreterDataType> localVariables) {

        if (node instanceof AssignmentNode) {
            //make sure that the target is a variable (variableReferenceNode or OperationNode with type DOLLAR).
            // Call GetIDT on the right side of the assignment. Set the target’s value to the result.
            // Return the result.

            AssignmentNode assignmentNode = (AssignmentNode) node;
            InterpreterDataType idt = getIDT(assignmentNode.getRight(), globalVariables);
            if (assignmentNode.getLeft() instanceof VariableReferenceNode) {
                ((VariableReferenceNode) assignmentNode.getLeft()).setValue(idt);
                globalVariables.put(assignmentNode.getLeft().toString(), idt);
            } else if (assignmentNode.getLeft() instanceof OperationNode) {

                if (((OperationNode) assignmentNode.getLeft()).getOperation() == OperationNode.MathOperation.DOLLAR) {
                    String var = "$" + ((OperationNode) assignmentNode.getLeft()).toString();
                    globalVariables.get(var);
                }
            } else {
                throw new RuntimeException("AssignmentNode: Left side of assignment is not a variable");
            }

            return idt;
        } else if (node instanceof ConstantNode) {
            // Return the value of the constant
            ConstantNode constantNode = (ConstantNode) node;
            InterpreterDataType idt = new InterpreterDataType(constantNode.getValue());
            return idt;
        } else if (node instanceof FunctionCallNode) {
            FunctionCallNode functionCallNode = (FunctionCallNode) node;
            String idt = RunFunctionCall(functionCallNode, globalVariables);
            return new InterpreterDataType(idt);
        } else if (node instanceof PatternNode) {
            //  This is a case where someone is trying to pass a pattern to a function or an assignment. Throw an exception.
            throw new RuntimeException("PatternNode is not a function or assignment");
        } else if (node instanceof TernaryNode) {
            //evaluate the boolean condition (using GetIDT)
            // if true, evaluate the true case (using GetIDT) and return the result
            // if false, evaluate the false case (using GetIDT) and return the result
            TernaryNode ternaryNode = (TernaryNode) node;
            InterpreterDataType idt = getIDT(ternaryNode.getCondition(), globalVariables);
            if (idt.getValue().equals("true")) {
                return getIDT(ternaryNode.getConsequent(), globalVariables);
            } else {
                return getIDT(ternaryNode.getAlternate(), globalVariables);
            }
        } else if (node instanceof VariableReferenceNode) {
            VariableReferenceNode varRefNode = (VariableReferenceNode) node;
            String name = varRefNode.getName();
            if(varRefNode.getIndex().isPresent())
            {
               String index = String.valueOf(varRefNode.getIndex().get());
               try{
                   int indexInt = Integer.parseInt(index);
                   if(indexInt < 0)
                   {
                       throw new RuntimeException("Index out of bounds");
                   }
                   if(indexInt > 0)
                   {
                       InterpreterArrayDataType arry = (InterpreterArrayDataType)globalVariables.get(name);
                       return new InterpreterDataType(String.valueOf(arry.getArryData().get(index)));
                   }
               }catch(NumberFormatException e)
               {
                   throw new RuntimeException("Index out of bounds");
               }
            }else if(varRefNode.getIndex().isEmpty())
            {
                if(globalVariables.get(name) instanceof InterpreterArrayDataType)
                {
                    return new InterpreterDataType(name);
                }
                else {
                    if (localVariables.containsKey(name))
                    {
                        return localVariables.get(name);
                    } else if (globalVariables.containsKey(name))
                    {
                        return globalVariables.get(name);
                    } else {
                        globalVariables.put(name, new InterpreterDataType(name));
                        return new InterpreterDataType(name);


                    }
             }

            }

        } else if (node instanceof OperationNode) {
            OperationNode operationNode = (OperationNode) node;
            String operation = operationNode.getOperation().name(); // Convert the enum to a string

            switch (operation) {
                case "ADD":
                case "SUBTRACT":
                case "MULTIPLY":
                case "DIVIDE":
                case "MODULO":
                case "EXPONENT":
                    InterpreterDataType leftIDT = getIDT(operationNode.getLeft(), globalVariables);
                    InterpreterDataType rightIDT = getIDT(operationNode.getRight().get(), globalVariables);
                    float leftValue = Float.parseFloat(leftIDT.getValue());
                    float rightValue = Float.parseFloat(rightIDT.getValue());
                    float result = 0.0f;

                    switch (operation) {
                        case "ADD":
                            result = leftValue + rightValue;
                            break;
                        case "SUBTRACT":
                            result = leftValue - rightValue;
                            break;
                        case "MULTIPLY":
                            result = leftValue * rightValue;
                            break;
                        case "DIVIDE":
                            if (rightValue == 0) {
                                throw new ArithmeticException("Division by zero");
                            }
                            result = leftValue / rightValue;
                            break;
                        case "MODULO":
                            if (rightValue == 0) {
                                throw new ArithmeticException("Modulo by zero");
                            }
                            result = leftValue % rightValue;
                            break;
                        case "EXPONENT":
                            result = (float) Math.pow(leftValue, rightValue);
                            break;
                    }
                    return new InterpreterDataType(Float.toString(result));
                case "LT":
                case "LE":
                case "GT":
                case "GE":
                case "EQ":
                case "NE":
                    InterpreterDataType left = getIDT(operationNode.getLeft(), globalVariables);
                    InterpreterDataType right = getIDT(operationNode.getRight().get(), globalVariables);

                    boolean comparisonResult;
                    try {
                        float leftFloat = Float.parseFloat(left.getValue());
                        float rightFloat = Float.parseFloat(right.getValue());

                        switch (operation) {
                            case "LT":
                                comparisonResult = leftFloat < rightFloat;
                                break;
                            case "LE":
                                comparisonResult = leftFloat <= rightFloat;
                                break;
                            case "GT":
                                comparisonResult = leftFloat > rightFloat;
                                break;
                            case "GE":
                                comparisonResult = leftFloat >= rightFloat;
                                break;
                            case "EQ":
                                comparisonResult = leftFloat == rightFloat;
                                break;
                            case "NE":
                                comparisonResult = leftFloat != rightFloat;
                                break;
                            default:
                                // Handle other comparison operators
                                throw new RuntimeException("Unsupported comparison operator");
                        }
                    } catch (NumberFormatException e) {
                        // If conversion to float fails, compare as strings
                        switch (operation) {
                            case "LT":
                                comparisonResult = left.getValue().compareTo(right.getValue()) < 0;
                                break;
                            case "LE":
                                comparisonResult = left.getValue().compareTo(right.getValue()) <= 0;
                                break;
                            case "GT":
                                comparisonResult = left.getValue().compareTo(right.getValue()) > 0;
                                break;
                            case "GE":
                                comparisonResult = left.getValue().compareTo(right.getValue()) >= 0;
                                break;
                            case "EQ":
                                comparisonResult = left.getValue().equals(right.getValue());
                                break;
                            case "NE":
                                comparisonResult = !left.getValue().equals(right.getValue());
                                break;
                            default:
                                // Handle other comparison operators for strings
                                throw new RuntimeException("Error in comparison operator");
                        }
                    }
                    return new InterpreterDataType(Boolean.toString(comparisonResult));
                case "AND":
                case "OR":
                case "NOT":
                    InterpreterDataType op = getIDT(operationNode.getLeft(), globalVariables);
                    InterpreterDataType op2 = getIDT(operationNode.getRight().get(), globalVariables);
                    boolean opValue;
                    try {
                        float floatValue = Float.parseFloat(op.getValue());
                        float floatValue2 = Float.parseFloat(op2.getValue());
                        switch (operation) {
                            case "AND":
                                opValue = floatValue != 0 && floatValue2 != 0;
                                break;
                            case "OR":
                                opValue = floatValue != 0 || floatValue2 != 0;
                                break;
                            case "NOT":
                                opValue = floatValue == 0;
                                break;
                            default:
                                // Handle other boolean operations
                                throw new RuntimeException("Unsupported boolean operation");
                        }


                    } catch (NumberFormatException e) {
                        switch (operation) {
                            case "AND":
                                opValue = op.getValue().equals("true") && op2.getValue().equals("true");
                                break;
                            case "OR":
                                opValue = op.getValue().equals("true") || op2.getValue().equals("true");
                                break;
                            case "NOT":
                                opValue = op.getValue().equals("false");
                                break;
                            default:
                                // Handle other boolean operations for non-numeric values
                                throw new RuntimeException("Unsupported boolean operation");
                        }
                    }
                    return new InterpreterDataType(Boolean.toString(opValue));
                case "MATCH":
                case "NOTMATCH":
                    // Retrieves the right operand to make sure it has a pattern
                    if (operationNode.getRight().toString().contains("~")) {
                        // Takes right operand and converts it to a pattern
                        // Takes left operand and converts it to a string
                        // Uses java's regex functionality to match the left operand to the pattern\
                        PatternNode patternNode = (PatternNode) operationNode.getRight().orElse(null);
                        String leftOp = String.valueOf(getIDT(operationNode.getLeft(), globalVariables));
                        Pattern pattern = Pattern.compile(patternNode.getPattern());
                        Matcher match = pattern.matcher(leftOp);
                        boolean matchResult = match.matches();
                        String resultString = matchResult ? "true" : "false";
                        return new InterpreterDataType(resultString);
                    } else {
                        throw new RuntimeException("Right side must be a pattern");

                    }
                case "DOLLAR":
                    InterpreterDataType idt = getIDT(operationNode.getLeft(), globalVariables);
                    String var = "$" + idt.getValue();
                    localVariables.put(var, new InterpreterDataType(var));
                    if (localVariables.containsKey(var)) {
                        return localVariables.get(var);
                    } else {
                        throw new RuntimeException("Variable '" + var + "' not found");
                    }
                case "PREINC":
                case "POSTINC":
                case "PREDEC":
                case "UNARYPOS":
                case "UNARYNEG":
                    InterpreterDataType operand = getIDT(operationNode.getLeft(), globalVariables);
                    float operandValue = Float.parseFloat(operand.getValue());
                    result = 0.0f;
                    switch (operation) {
                        case "PREINC":
                            result = ++operandValue;
                            break;
                        case "POSTINC":
                            result = operandValue + 1;
                            break;
                        case "PREDEC":
                            result = --operandValue;
                            break;
                        case "POSTDEC":
                            result = operandValue--;
                            break;
                        case "UNARYPOS":
                            result = +operandValue;
                            break;
                        case "UNARYNEG":
                            result = -operandValue;
                            break;
                    }
                    return new InterpreterDataType(Float.toString(result));
                case "CONCATENATION":
                    // Retrieves the left and right operands to concatenate
                    InterpreterDataType leftOperand = getIDT(operationNode.getLeft(), localVariables);
                    InterpreterDataType rightOperand = getIDT(operationNode.getRight().get(), localVariables);
                    // Concatenates the left and right operands
                    String conCat = leftOperand.getValue() + rightOperand.getValue();
                    // Adds the concatenated string to the global variables
                    localVariables.put(conCat, new InterpreterDataType(conCat));
                    return new InterpreterDataType(conCat);
                case "IN":
                    if (operationNode.getRight().get() instanceof VariableReferenceNode) {
                        if (((VariableReferenceNode) operationNode.getRight().get()).getIndex().isEmpty()) {
                            String arrayName = getIDT(operationNode.getRight().get(), localVariables).getValue();
                            InterpreterArrayDataType arrayData = new InterpreterArrayDataType (((InterpreterArrayDataType)globalVariables.get(arrayName)).getArryData());
                            if (localVariables.containsKey(arrayName) || globalVariables.containsKey(arrayName)) {
                                // Retrieves the appropriate data based on the scope (local or global)
                                    String arry = String.valueOf(localVariables.containsKey(arrayName)
                                        ? localVariables.get(arrayName) : globalVariables.get(arrayName));
                                if (arrayData instanceof InterpreterArrayDataType) {
                                    InterpreterDataType leftData = getIDT(operationNode.getLeft(), localVariables);
                                    if (leftData != null) {
                                        return new InterpreterDataType(String.valueOf(arrayData.getArryData().containsKey(String.valueOf(leftData.getValue()))));
                                    }
                                }
                            }

                            // If it's not found in either localVariables or globalVariables, throw an exception
                            throw new RuntimeException("Variable '" + arrayName + "' not found in either local or global variables");
                        }
                    }
            }
        }
        return null;
    }

    public ReturnType ProcessStatement(HashMap<String, InterpreterDataType> locals, StatementNode stmt) {
        //AssignmentNode:
//        if (stmt instanceof AssignmentNode) {
//            //Use GetIDT to evaluate the left and right side,
//            InterpreterDataType leftOp = getIDT(((AssignmentNode) stmt).getLeft(), locals);
//            InterpreterDataType rightOp = getIDT(((AssignmentNode) stmt).getRight(), locals);
//            // set left’s value equal to the GetIDT(right).
//            leftOp.setValue(rightOp.getValue());
//            // Return type None, and the value of right
//            return new ReturnType(null, rightOp.getValue());
//        }
        if (stmt instanceof BreakNode) {
            // return with a return type of break
            return new ReturnType(ExecutionResult.BREAK);
        }
        if (stmt instanceof ContinueNode) {
            // return with a return type of continue
            return new ReturnType(ExecutionResult.CONTINUE);
        }
        if (stmt instanceof DeleteNode) {
            InterpreterArrayDataType varRefNode = null;
            // Checks if the variable exists in local variables
            if (locals.containsKey(((DeleteNode) stmt).getName())) {
                // assigns the variable to varRefNode in locals
                varRefNode = (InterpreterArrayDataType) locals.get(((DeleteNode) stmt).getName());
            }
            // If not in local, checks global variables
            else if (globalVariables.containsKey(((DeleteNode) stmt).getName())) {
                varRefNode = (InterpreterArrayDataType) globalVariables.get(((DeleteNode) stmt).getName());
            }
            if (varRefNode != null) {
                // If the variable is an array, it loops through the indices and removes them
                if (((DeleteNode) stmt).getIndices().isPresent()) {
                    LinkedList<String> indices = ((DeleteNode) stmt).getIndices().get();
                    for (String index : indices) {
                        if (varRefNode.getArryData().containsKey(index)) {
                            varRefNode.getArryData().remove(String.valueOf(index));
                        } else {
                            throw new RuntimeException("Invalid index for deletion");
                        }
                    }
                    // If the indices are empty, it clears the array
                } else {
                    varRefNode.getArryData().clear();
                }
                return new ReturnType(ExecutionResult.NORMAL);
            } else {
                throw new RuntimeException("Variable not found");
            }
        }
        if (stmt instanceof DoWhileNode) {
            InterpreterDataType conditionValue = null;

            do {
                // Interprets the list of statements inside the do while loop
                ReturnType loopBodyResult = InterpretListOfStatements(((DoWhileNode) stmt).getStatements().getStatements(), locals);

                switch (loopBodyResult.getResultType()) {
                    case BREAK:
                        break;
                    case RETURN:
                        return loopBodyResult;
                    case CONTINUE:
                        continue;
                }

                // Evaluates the condition using GetIDT
                conditionValue = getIDT(((DoWhileNode) stmt).getCondition(), locals);

            } while (conditionValue.getValue().equals("true"));

            return new ReturnType(ExecutionResult.NORMAL);
        }
        if(stmt instanceof ForNode)
        {
            ForNode forNode = (ForNode) stmt;
            if (((ForNode) stmt).getInitialization() != null) {
                ProcessStatement(locals, ((StatementNode) forNode.getInitialization()));
            }
            InterpreterDataType conditionValue = getIDT(((ForNode) stmt).getCondition(), locals);
            while (conditionValue.getValue().equals("true")) {
                // Interpret the list of statements inside the for loop
                ReturnType loopBodyResult = InterpretListOfStatements(((ForNode) stmt).getStatements().getStatements(), locals);

                switch (loopBodyResult.getResultType()) {
                    case BREAK:
                        break;
                    case RETURN:
                        return loopBodyResult;
                    case CONTINUE:
                        continue;
                }

                // Executes the increment expression if it exists
                if (((ForNode) stmt).getIncrement() != null) {
                    ProcessStatement(locals, ((StatementNode) forNode.getIncrement()));
                }
                // ReEvaluates the condition for the next iteration
                conditionValue = getIDT(((ForNode) stmt).getCondition(), locals);
            }
            return new ReturnType(ExecutionResult.NORMAL, null);
        }
        if (stmt instanceof ForeachNode) {
            // Get the array data using GetIDT
            InterpreterDataType arrayData = getIDT(((ForeachNode) stmt).getArray(), locals);

            // Ensures the arrayData is of type InterpreterArrayDataType
            if (arrayData instanceof InterpreterArrayDataType) {
                // Loop through the array data and sets the variable to the current key
                for (String key : ((InterpreterArrayDataType) arrayData).getArryData().keySet()) {
                    locals.put(((ForeachNode) stmt).getVariable().toString(), new InterpreterDataType(key));
                    // Looks for the return type of the loop body
                    ReturnType loopBodyResult = InterpretListOfStatements(((ForeachNode) stmt).getStatements().getStatements(), locals);

                    switch (loopBodyResult.getResultType()) {
                        case BREAK:
                            break;
                        case RETURN:
                            return loopBodyResult;
                        case CONTINUE:
                            continue;
                    }
                }
            }
            else{
                throw new RuntimeException("ForeachNode: Array is not of type InterpreterArrayDataType");
            }
            return new ReturnType(ExecutionResult.NORMAL);
        }

        if(stmt instanceof FunctionCallNode)
        {
            // call RunFunctionCall and return the result
            FunctionCallNode functionCallNode = (FunctionCallNode) stmt;
            return new ReturnType(ExecutionResult.NORMAL, RunFunctionCall(functionCallNode, locals));
        }
        if (stmt instanceof IfNode) {
            IfNode currentIfNode = (IfNode) stmt;

            do {
                String conditionValue;
                Optional<Node> condition = currentIfNode.getCondition();

                // Evaluate the condition if it's present
                if (condition.isPresent()) {
                    conditionValue = getIDT(condition.get(), locals).getValue();
                } else {
                    // If there is no condition, it means it's an else block
                    conditionValue = String.valueOf(true);
                }

                // If the condition is true, interpret the statements within this if or else block
                if (conditionValue.equals("true")) {
                    return InterpretListOfStatements(currentIfNode.getStatements().getStatements(), locals);
                } else {
                    // Move to the next else-if or else statement
                    Optional<Node> elseIfNode = currentIfNode.getElseIf();
                    if (elseIfNode.isPresent()) {
                        Node nextNode = elseIfNode.get();
                        if (nextNode instanceof IfNode) {
                            currentIfNode = (IfNode) nextNode;
                        } else if (nextNode instanceof BlockNode) {
                            // Handles the BlockNode else block statements
                            return InterpretListOfStatements(((BlockNode) nextNode).getStatements(), locals);
                        } else {
                            currentIfNode = null;
                        }
                    } else {
                        currentIfNode = null;
                    }
                }
            } while (currentIfNode != null);

            // Return a normal execution result if none of the conditions are met
            return new ReturnType(ExecutionResult.NORMAL);
        }


            if(stmt instanceof ReturnNode)
        {
            if (((ReturnNode) stmt).getExpression() != null) {
                // Create a ReturnType with the value and result type "RETURN"
            return new ReturnType(ExecutionResult.RETURN, getIDT(((ReturnNode) stmt).getExpression(), locals).getValue());
        } else {
            // If there's no value, create a ReturnType with result type "RETURN" and a null value
            return new ReturnType(ExecutionResult.RETURN, null);
            }
        }
        if(stmt instanceof WhileNode)
        {
               InterpreterDataType conditionValue = getIDT(((WhileNode) stmt).getCondition(), locals);

                while (conditionValue.getValue().equals("true")) {
                    ReturnType loopBodyResult = InterpretListOfStatements(((WhileNode) stmt).getStatements().getStatements(), locals);

                    switch (loopBodyResult.getResultType()) {
                        case BREAK:
                           break;
                        case RETURN:
                            return loopBodyResult;
                        case CONTINUE:
                            continue;
                    }

                    conditionValue = getIDT(((WhileNode) stmt).getCondition(), locals);
                }


                return new ReturnType(ExecutionResult.NORMAL, null);

        }
        else{
            InterpreterDataType idt = getIDT(stmt, locals);
            if(idt != null)
            {
                return new ReturnType(ExecutionResult.NORMAL);
            }
        }
        return null;
    }


    public ReturnType InterpretListOfStatements(LinkedList<StatementNode> statements, HashMap<String, InterpreterDataType> locals) {
        ReturnType returnType = null;
        // Loops through the list of statements and calls ProcessStatement on each statement
        for (StatementNode stmt : statements) {
            returnType = ProcessStatement(locals, stmt);

            // If the return type is not normal, returns the return type
            if (!returnType.getResultType().equals(ExecutionResult.NORMAL)) {
                return returnType;
            }
        }

        // No return statement encountered, return the last ReturnType
        return returnType;
    }




    public String RunFunctionCall(FunctionCallNode functionCallNode, HashMap<String, InterpreterDataType> locals) {

        if (functionVariables.containsKey(functionCallNode.getFunctionName())) {
            FunctionDefinitionNode func = functionVariables.get(functionCallNode.getFunctionName());
            if (func instanceof BuiltInFunctionDefinitionNode) {
                BuiltInFunctionDefinitionNode builtInFunctionDefinitionNode = (BuiltInFunctionDefinitionNode) func;
                if (builtInFunctionDefinitionNode.isVariadic()) {

                    HashMap<String, InterpreterDataType> parameters = new HashMap<>();
                    String index = "0";
                    HashMap<String, InterpreterDataType> lambdaParameters = new HashMap<>();
                    // loops through the parameters of the function and add them to the HashMap
                    for (int i = 0; i < functionCallNode.getArguments().size(); i++) {
                        lambdaParameters.put(String.valueOf(i), getIDT(functionCallNode.getArguments().get(i), locals));
                    }
                    parameters.put(index, new InterpreterArrayDataType(lambdaParameters));
                    // calls the Execute function of the BuiltInFunctionDefinitionNode and return the result
                    return builtInFunctionDefinitionNode.Execute(parameters);
                } else {

                    HashMap<String, InterpreterDataType> parameters = new HashMap<>();
                    // loops through the parameters of the function and add them to the HashMap
                    for (int i = 0; i < functionCallNode.getArguments().size(); i++) {
                        parameters.put(String.valueOf(functionVariables.get(i)), getIDT(functionCallNode.getArguments().get(i), locals));
                    }
                    // calls the Execute function of the BuiltInFunctionDefinitionNode and return the result
                    return builtInFunctionDefinitionNode.Execute(parameters);
                }
            } else {

                HashMap<String, InterpreterDataType> parameters = new HashMap<>();
                //  loop through the parameters of the function and add them to the HashMap
                for (int i = 0; i < functionCallNode.getArguments().size(); i++) {
                    int j = 0;
                    parameters.put(String.valueOf(j), getIDT(functionCallNode.getArguments().get(i), locals));
                    j++;
                }
                return InterpretListOfStatements(func.statements, parameters).getReturnValue();
            }
        }
        else{
            throw new RuntimeException("Function not found");
        }
}

    // addLine is used to add a line to the LineManager for testing purposes
    public void addLine(String foo) {
        LineManager.lines.add(foo);
    }
    // InterpretProgram is used to interpret the program
    // Looks at the begin, other, and end blocks
    // Loops through the begin blocks and calls InterpretBlock
    public void InterpretProgram(ProgramNode block) {
        for (BlockNode begin : block.Begin) {
            InterpretBlock(begin);
        }
        while(LineManager.SplitandAssign())
        {
            for(BlockNode other : block.Other)
            {
            InterpretBlock(other);
            }

        }
        for (BlockNode end : block.End) {
            InterpretBlock(end);
        }
    }
    public void InterpretBlock(BlockNode block)
    {
        // If the block has a condition, we will test it to see if it is true.\
            if(block.getCondition().isPresent())
            {
                if(getIDT(block.getCondition().get(), globalVariables).getValue().equals("true"))
                {
                    // If the condition is true, we will call ProcessStatement for each statement in the block.
                    for(StatementNode stmt : block.getStatements())
                    {
                        ProcessStatement(globalVariables, stmt);
                    }
                }
            }
            else
            {
                // If there is no condition, we will call ProcessStatement for each statement in the block.
                for(StatementNode stmt : block.getStatements())
                {
                    ProcessStatement(globalVariables, stmt);
                }
            }
    }




    // LineManager is used to manage the lines of a file
    class LineManager {

        private List<String> lines;
        private int currentLine;
        private  int NR;
        private int FNR;

        public LineManager(List<String> lines) {
            this.lines = lines;
            this.currentLine = 0;
            this.FNR = 0;
        }

        public boolean SplitandAssign() {
            //checks if there are more lines to read.
            if (currentLine < lines.size()) {
                // Gets current line.
                String line = lines.get(currentLine);
                // Splits the line using the FS variable.
                String[] splitLine = line.split(globalVariables.get("FS").getValue());
                // Assigns the split line to the global variables $0, $1, etc.
                for (int i = 0; i < splitLine.length; i++) {
                    globalVariables.put("$" + i, new InterpreterDataType(splitLine[i]));
                }
                // Assigns the NF variable to the number of fields in the split line.
                globalVariables.put("NF", new InterpreterDataType(Integer.toString(splitLine.length)));
                NR++;
                FNR++;
                // Moves to next line
                currentLine++;
                return true;
            }
            return false;
        }

    }
}
