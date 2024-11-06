import java.util.LinkedList;
import java.util.List;
// BuiltInPrint is a built in awk like function that prints the input
public class BuiltInPrint extends BuiltInFunctionDefinitionNode {
    public BuiltInPrint() {
        super("print", new LinkedList<String>(List.of("0")), new LinkedList<>(), parameters -> {
            if(parameters.get("0") != null)
            {
                // String val takes the value of the input
                InterpreterArrayDataType val = (InterpreterArrayDataType) parameters.get("0");
                for(String key : val.getArryData().keySet())
                {
                    System.out.println(val.getArrayElement(key));
                }
                // Returns the value of the input
                return String.valueOf(new InterpreterDataType(val.getValue()));
            }else {
                return String.valueOf(new InterpreterDataType("null"));
            }
        }, true);
    }

}
