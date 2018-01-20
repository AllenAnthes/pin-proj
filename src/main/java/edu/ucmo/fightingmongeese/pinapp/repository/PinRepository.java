package edu.ucmo.fightingmongeese.pinapp.repository;


import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PinRepository extends JpaRepository<Pin, Integer> {


    Optional<Pin> findByAccountAndPin(String account, String pin);
    Optional<Pin> findByPin(String pin);

    List<Pin> findByAccount(String account);
}
