/**
 * Guangze Zu
 * gxz170001
 * Section 0W1
 * 
 * This program iterates through a while loop using recrusive descent parsing by breaking down the while loop into its associated EBNF rules.
 * 
 * EBNF Rules:
 * 
 * <whileLoop> -> while (<cond>) '{' {(<assign>; | <ifStmt>)} '}'
 * <ifStmt> -> if (<cond>) <assign> [else <assign>]
 * <cond> -> <expr> [(> | >= | < | <= | == | !=) <expr>]
 * <assign> -> id (=<expr> | ++ | --)
 * <expr> -> <term> {(+ | -) <term>}
 * <term> -> <factor> {(* | / | %) <factor>} # modulo same precedence as multiplication and division
 * <factor> -> id | int_constant | ( <expr> )
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Enum for character classes
 * @author Guangze
 *
 */
enum Char {
	LETTER,
	DIGIT,
	UNKNOWN,
	EOF
}

/**
 * Enum for token codes
 * @author Guangze
 *
 */
enum Token {
	LEFT_PAREN,
	RIGHT_PAREN,
	ADD_OP,
	SUB_OP,
	MULT_OP,
	DIV_OP,
	MOD_OP,
	INT_LIT,
	IDENT,
	ASSIGN_OP,
	GR_THAN,
	GR_EQ_THAN,
	LS_THAN,
	LS_EQ_THAN,
	EQ_TO,
	NT_EQ_TO,
	INCR_OP,
	DECR_OP,
	NEGATION,
	SEMICOLON,
	LEFT_CURLY,
	RIGHT_CURLY,
	WHILE,
	IF,
	ELSE,
	EOF
}

public class gxz170001_A2Q5 {
	
	// input file name
	public static final String filePath = "whileStatement.txt";
	public static BufferedReader inputFile; // input file reader
	public static char[] lexeme = new char[100]; // current lexeme array
	public static int lexLen = 0; // current lexeme length 
	public static Char charClass; // next character class
	public static Token nextToken; // next token code
	public static char nextChar; // next character read in

