package ua.org.javatraining.automessenger.backend.entity;

import javax.persistence.*;

/**
 * Created by fisher on 26.07.15.
 */
@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "tag_name")
    private String tagName;

    public Tag() {
    }

    public Tag(String tagName) {
        this.tagName = tagName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", tagName='" + tagName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        if (!id.equals(tag.id)) return false;
        return !(tagName != null ? !tagName.equals(tag.tagName) : tag.tagName != null);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (tagName != null ? tagName.hashCode() : 0);
        return result;
    }
}
