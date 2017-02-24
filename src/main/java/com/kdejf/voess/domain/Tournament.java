package com.kdejf.voess.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Tournament.
 */
@Entity
@Table(name = "tournament")
public class Tournament implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "tournament_name")
    private String tournamentName;

    @Column(name = "tournament_date")
    private ZonedDateTime tournamentDate;

    @Lob
    @Column(name = "tournament_image")
    private byte[] tournamentImage;

    @Column(name = "tournament_image_content_type")
    private String tournamentImageContentType;

    @ManyToMany
    @JoinTable(name = "tournament_team_name",
               joinColumns = @JoinColumn(name="tournaments_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="team_names_id", referencedColumnName="ID"))
    private Set<Team> teamNames = new HashSet<>();

    @OneToMany(mappedBy = "tournament")
    @JsonIgnore
    private Set<Video> videos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public Tournament tournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
        return this;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public ZonedDateTime getTournamentDate() {
        return tournamentDate;
    }

    public Tournament tournamentDate(ZonedDateTime tournamentDate) {
        this.tournamentDate = tournamentDate;
        return this;
    }

    public void setTournamentDate(ZonedDateTime tournamentDate) {
        this.tournamentDate = tournamentDate;
    }

    public byte[] getTournamentImage() {
        return tournamentImage;
    }

    public Tournament tournamentImage(byte[] tournamentImage) {
        this.tournamentImage = tournamentImage;
        return this;
    }

    public void setTournamentImage(byte[] tournamentImage) {
        this.tournamentImage = tournamentImage;
    }

    public String getTournamentImageContentType() {
        return tournamentImageContentType;
    }

    public Tournament tournamentImageContentType(String tournamentImageContentType) {
        this.tournamentImageContentType = tournamentImageContentType;
        return this;
    }

    public void setTournamentImageContentType(String tournamentImageContentType) {
        this.tournamentImageContentType = tournamentImageContentType;
    }

    public Set<Team> getTeamNames() {
        return teamNames;
    }

    public Tournament teamNames(Set<Team> teams) {
        this.teamNames = teams;
        return this;
    }

    public Tournament addTeamName(Team team) {
        teamNames.add(team);
        team.getTournamentNames().add(this);
        return this;
    }

    public Tournament removeTeamName(Team team) {
        teamNames.remove(team);
        team.getTournamentNames().remove(this);
        return this;
    }

    public void setTeamNames(Set<Team> teams) {
        this.teamNames = teams;
    }

    public Set<Video> getVideos() {
        return videos;
    }

    public Tournament videos(Set<Video> videos) {
        this.videos = videos;
        return this;
    }

    public Tournament addVideo(Video video) {
        videos.add(video);
        video.setTournament(this);
        return this;
    }

    public Tournament removeVideo(Video video) {
        videos.remove(video);
        video.setTournament(null);
        return this;
    }

    public void setVideos(Set<Video> videos) {
        this.videos = videos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tournament tournament = (Tournament) o;
        if (tournament.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, tournament.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Tournament{" +
            "id=" + id +
            ", tournamentName='" + tournamentName + "'" +
            ", tournamentDate='" + tournamentDate + "'" +
            ", tournamentImage='" + tournamentImage + "'" +
            ", tournamentImageContentType='" + tournamentImageContentType + "'" +
            '}';
    }
}
