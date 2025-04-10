package com.ffaustin.jack_lexical_analyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Hello world!
 *
 */
public class JackTokenizer
{
    private static final Set<String> KEYWORDS = Set.of(
            "class", "constructor", "function", "method", "field", "static", "var", "int", "char", "boolean",
            "void", "true", "false","null", "this", "let","do", "if", "else", "while", "return"
    );

    private static  final Set<Character> SYMBOLS = Set.of(
            '{', '}', '(', ')', '[', ']', '.', ',', ';',
            '+', '-', '*', '/', '&', '|', '<', '>', '=', '~'
    );

    public List<Token> tokenize(String input){
        String noComments = removeComments(input);
        return lex(noComments);
    }

    public String removeComments(String input){
        return input.replaceAll("//.*", "") //single line comments
                .replaceAll("/\\*.*?\\*/", "") //multi-line comments
                .replaceAll("\r", ""); //normalize line endings
    }

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
