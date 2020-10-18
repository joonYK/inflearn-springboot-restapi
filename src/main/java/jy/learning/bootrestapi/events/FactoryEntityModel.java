package jy.learning.bootrestapi.events;

import org.springframework.hateoas.EntityModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class FactoryEntityModel {

    public static EntityModel<Event> eventEntityModel(Event event) {
        EntityModel<Event> entityModel = EntityModel.of(event);
        entityModel.add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
        return entityModel;
    }
}
