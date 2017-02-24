package com.kdejf.voess.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A TeamFormer.
 */
@Entity
@Table(name = "team_former")
public class TeamFormer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "start_date_time")
    private ZonedDateTime startDateTime;

    @Column(name = "finsh_date_time")
    private ZonedDateTime finshDateTime;

    @ManyToOne
    private Player player;

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

    public TeamFormer startDateTime(ZonedDateTime startDateTime) {
        this.startDateTime = startDateTime;
        return this;
    }

    public void setStartDateTime(ZonedDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public ZonedDateTime getFinshDateTime() {
        return finshDateTime;
    }

    public TeamFormer finshDateTime(ZonedDateTime finshDateTime) {
        this.finshDateTime = finshDateTime;
        return this;
    }

    public void setFinshDateTime(ZonedDateTime finshDateTime) {
        this.finshDateTime = finshDateTime;
    }

    public Player getPlayer() {
        return player;
    }

    public TeamFormer player(Player player) {
        this.player = player;
        return this;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Team getTeam() {
        return team;
    }

    public TeamFormer team(Team team) {
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
        TeamFormer teamFormer = (TeamFormer) o;
        if (teamFormer.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, teamFormer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TeamFormer{" +
            "id=" + id +
            ", startDateTime='" + startDateTime + "'" +
            ", finshDateTime='" + finshDateTime + "'" +
            '}';
    }
}
