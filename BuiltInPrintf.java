import java.util.LinkedList;
import java.util.List;

// BuiltInPrintf is a built in awk like function that takes in a format string and an object of args and prints the formatted string
public class BuiltInPrintf extends BuiltInFunctionDefinitionNode{
    public BuiltInPrintf() {
        super("printf", new LinkedList<>(List.of("format", "args")), new LinkedList<>(), parameters -> {
            // Checks if user inputted a correct printf function
            if (parameters.get("format") != null)
            {
                // String format to hold the value of the format parameter
                String format = parameters.get("format").getValue();
                // Creates an array to hold the arguements. Parameters.size() - 1 is used to account for the format parameter
                Object[] args = new Object[parameters.size() - 1];
                for (int i = 1; i < args.length; i++) {
                    // Retrieves each args parameter and stores in InterpreterDataType param
                    InterpreterDataType param = parameters.get("args" + i);
                    if (param != null) {
                        // Stores the value of the parameter in the args array. Args[i - 1] is used to account for the format parameter
                        args[i - 1] = param.getValue();
                    }
                }
                System.out.printf(String.format(format, args));
                return String.valueOf(new InterpreterDataType(String.format(format, args)));

            } else {
                throw new RuntimeException("printf: format string not found");
            }
        }, true);
    }
}
