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

    @PostMapping("/{categoryId}/show")
    @ResponseStatus(HttpStatus.OK)
    public long createShow(@PathVariable long categoryId, @RequestBody Show body) {
        log.trace("createShow");

        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            throw new BadRequestException(Response.status(Response.Status.BAD_REQUEST).build());
        }

        Show show = new Show();
        show.setCategory(category);
        show.setName(body.getName());
        show.setDescription(body.getDescription());
        show.setImage(body.getImage());
        show.setPrice(body.getPrice());
        show.setCapacity(body.getCapacity());
        show.setDuration(body.getDuration());
        show.setStatus("CREATED");

        // TODO Notificar
        return showRepository.save(show).getId();
    }

}
