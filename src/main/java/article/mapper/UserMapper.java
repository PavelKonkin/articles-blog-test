package article.mapper;

import article.model.User;
import article.model.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User convertDto(UserDto userDto) {
        return User.builder()
                .username(userDto.getUsername())
                .build();
    }
}
