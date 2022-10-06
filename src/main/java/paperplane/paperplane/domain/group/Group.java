package paperplane.paperplane.domain.group;

import lombok.*;
import paperplane.paperplane.domain.usergroup.UserGroup;
import paperplane.paperplane.domain.userpost.UserPost;

import javax.persistence.*;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Group {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    private String name;

    @Column
    private String code;

    @OneToMany(mappedBy = "group", cascade = {CascadeType.ALL})
    private Set<UserGroup> userGroups;
}
