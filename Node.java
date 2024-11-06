import java.util.LinkedList;
import java.util.Optional;
/**
 * Node is an abstract class that is the base class for all nodes.
 */
abstract class Node {

}
/**
 * StatementNode is an abstract class extends Node and is the base class for all statement nodes.
 */
abstract class StatementNode extends Node {

}
/**
 * BlockNode class extends Node and contains a LinkedList of StatementNodes and an optional Node.
 */
class BlockNode extends StatementNode{
    LinkedList<StatementNode> statements;
    Optional<Node> condition;

    /**
     * BlockNode constructor that initializes the statements LinkedList and sets the Condition to an empty optional.
     */
    BlockNode(LinkedList<StatementNode> statements, Optional<Node> condition) {
        this.statements = statements;
        this.condition = Optional.empty();
    }
    public LinkedList<StatementNode> getStatements()
    {
        return statements;
    }
    public Optional<Node> getCondition()
    {
        return condition;
    }


    /**
     * toString method that returns statements if statements.size() is greater than 0. If it is not, it returns an empty string.
     * @return statements
     */
    @Override
    public String toString() {
        if (statements.size() > 0) {
            return " " + statements;
        }
        else {
            return " ";
        }
    }
}
/**
 * ProgramNode class extends Node and contains four LinkedLists of BlockNodes and FunctionDefinitionNodes. ProgramNode is the top level node.
 */
class ProgramNode extends Node {
    LinkedList<BlockNode> Begin;
    LinkedList<BlockNode> End;
    LinkedList<BlockNode> Other;
    LinkedList<FunctionDefinitionNode> function;

    /**
     * ProgramNode constructor that takes in four LinkedLists of BlockNodes and FunctionDefinitionNodes and sets them to their respective variables.
     * @param begin
     * @param end
     * @param other
     * @param function
     */
    ProgramNode(LinkedList<BlockNode> begin, LinkedList<BlockNode> end, LinkedList<BlockNode> other, LinkedList<FunctionDefinitionNode> function) {
        Begin = begin;
        End = end;
        Other = other;
        this.function = function;
    }

    /**
     * toString method that returns a string result of the ProgramNode.
     * @return result string
     */
    @Override
    public String toString() {
        String result = "";
        if(Begin.size() > 0) {
            result += "BEGIN{}";
        }
        if (End.size() > 0) {
            result += "END{}";
        }
        if (Other.size() > 0) {
            result += "OTHER{}";
        }
        if (function.size() > 0) {
            result += "FUNCTION{}";
        }
        return result;
    }

}

/**
 * FunctionDefinitionNode class extends Node and contains a function name, a LinkedList of parameters, and a LinkedList of statements.
 */
class FunctionDefinitionNode extends Node{

    final String functionName;
    final LinkedList<String> parameters;
    final LinkedList<StatementNode> statements;

    /**
     * FunctionDefinitionNode constructor that takes in a function name, a LinkedList of parameters, and a LinkedList of statements.
     * @param functionName
     * @param parameters
     * @param statements
     */
    public FunctionDefinitionNode(String functionName, LinkedList<String> parameters, LinkedList<StatementNode> statements) {
        this.functionName = functionName;
        this.parameters = parameters;
        this.statements = new LinkedList<>();
    }

    /**
     * addStatement method that takes in a StatementNode and adds it to the statements LinkedList.
     * @param statement
     */

    public void addStatement(StatementNode statement) {
        statements.add(statement);
    }

    /**
     * toString method that returns the function name, parameters, and statements.
     * @return
     */
    @Override
    public String toString() {
        return "Function " + functionName + " (" + parameters + ")" + "\n" +
                statements + "\n" +
                " }";
    }

}
/**
 * OperationNode class extends Node and contains a left Node, an optional right Node, and a MathOperation enum.
 */

class OperationNode extends StatementNode {
    /**
     * MathOperation enum that contains all the math operations.
     */
    enum MathOperation {
        EQ, NE, LT, LE, GT, GE, AND, OR, NOT, MATCH, NOTMATCH,DOLLAR,
        PREINC, POSTINC, PREDEC, POSTDEC, UNARYPOS, UNARYNEG, IN,
        EXPONENT, ADD, SUBTRACT, MULTIPLY, DIVIDE, MODULO, CONCATENATION
    }

