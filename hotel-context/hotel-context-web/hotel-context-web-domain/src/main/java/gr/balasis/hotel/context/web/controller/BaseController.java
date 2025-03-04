package gr.balasis.hotel.context.web.controller;

import gr.balasis.hotel.context.base.domain.BaseDomain;
import gr.balasis.hotel.context.web.mapper.BaseMapper;
import gr.balasis.hotel.context.base.service.BaseService;
import gr.balasis.hotel.context.web.resource.BaseResource;
import gr.balasis.hotel.context.base.component.BaseComponent;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public abstract class BaseController<T extends BaseDomain, R extends BaseResource> extends BaseComponent {
    protected abstract BaseService<T, Long> getBaseService();

    protected abstract BaseMapper<T, R> getMapper();

    @GetMapping("/{Id}")
    public ResponseEntity<R> findById(@PathVariable final Long Id) {
        return ResponseEntity.ok(getMapper().toResource(getBaseService().findById(Id)));
    }

    @PostMapping
    public ResponseEntity<R> create(@RequestBody R resource) {
        T domain = getMapper().toDomain(resource);
        T created = getBaseService().create(domain);
        return ResponseEntity.ok(getMapper().toResource(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<R> update(@PathVariable Long id, @RequestBody R resource) {
        T domain = getMapper().toDomain(resource);
        getBaseService().update(domain);
        return ResponseEntity.ok(getMapper().toResource(domain));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        T domain = getBaseService().findById(id);
        getBaseService().delete(domain);
        return ResponseEntity.noContent().build();
    }

}
