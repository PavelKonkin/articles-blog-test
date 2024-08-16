package article.service;

import article.model.User;
import article.model.dto.UserDto;

import java.util.Optional;

public interface UserService {
    public User registerUser(UserDto user);

    public Optional<User> findByUsername(String username);
}
