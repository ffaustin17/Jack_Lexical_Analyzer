# Jack_Lexical_Analyzer
This project implements a Lexical Analyzer (Tokenizer) for the Jack programming language (used in Nand2Tetris course). It processes .jack source files and outputs XML-formatted token streams.

## Features
- Supports Jack language constructs:keywords, symbols, identifiers, integer & string constants.
- Strips both single-line `//` and multiline `/* */` comments.
- Converts special XML characters (`<`, `>`, `&`) to safe equivalents (`&lt;`, `&gt;`, `&amp;`).
- Outputs tokenized content as `XxxT.xml` alongside input `Xxx.jack`.

## How It Works

1. Comment Stripping - Removes all comments while preserving source structure.
2. Lexical Tokenization - Breaks the code into lexemes, then classifies each as `<keyword>`, `<symbol>`, `<identifier>`, `<integerConstant>`, `<stringconstant>`, or `<invalid>`.
3. XML Generation - The resulting tokens are saved as a structured XML file enclosed within `<tokens>...<tokens>`.


