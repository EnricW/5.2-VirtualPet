package s05.virtualpet.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import s05.virtualpet.dto.PetCreateDTO;
import s05.virtualpet.dto.PetDTO;
import s05.virtualpet.enums.PetAction;
import s05.virtualpet.service.PetService;

import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping("/{id}")
    public PetDTO getPetById(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        return petService.getPetForUser(id, userDetails.getUsername());
    }

    @GetMapping
    public List<PetDTO> getUserPets(@AuthenticationPrincipal UserDetails userDetails) {
        return petService.getUserPets(userDetails.getUsername());
    }

    @PostMapping()
    public PetDTO createPet(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody PetCreateDTO request) {
        return petService.createPet(request.name(), String.valueOf(request.type()), userDetails.getUsername());
    }

    @PostMapping("/{id}/action")
    public PetDTO performAction(@AuthenticationPrincipal UserDetails userDetails,
                                @PathVariable Long id,
                                @RequestParam PetAction action) {
        return petService.handleActionForUser(id, action, userDetails.getUsername());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePet(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        petService.deletePetForUser(id, userDetails.getUsername());
        return ResponseEntity.ok("Pet deleted successfully!");
    }
}
