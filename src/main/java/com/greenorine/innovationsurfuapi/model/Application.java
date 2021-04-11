package com.greenorine.innovationsurfuapi.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "applications")
@Data
public class Application extends BaseEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @Column(name = "price")
    private double price = 0;

    @Column(name = "contacts")
    private String contacts = "";

    @Column(name = "address")
    private String address = "";

    @Column(name = "tags")
    private String tags = "";

    @Column(name = "expert_comment")
    private String expertComment = "";

    @Column(name = "attachments")
    private String attachments = "";

    @ToString.Exclude
    @JsonIgnore
    @Fetch(value = FetchMode.SUBSELECT)
    @ManyToMany(mappedBy = "votes", fetch = FetchType.LAZY)
    private List<User> votedUsers = new ArrayList<>();

    @JsonGetter("votes")
    public int votes() {
        return votedUsers.size();
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "app_status")
    private ApplicationStatus appStatus = ApplicationStatus.NEW;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @JsonGetter("authorId")
    public Long authorId() {
        return author.getId();
    }

    @JsonGetter("authorName")
    public String authorName() {
        return author.getFullName();
    }
}
