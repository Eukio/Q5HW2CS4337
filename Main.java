
/*
Alishba Jafri - ARJ220005
Eucharist Tan - ETT220002
CS 4337.007
Professor Karami
3/23/2025

 * 
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public class Char {
        public static final char LETTER = 65;
        public static final char DIGIT = 66;
        public static final char UNKNOWN = 67;
        public static final char EOF = 101;
    }

    public class Token { //Somewhat similar to ascii values
        public static final char INT_LIT = 73;
        public static final char IDENT = 74;
        public static final char WHILE = 75;
        public static final char FOR =76;
        public static final char IF = 77;
        public static final char ELSEIF = 78;
        public static final char ELSE = 79;
        public static final char INCR_OP = 80;
        public static final char DECR_OP = 81;

        public static final char LEFT_PAREN = 40;
        public static final char RIGHT_PAREN = 41;
        public static final char MULT_OP = 42;
        public static final char ADD_OP = 43;
        public static final char SUB_OP = 45;
        public static final char DIV_OP = 47;
    
        public static final char EQ_TO = 58;
        public static final char LS_THAN = 60;
        public static final char ASSIGN_OP = 61;
        public static final char GR_THAN = 62;
        public static final char GR_EQ_THAN = 63;
        public static final char LS_EQ_THAN = 64;

        public static final char NEGATION = 33;
        public static final char SEMICOLON = 59;
        public static final char LEFT_CURLY = 123;
        public static final char RIGHT_CURLY = 125;

        public static final char EOF = 101;
    }
    public static final String FILEPATH = "program.txt";
    public static char nextToken;
    public static char charClass; 
    public static char nextChar;
    public static int lexLen = 0;
	public static char[] lexeme = new char[100]; 
    public static BufferedReader inputFile;
    Scanner sc;
    public static void main(String[] args) throws IOException {
    try {
			inputFile = new BufferedReader(new FileReader(FILEPATH));	// read in file
		} catch (FileNotFoundException e) {
			System.out.println("ERROR - cannot open " + FILEPATH); // print error if cannot open
			System.exit(1);
		}
		
		getChar();
		lex();
                
                try {
                    inputFile.close(); // close input file
                } catch (IOException e) {
                    e.printStackTrace();
                }
    }
        //Lookup function for operators and parentheses, return token
        public static char lookup(char ch){
                        switch (ch) {
                        case '(':
                        addChar();
                        nextToken = Token.LEFT_PAREN;
                        break;
                        case ')':
                        addChar();
                        nextToken = Token.RIGHT_PAREN;
                        break;
                        case '+':
                        addChar();
                        nextToken = Token.ADD_OP;
                        break;
                        case '-':
                        addChar();
                        nextToken = Token.SUB_OP;
                        break;
                        case'*':
                        addChar();
                        nextToken = Token.MULT_OP;
                        break;
                        case '/':
                        addChar();
                        nextToken = Token.DIV_OP;
                        break;
                        default:
                        addChar();
                        nextToken = Token.EOF;
                        break;
                        }
                        return nextToken;
                }
                public static void addChar(){
                            if (lexLen <= 98) {
                            lexeme[lexLen++] = nextChar;
                            lexeme[lexLen] = 0;
                            }
                            else
                            System.out.println("Error - lexeme is too long");
                        }
                        public static void getChar() {
                            try {
                                if((nextChar = (char) inputFile.read()) != -1) { 
                                    if(Character.isAlphabetic(nextChar)) 
                                        charClass = Char.LETTER;
                                    else if(Character.isDigit(nextChar))
                                        charClass = Char.DIGIT;
                                    else charClass = Char.UNKNOWN;
                                } else
                                    charClass = Char.EOF;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                       public static void getNonBlank () {
                                   while (nextChar == ' ')
                                   getChar();
                               }
                            public static char lex() {
                           lexLen = 0;
                           getNonBlank();
                    switch (charClass) {
                    /* Parse identifiers */
                    case Char.LETTER:
                    addChar();
            getChar() ; 
            while (charClass == Char.LETTER || charClass == Char.DIGIT){
            addChar();
            getChar();
            }
            nextToken = Token.IDENT;
            break;
            case Char.DIGIT:
            addChar();
            getChar();
            while (charClass == Char.DIGIT) {
                addChar();
                getChar();
            }
            nextToken = Token.INT_LIT;
            break;
           case Char.UNKNOWN:
            lookup(nextChar);
    getChar();
    break;
    case Char.EOF:
    nextToken = Token.EOF;
    lexeme[0] = 'E';        
    lexeme[1] = '0';
    lexeme [2] = 'F';
    lexeme[3] = 0;
    break;
    }
    System.out.println("Next token is: "+((int)nextToken)+", Next lexeme is "+toStringLexeme());
    
        return nextToken;
    }
        public static String toStringLexeme(){
        String s = "";
        for(int i = 0; i < lexLen; i++){
            s+= lexeme[i];
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

    public void factor(){
        System.out.println("Enter <factor>");
        if (nextToken == Token.IDENT || nextToken ==  Token.INT_LIT)
            lex();
      else {
        if (nextToken ==  Token.LEFT_PAREN) {
        lex();
        expr();
    if (nextToken ==  Token.RIGHT_PAREN)
        lex();
        else
        error();
        } 
        else
        error();
    }
        System.out.println("Exit <factor>");
    }
    public static void error() {
		System.out.println("ERROR - parsing error occured");
		System.exit(1);
	}

}
