import java.util.LinkedList;
import java.util.List;
// BuiltInLength is a built in awk like function that takes in a string.
public class BuiltInLength extends BuiltInFunctionDefinitionNode{
    public BuiltInLength(){
        super("length", new LinkedList<>(List.of("string")), new LinkedList<>(), paramaters ->{
            // Variable str takes in the value of the string.
            String str = paramaters.get("string").getValue();
            // length is the length of the string using Java's length function.
            int length = str.length();
            // Returns the length of the string as a String.valueOf.
            return String.valueOf(new InterpreterDataType(String.valueOf(length)));
        }, false);
    }
}
