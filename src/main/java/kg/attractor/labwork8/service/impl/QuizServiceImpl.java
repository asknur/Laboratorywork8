package kg.attractor.labwork8.service.impl;

import kg.attractor.labwork8.dao.OptionDao;
import kg.attractor.labwork8.dao.QuestionDao;
import kg.attractor.labwork8.dao.QuizDao;
import kg.attractor.labwork8.dao.UserDao;
import kg.attractor.labwork8.dto.*;
import kg.attractor.labwork8.exception.AlreadyCompletedException;
import kg.attractor.labwork8.exception.NotFoundException;
import kg.attractor.labwork8.model.*;
import kg.attractor.labwork8.service.QuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class QuizServiceImpl implements QuizService {
    private final QuizDao quizDao;
    private final QuestionDao questionDao;
    private final OptionDao optionDao;
    private final UserDao userDao;

    @Override
    public List<StatisticsDto> getLeaderBoard(Long quizId) {
        List<StatisticsDto> statisticsDto = new ArrayList<>();
        List<User> users = new ArrayList<>();
        List<QuizResult> results = quizDao.getStatistics(quizId);

        for (QuizResult result : results) {
            users.add(userDao.findById(result.getUserId()).orElseThrow(NotFoundException::new));
        }

        statisticsDto = results.stream()
                .map(r -> StatisticsDto.builder()
                        .email(users.stream()
                                .filter(u -> u.getId().equals(r.getUserId()))
                                .findFirst()
                                .orElseThrow(NotFoundException::new)
                                .getEmail())
                        .score(r.getScore())
                        .build())
                .sorted((d1, d2) -> Double.compare(d2.getScore(), d1.getScore())) // сортировка по убыванию
                .collect(Collectors.toList());

        return statisticsDto;
    }

    @Override
    public QuizResultDto getResult(Long quizId) {
        Quiz quiz = quizDao.findById(quizId);
        List<Question> questions = questionDao.findQuestionsByQuizId(quizId);
        List<Option> options = new ArrayList<>();
        for (Question question : questions) {
            options.addAll(optionDao.findByQuestionId(question.getId()));
        }
        List<Option> correctOptions = new ArrayList<>();
        for (Option option : options) {
            if(option.getIsCorrect()) {
                correctOptions.add(option);
            }
        }
        List<OptionDto> optionDto = correctOptions
                .stream()
                .map(o -> OptionDto
                        .builder()
                        .id(o.getId())
                        .questionId(o.getQuestionId())
                        .option(o.getOptionText())
                        .isCorrect(o.getIsCorrect())
                        .build()).toList();

        List<QuestionDto> questionDto = questions
                .stream()
                .map(o -> QuestionDto
                        .builder()
                        .id(o.getId())
                        .quizId(o.getQuizId())
                        .question(o.getQuestionText())
                        .build()).toList();

        List<QuestionOptionDto> qos = new ArrayList<>();
        for (QuestionDto question : questionDto) {
            QuestionOptionDto questionOptionDto = new QuestionOptionDto();
            questionOptionDto.setQuestion(question);
            for(OptionDto option : optionDto) {
                if(option.getQuestionId().equals(question.getId())){
                    questionOptionDto.getOptions().add(option);
                }
            }
            qos.add(questionOptionDto);

        }

        int correctOptionsCount = 0;
        for (Option option : options) {
            if(option.getIsCorrect()){
                correctOptionsCount += 1;
            }
        }
        QuizResultDto resultDto = new QuizResultDto();
        resultDto.setCountQuestions(questions.size());
        resultDto.setCountCorrectOptions(correctOptionsCount);
        resultDto.setQuestionOptionDto(qos);
        resultDto.setOptions(options.size());
        return resultDto;
    }

    @Override
    public Map<Long, String> getAnswers(List<AnswerDto> answersDto, Long userId) throws AlreadyCompletedException {
        if (!answersDto.isEmpty()) {
            Long quizId = quizDao.findByQuestionId(answersDto.get(0).getQuestionId())
                    .orElseThrow(NotFoundException::new).getId();
            if (quizDao.hasUserCompletedQuiz(quizId, userId)) {
                throw new AlreadyCompletedException("User has already completed this quiz");
            }
        }

        User user = userDao.findById(userId).orElseThrow(NotFoundException::new);

        Map<Long, String> results = new HashMap<>();
        int correctCount = 0;

        for (AnswerDto answerDto : answersDto) {
            Question question = questionDao.getQuestionById(answerDto.getQuestionId())
                    .orElseThrow(NotFoundException::new);

            Quiz quiz = quizDao.findByQuestionId(question.getId())
                    .orElseThrow(NotFoundException::new);

            List<Option> options = optionDao.findByQuestionId(question.getId());

            Option selectedOption = options.stream()
                    .filter(option -> option.getId().equals(answerDto.getSelectedOptionId()))
                    .findFirst()
                    .orElseThrow(NotFoundException::new);

            if (selectedOption.getIsCorrect()) {
                results.put(question.getId(), "Correct");
                correctCount++;
            } else {
                results.put(question.getId(), "Incorrect");
            }
        }

        if (!answersDto.isEmpty()) {
            Long quizId = quizDao.findByQuestionId(answersDto.get(0).getQuestionId())
                    .orElseThrow(NotFoundException::new).getId();

            quizDao.setScore(quizId, userId, (double) correctCount);
        }

        return results;
    }


    @Override
    public QuizDto findById(Long id) {
        Quiz quiz = quizDao.findById(id);
        List<Question> questions = questionDao.findQuestionsByQuizId(quiz.getId());
        List<Option> options = new ArrayList<>();
        for (Question question : questions) {
            options.addAll(optionDao.findByQuestionId(question.getId()));
        }
        List<QuestionDto> questionDto = questions
                .stream()
                .map(q -> QuestionDto
                        .builder()
                        .id(q.getId())
                        .quizId(q.getQuizId())
                        .question(q.getQuestionText())
                        .build()
                ).toList();
        List<OptionDto> optionDto = options
                .stream()
                .map(o -> OptionDto
                        .builder()
                        .id(o.getId())
                        .questionId(o.getQuestionId())
                        .option(o.getOptionText())
                        .isCorrect(null)
                        .build()
                ).toList();

        List<QuestionOptionDto> qo = new ArrayList<>();
        for (QuestionDto question : questionDto) {
            QuestionOptionDto newQO = new QuestionOptionDto();
            newQO.setQuestion(question);
            for (OptionDto option : optionDto) {
                if(question.getId().equals(option.getQuestionId())){
                    newQO.getOptions().add(option);
                }
            }
            qo.add(newQO);
        }


        return QuizDto
                .builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .description(quiz.getDescription())
                .creatorId(quiz.getCreatorId())
                .questionOptions(qo)
                .build();

    }

    @Override
    public List<QuizDto> findQuizzes(){
        List<Quiz> quizzes = quizDao.findQuizzes();
        for(Quiz quiz : quizzes){
            quiz.setCountOfQuestions(quizDao.getCountOfQuestions(quiz.getId()));
        }
        return quizzes
                .stream()
                .map(q -> QuizDto
                        .builder()
                        .title(q.getTitle())
                        .description(q.getDescription())
                        .creatorId(q.getCreatorId())
                        .countOfQuestions(q.getCountOfQuestions())
                        .build()).toList();

    }

    @Override
    public void createQuiz(QuizDto quizDto) {
        Quiz quiz = Quiz
                .builder()
                .id(quizDto.getId())
                .title(quizDto.getTitle())
                .description(quizDto.getDescription())
                .creatorId(quizDto.getCreatorId())
                .build();

        Long quizId = quizDao.createQuiz(quiz);
        log.info("Quiz created: {} id: {}", quiz.getTitle(), quizId);

        List<QuestionOptionDto> questionOptions = quizDto.getQuestionOptions();

        for (QuestionOptionDto questionOptionDto : questionOptions) {
            Question question = Question
                    .builder()
                    .quizId(quizId)
                    .questionText(questionOptionDto.getQuestion().getQuestion())
                    .build();
            Long questionId = questionDao.createQuestion(question);
            log.info("Question created: {} id: {}", question.getQuestionText(), questionId);

            for (OptionDto optionDto : questionOptionDto.getOptions()) {
                Option option = Option
                        .builder()
                        .questionId(questionId)
                        .optionText(optionDto.getOption())
                        .isCorrect(optionDto.getIsCorrect())
                        .build();
                optionDao.createOption(option);
                log.info("Option created: {}", option.getOptionText());
            }
        }
    }
}
