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

import edu.umcs.bookystore.dtos.PublisherDto;
import edu.umcs.bookystore.entities.PublisherEntity;
import edu.umcs.bookystore.repositories.PublisherRepository;

@Controller
@RequestMapping("/library/manage/publishers")
public class ManagePublishersController {

	private static final String TEMPLATES_DIRECTORY = "library/manage";
	private static final Logger logger = LoggerFactory.getLogger(ManagePublishersController.class);

	private final PublisherRepository publisherRepository;

	public ManagePublishersController(PublisherRepository publisherRepository) {
		this.publisherRepository = publisherRepository;
	}

	private String template(String name) {
		return String.format("%s/%s", TEMPLATES_DIRECTORY, name);
	}

	@GetMapping
	public String getManagePublishers(Model model, @RequestParam(required = false) Long delete) {
		logger.debug("GET manage publishers endpoint called");
		PublisherDto newPublisher = new PublisherDto();
		model.addAttribute("newPublisher", newPublisher);
		if (delete != null) {
			try {
				PublisherEntity publisher = this.publisherRepository.findById(delete).get();
				this.publisherRepository.delete(publisher);
				String successMessage = String.format("Publisher %s deleted", publisher.getName());
				model.addAttribute("successMessage", successMessage);
			} catch (NoSuchElementException e) {
				String errorMessage = "Error deleting publisher, given publisher does not exist";
				model.addAttribute("errorMessage", errorMessage);
			} catch (DataIntegrityViolationException e) {
				String errorMessage = "Cannot delete given publisher, it is used by some books";
				model.addAttribute("errorMessage", errorMessage);
			} catch (Exception e) {
				logger.error("Error deleting publisher", e);
				String errorMessage = String.format("Error while deleting a publisher: %s", e.getMessage());
				model.addAttribute("errorMessage", errorMessage);
			}
		}
		model.addAttribute("publishers", this.publisherRepository.findAll());
		return template("publishers");
	}

	@PostMapping
	public String postManagePublishers(Model model, @ModelAttribute PublisherDto newPublisher) {
		logger.debug("POST manage publishers endpoint called");
		try {
			PublisherEntity publisherEntity = new PublisherEntity(
					newPublisher.getName());
			publisherEntity = this.publisherRepository.save(publisherEntity);
			String successMessage = String.format("Publisher \"%s\" created", publisherEntity.getName());
			model.addAttribute("successMessage", successMessage);
			newPublisher = new PublisherDto();
		} catch (Exception e) {
			logger.error("Error creating publisher", e);
			String errorMessage = String.format("Error creating author: %s", e.getMessage());
			model.addAttribute("errorMessage", errorMessage);
		}
		model.addAttribute("newPublisher", newPublisher);
		model.addAttribute("publishers", this.publisherRepository.findAll());
		return template("publishers");
	}

	@GetMapping("/{id}/details")
	public String getPublisherDetails(Model model, @PathVariable long id) {
		logger.debug("GET publisher details endpoint called");
		model.addAttribute("id", id);
		PublisherEntity publisherEntity = this.publisherRepository.findById(id).orElse(null);
		PublisherDto publisher = null;
		if (publisherEntity != null) {
			model.addAttribute("books", publisherEntity.getBooks());
			publisher = new PublisherDto(publisherEntity.getName());
		}
		model.addAttribute("publisher", publisher);
		return template("publisher-details");
	}

	@PostMapping("/{id}/details")
	public String postPublisherDetails(Model model, @PathVariable long id, @ModelAttribute PublisherDto publisher) {
		logger.debug("POST publisher details endpoint called");
		model.addAttribute("id", id);
		try {
			PublisherEntity publisherEntity = this.publisherRepository.findById(id).get();
			publisherEntity.setName(publisher.getName());
			publisherEntity = this.publisherRepository.save(publisherEntity);
			model.addAttribute("books", publisherEntity.getBooks());
			publisher.setName(publisherEntity.getName());
			String successMessage = "Publisher successfully updated";
			model.addAttribute("successMessage", successMessage);
		} catch (NoSuchElementException e) {
			publisher = null;
		} catch (IllegalArgumentException e) {
			String errorMessage = String.format("Error updating publisher: %s", e.getMessage());
			model.addAttribute("errorMessage", errorMessage);
		} catch (Exception e) {
			logger.error("Error updating publisher", e);
			String errorMessage = String.format("Error updating publisher: %s", e.getMessage());
			model.addAttribute("errorMessage", errorMessage);
		}
		model.addAttribute("publisher", publisher);
		return template("publisher-details");
	}

}
