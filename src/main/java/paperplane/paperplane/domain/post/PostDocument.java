package paperplane.paperplane.domain.post;

import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;

@ApiModel(value = "편지 검색 결과 모델")
@Document(indexName = "post")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDocument {
    @Id
    private Integer id;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String content;

    @Field(type = FieldType.Keyword)
    private PostColor postColor;

    @Field(type = FieldType.Integer)
    private Integer likeCount;


    public PostDocument(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.postColor = post.getPostColor();
        this.likeCount = post.getLikeCount();
    }
}
