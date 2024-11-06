import java.util.LinkedList;
import java.util.List;
// BuiltInSprintf is a built in awk like function that takes in a format string and an expression and returns a string
public class BuiltInSprintf extends BuiltInFunctionDefinitionNode{
    public BuiltInSprintf()
    {
        super("sprintf", new LinkedList<>(List.of("format", "expression")), new LinkedList<>(), parameters ->{
            // Checks if user inputted a correct sprintf function
            if (parameters.get("format") != null) {
                // String format to hold the value of the format parameter
                String format = parameters.get("format").getValue();
                // Creates an array to hold the expression. Parameters.size() - 1 is used to account for the format parameter
                Object[] expr = new Object[parameters.size()-1];
                for (int i = 1; i < expr.length; i++) {
                    if (parameters.get("expression" + i) != null) {
                        // Stores the value of the parameter in the expr array. expr[i - 1] is used to account for the format parameter
                        expr[i - 1] = parameters.get("expression" + i).getValue();
                    }
                }
                String formatted = String.format(format, expr);
                return String.valueOf(new InterpreterDataType(formatted));
            } else {
                throw new RuntimeException("sprintf: format string not found");
            }
        }, true);
    }
}
