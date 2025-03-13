package gr.balasis.hotel.context.web.controller;


import gr.balasis.hotel.context.base.model.BaseModel;
import gr.balasis.hotel.context.web.mapper.BaseMapper;
import gr.balasis.hotel.context.base.service.BaseService;
import gr.balasis.hotel.context.web.resource.BaseResource;
import gr.balasis.hotel.context.base.component.BaseComponent;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public abstract class BaseController<T extends BaseModel, R extends BaseResource> extends BaseComponent {
    protected abstract BaseService<T,Long> getBaseService();
    protected abstract BaseMapper<T, R> getMapper();


    @GetMapping
    public ResponseEntity <List<R>> findAll() {

        return ResponseEntity.ok(
                getMapper().toResources(
                        getBaseService().findAll())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<R> get(
            @PathVariable("id") final Long id) {
        return ResponseEntity.ok(
                getMapper().toResource(getBaseService().get(id))
        );
    }

    @PostMapping
    public ResponseEntity<R> create(
            @RequestBody @Valid final R resource) {

        return ResponseEntity.ok(
                getMapper().toResource(
                        getBaseService().create(getMapper().toDomain(resource)))
        );
    }

    @PutMapping
    public ResponseEntity<Void> update(
            @RequestBody @Valid final R resource) {

        getBaseService().update(getMapper().toDomain(resource));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(
            @RequestBody @Valid final R resource) {

        T domainObject = getMapper().toDomain(resource);
        if (getBaseService().exists(domainObject)) {
            getBaseService().delete(domainObject);
        }
        return ResponseEntity.noContent().build();
    }

}
