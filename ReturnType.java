enum ExecutionResult {
    NORMAL,
    BREAK,
    CONTINUE,
    RETURN
}

public class ReturnType {
    private ExecutionResult resultType;
    private String returnValue;

    // Constructor with only the enum
    public ReturnType(ExecutionResult resultType) {
        this.resultType = resultType;
        this.returnValue = null;
    }

    // Constructor with the enum and a string
    public ReturnType(ExecutionResult resultType, String returnValue) {
        this.resultType = resultType;
        this.returnValue = returnValue;
    }
    public ExecutionResult getResultType() {
        return resultType;
    }
    public String getReturnValue() {
        return returnValue;
    }
    @Override
    public String toString() {
        if (returnValue == null) {
            return resultType.toString();
        } else {
            return resultType.toString() + " (" + returnValue + ")";
        }
    }
}
