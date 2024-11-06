
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;

// Unit tests for the interpreter
public class InterpreterUnitTests {
    private Interpreter interpreter;
    private Interpreter.LineManager lineManager;

    //Tests the built-in function print
    @Test
    public void testPrint() throws IOException {
        ProgramNode programNode = new ProgramNode(new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
        Interpreter interpreter = new Interpreter(programNode, null);
        BuiltInFunctionDefinitionNode print = new BuiltInPrint();
        interpreter.functionVariables.put("print", print);
        interpreter.globalVariables.put("0", new InterpreterDataType("Hello, World!"));
        String result = ((BuiltInFunctionDefinitionNode) interpreter.functionVariables.get("print")).Execute.apply(interpreter.globalVariables).toString();
        assertEquals("Expected print to return 'Hello, World!'", "Hello, World!", result);
    }

    // Tests the built-in function printf
    @Test
    public void testPrintf() throws IOException {
        ProgramNode programNode = new ProgramNode(new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
        Interpreter interpreter = new Interpreter(programNode, null);
        BuiltInFunctionDefinitionNode printf = new BuiltInPrintf();
        interpreter.functionVariables.put("printf", printf);
        interpreter.globalVariables.put("format", new InterpreterDataType("%s %s %s %s"));
        interpreter.globalVariables.put("args1", new InterpreterDataType("World"));
        interpreter.globalVariables.put("args2", new InterpreterDataType("Hello"));
        interpreter.globalVariables.put("args3", new InterpreterDataType("BYE"));
        interpreter.globalVariables.put("args4", new InterpreterDataType("Hi"));
        String result = ((BuiltInFunctionDefinitionNode) interpreter.functionVariables.get("printf")).Execute.apply(interpreter.globalVariables).toString();
        assertEquals("World Hello BYE Hi", result);

    }

    //Tests the built-in function sprintf
    @Test
    public void testSPrintf() throws IOException {
        ProgramNode programNode = new ProgramNode(new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
        Interpreter interpreter = new Interpreter(programNode, null);
        BuiltInFunctionDefinitionNode sprintf = new BuiltInSprintf();
        interpreter.functionVariables.put("sprintf", sprintf);
        interpreter.globalVariables.put("format", new InterpreterDataType("pi = %s approx,"));
        interpreter.globalVariables.put("expression1", new InterpreterDataType("3.14159"));

        String result = ((BuiltInFunctionDefinitionNode) interpreter.functionVariables.get("sprintf")).Execute.apply(interpreter.globalVariables).toString();
        assertEquals("pi = 3.14159 approx,", result);
        System.out.println(result);

    }

    // Tests the built-in function gsub
    @Test
    public void testGsub() throws IOException {
        ProgramNode programNode = new ProgramNode(new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
        Interpreter interpreter = new Interpreter(programNode, null);
        HashMap<String, InterpreterDataType> params = new HashMap<>();
        params.put("target", new InterpreterDataType("apple sucks live apple love apple"));
        params.put("regexp", new InterpreterDataType("apple"));
        params.put("replacement", new InterpreterDataType("foo"));
        String result = ((BuiltInFunctionDefinitionNode) interpreter.functionVariables.get("gsub")).Execute.apply(params).toString();
        assertEquals("foo sucks live foo love foo", result);
        System.out.println(result);

    }

    // Tests the built-in function sub
    @Test
    public void testSub() throws IOException {
        ProgramNode programNode = new ProgramNode(new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
        Interpreter interpreter = new Interpreter(programNode, null);
        HashMap<String, InterpreterDataType> params = new HashMap<>();
        params.put("target", new InterpreterDataType("apple.apple"));
        params.put("regexp", new InterpreterDataType("apple"));
        params.put("replacement", new InterpreterDataType("orange"));
        String result = ((BuiltInFunctionDefinitionNode) interpreter.functionVariables.get("sub")).Execute.apply(params).toString();
        assertEquals("orange.apple", result);
        System.out.println(result);


    }

    // Tests the built-in function substr
    @Test
    public void testSubstr() throws IOException {
        ProgramNode programNode = new ProgramNode(new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
        Interpreter interpreter = new Interpreter(programNode, null);
        HashMap<String, InterpreterDataType> params = new HashMap<>();
        params.put("string", new InterpreterDataType("Washington"));
        params.put("start", new InterpreterDataType("5"));
        params.put("length", new InterpreterDataType(""));
        String result = ((BuiltInFunctionDefinitionNode) interpreter.functionVariables.get("substr")).Execute.apply(params).toString();
        assertEquals("ington", result);
        System.out.println(result);
    }

    // Tests the built-in function split
    @Test
    public void testSplit() throws IOException {
        ProgramNode programNode = new ProgramNode(new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
        Interpreter interpreter = new Interpreter(programNode, null);
        HashMap<String, InterpreterDataType> params = new HashMap<>();
        params.put("string", new InterpreterDataType("abc-de-fgh"));
        params.put("array", new InterpreterDataType("arry"));
        params.put("fieldsep", new InterpreterDataType("-"));
        params.put("seps", new InterpreterDataType("sep"));
        String result = ((BuiltInFunctionDefinitionNode) interpreter.functionVariables.get("split")).Execute.apply(params).toString();
        assertEquals("3", result);
/*
        for(int i = 1; i < 4; i++)
        {
            InterpreterDataType data = params.get("arry" + i);
            String value = data.getValue();
            System.out.print(value);


        }
        System.out.println();
        for(int i = 1; i < 3; i++)
        {
            InterpreterDataType data2 = params.get("sep" + i + "]");
            String value2 = data2.getValue();
            System.out.println(value2);
        }
*/
    }

    // Tests the built-in function tolower
    @Test
    public void testToLower() throws IOException {
        ProgramNode programNode = new ProgramNode(new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
        Interpreter interpreter = new Interpreter(programNode, null);
        HashMap<String, InterpreterDataType> params = new HashMap<>();
        params.put("string", new InterpreterDataType("Hello, World!"));
        String result = ((BuiltInFunctionDefinitionNode) interpreter.functionVariables.get("tolower")).Execute.apply(params).toString();
        assertEquals("hello, world!", result);
        System.out.println(result);
    }

    // Tests the built-in function toupper
    @Test
    public void testToUpper() throws IOException {
        ProgramNode programNode = new ProgramNode(new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
        Interpreter interpreter = new Interpreter(programNode, null);
        HashMap<String, InterpreterDataType> params = new HashMap<>();
        params.put("string", new InterpreterDataType("Hello, World!"));
        String result = ((BuiltInFunctionDefinitionNode) interpreter.functionVariables.get("toupper")).Execute.apply(params).toString();
        assertEquals("Expected toupper to return 'HELLO, WORLD!'", "HELLO, WORLD!", result);
        System.out.println(result);
    }

    // Tests the built-in function Length
    @Test
    public void testLength() throws IOException {
        ProgramNode programNode = new ProgramNode(new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>());

        Interpreter interpreter = new Interpreter(programNode, null);

        HashMap<String, InterpreterDataType> params = new HashMap<>();

        params.put("string", new InterpreterDataType("abcde"));

        String result = ((BuiltInFunctionDefinitionNode) interpreter.functionVariables.get("length")).Execute.apply(params).toString();

        assertEquals("5", result);

        System.out.println(result);
    }

    // Tests the built-in function Index
    @Test
    public void testIndex() throws IOException {
        ProgramNode programNode = new ProgramNode(new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
        Interpreter interpreter = new Interpreter(programNode, null);
        HashMap<String, InterpreterDataType> params = new HashMap<>();
        params.put("in", new InterpreterDataType("peanut"));
        params.put("find", new InterpreterDataType("an"));
        String result = ((BuiltInFunctionDefinitionNode) interpreter.functionVariables.get("index")).Execute.apply(params).toString();
        assertEquals("3", result);
        System.out.println(result);
    }

    // Tests the built in function match
    @Test
    public void testMatch() throws IOException {
        ProgramNode programNode = new ProgramNode(new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
        Interpreter interpreter = new Interpreter(programNode, null);
        HashMap<String, InterpreterDataType> params = new HashMap<>();
        params.put("string", new InterpreterDataType("This is a unit test "));
        params.put("regexp", new InterpreterDataType("test"));
        params.put("arr", new InterpreterDataType("arry"));
        String result = ((BuiltInFunctionDefinitionNode) interpreter.functionVariables.get("match")).Execute.apply(params).toString();
        assertEquals("test", result);
        System.out.println(result);

        //params.put("input", new InterpreterDataType("arry[1]"));
        //   String result2 = ((BuiltInFunctionDefinitionNode) interpreter.functionVariables.get("print")).Execute.apply(params).getValue();


    }

    //Tests the built-in function getline
    @Test
    public void testGetLine() throws IOException {
        ProgramNode programNode = new ProgramNode(new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
        Interpreter interpreter = new Interpreter(programNode, null);
        interpreter.addLine("now this is valid");
        HashMap<String, InterpreterDataType> params = new HashMap<>();
        String result = ((BuiltInFunctionDefinitionNode) interpreter.functionVariables.get("getline")).Execute.apply(params).toString();
        assertEquals("1", result);
        System.out.println(result);
    }

    //Tests the built-in function next
    @Test
    public void testNext() throws IOException {
        ProgramNode programNode = new ProgramNode(new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
        Interpreter interpreter = new Interpreter(programNode, null);
        interpreter.addLine("now this is valid");
        HashMap<String, InterpreterDataType> params = new HashMap<>();
        String result = ((BuiltInFunctionDefinitionNode) interpreter.functionVariables.get("next")).Execute.apply(params).toString();
        assertEquals("1", result);
        System.out.println(result);
    }

    @Test
    public void testAssignmentNode() throws Exception {
        ProgramNode programNode = new ProgramNode(new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
        Interpreter interpreter = new Interpreter(programNode, null);
        AssignmentNode assignmentNode = new AssignmentNode(
                new VariableReferenceNode("a", Optional.empty()), // Left side
                new ConstantNode("2") // Right sidee
        );
        InterpreterDataType result = interpreter.getIDT(assignmentNode, interpreter.globalVariables);
        assertEquals("2", result.getValue());
        interpreter.globalVariables.put("a", result);
        // Assert that the result is the same as the value of "a"
        assertEquals(result.getValue(), interpreter.globalVariables.get("a").getValue());
        System.out.println(result.getValue());
    }

    @Test
    public void testTernaryNodeTrue() throws IOException {
        // Create a TernaryNode with a condition that evaluates to "true"
        ProgramNode programNode = new ProgramNode(new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>());

        // Create your interpreter or use an existing instance
        Interpreter interpreter = new Interpreter(programNode, null);

        TernaryNode ternaryNode = new TernaryNode(
                new OperationNode(
                        new ConstantNode("1"),
                        OperationNode.MathOperation.EQ,
                        Optional.of(new ConstantNode("1"))
                ),
                new ConstantNode("true"),
                new ConstantNode("false")
        );
        // Call the method you want to test
        InterpreterDataType result = interpreter.getIDT(ternaryNode, interpreter.globalVariables);

        // Assert that the result should be the value from the consequent (true) case
        assertEquals("Expected true case value", "true", result.getValue());
        System.out.println(result.getValue());
    }

    @Test
    public void testVariableReference() throws IOException {
        ProgramNode programNode = new ProgramNode(new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
        Interpreter interpreter = new Interpreter(programNode, null);
        VariableReferenceNode variableReferenceNode = new VariableReferenceNode("a", Optional.empty());
        interpreter.globalVariables.put("a", new InterpreterDataType("2"));
        InterpreterDataType result = interpreter.getIDT(variableReferenceNode, new HashMap<>());
        assertEquals("2", result.getValue());
        System.out.println(result.getValue());

    }
    @Test
    public void testIn() throws Exception{
        ProgramNode programNode = new ProgramNode(new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
        Interpreter interpreter = new Interpreter(programNode,null);
        HashMap<String, InterpreterDataType> arryData = new HashMap<String, InterpreterDataType>();
        char a = 'a';
        for(int i = 0; i < 26; i++)
        {
            arryData.put(String.valueOf(i), new InterpreterDataType(Character.toString(a)));
            a++;
        }

InterpreterArrayDataType arry = new InterpreterArrayDataType(arryData);
        interpreter.globalVariables.put("arry", arry);
        OperationNode operationNode = new OperationNode(
                new ConstantNode("1"),
                OperationNode.MathOperation.IN,
                Optional.of(new VariableReferenceNode("arry", Optional.empty()))
        );
        InterpreterDataType result = interpreter.getIDT(operationNode, new HashMap<>());
        assertEquals("true", result.getValue());
        System.out.println(result.getValue());
    }
    @Test
    public void testAdd() throws Exception{
        ProgramNode programNode = new ProgramNode(new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
        Interpreter interpreter = new Interpreter(programNode,null);
        OperationNode operationNode = new OperationNode(
                new ConstantNode("1"),
                OperationNode.MathOperation.ADD,
                Optional.of(new ConstantNode("2"))
        );
        AssignmentNode assignmentNode = new AssignmentNode(
                new VariableReferenceNode("a", Optional.empty()), // Left side
                operationNode // Right sidee
        );
        InterpreterDataType result = interpreter.getIDT(assignmentNode, interpreter.globalVariables);
        assertEquals("3.0", result.getValue());
        System.out.println(result.getValue());
    }
    @Test
    public void testMod() throws Exception{
        ProgramNode programNode = new ProgramNode(new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
        Interpreter interpreter = new Interpreter(programNode,null);
        OperationNode operationNode = new OperationNode(
                new ConstantNode("5"),
                OperationNode.MathOperation.MODULO,
                Optional.of(new ConstantNode("2"))
        );
        InterpreterDataType result = interpreter.getIDT(operationNode, interpreter.globalVariables);
        assertEquals("1.0", result.getValue());
        System.out.println(result.getValue());
    }
    // Tests matchNode
    @Test
    public void testMatchNode() throws Exception {
        ProgramNode programNode = new ProgramNode(new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
        Interpreter interpreter = new Interpreter(programNode, null);
        OperationNode operationNode = new OperationNode(
                new ConstantNode("~bye~"),
                OperationNode.MathOperation.MATCH,
                Optional.of(new PatternNode("~bye~"))
        );
        InterpreterDataType result = interpreter.getIDT(operationNode, interpreter.globalVariables);
        assertEquals("true", result.getValue());
        System.out.println(result.getValue());

    }
    // Tests Dollar
    @Test
    public void testDollar() throws Exception{
        ProgramNode programNode = new ProgramNode(new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
        Interpreter interpreter = new Interpreter(programNode,null);
        OperationNode leftSide = new OperationNode(
                new ConstantNode("1"),
                OperationNode.MathOperation.ADD,
                Optional.of(new ConstantNode("1"))
        );
        OperationNode operationNode = new OperationNode(
                leftSide,
                OperationNode.MathOperation.DOLLAR,
                Optional.empty()
        );
        InterpreterDataType result = interpreter.getIDT(operationNode, interpreter.globalVariables);
        assertEquals("$2.0", result.getValue());
        System.out.println(result.getValue());
    }
    // Tests Concatenation
    @Test
    public void testConcatenation() throws Exception{
        ProgramNode programNode = new ProgramNode(new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
        Interpreter interpreter = new Interpreter(programNode, null);

        OperationNode operationNode = new OperationNode(
                new ConstantNode("Hello"),
                OperationNode.MathOperation.CONCATENATION,
                Optional.of(new ConstantNode(" World"))
        );

        InterpreterDataType result = interpreter.getIDT(operationNode, interpreter.globalVariables);

        assertEquals("Hello World", result.getValue());
        System.out.println(result.getValue());
    }
    // Tests Less Than
    @Test
    public void testLessThan() throws IOException{
        ProgramNode programNode = new ProgramNode(new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
        Interpreter interpreter = new Interpreter(programNode,null);
        OperationNode operationNode = new OperationNode(
                new ConstantNode("1"),
                OperationNode.MathOperation.LT,
                Optional.of(new ConstantNode("2"))
        );
        InterpreterDataType result = interpreter.getIDT(operationNode, interpreter.globalVariables);
        assertEquals("true", result.getValue());
        System.out.println(result.getValue());
    }
    // Tests And
    @Test
    public void testAnd() throws IOException{
        ProgramNode programNode = new ProgramNode(new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
        Interpreter interpreter = new Interpreter(programNode,null);
        OperationNode operationNode = new OperationNode(
                new ConstantNode("1"),
                OperationNode.MathOperation.AND,
                Optional.of(new ConstantNode("2"))
        );
        InterpreterDataType result = interpreter.getIDT(operationNode, interpreter.globalVariables);
        assertEquals("true", result.getValue());
        System.out.println(result.getValue());
    }
    // Tests PreIncrement
    @Test
    public void testPreIncrement() throws Exception {
        ProgramNode programNode = new ProgramNode(new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
        Interpreter interpreter = new Interpreter(programNode, null);

        OperationNode operationNode = new OperationNode(
                new ConstantNode("2"),
                OperationNode.MathOperation.PREINC,
                Optional.empty()
        );

        InterpreterDataType result = interpreter.getIDT(operationNode, interpreter.globalVariables);


        assertEquals("3.0", result.getValue());
    }
    // Tests PreIncrement with a variable
    @Test
    public void testPreIncrementConst() throws IOException{
        ProgramNode programNode = new ProgramNode(new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
        Interpreter interpreter = new Interpreter(programNode, null);
        interpreter.globalVariables.put("x", new InterpreterDataType("1"));


        OperationNode operationNode = new OperationNode(
                new VariableReferenceNode("x", Optional.empty()),
                OperationNode.MathOperation.PREINC,
                Optional.empty()
        );


        InterpreterDataType result = interpreter.getIDT(operationNode, interpreter.globalVariables);

        assertEquals("2.0", result.getValue());
        System.out.println(result.getValue());
    }
}