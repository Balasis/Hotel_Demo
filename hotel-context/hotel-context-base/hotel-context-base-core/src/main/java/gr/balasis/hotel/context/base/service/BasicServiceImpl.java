package gr.balasis.hotel.context.base.service;


import gr.balasis.hotel.context.base.component.BaseComponent;
import gr.balasis.hotel.context.base.model.BaseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public abstract class BasicServiceImpl<T extends BaseModel> extends BaseComponent implements BaseService<T> {
    public abstract JpaRepository<T, Long> getRepository();

    @Override
    public T create(final T item) {
        return getRepository().save(item);
    }

    @Override
    public void update(T item) {
        getRepository().save(item);
    }

    @Override
    public void delete(T item) {
        getRepository().delete(item);
    }

    @Override
    public List<T> findAll() {
        return getRepository().findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(final T item) {
        return getRepository().existsById(item.getId());
    }

}
