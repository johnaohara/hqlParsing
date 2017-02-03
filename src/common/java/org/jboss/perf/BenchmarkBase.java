package org.jboss.perf;


import com.mockrunner.jdbc.PreparedStatementResultSetHandler;
import com.mockrunner.jdbc.ResultSetFactory;
import com.mockrunner.mock.jdbc.MockResultSet;
import com.mockrunner.util.regexp.StartsEndsPatternMatcher;
import org.hibernate.cache.infinispan.InfinispanRegionFactory;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cfg.AvailableSettings;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.perfmock.FunctionalMockResultSet;
import org.perfmock.PerfMockDriver;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.OptimisticLockException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.PessimisticLockException;
import javax.persistence.SharedCacheMode;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;
import javax.transaction.Status;
import javax.transaction.TransactionManager;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;


public abstract class BenchmarkBase<T> {
   private static final Map<String, String> NON_CACHED_PROPERTIES = new HashMap<String, String>();
   private static final boolean PRINT_STACK_TRACES = Boolean.valueOf( System.getProperty( "printStackTraces", "true" ) );
   private static final boolean THROW_ON_LOCK_EXCEPTIONS = Boolean.valueOf( System.getProperty( "throwOnLockExceptions", "true" ) );
   private static final int HIBERNATE_VERSION;
   private static final Properties properties;
   private static final ClassLoader classloader;

   static {

      // horrible little hack
      // since ORM 5.2.0 Version.getVersionString no longer returns the version number
      // need to resolve from the maven pom.
      properties = new Properties();
      classloader = BenchmarkBase.class.getClassLoader();
      try {
         final InputStream stream = classloader.getResourceAsStream("app.properties");
         properties.load(stream);
      }
      catch (Exception e){
         e.printStackTrace();
      }

//      String[] hv = Version.getVersionString().replaceAll( "[^0-9.]", "" ).split( "\\." );
      String[] hv = properties.getProperty( "hibernate.version" ).replaceAll( "[^0-9.]", "" ).split( "\\." );
      HIBERNATE_VERSION = Integer.parseInt( hv[0] ) * 10000 + Integer.parseInt( hv[1] ) * 100 + Integer.parseInt( hv[2] );
      NON_CACHED_PROPERTIES.put( AvailableSettings.USE_SECOND_LEVEL_CACHE, "false" );
      PerfMockDriver.getInstance(); // make sure PerfMockDriver is classloaded
   }

   protected static final String C3P0 = "c3p0";
   protected static final String HIKARI = "hikari";
   protected static final String IRON_JACAMAR = "ironjacamar";

   //protected static Log log = LogFactory.getLog("Benchmark");

   protected static void trace(String msg) {
//        System.err.println(msg);
   }

   protected static void error(String msg, Throwable t) {
      //System.err.println(msg);
      //if (printStackTraces) t.printStackTrace();
   }

   protected static void log(Throwable t) {
      if ( PRINT_STACK_TRACES ) t.printStackTrace();
   }

   @State(Scope.Benchmark)
   public abstract static class BenchmarkState<T> {

      @Param(C3P0 + ".mock")
      String persistenceUnit;

      @Param("10000")
      int dbSize;

      @Param("100")
      int batchLoadSize;

      @Param("10")
      int transactionSize;

      @Param("none")
      String secondLevelCache;

      @Param("true")
      boolean useTx;

      @Param("true")
      boolean queryCache;

      @Param("true")
      boolean directReferenceEntries;

      @Param("false")
      boolean minimalPuts;

      @Param("false")
      boolean lazyLoadNoTrans;

      private JndiHelper jndiHelper = new JndiHelper();
      private JtaHelper jtaHelper = new JtaHelper();
      private JacamarHelper jacamarHelper = new JacamarHelper();
      protected EntityManagerFactory entityManagerFactory;
      // IDs of Persons that are commonly in the DB to make it big
      protected ArrayList<Long> regularIds;
      private PersistenceUnitUtil persistenceUnitUtil;
      private TransactionManager tm = com.arjuna.ats.jta.TransactionManager.transactionManager();
      private boolean managedTransaction;
      private SingularAttribute idProperty;

