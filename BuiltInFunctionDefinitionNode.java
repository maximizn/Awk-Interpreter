import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.Function;
// BuiltInFunctionDefinitionNode is the class that is used to define a built in function
class BuiltInFunctionDefinitionNode extends FunctionDefinitionNode{

    // lambda function that takes in a HashMap of String and InterDataType and returns an InterpreterDataType
    Function<HashMap<String, InterpreterDataType>, String> Execute;
    // isVariadic is used to determine if the function is variadic or not set to false by default
    boolean isVariadic = false;



    /**
     * FunctionDefinitionNode constructor that takes in a function name, a LinkedList of parameters, a LinkedList of statements, a lambda function, and a boolean.
     *
     * @param functionName
     * @param parameters
     * @param statements
     * @param Execute
     * @param isVariadic
     */
    public BuiltInFunctionDefinitionNode(String functionName, LinkedList<String> parameters, LinkedList<StatementNode> statements, Function<HashMap<String, InterpreterDataType>, String> Execute, boolean isVariadic) {
        super(functionName, parameters, statements);
        this.Execute = Execute;
        this.isVariadic = isVariadic;
    }

    /**
     * FunctionDefinitionNode constructor that takes in a function name, a LinkedList of parameters, a LinkedList of statements, and a lambda function.
     * @param parameters
     * @return Execute.apply(parameters)
     */
    public String Execute(HashMap<String, InterpreterDataType> parameters) {
        return Execute.apply(parameters);
    }
    public boolean isVariadic() {
        return isVariadic;
    }

}

