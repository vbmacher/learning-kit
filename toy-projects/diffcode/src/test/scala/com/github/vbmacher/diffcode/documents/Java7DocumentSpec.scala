package com.github.vbmacher.diffcode.documents

import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class Java7DocumentSpec extends FlatSpec {

  "A Java program" should "be parsed correctly" in {
    val javaDocument = Java7Document("""
    private void prefixes() {
        pref[0] = length;
        hist[length] = 1;
        int g = 0;
        int f = 0; // shall be undefined

        for (int i = 1; i < length; i++) {
            if ((i < g) && (pref[i - f] != g - i)) {
                pref[i] = Math.min(pref[i - f], g - i);
                hist[pref[i]]++;
            } else {
                g = Math.max(g, i);
                f = i;
                while ((g < length) && (input[g] == input[g - f])) {
                    g++;
                }
                pref[i] = g - f;
                hist[pref[i]]++;
            }
        }
    }""")

    assert(javaDocument.parse().length === 92)
  }


}
