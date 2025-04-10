package com.ffaustin.jack_lexical_analyzer;

public class Token {

    public enum Type{
        KEYWORD, SYMBOL, IDENTIFIER, INT_CONST, STRING_CONST, INVALID
    }

    private final Type type;
    private final String value;

    public Token(Type type, String value){
        this.type = type;
        this.value = value;
    }

    public Type getType(){
        return type;
    }

    public String getValue(){
        return value;
    }

    public String escapeXML(String input){
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

    public String toXML(){
        String escapedValue = escapeXML(value);

        switch(type){
            case KEYWORD: return "\t<keyword> " + escapedValue + " </keyword>\n";
            case SYMBOL: return "\t<symbol> " + escapedValue + " </symbol>\n";
            case IDENTIFIER: return "\t<identifier> " + escapedValue + " </identifier>\n";
            case INT_CONST: return "\t<integerConstant> " + escapedValue + " </integerConstant>\n";
            case STRING_CONST: return "\t<stringConstant> " + escapedValue + " </stringConstant>\n";
            default: return "\t<invalid> " + escapedValue + " </invalid>\n";
        }
    }
}
