package paperplane.paperplane.domain.userinterest.dto;

import lombok.*;
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

    public static PostInterestResponseDto of(UserInterest userInterest){
        return PostInterestResponseDto.builder()
                .id(userInterest.getInterest().getId())
                .keyword(userInterest.getInterest().getKeyword())
                .build();
    }

    public static List<PostInterestResponseDto> of(List<UserInterest> userInterests){
        return userInterests.stream().map(PostInterestResponseDto::of).collect(Collectors.toList());
    }
}
