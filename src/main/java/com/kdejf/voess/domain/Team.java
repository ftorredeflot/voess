package com.kdejf.voess.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Team.
 */
@Entity
@Table(name = "team")
public class Team implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "team_name")
    private String teamName;

    @Lob
    @Column(name = "team_avatar")
    private byte[] teamAvatar;

    @Column(name = "team_avatar_content_type")
    private String teamAvatarContentType;

    @Column(name = "team_win")
    private Integer teamWin;

    @OneToMany(mappedBy = "team1")
    @JsonIgnore
    private Set<Video> t1videos = new HashSet<>();

    @OneToMany(mappedBy = "team2")
    @JsonIgnore
    private Set<Video> t2videos = new HashSet<>();

    @ManyToMany(mappedBy = "teamNames")
    @JsonIgnore
    private Set<Tournament> tournamentNames = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public Team teamName(String teamName) {
        this.teamName = teamName;
        return this;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public byte[] getTeamAvatar() {
        return teamAvatar;
    }

    public Team teamAvatar(byte[] teamAvatar) {
        this.teamAvatar = teamAvatar;
        return this;
    }

    public void setTeamAvatar(byte[] teamAvatar) {
        this.teamAvatar = teamAvatar;
    }

    public String getTeamAvatarContentType() {
        return teamAvatarContentType;
    }

    public Team teamAvatarContentType(String teamAvatarContentType) {
        this.teamAvatarContentType = teamAvatarContentType;
        return this;
    }

    public void setTeamAvatarContentType(String teamAvatarContentType) {
        this.teamAvatarContentType = teamAvatarContentType;
    }

    public Integer getTeamWin() {
        return teamWin;
    }

    public Team teamWin(Integer teamWin) {
        this.teamWin = teamWin;
        return this;
    }

    public void setTeamWin(Integer teamWin) {
        this.teamWin = teamWin;
    }

    public Set<Video> getT1videos() {
        return t1videos;
    }

    public Team t1videos(Set<Video> videos) {
        this.t1videos = videos;
        return this;
    }

    public Team addT1video(Video video) {
        t1videos.add(video);
        video.setTeam1(this);
        return this;
    }

    public Team removeT1video(Video video) {
        t1videos.remove(video);
        video.setTeam1(null);
        return this;
    }

    public void setT1videos(Set<Video> videos) {
        this.t1videos = videos;
    }

    public Set<Video> getT2videos() {
        return t2videos;
    }

    public Team t2videos(Set<Video> videos) {
        this.t2videos = videos;
        return this;
    }

    public Team addT2video(Video video) {
        t2videos.add(video);
        video.setTeam2(this);
        return this;
    }

    public Team removeT2video(Video video) {
        t2videos.remove(video);
        video.setTeam2(null);
        return this;
    }

    public void setT2videos(Set<Video> videos) {
        this.t2videos = videos;
    }

    public Set<Tournament> getTournamentNames() {
        return tournamentNames;
    }

    public Team tournamentNames(Set<Tournament> tournaments) {
        this.tournamentNames = tournaments;
        return this;
    }

    public Team addTournamentName(Tournament tournament) {
        tournamentNames.add(tournament);
        tournament.getTeamNames().add(this);
        return this;
    }

    public Team removeTournamentName(Tournament tournament) {
        tournamentNames.remove(tournament);
        tournament.getTeamNames().remove(this);
        return this;
    }

    public void setTournamentNames(Set<Tournament> tournaments) {
        this.tournamentNames = tournaments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Team team = (Team) o;
        if (team.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, team.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Team{" +
            "id=" + id +
            ", teamName='" + teamName + "'" +
            ", teamAvatar='" + teamAvatar + "'" +
            ", teamAvatarContentType='" + teamAvatarContentType + "'" +
            ", teamWin='" + teamWin + "'" +
            '}';
    }
}
