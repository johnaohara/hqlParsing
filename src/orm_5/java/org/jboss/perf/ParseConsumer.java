package org.jboss.perf;

/**
 * Created by johara on 05/05/17.
 */
@FunctionalInterface
public interface ParseConsumer<T, R> {
   /**
    * Performs this operation on the given arguments.
    *
    * @param t the first input argument
    * @param value the second input argument
    */
   R accept(T t, boolean value);
}
