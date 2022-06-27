package edu.umcs.bookystore.controllers;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.umcs.bookystore.dtos.CategoryDto;
import edu.umcs.bookystore.entities.CategoryEntity;
import edu.umcs.bookystore.repositories.CategoryRepository;

@Controller
@RequestMapping("/library/manage/categories")
public class ManageCategoriesController {

	private static final String TEMPLATES_DIRECTORY = "library/manage";
	private static final Logger logger = LoggerFactory.getLogger(ManageCategoriesController.class);

	private final CategoryRepository categoryRepository;

	public ManageCategoriesController(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	private String template(String name) {
		return String.format("%s/%s", TEMPLATES_DIRECTORY, name);
	}

	@GetMapping
	public String getManageCategories(Model model) {
		logger.debug("GET manage categories endpoint called");
		CategoryDto newCategory = new CategoryDto();
		model.addAttribute("newCategory", newCategory);
		model.addAttribute("categories", this.categoryRepository.findAll());
		return template("categories");
	}

	@PostMapping(params = "delete")
	public String postManageCategoriesDelete(Model model, @RequestParam(required = true) long delete) {
		logger.debug("POST manage categories (DELETE variant) endpoint called");
		CategoryDto newCategory = new CategoryDto();
		model.addAttribute("newCategory", newCategory);
		try {
			CategoryEntity category = this.categoryRepository.findById(delete).get();
			this.categoryRepository.delete(category);
			String successMessage = String.format("Category %s deleted", category.getName());
			model.addAttribute("successMessage", successMessage);
		} catch (NoSuchElementException e) {
			String errorMessage = "Error deleting category, given category does not exist";
			model.addAttribute("errorMessage", errorMessage);
		} catch (DataIntegrityViolationException e) {
			String errorMessage = "Cannot delete given category, it is used by some books";
			model.addAttribute("errorMessage", errorMessage);
		} catch (Exception e) {
			logger.error("Error deleting category", e);
			String errorMessage = String.format("Error while deleting a category: %s", e.getMessage());
			model.addAttribute("errorMessage", errorMessage);
		}
		model.addAttribute("categories", this.categoryRepository.findAll());
		return template("categories");
	}

	@PostMapping
	public String postManageCategories(Model model, @ModelAttribute CategoryDto newCategory) {
		logger.debug("POST manage categories endpoint called");
		try {
			CategoryEntity categoryEntity = new CategoryEntity(
					newCategory.getName());
			categoryEntity = this.categoryRepository.save(categoryEntity);
			String successMessage = String.format("Category \"%s\" created", categoryEntity.getName());
			model.addAttribute("successMessage", successMessage);
			newCategory = new CategoryDto();
		} catch (Exception e) {
			logger.error("Error creating category", e);
			String errorMessage = String.format("Error creating category: %s", e.getMessage());
			model.addAttribute("errorMessage", errorMessage);
		}
		model.addAttribute("newCategory", newCategory);
		model.addAttribute("categories", this.categoryRepository.findAll());
		return template("categories");
	}

	@GetMapping("/{id}/details")
	public String getCategoryDetails(Model model, @PathVariable long id) {
		logger.debug("GET category details endpoint called");
		model.addAttribute("id", id);
		CategoryEntity categoryEntity = this.categoryRepository.findById(id).orElse(null);
		CategoryDto category = null;
		if (categoryEntity != null) {
			model.addAttribute("books", categoryEntity.getBooks());
			category = new CategoryDto(categoryEntity.getName());
		}
		model.addAttribute("category", category);
		return template("category-details");
	}

	@PostMapping("/{id}/details")
	public String postCategoryDetails(Model model, @PathVariable long id, @ModelAttribute CategoryDto category) {
		logger.debug("POST category details endpoint called");
		model.addAttribute("id", id);
		try {
			CategoryEntity categoryEntity = this.categoryRepository.findById(id).get();
			categoryEntity.setName(category.getName());
			categoryEntity = this.categoryRepository.save(categoryEntity);
			model.addAttribute("books", categoryEntity.getBooks());
			category.setName(categoryEntity.getName());
			String successMessage = "Category successfully updated";
			model.addAttribute("successMessage", successMessage);
		} catch (NoSuchElementException e) {
			category = null;
		} catch (IllegalArgumentException e) {
			String errorMessage = String.format("Error updating category: %s", e.getMessage());
			model.addAttribute("errorMessage", errorMessage);
		} catch (Exception e) {
			logger.error("Error updating category", e);
			String errorMessage = String.format("Error updating category: %s", e.getMessage());
			model.addAttribute("errorMessage", errorMessage);
		}
		model.addAttribute("category", category);
		return template("category-details");
	}

}
