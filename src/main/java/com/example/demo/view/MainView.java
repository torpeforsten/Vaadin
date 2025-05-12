package com.example.demo.view;

import com.example.demo.entity.Person;
import com.example.demo.repository.PersonRepository;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
public class MainView extends VerticalLayout {

    private final PersonRepository personRepository;
    private final Grid<Person> grid = new Grid<>(Person.class);

    @Autowired
    public MainView(PersonRepository personRepository) {
        this.personRepository = personRepository;

        Button addButton = new Button("Lisää henkilö", e -> addPerson());
        grid.setItems(personRepository.findAll());
        grid.setColumns("id", "name");

        add(new Text("Henkilölista"), addButton, grid);
    }

    private void addPerson() {
        Person person = new Person();
        person.setName("Testihenkilö");
        personRepository.save(person);
        grid.setItems(personRepository.findAll());
    }
}
