all: flex yacc kcalc.c parser.c lexer.c
	gcc -o kcalc parser.c kcalc.c lexer.c -lm

flex:
	flex -i -olexer.c lexer.l

yacc: flex lexer.c
	yacc -o parser.c --report=all --warnings=error --defines parser.y

