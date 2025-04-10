package com.ffaustin.jack_lexical_analyzer;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.ffaustin.jack_lexical_analyzer.TokenFixtures.*;
import static org.junit.jupiter.api.Assertions.*;

public class JackTokenizerTest {

    @Test
    void shouldReturnCorrectTokenSequence() {
        String code = "class Main {return;}";
        List<Token> tokens = new JackTokenizer().tokenize(code);

        List<Token> expected = List.of(
                keyword("class"),
                identifier("Main"),
                symbol("{"),
                keyword("return"),
                symbol(";"),
                symbol("}")
        );

        assertEquals(expected, tokens, "Expected correct token sequence for simple class declaration");
    }

    @Test
    void tokenize_variableAssignmentWithInteger_returnsKeywordIdentifierSymbolIntSymbol() {
        String code = "let x = 123;";
        List<Token> tokens = new JackTokenizer().tokenize(code);

        List<Token> expected = List.of(
                keyword("let"),
                identifier("x"),
                symbol("="),
                intConst("123"),
                symbol(";")
        );

        assertEquals(expected, tokens, "Expected correct tokens for 'let x = 123;'");
    }

    @Test
    void tokenize_stringLiteralInsideFunctionCall_recognizesStringConstant() {
        String code = "do Output.printString(\"Hello, World\");";
        List<Token> tokens = new JackTokenizer().tokenize(code);

        assertTrue(
                tokens.contains(stringConst("Hello, World")),
                "Expected a STRING_CONST token with value 'Hello, World'"
        );
    }

    @Test
    void tokenize_ignoresSingleAndMultiLineComments_preservesCodeTokens() {
        String code = """
            // comment
            let x = 5; /* another comment */
            """;

        List<Token> tokens = new JackTokenizer().tokenize(code);

        List<Token> expected = List.of(
                keyword("let"),
                identifier("x"),
                symbol("="),
                intConst("5"),
                symbol(";")
        );

        assertEquals(expected, tokens, "Expected correct tokens after stripping comments");
    }

    @Test
    void tokenize_stringWithEscapedSymbols_outputsEscapedXMLValues() {
        String code = "let s = \"x < y & y > z\";";
        List<Token> tokens = new JackTokenizer().tokenize(code);

        Token stringToken = tokens.stream()
                .filter(t -> t.type() == Token.Type.STRING_CONST)
                .findFirst()
                .orElseThrow(() -> new AssertionError("No STRING_CONST token found"));

        assertEquals("x < y & y > z", stringToken.value(), "Raw string value should match unescaped input");

        String expectedXML = "\t<stringConstant> x &lt; y &amp; y &gt; z </stringConstant>\n";
        assertEquals(expectedXML, stringToken.toXML(), "XML should escape '<', '&', and '>' correctly");
    }

    @Test
    void tokenize_invalidCharacters_marksThemAsInvalidTokens() {
        String code = "let x = $oops;";
        List<Token> tokens = new JackTokenizer().tokenize(code);

        assertTrue(tokens.stream().anyMatch(t -> t.type() == Token.Type.INVALID),
                "Tokenizer should detect invalid characters like '$'");
    }

    @Test
    void tokenize_emptyInput_returnsEmptyTokenList() {
        String code = "";
        List<Token> tokens = new JackTokenizer().tokenize(code);

        assertTrue(tokens.isEmpty(), "Tokenizer should return empty list for empty input");
    }

    @Test
    void tokenize_multipleStringLiterals_recognizesEachIndividually() {
        String code = "do Output.printString(\"Hello\"); do Output.printString(\"World\");";
        List<Token> tokens = new JackTokenizer().tokenize(code);

        long stringCount = tokens.stream()
                .filter(t -> t.type() == Token.Type.STRING_CONST)
                .count();

        assertEquals(2, stringCount, "Tokenizer should detect two separate string constants");
    }

    @Test
    void tokenize_unterminatedString_detectsInvalidToken() {
        String code = "let s = \"This is broken;";
        List<Token> tokens = new JackTokenizer().tokenize(code);

        Token lastToken = tokens.getLast();

        boolean isInvalid = lastToken.type() == Token.Type.INVALID;
        boolean isMalformedString = lastToken.type() == Token.Type.STRING_CONST &&
                !lastToken.value().endsWith("\"");

        assertTrue(isInvalid || isMalformedString,
                "Unterminated strings should not be classified as valid STRING_CONST tokens");
    }

    @Test
    void tokenize_keywordsInsideIdentifiers_recognizesOnlyExactKeywords() {
        String code = "classroom methodical voided";
        List<Token> tokens = new JackTokenizer().tokenize(code);

        List<Token> expected = List.of(
                identifier("classroom"),
                identifier("methodical"),
                identifier("voided")
        );

        assertEquals(expected, tokens, "Only exact keyword matches should be classified as KEYWORD");
    }

    @Test
    void tokenize_mixedValidAndInvalidTokens_detectsAllProperly() {
        String code = "let x = 5; $invalid ^caret";
        List<Token> tokens = new JackTokenizer().tokenize(code);

        boolean containsInvalid = tokens.stream()
                .anyMatch(t -> t.type() == Token.Type.INVALID);

        assertTrue(containsInvalid, "Tokenizer should detect invalid characters like '$' and '^'");
    }
}
