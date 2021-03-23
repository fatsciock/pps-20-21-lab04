package u04lab.code

import Optionals._
import Lists._

import scala.util.Random

trait PowerIterator[A] {
  def next(): Option[A]
  def allSoFar(): List[A]
  def reversed(): PowerIterator[A]
}

trait PowerIteratorsFactory {

  def incremental(start: Int, successive: Int => Int): PowerIterator[Int]
  def fromList[A](list: List[A])
  def randomBooleans(size: Int): PowerIterator[Boolean]
}

class PowerIteratorsFactoryImpl extends PowerIteratorsFactory {

  import Streams._

  def fromStream[A](stream: Stream[A]): PowerIterator[A] = {

    class PowerIteratorImpl[A]() extends PowerIterator[A] {

      import List._
      import Streams.Stream._

      private var elements: List[A] = nil
      private var index: Int = 1

      override def next(): Option[A] = {
        val el = reverse(toList(take(stream)(index)))(0)
        index += 1
        elements = append(elements, List.Cons(el, Nil()))
        Option.of(el)
      }

      override def allSoFar(): List[A] = {
        elements
      }

      override def reversed(): PowerIterator[A] = {
        fromStream(Stream.generate(reverse(toList(stream))))
      }
    }
  }

  override def incremental(start: Int, successive: Int => Int): PowerIterator[Int] = {
    fromStream[Int](Stream.generate[Int](successive(start)))
  }

  override def fromList[A](list: List[A]): Unit = {
    fromStream[A](Stream.generate(list))
  }

  override def randomBooleans(size: Int): PowerIterator[Boolean] = {
    fromStream[Boolean](Stream.generate(Random.nextBoolean()))
  }
}
