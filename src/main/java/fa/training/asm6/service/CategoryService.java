package fa.training.asm6.service;

import fa.training.asm6.entity.Category;
import fa.training.asm6.repository.CategoryRepository;
import fa.training.asm6.service.base.GenericServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class CategoryService extends GenericServiceImpl<Category, Integer> {

    public CategoryService(CategoryRepository repository) {
        super(repository);
    }
}
