package edu.uoc.epcsd.showcatalog.service;

import edu.uoc.epcsd.showcatalog.dtos.CategoryDTO;
import edu.uoc.epcsd.showcatalog.entities.Category;
import edu.uoc.epcsd.showcatalog.repositories.CategoryRepository;
import edu.uoc.epcsd.showcatalog.repositories.ShowRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Log4j2
@Service
public class CatalogService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ShowRepository showRepository;

    public List<Category> getAllCategories() {
        log.trace("getAllCategories");

        return categoryRepository.findAll();
    }

    public ResponseEntity<Long> createCategory(Category category) {
        log.trace("createCategory");

        Category saved = categoryRepository.save(category);
        log.info("Category {} created", saved.getId());

        return new ResponseEntity<>(saved.getId(), HttpStatus.OK);
    }

    /**
     * "No solo no se debe hacer cascade, es que en esta implementación
     * no se debe permitir borrar una categoría que tenga actos asignados."
     */
    public ResponseEntity<Object> deleteCategory(long categoryId) {
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
