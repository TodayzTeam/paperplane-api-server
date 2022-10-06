package paperplane.paperplane.domain.post.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;
import paperplane.paperplane.domain.group.dto.GroupResponseDto;
import paperplane.paperplane.domain.post.Post;
import paperplane.paperplane.domain.postinterest.dto.PostInterestResponseDto;
import paperplane.paperplane.domain.userpost.UserPost;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PostResponseDto {

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
        private GroupResponseDto.Info group;
        private List<PostInterestResponseDto> interest;

        public static Info of(Post post){
            return Info.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .date(LocalDateTime.now())
                    .reportCount(post.getReportCount())
                    .likeCount(post.getLikeCount())
                    .interest(PostInterestResponseDto.of(new ArrayList<>( post.getPostInterests())))
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
        private String title;
        private String content;
        private Integer likeCount;

        public static Simple of(Post post){
            return Simple.builder()
                    .title(post.getTitle())
                    .content(post.getContent())
                    .likeCount(post.getLikeCount())
                    .build();
        }

        public static List<PostResponseDto.Simple> of(List<Post> posts){
            return posts.stream().map(PostResponseDto.Simple::of).collect(Collectors.toList());
        }

    }
}
