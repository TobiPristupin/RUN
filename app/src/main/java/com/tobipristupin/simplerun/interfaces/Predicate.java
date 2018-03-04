package com.tobipristupin.simplerun.interfaces;

/**
 * Java 8 features such as predicates cannot be used when minSDK < 24, so this class recreates the java
 * 8 predicate.
 * @param <T> Type
 */

public interface Predicate<T> {

    boolean evaluate(T t);
}
