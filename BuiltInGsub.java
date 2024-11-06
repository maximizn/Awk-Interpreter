import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
// BuiltInGsub is a built in awk like function that takes in a regexp, a replacement, and a target.
class BuiltInGsub extends BuiltInFunctionDefinitionNode {
    public BuiltInGsub() {
        super("gsub", new LinkedList<>(List.of("regexp", "replacement", "target")), new LinkedList<>(), parameters -> {

            // Pattern and Matcher are used to find the regexp in the target and replace it with the replacement
            Pattern pattern = Pattern.compile(parameters.get("regexp").getValue());
            Matcher matcher = pattern.matcher(parameters.get("target").getValue());
            // result is the target with the regexp replaced with the replacement
            String result = matcher.replaceAll(parameters.get("replacement").getValue());
            // tidt is the target with the regexp replaced with the replacement
            var tidt = parameters.get("target");
            // Setting the value of tidt to the result
            tidt.setValue(result);
            return String.valueOf(tidt);
        }, false);
    }
}
