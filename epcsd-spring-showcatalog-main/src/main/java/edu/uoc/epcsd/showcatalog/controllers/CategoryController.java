package edu.uoc.epcsd.showcatalog.controllers;

import edu.uoc.epcsd.showcatalog.dtos.CategoryDTO;
import edu.uoc.epcsd.showcatalog.entities.Category;
import edu.uoc.epcsd.showcatalog.repositories.CategoryRepository;
import edu.uoc.epcsd.showcatalog.service.CatalogService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CatalogService catalogService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Category> getAllCategories() {
        log.trace("getAllCategories");

        return catalogService.getAllCategories();
    }

    @PostMapping()
    public ResponseEntity<Long> createCategory(@RequestBody CategoryDTO requestBody) {
        log.trace("createCategory");

        if (requestBody.getName() == null || requestBody.getName().isEmpty()) {
            log.warn("Category name is required");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Category category = new Category();
        category.setName(requestBody.getName());
        category.setDescription(requestBody.getDescription());

        return catalogService.createCategory(category);
    }

    @PostMapping("/{categoryId}/delete")
    public ResponseEntity<Object> deleteCategory(@PathVariable long categoryId) {
        log.trace("deleteCategory");

        return catalogService.deleteCategory(categoryId);
    }

}
