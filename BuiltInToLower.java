import java.util.LinkedList;
import java.util.List;
// BuiltInToLower is a built in awk like function that takes in a string and returns a string in all lowercase
public class BuiltInToLower extends BuiltInFunctionDefinitionNode{
    public BuiltInToLower()
    {
        super("toLower", new LinkedList<>(List.of("string")), new LinkedList<>(), parameters -> {
            String str = parameters.get("string").getValue();
            return String.valueOf(new InterpreterDataType(str.toLowerCase()));
        }, false);
    }
}
