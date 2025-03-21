package s05.virtualpet.service.impl;

import org.springframework.stereotype.Service;
import s05.virtualpet.enums.Luck;
import s05.virtualpet.enums.PetAction;
import s05.virtualpet.enums.PetType;
import s05.virtualpet.exception.custom.*;
import s05.virtualpet.model.Pet;
import s05.virtualpet.model.User;
import s05.virtualpet.repository.PetRepository;
import s05.virtualpet.repository.UserRepository;
import s05.virtualpet.service.PetService;

import java.util.List;
import java.util.Random;

@Service
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final Random random = new Random();

    public PetServiceImpl(PetRepository petRepository, UserRepository userRepository) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Pet createPet(String name, String type, String username) {
        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new PetNotFoundException("User not found"));

        Pet pet = Pet.builder()
                .name(name)
                .type(PetType.valueOf(type.toUpperCase()))
                .luck(Luck.LUCKY)
                .chips(100)
                .owner(owner)
                .build();
        return petRepository.save(pet);
    }

    @Override
    public List<Pet> getUserPets(String username) {
        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new PetNotFoundException("User not found"));
        return petRepository.findByOwner(owner);
    }

    @Override
    public Pet getPetForUser(Long petId, String username) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetNotFoundException("Pet not found"));

        if (!pet.getOwner().getUsername().equals(username)) {
            throw new UnauthorizedPetAccessException("You do not own this pet!");
        }

        return pet;
    }

    @Override
    public Pet handleActionForUser(Long petId, PetAction action, String username) {
        Pet pet = getPetForUser(petId, username); // Ownership validation is already handled here

        if (pet.getChips() == 0) {
            throw new PetOutOfChipsException("Pet " + pet.getName() + " has no chips left");
        }
        if (pet.getLuck() == Luck.BANKRUPT) {
            throw new PetAlreadyBankruptException("Pet " + pet.getName() + " is already bankrupt");
        }

        switch (action) {
            case PLACE_BET  -> pet.placeBet();
            case WIN_BIG -> {
                if (random.nextDouble() < 0.3) { // 30% chance to win big
                    pet.winBig();
                } else {
                    pet.placeBet(); // Losing scenario
                }
            }
            case GO_ALL_IN -> pet.goAllIn();
            default -> throw new InvalidPetActionException("Invalid action: " + action);
        }
        return petRepository.save(pet);
    }

    @Override
    public void deletePetForUser(Long petId, String username) {
        Pet pet = getPetForUser(petId, username); // Ownership check
        petRepository.delete(pet);
    }
}
