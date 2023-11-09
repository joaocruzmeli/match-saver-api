package br.com.meli.matchsaver.repository;

import br.com.meli.matchsaver.model.ClubModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClubRepository extends JpaRepository<ClubModel, UUID> {
    Optional<ClubModel> findByName(String name);
}
