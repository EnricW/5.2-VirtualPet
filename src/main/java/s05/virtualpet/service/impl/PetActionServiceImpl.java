package s05.virtualpet.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import s05.virtualpet.enums.Luck;
import s05.virtualpet.enums.PetAction;
import s05.virtualpet.exception.custom.InvalidPetActionException;
import s05.virtualpet.model.Pet;
import s05.virtualpet.service.PetActionService;

import java.util.Random;

@Slf4j
@Service
public class PetActionServiceImpl implements PetActionService {

    private static final int FEED_BOOST = 10;
    private static final int PLAY_COST = 15;
    private static final int INTERACT_BOOST = 5;
    private static final int MAX_CHIPS = 100;

    private final Random random = new Random();

    @Override
    public void applyAction(Pet pet, PetAction action) {
        if (pet.getLuck() == Luck.BANKRUPT) {
            log.warn("Pet '{}' is BANKRUPT, skipping action '{}'", pet.getName(), action);
            return;
        }

        log.info("Applying action '{}' to pet '{}'", action, pet.getName());

        switch (action) {
            case FEED -> feed(pet);
            case PLAY -> play(pet);
            case INTERACT -> interact(pet);
            default -> throw new InvalidPetActionException("Unknown action: " + action);
        }

        updateLuck(pet);
    }

    private void feed(Pet pet) {
        int before = pet.getChips();
        pet.setChips(Math.min(before + FEED_BOOST, MAX_CHIPS));
        log.debug("Fed pet '{}': chips {} -> {}", pet.getName(), before, pet.getChips());
    }

    private void play(Pet pet) {
        int before = pet.getChips();
        pet.setChips(Math.max(before - PLAY_COST, 0));
        log.debug("Played with pet '{}': chips {} -> {}", pet.getName(), before, pet.getChips());
    }

    private void interact(Pet pet) {
        int before = pet.getChips();
        pet.setChips(Math.min(before + INTERACT_BOOST, MAX_CHIPS));
        log.debug("Interacted with pet '{}': chips {} -> {}", pet.getName(), before, pet.getChips());

        if (random.nextDouble() < 0.3) {
            switch (pet.getLuck()) {
                case UNHAPPY -> pet.setLuck(Luck.OKAY);
                case OKAY -> pet.setLuck(Luck.HAPPY);
                default -> {}
            }
        }
    }

    private void updateLuck(Pet pet) {
        int chips = pet.getChips();

        if (chips == 0) {
            pet.setLuck(Luck.BANKRUPT);
        } else if (chips < 30) {
            pet.setLuck(Luck.UNHAPPY);
        } else if (chips < 70) {
            pet.setLuck(Luck.OKAY);
        } else {
            pet.setLuck(Luck.HAPPY);
        }
    }
}
