package org.jboss.perf;

import com.mockrunner.jdbc.PreparedStatementResultSetHandler;
import com.mockrunner.mock.jdbc.MockResultSet;
import org.hibernate.Version;
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
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class HqlParsingBenchmark extends BenchmarkBase<Person> {


   @Benchmark
   public Query testMethod(HqlParsingBenchmarkState benchmarkState, BenchmarkBase.ThreadState threadState, Blackhole blackhole) {
      // This is a demo/sample template for building your JMH benchmarks. Edit as needed.
      // Put your benchmark code here.
      EntityManager entityManager;

      entityManager = benchmarkState.entityManagerFactory.createEntityManager();

      Query query = entityManager.createQuery( "SELECT p FROM Person p" ) ;

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

         handler.prepareUpdateCount( "insert into Person \\(firstName, lastName, middleName, id\\) values \\(\\?, \\?, \\?, \\?\\)", 1 );
         // for Hibernate 4.x
         handler.prepareGeneratedKeys( "insert into Person \\(id, firstName, lastName, middleName\\) values \\(null, \\?, \\?, \\?\\)", getIncrementing( dbSize ) );

         MockResultSet readPerson = handler.createResultSet();
         readPerson.addColumn( "id1_8_0_", Collections.singletonList( 1L ) );
         readPerson.addColumn( "firstNam2_8_0_", Collections.singletonList( "firstName" ) );
         readPerson.addColumn( "lastName3_8_0_", Collections.singletonList( "lastName" ) );
         readPerson.addColumn( "middleNa4_8_0_", Collections.singletonList( "middleName" ) );
         handler.prepareResultSet( "select person0_\\.id as id1_8_0_, person0_\\.firstName as firstNam2_8_0_, person0_\\.lastName as lastName3_8_0_, person0_\\.middleName as middleNa4_8_0_ from Person person0_ where person0_\\.id=\\?", readPerson );

         handler.prepareUpdateCount( "update Person set firstName=\\?, lastName=\\?, middleName=\\? where id=\\?", 1 );

         handler.prepareUpdateCount( "delete from Person where id=\\?", 1 );

         MockResultSet readPersons = handler.createResultSet();
         readPersons.addColumn( "id1_8_", seq( 0, transactionSize ) );
         readPersons.addColumn( "firstNam2_8_", list( transactionSize, "firstName" ) );
         readPersons.addColumn( "lastName3_8_", list( transactionSize, "lastName" ) );
         readPersons.addColumn( "middleNa4_8_", list( transactionSize, "middleName" ) );
         handler.prepareResultSet( "select person0_\\.id as id1_8_, person0_\\.firstName as firstNam2_8_, person0_\\.lastName as lastName3_8_, person0_\\.middleName as middleNa4_8_ from Person person0_ where person0_\\.id in \\(.*\\)", readPersons );

         handler.prepareResultSet( "select person0_\\.id as id1_8_, person0_\\.firstName as firstNam2_8_, person0_\\.lastName as lastName3_8_, person0_\\.middleName as middleNa4_8_ from Person person0_ where person0_\\.firstName like \\?", readPersons );
         handler.prepareResultSet( "select person0_\\.id as id1_8_, person0_\\.firstName as firstNam2_8_, person0_\\.lastName as lastName3_8_, person0_\\.middleName as middleNa4_8_ from Person person0_ where \\(person0_\\.firstName like \\?\\) and \\(person0_\\.middleName like \\?\\)", readPersons );
         handler.prepareResultSet( "select person0_\\.id as id1_8_, person0_\\.firstName as firstNam2_8_, person0_\\.lastName as lastName3_8_, person0_\\.middleName as middleNa4_8_ from Person person0_ where \\(person0_\\.firstName like \\?\\) and \\(person0_\\.middleName like \\?\\) and \\(person0_.lastName like \\?\\)", readPersons );

         handler.prepareUpdateCount( "delete from Person where id in \\(.*\\)", transactionSize );
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
         return null;
      }

      @Override
      public void modify(Person entity, ThreadLocalRandom random) {

      }

   }

   public static void main(String[] args) throws RunnerException {

      Options opt = new OptionsBuilder()
         .include(HqlParsingBenchmark.class.getSimpleName())
         .warmupIterations(5)
         .measurementIterations(100)
         .forks(1)
         .jvmArgs(
//            "-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005",
            "-Djava.net.preferIPv4Stack=true",
            "-Dcom.arjuna.ats.arjuna.common.propertiesFile=default-jbossts-properties.xml")
         //.threads(5)
         .build();

      new Runner(opt).run();
   }

}