    private final Node left;
    private final Optional<Node> right;
    private final MathOperation operation;

    /**
     * OperationNode constructor that takes in a left Node, a MathOperation enum, and an optional right Node.
     * @param left
     * @param operation
     * @param right
     */
    public OperationNode(Node left, MathOperation operation, Optional<Node> right) {
        this.left = left;
        this.right = right;
        this.operation = operation;
    }

    /**
     * OperationNode constructor that takes in a left Node and a MathOperation enum.
     * @param left
     * @param operation
     */
    public OperationNode(Node left, MathOperation operation) {
        this.left = left;
        this.right = Optional.empty();
        this.operation = operation;
    }

    /**
     * getLeft method that returns the left Node.
     * @return left
     */

    public Node getLeft() {
        return left;
    }

    /**
     * getRight method that returns the right Node.
     *
     * @return right
     */
    public Optional<Node> getRight() {
        return right;
    }

    /**
     * getOperation method that returns the MathOperation enum.
     * @return operation
     */
    public Enum<MathOperation> getOperation() {
        return operation;
    }

    /**
     * toString method that returns the left Node, the operation, and the right Node if it is present.
     * @return left, operation, right
     */

    public String toString() {

        if (right.isEmpty()) {
            return left + " " + operation;
        }
        return left + " " + operation + " " + right;
    }
}

/**
 * VariableReferenceNode class extends Node and contains a name and an optional index.
 */
class VariableReferenceNode extends Node{
    private String name;
    private final Optional<Node> index;

    /**
     * VariableReferenceNode constructor that takes in a name and an optional index.
     * @param name
     * @param index
     */

    public VariableReferenceNode(String name, Optional<Node> index) {
        this.name = name;
        this.index = index;
    }
    public Optional<Node> getIndex()
    {
        return index;
    }
    public String getName()
    {
        return name;
    }
    public void setValue(InterpreterDataType idt)
    {
    }

    /**
     * getName method that returns the name.
     * @return name, index
     */

    public String toString()
    {
        if(index.isPresent())
        {
            return name + "[" + index + "]";
        }
        else
        {
            return name;
        }
    }
}
/**
 * ConstantNode class extends Node and contains a value.
 */
class ConstantNode extends Node{
    private final String value;

    /**
     * ConstantNode constructor that takes in a value.
     * @param value
     */
    public ConstantNode(String value) {
        this.value = value;
    }
    public String getValue()
    {
        return value;
    }
    /**
     *toString method that returns the value.
     * @return value
     */
    public String toString() {
        return value;
    }
}
/**
 * PatternNode class extends Node and contains a pattern.
 */
class PatternNode extends Node{
    private final String pattern;

    /**
     * PatternNode constructor that takes in a pattern.
     * @param pattern
     */
    public PatternNode(String pattern) {
        this.pattern = pattern;
    }
    public String getPattern()
    {
        return pattern;
    }

    /**
     * toString method that returns the pattern.
     * @return pattern
     */
    public String toString() {
        return pattern;
    }
}
class AssignmentNode extends StatementNode{
    private final Node variable;
    private final Node expression;
    public AssignmentNode(Node variable, Node expression) {

        this.expression = expression;
        this.variable = variable;
    }
    public Node getRight()
    {
        return expression;
    }
    public Node getLeft()
    {
        return variable;
    }
    public  String toString()
    {
        return variable + " " + expression;
    }
}
class TernaryNode extends Node {
    private final Node condition;
    private final Node consequent;
    private final Node alternate;

    public TernaryNode(Node condition, Node consequent, Node alternate) {
        this.condition = condition;
        this.consequent = consequent;
        this.alternate = alternate;
    }

    public Node getCondition() {
        return condition;
    }

    public Node getConsequent() {
        return consequent;
    }

    public Node getAlternate() {
        return alternate;
    }

    @Override
    public String toString() {
        return condition + " ? " + consequent + " : " + alternate;
    }
}
class IfNode extends StatementNode{
    private final Optional<Node> condition;
    private final BlockNode statements;
    private final Optional<Node> elseIf;


