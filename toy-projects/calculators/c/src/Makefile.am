BUILT_SOURCES = parser.h lexer.h
AM_LFLAGS = -i --header-file=lexer.h
AM_YFLAGS = -d
bin_PROGRAMS = lecalc
lecalc_CFLAGS = -I$(srcdir)/include
lecalc_SOURCES = lexer.l parser.y lecalc.c $(srcdir)/include/lecalc.h 
lexer.h: lexer.l
	$(LEX) $(AM_LFLAGS) -o lextmp.c $< && rm -f lextmp.c
CLEANFILES = parser.h lexer.h parser.c lexer.c

