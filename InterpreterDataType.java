public class InterpreterDataType {
    private String value;

    // InterpreterDataType constructor without value
    public InterpreterDataType() {
        this.value = "";
    }
    // InterpreterDataType constructor with value
    public InterpreterDataType(String value) {
        this.value = value;
    }

    // Getter method to retrieve the value
    public String getValue() {
        return value;
    }

    // Setter method to set value
    public void setValue(String value) {
        this.value = value;
    }
    public String toString()
    {
        return this.value;
    }
}
