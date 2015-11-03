
import com.github.e8kor.states._
import rx._

import scala.collection.immutable.Stack
import scala.language.postfixOps


import com.github.e8kor.main.Solution.mkNode

// creating entry point to db node
val dbChecks = Var(Map[String, State]() + ("CPU" -> NoData) + ("Mem" -> NoData))

// creating db node
val db2 = mkNode("DB", dbChecks)

// creating entry point to app node
val appChecks = Var(Map[String, State]() + ("CPU" -> NoData) + ("Mem" -> NoData))

// creating app node
val app2 = mkNode("App", appChecks, Rx {
  Set(db2)
})

/**
  * Suitable data structure for keeping updates
  * Rx[ Map[ String,State ] ] - entry point to graph node
  * (String,(State, State)) - check name -> (state was, state become)
  */
val history = Var(Stack[(Rx[Map[String,State]], (String,(State, State)))])()

// touching db node
dbChecks() = dbChecks() + ("CPU" -> Warning)

db2()

app2()

// touching app node
appChecks()= appChecks() + ("CPU" -> Warning)

app2()
