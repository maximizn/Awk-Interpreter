import java.util.LinkedList;
// BuiltInNext is a built in awk like function that returns 1 if the next line is read and 0 if it is not
class BuiltInNext extends BuiltInFunctionDefinitionNode {
    public BuiltInNext(Interpreter.LineManager lineManager) {
        super("next", new LinkedList<>(), new LinkedList<>(), parameters -> {
            // Calls the SplitandAssign from Line Manager and returns 1 if it is successful and 0 if it is not
            return String.valueOf(lineManager.SplitandAssign() ? new InterpreterDataType("1") : new InterpreterDataType("0"));
        }, false);
    }
}