      protected EntityManagerFactory getEntityManagerFactory() {
         return entityManagerFactory;
      }


      @Setup
      public void setup() throws Throwable {
         if ( persistenceUnit.contains( "mock" ) ) {
            setupMock();
         }
         tm.setTransactionTimeout( 1200 );
         try {
            if ( persistenceUnit.startsWith( IRON_JACAMAR ) ) {
               jacamarHelper.start();
               managedTransaction = persistenceUnit.endsWith( ".xa" );
            } else {
               jndiHelper.start();
               jtaHelper.start();
            }
            if ( !secondLevelCache.equals( "none" ) && HIBERNATE_VERSION >= 50002 ) {
               // we always need managed transactions with 2LC since there are multiple participants
               managedTransaction = true;
            }
            entityManagerFactory = Persistence.createEntityManagerFactory( persistenceUnit,
               secondLevelCache.equals( "none" ) ? NON_CACHED_PROPERTIES : getSecondLevelCacheProperties() );
            persistenceUnitUtil = entityManagerFactory.getPersistenceUnitUtil();
            regularIds = new ArrayList<Long>( dbSize );
            Metamodel metamodel = entityManagerFactory.getMetamodel();
            EntityType entity = metamodel.entity( getClazz() );
            Set<SingularAttribute> singularAttributes = entity.getSingularAttributes();
            for (SingularAttribute singularAttribute : singularAttributes) {
               if ( singularAttribute.isId() ) {
                  idProperty = singularAttribute;
                  break;
               }
            }

            if ( persistenceUnit.contains( "mock" ) ) {
               PerfMockDriver.getInstance().setMocking( true );
            }
         } catch (Throwable t) {
            t.printStackTrace();
            log( t );
            throw t;
         }
      }

      public void setupMock() {
         PreparedStatementResultSetHandler handler = PerfMockDriver.getInstance().getPreparedStatementHandler();
         // case-sensitive comparison is more performant
         handler.setResultSetFactory( new ResultSetFactory.Default( true ) );
         // we need regexp for the select ... where x in ( ... )
         handler.setPatternMatcherFactory( new StartsEndsPatternMatcher.Factory() );

         handler.prepareResultSet( "call next value for hibernate_sequence", getIncrementing( dbSize ) );

         MockResultSet size = handler.createResultSet();
         size.addColumn( "col_0_0_" );
         size.addRow( Collections.singletonList( (long) dbSize ) );
         handler.prepareResultSet( "select count\\(.*\\) as col_0_0_ from .*", size );

         MockResultSet nonMatching = handler.createResultSet();
         // let's have one column, one row, but not matching to anything
         nonMatching.addColumn( ":-)", Collections.singletonList( null ) );
         handler.prepareGlobalResultSet( nonMatching );
      }

      protected MockResultSet getIncrementing(int initialValue) {
         MockResultSet newId = new FunctionalMockResultSet( "newId" );
         newId.setColumnsCaseSensitive( true );
         AtomicLong counter = new AtomicLong( initialValue );
         newId.addRow( Collections.singletonList( (Supplier) counter::getAndIncrement ) );
         return newId;
      }