    public IfNode(Optional<Node> condition, BlockNode statements, Optional<Node> elseIf) {
        this.condition = condition;
        this.statements = statements;
        this.elseIf = elseIf;

    }
    public Optional<Node> getCondition()
    {
        return condition;
    }
    public BlockNode getStatements()
    {
        return statements;
    }
    public Optional<Node> getElseIf()
    {
        return elseIf;
    }
    public String toString()
    {
        return " " + condition + " " + statements;
    }

}
class WhileNode extends StatementNode{
    private final Node condition;
    private final BlockNode statements;
    public WhileNode(Node condition, BlockNode statements) {
        this.condition = condition;
        this.statements = statements;
    }
    public Node getCondition()
    {
        return condition;
    }
    public BlockNode getStatements()
    {
        return statements;
    }
    public String toString()
    {
        return " " + condition + " " + statements;
    }
}
class DoWhileNode extends StatementNode{
    private final StatementNode condition;
    private final BlockNode statements;
    public DoWhileNode(StatementNode condition, BlockNode statements) {
        this.condition = condition;
        this.statements = statements;
    }
    public StatementNode getCondition()
    {
        return condition;
    }
    public BlockNode getStatements()
    {
        return statements;
    }
    public String toString()
    {
        return " " + condition + " " + statements;
    }
}
class ForNode extends StatementNode{
    private final Node initialization;
    private final Node condition;
    private final Node increment;
    private final BlockNode statements;
    public ForNode(Node initialization, Node condition, Node increment, BlockNode statements) {
        this.initialization = initialization;
        this.condition = condition;
        this.increment = increment;
        this.statements = statements;
    }
    public Node getInitialization()
    {
        return initialization;
    }
    public Node getCondition()
    {
        return condition;
    }
    public Node getIncrement()
    {
        return increment;
    }
    public BlockNode getStatements()
    {
        return statements;
    }

    public String toString()
    {
        return " " + initialization + " " + condition + " " + increment + " " + statements;
    }
}
class ForeachNode extends StatementNode{
    private final VariableReferenceNode variable;
    private final Node array;
    private final BlockNode statements;
    public ForeachNode(VariableReferenceNode variable, Node array, BlockNode statements) {
        this.variable = variable;
        this.array = array;
        this.statements = statements;
    }
    public Node getArray()
    {
        return array;
    }
    public VariableReferenceNode getVariable()
    {
        return variable;
    }
    public BlockNode getStatements()
    {
        return statements;
    }
    public String toString()
    {
        return "" + variable + " in " + array + " " + statements;
    }
}
class ReturnNode extends StatementNode{
    private final Node expression;
    public ReturnNode(Node expression) {
        this.expression = expression;
    }
    public Node getExpression()
    {
        return expression;
    }
    public String toString()
    {
        return "Return " + expression;
    }
}
class BreakNode extends StatementNode {
    public BreakNode() {
    }

    public String toString() {
        return "break;";
    }
}
class ContinueNode extends StatementNode{
    public ContinueNode()
    {

    }
    public String toString()
    {
        return "continue;";
    }
}
class DeleteNode extends StatementNode{
    private final VariableReferenceNode name;
    private final Optional<LinkedList> indices;

    public DeleteNode(VariableReferenceNode name, Optional<LinkedList> indices) {
        this.name = name;
        this.indices = indices;
    }
    public String getName()
    {
        return name.getName();
    }
    public Optional<LinkedList> getIndices()
    {
        return indices;
    }
    public String toString()
    {
        return " " + name + " " + indices;
    }

}
class FunctionCallNode extends StatementNode{
    private final String functionName;
    private final LinkedList<Node> arguments;
    public FunctionCallNode(String functionName, LinkedList<Node> arguments) {
        this.functionName = functionName;
        this.arguments = arguments;
    }
    public String getFunctionName()
    {
        return functionName;
    }
    public LinkedList<Node> getArguments()
    {
        return arguments;
    }
    public String toString()
    {
        return " " + functionName + " " + arguments;
    }
}
