package org.jboss.perf;

import com.mockrunner.jdbc.PreparedStatementResultSetHandler;
import com.mockrunner.mock.jdbc.MockResultSet;
import org.jboss.perf.model.Person;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.perfmock.PerfMockDriver;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.metamodel.SingularAttribute;
import java.util.concurrent.ThreadLocalRandom;

public class HqlParsingBenchmark extends BenchmarkBase<Person> {


   @Benchmark
   public Query simpleSelect(HqlParsingBenchmarkState benchmarkState, BenchmarkBase.ThreadState threadState, Blackhole blackhole) {
      // This is a demo/sample template for building your JMH benchmarks. Edit as needed.
      // Put your benchmark code here.
      EntityManager entityManager;

      entityManager = benchmarkState.entityManagerFactory.createEntityManager();

      Query query = entityManager.createQuery( "SELECT p FROM Person p" ) ;

      return query;

   }


   @Benchmark
   public Query simpleWhere(HqlParsingBenchmarkState benchmarkState, BenchmarkBase.ThreadState threadState, Blackhole blackhole) {
      // This is a demo/sample template for building your JMH benchmarks. Edit as needed.
      // Put your benchmark code here.
      EntityManager entityManager;

      entityManager = benchmarkState.entityManagerFactory.createEntityManager();

      Query query = entityManager.createQuery( "SELECT p FROM Person p where p.firstName = ''" ) ;

      return query;

   }

   @State(Scope.Benchmark)
   public static class HqlParsingBenchmarkState extends BenchmarkBase.BenchmarkState<Person> {

      private SingularAttribute<Person, String> firstName;
      private SingularAttribute<Person, String> middleName;
      private SingularAttribute<Person, String> lastName;

      @Setup
      public void setupAttributes() {
         firstName = (SingularAttribute<Person, String>) getEntityManagerFactory().getMetamodel().entity( Person.class ).getSingularAttribute( "firstName" );
         middleName = (SingularAttribute<Person, String>) getEntityManagerFactory().getMetamodel().entity( Person.class ).getSingularAttribute( "middleName" );
         lastName = (SingularAttribute<Person, String>) getEntityManagerFactory().getMetamodel().entity( Person.class ).getSingularAttribute( "lastName" );
      }

      @Override
      public void setupMock() {
         super.setupMock();
         PreparedStatementResultSetHandler handler = PerfMockDriver.getInstance().getPreparedStatementHandler();

         MockResultSet all = handler.createResultSet();
         all.addColumn( "col_0_0_", seq( 0, dbSize ) );
         handler.prepareResultSet( "select person0_.id as col_0_0_ from Person person0_", all );
      }

      @Override
      public Class<Person> getClazz() {
         return Person.class;
      }

      @Override
      protected boolean hasForeignKeys() {
         return false;
      }

      @Override
      public Person randomEntity(ThreadLocalRandom random) {
         return new Person(Randomizer.randomString(2, 12, random), Randomizer.randomString(2, 12, random), Randomizer.randomString(6, 12, random));

      }

   }

   public static void main(String[] args) throws RunnerException {

      Options opt = new OptionsBuilder()
         .include(HqlParsingBenchmark.class.getSimpleName())
         .warmupIterations(5)
         .measurementIterations(100)
         .forks(1)
         .jvmArgs(
            "-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005",
            "-Djava.net.preferIPv4Stack=true",
            "-Dcom.arjuna.ats.arjuna.common.propertiesFile=default-jbossts-properties.xml")
         //.threads(5)
         .build();

      new Runner(opt).run();
   }

}
