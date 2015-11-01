package com.github.e8kor

import com.github.e8kor.nodes.states.{NoData, State}

import scala.language.{implicitConversions, postfixOps}

package object nodes {

  case class ComponentNode(name: String, state: State = NoData) {

    override def toString = {
      s"{$name, $state}"
    }

    override def equals(obj: Any) = {
      if ((obj isInstanceOf)[ComponentNode]) {
        val casted = (obj asInstanceOf)[ComponentNode]
        name equals (casted name)
      } else {
        false
      }
    }

  }


}
