package org.jboss.perf;

import antlr.collections.AST;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.hql.internal.ast.ASTQueryTranslatorFactory;
import org.hibernate.hql.internal.ast.HqlParser;
import org.hibernate.hql.internal.ast.QueryTranslatorImpl;
import org.hibernate.hql.spi.QueryTranslatorFactory;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Function;

/**
 * Created by johara on 25/01/17.
 */
public class ORM5HQLInterpreter implements BenchmarkHQLParser {
   private SessionFactoryImplementor sessionFactoryImplementor;

   Method reflected = null;
   MethodHandle mh;
   CallSite site = null;
   Function parseFunction;

   public ORM5HQLInterpreter() {

      try {

         final MethodHandles.Lookup original = MethodHandles.lookup();
         final Field internal = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
         internal.setAccessible(true);
         final MethodHandles.Lookup trusted = (MethodHandles.Lookup) internal.get(original);

         final MethodHandles.Lookup caller = trusted.in(QueryTranslatorImpl.class);

         final Method parseMethod = QueryTranslatorImpl.class.getDeclaredMethod("parse",
            boolean.class);

         final MethodHandle parseHandle = caller.unreflect(parseMethod);

         mh = parseHandle;

         CallSite site = LambdaMetafactory.metafactory(
             trusted, "accept", MethodType.methodType( ParseConsumer.class ),
             mh.type(), mh, mh.type() );

         MethodHandle factory = site.getTarget();

         parseFunction = (Function) factory.invokeExact( );

//         parseLambda = parserLambda(caller, parseHandle);

      } catch (Throwable throwable) {
         throwable.printStackTrace();
      }

   }

   @Override
   public Object parseHQL(String hqlString) {

      QueryTranslatorImpl translator = createNewQueryTranslator( hqlString );

      AST ast = null;

//      HqlParser parser = null;
      Object parser = null;
      try {

         parser = parseFunction.apply(translator);

      } catch (Throwable throwable) {
         throwable.printStackTrace();
      }

      ast = null;// parser; // parser.getAST();


      return parser;
   }

   private QueryTranslatorImpl createNewQueryTranslator(String hql) {

      QueryTranslatorFactory ast = new ASTQueryTranslatorFactory();
      QueryTranslatorImpl newQueryTranslator = (QueryTranslatorImpl) ast.createQueryTranslator( hql, hql, Collections.EMPTY_MAP, sessionFactoryImplementor, null );
      newQueryTranslator.compile( new HashMap(), false );
      return newQueryTranslator;

   }

   @Override
   public void configure(SessionFactoryImplementor sessionFactoryImplementor) {
      this.sessionFactoryImplementor = sessionFactoryImplementor;
//      throw new RuntimeException( "Not yet Supported" );
   }




   static ParseConsumer<QueryTranslatorImpl, HqlParser> parserLambda(final MethodHandles.Lookup caller,
                                      final MethodHandle setterHandle) throws Throwable {

      final Class<?> functionKlaz = ParseConsumer.class;

      final String functionName = "accept";
      final Class<?> functionReturn = HqlParser.class;
      final Class<?>[] functionParams = new Class<?>[] { QueryTranslatorImpl.class,
         boolean.class };

      final MethodType factoryMethodType = MethodType
         .methodType(functionKlaz);
      final MethodType functionMethodType = MethodType.methodType(
         functionReturn, functionParams);

      final CallSite setterFactory = LambdaMetafactory.metafactory( //
         caller, // Represents a lookup context.
         functionName, // The name of the method to implement.
         factoryMethodType, // Signature of the factory method.
         functionMethodType, // Signature of function implementation.
         setterHandle, // Function method implementation.
         setterHandle.type() // Function method type signature.
      );

      final MethodHandle parseInvoker = setterFactory.getTarget();

      final ParseConsumer setterLambda = (ParseConsumer) parseInvoker
         .invoke();

      return setterLambda;
   }
}
