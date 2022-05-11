package edu.uoc.epcsd.showcatalog.controllers;

import edu.uoc.epcsd.showcatalog.entities.Category;
import edu.uoc.epcsd.showcatalog.repositories.CategoryRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/categories")
public class CategoryController {

    /**
     * TODO: fix the 500 error on bad requests
     * TODO: use DTO instead of Category, Show classes
     */

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
    public long createCategory(@RequestBody Category body) {
        log.trace("createCategory");

        Category category = new Category();
        category.setName(body.getName());
        category.setDescription(body.getDescription());

        return categoryRepository.save(category).getId();
    }

    /**
     * "No solo no se debe hacer cascade, es que en esta implementación
     * no se debe permitir borrar una categoría que tenga actos asignados."
     */
    @PostMapping("/{categoryId}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCategory(@PathVariable long categoryId) {
        log.trace("deleteCategory");

        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            log.warn("Category not found");
            throw new NotFoundException(Response.status(Response.Status.NOT_FOUND).build());
        }

        if (category.getShows().isEmpty()) {
            categoryRepository.delete(category);
            log.info("Category {} deleted", categoryId);
        } else {
            log.warn("Category {} cannot be deleted because it has shows assigned", categoryId);
        }
    }

}
