package com.github.vbmacher.diffcode.documents

import com.github.vbmacher.diffcode.documents.As8080Document._
import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class As8080DocumentSpec extends FlatSpec {


  "A 8080 program" should "be parsed correctly" in {
    val as8080document = As8080Document("""
            lxi h, bin
            call putstr

      mov h, b
            mvi e,8
    bin1:			; loop:
            mov  a,h		;   comment1
            ral		      ;   comment2
            mov  h,a
            jc bin2		  ;   comment3
            mvi a, '0'	;   comment4
            jmp bin3
    bin2:	mvi a, '1'		;   comment5
    bin3:         			;   comment6
    """)

    val expected = List(
      Instruction("lxi"), Register("h"), Identifier("bin"),
      Instruction("call"), Identifier("putstr"),
      Instruction("mov"), Register("h"), Register("b"),
      Instruction("mvi"), Register("e"), Literal("8"),
      Label("bin1:"), Instruction("mov"), Register("a"), Register("h"),
      Instruction("ral"),
      Instruction("mov"), Register("h"), Register("a"),
      Instruction("jc"), Identifier("bin2"),
      Instruction("mvi"), Register("a"), Literal("'0'"),
      Instruction("jmp"), Identifier("bin3"),
      Label("bin2:"), Instruction("mvi"), Register("a"), Literal("'1'"),
      Label("bin3:")
    )

    assert(as8080document.parse() === expected)
  }
}
