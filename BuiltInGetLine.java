import java.util.LinkedList;
import java.util.List;

// BuiltInGetLine is a built in awk like function that takes in a line manager and calls the SplitandAssign function from the Line Manager
public class BuiltInGetLine extends BuiltInFunctionDefinitionNode {
    public BuiltInGetLine(Interpreter.LineManager lineManager) {
        super("getline", new LinkedList<>(List.of("String")), new LinkedList<>(), parameters -> {
            // Calls the SplitandAssign from Line Manager and returns 1 if it is successful and 0 if it is not
            return String.valueOf(lineManager.SplitandAssign() ? new InterpreterDataType("1") : new InterpreterDataType("0"));
        }, false);
    }
}
