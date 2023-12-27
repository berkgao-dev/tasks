package es.adasoft.tasks.repository;

import es.adasoft.tasks.domain.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByNameContaining(String partialName);
    List<Task> findByCompletedOrderByEndDateDesc(Boolean completed);
    List<Task> findByCompletedOrderByStartDateAsc(Boolean completed);

}
