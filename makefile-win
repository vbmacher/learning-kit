all: flex yacc kcalc.tab.c
	gcc -o kcalc.exe kcalc.tab.c

flex:
	flex -olexyy.c kcalc.l

yacc: flex lexyy.c
	yacc -b kcalc kcalc.y

