BUILT_SOURCES = parser.h lexer.h
AM_LFLAGS = -i --header-file=lexer.h
AM_YFLAGS = -d
bin_PROGRAMS = kcalc
kcalc_CFLAGS = -I$(srcdir)/include
kcalc_SOURCES = lexer.l parser.y kcalc.c $(srcdir)/include/kcalc.h 
lexer.h: lexer.l
	$(LEX) $(AM_LFLAGS) -o lextmp.c $< && rm -f lextmp.c
CLEANFILES = parser.h lexer.h parser.c lexer.c

