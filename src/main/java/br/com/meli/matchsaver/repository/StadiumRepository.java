package br.com.meli.matchsaver.repository;

import br.com.meli.matchsaver.model.StadiumModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StadiumRepository extends JpaRepository<StadiumModel, Long> {
    Optional<StadiumModel> findByName(String name);
}
