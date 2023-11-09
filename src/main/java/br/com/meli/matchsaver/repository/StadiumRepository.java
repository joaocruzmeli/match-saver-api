package br.com.meli.matchsaver.repository;

import br.com.meli.matchsaver.model.StadiumModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StadiumRepository extends JpaRepository<StadiumModel, UUID> {
    Optional<StadiumModel> findByName(String name);
}
