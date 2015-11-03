
import com.github.e8kor.states._
import rx._
import rx.ops._

val derivedStateCaseA = Set[State] (NoData, Clear)
val derivedStateCaseB = Set[State] (Warning, Alert)

val edges = Var ((Set empty)[Edge] )
val nodes = Var ((Set empty)[Node2] )
val nodesHistory = Var (List[(Node2, Node2)] () )
val edgesReducer = edges reduce {
(oldEdges, newEdges) =>
(oldEdges diff newEdges) foreach {
case Edge (removeFrom, removeTo) =>
println (s"removing edges, from $removeFrom edge $removeTo")
(removeFrom dependsOn) () = (removeFrom dependsOn () ) filterNot (node => (node name) equals (removeTo name) )
}

(newEdges diff oldEdges) foreach {
case edge@Edge (addFrom, addTo) =>

val notExistsInNode = {
node: Node2 =>
! (nodes () map (_ name) exists ((node name) equals) )
}

edge setOfNodes () filter notExistsInNode map {
value =>
println (s"appending node: $value")
nodes () + value
}

println (s"appending dependent node add $addTo to node $addFrom")
addFrom dependsOn () = (addFrom dependsOn) () + addTo
}

newEdges
}
val nodesReducer = nodes reduce {
(oldSet, newSet) =>

val delta = for {
source <- oldSet diff newSet headOption

target <- newSet diff oldSet headOption

} yield {
source -> target
}

delta foreach {
value =>

println (s"added history delta: $value")

nodesHistory () = nodesHistory () :+ value
}

newSet
}

case class Edge(from: Node2, to: Node2) {
  require((from name) ne (to name), " node can not point to itself")

  override def toString = {
    setOfNodes() mkString("{", " => ", "}")
  }

  def setOfNodes() = {
    Set(from, to)
  }

}

case class Node2(
                  name: String,
                  checks: Var[Map[String, State]] = Var(Map[String, State]()),
                  dependsOn: Var[Set[Node2]] = Var(Set[Node2]())
                ) {
  val own = Rx {
    val state = (((checks() values) toSeq) sortBy (_ priority) lastOption) getOrElse NoData
    println(s"own state of $name updated to $state")
    state
  }
  val derived = Rx {
    val state = if (derivedStateCaseA contains own()) {
      Clear
    } else {
      val interestedEdges = dependsOn() collect {
        case node if derivedStateCaseB contains (node own()) =>
          node own()
      } toSeq

      val derivedState = interestedEdges sortBy (_ priority) lastOption

      derivedState getOrElse NoData
    }

    println(s"derived state of $name updated to $state")

    state
  }

  override def toString = {
    s"{name: $name; own: ${own()}; derived: ${derived()}}"
  }

}


val app = Node2 ("App")
(app checks) () = (app checks () ) + ("CPU" -> Warning) + ("Mem" -> Clear)

val db = Node2 ("DB")
(db checks) () = (db checks () ) + ("CPU" -> Clear) + ("Mem" -> Clear)

edges () = edges () + Edge (app, db)

app toString

db toString

Obs (app own) {
println ("triggered")
}
