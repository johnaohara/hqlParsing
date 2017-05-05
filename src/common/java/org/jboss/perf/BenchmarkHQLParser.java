package org.jboss.perf;

import org.hibernate.engine.spi.SessionFactoryImplementor;

/**
 * Created by johara on 25/01/17.
 */
public interface BenchmarkHQLParser {
   Object parseHQL(String hqlString);
   void configure(SessionFactoryImplementor sessionFactoryImplementor);
}
