package s05.virtualpet.service;

import s05.virtualpet.enums.PetAction;
import s05.virtualpet.model.Pet;

public interface PetActionService {
    void applyAction(Pet pet, PetAction action);
}
