import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
// BuiltInMatch is built in awk like function that takes in a string, a regexp, and an array and returns a string result.
public class BuiltInMatch extends BuiltInFunctionDefinitionNode {
    public BuiltInMatch()
    {
        super("match", new LinkedList<>(List.of("string", "regexp", "arr")), new LinkedList<>(), parameters ->{
            // Pattern and Matcher are used to find the first occurrence of the regexp in the string
            Pattern pattern = Pattern.compile(parameters.get("regexp").getValue());
            Matcher matcher = pattern.matcher(parameters.get("string").getValue());
            // arryName is used to store the name of the array
            String arryName = parameters.get("arr").getValue();
            // result is used to store the result of the match
            StringBuilder result = new StringBuilder();
            if(matcher.find())
            {
                if(!arryName.isEmpty())
                {
                    // puts the result of the match into IDT with the name of the array and the index of the match
                    for(int i = 1; i <= matcher.groupCount(); i++)
                    {
                        parameters.put(arryName + "[" + i + "]", new InterpreterDataType(String.valueOf(matcher.group(i))));
                    }
                }
                result.append(matcher.group());
                // appends the capture groups to the result
                for(int i = 1; i <= matcher.groupCount(); i++)
                {
                    result.append(" ").append(matcher.group(i));
                }
            }else {
                throw new RuntimeException("No match found");
            }
            return String.valueOf(new InterpreterDataType(result.toString()));
        }, false);
    }

}