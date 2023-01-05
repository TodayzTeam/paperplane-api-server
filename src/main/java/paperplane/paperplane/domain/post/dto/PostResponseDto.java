package paperplane.paperplane.domain.post.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;
import paperplane.paperplane.domain.group.dto.GroupResponseDto;
import paperplane.paperplane.domain.post.Post;
import paperplane.paperplane.domain.post.PostColor;
import paperplane.paperplane.domain.postinterest.dto.PostInterestResponseDto;
import paperplane.paperplane.domain.user.User;
import paperplane.paperplane.domain.userpost.UserPost;
import paperplane.paperplane.domain.userpost.dto.UserPostResponseDto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        private GroupResponseDto.Info group;
        private List<PostInterestResponseDto> interest;
        private User sender;
        private List<UserPostResponseDto> receivers;

        public static Info of(Post post){
            return Info.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .date(LocalDateTime.now())
                    .reportCount(post.getReportCount())
                    .likeCount(post.getLikeCount())
                    .postColor(post.getPostColor())
                    .group(GroupResponseDto.Info.of(post.getGroup()))
                    .interest(PostInterestResponseDto.of(new ArrayList<>( post.getPostInterests())))
                    .sender(post.getSender())
                    .receivers(UserPostResponseDto.of(new ArrayList<>(post.getUserPosts())))
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

        public static Simple of(Post post){
            return Simple.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .likeCount(post.getLikeCount())
                    .postColor(post.getPostColor())
                    .build();
        }

        public static List<PostResponseDto.Simple> of(List<Post> posts){
            return posts.stream().map(PostResponseDto.Simple::of).collect(Collectors.toList());
        }

    }
}