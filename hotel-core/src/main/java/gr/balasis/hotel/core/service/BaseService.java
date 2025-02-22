package gr.balasis.hotel.core.service;

import java.util.List;

public interface BaseService <T,K>{
    T create(final T item);
    void update(T item);
    void delete(T item);
    T findById(K id);
    List<T> findAll();
}
