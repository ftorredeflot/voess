package com.kdejf.voess.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.kdejf.voess.domain.enumeration.SexGender;

/**
 * A Player.
 */
@Entity
@Table(name = "player")
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "player_name")
    private String playerName;

    @Column(name = "player_last_name")
    private String playerLastName;

    @Column(name = "player_nick")
    private String playerNick;

    @Column(name = "player_age")
    private Integer playerAge;

    @Lob
    @Column(name = "player_image")
    private byte[] playerImage;

    @Column(name = "player_image_content_type")
    private String playerImageContentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "player_sex")
    private SexGender playerSex;

    @Column(name = "player_score")
    private Integer playerScore;

    @ManyToOne
    private Country country;

    @ManyToMany(mappedBy = "players")
    @JsonIgnore
    private Set<Video> videos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Player playerName(String playerName) {
        this.playerName = playerName;
        return this;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerLastName() {
        return playerLastName;
    }

    public Player playerLastName(String playerLastName) {
        this.playerLastName = playerLastName;
        return this;
    }

    public void setPlayerLastName(String playerLastName) {
        this.playerLastName = playerLastName;
    }

    public String getPlayerNick() {
        return playerNick;
    }

    public Player playerNick(String playerNick) {
        this.playerNick = playerNick;
        return this;
    }

    public void setPlayerNick(String playerNick) {
        this.playerNick = playerNick;
    }

    public Integer getPlayerAge() {
        return playerAge;
    }

    public Player playerAge(Integer playerAge) {
        this.playerAge = playerAge;
        return this;
    }

    public void setPlayerAge(Integer playerAge) {
        this.playerAge = playerAge;
    }

    public byte[] getPlayerImage() {
        return playerImage;
    }

    public Player playerImage(byte[] playerImage) {
        this.playerImage = playerImage;
        return this;
    }

    public void setPlayerImage(byte[] playerImage) {
        this.playerImage = playerImage;
    }

    public String getPlayerImageContentType() {
        return playerImageContentType;
    }

    public Player playerImageContentType(String playerImageContentType) {
        this.playerImageContentType = playerImageContentType;
        return this;
    }

    public void setPlayerImageContentType(String playerImageContentType) {
        this.playerImageContentType = playerImageContentType;
    }

    public SexGender getPlayerSex() {
        return playerSex;
    }

    public Player playerSex(SexGender playerSex) {
        this.playerSex = playerSex;
        return this;
    }

    public void setPlayerSex(SexGender playerSex) {
        this.playerSex = playerSex;
    }

    public Integer getPlayerScore() {
        return playerScore;
    }

    public Player playerScore(Integer playerScore) {
        this.playerScore = playerScore;
        return this;
    }

    public void setPlayerScore(Integer playerScore) {
        this.playerScore = playerScore;
    }

    public Country getCountry() {
        return country;
    }

    public Player country(Country country) {
        this.country = country;
        return this;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Set<Video> getVideos() {
        return videos;
    }

    public Player videos(Set<Video> videos) {
        this.videos = videos;
        return this;
    }

    public Player addVideo(Video video) {
        videos.add(video);
        video.getPlayers().add(this);
        return this;
    }

    public Player removeVideo(Video video) {
        videos.remove(video);
        video.getPlayers().remove(this);
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
        Player player = (Player) o;
        if (player.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, player.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Player{" +
            "id=" + id +
            ", playerName='" + playerName + "'" +
            ", playerLastName='" + playerLastName + "'" +
            ", playerNick='" + playerNick + "'" +
            ", playerAge='" + playerAge + "'" +
            ", playerImage='" + playerImage + "'" +
            ", playerImageContentType='" + playerImageContentType + "'" +
            ", playerSex='" + playerSex + "'" +
            ", playerScore='" + playerScore + "'" +
            '}';
    }
}