      protected Map<String, String> getSecondLevelCacheProperties() {
         Map<String, String> l2Properties = new HashMap<>();
         if ( HIBERNATE_VERSION >= 50002 ) {
            // In Hibernate 5.0.2+ we don't need the transactions and can use Infinispan 8.0
            l2Properties.put( InfinispanRegionFactory.INFINISPAN_CONFIG_RESOURCE_PROP, "2lc-cfg-80.xml" );
         } else {
            l2Properties.put( InfinispanRegionFactory.INFINISPAN_CONFIG_RESOURCE_PROP, "2lc-cfg-71.xml" );
         }
         l2Properties.put( org.hibernate.cfg.AvailableSettings.JPA_SHARED_CACHE_MODE, SharedCacheMode.ALL.toString() );
         l2Properties.put( AvailableSettings.CACHE_REGION_FACTORY, InfinispanRegionFactory.class.getName() );

         l2Properties.put( AvailableSettings.USE_SECOND_LEVEL_CACHE, "true" );
         l2Properties.put( AvailableSettings.USE_QUERY_CACHE, String.valueOf( queryCache ) );
         l2Properties.put( AvailableSettings.USE_DIRECT_REFERENCE_CACHE_ENTRIES, String.valueOf( directReferenceEntries ) );
         l2Properties.put( AvailableSettings.USE_MINIMAL_PUTS, String.valueOf( minimalPuts ) );
         l2Properties.put( AvailableSettings.ENABLE_LAZY_LOAD_NO_TRANS, String.valueOf( lazyLoadNoTrans ) );
         AccessType defaultAccessType = null;
         switch (secondLevelCache) {
            case "tx":
               defaultAccessType = AccessType.TRANSACTIONAL;
               break;
            case "ro":
               defaultAccessType = AccessType.READ_ONLY;
               break;
            case "rw":
               defaultAccessType = AccessType.READ_WRITE;
               break;
            case "ns":
               defaultAccessType = AccessType.NONSTRICT_READ_WRITE;
               break;
         }
         if ( defaultAccessType != null ) {
            l2Properties.put( AvailableSettings.DEFAULT_CACHE_CONCURRENCY_STRATEGY, defaultAccessType.getExternalName() );
         }
         l2Properties.put( "hibernate.transaction.manager_lookup_class", "org.hibernate.transaction.JBossTransactionManagerLookup" );
         return l2Properties;
      }

      @Setup(Level.Iteration)
      public void refreshDB() throws Exception {
         EntityManager entityManager = entityManagerFactory.createEntityManager();
         try {
            long pre;
            beginTransaction( entityManager );
            try {
               pre = getSize( entityManager );
               System.out.printf( "Refreshing DB with %d entities\n", pre );

               CriteriaBuilder cb = entityManager.getCriteriaBuilder();
               CriteriaQuery<Long> query = cb.createQuery( Long.class );
               Root<T> root = query.from( getClazz() );
               query = query.select( root.<Long>get( idProperty ) );
               Predicate condition = getRootLevelCondition( cb, root );
               if ( condition != null ) {
                  query = query.where( condition );
               }
               regularIds.clear();
               regularIds.ensureCapacity( dbSize );

               List<Long> resultList = entityManager.createQuery( query ).getResultList();
               for (Long id : resultList) {
                  if ( regularIds.size() == dbSize ) break;
                  regularIds.add( id );
               }
               Collections.sort( regularIds );
               System.out.printf( "Registered %d existing entities\n", regularIds.size() );
            } catch (Exception e) {
               log( e );
               throw e;
            } finally {
               commitTransaction( entityManager );
            }
            if ( pre > dbSize ) {
               int deleted = delete( entityManager, regularIds );
               long post = getSize( entityManager );
               System.out.printf( "DB contained %d entities (%s), %d deleted, now %d\n", pre, getClazz().getSimpleName(), deleted, post );
               pre = post;
            }
            if ( pre < dbSize ) {
               int created = 0;
               boolean failed;
               do {
                  failed = false;
                  beginTransaction( entityManager );
                  try {
                     created = addRandomEntities( dbSize - regularIds.size(), ThreadLocalRandom.current(), entityManager );
                     commitTransaction( entityManager );
                  } catch (Exception e) {
                     if ( isLockException( e ) ) {
                        rollbackTransaction( entityManager );
                        failed = true;
                     } else {
                        log( e );
                        throw e;
                     }
                  }
               } while (failed);
               try {
                  long post = getSize( entityManager );
                  System.out.printf( "DB contained %d entries, %d created, now %d, ids %d\n", pre, created, post, regularIds.size() );
                  if ( regularIds.size() != post ) {
                     throw new IllegalStateException();
                  }
               } finally {
               }
            }
         } catch (Exception e) {
            log( e );
            throw e;
         } finally {
            entityManager.close();
         }
      }

