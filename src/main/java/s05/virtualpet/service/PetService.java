package s05.virtualpet.service;

import s05.virtualpet.enums.PetAction;
import s05.virtualpet.model.Pet;

import java.util.List;

public interface PetService {
    Pet createPet(String name, String type, String username);
    List<Pet> getUserPets(String username);
    Pet getPetForUser(Long petId, String username);
    Pet handleActionForUser(Long petId, PetAction action, String username);
    void deletePetForUser(Long petId, String username);
}