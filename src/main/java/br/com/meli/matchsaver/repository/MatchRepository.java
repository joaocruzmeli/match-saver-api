package br.com.meli.matchsaver.repository;

import br.com.meli.matchsaver.model.MatchModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<MatchModel, Long> {
}
