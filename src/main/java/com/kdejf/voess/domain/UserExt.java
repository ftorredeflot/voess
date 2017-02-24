package com.kdejf.voess.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.kdejf.voess.domain.enumeration.SexGender;

/**
 * A UserExt.
 */
@Entity
@Table(name = "user_ext")
public class UserExt implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "user_age", nullable = false)
    private Integer userAge;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "user_sex", nullable = false)
    private SexGender userSex;

    @NotNull
    @Size(max = 5242880)
    @Lob
    @Column(name = "user_image", nullable = false)
    private byte[] userImage;

    @Column(name = "user_image_content_type", nullable = false)
    private String userImageContentType;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserAge() {
        return userAge;
    }

    public UserExt userAge(Integer userAge) {
        this.userAge = userAge;
        return this;
    }

    public void setUserAge(Integer userAge) {
        this.userAge = userAge;
    }

    public SexGender getUserSex() {
        return userSex;
    }

    public UserExt userSex(SexGender userSex) {
        this.userSex = userSex;
        return this;
    }

    public void setUserSex(SexGender userSex) {
        this.userSex = userSex;
    }

    public byte[] getUserImage() {
        return userImage;
    }

    public UserExt userImage(byte[] userImage) {
        this.userImage = userImage;
        return this;
    }

    public void setUserImage(byte[] userImage) {
        this.userImage = userImage;
    }

    public String getUserImageContentType() {
        return userImageContentType;
    }

    public UserExt userImageContentType(String userImageContentType) {
        this.userImageContentType = userImageContentType;
        return this;
    }

    public void setUserImageContentType(String userImageContentType) {
        this.userImageContentType = userImageContentType;
    }

    public User getUser() {
        return user;
    }

    public UserExt user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserExt userExt = (UserExt) o;
        if (userExt.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, userExt.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UserExt{" +
            "id=" + id +
            ", userAge='" + userAge + "'" +
            ", userSex='" + userSex + "'" +
            ", userImage='" + userImage + "'" +
            ", userImageContentType='" + userImageContentType + "'" +
            '}';
    }
}
