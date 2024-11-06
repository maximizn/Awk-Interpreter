import java.util.HashMap;

public class InterpreterArrayDataType extends InterpreterDataType {
    private HashMap<String, InterpreterDataType> arryData;

    public InterpreterArrayDataType(HashMap<String, InterpreterDataType> arryData) {
        //super();
        this.arryData = arryData;
    }

    // Getter method to retrieve  array data
    public HashMap<String, InterpreterDataType> getArryData() {
        return arryData;
    }

    // Setter method to set array data
    public void setArryData(HashMap<String, InterpreterDataType> arryData) {
        this.arryData = arryData;
    }

    // Method to get an element from array by key
    public InterpreterDataType getArrayElement(String key) {
        return arryData.get(key);
    }

    // Method to set an element in  array with a key and value
    public void setArrayElement(String key, InterpreterDataType value) {
        arryData.put(key, value);
    }
    public String toString()
    {
        return this.arryData.toString();
    }
}
