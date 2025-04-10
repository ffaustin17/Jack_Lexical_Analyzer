package com.ffaustin.jack_lexical_analyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The JackTokenizer class handles the lexical analysis (tokenization) of .jack source code.
 * It removes comments, splits the input into tokens, and classifies each as per Jack language rules.
 */
public class JackTokenizer
{
    /** Set of all Jack language keywords */
    private static final Set<String> KEYWORDS = Set.of(
            "class", "constructor", "function", "method", "field", "static", "var", "int", "char", "boolean",
            "void", "true", "false","null", "this", "let","do", "if", "else", "while", "return"
    );

    /** Set of all single-character symbols in Jack */
    private static  final Set<Character> SYMBOLS = Set.of(
            '{', '}', '(', ')', '[', ']', '.', ',', ';',
            '+', '-', '*', '/', '&', '|', '<', '>', '=', '~'
    );

    /**
     * Performs full tokenization : removes comments and lexes the code.
     * @param input the raw Jack source code
     * @return list of classified Token objects
     */
    public List<Token> tokenize(String input){
        String noComments = removeComments(input);
        return lex(noComments);
    }


    /**
     * Removes all single-line and multi-line comments from the input.
     * @param input source code with possible comments
     * @return cleaned code with no comments
     */
    public String removeComments(String input){
        return input.replaceAll("//.*", "") //single line comments
                .replaceAll("(?s)/\\*.*?\\*/", "") //multi-line comments
                .replaceAll("\r", ""); //normalize line endings
    }


    /**
     * Lexes the given input into a list of tokens.
     * Handles string literals, symbols, identifiers, keywords, numbers, etc.
     * @param input the cleaned source code (no comments)
     * @return list of classified tokens
     */
    public List<Token> lex(String input){
        List<Token> tokens = new ArrayList<>();

        StringBuilder current = new StringBuilder();
        boolean insideString = false;

        for(int i = 0; i < input.length(); i++){
            char c = input.charAt(i);

            if(c == '"'){
                if(insideString){
                    current.append(c);
                    tokens.add(new Token(Token.Type.STRING_CONST, current.toString().replaceAll("^\"|\"$", "")));
                    current.setLength(0);
                    insideString = false;
                }
                else{
                    if(!current.isEmpty()){
                        tokens.add(classify(current.toString()));
                        current.setLength(0);
                    }
                    current.append(c);
                    insideString = true;
                }
            }
            else if(insideString){
                current.append(c);
            }
            else if(Character.isWhitespace(c)){
                if(!current.isEmpty()){
                    tokens.add(classify(current.toString()));
                    current.setLength(0);
                }
            }
            else if(SYMBOLS.contains(c)){
                if(!current.isEmpty()){
                    tokens.add(classify(current.toString()));
                    current.setLength(0);
                }
                tokens.add(new Token(Token.Type.SYMBOL, String.valueOf(c)));
            }
            else{
                current.append(c);
            }
        }

        if(!current.isEmpty()){
            tokens.add(classify(current.toString()));
        }

        return tokens;
    }


    /**
     * Determines the correct token type based on the lexeme's content.
     * @param lexeme the raw string to classify
     * @return Token with appropriate type
     */
    private Token classify(String lexeme){
        if(KEYWORDS.contains(lexeme)){
            return new Token(Token.Type.KEYWORD, lexeme);
        }
        else if(lexeme.matches("\\d+")){
            return new Token(Token.Type.INT_CONST, lexeme);
        }
        else if(lexeme.matches("[a-zA-Z_][a-zA-Z0-9_]*")){
            return new Token(Token.Type.IDENTIFIER, lexeme);
        }
        else if(lexeme.startsWith("\"") && lexeme.endsWith("\"")){
            return new Token(Token.Type.STRING_CONST, lexeme.substring(1, lexeme.length() - 1));
        }
        else{
            return new Token(Token.Type.INVALID, lexeme);
        }
    }
}
