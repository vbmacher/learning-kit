package vbmacher

import scala.collection.mutable

final case class Entry[K, V](key: K, value: V) {
  var next: Entry[K, V] = _

  def seq(): List[(K, V)] = next match {
    case e => (key, value) :: e.seq()
    case _ => List((key, value))
  }
}

class BucketMap[K, V] extends Map[K, V] with mutable.MapLike[K, V, BucketMap[K, V]] {
  val buckets: Array[Entry[K, V]] = Array.fill(32)(null)

  val m = new mutable.HashMap[Int, String](0 -> "Peter")

  private def index(key: K): Int = {
    key.hashCode() % buckets.length
  }

  private def find(k: K): Option[Entry[K, V]] = {
    var e = buckets(index(k))
    while (e != null && e.key != k) {
      e = e.next
    }
    Option(e)
  }

  private def insert(k: K, v: V): BucketMap[K, V] = {
    val entry = Entry(k, v)
    val i = index(k)
    var e = buckets(i)
    var prev: Entry[K, V] = null
    while (e != null && e.next != null && e.key != k) {
      prev = e
      e = e.next
    }

    if (e == null) {
      buckets(i) = e
    } else if (e.key == k) {
      entry.next = e.next
      prev.next = entry
      e.next = _
    } else {
      e.next = entry
    }
    this
  }

  override def +=(kv: (K, V)): BucketMap.this.type = kv match {
    case (k, v) => insert(k, v)
  }

  override def -=(key: K): BucketMap.this.type = {
    var e = buckets(0)
    var prev: Entry[K, V] = null
    while (e != null && e.key != key) {
      prev = e
      e = e.next
    }
    if (prev != null && e != null) {
      prev.next = e.next
      e.next = _
    }
    this
  }

  override def get(key: K): Option[V] = {
    val bucket = buckets(index(key))
    if (bucket.next == null) Option(bucket.value)
    else {
      var tmp = bucket
      while (tmp != null && tmp.key != key) {
        tmp = tmp.next
      }
      Option(tmp.value)
    }
  }

  override def iterator: Iterator[(K, V)] = buckets.flatMap(_.seq()).iterator
}
