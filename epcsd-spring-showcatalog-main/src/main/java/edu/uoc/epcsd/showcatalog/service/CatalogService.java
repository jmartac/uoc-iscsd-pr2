package edu.uoc.epcsd.showcatalog.service;

import edu.uoc.epcsd.showcatalog.entities.Category;
import edu.uoc.epcsd.showcatalog.entities.Performance;
import edu.uoc.epcsd.showcatalog.entities.Show;
import edu.uoc.epcsd.showcatalog.repositories.CategoryRepository;
import edu.uoc.epcsd.showcatalog.repositories.ShowRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class CatalogService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ShowRepository showRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public ResponseEntity<Long> createCategory(Category category) {
        Category saved = categoryRepository.save(category);
        log.info("Category {} created", saved.getId());

        return new ResponseEntity<>(saved.getId(), HttpStatus.OK);
    }

    /**
     * "No solo no se debe hacer cascade, es que en esta implementación
     * no se debe permitir borrar una categoría que tenga actos asignados."
     */
    public ResponseEntity<Object> deleteCategory(long categoryId) {
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

    /**
     * This operation only accepts one of two request parameters:
     * @return Bad Request if both parameters, or neither, are provided
     */
    public ResponseEntity<List<Show>> findShowsByNameOrCategory(String name, Optional<Long> categoryId) {
        if ((name != null && !name.isBlank() && categoryId.isPresent())
                || ((name == null || name.isBlank()) && categoryId.isEmpty())
        ) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (categoryId.isPresent()) {
            return new ResponseEntity<>(showRepository.findShowByCategoriesId(categoryId.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(showRepository.findByNameContaining(name), HttpStatus.OK);
    }

    public ResponseEntity<Long> createShow(Show show) {
        Show saved = showRepository.save(show);
        log.info("Show {} created", saved.getId());

        // TODO Notify Kafka ?

        return new ResponseEntity<>(saved.getId(), HttpStatus.OK);
    }

    public ResponseEntity<Show> getShowDetails(long showId) {
        Show show = showRepository.findById(showId).orElse(null);
        if (show == null) {
            log.warn("Show not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(show, HttpStatus.OK);
    }

    public ResponseEntity<List<Performance>> findShowPerformances(long showId) {
        Show show = showRepository.findById(showId).orElse(null);
        if (show == null) {
            log.warn("Show not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(show.getPerformances(), HttpStatus.OK);
    }

    public ResponseEntity<String> createPerformance(long showId, Performance performance) {
        Show show = showRepository.findById(showId).orElse(null);
        if (show == null) {
            log.warn("Show not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        performance.setShow(show);
        show.addPerformance(performance);

        Show showSaved = showRepository.save(show);
        Performance performanceSaved = showSaved.findPerformance(performance);

        log.info("Performance {} created for Show {}", performanceSaved.idToString(), showSaved.getId());
        return new ResponseEntity<>(performanceSaved.idToString(), HttpStatus.OK);
    }

    public ResponseEntity<Show> addShowCategory(long showId, long categoryId) {
        Show show = showRepository.findById(showId).orElse(null);
        if (show == null) {
            log.warn("Show not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            log.warn("Category not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        show.addCategory(category);
        return new ResponseEntity<>(showRepository.save(show), HttpStatus.OK);
    }
}
