package com.peso.elBuenSabor.repositories;


import java.util.Optional;

import com.peso.elBuenSabor.entities.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
