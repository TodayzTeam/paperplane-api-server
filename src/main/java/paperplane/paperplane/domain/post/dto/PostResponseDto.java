package paperplane.paperplane.domain.post.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import paperplane.paperplane.domain.group.dto.GroupResponseDto;
import paperplane.paperplane.domain.post.Post;
import paperplane.paperplane.domain.post.PostColor;
import paperplane.paperplane.domain.postinterest.dto.PostInterestResponseDto;
import paperplane.paperplane.domain.user.dto.UserResponseDto;
import paperplane.paperplane.domain.userpost.dto.UserPostResponseDto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
public class PostResponseDto implements Serializable {

    @ApiModel(value = "편지 상세 정보")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info{
        private Integer id;
        private String title;
        private String content;
        private LocalDateTime date;
        private Integer reportCount;
        private Integer likeCount;
        private PostColor postColor;
        private GroupResponseDto.Simple group;
        private List<PostInterestResponseDto> interest;
        private UserResponseDto.Simple sender;
        private List<UserPostResponseDto.Receivers> receivers;


        public static Info of(Post post){
                return Info.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .date(post.getDate())
                        .reportCount(post.getReportCount())
                        .likeCount(post.getLikeCount())
                        .postColor(post.getPostColor())
                        .group(GroupResponseDto.Simple.of(post.getGroup()))
                        .interest(PostInterestResponseDto.of(new ArrayList<>(post.getPostInterests())))
                        .sender(UserResponseDto.Simple.of(post.getSender()))
                        .receivers(UserPostResponseDto.Receivers.of(new ArrayList<>(post.getUserPosts())))
                        .build();
        }

        public static List<PostResponseDto.Info> of(List<Post> posts){
            return posts.stream().map(PostResponseDto.Info::of).collect(Collectors.toList());
        }
    }

    @ApiModel(value = "편지 요약 정보")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Simple{
        private Integer id;
        private String title;
        private String content;
        private Integer likeCount;
        private PostColor postColor;
        private LocalDateTime date;

        public static Simple of(Post post){
            return Simple.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .likeCount(post.getLikeCount())
                    .postColor(post.getPostColor())
                    .date(post.getDate())
                    .build();
        }

        public static List<PostResponseDto.Simple> of(List<Post> posts){
            return posts.stream().map(PostResponseDto.Simple::of).collect(Collectors.toList());
        }

    }


}