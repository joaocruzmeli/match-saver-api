package br.com.meli.matchsaver.repository;

import br.com.meli.matchsaver.model.MatchModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<MatchModel, Long> {
    List<MatchModel> findByStadiumModelAndAndDateTime(String stadiumName, LocalDateTime dateTime);
}
