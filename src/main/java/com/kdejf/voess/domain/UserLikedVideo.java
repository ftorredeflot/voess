package com.kdejf.voess.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A UserLikedVideo.
 */
@Entity
@Table(name = "user_liked_video")
public class UserLikedVideo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "start_date_time")
    private ZonedDateTime startDateTime;

    @NotNull
    @Column(name = "user_liked", nullable = false)
    private Boolean userLiked;

    @ManyToOne
    private User user;

    @ManyToOne
    private Video video;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getStartDateTime() {
        return startDateTime;
    }

    public UserLikedVideo startDateTime(ZonedDateTime startDateTime) {
        this.startDateTime = startDateTime;
        return this;
    }

    public void setStartDateTime(ZonedDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Boolean isUserLiked() {
        return userLiked;
    }

    public UserLikedVideo userLiked(Boolean userLiked) {
        this.userLiked = userLiked;
        return this;
    }

    public void setUserLiked(Boolean userLiked) {
        this.userLiked = userLiked;
    }

    public User getUser() {
        return user;
    }

    public UserLikedVideo user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Video getVideo() {
        return video;
    }

    public UserLikedVideo video(Video video) {
        this.video = video;
        return this;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserLikedVideo userLikedVideo = (UserLikedVideo) o;
        if (userLikedVideo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, userLikedVideo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UserLikedVideo{" +
            "id=" + id +
            ", startDateTime='" + startDateTime + "'" +
            ", userLiked='" + userLiked + "'" +
            '}';
    }
}
