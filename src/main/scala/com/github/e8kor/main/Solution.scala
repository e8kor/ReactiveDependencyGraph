package com.github.e8kor.main

import com.github.e8kor.states._
import rx._

import scala.language.postfixOps

/**
  * dummy object to hold function
  */
object Solution {

  /**
    * Function is responsible for creation of nodes.
    *
    * @param nodeName name for node output point, useful for debugging and logging
    * @param checks represent characteristics user found valuable
    * @param dependsOn describes relations of current node, require to calculate derived state
    * @return current state of node
    */
  def mkNode(
              nodeName: String,
              checks: Var[Map[String, State]] = Var(Map[String, State]()),
              dependsOn: Rx[Set[Rx[State]]] = Var((Set empty)[Rx[State]])
            ): Rx[State] = {

    val own = Rx {

      val state = {
        val values = (checks() values) toSeq

        println(s"possible own states: ${values mkString ", "}")

        ((values sorted) lastOption) getOrElse NoData
      }

      println(s"picked own state: $state")

      state
    }

    val aggregated = Rx {

      val states = ((dependsOn() map (entry => entry()) collect {
        case state: CanPropagate =>
          state
      } toSeq) sorted)[State]

      println(s"possible derived states: ${states mkString ", "}")

      val propagated = states lastOption

      propagated foreach {
        state =>
          println(s"chosen propagated state is $state")
      }

      propagated getOrElse NoData
    }

    Rx{
      DerivedState(own(), aggregated())
    }

  }

}
