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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Virtual Pets", description = "Endpoints for managing virtual pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get pet by ID",
            description = "Retrieves a specific pet owned by the logged-in user or all pets if admin"
    )
    @ApiResponse(responseCode = "200", description = "Pet retrieved successfully")
    public PetDTO getPetById(@AuthenticationPrincipal UserDetails userDetails,
                             @PathVariable Long id) {
        return petService.getPetForUser(id, userDetails.getUsername());
    }

    @GetMapping
    @Operation(
            summary = "Get all user pets",
            description = "Returns all pets associated with the currently authenticated user"
    )
    @ApiResponse(responseCode = "200", description = "List of pets returned successfully")
    public List<PetDTO> getUserPets(@AuthenticationPrincipal UserDetails userDetails) {
        return petService.getUserPets(userDetails.getUsername());
    }

    @PostMapping
    @Operation(
            summary = "Create a new pet",
            description = "Creates a new virtual pet with a name and type for the authenticated user"
    )
    @ApiResponse(responseCode = "200", description = "Pet created successfully")
    public PetDTO createPet(@AuthenticationPrincipal UserDetails userDetails,
                            @Valid @RequestBody PetCreateDTO request) {
        return petService.createPet(request.name(), String.valueOf(request.type()), userDetails.getUsername());
    }

    @PostMapping("/{id}/action")
    @Operation(
            summary = "Perform action on a pet",
            description = "Performs an action (FEED, PLAY, INTERACT) on a specific pet"
    )
    @Parameters({
            @Parameter(name = "id", description = "ID of the pet", required = true),
            @Parameter(name = "action", description = "Action to perform (FEED, PLAY, INTERACT)", required = true)
    })
    @ApiResponse(responseCode = "200", description = "Action performed and pet updated")
    public PetDTO performAction(@AuthenticationPrincipal UserDetails userDetails,
                                @PathVariable Long id,
                                @RequestParam PetAction action) {
        return petService.handleActionForUser(id, action, userDetails.getUsername());
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a pet",
            description = "Deletes a specific virtual pet owned by the authenticated user"
    )
    @ApiResponse(responseCode = "200", description = "Pet deleted successfully")
    public ResponseEntity<?> deletePet(@AuthenticationPrincipal UserDetails userDetails,
                                       @PathVariable Long id) {
        petService.deletePetForUser(id, userDetails.getUsername());
        return ResponseEntity.ok("Pet deleted successfully!");
    }
}