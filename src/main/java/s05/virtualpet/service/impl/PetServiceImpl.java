package s05.virtualpet.service.impl;

import org.springframework.stereotype.Service;
import s05.virtualpet.dto.PetDTO;
import s05.virtualpet.enums.Luck;
import s05.virtualpet.enums.PetAction;
import s05.virtualpet.enums.PetType;
import s05.virtualpet.enums.UserRole;
import s05.virtualpet.exception.custom.*;
import s05.virtualpet.model.Pet;
import s05.virtualpet.model.User;
import s05.virtualpet.repository.PetRepository;
import s05.virtualpet.repository.UserRepository;
import s05.virtualpet.service.PetActionService;
import s05.virtualpet.service.PetService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final PetActionService petActionService;

    public PetServiceImpl(PetRepository petRepository,
                          UserRepository userRepository,
                          PetActionService petActionService) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
        this.petActionService = petActionService;
    }

    @Override
    public PetDTO createPet(String name, String type, String username) {
        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new PetNotFoundException("User not found"));

        Pet pet = Pet.builder()
                .name(name)
                .type(PetType.valueOf(type.toUpperCase()))
                .luck(Luck.HAPPY)
                .chips(100)
                .owner(owner)
                .build();
        pet = petRepository.save(pet);

        return toDTO(pet);
    }

    @Override
    public List<PetDTO> getUserPets(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new PetNotFoundException("User not found"));

        if (user.getRole() == UserRole.ROLE_ADMIN) {
            return petRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
        }

        return petRepository.findByOwner(user).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public PetDTO getPetForUser(Long petId, String username) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetNotFoundException("Pet not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new PetNotFoundException("User not found"));

        ensureUserCanAccessPet(user, pet);

        return toDTO(pet);
    }

    @Override
    public PetDTO handleActionForUser(Long petId, PetAction action, String username) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetNotFoundException("Pet not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new PetNotFoundException("User not found"));

        ensureUserCanAccessPet(user, pet);

        if (pet.getChips() == 0) {
            throw new PetOutOfChipsException("Pet " + pet.getName() + " has no chips left");
        }

        if (pet.getLuck() == Luck.BANKRUPT) {
            throw new PetAlreadyBankruptException("Pet " + pet.getName() + " is already bankrupt");
        }

        petActionService.applyAction(pet, action);

        pet = petRepository.save(pet);
        return toDTO(pet);
    }

    @Override
    public void deletePetForUser(Long petId, String username) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetNotFoundException("Pet not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new PetNotFoundException("User not found"));

        ensureUserCanAccessPet(user, pet);

        petRepository.delete(pet);
    }

    private PetDTO toDTO(Pet pet) {
        return new PetDTO(pet.getId(), pet.getName(), pet.getType(), pet.getLuck(), pet.getChips());
    }

    private void ensureUserCanAccessPet(User user, Pet pet) {
        if (!pet.getOwner().getUsername().equals(user.getUsername()) && user.getRole() != UserRole.ROLE_ADMIN) {
            throw new UnauthorizedPetAccessException("You do not own this pet!");
        }
    }
}
