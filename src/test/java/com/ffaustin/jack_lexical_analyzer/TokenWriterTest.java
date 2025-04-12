package com.ffaustin.jack_lexical_analyzer;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TokenWriterTest {
    @Test
    void writtenXmlShouldMatchExpectedSolutionFile() throws IOException{
        //Define input/output/solution paths
        Path inputPath = Path.of("resources/test/Main.jack");
        Path expectedXmlPath = Path.of("resources/test/MainT.xml");
        Path generatedXmlPath = Path.of("resources/test/MainT.generated.xml");

        //tokenize
        String jackCode = Files.readString(inputPath);
        List<Token> tokens = JackTokenizer.tokenize(jackCode);

        //write actual output
        TokenWriter.writeTokensToFile(tokens, generatedXmlPath);

        //compare expected vs generated
        List<String> expectedLines = Files.readAllLines(expectedXmlPath);
        List<String> actualLines = Files.readAllLines(generatedXmlPath);

        assertEquals(expectedLines, actualLines, "Generated XML does not match expected XML output.");

        Files.deleteIfExists(generatedXmlPath);
    }
}
