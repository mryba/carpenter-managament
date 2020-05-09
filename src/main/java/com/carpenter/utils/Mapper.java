package com.carpenter.utils;

public interface Mapper<T1, T2> {

    T1 mapFromDomain(T2 t2);

    T2 mapToDomain(T1 t1);
}
