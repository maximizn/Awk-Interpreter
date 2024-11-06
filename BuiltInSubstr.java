import java.util.LinkedList;
import java.util.List;
// BuiltInSubstr is a built in awk like function that takes in a string, a start index and an optional length and returns a substring of the string.
public class BuiltInSubstr extends BuiltInFunctionDefinitionNode{
    public BuiltInSubstr()
    {
        super("substr", new LinkedList<>(List.of("string", "start", "length")), new LinkedList<>(), parameters -> {
            String str = parameters.get("string").getValue();
            // subtract 1 from start because awk is 1 indexed
            int start = Integer.parseInt(parameters.get("start").getValue()) - 1;
            String length = parameters.get("length").getValue();
            // if length is empty return the substring from start to the end of the string
            // else return the substring from start to start + length
            if (length.equals("")) {
                return String.valueOf(new InterpreterDataType(str.substring(start)));
            } else {
                return String.valueOf(new InterpreterDataType(str.substring(start, start + Integer.parseInt(length))));
            }
        }, false);
    }
}
