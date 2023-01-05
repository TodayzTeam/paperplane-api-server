package paperplane.paperplane.domain.Interest.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;
import paperplane.paperplane.domain.Interest.Interest;
import paperplane.paperplane.domain.group.Group;
import paperplane.paperplane.domain.group.dto.GroupResponseDto;

import java.util.List;
import java.util.stream.Collectors;

public class InterestResponseDto {

    @ApiModel(value = "키워드 상세 정보")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info{
        private Integer id;
        private String keyword;
        private Integer count;

        public static Info of(Interest interest){
            return Info.builder()
                    .id(interest.getId())
                    .keyword(interest.getKeyword())
                    .count(interest.getCount())
                    .build();
        }

        public static List<Info> of(List<Interest> interests){
            return interests.stream().map(Info::of).collect(Collectors.toList());
        }
    }
}
