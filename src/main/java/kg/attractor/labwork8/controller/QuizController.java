package kg.attractor.labwork8.controller;

import jakarta.validation.Valid;
import kg.attractor.labwork8.dto.AnswerDto;
import kg.attractor.labwork8.dto.QuizDto;
import kg.attractor.labwork8.dto.QuizResultDto;
import kg.attractor.labwork8.dto.StatisticsDto;
import kg.attractor.labwork8.exception.AlreadyCompletedException;
import kg.attractor.labwork8.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;

    @GetMapping("/{quizId}/leaderboard")
    public ResponseEntity<List<StatisticsDto>> getLeaderBoard(@PathVariable Long quizId) {
        return ResponseEntity.ok(quizService.getLeaderBoard(quizId));
    }

    @GetMapping("/{quizId}/results")
    public ResponseEntity<QuizResultDto> getResult(@PathVariable Long quizId) {
        return ResponseEntity.ok(quizService.getResult(quizId));
    }

    @PostMapping("/{quizId}/solve")
    public ResponseEntity<Map<Long, String>> getAnswer(@RequestBody @Valid List<AnswerDto> answerDto, @PathVariable Long quizId, @RequestParam Long userId) throws AlreadyCompletedException {
        return ResponseEntity.status(HttpStatus.CREATED).body(quizService.getAnswers(answerDto, userId));
    }

    @PostMapping
    public ResponseEntity<String> createQuiz(@Valid @RequestBody QuizDto quizDto) {
        quizService.createQuiz(quizDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Quiz created");
    }

    @GetMapping
    public ResponseEntity<List<QuizDto>> getAllQuizzes() {
        return ResponseEntity.ok(quizService.findQuizzes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizDto> getQuizById(@PathVariable Long id) {
        return ResponseEntity.ok(quizService.findById(id));
    }
}
