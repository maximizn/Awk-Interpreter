import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;

/**
 * The Awk class is the main class that runs the program and reads the AwkFile
 */
public class Awk {


    public static void main(String[] args) {
        try {
            Path code = Path.of("/Users/mdot/IntelliJProjects/Lexer-Parser-Interpreter/src/AwkFile");
            Path dollarInput = Path.of("/Users/mdot/IntelliJProjects/Lexer-Parser-Interpreter/src/InputTest.txt");
            String file = new String(Files.readAllBytes(code));
            Lexer lexer = new Lexer(file);
            LinkedList<Token> tokens = lexer.Lex();
            TokenManager tokenManager = new TokenManager(tokens);
            Parser parser = new Parser(tokenManager);
            ProgramNode programNode = parser.Parse();
            //"/Users/mdot/IntelliJProjects/Lexer-Parser-Interpreter/src/AwkFile"
            Interpreter interp = new Interpreter(programNode, "/Users/mdot/IntelliJProjects/Lexer-Parser-Interpreter/src/InputTest.txt");
            interp.InterpretProgram(programNode);
        } catch (IOException e) {
            System.out.println("File wasn't found");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
