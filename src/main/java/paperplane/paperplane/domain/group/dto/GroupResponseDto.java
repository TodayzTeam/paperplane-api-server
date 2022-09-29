package paperplane.paperplane.domain.group.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;
import paperplane.paperplane.domain.group.Group;

import java.util.List;
import java.util.stream.Collectors;

public class GroupResponseDto {

    @ApiModel(value = "그룹 상세 정보")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info{
        private Integer id;
        private String name;
        private String code;

        public static Info of(Group group){
            return Info.builder()
                    .id(group.getId())
                    .name(group.getName())
                    .code(group.getCode())
                    .build();
        }

        public static List<Info> of(List<Group> groups){
            return groups.stream().map(Info::of).collect(Collectors.toList());
        }
    }
}
