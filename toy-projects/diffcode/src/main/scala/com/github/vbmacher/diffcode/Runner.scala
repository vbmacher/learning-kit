package com.github.vbmacher.diffcode

import java.io.File
import java.util.Locale

import com.github.vbmacher.diffcode.documents.{As8080Document, Java7Document, StringDocument}
import com.github.vbmacher.diffcode.model.VectorDocument
import com.github.vbmacher.diffcode.similarity.{Cosine, Levenshtein, Similarity}
import scopt.OptionParser

import scala.language.postfixOps
import scala.util.Try
import scala.util.control.NonFatal

object Runner extends App {

  case class CmdLine(lang: String = "text", algorithms: Seq[String] = Seq("all"), verbose: Boolean = false,
                     files: Seq[File] = Seq())

  val cmdLineParser = new OptionParser[CmdLine]("diffcode") {
    head("diffcode", "0.1.0")

    help("help").abbr("h").text("prints this usage text")

    opt[String]('l', "language")
      .action((language, cmdLine) => cmdLine.copy(lang = language))
      .text("Language of the files (java, as8080, text). Default: text")

    opt[Seq[String]]('a', "algorithms")
      .action((alg, cmdLine) => cmdLine.copy(algorithms = alg))
      .text("Similarity algorithms to use (levenshtein, cosine, all). Default: all")

    opt[Unit]('v', "verbose")
      .action((_, cmdLine) => cmdLine.copy(verbose = true))
      .text("Verbose output")

    arg[File]("<file>...").unbounded().optional()
      .action((file, cmdLine) => cmdLine.copy(files = cmdLine.files :+ file))
      .text("Files to compare")
  }

  cmdLineParser.parse(args, CmdLine()) match {
    case Some(cmdLine) =>
      cmdLine.files combinations 2 map { twoFiles =>
        val (file1, file2) = (twoFiles.head, twoFiles(1))

        Try(Runner.documents(cmdLine.lang)) flatMap { docFactory =>
          val doc1 = docFactory(file1)
          val doc2 = docFactory(file2)

          Try(cmdLine.algorithms flatMap Runner.algorithms distinct) map { similarity =>
            val results = similarity map { s =>
              if (cmdLine.verbose) "%1.3f - %s".format(s.compare(doc1, doc2), s.name)
              else "%1.3f".format(s.compare(doc1, doc2))
            }

            (twoFiles, results)
          }
        } recover {
          case NonFatal(ex) =>
            if (cmdLine.verbose) println(ex.getMessage)
            System.exit(1)
            (Nil, Nil) // will never happen
        } get
      } foreach { case (fileNames, results) =>
        if (cmdLine.verbose) println(fileNames.mkString(",") + ":\n\t" + results.mkString("\n\t"))
        else println(fileNames.mkString(",") + ": " + results.mkString(" "))
      }

    case None => System.exit(1)
  }



  def documents(lang: String): File => VectorDocument = lang.toLowerCase(Locale.ENGLISH) match {
    case "as8080" => (file: File) => As8080Document(file)
    case "java" => (file: File) => Java7Document(file)
    case "text" => (file: File) => StringDocument(file)
    case x => throw new IllegalArgumentException(s"Unknown document type: $x")
  }


  def algorithms(alg: String): List[Similarity[VectorDocument]] = alg.toLowerCase(Locale.ENGLISH) match {
    case "levenshtein" => List(Levenshtein)
    case "cosine" => List(Cosine)
    case "all" => List(Levenshtein, Cosine)
    case x => throw new IllegalArgumentException(s"Unknown algorithm: $x")
  }

}
