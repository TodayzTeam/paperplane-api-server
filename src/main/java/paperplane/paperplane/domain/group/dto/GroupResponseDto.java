package paperplane.paperplane.domain.group.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;
import paperplane.paperplane.domain.group.Group;
import paperplane.paperplane.domain.user.User;
import paperplane.paperplane.domain.usergroup.UserGroup;
import paperplane.paperplane.domain.usergroup.dto.UserGroupResponseDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GroupResponseDto {

    @ApiModel(value = "그룹 요약 정보")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Simple{
        private Integer id;
        private String name;
        private String code;

        public static Simple of(Group group){
            return Simple.builder()
                    .id(group.getId())
                    .name(group.getName())
                    .code(group.getCode())
                    .build();
        }

        public static List<Simple> of(List<Group> groups){
            return groups.stream().map(Simple::of).collect(Collectors.toList());
        }
    }
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
        private List<UserGroupResponseDto> userGroupResponseDtoList;

        public static Info of(Group group){
            return Info.builder()
                    .id(group.getId())
                    .name(group.getName())
                    .code(group.getCode())
                    .userGroupResponseDtoList(UserGroupResponseDto.of(new ArrayList<>(group.getUserGroups())))
                    .build();
        }

        public static List<Info> of(List<Group> groups){
            return groups.stream().map(Info::of).collect(Collectors.toList());
        }
    }
}
