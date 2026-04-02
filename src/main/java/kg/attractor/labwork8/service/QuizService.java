package kg.attractor.labwork8.service;

import kg.attractor.labwork8.dto.AnswerDto;
import kg.attractor.labwork8.dto.QuizDto;
import kg.attractor.labwork8.dto.QuizResultDto;
import kg.attractor.labwork8.dto.StatisticsDto;
import kg.attractor.labwork8.exception.AlreadyCompletedException;

import java.util.List;
import java.util.Map;

public interface QuizService {
    List<StatisticsDto> getLeaderBoard(Long quizId);

    QuizResultDto getResult(Long quizId);

    Map<Long, String> getAnswers(List<AnswerDto> answersDto, Long userId) throws AlreadyCompletedException;

    QuizDto findById(Long id);

    List<QuizDto> findQuizzes();

    void createQuiz(QuizDto quizDto);
}
