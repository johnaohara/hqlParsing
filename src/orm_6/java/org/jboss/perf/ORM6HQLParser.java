package org.jboss.perf;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.persister.internal.PersisterFactoryImpl;
import org.hibernate.persister.internal.PersisterFactoryInitiator;
import org.hibernate.sqm.ConsumerContext;
import org.hibernate.sqm.SemanticQueryInterpreter;
import org.hibernate.sqm.query.SqmStatement;
import sql.support.ConsumerContextImpl;

/**
 * Created by johara on 25/01/17.
 */
public class ORM6HQLParser implements HQLParser {
   private SessionFactoryImplementor sessionFactory;
   private ConsumerContextImpl consumerContext;

   @Override
   public Object parseHQL(String hqlString) {

      SqmStatement sqmStatment = SemanticQueryInterpreter.interpret( hqlString, getConsumerContext() );

      return sqmStatment ;
   }

   private ConsumerContext getConsumerContext() {
      return consumerContext;
   }

   @Override
   public void configure() {
      //todo configure sessionFactory
      sessionFactory = null;

      final StandardServiceRegistry ssr = new StandardServiceRegistryBuilder()
         .applySetting( AvailableSettings.JPAQL_STRICT_COMPLIANCE, strictJpaCompliance() )
         .applySetting( AvailableSettings.HBM2DDL_AUTO, exportSchema() ? "create-drop" : "none" )
         .applySetting( PersisterFactoryInitiator.IMPL_NAME, new PersisterFactoryImpl() )
         .build();

      try {
         MetadataSources metadataSources = new MetadataSources( ssr );
         applyMetadataSources( metadataSources );

         this.sessionFactory = (SessionFactoryImplementor) metadataSources.buildMetadata().buildSessionFactory();
      }
      catch (Exception e) {
         StandardServiceRegistryBuilder.destroy( ssr );
         throw e;
      }

      consumerContext = new ConsumerContextImpl( sessionFactory );

//      throw new RuntimeException( "Not yet Supported" );
   }

   protected void applyMetadataSources(MetadataSources metadataSources) {
   }

   private boolean exportSchema() {
      return false;
   }

   private boolean strictJpaCompliance() {
      return false;
   }
}
