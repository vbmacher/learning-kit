# diffcode

Command-line tool for comparing source codes.
The tool compares every pair of input files with various text similarity algorithms and prints the normalized
output on standard output.

Build with: `./gradlew jar`. Resulting JAR file is in `build/libs`.

## Usage

Run diffcode with command:

`java -jar diffcode.jar [options] [<file>...]`

Options are:

```
  --help                   prints this usage text
  -l, --language <value>   Language of the files (java, as8080, text). Default: text
  -a, --algorithms <value>
                           Similarity algorithms to use (levenshtein, cosine, all). Default: all
  -v, --verbose <value>    Verbose output
  <file>...                Files to compare
```

The standard output has the form:

```
file1,file2: xx.xxx yy.yyy
```

Where pair `file1,file2` is a pair of compared file names. After colon and space (`: `) follow normalized outputs of
chosen similarity algorithms, separated with single space ('` `') in order as they were specified (or as they appear
in the "help" text).

The normalization means that the results are float numbers, ranging from 0 to 1, where:

- 0 - files are not similar
- 1 - files have equal content (are the same)


## Example

Let's have two files: `A.java` and `B.java`. Their content is:

`A.java`:

```java
class Runner {
  boolean b = true;

  public static void main(String[] args) {
    if (!b) {
      System.out.println("non-sense");
    }
  }
}
```

`B.java`:

```java
class Runner {
  boolean variable = true;

  public static void main(String[] args) {
    if (!variable) System.out.println("non-sense");
  }
}
```

You can see the difference is just syntactic:

- in not using curly braces (`{` and `}`) when printing on the stdout
- variable is renamed

Semantically the programs are equal. Now, we can run the `diffcode` and observe the results:

```
> java -jar diffcode.jar -v -l java A.java B.java

A.java,B.java:
	0.833 - levenshtein
	1.000 - cosine
``` 

As we can see, our implementation of cosine similarity algorithm seems to better reflect semantical equivalence, but
levenshtein reflects more the syntactical equivalence.

## Supported languages

Currently, the following languages/formats are supported:

- Plaintext
- Java 7, based on https://docs.oracle.com/javase/specs/jls/se7/html/jls-3.html#jls-3.9 . Apparently lexer work also for
  Java 8 and Java 9 too. 
- 8080 Assembler, based on emuStudio implementation: http://www.emustudio.net/docuser/mits_altair_8800/index/#AS-8080
 
 