      private int addRandomEntities(int numEntries, ThreadLocalRandom random, EntityManager entityManager) throws Exception {
         ArrayList<T> batchEntities = new ArrayList<T>( batchLoadSize );
         for (int i = 0; i < numEntries; i++) {
            T entity = randomEntity( random );
            trace( "Persisting entity " + entity );
            entityManager.persist( entity );
            batchEntities.add( entity );
            if ( (i + 1) % batchLoadSize == 0 ) {
               trace( "Flushing " + batchEntities.size() + " entities" );
               entityManager.flush();
               entityManager.clear();
               // let's commit the transaction in order not to timeout
               commitTransaction( entityManager );
               beginTransaction( entityManager );
               // add regularIds after successful commit
               for (T e : batchEntities) {
                  Long id = (Long) persistenceUnitUtil.getIdentifier( e );
                  regularIds.add( id );
               }
               batchEntities.clear();
            }
         }
         trace( "Flushing " + batchEntities.size() + " entities" );
         entityManager.flush();
         for (T e : batchEntities) {
            Long id = (Long) persistenceUnitUtil.getIdentifier( e );
            regularIds.add( id );
         }
         return numEntries;
      }

      public void beginTransaction(EntityManager entityManager) throws Exception {
         trace( "Transaction begin, state is " + tm.getStatus() );
         try {
            if ( managedTransaction ) {
               tm.begin();
               entityManager.joinTransaction();
            } else {
               entityManager.getTransaction().begin();
            }
            trace( "Transaction began, state is " + tm.getStatus() );
         } catch (Exception e) {
            error( "Failed starting TX", e );
            throw e;
         }
      }

      public void commitTransaction(EntityManager entityManager) throws Exception {
         trace( "Transaction commit, state is " + tm.getStatus() );
         try {
            if ( managedTransaction ) {
               tm.commit();
            } else {
               entityManager.getTransaction().commit();
            }
            trace( "Transaction commited, state is " + tm.getStatus() );
         } catch (Exception e) {
            error( "Failed committing TX", e );
            throw e;
         }
      }

      public void rollbackTransaction(EntityManager entityManager) throws Exception {
         trace( "Rolling back" );
         try {
            if ( managedTransaction ) {
               if ( tm.getStatus() != Status.STATUS_NO_TRANSACTION ) {
                  tm.rollback();
               }
            } else {
               EntityTransaction tx = entityManager.getTransaction();
               if ( tx.isActive() ) {
                  tx.rollback();
               }
            }
         } catch (Exception e) {
            error( "Failed rolling back TX", e );
            throw e;
         }
      }


