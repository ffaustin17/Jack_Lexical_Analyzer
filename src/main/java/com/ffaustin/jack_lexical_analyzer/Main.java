package com.ffaustin.jack_lexical_analyzer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args){
        if(args.length != 1){
            System.out.println("Usage: java -jar jack-tokenizer.jar <path-to-file-or-directory>");
            return;
        }

        Path inputPath = Paths.get(args[0]);

        if(!Files.exists(inputPath)){
            System.out.println("Path not found: " + inputPath);
            return;
        }

        try{
            if(Files.isDirectory(inputPath)){
                //tokenize all .jack files in the directory
                Files.list(inputPath)
                        .filter(path -> path.toString().endsWith(".jack"))
                        .forEach(Main::processJackFile);
            }
            else if(inputPath.toString().endsWith(".jack")){
                processJackFile(inputPath);
            }
            else{
                System.out.println("Input must be a .jack file or a directory containing .jack files");
            }
        }
        catch(IOException e){
            System.err.println("Error reading files: " + e.getMessage());
        }
    }

    /**
     * Processes a single Jack file : tokenizes it and writes the XML output
     * @param jackFile path to the .jack file
     */
    private static void processJackFile(Path jackFile){
        System.out.println("Tokenizing: " + jackFile.getFileName());

        try{
            String source = Files.readString(jackFile);
            List<Token> tokens = new JackTokenizer().tokenize(source);

            Path outputPath = generateOutputPath(jackFile);
            TokenWriter.writeTokensToFile(tokens, outputPath);

            System.out.println(" -> Tokenized to : " + outputPath);
        }
        catch(IOException e){
            System.err.println("Failed to process  " + jackFile + ": " + e.getMessage());
        }
    }


    /**
     * Generates the corresponding output file path (XxxxT.xml).
     * @param inputFile path to the .jack input
     * @return corresponding .xml output path
     */
    private static Path generateOutputPath(Path inputFile){
        String filename = inputFile.getFileName().toString();
        String newName = filename.replace(".jack", "T.xml");
        return inputFile.resolveSibling(newName);
    }
}
