package es.adasoft.tasks.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;

    @Column(name = "completed")
    private Boolean completed;


    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    public Task(Long id, String task) {
       this.id=id;
       this.name=task;
       this.completed=false;
       this.startDate=new Date();
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) &&
                Objects.equals(name, task.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }


}

