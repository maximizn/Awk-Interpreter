import java.util.LinkedList;
import java.util.List;
// BuiltInToUpper is built in awk like function that takes in a string and returns a string in all uppercase
public class BuiltInToUpper extends BuiltInFunctionDefinitionNode{
    public BuiltInToUpper()
    {
        super("toUpper", new LinkedList<>(List.of("string")), new LinkedList<>(), parameters -> {
            String str = parameters.get("string").getValue();
            return String.valueOf(new InterpreterDataType(str.toUpperCase()));
        }, false);
    }
}
