import com.github.e8kor.states._
import org.scalacheck.Prop.forAll
import org.scalacheck.{Arbitrary, Gen, Properties}
import rx.core.{Rx, Var}

object CheckStateSpecification extends Properties("CheckStates") {

  import com.github.e8kor.main.Solution.mkNode

  val checkStateGenerator: Gen[State] = Gen oneOf(Clear, NoData, Alert, Warning, Check)

  implicit val arb2 = Arbitrary[State](checkStateGenerator)

  property("own state property") = forAll {
    (a: State, b: State) =>
      val checks = Var(Map("test" -> a, "test2" -> b))
      val node = mkNode("test", checks, Rx(Set.empty))

      println(s"expected state: ${Set(a, b) max}")

      (node() priority) equals ((Set(a, b) max) priority)
  }

  property("derived state property") = forAll({
    (a: State, b: State) =>

      val rootChecks = Var(Map[String, State]() + ("CPU" -> a))
      val root = mkNode("DB", rootChecks)

      val targetChecks = Var(Map[String, State]() + ("CPU" -> a))
      val target = mkNode("App", targetChecks, Rx {
        Set(root)
      })

      rootChecks() = rootChecks() + ("CPU" -> b)

      if (a equals Check) {
        (target() priority) equals (Check priority)
      } else {
        println(s"expected state: ${Set(a, b) max}")
        (target() priority) equals ((Set(a, b) max) priority)
      }
  })


}