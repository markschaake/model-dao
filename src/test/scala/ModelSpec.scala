package markschaake.models

import org.specs2.mutable._
import org.specs2.runner.JUnitRunner

class ModelSpec extends Specification {
  "save" should {
    "foo" in {
      case class Foo(var id: Option[String], name: String) extends Model[String]
      object FooDAO extends ModelMapDAO[String, Foo]
      FooDAO.save(Foo(Some("1"), "one"))
      FooDAO.get("1") must_== Some(Foo(Some("1"), "one"))
    }
  }
}
