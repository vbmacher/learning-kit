# Process this file with autoconf to produce a configure script.
AC_INIT([lecalc],[0.12b],[pjakubco@gmail.com])
AC_CONFIG_AUX_DIR([build-aux])
AM_INIT_AUTOMAKE([-Wall -Werror])

AC_DEFUN([AC_PROG_BISON], [

AC_CHECK_PROG(have_prog_bison, [bison], [yes],[no])

#Do not use *.h extension for parser header file but *.hh
bison_use_parser_h_extension=false

if test "$have_prog_bison" = "yes" ; then
#Verify automake version
#Upto version 1.11 parser headers for yy files are with h extension, from 1.12 it is hh
        automake_version=`automake --version | head -n 1 | cut '-d ' -f 4`
        AC_DEFINE_UNQUOTED([AUTOMAKE_VERSION], [$automake_version],
                           [Defines automake version])

        if test "$automake_version" \< "1.12" ; then
            #Use *.h extension for parser header file
        bison_use_parser_h_extension=true
        echo "Automake version < 1.12"
            AC_DEFINE([BISON_USE_PARSER_H_EXTENSION], [1],
                      [Use *.h extension for parser header file])
        fi
fi

AM_CONDITIONAL([BISON_USE_PARSER_H_EXTENSION], [test x$bison_use_parser_h_extension = xtrue])
])


# Checks for programs.
AC_PROG_INSTALL
AC_PROG_CC
AM_PROG_CC_C_O
AC_PROG_YACC
AC_PROG_LEX
AC_PROG_BISON
AC_PROG_SED
AC_PROG_AWK

# Checks for libraries.
AC_CHECK_LIB(m,[floor])
AC_CHECK_LIB(m,[pow])
AC_CHECK_LIB(m,[sqrt])

# Checks for header files.
AC_CHECK_HEADERS([inttypes.h libintl.h malloc.h stddef.h stdlib.h string.h unistd.h],,AC_MSG_ERROR([Missing one or more header files!])) 

# Checks for typedefs, structures, and compiler characteristics.
AC_TYPE_INT16_T
AC_TYPE_INT32_T
AC_TYPE_INT8_T
AC_TYPE_SIZE_T
AC_TYPE_UINT16_T
AC_TYPE_UINT32_T
AC_TYPE_UINT8_T

# Checks for library functions.
AC_CHECK_FUNCS([memset strdup floor pow sqrt],,AC_MSG_ERROR([Missing one or more library functions!]))
AC_FUNC_ALLOCA
AC_FUNC_MALLOC
AC_FUNC_REALLOC

AC_CONFIG_HEADERS([config.h])
AC_CONFIG_FILES([Makefile src/Makefile])
AC_OUTPUT

