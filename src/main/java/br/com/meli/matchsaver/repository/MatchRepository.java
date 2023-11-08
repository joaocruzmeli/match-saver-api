package br.com.meli.matchsaver.repository;

import br.com.meli.matchsaver.model.ClubModel;
import br.com.meli.matchsaver.model.MatchModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<MatchModel, Long> {
    List<MatchModel> findAllByStadiumModelNameContainingIgnoreCaseAndDateTime(String stadiumName, LocalDateTime dateTime);

    @Query("SELECT MatchModel FROM MatchModel " +
            "WHERE (homeClub = :club OR visitingClub = :club) " +
            "ORDER BY dateTime")
    List<MatchModel> findAllMatchesForClub(@Param("club") ClubModel club);

    List<MatchModel> findAllByHomeClubNameContainingIgnoreCaseOrVisitingClubNameContainingIgnoreCase(String homeClub, String visitingClub);
}
