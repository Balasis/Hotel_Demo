package gr.balasis.hotel.context.base.service;

import java.util.List;

public interface BaseService<T> {
    T create(final T item);

    void update(T item);

    void delete(T item);

    List<T> findAll();

    boolean exists(T item);
}
