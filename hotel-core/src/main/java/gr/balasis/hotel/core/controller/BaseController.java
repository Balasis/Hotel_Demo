package gr.balasis.hotel.core.controller;

import gr.balasis.hotel.context.base.domain.BaseDomain;
import gr.balasis.hotel.context.web.resource.BaseResource;
import gr.balasis.hotel.core.base.BaseComponent;
import gr.balasis.hotel.core.mapper.resourcedomain.RDbaseMapper;
import gr.balasis.hotel.core.service.BaseService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


public abstract class BaseController<T extends BaseDomain,R extends BaseResource> extends BaseComponent {
    protected abstract BaseService<T, Long> getBaseService();
    protected abstract RDbaseMapper<R,T> getMapper();

    @GetMapping("/{findById}")
    public ResponseEntity<R> findById(@PathVariable("findById") final Long id) {
        return ResponseEntity.ok(getMapper().toResource(getBaseService().findById(id) ));
    }

    @GetMapping
    public ResponseEntity<List<R>> findAll() {
        List<R> resources = getBaseService().findAll()
                .stream()
                .map(getMapper()::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
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
