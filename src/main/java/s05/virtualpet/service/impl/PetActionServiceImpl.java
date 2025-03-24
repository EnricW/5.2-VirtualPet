package s05.virtualpet.service.impl;

import org.springframework.stereotype.Service;
import s05.virtualpet.enums.Luck;
import s05.virtualpet.enums.PetAction;
import s05.virtualpet.exception.custom.InvalidPetActionException;
import s05.virtualpet.model.Pet;
import s05.virtualpet.service.PetActionService;

import java.util.Random;

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
            // No actions allowed when pet is bankrupt
            return;
        }

        switch (action) {
            case FEED -> feed(pet);
            case PLAY -> play(pet);
            case INTERACT -> interact(pet);
            default -> throw new InvalidPetActionException("Unknown action: " + action);
        }

        updateLuck(pet);
    }

    private void feed(Pet pet) {
        pet.setChips(Math.min(pet.getChips() + FEED_BOOST, MAX_CHIPS));
    }

    private void play(Pet pet) {
        pet.setChips(Math.max(pet.getChips() - PLAY_COST, 0));
    }

    private void interact(Pet pet) {
        // +5 chips
        pet.setChips(Math.min(pet.getChips() + INTERACT_BOOST, MAX_CHIPS));

        // 30% chance to improve luck by one tier
        if (random.nextDouble() < 0.3) {
            switch (pet.getLuck()) {
                case UNHAPPY -> pet.setLuck(Luck.OKAY);
                case OKAY -> pet.setLuck(Luck.HAPPY);
                default -> {} // already HAPPY or BANKRUPT
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
