package s05.virtualpet.service;

import s05.virtualpet.dto.PetDTO;
import s05.virtualpet.enums.PetAction;

import java.util.List;

public interface PetService {
    PetDTO createPet(String name, String type, String username);
    List<PetDTO> getUserPets(String username);
    PetDTO getPetForUser(Long petId, String username);
    PetDTO handleActionForUser(Long petId, PetAction action, String username);
    void deletePetForUser(Long petId, String username);
}