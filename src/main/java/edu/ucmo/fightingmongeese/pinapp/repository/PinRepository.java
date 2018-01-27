package edu.ucmo.fightingmongeese.pinapp.repository;


import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PinRepository extends JpaRepository<Pin, Integer> {


    Optional<Pin> findByPin(String pin);
    @Query(value = "select p from Pin p where p.account = ?1 and p.claim_ip is null")
    Optional<Pin> findActivePin(String account);

}
