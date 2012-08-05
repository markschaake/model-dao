package markschaake.models

import scala.collection.generic.GenericTraversableTemplate

trait Model[ID] {

  def id: Option[ID]
}

trait Store[ID, M <: Model[ID]] {
  def save(id: ID, m: M): ID
  def foreach(p: M => Unit): Unit
  def head: M
  def headOption: Option[M]
  def get(id: ID): Option[M]
  def find(p: M => Boolean): Iterable[M]
  def findOne(p: M => Boolean): Option[M] = find(p).headOption
  def remove(m: M): Unit
  def removeAll(): Unit
}

case class MapStore[ID, M <: Model[ID]]() extends Store[ID, M] {
  protected val store = scala.collection.mutable.Map[ID, M]()
  def save(id: ID, m: M): ID = {
    store(id) = m
    id
  }
  def foreach(p: M => Unit): Unit = store.values.foreach(p)
  def head: M = store.values.head
  def headOption: Option[M] = store.values.headOption
  def get(id: ID): Option[M] = store.get(id)
  def find(p: M => Boolean): Iterable[M] = store.values.find(p)
  def remove(m: M) { store.remove(m.id.getOrElse(throw new Exception("id required for removal"))) }
  def removeAll() = store.clear()
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

  def save(m: M): ID = m.id.orElse(nextId) map { id => store.save(id, m) } getOrElse { throw new Exception("id required. Consider overriding `nextId`") }

  def save(ms: Iterable[M]) { ms.foreach { save } }

  def foreach(p: M => Unit): Unit = store.foreach(p)
  def head: M = store.head
  def headOption: Option[M] = store.headOption
  def get(id: ID): Option[M] = store.get(id)
  def find(p: M => Boolean): Iterable[M] = store.find(p)
  def findOne(p: M => Boolean): Option[M] = find(p).headOption
  def remove(m: M) { store.remove(m) }
  def removeAll() { store.removeAll() }
}

abstract class ModelMapDAO[ID, M <: Model[ID]] extends ModelDAO[ID, M] {
  override val store = MapStore[ID, M]()
}
