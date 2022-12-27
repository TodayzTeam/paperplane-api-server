package paperplane.paperplane.domain.user.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;
import paperplane.paperplane.domain.user.User;
import paperplane.paperplane.domain.usergroup.dto.UserGroupResponseDto;
import paperplane.paperplane.domain.userinterest.dto.UserInterestResponseDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserResponseDto {
    @ApiModel(value = "유저 상세 정보")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info{
        private Integer id;
        private String name;
        private String email;
        private String profileImageUrl;
        private Boolean isReadWeb;
        private Boolean isRepliedWeb;
        private Boolean isPopularLetterWeb;
        private Boolean isReadEmail;
        private Boolean isRepliedEmail;
        private Boolean isPopularLetterEmail;
        private List<UserGroupResponseDto> userGroups;
        private List<UserInterestResponseDto> userInterests;

        public static Info of(User user){
            return Info.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .profileImageUrl(user.getProfileImageUrl())
                    .isReadWeb(user.getIsReadWeb())
                    .isRepliedWeb(user.getIsRepliedWeb())
                    .isPopularLetterWeb(user.getIsPopularLetterWeb())
                    .isReadEmail(user.getIsReadEmail())
                    .isPopularLetterEmail(user.getIsPopularLetterEmail())
                    .isRepliedEmail(user.getIsRepliedEmail())
                    .userGroups(UserGroupResponseDto.of(new ArrayList<>(user.getUserGroups())))
                    .userInterests(UserInterestResponseDto.of(new ArrayList<>(user.getUserInterests())))
                    .build();
        }

        public static List<UserResponseDto.Info> of(List<User> users){
            return users.stream().map(UserResponseDto.Info::of).collect(Collectors.toList());
        }
    }

    @ApiModel(value = "유저 요약 정보")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Simple{
        private String name;
        private String email;
        private String profileImageUrl;

        public static Simple of(User user){
            return Simple.builder()
                    .name(user.getName())
                    .email(user.getEmail())
                    .profileImageUrl(user.getProfileImageUrl())
                    .build();
        }

        public static List<UserResponseDto.Simple> of(List<User> users){
            return users.stream().map(UserResponseDto.Simple::of).collect(Collectors.toList());
        }
    }
}
