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
public class UserInterestResponseDto {

    private Integer id;
    private String keyword;

    public static UserInterestResponseDto of(UserInterest userInterest){
        return UserInterestResponseDto.builder()
                .id(userInterest.getInterest().getId())
                .keyword(userInterest.getInterest().getKeyword())
                .build();
    }

    public static List<UserInterestResponseDto> of(List<UserInterest> userInterests){
        return userInterests.stream().map(UserInterestResponseDto::of).collect(Collectors.toList());
    }
}
