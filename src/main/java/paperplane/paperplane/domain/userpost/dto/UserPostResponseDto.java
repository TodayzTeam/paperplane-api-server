package paperplane.paperplane.domain.userpost.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;
import paperplane.paperplane.domain.userpost.UserPost;

import java.util.List;
import java.util.stream.Collectors;

public class UserPostResponseDto {

    @ApiModel(value = "편지 옵션")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Option {
        private Boolean isReply;
        private Boolean isReport;
        private Boolean isRead;
        private Boolean isLike;

        public static UserPostResponseDto.Option of(UserPost userPost) {
            return UserPostResponseDto.Option.builder()
                    .isReply(userPost.getIsReply())
                    .isReport(userPost.getIsReport())
                    .isRead(userPost.getIsRead())
                    .isLike(userPost.getIsLike()).build();
        }

        public static List<UserPostResponseDto.Option> of(List<UserPost> userPosts) {
            return userPosts.stream().map(UserPostResponseDto.Option::of).collect(Collectors.toList());
        }
    }

    @ApiModel(value = "편지 옵션")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Receivers {
        private Integer id;

        public static UserPostResponseDto.Receivers of(UserPost userPost) {
            return UserPostResponseDto.Receivers.builder()
                    .id(userPost.getReceiver().getId()).build();
        }

        public static List<UserPostResponseDto.Receivers> of(List<UserPost> userPosts) {
            return userPosts.stream().map(UserPostResponseDto.Receivers::of).collect(Collectors.toList());
        }
    }
}
