package edu.uoc.epcsd.showcatalog.repositories;

import edu.uoc.epcsd.showcatalog.entities.Show;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findByName(String name);
    List<Show> findByCategoryId(long categoryId);
}
