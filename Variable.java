/**
 * Variable
 */
public class Variable {

    String name;
    TokenType type;
    String typeName;
    Object contents;

    Variable(String name, TokenType type, Object contents) {
        setVariable(name, type, contents);
    }

    Variable(String name, String typeName, Object contents) {
        setVariable(name, typeName, contents);
    }

    public void setVariable(String name, TokenType type, Object contents) {
        this.name = name;
        this.type = type;
        this.typeName = null;
        this.contents = contents;
    }

    public void setVariable(String name, String typeName, Object contents) {
        this.name = name;
        this.type = null;
        this.typeName = typeName;
        this.contents = contents;
    }
}
