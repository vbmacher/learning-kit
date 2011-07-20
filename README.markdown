What's this?
============

This is a command-line calculator, written in C. Supported platforms are all that supports utilities FLEX, YACC and GNU C.


Build and Install
=================

This is free software, released under GNU GPL v2 license. The source code is written for
the FLEX [[1]] and YACC [[2]] compiler generators. To build it, you must have installed these utilities.

The project was then written in GNU C. For easy build, use the 'make' utility.

[1]: http://flex.sourceforge.net/
[2]: http://gnuwin32.sourceforge.net/packages/byacc.htm


Usage
======

The calculator supports the following commands:

* `h`, `help`
* `q`, `quit` - quit the calculator.

There are supported numbers of various format:

* integral
* decimal

Both number formats can be expressed in various radixes, such as:

* in decadic radix (`1`, `2.3`, `3`, `-10.554`, ...)
* in hexadecimal radix (`x4`, `xA.f3`, `x5FC`, `-x5B.12`, ...)
* in octal radix (`o3`, `o7`, `o43.243`, `-o23.05`, ...)
* in binary radix (`b1`, `b10101.10101`, `-b01101.11`, ...)

Results are printed in:

*decadic, octal, hexadecimal and binary radix

The following operators are supported:

* `+` - addition, or unary plus (`5+5=6`)
* `-` - substraction, or unary minus (`3-2=1`)
* `*` - multiplication (`5*6=30`)
* `/` - divide (`4/2=2`)
* `%`, `mod` - divide remainder (`6%3=0`)
* `^` - power (`2^3=8`)
* `!` - factorial (`3!=6`)

The following math functions are supported:

* `sin`   - sinus, input in radians (`sin PI=0`)
* `cos`   - cosinus, input in radians (`cos PI=-1`)
* `tan`   - tangens, input in radians (`tan PI=0`)
* `cotan` - cotangens, input in radians (`cotan (PI/2)=0`)
* `log`   - logarithm with base 10 (`log 10=1`)
* `log2`  - logarithm with base 2 (`log2 2=1`)
* `ln`    - logarithm with base E (`ln E =1`)
* `sqrt`  - square root (`sqrt 16=4`)
* `ceil`  - smallest integral value that is not less than input (`ceil 4.3=5`)
* `floor` - greatest integral value that is not greater than input (`floor 4.6=4`)

The following constants are supported:

*  `PI` - The Ludolf PI number (`3.141592`...)
*  `E`  - The Euler number (`2.71828182`...)

This calculator also supports variables that store values, e.g.:

*  `x = 5`
*  `var = 4.23 * x`
*  `(-2 * var + x)/ (4*var^2)`

Every result is stored to the R variable.
        
Operator priorities and associativity
=====================================

I tried to follow the associations and priorities like it is in real math.
The higher number of the operator, the higher priority it has.

1. (right)  `=`
2. (left)   `+` `-`
3. (left)   `*` `/`
4. (left)   `%`
5. (right)  `-` (unary) `+` (unary)
6. (left)   `^`
7. (left)   `!`

Notes:
------

Left associativity means:

> `5 + 5 + 5 + 5 = (((5 + 5) + 5) + 5)`

Right associativity means:

> `5 + 5 + 5 + 5 = (5 + (5 + (5 + 5)))`

Examples:
=========

Factorial (`!`)
-------------

* `5!5!    =>` error (missing operator, `!` is unary)
* `-5!     = -(5!)       = -120`
* `5! * 5! = (5!) * (5!) = 14400`
* `5! ^ 5  = (5!) ^ 5    = 24883200000`


Power (`^`)
-----------

* `5 ^ 5 * 5 = (5 ^ 5) * 5 = 125`
* `-5 ^ 2    = - (5 ^ 2)   = -25`

Modulo (`%`)
------------

* `5 % 3 * 5 = ((5 % 3) * 5) = 10`
* `5 * 5 % 3 = (5 * (5 % 3)) = 10`
