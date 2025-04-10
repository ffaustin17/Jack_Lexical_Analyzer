# Jack Lexical Analyzer (Tokenizer)

A fully-functional **command-line tokenizer** for the Jack programming language, built as part of the [Nand2Tetris](https://www.nand2tetris.org/) compiler toolchain.

It takes `.jack` files, strips comments, tokenizes their contents, and outputs an XML file (`XxxT.xml`) that conforms to the course specification.

> Built in Java 21  
> Tested with JUnit  
> Runnable as a CLI tool  
> Self-contained, no dependencies beyond JDK and Maven

---

## Features

- **Tokenizes**:
    - Jack language **keywords**, **symbols**, **identifiers**
    - **Integer constants** and **string constants**
    - Invalid lexemes are flagged for safety
- **Strips all comments**:
    - `// single-line comments`
    - `/* multi-line comments */` — across multiple lines
- **Escapes XML characters**: `<`, `>`, `&` → `&lt;`, `&gt;`, `&amp;`
- **Comprehensive test coverage** (comment handling, edge cases, and more)
- Accepts **both single files and folders** of `.jack` source files

---

## How to Run (Command Line)

> Requires Java 21+

### Option 1: Use the Precompiled JAR

```bash
java -jar releases/jack-tokenizer.jar path/to/fileOrFolder
```
- Input can be a single `.jack` file or a directory containing multiple `.jack` files.
- Each file produces a corresponding XxxT.xml output in the same folder.
---

### Option 2: Build it Yourself (Maven)

Clone and build:
```bash
git clone https://github.com/ffaustin17/Jack_Lexical_Analyzer.git
cd .../Jack_Lexical_Analyzer
mvn clean package
```

Then run:
```bash
java -jar target/jack-tokenizer-1.0-SNAPSHOT.jar path/to/fileOrFolder
```

---

## Testing

Run all unit tests using Maven
```bash
mvn test
```
This includes:
- Lexical unit tests
- Comment-stripping verification
- String escape correctness
- XML output comparison


You can also test the actual application by using resources/main directory as the directory containing the`.jack` files.
You should see some nice output!

---

## Possible Future Enhancements
- Syntax validation or parser integration
- Web frontend with file upload
- Integration into full `Jack` -> `VM compiler`
- Comment position tracking

---

## Author
Thank you for making it this far! This project was made by me (Fabrice Faustin) as part of a deeper exploration into compiler
design and lexical analysis tools.


