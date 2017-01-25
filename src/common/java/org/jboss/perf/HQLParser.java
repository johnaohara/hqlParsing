package org.jboss.perf;

/**
 * Created by johara on 25/01/17.
 */
public interface HQLParser {
   void parseHQL(String hqlString);
   void configure();
}
