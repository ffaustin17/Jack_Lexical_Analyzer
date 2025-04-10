package com.ffaustin.jack_lexical_analyzer;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for simple App.
 */
public class JackTokenizerTest {

    @Test
    void shouldReturnCorrectTokenSequence() {
        String code = "class Main {return;}";

        JackTokenizer tokenizer = new JackTokenizer();
        List<Token> tokens = tokenizer.tokenize(code);

        assertEquals(6, tokens.size(), "Expect 6 tokens for 'class' 'Main' '{' 'return' ';' '}'");

        // Token 0: 'class' keyword
        assertEquals("class", tokens.get(0).getValue(), "Token 0 should be 'class'");
        assertEquals(Token.Type.KEYWORD, tokens.get(0).getType(), "Token 0 type should be KEYWORD");

        // Token 1: 'Main' identifier
        assertEquals("Main", tokens.get(1).getValue(), "Token 1 should be 'Main'");
        assertEquals(Token.Type.IDENTIFIER, tokens.get(1).getType(), "Token 1 type should be IDENTIFIER");

        // Token 2: '{' symbol
        assertEquals("{", tokens.get(2).getValue(), "Token 2 should be '{'");
        assertEquals(Token.Type.SYMBOL, tokens.get(2).getType(), "Token 2 type should be SYMBOL");

        // Token 3: 'return' keyword
        assertEquals("return", tokens.get(3).getValue(), "Token 3 should be 'return'");
        assertEquals(Token.Type.KEYWORD, tokens.get(3).getType(), "Token 3 type should be KEYWORD");

        // Token 4: ';' symbol
        assertEquals(";", tokens.get(4).getValue(), "Token 4 should be ';'");
        assertEquals(Token.Type.SYMBOL, tokens.get(4).getType(), "Token 4 type should be SYMBOL");

        // Token 5: '}' symbol
        assertEquals("}", tokens.get(5).getValue(), "Token 5 should be '}'");
        assertEquals(Token.Type.SYMBOL, tokens.get(5).getType(), "Token 5 type should be SYMBOL");
    }

    @Test
    void tokenize_variableAssignmentWithInteger_returnsKeywordIdentifierSymbolIntSymbol() {
        String code = "let x = 123;";
        List<Token> tokens = new JackTokenizer().tokenize(code);

        assertEquals(5, tokens.size(), "Expected 5 tokens for 'let x = 123;'");

        assertEquals("let", tokens.get(0).getValue(), "Token 0 should be 'let'");
        assertEquals(Token.Type.KEYWORD, tokens.get(0).getType(), "Token 0 type should be KEYWORD");

        assertEquals("x", tokens.get(1).getValue(), "Token 1 should be 'x'");
        assertEquals(Token.Type.IDENTIFIER, tokens.get(1).getType(), "Token 1 type should be IDENTIFIER");

        assertEquals("=", tokens.get(2).getValue(), "Token 2 should be '='");
        assertEquals(Token.Type.SYMBOL, tokens.get(2).getType(), "Token 2 type should be SYMBOL");

        assertEquals("123", tokens.get(3).getValue(), "Token 3 should be '123'");
        assertEquals(Token.Type.INT_CONST, tokens.get(3).getType(), "Token 3 type should be INT_CONST");

        assertEquals(";", tokens.get(4).getValue(), "Token 4 should be ';'");
        assertEquals(Token.Type.SYMBOL, tokens.get(4).getType(), "Token 4 type should be SYMBOL");
    }

    @Test
    void tokenize_stringLiteralInsideFunctionCall_recognizesStringConstant() {
        String code = "do Output.printString(\"Hello, World\");";
        List<Token> tokens = new JackTokenizer().tokenize(code);

        assertTrue(
                tokens.stream().anyMatch(
                        t -> t.getType() == Token.Type.STRING_CONST && t.getValue().equals("Hello, World")
                ),
                "Expected a STRING_CONST token with value 'Hello, World'"
        );
    }

