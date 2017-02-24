package com.kdejf.voess.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Country.
 */
@Entity
@Table(name = "country")
public class Country implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "country_name")
    private String countryName;

    @Lob
    @Column(name = "coutry_flag")
    private byte[] coutryFlag;

    @Column(name = "coutry_flag_content_type")
    private String coutryFlagContentType;

    @OneToMany(mappedBy = "country")
    @JsonIgnore
    private Set<Player> players = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public Country countryName(String countryName) {
        this.countryName = countryName;
        return this;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public byte[] getCoutryFlag() {
        return coutryFlag;
    }

    public Country coutryFlag(byte[] coutryFlag) {
        this.coutryFlag = coutryFlag;
        return this;
    }

    public void setCoutryFlag(byte[] coutryFlag) {
        this.coutryFlag = coutryFlag;
    }

    public String getCoutryFlagContentType() {
        return coutryFlagContentType;
    }

    public Country coutryFlagContentType(String coutryFlagContentType) {
        this.coutryFlagContentType = coutryFlagContentType;
        return this;
    }

    public void setCoutryFlagContentType(String coutryFlagContentType) {
        this.coutryFlagContentType = coutryFlagContentType;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public Country players(Set<Player> players) {
        this.players = players;
        return this;
    }

    public Country addPlayer(Player player) {
        players.add(player);
        player.setCountry(this);
        return this;
    }

    public Country removePlayer(Player player) {
        players.remove(player);
        player.setCountry(null);
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
        Country country = (Country) o;
        if (country.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, country.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Country{" +
            "id=" + id +
            ", countryName='" + countryName + "'" +
            ", coutryFlag='" + coutryFlag + "'" +
            ", coutryFlagContentType='" + coutryFlagContentType + "'" +
            '}';
    }
}
