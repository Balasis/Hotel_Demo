package gr.balasis.hotel.context.base.service;


import gr.balasis.hotel.context.base.component.BaseComponent;
import gr.balasis.hotel.context.base.exception.EntityNotFoundException;
import gr.balasis.hotel.context.base.model.BaseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public abstract class BasicServiceImpl<T extends BaseModel> extends BaseComponent implements BaseService<T,Long> {
    public abstract JpaRepository<T, Long> getRepository();

    @Override
    public T create(final T item) {
        return getRepository().save(item);
    }

    @Override
    public T get(final Long id){
        return getRepository().findById(id).orElseThrow(
                () ->  new EntityNotFoundException("Entity with ID " + id + " not found.")
        );
    }

    @Override
    public void update(T item) {
        if (!getRepository().existsById(item.getId())) {
            throw new EntityNotFoundException("Entity with ID " + item.getId() + " not found.");
        }
        getRepository().save(item);
    }

    @Override
    public void delete(final Long id) {
        if (!getRepository().existsById(id)) {
            throw new EntityNotFoundException("Entity with ID " + id + " not found.");
        }
        getRepository().deleteById(id);
    }

    @Override
    public List<T> findAll() {
        return getRepository().findAll();
    }

    @Override
    public boolean exists(final T item) {
        return getRepository().existsById(item.getId());
    }

}
