package com.kdejf.voess.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Video.
 */
@Entity
@Table(name = "video")
public class Video implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "video_name")
    private String videoName;

    @Column(name = "video_duration")
    private Integer videoDuration;

    @Column(name = "video_date")
    private ZonedDateTime videoDate;

    @Column(name = "video_viewer_count")
    private Integer videoViewerCount;

    @Column(name = "video_viewer_count_live")
    private Integer videoViewerCountLive;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "video_picks")
    private Integer videoPicks;

    @Column(name = "video_game_start")
    private Integer videoGameStart;

    @ManyToOne
    private Game game;

    @ManyToOne
    private Team team1;

    @ManyToOne
    private Team team2;

    @ManyToOne
    private Tournament tournament;

    @ManyToMany
    @JoinTable(name = "video_player",
               joinColumns = @JoinColumn(name="videos_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="players_id", referencedColumnName="ID"))
    private Set<Player> players = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVideoName() {
        return videoName;
    }

    public Video videoName(String videoName) {
        this.videoName = videoName;
        return this;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public Integer getVideoDuration() {
        return videoDuration;
    }

    public Video videoDuration(Integer videoDuration) {
        this.videoDuration = videoDuration;
        return this;
    }

    public void setVideoDuration(Integer videoDuration) {
        this.videoDuration = videoDuration;
    }

    public ZonedDateTime getVideoDate() {
        return videoDate;
    }

    public Video videoDate(ZonedDateTime videoDate) {
        this.videoDate = videoDate;
        return this;
    }

    public void setVideoDate(ZonedDateTime videoDate) {
        this.videoDate = videoDate;
    }

    public Integer getVideoViewerCount() {
        return videoViewerCount;
    }

    public Video videoViewerCount(Integer videoViewerCount) {
        this.videoViewerCount = videoViewerCount;
        return this;
    }

    public void setVideoViewerCount(Integer videoViewerCount) {
        this.videoViewerCount = videoViewerCount;
    }

    public Integer getVideoViewerCountLive() {
        return videoViewerCountLive;
    }

    public Video videoViewerCountLive(Integer videoViewerCountLive) {
        this.videoViewerCountLive = videoViewerCountLive;
        return this;
    }

    public void setVideoViewerCountLive(Integer videoViewerCountLive) {
        this.videoViewerCountLive = videoViewerCountLive;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public Video videoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        return this;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Integer getVideoPicks() {
        return videoPicks;
    }

    public Video videoPicks(Integer videoPicks) {
        this.videoPicks = videoPicks;
        return this;
    }

    public void setVideoPicks(Integer videoPicks) {
        this.videoPicks = videoPicks;
    }

    public Integer getVideoGameStart() {
        return videoGameStart;
    }

    public Video videoGameStart(Integer videoGameStart) {
        this.videoGameStart = videoGameStart;
        return this;
    }

    public void setVideoGameStart(Integer videoGameStart) {
        this.videoGameStart = videoGameStart;
    }

    public Game getGame() {
        return game;
    }

    public Video game(Game game) {
        this.game = game;
        return this;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Team getTeam1() {
        return team1;
    }

    public Video team1(Team team) {
        this.team1 = team;
        return this;
    }

    public void setTeam1(Team team) {
        this.team1 = team;
    }

    public Team getTeam2() {
        return team2;
    }

    public Video team2(Team team) {
        this.team2 = team;
        return this;
    }

    public void setTeam2(Team team) {
        this.team2 = team;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public Video tournament(Tournament tournament) {
        this.tournament = tournament;
        return this;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public Video players(Set<Player> players) {
        this.players = players;
        return this;
    }

    public Video addPlayer(Player player) {
        players.add(player);
        player.getVideos().add(this);
        return this;
    }

    public Video removePlayer(Player player) {
        players.remove(player);
        player.getVideos().remove(this);
        return this;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Video video = (Video) o;
        if (video.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, video.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Video{" +
            "id=" + id +
            ", videoName='" + videoName + "'" +
            ", videoDuration='" + videoDuration + "'" +
            ", videoDate='" + videoDate + "'" +
            ", videoViewerCount='" + videoViewerCount + "'" +
            ", videoViewerCountLive='" + videoViewerCountLive + "'" +
            ", videoUrl='" + videoUrl + "'" +
            ", videoPicks='" + videoPicks + "'" +
            ", videoGameStart='" + videoGameStart + "'" +
            '}';
    }
}
