package paperplane.paperplane.domain.post.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import paperplane.paperplane.domain.post.PostDocument;

@Repository
public interface PostDocumentRepository extends ElasticsearchRepository<PostDocument,Integer> {
}
