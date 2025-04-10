package com.ffaustin.jack_lexical_analyzer;

public record Token(com.ffaustin.jack_lexical_analyzer.Token.Type type, String value) {

    public enum Type {
        KEYWORD, SYMBOL, IDENTIFIER, INT_CONST, STRING_CONST, INVALID
    }

    public String escapeXML(String input) {
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

    public String toXML() {
        String escapedValue = escapeXML(value);

        return switch (type) {
            case KEYWORD -> "\t<keyword> " + escapedValue + " </keyword>\n";
            case SYMBOL -> "\t<symbol> " + escapedValue + " </symbol>\n";
            case IDENTIFIER -> "\t<identifier> " + escapedValue + " </identifier>\n";
            case INT_CONST -> "\t<integerConstant> " + escapedValue + " </integerConstant>\n";
            case STRING_CONST -> "\t<stringConstant> " + escapedValue + " </stringConstant>\n";
            default -> "\t<invalid> " + escapedValue + " </invalid>\n";
        };
    }
}
