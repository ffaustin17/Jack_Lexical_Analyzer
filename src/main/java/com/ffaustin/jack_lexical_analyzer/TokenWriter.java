package com.ffaustin.jack_lexical_analyzer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TokenWriter {
    public static void writeTokensToFile(List<Token>tokens, Path outputPath) throws IOException{
        try(BufferedWriter writer = Files.newBufferedWriter(outputPath))
        {
            writer.write("<tokens>\n");
            for(Token token : tokens){
                writer.write(token.toXML());
            }
            writer.write("</tokens>\n");

        }
    }
}
