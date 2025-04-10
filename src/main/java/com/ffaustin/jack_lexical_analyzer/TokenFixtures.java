package com.ffaustin.jack_lexical_analyzer;

public class TokenFixtures {

    public static Token keyword(String value){
        return new Token(Token.Type.KEYWORD, value);
    }

    public static Token symbol(String value){
        return new Token(Token.Type.SYMBOL, value);
    }

    public static Token identifier(String value){
        return new Token(Token.Type.IDENTIFIER, value);
    }

    public static Token intConst(String value){
        return new Token(Token.Type.INT_CONST, value);
    }

    public static Token stringConst(String value){
        return new Token(Token.Type.STRING_CONST, value);
    }

    public static Token invalid(String value){
        return new Token(Token.Type.INVALID, value);
    }
}
