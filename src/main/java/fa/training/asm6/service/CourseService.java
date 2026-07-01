package fa.training.asm6.service;

import fa.training.asm6.dto.request.CourseRequest;
import fa.training.asm6.dto.response.CourseWithStatus;
import fa.training.asm6.entity.Category;
import fa.training.asm6.entity.Course;
import fa.training.asm6.repository.CategoryRepository;
import fa.training.asm6.repository.CourseRepository;
import fa.training.asm6.service.base.GenericServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CourseService extends GenericServiceImpl<Course, Integer> {

    private final int LIMIT = 10;
    private final CategoryRepository categoryRepository;

    public CourseService(CourseRepository repository, CategoryRepository categoryRepository) {
        super(repository);
        this.categoryRepository = categoryRepository;
    }

    public Page<CourseWithStatus> findCoursesPagination(Integer page){
        PageRequest pageRequest = PageRequest.of(page - 1, LIMIT);
        return ((CourseRepository) repository).findCourseWithStatus(pageRequest);
    }

    public Page<Course> findRecentPublishedCourses(Integer page) {
        PageRequest pageable = PageRequest.of(page - 1, LIMIT);
        return ((CourseRepository) repository).findRecentPublishedCourses(pageable);
    }

    public Course findCourseWithReviews(Integer id) {
        return ((CourseRepository) repository).findCourseWithReviews(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
    }

    public Page<Course> findCourseByCategoryName(String name, Integer page) {
        PageRequest pageable = PageRequest.of(page - 1, LIMIT);
        return ((CourseRepository) repository).findCourseByCategoryContainingIgnoreCase(name, pageable);
    }

    private Set<String> parseCategories(String categoryStr) {
        if (categoryStr == null || categoryStr.trim().isEmpty()) {
            return Collections.emptySet();
        }
        return Arrays.stream(categoryStr.split(","))
                .map(String::trim)
                .filter(c -> !c.isEmpty()) // Chống lỗi chuỗi rỗng
                .collect(Collectors.toSet());
    }

    @Transactional
    public void saveCourse(CourseRequest courseRequest) {
        Course course = Course.builder()
                .id(courseRequest.getId())
                .title(courseRequest.getTitle())
                .description(courseRequest.getDescription())
                .content(courseRequest.getContent())
                .category(courseRequest.getCategory())
                .status(courseRequest.getStatus())
                .build();
        repository.save(course);
        log.info("Saved new course with title: {}", course.getTitle());

        Set<String> requestCategories = parseCategories(courseRequest.getCategory());
        if (requestCategories.isEmpty()) return;

        // Chỉ lấy những category có liên quan từ DB
        List<Category> existingCategories = categoryRepository.findByNameIn(requestCategories);
        Set<String> existingNames = existingCategories.stream()
                .map(Category::getName)
                .collect(Collectors.toSet());

        // Tăng frequency cho category đã tồn tại
        existingCategories.forEach(c -> c.setFrequency(c.getFrequency() + 1));

        // Tạo mới category chưa tồn tại
        requestCategories.forEach(category -> {
            if (!existingNames.contains(category)) {
                Category newCategory = new Category();
                newCategory.setName(category);
                newCategory.setFrequency(1);
                existingCategories.add(newCategory);
            }
        });
        categoryRepository.saveAll(existingCategories);
        log.info("Saving categories for course '{}': {}", course.getTitle(),
                existingCategories.stream()
                        .map(c -> c.getName() + " (freq: " + c.getFrequency() + ")")
                        .collect(Collectors.joining(", ")));
    }

    @Transactional
    public void updateCourse(CourseRequest courseRequest) {
        Course course = findById(courseRequest.getId());
        course.setTitle(courseRequest.getTitle());
        course.setDescription(courseRequest.getDescription());
        course.setContent(courseRequest.getContent());
        course.setCategory(courseRequest.getCategory());
        course.setStatus(courseRequest.getStatus());
        repository.save(course);
        log.info("Updated course with id {} and title: {}", course.getId(), course.getTitle());

        Set<String> newCategories = parseCategories(courseRequest.getCategory());
        Set<String> oldCategories = parseCategories(courseRequest.getOldCategory());

        // Tìm ra những category thực sự THÊM MỚI và BỊ XÓA (Thuật toán Diff)
        Set<String> categoriesToAdd = new HashSet<>(newCategories);
        categoriesToAdd.removeAll(oldCategories);

        Set<String> categoriesToRemove = new HashSet<>(oldCategories);
        categoriesToRemove.removeAll(newCategories);

        Set<String> allAffectedNames = new HashSet<>();
        allAffectedNames.addAll(categoriesToAdd);
        allAffectedNames.addAll(categoriesToRemove);

        if (allAffectedNames.isEmpty()) return; // Không có thay đổi về category

        List<Category> affectedCategories = categoryRepository.findByNameIn(allAffectedNames);
        List<Category> categoriesToDelete = new ArrayList<>();
        List<Category> categoriesToSave = new ArrayList<>();

        log.info("Updating categories for course '{}'. To add: {}, To remove: {}",
                course.getTitle(),
                String.join(", ", categoriesToAdd),
                String.join(", ", categoriesToRemove));
        affectedCategories.forEach(category -> {
            if (categoriesToAdd.contains(category.getName())) {
                category.setFrequency(category.getFrequency() + 1);
                categoriesToSave.add(category);
            } else if (categoriesToRemove.contains(category.getName())) {
                category.setFrequency(category.getFrequency() - 1);
                if (category.getFrequency() <= 0) {
                    categoriesToDelete.add(category);
                } else {
                    categoriesToSave.add(category);
                }
            }
        });

        // Xử lý những category thêm mới hoàn toàn (chưa có trong DB)
        Set<String> existingAffectedNames = affectedCategories.stream()
                .map(Category::getName).collect(Collectors.toSet());
        categoriesToAdd.forEach(name -> {
            if (!existingAffectedNames.contains(name)) {
                Category newCat = new Category();
                newCat.setName(name);
                newCat.setFrequency(1);
                categoriesToSave.add(newCat);
            }
        });

        // Tách bạch rõ ràng quá trình Delete và Save để tránh lỗi Hibernate
        if (!categoriesToDelete.isEmpty()) {
            log.info("Number of categories to be deleted: {}", categoriesToDelete.size());
            categoryRepository.deleteAll(categoriesToDelete);
        }
        if (!categoriesToSave.isEmpty()) {
            log.info("Number of categories to be saved: {}", categoriesToSave.size());
            categoryRepository.saveAll(categoriesToSave);
        }
    }

    @Transactional
    public void deleteCourse(Integer id) {
        Course course = findById(id);
        Set<String> categoriesToRemove = parseCategories(course.getCategory());

        if (!categoriesToRemove.isEmpty()) {
            List<Category> existingCategories = categoryRepository.findByNameIn(categoriesToRemove);
            List<Category> categoriesToDelete = new ArrayList<>();
            List<Category> categoriesToUpdate = new ArrayList<>();

            existingCategories.forEach(category -> {
                category.setFrequency(category.getFrequency() - 1);
                if (category.getFrequency() <= 0) {
                    categoriesToDelete.add(category);
                } else {
                    categoriesToUpdate.add(category);
                }
            });

            if (!categoriesToDelete.isEmpty()) {
                log.info("Deleting categories: {}", categoriesToDelete.stream().map(Category::getName).collect(Collectors.joining(", ")));
                categoryRepository.deleteAll(categoriesToDelete);
            }
            if (!categoriesToUpdate.isEmpty()) {
                log.info("Updating categories: {}", categoriesToUpdate.stream().map(c -> c.getName() + " (new freq: " + c.getFrequency() + ")").collect(Collectors.joining(", ")));
                categoryRepository.saveAll(categoriesToUpdate);
            }
        }
        log.info("Deleting course with id {} and its associated categories", id);
        repository.delete(course);
    }

    public CourseRequest findUpdateCourse(Integer id){
        Course course = findById(id);
        CourseRequest courseRequest = new CourseRequest();
        courseRequest.setId(course.getId());
        courseRequest.setTitle(course.getTitle());
        courseRequest.setDescription(course.getDescription());
        courseRequest.setContent(course.getContent());
        courseRequest.setOldCategory(course.getCategory());
        courseRequest.setStatus(course.getStatus());
        return courseRequest;
    }

    public void updateStatus(Integer id, Integer status) {
        Course course = findById(id);
        course.setStatus(status);
        repository.save(course);
        log.info("Updated status of course with id {} to {}", id, status);
    }
}
