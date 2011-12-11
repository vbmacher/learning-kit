# Process this file with autoconf to produce a configure script.
AC_INIT([kcalc],[0.12b],[pjakubco@gmail.com])
AC_CONFIG_AUX_DIR([build-aux])
AM_INIT_AUTOMAKE([-Wall -Werror])

# Checks for programs.
AC_PROG_INSTALL
AC_PROG_CC
AM_PROG_CC_C_O
AC_PROG_LEX
AC_PROG_YACC
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

