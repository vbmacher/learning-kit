import scala.collection.{SortedMap, mutable}

trait Variable[T] {

  def domainSize(): Int

  def domain(): Domain[T]

  def isAssigned: Boolean

}

trait Domain[T] {

  def values(): Seq[T]

  def cloneDomain(): Domain[T]

}

trait Assignment[T] {

  def isComplete: Boolean = {
    false
  }

  def cloneSelf():Assignment[T] = {
    this
  }

  def done(): Unit

  def variables(): Seq[Variable[T]]

  def freeVariables(): SortedMap[Int, Variable[T]] = {
    variables().filterNot(_.isAssigned).foldLeft(new mutable.TreeMap[Int, Variable[T]]()) {
      (map, variable) => map.put(variable.domainSize(), variable); map
    }
  }

  def relatedFreeVariables(variable: Variable[T]): Seq[Variable[T]]

  def assign(variable: Variable[T], value: T): Option[Assignment[T]]


  def isConsistent: Boolean

}

case class Selection[T] (variable: Variable[T])

object CSP {

  private def selectVar[T](assignment: Assignment[T]):Option[Variable[T]] = {
    // ordered by size of block domain(most-constrained heuristics)
    assignment.freeVariables().headOption.map(p => p._2)
  }

  private def removeValues[T](assignment: Assignment[T], varX: Variable[T], varY: Variable[T]): Boolean = {
    var removed = false

    var newDomain = for {
      value <- varY.domain().values()
      tryAssignment <- assignment.assign(varY, value)
      if assignment.isConsistent
    } yield value



    val yBlock = yDomain(y.index)(y.blockIndex)
    val xBlock = xDomain(x.index)(x.blockIndex)
    val yStarts = new Nothing(yBlock.starts)
    for (yValue <- yStarts) {
      val gridClone = cloneGrid(grid)
      if (!assign(y, varY, gridClone, yValue)) {
        yBlock.starts.remove(yValue.asInstanceOf[Nothing])
        removed = true
        continue //todo: continue is not supported
      }
      val yValueIsValid = sequenceDoesNotOverlap(varY, yDomain, y.index) && sequenceContainsOnlyValidColors(y.isRow, gridClone, varY, yDomain, y.index)
      if (!yValueIsValid) {
        unassign(y, varY)
        yBlock.starts.remove(yValue.asInstanceOf[Nothing])
        removed = true
        continue //todo: continue is not supported
      }
      var found = false
      import scala.collection.JavaConversions._
      for (xValue <- xBlock.starts) {
        val gridCloneSecond = cloneGrid(gridClone)
        if (!assign(x, varX, gridCloneSecond, xValue)) continue //todo: continue is not supported
        val xValid = sequenceDoesNotOverlap(varX, xDomain, x.index) && sequenceContainsOnlyValidColors(x.isRow, gridCloneSecond, varX, xDomain, x.index) && sequenceContainsOnlyValidColors(y.isRow, gridCloneSecond, varY, yDomain, y.index)
        unassign(x, varX)
        if (xValid) {
          found = true
          break //todo: break is not supported
        }
      }
      if (!found) {
        yBlock.starts.remove(yValue.asInstanceOf[Nothing])
        removed = true
      }
      unassign(y, varY)
    }
    removed
  }


  private def ac3[T](assignment: Assignment[T]) = {
    val queue = new mutable.Queue[Variable[T]]()

    queue ++= assignment.freeVariables().values

    while (queue.nonEmpty) {
      val sel = queue.dequeue()

      for (relatedSel <-assignment.relatedFreeVariables(sel)) {
        if (removeValues(sel, relatedSel)) {
          if (transposedDomain(relatedSel.index)(relatedSel.blockIndex).starts.isEmpty) return false
          queue += relatedSel
        }
      }
    }
    true
  }

  private def forwardChecking(checkRowVar: Boolean, grid: Array[Array[Nothing]], varRow: Array[Array[Int]], varCol: Array[Array[Int]], domainRow: Array[Array[CSPMain.Block]], domainCol: Array[Array[CSPMain.Block]]) = {
    val `var` = if (checkRowVar) varRow
    else varCol
    val transposedVar = if (checkRowVar) varCol
    else varRow
    val domain = if (checkRowVar) domainRow
    else domainCol
    val transposedDomain = if (checkRowVar) domainCol
    else domainRow
    import scala.collection.JavaConversions._
    for (sel <- findFreeVars(checkRowVar, `var`, domain).values) {
      val validStarts = new Nothing
      val block = domain(sel.index)(sel.blockIndex)
      import scala.collection.JavaConversions._
      for (value <- block.starts) {
        val gridClone = cloneGrid(grid)
        if (!assign(sel, `var`, gridClone, value)) continue //todo: continue is not supported
        var isValid = sequenceDoesNotOverlap(`var`, domain, sel.index) && sequenceContainsOnlyValidColors(checkRowVar, gridClone, `var`, domain, sel.index)
        var k = value
        while ( {
          isValid && k < value + block.length
        }) {
          isValid = sequenceContainsOnlyValidColors(!checkRowVar, gridClone, transposedVar, transposedDomain, k)

          {
            k += 1; k - 1
          }
        }
        if (isValid) validStarts.add(value)
        unassign(sel, `var`)
      }
      domain(sel.index)(sel.blockIndex).starts.clear
      domain(sel.index)(sel.blockIndex).starts.addAll(validStarts)
      if (validStarts.isEmpty) { // if domain is empty, do not continue
        return false
      }
    }
    true
  }


  def solve(assignment: Assignment): Boolean = {
    if (assignment.isComplete) {
      assignment.done()
      return true
    }

    val assignmentClone = assignment.cloneSelf()

    if (!ac3(assignmentClone)) return false

    val sel = selectVar(assignmentClone)

    val `var` = if (sel.isRow) varRow
    else varCol
    val domain = if (sel.isRow) domainRowClone
    else domainColClone
    val block = domain(sel.index)(sel.blockIndex)

    var foundAtLeastOne = false
    for (value <- block.starts) {
      val gridBackup = cloneGrid(grid)
      if (!assign(sel, `var`, grid, value)) continue //todo: continue is not supported
      val domainRowBackup = cloneDomain(domainRowClone)
      val domainColBackup = cloneDomain(domainColClone)
      var isValid = forwardChecking(false, grid, varRow, varCol, domainRowBackup, domainColBackup)
      isValid = isValid && forwardChecking(true, grid, varRow, varCol, domainRowBackup, domainColBackup)
      if (isValid) foundAtLeastOne = foundAtLeastOne || csp(grid, varRow, varCol, domainRowBackup, domainColBackup)
      unassign(sel, `var`)
      grid = gridBackup
    }

    foundAtLeastOne

  }


}
