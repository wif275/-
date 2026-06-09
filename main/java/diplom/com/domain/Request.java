package diplom.com.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Request {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Заполните это поле")
    @Size (max = 4096, message = "Слишком длинное")
    private String textR;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    private boolean finished;

    public Request() {
    }

    public Request(User author, String textR) {
        this.author = author;
        this.textR = textR;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTextR() {
        return textR;
    }

    public void setTextR(String textR) {
        this.textR = textR;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String getNameR() {
        return author != null ? author.getUsername() : "—";
    }

    public String getNumberR() {
        return author != null ? author.getNumber() : "—";
    }
}
