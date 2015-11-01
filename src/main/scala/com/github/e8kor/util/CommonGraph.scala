package com.github.e8kor.util

import com.github.e8kor.nodes.ComponentNode
import com.github.e8kor.nodes.states.{Check, DerivedState, NoData, State}

import scala.language.postfixOps
import scalax.collection.GraphEdge._
import scalax.collection.GraphPredef._
import scalax.collection.immutable.Graph

object CommonGraph {

  def apply(edges: DiEdge[ComponentNode]*) = {
    new CommonGraph(Graph from (edges = edges))
  }

  def unapply(commonGraph: CommonGraph): Option[collection.Set[DiEdge[ComponentNode]]] = {

    Option(commonGraph) map {
      value =>
        ((value it) edges) map (_ toEdgeIn)
    }
  }

}

class CommonGraph(
                   val it: Graph[ComponentNode, DiEdge],
                   private val history: Seq[Graph[ComponentNode, DiEdge]] = Seq[Graph[ComponentNode, DiEdge]]()
                 ) {

  def state(node: ComponentNode): State = {

    val states = neighbors(node) map {
      case value if value == node => value state
      case value if (value != node) && ((value state) == Check) => DerivedState(NoData)
      case value if (value != node) && ((value state) != Check) => DerivedState(value state)
    } toSeq

    (states sortBy (state => state priority) headOption) getOrElse NoData
  }

  def neighbors(node: ComponentNode): Set[ComponentNode] = {

    it find node map {
      value =>
        (value neighbors) map (_ value)
    } getOrElse (Set empty)[ComponentNode]

  }

  def updateState(node: ComponentNode, state: State) = {

    val source = node

    val target = node copy (state = state)

    val edges: collection.Set[DiEdge[ComponentNode]] = (it edges) map (_ toEdgeIn) map {
      case (f ~> t) if f equals source => target ~> t
      case (f ~> t) if t equals source => f ~> target
      case default => default
    }

    new CommonGraph(Graph from (edges = edges), history :+ it)
  }

  def append(newEdges: DiEdge[ComponentNode]*) = {

    val edges = it match {
      case CommonGraph(oldEdges) => oldEdges ++ newEdges
      case default => newEdges
    }

    new CommonGraph(Graph from (edges = edges), history :+ it)
  }

  def exclude(newEdges: DiEdge[ComponentNode]*) = {

    val edges = it match {
      case CommonGraph(oldEdges) => oldEdges filterNot (newEdges contains)
      case default => newEdges
    }

    new CommonGraph(Graph from (edges = edges), history :+ it)
  }

  override def toString = {
    it mkString ", "
  }

}
