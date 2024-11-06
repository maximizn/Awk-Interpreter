class StringHandler {
    private String AwkFile;
    private int fingerIndex;

    /**
     * Constructor for StringHandler
     * The String Handler object using the AwkFile and sets the fingerIndex to 0
     * @param AwkFile
     */
    public StringHandler(String AwkFile) {
        this.AwkFile = AwkFile;
        fingerIndex = 0;
    }
    /**
     * Peek method for StringHandler peeks ahead i characters
     * @param i number of characters to peek ahead
     * @return the character at the index i, or null if the index is out of bounds
     */
    public char Peek(int i) {
        if (fingerIndex + i < AwkFile.length()) {
            return AwkFile.charAt(fingerIndex + i);
        } else {
            return '\0';
        }
    }

    /**
     * PeekString method for StringHandler peeks ahead i characters at a substring
     * @param i number of characters to peek ahead in the substring
     * @return the substring at the index i, or empty string if the index is out of bounds
     */

    public String PeekString(int i) {
        if (fingerIndex + i < AwkFile.length()) {
            return AwkFile.substring(fingerIndex, fingerIndex + i);
        } else {
            return " ";
        }
    }

    /**
     * GetChar method for StringHandler gets the character at its current position and increments the fingerIndex
     * @return the character at the current position or null if the index is out of bounds
     */
    public char GetChar() {
        if (fingerIndex < AwkFile.length()) {
            char c = AwkFile.charAt(fingerIndex);
            fingerIndex++;
            return c;
        }
        return '\0';
    }

    /**
     * Swallow method for StringHandler increments the fingerIndex by i
     * @param i the number of characters to increment the fingerIndex by
     */
    public void Swallow(int i) {
        if (fingerIndex + i < AwkFile.length()) {
            fingerIndex += i;
        }
    }

    /**
     * isDone method for StringHandler checks if the fingerIndex is at the end of the AwkFile
     * @return true if the fingerIndex is at the end of the AwkFile, or false if it is not
     */
    public boolean isDone() {
        return fingerIndex < AwkFile.length() ;


    }

    /**
     * Remainder method for StringHandler returns the substring of the AwkFile from the fingerIndex to the end of the AwkFile
     * @return the substring of the AwkFile from the fingerIndex to the end of the AwkFile
     */
    public String Remainder() {
        return AwkFile.substring(fingerIndex);
    }
}