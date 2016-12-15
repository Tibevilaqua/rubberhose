package com.rubberhose.infrastructure.function;

/**
 * Created by root on 15/12/16.
 */
@FunctionalInterface
public interface PentaFunction<T, U, R,V,S> {


    S fire(T t,U u,R r,V v);

}
