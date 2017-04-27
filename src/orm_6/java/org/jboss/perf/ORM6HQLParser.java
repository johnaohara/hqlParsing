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

   @Override
   public void configure(SessionFactoryImplementor sessionFactoryImplementor) {

   }

   private ConsumerContext getConsumerContext() {
      return consumerContext;
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
