Guangze Zu
gxz170001
Section 0W1

Exeuction Instructions:
** Running the program requires you to change the hardcoded filePath string in gxz170001_A2Q5.java to the input file. **
1. javac gxz170001_A2Q5.java
2. java gxz170001_A2Q5

This program iterates through a while loop using recursive decent programing in order to break it down using the associated EBNF rules.

EBNF Rules:
<whileLoop> -> while (<cond>) '{' {(<assign>; | <ifStmt>)} '}'
<ifStmt> -> if (<cond>) <assign> [else <assign>]
<cond> -> <expr> [(> | >= | < | <= | == | !=) <expr>]
<assign> -> id (=<expr> | ++ | --)
<expr> -> <term> {(+ | -) <term>}
<term> -> <factor> {(* | / | %) <factor>} # modulo same precedence as multiplication and division
<factor> -> id | int_constant | ( <expr> )

In order to do so Lexical analysis is preformed to break the code down into understandable tokens to be operated on.