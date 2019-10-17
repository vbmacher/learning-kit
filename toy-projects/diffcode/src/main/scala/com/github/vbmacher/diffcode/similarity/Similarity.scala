package com.github.vbmacher.diffcode.similarity

trait Similarity[-T] {

  def compare(doc1: T, doc2: T): Double

  def name: String
}
