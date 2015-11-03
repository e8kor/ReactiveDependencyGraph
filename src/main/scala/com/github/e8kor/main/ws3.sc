
import com.github.e8kor.states._
import rx._

import scala.language.postfixOps

val dbChecks = Var(Map[String, State]() + ("CPU" -> Clear) + ("Mem" -> Clear))
val db2 = mkNode("DB", dbChecks)

val appChecks = Var(Map[String, State]() + ("CPU" -> Clear) + ("Mem" -> Clear))
val app2 = mkNode("App", appChecks, Rx {
  Set(db2)
})

def mkNode(
            nodeName: String,
            checks: Var[Map[String, State]] = Var(Map[String, State]()),
            dependsOn: Rx[Set[Rx[State]]] = Rx {
              Set.empty[Rx[State]]
            }
          ): Rx[State] = {

  val own = Rx {

    val state = {
      val values = (checks() values) toSeq

      ((values sorted) lastOption) getOrElse NoData
    }

    println(s"own state of $nodeName updated to $state")

    state
  }

  val aggregated = Rx {

    val states = (dependsOn() map (entry => entry()) collect {
      case state: CanPropagate =>
        state
    } toSeq) sorted

    states foreach {
      state =>
        println(s"derived state of $nodeName updated to $state")
    }

    val propagated = states lastOption

    propagated foreach {
      state =>
        println(s"chosen propagated state is $state")
    }

    propagated getOrElse NoData
  }

  Rx(name = nodeName)({
    DerivedState(own(), aggregated())
  })

}

dbChecks() = dbChecks() + ("CPU" -> Warning)