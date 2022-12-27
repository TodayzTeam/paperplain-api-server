package paperplane.paperplane.domain.user;


import lombok.*;
import paperplane.paperplane.domain.post.Post;
import paperplane.paperplane.domain.postinterest.PostInterest;
import paperplane.paperplane.domain.usergroup.UserGroup;
import paperplane.paperplane.domain.userinterest.UserInterest;
import paperplane.paperplane.domain.userpost.UserPost;

import javax.persistence.*;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    private String email;

    @Column
    private String name;

    @Column
    private String profileImageUrl;

    @Column
    private Boolean isReadWeb;

    @Column
    private Boolean isRepliedWeb;

    @Column
    private Boolean isPopularLetterWeb;

    @Column
    private Boolean isReadEmail;

    @Column
    private Boolean isRepliedEmail;

    @Column
    private Boolean isPopularLetterEmail;


    @OneToMany(cascade = {CascadeType.ALL})
    private Set<UserGroup> userGroups;

    @OneToMany(cascade = {CascadeType.ALL})
    private Set<UserInterest> userInterests;

    @OneToMany(cascade = {CascadeType.ALL})
    private Set<UserPost> userPosts;

    @OneToMany
    @JoinColumn(name = "user_id")
    private Set<Post> Posts;
}
