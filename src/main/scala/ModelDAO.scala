package markschaake.models

import scala.collection.generic.GenericTraversableTemplate

trait Model[ID] {
  var id: Option[ID]
}

trait Store[ID, M <: Model[ID]] {
  def save(id: ID, m: M): ID
  def foreach(p: M => Unit): Unit
  def head: M
  def headOption: Option[M]
  def get(id: ID): Option[M]
  def findAll(): Iterable[M]
  def find(p: M => Boolean): Iterable[M]
  def findOne(p: M => Boolean): Option[M] = find(p).headOption
  def findById(id: ID): Option[M] = findOne(_.id.get == id)
  def remove(id: ID): Unit
  def remove(m: M): Unit
  def removeAll(): Unit
}

case class MapStore[ID, M <: Model[ID]]() extends Store[ID, M] {

  val repo = scala.collection.mutable.Map[ID, M]()

  def save(id: ID, m: M): ID = {
    repo(id) = m
    id
  }
  def foreach(p: M => Unit): Unit = repo.values.foreach(p)
  def head: M = repo.values.head
  def headOption: Option[M] = repo.values.headOption
  def get(id: ID): Option[M] = repo.get(id)
  def find(p: M => Boolean): Iterable[M] = repo.values.find(p)
  def findAll(): Iterable[M] = repo.values
  def remove(id: ID) { repo.remove(id) }
  def remove(m: M) { repo.remove(m.id.getOrElse(throw new Exception("id required for removal"))) }
  def removeAll() = repo.clear()
}

abstract class ModelDAO[ID, M <: Model[ID]] {

  val store: Store[ID, M]

  /**
   * Override to provide auto-increment like functionality
   */
  def nextId: Option[ID] = None

  defaultValues.foreach { save(_) }

  /**
   * Override to provide default values
   */
  def defaultValues: Seq[M] = Nil

  def save(m: M): ID = {
	if (m.id.isEmpty)
	  m.id = nextId
    m.id map { id =>
      println("going to save with id [%s]".format(id.toString))
      store.save(id, m)
      println("saved with id [%s]".format(id.toString))
      id
    } getOrElse {
      throw new Exception("id required. Consider overriding `nextId`")
    }
  }

  def save(ms: Traversable[M]) { ms.foreach { save } }

  def foreach(p: M => Unit): Unit = store.foreach(p)
  def head: M = store.head
  def headOption: Option[M] = store.headOption
  def get(id: ID): Option[M] = store.get(id)
  def find(p: M => Boolean): Iterable[M] = store.find(p)
  def findAll(): Iterable[M] = store.findAll
  def findOne(p: M => Boolean): Option[M] = find(p).headOption
  def remove(m: M) { store.remove(m) }
  def removeAll() { store.removeAll() }
}

class ModelMapDAO[ID, M <: Model[ID]] extends ModelDAO[ID, M] {
  override val store: Store[ID, M] = MapStore[ID, M]()
}
