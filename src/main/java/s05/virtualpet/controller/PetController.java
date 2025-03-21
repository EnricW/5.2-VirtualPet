package s05.virtualpet.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import s05.virtualpet.dto.PetCreateDTO;
import s05.virtualpet.dto.PetDTO;
import s05.virtualpet.enums.PetAction;
import s05.virtualpet.model.Pet;
import s05.virtualpet.service.PetService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping()
    public PetDTO createPet(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody PetCreateDTO request) {
        Pet pet = petService.createPet(request.name(), String.valueOf(request.type()), userDetails.getUsername());
        return new PetDTO(pet.getId(), pet.getName(), pet.getType(), pet.getLuck(), pet.getChips());
    }

    @GetMapping("/{id}")
    public PetDTO getPetById(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        Pet pet = petService.getPetForUser(id, userDetails.getUsername());
        return new PetDTO(pet.getId(), pet.getName(), pet.getType(), pet.getLuck(), pet.getChips());
    }

    @GetMapping
    public List<PetDTO> getUserPets(@AuthenticationPrincipal UserDetails userDetails) {
        return petService.getUserPets(userDetails.getUsername()).stream()
                .map(pet -> new PetDTO(pet.getId(), pet.getName(), pet.getType(), pet.getLuck(), pet.getChips()))
                .collect(Collectors.toList());
    }

    @PostMapping("/{id}/action")
    public PetDTO performAction(@AuthenticationPrincipal UserDetails userDetails,
                                @PathVariable Long id,
                                @RequestParam PetAction action) {
        Pet pet = petService.handleActionForUser(id, action, userDetails.getUsername());
        return new PetDTO(pet.getId(), pet.getName(), pet.getType(), pet.getLuck(), pet.getChips());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePet(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        petService.deletePetForUser(id, userDetails.getUsername());
        return ResponseEntity.ok("Pet deleted successfully!");
    }
}
