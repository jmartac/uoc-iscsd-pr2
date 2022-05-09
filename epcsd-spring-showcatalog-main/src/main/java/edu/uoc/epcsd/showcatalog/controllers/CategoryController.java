package edu.uoc.epcsd.showcatalog.controllers;

import edu.uoc.epcsd.showcatalog.entities.Category;
import edu.uoc.epcsd.showcatalog.repositories.CategoryRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<Category> getAllCategories() {
        log.trace("getAllCategories");

        return categoryRepository.findAll();
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public long createCategory(String name, @RequestParam(required = false) String description) {
        log.trace("createCategory");

        Category category = new Category();
        category.setName(name);
        category.setDescription(description);

        return categoryRepository.save(category).getId();
    }

}
