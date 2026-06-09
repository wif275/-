package diplom.com.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;



@Entity
public class Product {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String text;
    private String tag;
    private Integer price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    private String filename;

    public Product(){

    }
    public Product(String text, String tag, User user, Integer price) {
        this.author = user;
        this.text = text;
        this.tag = tag;
        this.price = price;
    }

    public String getAuthorName(){
        return author !=null ? author.getUsername() : "<none>";
    }

        public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author=author;
    }


    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id=id;
    }
    
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

}
