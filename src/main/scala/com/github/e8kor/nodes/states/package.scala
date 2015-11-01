package com.github.e8kor.nodes

import scala.language.postfixOps

package object states {

  sealed trait State {

    def priority: Int

  }

  case class DerivedState(state: State) extends State {

    def priority: Int = state priority

  }

  case object NoData extends State {

    override def priority: Int = 1

  }

  case object Clear extends State {

    override def priority: Int = 2

  }

  case object Warning extends State {

    override def priority: Int = 3

  }

  case object Alert extends State {

    override def priority: Int = 4

  }

  case object Check extends State {

    override def priority: Int = 5

  }

}
