import com.github.e8kor.nodes.ComponentNode
import com.github.e8kor.nodes.states.NoData
import com.github.e8kor.util.CommonGraph

import scalax.collection.GraphEdge._
import scalax.collection.GraphPredef._

def nd(name:String) = ComponentNode.apply(name, NoData)

val set: Set[DiEdge[ComponentNode]] = Set(nd("1") ~> nd("2"), nd("3") ~> nd("1"), nd("2") ~> nd("1"), nd("2") ~> nd("1"), nd("3") ~> nd("1"))

CommonGraph(nd("1") ~> nd("2"), nd("3") ~> nd("1"), nd("2") ~> nd("1"))