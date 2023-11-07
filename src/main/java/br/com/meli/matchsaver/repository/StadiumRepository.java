package br.com.meli.matchsaver.repository;

import br.com.meli.matchsaver.model.StadiumModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StadiumRepository extends JpaRepository<StadiumModel, Long> {
}
