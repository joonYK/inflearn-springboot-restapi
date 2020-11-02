package jy.learning.bootrestapi.events;

import jy.learning.bootrestapi.accounts.Account;
import jy.learning.bootrestapi.accounts.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class EventController {

    private final EventRepository eventRepository;

    private final ModelMapper modelMapper;

    private final EventValidator eventValidator;

    @PostMapping
    public ResponseEntity createEvent(
            @RequestBody @Valid EventDto eventDto,
            Errors errors,
            @CurrentUser Account account
    ) {
        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        eventValidator.validate(eventDto, errors);
        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        Event event = modelMapper.map(eventDto, Event.class);
        event.update();
        event.setManager(account);
        Event newEvent = this.eventRepository.save(event);

        WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
        URI createdUri = selfLinkBuilder.toUri();
        EntityModel<Event> entityModel = FactoryEntityModel.eventEntityModel(event);
        entityModel.add(linkTo(EventController.class).withRel("query-events"));
        entityModel.add(selfLinkBuilder.withRel("update-event"));

        return ResponseEntity.created(createdUri).body(entityModel);
    }

    @GetMapping
    public ResponseEntity queryEvents(
            Pageable pageable,
            PagedResourcesAssembler<Event> assembler,
            @CurrentUser Account account
    ) {
        Page<Event> page = this.eventRepository.findAll(pageable);
        PagedModel<EntityModel<Event>> pagedModel = assembler.toModel(page, FactoryEntityModel::eventEntityModel);
        pagedModel.add(Link.of("/docs/index.html#resources-events-list").withRel("profile"));
        if (account != null) {
            pagedModel.add(linkTo(EventController.class).withRel("create-event"));
        }
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity getEvent(@PathVariable Integer id,
                                   @CurrentUser Account currentUser) {
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if (!optionalEvent.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Event event = optionalEvent.get();
        EntityModel<Event> entityModel = FactoryEntityModel.eventEntityModel(event);
        entityModel.add(Link.of("/docs/index.html#resources-events-get").withRel("profile"));
        if(event.getManager().equals(currentUser)) {
            entityModel.add(linkTo(EventController.class).slash(event.getId()).withRel("update-event"));
        }

        return ResponseEntity.ok(entityModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateEvent(
            @PathVariable Integer id,
            @RequestBody @Valid EventDto eventDto,
            Errors errors,
            @CurrentUser Account currentUser
    ) {
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if (!optionalEvent.isPresent())
            return ResponseEntity.notFound().build();
        else if (errors.hasErrors())
            return ResponseEntity.badRequest().build();

        this.eventValidator.validate(eventDto, errors);
        if (errors.hasErrors())
            return ResponseEntity.badRequest().build();

        Event existingEvent = optionalEvent.get();
        if(!existingEvent.getManager().equals(currentUser))
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);

        this.modelMapper.map(eventDto, existingEvent);
        Event event = this.eventRepository.save(existingEvent);

        EntityModel<Event> entityModel = FactoryEntityModel.eventEntityModel(event);
        entityModel.add(Link.of("/docs/index.html#resources-events-update").withRel("profile"));


        return ResponseEntity.ok(entityModel);
    }
}