      private int delete(EntityManager entityManager, Collection<Long> allowedIds) throws Exception {
         beginTransaction( entityManager );
         try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            int deleted = 0;
            if ( hasForeignKeys() ) {
               for (; ; ) {
                  CriteriaQuery<T> query = cb.createQuery( getClazz() );
                  Root<T> root = query.from( getClazz() );
                  Predicate condition = getRootLevelCondition( cb, root );
                  if ( allowedIds != null ) {
                     // TODO this does not scale well
                     Predicate allowedCondition = cb.not( root.<Long>get( idProperty ).in( allowedIds ) );
                     if ( condition != null ) {
                        condition = cb.and( condition, allowedCondition );
                     } else {
                        condition = allowedCondition;
                     }
                  }
                  if ( condition != null ) {
                     query = query.where( condition );
                  }
                  List<T> list = entityManager.createQuery( query ).setMaxResults( batchLoadSize ).getResultList();
                  if ( list.isEmpty() ) break;
                  for (T entity : list) {
                     if ( !checkRootEntity( entity ) ) {
                        throw new IllegalStateException( String.valueOf( entity ) );
                     }
                     entityManager.remove( entity );
                     ++deleted;
                  }
                  entityManager.flush();
                  entityManager.clear();
                  commitTransaction( entityManager );
                  beginTransaction( entityManager );
               }
            } else {
               CriteriaDelete<T> query = cb.createCriteriaDelete( getClazz() );
               Root<T> root = query.from( getClazz() );
               if ( allowedIds != null ) {
                  query.where( cb.not( root.get( idProperty ).in( allowedIds ) ) );
               }
               deleted = entityManager.createQuery( query ).executeUpdate();
            }
            return deleted;
         } catch (Exception e) {
            log( e );
            throw e;
         } finally {
            commitTransaction( entityManager );
         }
      }

      protected boolean checkRootEntity(T entity) {
         return true;
      }

      protected Predicate getRootLevelCondition(CriteriaBuilder criteriaBuilder, Root<T> root) {
         return null;
      }

      @TearDown
      public void shutdown() throws Throwable {
         try {
            regularIds.clear();
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            try {
               System.out.println( "There are " + getSize( entityManager ) + " entities" );
            } finally {
               entityManager.close();
            }
            if ( persistenceUnit.contains( "mock" ) ) {
               PerfMockDriver.getInstance().setMocking( false );
            }
            entityManagerFactory.close();
            if ( persistenceUnit.startsWith( IRON_JACAMAR ) ) {
               jacamarHelper.stop();
            } else {
               jtaHelper.stop();
               jndiHelper.stop();
            }
         } catch (Throwable t) {
            log( t );
            throw t;
         }
      }

      private long getSize(EntityManager entityManager) {
         CriteriaBuilder cb = entityManager.getCriteriaBuilder();
         CriteriaQuery<Long> query = cb.createQuery( Long.class );
         Root<T> root = query.from( getClazz() );
         query.select( cb.count( root ) );
         Predicate condition = getRootLevelCondition( cb, root );
         if ( condition != null ) {
            query = query.where( condition );
         } else {
            // for some reason H2 sometimes returns incorrect results for
            // 'SELECT COUNT(*) FROM PERSON' while correct for 'SELECT COUNT(*) FROM PERSON WHERE 1=1'
            query = query.where( cb.equal( cb.literal( 1 ), cb.literal( 1 ) ) );
         }
         return entityManager.createQuery( query ).getSingleResult();
      }

      protected List<Object> seq(int from, int to) {
         ArrayList<Object> list = new ArrayList<>( to - from );
         for (long i = from; i < to; ++i) {
            list.add( i );
         }
         return list;
      }

      protected List<Object> list(int size, Object item) {
         ArrayList<Object> list = new ArrayList<>( size );
         for (int i = 0; i < size; ++i) {
            list.add( item );
         }
         return list;
      }

      public abstract Class<T> getClazz();

      protected abstract boolean hasForeignKeys();

      public abstract T randomEntity(ThreadLocalRandom random);

   }

   @State(Scope.Thread)
   public static class ThreadState {
      ThreadLocalRandom random = ThreadLocalRandom.current();
   }

   protected static boolean isLockException(Throwable e) {
      if ( THROW_ON_LOCK_EXCEPTIONS ) {
         if ( e instanceof OptimisticLockException ) {
            return true;
         } else if ( e instanceof PessimisticLockException ) {
            return true;
         } else if ( e.getMessage() != null && e.getMessage().startsWith( "Row not found" ) ) {
            return true;
         } else if ( e.getCause() != null && isLockException( e.getCause() ) ) {
            return true;
         }
      }
      return false;
   }

}
