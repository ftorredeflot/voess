package com.kdejf.voess.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A UserLikedTeams.
 */
@Entity
@Table(name = "user_liked_teams")
public class UserLikedTeams implements Serializable {

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
    private Team team;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getStartDateTime() {
        return startDateTime;
    }

    public UserLikedTeams startDateTime(ZonedDateTime startDateTime) {
        this.startDateTime = startDateTime;
        return this;
    }

    public void setStartDateTime(ZonedDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Boolean isUserLiked() {
        return userLiked;
    }

    public UserLikedTeams userLiked(Boolean userLiked) {
        this.userLiked = userLiked;
        return this;
    }

    public void setUserLiked(Boolean userLiked) {
        this.userLiked = userLiked;
    }

    public User getUser() {
        return user;
    }

    public UserLikedTeams user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Team getTeam() {
        return team;
    }

    public UserLikedTeams team(Team team) {
        this.team = team;
        return this;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserLikedTeams userLikedTeams = (UserLikedTeams) o;
        if (userLikedTeams.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, userLikedTeams.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UserLikedTeams{" +
            "id=" + id +
            ", startDateTime='" + startDateTime + "'" +
            ", userLiked='" + userLiked + "'" +
            '}';
    }
}