    @Test
    void tokenize_ignoresSingleAndMultiLineComments_preservesCodeTokens() {
        String code = """
        // this is a comment
        let x = 5; /* another comment */
        """;

        List<Token> tokens = new JackTokenizer().tokenize(code);

        assertEquals(5, tokens.size(), "Expected 5 tokens after removing comments");

        assertEquals("let", tokens.get(0).getValue(), "Token 0 should be 'let'");
        assertEquals(Token.Type.KEYWORD, tokens.get(0).getType(), "Token 0 type should be KEYWORD");

        assertEquals("x", tokens.get(1).getValue(), "Token 1 should be 'x'");
        assertEquals(Token.Type.IDENTIFIER, tokens.get(1).getType(), "Token 1 type should be IDENTIFIER");

        assertEquals("=", tokens.get(2).getValue(), "Token 2 should be '='");
        assertEquals(Token.Type.SYMBOL, tokens.get(2).getType(), "Token 2 type should be SYMBOL");

        assertEquals("5", tokens.get(3).getValue(), "Token 3 should be '5'");
        assertEquals(Token.Type.INT_CONST, tokens.get(3).getType(), "Token 3 type should be INT_CONST");

        assertEquals(";", tokens.get(4).getValue(), "Token 4 should be ';'");
        assertEquals(Token.Type.SYMBOL, tokens.get(4).getType(), "Token 4 type should be SYMBOL");
    }

    @Test
    void tokenize_stringWithEscapedSymbols_outputsEscapedXMLValues() {
        String code = "let s = \"x < y & y > z\";";
        List<Token> tokens = new JackTokenizer().tokenize(code);

        Token stringToken = tokens.stream()
                .filter(t -> t.getType() == Token.Type.STRING_CONST)
                .findFirst()
                .orElseThrow(() -> new AssertionError("No STRING_CONST token found"));

        assertEquals("x < y & y > z", stringToken.getValue(), "Raw string value should match unescaped input");

        String expectedXML = "\t<stringConstant> x &lt; y &amp; y &gt; z </stringConstant>\n";
        assertEquals(expectedXML, stringToken.toXML(), "String constant should be properly escaped in XML output");
    }

    @Test
    void tokenize_invalidCharacters_marksThemAsInvalidTokens() {
        String code = "let x = $oops;";
        List<Token> tokens = new JackTokenizer().tokenize(code);

        boolean hasInvalid = tokens.stream()
                .anyMatch(t -> t.getType() == Token.Type.INVALID);

        assertTrue(hasInvalid, "Tokenizer should detect and mark invalid characters like '$'");
    }

    @Test
    void tokenize_emptyInput_returnsEmptyTokenList() {
        String code = "";
        List<Token> tokens = new JackTokenizer().tokenize(code);
        assertTrue(tokens.isEmpty(), "Tokenizer should return an empty list for empty input");
    }

    @Test
    void tokenize_multipleStringLiterals_recognizesEachIndividually() {
        String code = "do Output.printString(\"Hello\"); do Output.printString(\"World\");";
        List<Token> tokens = new JackTokenizer().tokenize(code);

        long stringCount = tokens.stream()
                .filter(t -> t.getType() == Token.Type.STRING_CONST)
                .count();

        assertEquals(2, stringCount, "Tokenizer should detect two separate string constants");
    }

    @Test
    void tokenize_unterminatedString_detectsInvalidToken() {
        String code = "let s = \"This is broken;";
        List<Token> tokens = new JackTokenizer().tokenize(code);

        Token lastToken = tokens.getLast();
        assertTrue(
                lastToken.getType() == Token.Type.INVALID || lastToken.getType() == Token.Type.STRING_CONST && !lastToken.getValue().endsWith("\""),
                "Unterminated strings should not be classified as valid STRING_CONST tokens"
        );
    }

    @Test
    void tokenize_keywordsInsideIdentifiers_recognizesOnlyExactKeywords() {
        String code = "classroom methodical voided";
        List<Token> tokens = new JackTokenizer().tokenize(code);

        assertEquals(Token.Type.IDENTIFIER, tokens.get(0).getType(), "'classroom' should be an IDENTIFIER");
        assertEquals(Token.Type.IDENTIFIER, tokens.get(1).getType(), "'methodical' should be an IDENTIFIER");
        assertEquals(Token.Type.IDENTIFIER, tokens.get(2).getType(), "'voided' should be an IDENTIFIER");
    }

    @Test
    void tokenize_mixedValidAndInvalidTokens_detectsAllProperly() {
        String code = "let x = 5; $invalid ^caret";
        List<Token> tokens = new JackTokenizer().tokenize(code);

        boolean containsInvalid = tokens.stream()
                .anyMatch(t -> t.getType() == Token.Type.INVALID);

        assertTrue(containsInvalid, "Tokenizer should detect invalid characters like '$' and '^'");
    }
}
