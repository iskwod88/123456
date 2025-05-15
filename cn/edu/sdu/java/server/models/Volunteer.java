package cn.edu.sdu.java.server.models;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.boot.registry.selector.spi.StrategyCreator;

@Getter
@Setter
@Entity
@Table(	name = "volunteer",
        uniqueConstraints = {
        })
public class Volunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer activityId;

    @Size(max = 20)
    private String activity;

    private Integer volunteerHour;

    @OneToOne
    @JoinColumn(name = "personId", insertable = false, updatable = false)
    private Student student;

    @OneToOne
    @JoinColumn(name = "personId", insertable = false, updatable = false)
    private Teacher teacher;

}
