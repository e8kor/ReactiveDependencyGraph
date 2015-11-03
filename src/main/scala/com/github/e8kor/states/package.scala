package com.github.e8kor

import scala.language.postfixOps

package object states {

  implicit object StateOrder extends Ordering[State] {
    override def compare(x: State, y: State): Int = {
      (x priority) compare (y priority)
    }
  }

  sealed trait CanPropagate

  sealed trait State {

    def priority: Int

  }

  case class DerivedState(own:State, aggregated: State) extends State with CanPropagate {

    require(aggregated ne Check, s"aggregated state cannot be $Check")

    val derivedState = Set(own, aggregated) max

    def priority: Int = derivedState priority

    override def toString = {
      derivedState toString
    }

  }

  case object NoData extends State with CanPropagate{

    override def priority: Int = 1

    override def toString = {
      s"{state: NoData; priority: $priority}"
    }

  }

  case object Clear extends State with CanPropagate{

    override def priority: Int = 2

    override def toString = {
      s"{state: Clear; priority: $priority}"
    }

  }

  case object Warning extends State with CanPropagate {

    override def priority: Int = 3

    override def toString = {
      s"{state: Warning; priority: $priority}"
    }

  }

  case object Alert extends State with CanPropagate {

    override def priority: Int = 4

    override def toString = {
      s"{state: Alert; priority: $priority}"
    }

  }

  case object Check extends State {

    override def priority: Int = 5

    override def toString = {
      s"{state: Check; priority: $priority}"
    }

  }

}
