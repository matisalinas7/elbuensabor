package com.peso.elBuenSabor.services;

import com.peso.elBuenSabor.entities.User;
import com.peso.elBuenSabor.repositories.BaseRepository;
import com.peso.elBuenSabor.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class UserServiceImpl extends BaseServiceImpl<User, Long> implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(BaseRepository<User, Long> baseRepository) {
        super(baseRepository);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Sin esto, los usuarios creados desde el ABM quedaban con la contraseña en texto plano y no podían loguearse
    @Override
    @Transactional
    public User save(User user) throws Exception {
        if (!StringUtils.hasText(user.getPassword())) {
            throw new Exception("La contraseña es obligatoria");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return super.save(user);
    }

    // Contraseña vacía al editar = mantener la actual
    @Override
    @Transactional
    public User update(Long id, User user) throws Exception {
        User actual = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la entidad con id " + id));
        user.setPassword(StringUtils.hasText(user.getPassword())
                ? passwordEncoder.encode(user.getPassword())
                : actual.getPassword());
        return super.update(id, user);
    }
}
