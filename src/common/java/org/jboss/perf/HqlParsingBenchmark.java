package org.jboss.perf;

import org.jboss.perf.model.Person;
import org.openjdk.jmh.annotations.Benchmark;

import javax.persistence.Query;

public class HqlParsingBenchmark extends BenchmarkBase<Person> {

   @Benchmark
   public Query simpleSelect(BenchmarkState benchmarkState) {
      return parseQuery( "SELECT p FROM Person p", benchmarkState );
   }

   @Benchmark
   public Query simpleWhere(BenchmarkState benchmarkState) {
      return parseQuery( "SELECT p FROM Person p where p.firstName = ''", benchmarkState );
   }

   private Query parseQuery(String query, BenchmarkState benchmarkState) {
      return benchmarkState.entityManager.createQuery( query );
   }
}