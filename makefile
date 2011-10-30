all: flex yacc kcalc.c parser.c lexer.c
	gcc -g -o kcalc parser.c kcalc.c lexer.c -lm

flex:
	flex -i -olexer.c --header-file=lexer.h lexer.l

## yacc -o parser.c --report=all --warnings=error --defines parser.y
yacc: flex lexer.c
	yacc -o parser.c parser.y

