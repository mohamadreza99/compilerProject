import java.util.LinkedHashMap;

public class STEntry {
    String name;
    Descriptor desc;

    public STEntry(String name, Descriptor desc) {
        this.name = name;
        this.desc = desc;
    }

}
class Descriptor{
    String type;
    int adrs;

    public Descriptor(String type) {
        this.type = type;
        this.adrs = Parser.ADRS;
        Parser.ADRS += Parser.getSize(type);
    }

    public Descriptor() {
    }
}

class ArrayDescriptor extends Descriptor {
    int size;
    String elementType;

    public ArrayDescriptor(String type,String elementType, int adrs, int size) {
        super(type);
        this.elementType = elementType;
        super.adrs = Parser.ADRS;
        this.size = size;
    }
    public ArrayDescriptor(){}
}

class FunctionDescriptor extends Descriptor {
    int startOfCode;
    LinkedHashMap<String, String> parameters = new LinkedHashMap<>();

    public FunctionDescriptor(int startOfCode) {
        super("function");
        this.startOfCode = startOfCode;
    }
}