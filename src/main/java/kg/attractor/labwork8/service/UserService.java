package kg.attractor.labwork8.service;

import kg.attractor.labwork8.dto.StatisticsDto;
import kg.attractor.labwork8.dto.UserDto;

public interface UserService {
    StatisticsDto getUserStatistics(long userId);

    void register(UserDto userDto);
}
