package com.ssafy.petandmet.dto.animal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.petandmet.domain.Animal;
import lombok.Data;

@Data
public class FindAllAnimalResponse {

    @JsonProperty("animal_uuid")
    private String animalUuid;
    private String name;
    private int age;
    private String specie;
    private String breed;
    @JsonProperty("animal_photo_url")
    private String photoUrl;
    private Gender gender;
    private CharacterType character;

    public FindAllAnimalResponse(Animal animal) {
        this.animalUuid = animal.getUuid();
        this.name = animal.getName();
        this.age = animal.getAge();
        this.specie = animal.getSpecie();
        this.breed = animal.getBreed();
        this.photoUrl = animal.getPhotoUrl();
        this.gender = animal.getGender();
        this.character = animal.getCharacterType();
    }
}
