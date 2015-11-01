import com.github.e8kor.nodes.states.Clear
import com.github.e8kor.nodes.{ComponentNode}
import com.github.e8kor.util.CommonGraph

import scala.collection.immutable.{TreeMap, TreeSet}
import scala.collection.mutable
import scala.language.postfixOps
import scalax.collection.GraphEdge._
import scalax.collection.GraphPredef._
import scalax.collection.immutable.Graph

val appNode1 = ComponentNode("App1")
val dbNode1 = ComponentNode("DB1")
val mqNode = ComponentNode("MQ")
val appNode2 = ComponentNode("App2")
val dbNode2 = ComponentNode("DB2")
val graph: Graph[ComponentNode, DiEdge] = Graph from(Set(appNode1, appNode2, mqNode, dbNode1, dbNode2), Set(appNode1 ~> dbNode1, appNode1 ~> mqNode, appNode2 ~> dbNode2))

val wrapperGraph = new CommonGraph(graph)

wrapperGraph neighbors appNode1

val a = Graph (ComponentNode("1") ~> ComponentNode("2"), ComponentNode("2") ~> ComponentNode("3"))

a mkString ", "

val b = a + ComponentNode("2", Clear)

b mkString ", "

val c = b

val f = Graph(1 ~> 2, 2 ~> 3, 4 ~> 3, 5 ~ 6)

f mkString ", "

Graph.from(edges = f.edges.map(_.toEdgeIn).map {
  case u@(a ~> b) if a == 2 =>
    15 ~> b
  case u@(a ~> b) if b == 2 =>
    a ~> 16
  case default => default

}) mkString ", "


val tr = TreeMap.empty[Int, String]

tr + (1 -> "A", 1 -> "B")

val rh = new mutable.RevertibleHistory[]
