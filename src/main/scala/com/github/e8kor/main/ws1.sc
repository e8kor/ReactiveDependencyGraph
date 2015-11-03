

import scala.language.postfixOps
import scalax.collection.GraphEdge._
import scalax.collection.GraphPredef._
import scalax.collection.immutable.Graph

sealed trait State {

  def priority: Int

}
case object NoData extends State {

  override def toString = {
    s"{state: NoData; priority: $priority}"
  }

  override def priority: Int = 1

}

case object Clear extends State {

  override def toString = {
    s"{state: Clear; priority: $priority}"
  }

  override def priority: Int = 2

}

case object Warning extends State {

  override def toString = {
    s"{state: Warning; priority: $priority}"
  }

  override def priority: Int = 3

}

case object Alert extends State {

  override def toString = {
    s"{state: Alert; priority: $priority}"
  }

  override def priority: Int = 4

}


case object Check extends State {

  override def toString = {
    s"{state: Check; priority: $priority}"
  }

  override def priority: Int = 5

}

case class ComponentNode(name: String, state: State = NoData) {

  override def toString = {
    s"{$name:$state}"
  }

  override def equals(obj: Any) = {
    if ((obj isInstanceOf)[ComponentNode]) {
      val casted = (obj asInstanceOf)[ComponentNode]
      name equals (casted name)
    } else {
      false
    }
  }

  override def hashCode(): Int = name.hashCode()

}

case class DerivedStateVersion1(state: State) extends State {

  def priority: Int = state priority

}

class CommonGraph private(
                           private val history: Seq[Graph[ComponentNode, DiEdge]] = Seq[Graph[ComponentNode, DiEdge]]()
                         ) {

  def state(node: ComponentNode): State = {

    val list = neighbors(node)

    val states = list map {
      case value if value == node => value state
      case value if (value != node) && ((value state) == Check) => DerivedStateVersion1(NoData)
      case value if (value != node) && ((value state) != Check) => DerivedStateVersion1(value state)
    } toSeq

    (states sortBy (state => state priority) lastOption) getOrElse NoData
  }

  def neighbors(node: ComponentNode): Set[ComponentNode] = {

    it find node map {
      value =>
        val result = (value neighbors) map (_ value)
        result + (value value)
    } getOrElse (Set empty)[ComponentNode]

  }

  def updateState(node: ComponentNode, state: State) = {

    val source = node

    val target = node copy (state = state)

    val edges: collection.Set[DiEdge[ComponentNode]] = ((it edges) toEdgeInSet) map {
      case (f ~> t) if f equals source => target ~> t
      case (f ~> t) if t equals source => f ~> target
      case default => default
    }

    new CommonGraph(history :+ (Graph from (edges = edges)))
  }

  def append(newEdges: DiEdge[ComponentNode]*) = {

    val edges = it match {
      case CommonGraph(oldEdges) => oldEdges ++ newEdges
      case default => newEdges
    }

    new CommonGraph(history :+ (Graph from (edges = edges)))
  }

  def exclude(newEdges: DiEdge[ComponentNode]*) = {

    val edges = it match {
      case CommonGraph(oldEdges) => oldEdges filterNot (newEdges contains)
      case default => newEdges
    }

    new CommonGraph(history :+ (Graph from (edges = edges)))
  }

  override def toString = {
    it mkString ", "
  }

  private def it: Graph[ComponentNode, DiEdge] = history head

}

object CommonGraph {

  def apply(edges: DiEdge[ComponentNode]*) = {

    new CommonGraph(Seq[Graph[ComponentNode, DiEdge]](Graph from (edges = edges)))

  }

  def unapply(commonGraph: CommonGraph): Option[collection.Set[DiEdge[ComponentNode]]] = {

    Option(commonGraph) map {
      graph =>
        ((graph it) edges) toEdgeInSet
    }

  }

}
