package s05.virtualpet.service.impl;

import org.springframework.stereotype.Service;
import s05.virtualpet.dto.PetDTO;
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
import java.util.stream.Collectors;

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
    public PetDTO createPet(String name, String type, String username) {
        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new PetNotFoundException("User not found"));

        Pet pet = Pet.builder()
                .name(name)
                .type(PetType.valueOf(type.toUpperCase()))
                .luck(Luck.LUCKY)
                .chips(100)
                .owner(owner)
                .build();
        pet = petRepository.save(pet);

        return toDTO(pet);
    }

    @Override
    public List<PetDTO> getUserPets(String username) {
        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new PetNotFoundException("User not found"));

        return petRepository.findByOwner(owner).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PetDTO getPetForUser(Long petId, String username) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetNotFoundException("Pet not found"));

        if (!pet.getOwner().getUsername().equals(username)) {
            throw new UnauthorizedPetAccessException("You do not own this pet!");
        }

        return toDTO(pet);
    }

    @Override
    public PetDTO handleActionForUser(Long petId, PetAction action, String username) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetNotFoundException("Pet not found"));

        if (!pet.getOwner().getUsername().equals(username)) {
            throw new UnauthorizedPetAccessException("You do not own this pet!");
        }

        if (pet.getChips() == 0) {
            throw new PetOutOfChipsException("Pet " + pet.getName() + " has no chips left");
        }

        if (pet.getLuck() == Luck.BANKRUPT) {
            throw new PetAlreadyBankruptException("Pet " + pet.getName() + " is already bankrupt");
        }

        switch (action) {
            case PLACE_BET -> pet.placeBet();
            case WIN_BIG -> {
                if (random.nextDouble() < 0.3) {
                    pet.winBig();
                } else {
                    pet.placeBet();
                }
            }
            case GO_ALL_IN -> pet.goAllIn();
        }

        pet = petRepository.save(pet);
        return toDTO(pet);
    }

    @Override
    public void deletePetForUser(Long petId, String username) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetNotFoundException("Pet not found"));

        if (!pet.getOwner().getUsername().equals(username)) {
            throw new UnauthorizedPetAccessException("You do not own this pet!");
        }

        petRepository.delete(pet);
    }

    private PetDTO toDTO(Pet pet) {
        return new PetDTO(pet.getId(), pet.getName(), pet.getType(), pet.getLuck(), pet.getChips());
    }
}
