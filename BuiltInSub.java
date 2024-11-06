import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
// BuiltInSub is built in awk like function that takes in a regexp, replacement, and a target and replaces the first occurrence of a pattern in a string with a replacement string.
public class BuiltInSub extends BuiltInFunctionDefinitionNode {
    public BuiltInSub()
    {
        super("sub", new LinkedList<>(List.of("regexp","replacement","target")), new LinkedList<>(), parameters ->{
            // Pattern and Matcher are used to find the first occurrence of a pattern in a string
            Pattern pattern = Pattern.compile(parameters.get("regexp").getValue());
            Matcher matcher = pattern.matcher(parameters.get("target").getValue());
            // replaceFirst replaces the first occurrence of a pattern in a string with a replacement string
            String result = matcher.replaceFirst(parameters.get("replacement").getValue());
            // set the value of the target variable to the result of the sub function
            var tidt = parameters.get("target");
            tidt.setValue(result);
            return String.valueOf(tidt);

        }, false);
    }
}
