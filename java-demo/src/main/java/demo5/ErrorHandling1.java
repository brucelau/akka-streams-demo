package demo5;

import java.util.stream.IntStream;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.runtime.BoxedUnit;
import akka.actor.ActorSystem;
import akka.stream.ActorFlowMaterializer;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;

public class ErrorHandling1 {
  public static void main(String[] args) throws Exception {
    ActorSystem system = ActorSystem.create("demo5");
    ActorFlowMaterializer materializer = ActorFlowMaterializer.create(system);

    // // Here is a stream that will complete with a failure because of a division by zero
    IntStream stream = IntStream.range(0, 5);
    Source<Integer, BoxedUnit> source = Source.from(() -> stream.iterator()).map(x -> 100 / x);
    Future<Integer> result = source.runWith(Sink.fold(0, (x, y) -> x + y), materializer);
    Integer res = Await.result(result, Duration.Inf());

    System.out.println(res); // this will print an ArithmeticException
  }
}
