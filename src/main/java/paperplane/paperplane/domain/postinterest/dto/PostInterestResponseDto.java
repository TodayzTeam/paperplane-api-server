package paperplane.paperplane.domain.postinterest.dto;

import lombok.*;
import paperplane.paperplane.domain.postinterest.PostInterest;
import paperplane.paperplane.domain.userinterest.UserInterest;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostInterestResponseDto {
    private Integer id;
    private String keyword;

    public static PostInterestResponseDto of(PostInterest postInterest){
        return PostInterestResponseDto.builder()
                .id(postInterest.getInterest().getId())
                .keyword(postInterest.getInterest().getKeyword())
                .build();
    }

    public static List<PostInterestResponseDto> of(List<PostInterest> postInterests){
        return postInterests.stream().map(PostInterestResponseDto::of).collect(Collectors.toList());
    }

}