	public static void main(String[] args) {
		try {
			inputFile = new BufferedReader(new FileReader(filePath));	// read in file
		} catch (FileNotFoundException e) {
			System.out.println("ERROR - cannot open " + filePath); // print error if cannot open
			System.exit(1);
		}
		
		getChar();
		lex();
		whileLoop();
		
		try {
			inputFile.close(); // close input file
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/* Lexical Analyzer Functions */ 
	
	/**
	 * Function which gets the next character of the input and determine its character class
	 * @throws IOException
	 */
	public static void getChar() {
		try {
			if((nextChar = (char) inputFile.read()) != -1) { // EOF has not been reached
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
	
	/**
	 * adds nextChar to the lexeme
	 */
	public static void addChar() {
		if (lexLen <= 98) {
			lexeme[lexLen++] = nextChar;
			lexeme[lexLen] = 0;
		}
		else
			System.out.println("ERROR - lexeme is too long");
	}
	
	/**
	 * function that calls getChar until it returns a non-whitespace character
	 */
	public static void getNonBlank() {
		while(Character.isWhitespace(nextChar))
			getChar();
	}
	
	/**
	 * Simple lexical analyzer for arithmetic expressions
	 * @return next token code read in
	 */
	public static Token lex() {
		lexLen = 0;
		getNonBlank();
		switch(charClass) {
		
		// parse identifiers
		case LETTER:
			addChar();
			getChar();
			while(charClass == Char.LETTER || charClass == Char.DIGIT) {
				addChar();
				getChar();
			}
			
			
			if(lexeme[0] == 'w' && lexeme[1] == 'h' && lexeme[2] == 'i' && lexeme[3] == 'l' && lexeme[4] == 'e' && lexLen == 5) // check for while 
				nextToken = Token.WHILE;
			else if(lexeme[0] == 'i' && lexeme[1] == 'f' && lexLen == 2) // check for if
				nextToken = Token.IF;
			else if(lexeme[0] == 'e' && lexeme[1] == 'l' && lexeme[2] == 's' && lexeme[3] == 'e' && lexLen == 4) // check for else
				nextToken = Token.ELSE;
			else
				nextToken = Token.IDENT; // or else it is an identifier
			break;
			
		// parse integer literals
		case DIGIT:
			addChar();
			getChar();
			while(charClass == Char.DIGIT) {
				addChar();
				getChar();
			}
			nextToken = Token.INT_LIT;
			break;
		// parse parenthesis and operators
		case UNKNOWN:
			try {
				lookup(nextChar);
			} catch (IOException e) {
				e.printStackTrace();
			}
			getChar();
			break;
			
		// end of file
		case EOF:
			nextToken = Token.EOF;
			lexeme[0] = 'E';
			lexeme[1] = 'O';
			lexeme[2] = 'F';
			lexeme[3] = 0;
			break;
		}
		
		System.out.printf("Next token is: %s, Next lexeme is %s\n", nextToken, new String(lexeme, 0, lexLen));
		return nextToken;
	}
	
	/**
	 * looks up operators and parentheses and return the token
	 * @param ch character to lookup
	 * @return token code of the character
	 * @throws IOException if file read throws an error
	 */
	public static Token lookup(char ch) throws IOException {
		switch(ch) {
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
		case ';':
			addChar();
			nextToken = Token.SEMICOLON;
			break;
		case '+':
			addChar();
			
			// check if it is ++ instead of addition
			if(peek(inputFile) == '+') {
				nextChar = (char) inputFile.read();
				addChar();
				nextToken = Token.INCR_OP;
			} else
				nextToken = Token.ADD_OP;
			break;
		case '-':
			addChar();
			
			// check if -- instead of subtraction
			if(peek(inputFile) == '-') {
				nextChar = (char)inputFile.read();
				addChar();
				nextToken = Token.DECR_OP;
			} else
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
		case '%':
			addChar();
			nextToken = Token.MOD_OP;
			break;
		case '>':
			addChar();
			
			if(peek(inputFile) == '=') { // check if >=
				nextChar = (char)inputFile.read();
				addChar();
				nextToken = Token.GR_EQ_THAN;
			} else
				nextToken = Token.GR_THAN;
			break;
		case '<':
			addChar();
			
			if(peek(inputFile) == '=') { // check if <=
				nextChar = (char)inputFile.read();
				addChar();
				nextToken = Token.LS_EQ_THAN;
			} else
				nextToken = Token.LS_THAN;
			break;
		case '=':
			addChar();
			
			if(peek(inputFile) == '=') { // check if ==
				nextChar = (char)inputFile.read();
				addChar();
				nextToken = Token.EQ_TO;
			} else
				nextToken = Token.ASSIGN_OP;
			break;
		case '!':
			addChar();
			
			if(peek(inputFile) == '=') { // check if !=
				nextChar = (char)inputFile.read();
				addChar();
				nextToken = Token.NT_EQ_TO;
			} else
				nextToken = Token.NEGATION;
			break;
		default:
			addChar();
			nextToken = Token.EOF;	// set lexeme array to contain EOF
			lexeme[0] = 'E';
			lexeme[1] = 'O';
			lexeme[2] = 'F';
			lexeme[3] = 0;
			lexLen = 3;
			break;
		}
		
		return nextToken;
	}
	
	/**
	 * Acts as the peek() function for the buffered reader
	 * @param r BufferedReader object
	 * @return next char in buffer stream, will not remove char from the stream
	 * @throws IOException i/o error
	 */
	public static char peek(BufferedReader r) throws IOException {
		r.mark(1);
		int b = r.read();
		r.reset();
		return (char)b;
	}
	
	/* Parser code */
	
	/**
	 * Parses string for EBNF Rule: <expr> -> <term> {(+ | -) <term>}
	 */
	public static void expr() {
		System.out.println("Enter <expr>");
		
		// parse first term
		term();
		
		/* As long as the next token is * or /, get the
		 next token and parse the next factor */
		while (nextToken == Token.ADD_OP || nextToken == Token.SUB_OP) {
			lex();
			term();
		}
		
		System.out.println("Exit <expr>");
	}
	
	/**
	 * Parses string for EBNF Rule: <term> -> <factor> {(* | / | %) <factor>} # modulo same precedence as multiplication and division
	 */
	public static void term() {
		System.out.println("Enter <term>");
		
		// parse first factor
		factor();
		
		/* As long as the next token is *, /, or %, get the
		 next token and parse the next factor */
		while (nextToken == Token.MULT_OP || nextToken == Token.DIV_OP|| nextToken == Token.MOD_OP) {
			lex();
			factor();
		}
		
		System.out.println("Exit <term>");
	}
	
	/**
	 * Parses string for the EBNF Rule: <factor> -> id | int_constant | ( <expr> )
	 */
	public static void factor() {
		System.out.println("Enter <factor>");
		
		// determine which rhs
		if(nextToken == Token.IDENT || nextToken == Token.INT_LIT)
			lex();
		else {
			// if RHS is (<expr>), enforce the parenthesis between the expression
			if (nextToken == Token.LEFT_PAREN) {
				lex();
				expr();
				if(nextToken == Token.RIGHT_PAREN)
					lex();
				else
					error();
			}
			else
				error();
		}
		
		System.out.println("Exit <factor>");
		
	}
	
	/**
	 * Parses string for the EBNF Rule: <cond> -> <expr> [(> | >= | < | <= | == | !=) <expr>]
	 */
	public static void cond() {
		System.out.println("Enter <cond>");
		expr(); // we execute the first expr
		
		if(nextToken == Token.GR_THAN || nextToken == Token.GR_EQ_THAN || nextToken == Token.LS_THAN || nextToken == Token.LS_EQ_THAN || nextToken == Token.EQ_TO || nextToken == Token.NT_EQ_TO) { // cond can be just an expression or a comparison between two expressions
			lex();
			expr(); // execute second expr
		}
		
		System.out.println("Exit <cond>");
		
	}
	
	/**
	 * Parses string for the EBNF rule: <assign> -> id (=<expr> | ++ | --)
	 */
	public static void assign() {
		System.out.println("Enter <assign>");
		if (nextToken == Token.IDENT) {
			lex();
			
			if(nextToken == Token.ASSIGN_OP) { // if next token is assignment, we use =<expr>
				lex();
				expr();
			} else if(nextToken == Token.INCR_OP || nextToken == Token.DECR_OP) // next token is ++ or --
				lex();
			else
				error(); // assign has to be one of these three operations
			
		} else
			error(); // assign has to start with ident
		
		System.out.println("Exit <assign>");
	}
	
	/**
	 * Parses string for EBNF rule: <whileLoop> -> while (<cond>) '{' {(<assign>; | <ifStmt>)} '}'
	 */
	public static void whileLoop() {
		System.out.println("Enter <whileLoop>");
		if(nextToken == Token.WHILE) { // checks for while keyword
			lex();
			if(nextToken == Token.LEFT_PAREN) { // parses (<cond>)
				lex();
				cond();
				if(nextToken == Token.RIGHT_PAREN) {
					lex();
					if(nextToken == Token.LEFT_CURLY) { // checks for left curly brace to start statement block
						lex();
						while(nextToken != Token.RIGHT_CURLY) { // ends when '}' is found
							if(nextToken == Token.IF) // if next token is IF, call <ifStmt> subprogram
								ifStmt();
							else if(nextToken != Token.EOF && nextToken != Token.RIGHT_CURLY){
								assign(); // else it should be a normal <assign> statement
								if(nextToken == Token.SEMICOLON) { // <assign> should end with semicolon
									lex();
								} else
									error();
							}
						}
					} else
						error();
				} else 
					error();
			}
			else
				error();
		} else
			error();
		
		System.out.println("Exit <whileLoop>");
	}
	
	/**
	 * Parses string for EBNF rule: <ifStmt> -> if (<cond>) <assign> [else <assign>]
	 */
	public static void ifStmt() {
		System.out.println("Enter <ifStmt>");
		if(nextToken == Token.IF) { // checks for if keyword
			lex();
			if(nextToken == Token.LEFT_PAREN) { // parses (<cond>)
				lex();
				cond();
				if(nextToken == Token.RIGHT_PAREN) {
					lex();
					assign(); // executes <assign> statement
					
					if(nextToken == Token.SEMICOLON) { // <assign> should end with semicolon
						lex();
					} else
						error();
					
					if(nextToken == Token.ELSE) { // looks for optional else keyword
						lex();
						assign(); // does another <assign> statement if else is encountered
						if(nextToken == Token.SEMICOLON) { // <assign> should end with semicolon
							lex();
						} else
							error();
					}
				} else
					error();
			} else
				error();
		} else 
			error();
		System.out.println("Exit <ifStmt>");
	}
	
	public static void error() {
		System.out.println("ERROR - parsing error occured");
		System.exit(1);
	}
}
