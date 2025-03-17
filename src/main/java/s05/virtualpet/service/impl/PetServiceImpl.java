package s05.virtualpet.service.impl;

import org.springframework.stereotype.Service;
import s05.virtualpet.enums.Luck;
import s05.virtualpet.enums.PetType;
import s05.virtualpet.exception.custom.InvalidPetActionException;
import s05.virtualpet.exception.custom.PetAlreadyBankruptException;
import s05.virtualpet.exception.custom.PetNotFoundException;
import s05.virtualpet.exception.custom.PetOutOfChipsException;
import s05.virtualpet.model.Pet;
import s05.virtualpet.model.User;
import s05.virtualpet.repository.PetRepository;
import s05.virtualpet.service.PetService;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final Random random = new Random();

    public PetServiceImpl(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    public Pet createPet(String name, String type, User owner) {
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
    public List<Pet> getUserPets(User owner) {
        return petRepository.findByOwner(owner);
    }

    @Override
    public Optional<Pet> getPetById(Long id) {
        return petRepository.findById(id).or(() -> {
            throw new PetNotFoundException("Pet with ID " + id + " not found");
        });
    }

    @Override
    public void deletePet(Long id) {
        if (!petRepository.existsById(id)) {
            throw new PetNotFoundException("Pet with ID " + id + " does not exist");
        }
        petRepository.deleteById(id);
    }

    @Override
    public Pet handleAction(Long petId, String action) {
        return petRepository.findById(petId).map(pet -> {
            if (pet.getChips() == 0) {
                throw new PetOutOfChipsException("Pet " + pet.getName() + " has no chips left");
            }
            if (pet.getLuck() == Luck.BANKRUPT) {
                throw new PetAlreadyBankruptException("Pet " + pet.getName() + " is already bankrupt");
            }

            switch (action) {
                case "placeBet" -> pet.placeBet();
                case "winBig" -> {
                    if (random.nextDouble() < 0.3) { // 30% chance to win big
                        pet.winBig();
                    } else {
                        pet.placeBet(); // Losing scenario
                    }
                }
                case "goAllIn" -> pet.goAllIn();
                default -> throw new InvalidPetActionException("Invalid action: " + action);
            }
            return petRepository.save(pet);
        }).orElseThrow(() -> new PetNotFoundException("Pet with ID " + petId + " not found"));
    }
}
