package org.jboss.perf;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public abstract class BenchmarkBase<T> {
   private static final Map<String, String> NON_CACHED_PROPERTIES = new HashMap<String, String>();
   private static final boolean PRINT_STACK_TRACES = Boolean.valueOf( System.getProperty( "printStackTraces", "true" ) );

   static {
      NON_CACHED_PROPERTIES.put( AvailableSettings.USE_SECOND_LEVEL_CACHE, "false" );
   }

   protected static void log(Throwable t) {
      if ( PRINT_STACK_TRACES ) t.printStackTrace();
   }

   @State(Scope.Benchmark)
   public static class BenchmarkState {

      protected SessionFactory sessionFactory;
      protected Session session;
      protected BenchmarkHQLParser hqlParser;

      @Setup
      public void setup() throws Throwable {
         try {

            final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
               .configure() // configures settings from hibernate.cfg.xml
               .build();
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();

            session = sessionFactory.openSession();

            try {
               //TODO: determine benchmark to run from configuration

               Properties appProperties = readAppProperties();

               if ( appProperties.getProperty( "benchmark.type" ).equals( "parsing" ) )
                  hqlParser = HqlParserFactory.buildHqlParser();
               else if ( appProperties.getProperty( "benchmark.type" ).equals( "interpreter" ) )
                  hqlParser = HqlParserFactory.buildHqlInterpreter();
               else {
                  //todo:: better exception handling here
                  throw new RuntimeException( "benchmark.type unknown: " + appProperties.getProperty( "benchmark.type" ) );
               }

               System.out.println("Running benchmark with: " + hqlParser.getClass().getName());
            } catch (IllegalAccessException e) {
               e.printStackTrace();
            } catch (InstantiationException e) {
               e.printStackTrace();
            }
            hqlParser.configure( this.sessionFactory.getSessionFactory() );

         } catch (Throwable t) {
            t.printStackTrace();
            log( t );
            throw t;
         }
      }

      private Properties readAppProperties() {
         Properties prop = new Properties();
         try (
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream( "app.properties" )) {

            prop.load( inputStream );

         } catch (FileNotFoundException e) {
            e.printStackTrace( System.out );
         } catch (IOException e) {
            e.printStackTrace( System.out );
         }
         return prop;
      }

      @TearDown
      public void shutdown() throws Throwable {
         try {
            session.close();
            sessionFactory.close();
         } catch (Throwable t) {
            log( t );
            throw t;
         }
      }

   }

}
