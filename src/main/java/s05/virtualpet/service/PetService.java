package s05.virtualpet.service;

import s05.virtualpet.model.Pet;
import s05.virtualpet.model.User;

import java.util.List;
import java.util.Optional;

public interface PetService {

    Pet createPet(String name, String type, User owner);

    List<Pet> getUserPets(User owner);

    Optional<Pet> getPetById(Long id);

    void deletePet(Long id);

    Pet handleAction(Long petId, String action);
}
