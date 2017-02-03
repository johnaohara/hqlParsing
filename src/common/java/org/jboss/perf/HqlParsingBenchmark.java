package org.jboss.perf;

import org.jboss.perf.model.Person;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class HqlParsingBenchmark extends BenchmarkBase<Person> {


   @Benchmark
   public Query simpleSelect(HqlParsingBenchmarkState benchmarkState) {

      return parseQuery( "SELECT p FROM Person p", benchmarkState );

   }


   @Benchmark
   public Query simpleWhere(HqlParsingBenchmarkState benchmarkState) {
      return parseQuery( "SELECT p FROM Person p where p.firstName = ''", benchmarkState );

   }


   private Query parseQuery(String query, HqlParsingBenchmarkState benchmarkState) {

      return benchmarkState.entityManager.createQuery( query );

   }

   @State(Scope.Benchmark)
   public static class HqlParsingBenchmarkState extends BenchmarkBase.BenchmarkState<Person> {

      private EntityManager entityManager;

      @Setup
      public void setupAttributes() {
         entityManager = entityManagerFactory.createEntityManager();
      }

   }

   public static void main(String[] args) throws RunnerException {

      Options opt = new OptionsBuilder()
         .include( HqlParsingBenchmark.class.getSimpleName() )
         .warmupIterations( 5 )
         .measurementIterations( 100 )
         .forks( 1 )
         .jvmArgs(
            "-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005",
            "-Djava.net.preferIPv4Stack=true",
            "-Dcom.arjuna.ats.arjuna.common.propertiesFile=default-jbossts-properties.xml" )
         //.threads(5)
         .build();

      new Runner( opt ).run();
   }

}