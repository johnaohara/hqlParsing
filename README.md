# HQL Parsing Microbenchmark

A JMH micro beckmark to compare HQL parsing performance of different versions of Hibernate ORM.

## Build

    mvn clean install

Then you can build ORM5 with

    mvn -P orm_5,h2 clean package

or ORM6

    mvn -P orm_6,h2 clean package 

## Run

    java -jar target/benchmarks.jar -wi 10 -i 10 -f 5

where
  - wi ; number of warmup iterations
  - i ; number if measurement iterations
  - f ; number of benchmark forks
    

