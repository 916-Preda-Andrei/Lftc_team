package java;

import com.company.Parser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.util.List;

public class ParserTest {

    private static Parser parser;

    @BeforeAll
    static void setUp() throws IOException {
        parser = new Parser();
    }

    @Test
    void testFirstSet() {
        System.out.println("First set: " + parser.getFirstSet());

        assert(parser.getFirstSet().containsKey("S"));
        assert(parser.getFirstSet().containsKey("A"));
        List<String> aList = List.of("b","c");
        assert (parser.getFirstSet().get("A").containsAll(aList));
        List<String> sList = List.of("a");
        assert (parser.getFirstSet().get("S").containsAll(sList));

        System.out.println("First set tests passed!");
    }

    @Test
    void testFollowSet() {
        System.out.println("Follow set: " + parser.getFollowSet());

        assert(parser.getFollowSet().containsKey("S"));
        assert(parser.getFollowSet().containsKey("A"));
        List<String> aList = List.of("$");
        assert (parser.getFollowSet().get("A").containsAll(aList));
        List<String> sList = List.of("$");
        assert (parser.getFollowSet().get("S").containsAll(sList));

        System.out.println("Follow set tests passed!");
    }
}
