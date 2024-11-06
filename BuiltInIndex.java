import java.util.LinkedList;
import java.util.List;
// BuiltInIndex is a built in awk like function that takes in a string in and a string find.
public class BuiltInIndex extends BuiltInFunctionDefinitionNode{
    public BuiltInIndex(){
        super("index", new LinkedList<>(List.of("in", "find")), new LinkedList<>(), paramaters -> {
            // Variable str takes in the value of the string in.
            String str = paramaters.get("in").getValue();
            // Variable substr takes in the value of the string find.
            String substr = paramaters.get("find").getValue();
            // Returns the index of the first occurrence of substr in str adds 1 to the index to match the awk index.
            return String.valueOf(new InterpreterDataType (str.indexOf(substr) + 1 + ""));
        }, false);

    }
}

