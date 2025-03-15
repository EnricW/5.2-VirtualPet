package s05.virtualpet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import s05.virtualpet.dto.PetCreateDTO;
import s05.virtualpet.dto.PetDTO;
import s05.virtualpet.model.Pet;
import s05.virtualpet.model.User;
import s05.virtualpet.service.PetService;
import s05.virtualpet.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pets")
public class PetController {

    private final PetService petService;
    private final UserService userService;

    public PetController(PetService petService, UserService userService) {
        this.petService = petService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public PetDTO createPet(@AuthenticationPrincipal UserDetails userDetails, @RequestBody PetCreateDTO request) {
        User owner = userService.findByUsername(userDetails.getUsername());
        Pet pet = petService.createPet(request.name(), request.type().toString(), owner);
        return new PetDTO(pet.getId(), pet.getName(), pet.getType(), pet.getLuck(), pet.getChips());
    }

    @GetMapping("/{id}")
    public PetDTO getPetById(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        User owner = userService.findByUsername(userDetails.getUsername());
        Pet pet = petService.getPetById(id).orElseThrow(() -> new RuntimeException("Pet not found"));

        if (!pet.getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("You do not own this pet!");
        }

        return new PetDTO(pet.getId(), pet.getName(), pet.getType(), pet.getLuck(), pet.getChips());
    }

    @GetMapping
    public List<PetDTO> getUserPets(@AuthenticationPrincipal UserDetails userDetails) {
        User owner = userService.findByUsername(userDetails.getUsername());
        return petService.getUserPets(owner).stream()
                .map(pet -> new PetDTO(pet.getId(), pet.getName(), pet.getType(), pet.getLuck(), pet.getChips()))
                .collect(Collectors.toList());
    }

    @PostMapping("/{id}/action")
    public PetDTO performAction(@AuthenticationPrincipal UserDetails userDetails,
                                @PathVariable Long id,
                                @RequestParam String action) {
        User owner = userService.findByUsername(userDetails.getUsername());
        Pet pet = petService.getPetById(id).orElseThrow(() -> new RuntimeException("Pet not found"));

        if (!pet.getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("You do not own this pet!");
        }

        pet = petService.handleAction(id, action);
        return new PetDTO(pet.getId(), pet.getName(), pet.getType(), pet.getLuck(), pet.getChips());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePet(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        User owner = userService.findByUsername(userDetails.getUsername());
        Pet pet = petService.getPetById(id).orElseThrow(() -> new RuntimeException("Pet not found"));

        if (!pet.getOwner().getId().equals(owner.getId())) {
            return ResponseEntity.status(403).body("You do not own this pet!");
        }

        petService.deletePet(id);
        return ResponseEntity.ok("Pet deleted successfully!");
    }
}
