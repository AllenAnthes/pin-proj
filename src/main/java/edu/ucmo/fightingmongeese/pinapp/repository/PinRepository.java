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


    Optional<Pin> findByAccountAndPin(String account, String pin);
    Optional<Pin> findByPin(String pin);

    //TODO: Check
    @Query(value = "select p from Pin p where p.account = ?1 and p.claimIp is null")
    Optional<Pin> findActivePin(String account);

    List<Pin> findByAccount(String account);
}
