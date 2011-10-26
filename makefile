all: flex yacc kcalc.c parser.c
	gcc -o kcalc parser.c kcalc.c -lm

flex:
	flex -olexer.c lexer.l

yacc: flex lexer.c
	yacc -o parser.c parser.y

