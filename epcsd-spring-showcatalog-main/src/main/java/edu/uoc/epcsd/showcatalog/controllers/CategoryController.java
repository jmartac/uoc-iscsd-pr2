package edu.uoc.epcsd.showcatalog.controllers;

import edu.uoc.epcsd.showcatalog.DTOs.CategoryDTO;
import edu.uoc.epcsd.showcatalog.entities.Category;
import edu.uoc.epcsd.showcatalog.repositories.CategoryRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     * TODO: implement addShowToCategory
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
    public ResponseEntity<Long> createCategory(@RequestBody CategoryDTO requestBody) {
        log.trace("createCategory");

        if (requestBody.getName() == null || requestBody.getName().isEmpty()) {
            log.warn("Category name is required");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Category category = new Category();
        category.setName(requestBody.getName());
        category.setDescription(requestBody.getDescription());

        Category saved = categoryRepository.save(category);
        log.info("Category {} created", saved.getId());

        return new ResponseEntity<>(saved.getId(), HttpStatus.OK);
    }

    /**
     * "No solo no se debe hacer cascade, es que en esta implementación
     * no se debe permitir borrar una categoría que tenga actos asignados."
     */
    @PostMapping("/{categoryId}/delete")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable long categoryId) {
        log.trace("deleteCategory");

        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            log.warn("Category not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (category.getShows().isEmpty()) {
            categoryRepository.delete(category);
            log.info("Category {} deleted", categoryId);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        log.warn("Category {} cannot be deleted because it has shows assigned", categoryId);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
