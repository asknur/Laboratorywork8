package kg.attractor.labwork8.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionOptionDto {
    @NotBlank
    private QuestionDto question;

    @NotNull
    private List<OptionDto> options =  new ArrayList<>();



}
