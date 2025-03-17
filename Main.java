
/*
Alishba Jafri - ARJ220005
Eucharist Tan - ETT220002
CS 4337.007
Professor Karami
3/23/2025

 * 
 */
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {
    public static final String FILEPATH = "program.txt";
    Token nextToken;
    enum Char {
        LETTER,
        DIGIT,
    }

    enum Token {
        LEFT_PAREN,
        RIGHT_PAREN,
        ADD_OP,
        SUB_OP,
        MULT_OP,
        DIV_OP,
        INT_LIT,
        ID,
        ASSIGN_OP,
        GR_THAN,
        GR_EQ_THAN,
        LS_THAN,
        LS_EQ_THAN,
        EQ_TO,
        INCR_OP,
        DECR_OP,
        NEGATION,
        SEMICOLON,
        LEFT_CURLY,
        RIGHT_CURLY,
        WHILE,
        FOR,
        IF,
        ELSEIF,
        ELSE,
    }

    public static void main(String[] args) throws IOException {
        File f = new File(FILEPATH);
        f.createNewFile();
        Scanner sc = new Scanner(f);
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
/* End of if (nextToken == */
/* It was not an id, an integer literal, or a left
parenthesis */
else
error();
} /* End of else */

        System.out.println("Exit <factor>");

    }

}
