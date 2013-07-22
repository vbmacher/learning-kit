package codility;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class SolutionTest {
    
    private Solution solution;
    
    @Before
    public void setUp(){
        solution = new Solution();
    }
    
    @Test
    public void testExample1() {
        String S = "abababa";
        assertEquals(10, solution.solution(S));
    }
    
    @Test
    public void testExample2() {
        String S = "aaa";
        assertEquals(4, solution.solution(S));
    }

    @Test
    public void testSmallCycles() {
        String S = "babbbababbbb";
        assertEquals(12, solution.solution(S));
    }
    
    @Test
    public void testSmallMorphism() {
        String S = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        assertEquals(576, solution.solution(S));
    }
    
    @Test
    public void testSmallCyclesWithMoreCharsBetween() {
        String S = "abbaaabbaaaa";
        assertEquals(12, solution.solution(S));
    }
    
    @Test
    public void testLongCycles() {
        String S = "bbbbabaaaabbbbbbbbabaaaabbbbbbbbabaaaabbbbbbbbabaaaaabbbbbbabaaaabbbbbbbbabaaaabbbbbbbbabaaaabbbbbbbbabaaaabaababbbbabaaaabbbbbbbbabaaaabbbbbbbbabaaaabbbbbbbbabaaaaabbbbbbabaaaabbbbbbbbabaaaabbbbbbbbabaaaabbbbbbbbabaaaabbbbbbabaaaabbbbbbbbabaaaabbbbbbbbabaaaabbbbbbbbabaaaaabbbbbbabaaaabbbbbbbbabaaaabbbbbbbbabaaaabbbbbbbbabaaaabaababbbbabaaaabbbbbbbbabaaaabbbbbbbbabaaaabbbbbbbbabaaaaabbbbbbabaaaabbbbbbbbabaaaabbbbbbbbabaaaabbbbbbbbabaaaa";
        assertEquals(608, solution.solution(S));
    }
    
    @Test
    public void testLongCyclesWithTweaks() {
        String S = "acabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaaacabaa";
        assertEquals(3900, solution.solution(S));
    }

    @Test
    public void testLongRandomString() {
        String S = "btcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtclbtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtcebtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtclbtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtcglfbtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtclbtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtcebtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtclbtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtcjlqbtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtclbtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtcebtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtclbtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtcglfbtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtclbtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtcebtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtclbtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtcsbtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtclbtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtcebtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtclbtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtcglfbtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtclbtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtcebtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtclbtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtcjlqbtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtclbtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtcebtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtclbtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtcglfbtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtclbtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtcebtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtclbtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtcibtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtclbtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtcebtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtclbtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtcglfbtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtclbtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtcebtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtclbtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtcjlqbtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtclbtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtcebtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtclbtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtcglfbtcbtcbtctljbtcbtcbtcalbtcbtcbtctljbtcbtcbtclbtcbt";
        assertEquals(2160, solution.solution(S));
    }
    
    @Test
    public void testLongMorphismPrefixThenRandomString() {
        String S = "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzebnlmhjrqffboantqhqcpdnakseciabnttqipopjogfdhigrkitrbgnitosndbidlplocttgnnmfhhgrnmacjohpdosqobghagtipddbhrodheijrblhnrfjsmjglnjfhcgosghnqfsbpdkeodkcegjiqdmajkqgeaprfjjgrjgnpprbiciltqlnpqmaponltpmdfshememdceksarblogbtreehamsmqkarhebjcnsbbaijorfrpenpimftakpscrobibgtlhttennsapgihkhcgknpqndqsnjbhreromcqjagmggjboorsnermqegbhgmdrnlafkabetejdqaodbmkjtqfoqdfthhkmmfpfreapobqlbhcfnnkhmfigbssaggtrrpgdqdikdtgocrpbhaiaptdcpbhitpdfqdqgmjsdmlsjfqfeiljtbhtktdislahhhehgfbceottmdhookehhoqgfdjdjfklhhhrnhkngojojkhrnerqicejietdlbeofoqbjqhlzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzebnlmhjrqffboantqhqcpdnakseciabnttqipopjogfdhigrkitrbgnitosndbidlplocttgnnmfhhgrnmacjohpdosqobghagtipddbhrodheijrblhnrfjsmjglnjfhcgosghnqfsbpdkeodkcegjiqdmajkqgeaprfjjgrjgnpprbiciltqlnpqmaponltpmdfshememdceksarblogbtreehamsmqkarhebjcnsbbaijorfrpenpimftakpscrobibgtlhttennsapgihkhcgknpqndqsnjbhreromcqjagmggjboorsnermqegbhgmdrnlafkabetejdqaodbmkjtqfoqdfthhkmmfpfreapobqlbhcfnnkhmfigbssaggtrrpgdqdikdtgocrpbhaiaptdcpbhitpdfqdqgmjsdmlsjfqfeiljtbhtktdislahhhehgfbceottmdhookehhoqgfdjdjfklhhhrnhkngojojkhrnerqicejietdlbeofoqbjqhl";
        assertEquals(125500, solution.solution(S));
    }

    @Test
    public void testSmallCyclesBeforeInTheMiddleAndAtTheEnd() {
        String S = "aaabaaabaaa";
        assertEquals(14, solution.solution(S));
    }

    @Test
    public void testSmallCyclesBeforeAndAtTheEnd() {
        String S = "aabaaaa";
        assertEquals(8, solution.solution(S));
    }
    
    @Test
    public void testLongMorphismWithSmallTweaks() {
        String S = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabaaaaaaaaaaaaaaaaaaaaaaaaaaaaacaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        assertEquals(850, solution.solution(S));
    }

    @Test
    public void testLongMorphism() {
        String S = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        assertEquals(1049600, solution.solution(S));
    }
    
   
}
