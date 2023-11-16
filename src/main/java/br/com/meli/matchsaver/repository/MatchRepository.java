package br.com.meli.matchsaver.repository;

import br.com.meli.matchsaver.model.MatchModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface MatchRepository extends JpaRepository<MatchModel, UUID> {
    List<MatchModel> findAllByStadiumNameContainingIgnoreCaseAndDateTime(String stadiumName, LocalDateTime dateTime);

    List<MatchModel> findAllByHomeClubNameContainingIgnoreCaseOrVisitingClubNameContainingIgnoreCase(String homeClub, String visitingClub);
    List<MatchModel> findAllByHomeClubNameContainingIgnoreCaseAndVisitingClubNameContainingIgnoreCase(String homeClub, String visitingClub);
}
