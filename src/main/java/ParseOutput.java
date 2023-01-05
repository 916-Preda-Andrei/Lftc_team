import java.io.FileWriter;
import java.io.IOException;

public class ParseOutput {
    private final ParseTable parseTable;

    public ParseOutput(ParseTable parseTable){
        this.parseTable = parseTable;
    }

    public void printToFile(String filename) throws IOException {
        FileWriter myWriter = new FileWriter(filename);
        myWriter.write(parseTable.toString());
        myWriter.close();
    }
}
