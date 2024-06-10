package com.sparta.reviewspotproject.entity;

import com.sparta.reviewspotproject.dto.PostRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "post")
@NoArgsConstructor
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = false)
    private String title;

    @Column(nullable = false,unique = false)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> commentList = new ArrayList<>();

    @Column
    private int postLikesCount;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    public Post(PostRequestDto postRequestDto) {
        this.contents = postRequestDto.getContents();
        this.title = postRequestDto.getTitle();
    }

    public Post(PostRequestDto postRequestDto, User user) {
        this.contents = postRequestDto.getContents();
        this.title = postRequestDto.getTitle();
        this.user = user;
        this.postLikesCount = 0;
    }

    public void update(PostRequestDto postRequestDto, User user) {
        this.title = postRequestDto.getTitle();
        this.contents = postRequestDto.getContents();
        this.user = user;
    }
}
