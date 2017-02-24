package com.kdejf.voess.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Game.
 */
@Entity
@Table(name = "game")
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "game_name")
    private String gameName;

    @Lob
    @Column(name = "game_picture")
    private byte[] gamePicture;

    @Column(name = "game_picture_content_type")
    private String gamePictureContentType;

    @OneToMany(mappedBy = "game")
    @JsonIgnore
    private Set<Video> videos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGameName() {
        return gameName;
    }

    public Game gameName(String gameName) {
        this.gameName = gameName;
        return this;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public byte[] getGamePicture() {
        return gamePicture;
    }

    public Game gamePicture(byte[] gamePicture) {
        this.gamePicture = gamePicture;
        return this;
    }

    public void setGamePicture(byte[] gamePicture) {
        this.gamePicture = gamePicture;
    }

    public String getGamePictureContentType() {
        return gamePictureContentType;
    }

    public Game gamePictureContentType(String gamePictureContentType) {
        this.gamePictureContentType = gamePictureContentType;
        return this;
    }

    public void setGamePictureContentType(String gamePictureContentType) {
        this.gamePictureContentType = gamePictureContentType;
    }

    public Set<Video> getVideos() {
        return videos;
    }

    public Game videos(Set<Video> videos) {
        this.videos = videos;
        return this;
    }

    public Game addVideo(Video video) {
        videos.add(video);
        video.setGame(this);
        return this;
    }

    public Game removeVideo(Video video) {
        videos.remove(video);
        video.setGame(null);
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
        Game game = (Game) o;
        if (game.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, game.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Game{" +
            "id=" + id +
            ", gameName='" + gameName + "'" +
            ", gamePicture='" + gamePicture + "'" +
            ", gamePictureContentType='" + gamePictureContentType + "'" +
            '}';
    }
}
