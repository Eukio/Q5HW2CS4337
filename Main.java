
/*
Alishba Jafri - ARJ220005
Eucharist Tan - ETT220002
CS 4337.007
Professor Karami
3/23/2025

 * 
 TO DO: FIGURE OUT HOW TO PARSE STRING LITERALS "" CURRENTLY RETURNS EOF AND CAN CAUSE INFINITE LOOP
 Alishba- modifying getChar()- when we detect " call a function to process full string
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    enum Char {
        LETTER,
        DIGIT,
        UNKNOWN,
        EOF
    }

    enum Token { // Somewhat similar to ascii values
        IDENT,
        INT_LIT,
        FOR,
        IF,
        ELSE,
        INCR_OP,
        DECR_OP,
        HASH,
        INCLUDE_STR,
        INCLUDE_STDIO,
        PRINTF,
        FGETS,
        PUTS,
        STR_LIT,
        LEFT_PAREN,
        RIGHT_PAREN,
        MULT_OP,
        ADD_OP,
        SUB_OP,
        DIV_OP,
        LEFT_BRACKET,
        RIGHT_BRACKET,
        EQ_TO,
        LS_THAN,
        ASSIGN_OP,
        GR_THAN,
        GR_EQ_THAN,
        LS_EQ_THAN,
        COMMA,
        NEGATION,
        SEMICOLON,
        LEFT_CURLY,
        RIGHT_CURLY,
        QUOTATION,
        RETURN,
        VOID,
        TYPE,
        EOF
    }

    public static final String FILEPATH = "program.txt";
    public static Token nextToken;
    public static Char charClass;
    public static char nextChar;
    public static int lexLen = 0;
    public static char[] lexeme = new char[100];
    public static BufferedReader inputFile;
    Scanner sc;

    public static void main(String[] args) throws IOException {
        try {
            inputFile = new BufferedReader(new FileReader(FILEPATH)); // read in file
        } catch (FileNotFoundException e) {
            System.out.println("ERROR - cannot open " + FILEPATH); // print error if cannot open
            System.exit(1);
        }

        parseProgram(); // start parsing

        try {
            inputFile.close(); // close input file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Lookup function for operators and parentheses, return token
    public static Token lookup(char ch) {
        switch (ch) {
            case '(':
                addChar();
                nextToken = Token.LEFT_PAREN;
                break;
            case ')':
                addChar();
                nextToken = Token.RIGHT_PAREN;
                break;
            case '{':
                addChar();
                nextToken = Token.LEFT_CURLY;
                break;
            case '}':
                addChar();
                nextToken = Token.RIGHT_CURLY;
                break;
                case '[':
                addChar();
                nextToken = Token.LEFT_BRACKET;
                break;
                case ']':
                addChar();
                nextToken = Token.RIGHT_BRACKET;
                break;
                case ',':
                addChar();
                nextToken = Token.COMMA;
                break;
            case ';':
                addChar();
                nextToken = Token.SEMICOLON;
                break;
                case '"':
                addChar();
                nextToken = Token.QUOTATION;
                break;
            case '+':
                addChar();
                nextToken = Token.ADD_OP;
                break;
            case '-':
                addChar();
                nextToken = Token.SUB_OP;
                break;
            case '*':
                addChar();
                nextToken = Token.MULT_OP;
                break;
            case '/':
                addChar();
                nextToken = Token.DIV_OP;
                break;
            case '=':
                addChar();
                if (peek() == '=') { // Check for ==
                    getChar();
                    addChar();
                    nextToken = Token.EQ_TO;
                } else {
                    nextToken = Token.ASSIGN_OP;
                }
                break;
            case '<':
                addChar();
                if (peek() == '=') { // Check for <=
                    getChar();
                    addChar();
                    nextToken = Token.LS_EQ_THAN;
                } else {
                    nextToken = Token.LS_THAN;
                }
                break;
            case '>':
                addChar();
                if (peek() == '=') { // Check for >=
                    getChar();
                    addChar();
                    nextToken = Token.GR_EQ_THAN;
                } else {
                    nextToken = Token.GR_THAN;
                }
                break;
            default:
                addChar();
                nextToken = Token.EOF;
                break;
        }
        return nextToken;
    }

    public static char peek() {
        try {
            // Temporarily store the current position in the input file
            long currentPosition = inputFile.skip(0); // get current position (don't move forward)

            // Look at the next character
            char next = (char) inputFile.read();
            inputFile.skip(currentPosition); // return to the original position

            return next;
        } catch (IOException e) {
            e.printStackTrace();
            return (char) -1; // return EOF if something goes wrong
        }
    }

    public static void addChar() {
        if (lexLen <= 98) {
            lexeme[lexLen++] = nextChar;
            lexeme[lexLen] = 0;
        } else
            System.out.println("Error - lexeme is too long");
    }

    public static void getChar() {
        try {
            if ((nextChar = (char) inputFile.read()) != -1) {
                if (Character.isAlphabetic(nextChar) || nextChar == '#')
                    charClass = Char.LETTER;
                else if (Character.isDigit(nextChar))
                    charClass = Char.DIGIT;
              //  else if (nextChar == '\"') { // detects string literals
                    // addChar();
                    // nextToken = Token.LEFT_PAREN;
              //     lex();
                //    parseStringLiteral();
                    //return;
              //  }
                 else
                    charClass = Char.UNKNOWN;
            } else
                charClass = Char.EOF;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void parseStringLiteral() {
        lexLen = 0; // Reset lexeme length
        addChar(); // Add the first quote to lexeme

        try {
            while (true) {
                nextChar = (char) inputFile.read();
                if (nextChar == '\"') {
                    addChar(); // Add closing quote
                    break; // End of string
                } else if (nextChar == (char) -1) {
                    System.out.println("Error - Unclosed string literal");
                    nextToken = Token.EOF;
                    return;
                }
                addChar();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        nextToken = Token.STR_LIT; // Set token type for string literals
        System.out.println("Next token is: " + nextToken + ", Next lexeme is " + toStringLexeme());
     getChar(); 
    }

    public static void getNonBlank() {
        while (nextChar <= 32) { // handles spaces & newline
            getChar();
        }
    }

    public static void parseProgram() {
        System.out.println("Enter <program>");
        getChar();
        lex(); // Get first token

        while (nextToken != Token.EOF) {
             if (nextToken == Token.INCLUDE_STDIO || nextToken == Token.INCLUDE_STR) {
             lex(); // Process include statements 
             } 
             else if (nextToken == Token.VOID) {
                parseFunction(); // Process function declaration
                parseMainFunction();
                nextToken = Token.EOF;
            }
              else if (nextToken == Token.TYPE) {  
                parseDefineVar();
            } 
            else {
               lex();
                //error();
            }
        }
            
        System.out.println("Exit <program>");
        
    }

    // NEW- processing global variable
    public static void parseGlobalVariable() {
        System.out.println("Enter <global variable>");
      //  lex(); //variable name
        while(nextToken != Token.SEMICOLON)
        lex();
        lex();
       /*  if (nextToken == Token.IDENT) {
            lex(); // [
        } else {
            error(); // If no valid identifier, it's a syntax error
            return;
        }

        // Check if it's an array declaration (e.g., input[100])
        if (nextToken == Token.LEFT_BRACKET) {
            lex(); //literal

            // Ensure it's a number (e.g., '100')
            if (nextToken == Token.INT_LIT) {
                lex(); // ]
            } else {
                error(); // Expected array size
                return;
            }

            // Expect closing ']'
            if (nextToken == Token.RIGHT_BRACKET) {
                lex(); // ;
            } else {
                error(); // Expected closing bracket
                return;
            }
        }

        // // Expect ';' to end the statement
        // if (nextToken == Token.SEMICOLON) { 
        //     lex(); // Consume ';'
        // } else {
        //     error(); // Missing semicolon
        //     return;
        // }
*/
        System.out.println("Exit <global variable>");
    }

    public static void parseDefineVar() {
        System.out.println("Enter <defineVar>");
        getChar();
        lex();
        while(nextToken != Token.SEMICOLON)
        lex();

        //lex(); // Identifier
        //lex(); // =
        //lex(); // Expression
        //lex(); // ;
        System.out.println("Exit <defineVar>");
    }



    public static void addAndLoopToLexeme() {
        while (charClass == Char.LETTER || charClass == Char.DIGIT) {
            addCharToN(1);
        }
    }

    public static void addCharToN(int n) {
        for (int i = 0; i < n; i++) {
            addChar();
            getChar();
        }
    }

    public static void determineHeader() {

        addCharToN(1);
        if (nextChar == '<') { // <
            addCharToN(1); // add <
            addAndLoopToLexeme(); // Read "stdio.h" or "string.h"
            addCharToN(3); // add .h>
            if (toStringLexeme().equals("#include <stdio.h>")) {
                nextToken = Token.INCLUDE_STDIO;
            } else if (toStringLexeme().equals("#include <string.h>")) {
                nextToken = Token.INCLUDE_STR;
            } else {
                error();
            }
        }
    }

    public static Token lex() {
        lexLen = 0;
        getNonBlank();
        switch (charClass) {
            case LETTER:

                addCharToN(1);
                addAndLoopToLexeme();

                // handling #include
                if (lexeme[0] == '#' && lexeme[1] == 'i' && lexeme[2] == 'n' && lexeme[3] == 'c' &&
                        lexeme[4] == 'l' && lexeme[5] == 'u' && lexeme[6] == 'd' && lexeme[7] == 'e' && lexLen == 8) {
                    determineHeader();
                    getChar();
                }

                // adding input output statements
                else if (lexeme[0] == 'f' && lexeme[1] == 'o' && lexeme[2] == 'r' && lexLen == 3)
                    nextToken = Token.FOR;
                else if (lexeme[0] == 'i' && lexeme[1] == 'f' && lexLen == 2) // check for if
                    nextToken = Token.IF;
                else if (lexeme[0] == 'e' && lexeme[1] == 'l' && lexeme[2] == 's' && lexeme[3] == 'e' && lexLen == 4) // check
                                                                                                                      // for
                                                                                                                      // else
                    nextToken = Token.ELSE;
                else if (lexeme[0] == 'p' && lexeme[1] == 'r' && lexeme[2] == 'i' && lexeme[3] == 'n'
                        && lexeme[4] == 't' && lexeme[5] == 'f' && lexLen == 6)
                    nextToken = Token.PRINTF;
                else if (lexeme[0] == 'f' && lexeme[1] == 'g' && lexeme[2] == 'e' && lexeme[3] == 't'
                        && lexeme[4] == 's' && lexLen == 5)
                    nextToken = Token.FGETS;
                else if (lexeme[0] == 'p' && lexeme[1] == 'u' && lexeme[2] == 't' && lexeme[3] == 's' && lexLen == 4)
                    nextToken = Token.PUTS;
                else if (lexeme[0] == 'r' && lexeme[1] == 'e' && lexeme[2] == 't' && lexeme[3] == 'u' &&
                    lexeme[4] == 'r' && lexeme[5] == 'n' && lexLen == 6) {
                        nextToken = Token.RETURN;
                }
                else if (lexeme[0] == 'v' && lexeme[1] == 'o' && lexeme[2] == 'i' && lexeme[3] == 'd' && lexLen == 4) {
                    nextToken = Token.VOID;
                }
                else if ((lexeme[0] == 'i' && lexeme[1] == 'n' && lexeme[2] == 't' && lexLen == 3 )|| (lexeme[0] == 'c' && lexeme[1] == 'h' && lexeme[2] == 'a' && lexeme[3] == 'r' && lexLen == 4 )) {
                    nextToken = Token.TYPE;
                }
                
                 else
                    nextToken = Token.IDENT;
                break;
            case DIGIT:
                addChar();
                getChar();
                while (charClass == Char.DIGIT) {
                    addChar();
                    getChar();
                }
                nextToken = Token.INT_LIT;
                break;
            case UNKNOWN:
                nextToken = lookup(nextChar);
                getChar();
                break;

            case EOF:
                nextToken = Token.EOF;
                lexeme[0] = 'E';
                lexeme[1] = '0';
                lexeme[2] = 'F';
                lexeme[3] = 0;
                break;
        }
        if(nextToken != Token.EOF)
        System.out.println("Next token is: " + nextToken + ", Next lexeme is " + toStringLexeme());

        return nextToken;
    }

    public static String toStringLexeme() {
        String s = "";
        for (int i = 0; i < lexLen; i++) {
            s += lexeme[i];
        }
        return s;
    }

    public void expr() {
        System.out.println("Enter <expr>");
        term();
        while (nextToken == Token.ADD_OP || nextToken == Token.SUB_OP) {
            lex();
            term();
        }
        System.out.println("Exit <expr>");
    }

    public void term() {
        System.out.println("Enter <term>");
        factor();
        while (nextToken == Token.MULT_OP || nextToken == Token.DIV_OP) {
            lex();
            factor();
        }
        System.out.println("Exit <term>");
    }

    public void factor() {
        System.out.println("Enter <factor>");
        if (nextToken == Token.IDENT || nextToken == Token.INT_LIT || nextToken == Token.TYPE)
            lex();
        else {
            if (nextToken == Token.LEFT_PAREN) {
                lex();
                expr();
                if (nextToken == Token.RIGHT_PAREN)
                    lex();
                else
                    error();
            } else
                error();
        }
        System.out.println("Exit <factor>");
    }

    public static void error() {
        System.out.println("ERROR - parsing error occured");
        System.exit(1);
    }

    public static void parseFunction() {
        System.out.println("Enter <function>");
        lex(); // void
        lex(); // function name
        lex(); // (
        lex(); // )
        lex(); // {
        while (nextToken != Token.RIGHT_CURLY) { // Read function body
            parseStatements();
        }
        lex(); // }
        System.out.println("Exit <function>");
    }

    public static void parseMainFunction() {
        System.out.println("Enter <mainFunction>");
        lex(); // int
        lex(); // main
        lex(); // (
        lex(); // )
        lex(); // {
        while (nextToken != Token.RIGHT_CURLY && nextToken != Token.EOF) { // Read function body
                parseStatements();
        }
        System.out.println("Exit <mainFunction>");
    }

    public static void parseStatements() {
        if (nextToken == Token.FOR) {
            parseLoop();
        } else if (nextToken == Token.IF) {
            parseIfStmt();
        } else if (nextToken == Token.PRINTF || nextToken == Token.FGETS || nextToken == Token.PUTS) {
            parseIOStmt();
        } else if(nextToken == Token.TYPE) {
            parseDefineVar(); 
        } else{
            lex();
        }
    }

    public static void parseIOStmt() {
        System.out.println("Enter <ioStmt>");
        //lex(); // printf, fgets, or puts
        //addCharToN(1);
      //  getChar();
      //  addChar();
   //   lex();
       lex(); // (
       if(nextChar == '"') {
            parseStringLiteral();
       } 
        while (nextToken != Token.SEMICOLON)  // Handles multiple arguments
        lex(); // ;
        System.out.println("Exit <ioStmt>");
    }


    public static void parseLoop() {
        System.out.println("Enter <loop>");
        lex(); // for
        lex(); // (
        parseDefineVar(); // i = 0;
        parseCondition(); // i < stringLength
        lex(); // i++
        lex(); //+
        lex(); //+
        lex(); // )
        lex(); // {
        while (nextToken != Token.RIGHT_CURLY) { // Process loop body
            parseStatements();
        }
        lex(); // }
        System.out.println("Exit <loop>");
    }

    public static void parseIfStmt() {
        System.out.println("Enter <ifStmt>");
        lex(); // if
        parseCondition();
        lex(); // {
            parseDefineVar(); 
            parseDefineVar(); 
            parseDefineVar(); 
        lex(); // }
        System.out.println("Exit <ifStmt>");
    }


    public static void parseCondition() {
        System.out.println("Enter <condition>");
        lex(); // passby ;
        while(nextToken != Token.SEMICOLON && nextToken != Token.RIGHT_PAREN )
        lex();
        System.out.println("Exit <condition>");
    }

}
