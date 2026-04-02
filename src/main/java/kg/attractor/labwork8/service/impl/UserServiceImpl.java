package kg.attractor.labwork8.service.impl;

import kg.attractor.labwork8.dao.QuizDao;
import kg.attractor.labwork8.dao.UserDao;
import kg.attractor.labwork8.dto.StatisticsDto;
import kg.attractor.labwork8.dto.UserDto;
import kg.attractor.labwork8.exception.NotFoundException;
import kg.attractor.labwork8.model.User;
import kg.attractor.labwork8.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final PasswordEncoder encoder;
    private final QuizDao quizDao;

    @Override
    public StatisticsDto getUserStatistics(long userId) {
        User user = userDao.findById(userId)
                .orElseThrow(NotFoundException::new);

        double averageScore = quizDao.findAverageScore(userId);

        return StatisticsDto.builder()
                .email(user.getEmail())
                .score(averageScore)
                .build();
    }

    @Override
    public void register(UserDto userDto) {
        User user =  User
                .builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(encoder.encode(userDto.getPassword()))
                .build();
        userDao.register(user);
        log.info("User {} registered", userDto.getEmail());
    }
}
