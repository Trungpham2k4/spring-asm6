package fa.training.asm6.repository;

import fa.training.asm6.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Integer> {

    Instructor findByUsername(String name);
}
