import java.util.LinkedList;
import java.util.List;
// BuiltInSplit is a built in awk like function that takes in a string, an array, a field separator, and a seps.
public class BuiltInSplit extends BuiltInFunctionDefinitionNode{
    public BuiltInSplit()
    {
        super("split", new LinkedList<>(List.of("string", "array", "fieldsep", "seps")), new LinkedList<>(), parameters -> {
            // Variables to hold the values of the parameters
            String str = parameters.get("string").getValue();
            String array = parameters.get("array").getValue();
            String fieldsep = parameters.get("fieldsep").getValue();
            String seps = parameters.get("seps").getValue();

            //  str.split(fieldsep, - 1).length is the number of elements created by splitting the string
            // -1 is used to include any empty strings that are at the end of the input
            for (int i = 0; i < str.split(fieldsep, - 1).length; i++) {
                parameters.put(array + (i + 1), new InterpreterDataType(str.split(fieldsep, - 1)[i]));
                // for testing purposes to see output
                System.out.println(array + "[" + (i + 1) + "]  = \"" + str.split(fieldsep, - 1)[i] + "\"");
            }
            if (!fieldsep.isEmpty()){
                // Creating a new List of strings that contains the optional field Separators used in the split function
                // I do this to add all the optional field separators that occured within the input to the parameters list
                List<String> separators = new LinkedList<>();
                for (int i = 1; i < str.split(fieldsep, - 1).length; i++) {
                    separators.add(fieldsep);
                }
                // Printing the optional field separators that occured within the input
                for(int i = 0; i < separators.size(); i++) {
                    parameters.put(seps + (i + 1) + "]", new InterpreterDataType(separators.get(i)));
                    // For testing purposes to see output
                    System.out.println(seps + "[" + (i + 1) + "] = \"" + fieldsep + "\"");
                }
            }
            // Adds the number of splits occurred to InterpreterDataType
            return String.valueOf(new InterpreterDataType(Integer.toString(str.split(fieldsep, - 1).length)));

        }, false);
    }
}

