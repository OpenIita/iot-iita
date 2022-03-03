package cc.iotkit.manager.controller;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

public class DbBaseController<R extends MongoRepository<T, String>, T> {

    protected final R repository;

    public DbBaseController(R r) {
        this.repository = r;
    }

    @GetMapping("/list")
    public List<T> list() {
        return repository.findAll();
    }

    @PostMapping("/save")
    public void save(T t) {
        repository.save(t);
    }

    @DeleteMapping("/delete")
    public void delete(T t) {
        repository.delete(t);
    }
}
