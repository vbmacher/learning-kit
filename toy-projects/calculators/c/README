# What is leCalc?

leCalc is just a tiny and simple calculator for programmers.
It is written in C as my learning practice.

## License

This is free software, released under [Apache 2 license](https://www.apache.org/licenses/LICENSE-2.0).

# Build and Install

The parser is written using FLEX [[1]] and YACC/Bison [[2]]. The source code was written in GNU C. To build it in easy way, execute following commands:

1. `configure`
2. `make`

If you want to install it, type `make install` in root environment. If you want later uninstall the software, execute command `make uninstall`.

[1]: http://flex.sourceforge.net/
[2]: http://gnuwin32.sourceforge.net/packages/byacc.htm


# Usage

In interactive mode, the calculator supports following commands:

* `h`, `help`
* `use 'file'` - process external file.
* `q`, `quit`  - quit the calculator.

## Radix

There are supported integral and decimal numbers in many radixes and with any precision. 

* decadic (`1`, `2.3`, `3`, `-10.554`, ...)
* hexadecimal (`4x`, `A.f3x`, `5FCx`, `-5B.12x`, ...)
* octal (`3o`, `7o`, `43.243o`, `-23.05o`, ...)
* binary (`1b`, `10101.10101b`, `-01101.11b`, ...)

Results are printed in all these radixes. The following operators are supported:

* `+` - addition, or unary plus (`5+5=6`)
* `-` - substraction, or unary minus (`3-2=1`)
* `*` - multiplication (`5*6=30`)
* `/` - divide (`4/2=2`)
* `%`, `mod` - divide remainder (`6%3=0`)
* `^` - power (`2^3=8`)
* `!` - factorial (`3!=6`)

## Math functions

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

## Constants

The following constants are supported:

*  `PI` - The Ludolf PI number (`3.141592`...)
*  `E`  - The Euler number (`2.71828182`...)

## Variables, expressions

This calculator also supports variables that store values AND expressions. However,
recursion is not supported, e.g.:

*  `D = b^2 - 4 * a * c`
*  `x1 = (-b + sqrt(D))/(2*a)`
*  `x2 = (-b - sqrt(D))/(2*a)`
*  `a = 1`
*  `b = -1`
*  `c = -1`
*  `x1` (prints `1.168034`)

Every result is stored to the R variable.

# Operator priorities and associativity

I tried to follow the associations and priorities like it is in real math.
The higher number of the operator, the higher priority it has.

1. (right)  `=`
2. (left)   `+` `-`
3. (left)   `*` `/`
4. (left)   `%`
5. (right)  `-` (unary) `+` (unary)
6. (left)   `^`
7. (left)   `!`

## Notes:

Left associativity means:

> `5 + 5 + 5 + 5 = (((5 + 5) + 5) + 5)`

Right associativity means:

> `5 + 5 + 5 + 5 = (5 + (5 + (5 + 5)))`

# Examples:

## Factorial (`!`)

* `5!5!    =>` error (missing operator, `!` is unary)
* `-5!     = -(5!)       = -120`
* `5! * 5! = (5!) * (5!) = 14400`
* `5! ^ 5  = (5!) ^ 5    = 24883200000`


## Power (`^`)

* `5 ^ 5 * 5 = (5 ^ 5) * 5 = 125`
* `-5 ^ 2    = - (5 ^ 2)   = -25`

## Modulo (`%`)

* `5 % 3 * 5 = ((5 % 3) * 5) = 10`
* `5 * 5 % 3 = (5 * (5 % 3)) = 10`
