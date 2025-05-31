package com.example.demo.view;

import com.example.demo.entity.Person;
import com.example.demo.repository.PersonRepository;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Route("")
@PermitAll
public class MainView extends VerticalLayout {

    private final PersonRepository personRepository;
    private final Grid<Person> grid = new Grid<>(Person.class);

    private final TextField nameField = new TextField("Nimi");
    private final TextField filterField = new TextField("Suodata nimi");

    @Autowired
    public MainView(PersonRepository personRepository) {
        this.personRepository = personRepository;

        Button addButton = new Button("Lisää henkilö", e -> addPerson());

        grid.setColumns("id", "name");

        filterField.setPlaceholder("Syötä nimi...");
        filterField.setClearButtonVisible(true);
        filterField.addValueChangeListener(e -> updateGrid());

        HorizontalLayout form = new HorizontalLayout(nameField, addButton);

        add(
                new Text("Henkilölista"),
                form,
                filterField,
                grid
        );

        updateGrid();
    }

    private void addPerson() {
        String name = nameField.getValue().trim();
        if (!name.isEmpty()) {
            Person person = new Person(name);
            personRepository.save(person);
            nameField.clear();
            updateGrid();
        }
    }

    private void updateGrid() {
        String filter = filterField.getValue() != null ? filterField.getValue().trim().toLowerCase() : "";
        List<Person> people = personRepository.findAll();

        if (!filter.isEmpty()) {
            people = people.stream()
                    .filter(p -> p.getName().toLowerCase().contains(filter))
                    .collect(Collectors.toList());
        }

        grid.setItems(people);
    }

    @PostConstruct
    public void initTestData() {
        if (personRepository.count() == 0) {
            personRepository.save(new Person("Maija Meikäläinen"));
            personRepository.save(new Person("Antti Admin"));
            personRepository.save(new Person("Leena Lehtinen"));
            personRepository.save(new Person("Pekka Peloton"));
            personRepository.save(new Person("Laura Lintu"));
        }
        updateGrid();
    }
}
