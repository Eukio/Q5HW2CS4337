## NOTES
# example of ebnf while loop:
<whileLoop> -> while (<cond>) '{' {(<assign>; | <ifStmt>)} '}'
 * <ifStmt> -> if (<cond>) <assign> [else <assign>]
 * <cond> -> <expr> [(> | >= | < | <= | == | !=) <expr>]
 * <assign> -> id (=<expr> | ++ | --)
 * <expr> -> <term> {(+ | -) <term>}
 * <term> -> <factor> {(* | / | %) <factor>} 
 * <factor> -> id | int_constant | ( <expr> )

# for the program.txt
<forLoop> -> for
<assign> -> id (=<expr> | ++ | --)
<expr> -> <term> {(+ | - | * | /) <term>} | (<expr>)
<if_stmt> -> if (<cond>) <assign> [else <assign>]
<cond> -> <expr> [(> | >= | < | <= | == | !=) <expr>]
<expr> -> <term> {(+ | -) <term>}
<term> -> <factor> {(* | /) <factor>}
<factor> -> id | int_constant | ( <expr> )
<ident_list> → <identifier> {, <identifier>}





# Lexemes- numeric literals, operators, special words
# Identifers- names of methods, classes, variables
# Token- category of lexemes

index = 2 * count + 17;
lexemes:   tokens:
 index      identifer
   =         equal_sign
   2          int_literal
   *          mult_op
 count       identifier
   +          plus_op
   17         int_literal 
   ;          semicolon
